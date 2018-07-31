package org.port.trade.function;
/**
 * Created by 超悟空 on 2015/2/9.
 */

import android.util.Log;

import org.mobile.model.work.WorkBack;
import org.mobile.util.ContextUtil;
import org.port.trade.data.Message;
import org.port.trade.db.message.MessageOperator;
import org.port.trade.util.MemoryBroadcast;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.state.MessageNetworkState;
import org.port.trade.work.DeleteMessage;
import org.port.trade.work.PullMessageContent;
import org.port.trade.work.PullMessageList;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载消息功能类
 *
 * @author 超悟空
 * @version 1.0 2015/2/9
 * @since 1.0
 */
public class LoadMessage {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "LoadMessage.";

    /**
     * 本类的静态实例
     */
    private static LoadMessage loadMessage = null;

    /**
     * 当前使用的消息队列
     */
    private List<Message> messageList = null;

    /**
     * 新下载的消息队列
     */
    private List<Message> newMessageList = null;

    /**
     * 管理消息数据库的操作类
     */
    private MessageOperator messageOperator = null;

    /**
     * 每次下载的消息条数
     */
    private static final int LOOP_MESSAGE_COUNT = 5;

    /**
     * 下载的临时新消息数据
     */
    private List<Message> downloadMessageList = null;

    /**
     * 线程中断信号
     */
    private volatile boolean threadStop = false;

    /**
     * 构造函数
     */
    private LoadMessage() {
        // 将消息状态置为空闲
        MemoryConfig.getConfig().setMessageRequestState(MessageNetworkState.IDLE);
        // 新建数据库操作对象
        if (MemoryConfig.getConfig().getUserID() != null) {
            Log.i(LOG_TAG + "LoadMessage", "userID is " + MemoryConfig.getConfig().getUserID());
            this.messageOperator = new MessageOperator(ContextUtil.getContext(), MemoryConfig.getConfig().getUserID());
            // 从数据库取出该用户的全部消息
            this.messageList = messageOperator.queryAllForMessage();
            Log.i(LOG_TAG + "LoadMessage", "messageList count " + messageList.size());
        } else {
            Log.i(LOG_TAG + "LoadMessage", "userID is null");
        }
    }

    /**
     * 得到消息数据加载功能对象
     *
     * @return 消息功能对象
     */
    public static LoadMessage getLoadMessage() {
        if (loadMessage == null) {
            loadMessage = new LoadMessage();
        }
        return loadMessage;
    }

    /**
     * 重新构建消息数据加载功能对象，
     * 当用户重新登录或者更换登录等
     *
     * @return 消息功能对象
     */
    public static LoadMessage refreshLoadMessage() {

        if (loadMessage != null && loadMessage.messageOperator != null) {
            // 关闭之前的数据库
            loadMessage.messageOperator.close();

            // 通知未执行完的线程中断
            loadMessage.threadStop = true;
        }

        // 新建一个消息加载对象
        loadMessage = new LoadMessage();
        return loadMessage;
    }

    /**
     * 获取本地消息队列，作为消息列表的数据源
     *
     * @return 消息集合
     */
    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * 获取最新下载的消息队列，
     * 不含已有数据，
     * 即与{@link #getMessageList()}获取的数据无交集
     *
     * @return 消息集合，空集合或null引用表示没有新消息
     */
    public List<Message> getNewMessageList() {
        return newMessageList;
    }

    /**
     * 检查最新消息，异步执行，
     * 执行完成后会发送{@link org.port.trade.util.MemoryBroadcast#MEMORY_STATE_MESSAGE_REQUEST_STATE}广播，
     * 执行过程中会改变{@link org.port.trade.util.MemoryConfig#getMessageRequestState()}的状态，
     * 成功检查后{@link org.port.trade.util.MemoryConfig#getMessageRequestState()}状态值为{@link org.port.trade.util.state.MessageNetworkState#MESSAGE_LOAD_END}，
     * 如果有新消息会被保存于本地数据库，但是{@link #getMessageList()}返回的消息队列不会被刷新，
     * 新下载的消息可以通过{@link #getNewMessageList()}获取，
     * 只有调用{@link #refreshLocalMessage()}后才会刷新{@link #getMessageList()}返回的本地消息队列
     */
    public void checkLatestMessage() {

        if (messageOperator == null) {
            // 为空表示未登录
            Log.d(LOG_TAG + "checkLatestMessage", "messageOperator is null");

            // 检查结束
            loadEnd();
            return;
        }

        // 关闭线程中断
        this.threadStop = false;

        // 新线程,用于检查新消息
        Thread threadCheck = new Thread(new Runnable() {
            @Override
            public void run() {
                firstCheck();
            }
        });

        // 新线程，用于异步删除
        Thread threadDelete = new Thread(new Runnable() {
            @Override
            public void run() {
                deleteMessage(messageOperator.queryDeleteForMessage());
            }
        });

        threadCheck.start();
        threadDelete.start();
    }

    /**
     * 用于删除消息，在独立线程中执行
     *
     * @param messages 要删除的消息列表，删除执行完毕后该队列会被清空
     */
    public void deleteMessage(final List<Message> messages) {

        Log.i(LOG_TAG + "deleteMessage", "messages count is " + messages.size());

        // 新建消息删除任务
        DeleteMessage deleteMessage = new DeleteMessage();

        // 设置任务回调
        deleteMessage.setWorkBackListener(new WorkBack<List<Integer>>() {
            @Override
            public void doEndWork(boolean state, List<Integer> data) {

                Log.i(LOG_TAG + "deleteMessage", "messages count is " + messages.size());

                // 将删除成功的消息从数据库清除
                for (Message message : messages) {
                    messageOperator.delete(message);
                }
                // 释放资源
                messages.clear();
            }
        });

        // 执行任务
        deleteMessage.beginExecute(messages.toArray(new Message[messages.size()]));
    }

    /**
     * 第一次检测，如果有新消息会自动启动下载
     */
    private synchronized void firstCheck() {

        if (threadStop) {
            // 线程中断
            Log.d(LOG_TAG + "firstCheck", "thread stop");
            return;
        }

        // 改变消息加载状态为第一次尝试
        MemoryConfig.getConfig().setMessageRequestState(MessageNetworkState.TRY_REQUESTING);

        // 新建一个消息任务
        PullMessageList pullMessageList = new PullMessageList();

        // 设置任务回调
        pullMessageList.setWorkBackListener(new WorkBack<List<Message>>() {
            @Override
            public void doEndWork(boolean state, List<Message> data) {
                // 改变消息加载状态为第一次尝试结束
                MemoryConfig.getConfig().setMessageRequestState(MessageNetworkState.TRY_REQUESTED);
                if (state) {
                    // 执行成功，得到消息

                    if (messageList == null || messageList.size() < 1 || data.get(0).getMessageId() > messageList.get(0).getMessageId()) {
                        // 原来没有消息，表示存在新消息,
                        // 或者得到的消息比已有消息新
                        // 下载新消息
                        Log.i(LOG_TAG + "firstCheck", "update message");

                        downloadMessageList = new ArrayList<>();
                        downloadMessage();
                    }
                } else {
                    // 执行失败或没有消息
                    Log.i(LOG_TAG + "firstCheck", "state is false");

                    // 检查结束
                    loadEnd();
                }
            }
        });

        // 执行任务，当前在子线程中，第一次只请求一条消息
        pullMessageList.execute(1, 1);
    }

    /**
     * 消息加载结束
     */
    private void loadEnd() {
        if (threadStop) {
            // 线程中断
            Log.d(LOG_TAG + "loadEnd", "thread stop");
            return;
        }

        Log.i(LOG_TAG + "loadEnd", "check latest message end");
        // 改变消息加载状态为已完成
        MemoryConfig.getConfig().setMessageRequestState(MessageNetworkState.MESSAGE_LOAD_END);
        // 发送消息请求状态广播
        MemoryBroadcast.sendBroadcast(ContextUtil.getContext(), MemoryBroadcast.MEMORY_STATE_MESSAGE_REQUEST_STATE);
    }

    /**
     * 下载新消息
     */
    private void downloadMessage() {
        Log.i(LOG_TAG + "downloadMessage", "download message begin");

        if (threadStop) {
            // 线程中断
            Log.d(LOG_TAG + "downloadMessage", "thread stop");
            return;
        }

        // 改变消息加载状态为循环下载
        MemoryConfig.getConfig().setMessageRequestState(MessageNetworkState.DOWNLOAD_MESSAGE_LOOP);

        // 新建一个消息任务
        PullMessageList pullMessageList = new PullMessageList();

        // 设置任务回调
        pullMessageList.setWorkBackListener(new WorkBack<List<Message>>() {
            @Override
            public void doEndWork(boolean state, List<Message> data) {
                if (state) {
                    // 下载成功
                    downloadSuccess(data);
                } else {
                    // 下载失败或没有更多数据
                    downloadEnd(data);
                }
            }
        });

        // 执行下载，每次请求最多LOOP_MESSAGE_COUNT条消息
        pullMessageList.execute(downloadMessageList.size() + 1, downloadMessageList.size() + LOOP_MESSAGE_COUNT);
    }

    /**
     * 下载成功后的处理
     *
     * @param data 下载到的消息队列
     */
    private void downloadSuccess(List<Message> data) {
        Log.i(LOG_TAG + "downloadSuccess", "download message success");

        if (threadStop) {
            // 线程中断
            Log.d(LOG_TAG + "downloadSuccess", "thread stop");
            return;
        }

        if (data == null || data.size() < LOOP_MESSAGE_COUNT) {
            // 不足LOOP_MESSAGE_COUNT条，下载结束
            Log.i(LOG_TAG + "downloadSuccess", "new message list count less than " + LOOP_MESSAGE_COUNT);
            downloadEnd(data);
        } else {
            // 匹配并添加数据
            if (matchAddMessage(data)) {
                // 本次下载到的消息存在比原消息更旧的消息
                Log.i(LOG_TAG + "downloadSuccess", "new message list has old message");
                // 下载结束
                downloadEnd(null);
            } else {
                // 仍可能存在新消息，继续下载
                downloadMessage();
            }
        }
    }

    /**
     * 匹配并追加新消息，
     * 并返回匹配结果
     *
     * @param data 本次下载到的消息
     *
     * @return true表示本次下载消息存在比原消息更旧的消息，
     * 即没有更多新消息；false表示本次下载消息全部新于原消息
     */
    private boolean matchAddMessage(List<Message> data) {
        Log.i(LOG_TAG + "matchAddMessage", "match new message begin");

        if (messageList == null || messageList.size() < 1) {
            // 无原消息，本次下载全为最新消息
            downloadMessageList.addAll(data);
            Log.i(LOG_TAG + "matchAddMessage", "data count " + data.size());
            return false;
        }

        int messageId = messageList.get(0).getMessageId();

        Log.i(LOG_TAG + "matchAddMessage", "old messageId is " + messageId);

        // 顺序比较消息ID，并将新消息加入下载队列
        for (Message message : data) {
            if (message.getMessageId() <= messageId) {
                // 不是新消息，下载结束
                Log.i(LOG_TAG + "matchAddMessage", "end messageId is " + message.getMessageId());
                return true;
            } else {
                // 将新消息加入下载队列
                downloadMessageList.add(message);
                Log.i(LOG_TAG + "matchAddMessage", "new messageId is " + message.getMessageId());
            }
        }

        return false;
    }

    /**
     * 下载结束的操作
     *
     * @param data 下载到的消息队列
     */
    private void downloadEnd(List<Message> data) {
        Log.i(LOG_TAG + "downloadEnd", "download message end");

        if (threadStop) {
            // 线程中断
            Log.d(LOG_TAG + "downloadMessage", "thread stop");
            return;
        }
        // 改变消息加载状态为下载结束
        MemoryConfig.getConfig().setMessageRequestState(MessageNetworkState.DOWNLOAD_MESSAGE_END);

        if (data != null && data.size() > 0) {
            // 存在部分数据
            Log.i(LOG_TAG + "downloadEnd", "new message list not null");
            // 匹配并添加数据
            matchAddMessage(data);
        }
        // 执行持久化
        saveMessage();
        // 结束消息加载
        loadEnd();
    }

    /**
     * 持久化新下载的消息，
     * 同时填充{@link #newMessageList},
     * 清空{@link #downloadMessageList}
     */
    private void saveMessage() {
        Log.i(LOG_TAG + "saveMessage", "save message begin");

        if (threadStop) {
            // 线程中断
            Log.d(LOG_TAG + "downloadMessage", "thread stop");
            return;
        }

        if (messageOperator == null) {
            // 某些异常情况或者不进行消息本地化
            Log.d(LOG_TAG + "saveMessage", "messageOperator is null");
            newMessageList = downloadMessageList;
            downloadMessageList = null;
            return;
        }

        Log.i(LOG_TAG + "saveMessage", "downloadMessageList count " + downloadMessageList.size());

        // 将下载的临时数据存入数据库，并接收保存成功的数据库行ID
        List<Long> rowIdList = messageOperator.save(downloadMessageList);
        Log.i(LOG_TAG + "saveMessage", "rowIdList count " + rowIdList.size());

        if (rowIdList.size() > 0) {
            // 从数据库查出新保存的消息，rowIdList为升序序列
            newMessageList = messageOperator.queryByIds(rowIdList.get(0), rowIdList.get(rowIdList.size() - 1));
            Log.i(LOG_TAG + "saveMessage", "rowIdList count " + newMessageList.size());
        }
        downloadMessageList = null;
    }

    /**
     * 在本地数据库中更新消息
     *
     * @param message 要更新的消息对象
     */
    public void updateMessage(Message message) {
        if (messageOperator != null) {
            Log.i(LOG_TAG + "updateMessage", "messageId is " + message.getMessageId());
            messageOperator.update(message);
        } else {
            Log.i(LOG_TAG + "updateMessage", "messageOperator is null");
        }
    }

    /**
     * 刷新本地消息队列，异步执行
     * 用于在消息下载完毕后并且已经追加显示完毕后调用，
     * 即{@link org.port.trade.util.MemoryConfig#getMessageRequestState()}状态必须处于{@link org.port.trade.util.state.MessageNetworkState#MESSAGE_LOAD_END}时才有效，
     * 成功执行后{@link #getMessageList()}方法将返回最新的消息列表，
     * 包括在此之前通过{@link #checkLatestMessage()}获取的消息，
     * 且会重置{@link #getNewMessageList()}中的列表为null，
     * 重置{@link org.port.trade.util.MemoryConfig#getMessageRequestState()}状态为{@link org.port.trade.util.state.MessageNetworkState#IDLE}
     */
    public void refreshLocalMessage() {
        Log.i(LOG_TAG + "refreshLocalMessage", "refresh local message begin");

        // 新线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (MemoryConfig.getConfig().getMessageRequestState() != MessageNetworkState.MESSAGE_LOAD_END) {
                    // 当前消息请求状态错误
                    Log.i(LOG_TAG + "refreshLocalMessage", "message network state is " + MemoryConfig.getConfig().getMessageRequestState());
                    return;
                }

                // 从本地读取消息
                if (messageOperator != null) {
                    // 从数据库取出该用户的全部消息
                    messageList = messageOperator.queryAllForMessage();
                    Log.i(LOG_TAG + "LoadMessage", "messageList count " + messageList.size());
                }
                // 重置网络下载消息
                newMessageList = null;
                // 重置消息请求状态为空闲
                MemoryConfig.getConfig().setMessageRequestState(MessageNetworkState.IDLE);
            }
        });

        thread.start();
    }

    /**
     * 为指定的消息对象填充消息内容，
     * 即从服务器获取消息内容，
     * 同时持久化到本地数据库，
     * 方法为异步执行
     *
     * @param message 消息对象
     */
    public void fillingMessageContent(Message message) {
        fillingMessageContent(message, null);
    }

    /**
     * 为指定的消息对象填充消息内容，
     * 即从服务器获取消息内容，
     * 同时持久化到本地数据库，
     * 同时消息会被标记为已读，
     * 方法为异步执行
     *
     * @param message                   消息对象
     * @param backFillingMessageContent 执行结束的回调接口，在UI线程执行
     */
    public void fillingMessageContent(final Message message, final BackFillingMessageContent backFillingMessageContent) {
        Log.i(LOG_TAG + "fillingMessageContent", "filling message content begin");

        // 消息内容获取任务
        PullMessageContent pullMessageContent = new PullMessageContent();

        // 设置任务回调
        pullMessageContent.setWorkBackListener(new WorkBack<Message>() {
            @Override
            public void doEndWork(boolean state, Message data) {

                Log.i(LOG_TAG + "fillingMessageContent", "state is " + state);

                if (state) {
                    // 请求成功，保存消息
                    // 设置为已读
                    Log.i(LOG_TAG + "fillingMessageContent", "messageId is " + data.getMessageId());
                    message.setReadState(true);
                    // 更新本地数据库
                    updateMessage(data);
                }

                if (backFillingMessageContent != null) {
                    // 存在回调接口则执行回调
                    backFillingMessageContent.onEndBack(data, state);
                }
            }
        });

        // 执行任务
        pullMessageContent.beginExecute(message);
    }

    /**
     * 消息内容填充{@link #fillingMessageContent(org.port.trade.data.Message , org.port.trade.function.LoadMessage.BackFillingMessageContent)}结束回调接口
     *
     * @author 超悟空
     * @version 1.0 2015/2/11
     * @since 1.0
     */
    public interface BackFillingMessageContent {

        /**
         * 消息填充{@link #fillingMessageContent(org.port.trade.data.Message , org.port.trade.function.LoadMessage.BackFillingMessageContent)}结束的回调方法，
         * 如果请求内容成功，则该方法在消息内容被保存到本地数据库之后调用，否则直接调用
         *
         * @param message 填充后的消息对象，当state为true时{@link org.port.trade.data.Message#getMessageContent()}有值
         * @param state   请求执行结果
         */
        public void onEndBack(Message message, boolean state);
    }
}

