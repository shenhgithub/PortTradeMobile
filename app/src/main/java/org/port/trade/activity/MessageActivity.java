package org.port.trade.activity;
/**
 * Created by 超悟空 on 2015/2/11.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.port.trade.R;
import org.port.trade.data.Message;
import org.port.trade.function.LoadMessage;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.StaticValue;

/**
 * 展示消息内容的Activity
 *
 * @author 超悟空
 * @version 1.0 2015/2/11
 * @since 1.0
 */
public class MessageActivity extends ActionBarActivity {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MessageActivity.";

    /**
     * 标题栏的标题文本
     */
    private TextView toolbarTitleTextView = null;

    /**
     * 要加载的消息对象
     */
    private Message message = null;

    /**
     * 内容文本
     */
    private TextView messageContent = null;

    /**
     * 时间文本
     */
    private TextView messageTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_content);

        // 得到消息对象
        setMessage();

        // 加载界面
        initView();
    }

    /**
     * 获取要加载的消息对象
     */
    private void setMessage() {
        // 取出传递的参数
        Object o = getIntent().getSerializableExtra(StaticValue.MESSAGE_INTENT_TAG);

        if (o == null || !(o instanceof Message)) {
            // 不是消息对象
            Log.d(LOG_TAG + "setMessage", "no message");
        } else {
            // 得到消息对象
            message = (Message) o;
            Log.i(LOG_TAG + "setMessage", "message is " + message.getMessageId());
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 初始化Toolbar
        initToolbar();
        // 加载内容
        initMessage();
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        // 得到Toolbar标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 得到标题文本
        toolbarTitleTextView = (TextView) findViewById(R.id.toolbar_title);

        // 关联ActionBar
        setSupportActionBar(toolbar);

        // 取消原actionBar标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 显示后退
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 导航回父activity
                goBackParentActivity();
            }
        });
    }

    /**
     * 导航回父activity
     */
    private void goBackParentActivity() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
        } else {
            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    /**
     * 初始化界面内容
     */
    private void initMessage() {
        if (message != null) {
            // 设置标题为发送者
            setTitle(message.getSendUser());

            // 内容文本
            messageContent = (TextView) findViewById(R.id.activity_message_content_content);

            // 时间文本
            messageTime = (TextView) findViewById(R.id.activity_message_content_time);

            if (message.getMessageContent() != null && !message.getMessageContent().equals("")) {

                // 有内容，直接加载
                setMessageContent();
            } else {
                // 无内容，向服务器请求
                if (MemoryConfig.getConfig().isOpenNetwork()) {
                    // 显示正在请求
                    messageContent.setText(R.string.data_loading);

                    // 向服务器获取，异步方法
                    LoadMessage.getLoadMessage().fillingMessageContent(message, new LoadMessage.BackFillingMessageContent() {
                        @Override
                        public void onEndBack(Message data, boolean state) {
                            // 设置回调
                            if (state) {
                                // 获取成功，赋值
                                message = data;
                                Log.i(LOG_TAG + "initMessage", "message content:" + message.getMessageContent() + "; data content:" + data.getMessageContent());
                                setMessageContent();
                            } else {
                                // 获取失败
                                Log.d(LOG_TAG + "initMessage", "state is false");
                                messageContent.setText(R.string.data_load_error);
                            }
                        }
                    });
                } else {
                    // 网络不可用
                    Log.d(LOG_TAG + "initMessage", "no network");
                    messageContent.setText(R.string.no_network);
                }
            }
        }
    }

    /**
     * 给界面加载消息内容
     */
    private void setMessageContent() {
        Log.i(LOG_TAG + "setMessageContent", "message content is " + message.getMessageContent());
        messageContent.setText(message.getMessageContent());
        messageTime.setText(message.getMessageTime());
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        // 设置标题
        toolbarTitleTextView.setText(title);
    }
}
