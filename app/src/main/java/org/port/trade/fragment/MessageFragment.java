package org.port.trade.fragment;
/**
 * Created by 超悟空 on 2015/1/24.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import org.mobile.model.operate.BackHandle;
import org.mobile.model.operate.OnItemClickListenerForRecyclerViewItem;
import org.mobile.model.operate.OnItemLongClickListenerForRecyclerViewItem;
import org.port.trade.R;
import org.port.trade.activity.MessageActivity;
import org.port.trade.adapter.MessageItemDivider;
import org.port.trade.adapter.MessageItemViewHolder;
import org.port.trade.adapter.MessageRecyclerViewAdapter;
import org.port.trade.data.Message;
import org.port.trade.function.LoadMessage;
import org.port.trade.notify.NotifyReceiver;
import org.port.trade.util.MemoryBroadcast;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.StaticValue;
import org.port.trade.util.state.MessageNetworkState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 消息页的Fragment片段
 *
 * @author 超悟空
 * @version 1.0 2015/1/24
 * @since 1.0
 */
public class MessageFragment extends Fragment implements BackHandle {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MessageFragment.";

    /**
     * 消息的数据适配器
     */
    private MessageRecyclerViewAdapter adapter = null;

    /**
     * 重置适配器数据并显示
     */
    private Handler adapterReset = null;

    /**
     * 本地广播管理器
     */
    private LocalBroadcastManager localBroadcastManager = null;

    /**
     * 全局临时变量改变的广播接收者
     */
    private LoadMessageReceiver loadMessageReceiver = null;

    /**
     * 消息删除的悬浮按钮
     */
    private Button deleteButton = null;

    /**
     * 指示当前是否是删除消息布局状态，默认为false表示正常布局
     */
    private boolean isDeleteLayoutState = false;

    /**
     * 选中的要删除的消息索引
     */
    private List<Integer> selectDeleteMessage = new ArrayList<>();

    /**
     * 正常布局状态下读取消息的列表点击事件
     */
    private OnItemClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder> readMessageClick = new OnItemClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder>() {
        @Override
        public void onClick(List<Message> dataSource, MessageItemViewHolder holder) {

            // 跳转到消息内容界面
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            intent.putExtra(StaticValue.MESSAGE_INTENT_TAG, dataSource.get(holder.getPosition()));
            getActivity().startActivity(intent);

            // 取消未读标记
            holder.readMark.setImageResource(R.color.white);
        }
    };

    /**
     * 正常布局状态下消息列表长按事件
     */
    private OnItemLongClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder> readMessageLongClick = new OnItemLongClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder>() {
        @Override
        public boolean onLongClick(List<Message> dataSource, MessageItemViewHolder holder) {
            // 进入到删除模式
            changeDeleteModel();

            // 将当前选中索引加入删除队列
            selectDeleteMessage.add(holder.getPosition());
            dataSource.get(holder.getPosition()).setDeleteState(true);

            // 显示选中状态标记
            holder.deleteMark.setImageResource(R.color.blue);

            // 消费事件
            return true;
        }
    };

    /**
     * 删除消息布局状态下消息列表长按事件
     */
    private OnItemLongClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder> deleteMessageLongClick = new OnItemLongClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder>() {
        @Override
        public boolean onLongClick(List<Message> dataSource, MessageItemViewHolder holder) {

            // 不做任何操作
            return false;
        }
    };

    /**
     * 删除消息布局状态下选择要删除消息的列表点击事件
     */
    private OnItemClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder> deleteMessageClick = new OnItemClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder>() {
        @Override
        public void onClick(List<Message> dataSource, MessageItemViewHolder holder) {
            // 记录要删除的消息
            // 在删除队列中查找该消息索引的位置
            int index = selectDeleteMessage.indexOf(holder.getPosition());
            // 判断是否已加入该索引
            if (index > -1) {
                // 已存在，表示要取消删除，从删除队列中剔除
                selectDeleteMessage.remove(index);
                dataSource.get(holder.getPosition()).setDeleteState(false);

                // 取消选中状态标记
                holder.deleteMark.setImageResource(R.color.white);

            } else {
                // 不存在，将索引加入删除队列
                selectDeleteMessage.add(holder.getPosition());
                dataSource.get(holder.getPosition()).setDeleteState(true);

                // 显示选中状态标记
                holder.deleteMark.setImageResource(R.color.blue);
            }
        }
    };

    /**
     * 删除按钮的显示动画
     */
    private TranslateAnimation buttonShowAnimation = null;

    /**
     * 删除按钮的隐藏动画
     */
    private TranslateAnimation buttonHiddenAnimation = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 开启追加菜单项
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.message_fragment, container, false);

        // 初始化布局
        initView(rootView);
        // 注册接收者
        registerReceivers();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 注销接收者
        unregisterReceivers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.message_delete:
                if (!isDeleteLayoutState) {
                    // 当前是正常模式则切换到删除模式
                    changeDeleteModel();
                } else {
                    // 当前是删除模式则切换到正常模式
                    changeReadModel();
                }
        }
        return true;
    }

    /**
     * 改变界面到删除消息模式
     */
    private void changeDeleteModel() {
        Log.i(LOG_TAG + "changeDeleteModel", "now delete model");
        // 切换状态
        isDeleteLayoutState = true;

        // 设置点击事件
        Log.i(LOG_TAG + "changeDeleteModel", "now delete message model click listener");
        adapter.setOnItemClickListener(deleteMessageClick);
        // 设置长按事件
        Log.i(LOG_TAG + "changeDeleteModel", "now delete message model long click listener");
        adapter.setOnItemLongClickListener(deleteMessageLongClick);

        // 显示删除按钮
        deleteButton.startAnimation(buttonShowAnimation);
        deleteButton.setVisibility(View.VISIBLE);
    }

    /**
     * 改变界面到读取消息模式
     */
    private void changeReadModel() {
        Log.i(LOG_TAG + "changeReadModel", "now read model");
        // 切换状态
        isDeleteLayoutState = false;

        // 设置点击事件
        Log.i(LOG_TAG + "changeReadModel", "now read message model click listener");
        adapter.setOnItemClickListener(readMessageClick);
        // 设置长按事件
        Log.i(LOG_TAG + "changeReadModel", "now read message model long click listener");
        adapter.setOnItemLongClickListener(readMessageLongClick);

        // 隐藏删除按钮
        deleteButton.startAnimation(buttonHiddenAnimation);
        deleteButton.setVisibility(View.GONE);

        // 将已选择的列还原
        for (int position : selectDeleteMessage) {
            adapter.removeDeleteState(position);
        }
        // 清空删除队列
        selectDeleteMessage.clear();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_message, menu);
    }

    /**
     * 初始化布局
     */
    private void initView(View rootView) {
        // 删除按钮
        initDeleteButton(rootView);

        // 加载列表布局
        initMessageList(rootView);
    }

    /**
     * 初始化消息列表布局
     *
     * @param rootView 根布局
     */
    private void initMessageList(View rootView) {
        // 消息RecyclerView列表对象
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.message_recyclerView);

        // 设置分割线
        recyclerView.addItemDecoration(new MessageItemDivider(this.getActivity()));

        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 创建布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        // Item高度固定
        recyclerView.setHasFixedSize(true);

        // 新建数据适配器
        adapter = new MessageRecyclerViewAdapter();

        // 设置点击事件
        Log.i(LOG_TAG + "initView", "now read message model click listener");
        adapter.setOnItemClickListener(readMessageClick);

        // 设置长按事件
        Log.i(LOG_TAG + "initView", "now read message model long click listener");
        adapter.setOnItemLongClickListener(readMessageLongClick);

        // 注册适配器刷新工具
        adapterReset = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                adapter.notifyDataSetChanged();
            }
        };

        // 适配器数据初始化
        initMessageData();


        // 设置数据适配器
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化删除按钮
     *
     * @param rootView 根布局
     */
    private void initDeleteButton(View rootView) {
        // 得到删除按钮
        deleteButton = (Button) rootView.findViewById(R.id.message_fragment_delete);

        // 设置删除事件
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDeleteMessage();
            }
        });

        // 实例化显示动画
        buttonShowAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        buttonShowAnimation.setDuration(500);

        // 实例化隐藏动画
        buttonHiddenAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        buttonHiddenAnimation.setDuration(500);
    }

    /**
     * 删除选中的消息
     */
    private void doDeleteMessage() {
        // 对要删除的消息索引重排序
        Collections.sort(selectDeleteMessage);

        // 要删除的消息
        List<Message> messageList = new ArrayList<>();

        // 逐一删除
        for (int i = 0; i < selectDeleteMessage.size(); i++) {
            Log.i(LOG_TAG + "doDeleteMessage", "delete index is " + selectDeleteMessage.get(i));
            messageList.add(adapter.remove(selectDeleteMessage.get(i) - i));
        }

        // 清空删除队列
        selectDeleteMessage.clear();

        // 改变布局到读取状态
        changeReadModel();

        // 执行删除持久化
        if (MemoryConfig.getConfig().isOpenNetwork()) {
            // 有网络时，向服务器请求
            Log.i(LOG_TAG + "doDeleteMessage", "delete message from service");
            LoadMessage.getLoadMessage().deleteMessage(messageList);
        } else {
            // 无网络时，直接更新到本地数据库
            Log.i(LOG_TAG + "doDeleteMessage", "save delete message to location");
            for (Message message : messageList) {
                LoadMessage.getLoadMessage().updateMessage(message);
            }
        }
    }

    /**
     * 初始化消息数据
     */
    private void initMessageData() {

        // 获取本地加载的消息
        List<Message> messageList = LoadMessage.getLoadMessage().getMessageList();

        // 获取网络加载的消息
        List<Message> newMessageList = null;
        if (MemoryConfig.getConfig().getMessageRequestState() == MessageNetworkState.MESSAGE_LOAD_END) {
            newMessageList = LoadMessage.getLoadMessage().getNewMessageList();
            // 刷新消息加载器
            LoadMessage.getLoadMessage().refreshLocalMessage();
        }

        if (messageList == null) {
            // 本地没有就新建空集
            messageList = new ArrayList<>();
        }
        if (newMessageList != null) {
            // 有网络下载的新消息,追加到头部
            messageList.addAll(0, newMessageList);
        }
        // 给适配器装配数据
        adapter.setMessageList(messageList);
    }

    /**
     * 全局临时变量改变的广播接收者，
     * 用于实时控制本页的消息列表
     *
     * @author 超悟空
     * @version 1.0 2015/1/31
     * @since 1.0
     */
    private class LoadMessageReceiver extends BroadcastReceiver {

        /**
         * 得到本接收者监听的动作集合
         *
         * @return 填充完毕的意图集合
         */
        public final IntentFilter getRegisterIntentFilter() {
            // 新建动作集合
            IntentFilter filter = new IntentFilter();
            // 登录状态监听
            filter.addAction(MemoryBroadcast.MEMORY_STATE_LOGIN);
            // 消息请求状态监听
            filter.addAction(MemoryBroadcast.MEMORY_STATE_MESSAGE_REQUEST_STATE);
            // 消息推送广播监听
            filter.addAction(NotifyReceiver.ACTION_SHOW_NOTIFICATION);

            return filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取动作字符串
            String action = intent.getAction();
            Log.i(LOG_TAG + "LoadMessageReceiver.onReceive", "action is " + action);

            switch (action) {
                case MemoryBroadcast.MEMORY_STATE_LOGIN:
                    // 登录状态的改变
                    if (MemoryConfig.getConfig().isLogin()) {
                        // 登录成功
                        resetMessage();
                    } else {
                        // 登录失败
                        clearMessage();
                    }
                    break;
                case MemoryBroadcast.MEMORY_STATE_MESSAGE_REQUEST_STATE:
                    // 消息请求状态改变
                    if (MemoryConfig.getConfig().getMessageRequestState() == MessageNetworkState.MESSAGE_LOAD_END) {
                        // 消息请求完成
                        updateMessage();
                    }
                    break;
                case NotifyReceiver.ACTION_SHOW_NOTIFICATION:
                    // 接收到新消息推送
                    checkNewMessage();
                    break;
            }
        }
    }

    /**
     * 检查是否有新消息
     */
    private void checkNewMessage() {
        // 新线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 刷新消息加载器
                LoadMessage.getLoadMessage().refreshLocalMessage();
                // 尝试下载最新消息
                LoadMessage.getLoadMessage().checkLatestMessage();
            }
        });

        thread.start();
    }

    /**
     * 注册广播接收者
     */
    private void registerReceivers() {

        // 新建本地广播管理器
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());

        // 新建接收者
        loadMessageReceiver = new LoadMessageReceiver();

        // 注册接收者
        localBroadcastManager.registerReceiver(loadMessageReceiver, loadMessageReceiver.getRegisterIntentFilter());
    }

    /**
     * 注销广播接收者
     */
    private void unregisterReceivers() {

        if (localBroadcastManager == null) {
            return;
        }

        if (loadMessageReceiver != null) {
            localBroadcastManager.unregisterReceiver(loadMessageReceiver);
        }
    }

    /**
     * 重置消息列表，重新登录或更换用户时执行
     */
    private void resetMessage() {
        // 新线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 重置消息加载器
                LoadMessage.refreshLoadMessage();
                // 获取本地消息
                adapter.setMessageList(LoadMessage.getLoadMessage().getMessageList());
                // 更新界面
                adapterReset.sendEmptyMessage(0);
                // 尝试下载最新消息
                LoadMessage.getLoadMessage().checkLatestMessage();
            }
        });

        thread.start();
    }

    /**
     * 清空消息列表，用于无用户状态
     */
    private void clearMessage() {
        // 重置消息加载器
        LoadMessage.refreshLoadMessage();
        // 置空
        adapter.setMessageList(new ArrayList<Message>());
        // 更新界面
        adapterReset.sendEmptyMessage(0);
    }

    /**
     * 更新消息列表，用于新消息请求完成
     */
    private void updateMessage() {
        // 得到新下载的消息
        List<Message> newMessageList = LoadMessage.getLoadMessage().getNewMessageList();
        if (newMessageList != null && newMessageList.size() > 0) {
            // 追加新消息到头部
            adapter.add(newMessageList);
        }
        // 刷新消息加载器
        LoadMessage.getLoadMessage().refreshLocalMessage();
    }

    @Override
    public boolean onBackPressed() {
        // 判断当前是否处于删除消息状态
        if (isDeleteLayoutState) {
            // 是删除状态则还原模式
            changeReadModel();
            return true;
        } else {
            return false;
        }
    }
}
