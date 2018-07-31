package org.port.trade.work;
/**
 * Created by 超悟空 on 2015/2/11.
 */

import android.util.Log;

import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.port.trade.data.Message;
import org.port.trade.data.MessageContentData;
import org.port.trade.util.NetworkType;
import org.port.trade.util.communication.CommunicationFactory;

/**
 * 得到消息内容
 *
 * @author 超悟空
 * @version 1.0 2015/2/11
 * @since 1.0
 */
public class PullMessageContent extends WorkModel<Message, Message> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PullMessageContent.";

    @Override
    protected boolean onDoWork(Message... parameters) {
        if (parameters == null || parameters.length < 1) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "message is null");
            return false;
        }

        // 新建带有服务器公钥的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.WEBSERVICE_HAS_PUBLIC_KEY);

        // 新建消息内容数据模型
        MessageContentData data = new MessageContentData();
        data.setMessage(parameters[0]);

        // 设置调用的方法名
        communication.setTaskName("SelectMessageContent");
        Log.i(LOG_TAG + "onDoWork", "task name is SelectMessageContent");

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (data.parse(communication.Response())) {
            // 获取成功，设置结果
            Log.i(LOG_TAG + "onDoWork", "get message content success");
            setResult(data.getMessage());
            return true;
        } else {
            // 获取失败
            Log.i(LOG_TAG + "onDoWork", "get message content failed");
            return false;
        }
    }
}
