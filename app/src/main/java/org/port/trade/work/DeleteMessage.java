package org.port.trade.work;
/**
 * Created by 超悟空 on 2015/4/1.
 */

import android.util.Log;

import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.port.trade.data.Message;
import org.port.trade.data.MessageDeleteData;
import org.port.trade.util.NetworkType;
import org.port.trade.util.communication.CommunicationFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除消息
 *
 * @author 超悟空
 * @version 1.0 2015/4/1
 * @since 1.0
 */
public class DeleteMessage extends WorkModel<Message, List<Integer>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DeleteMessage.";

    @Override
    protected boolean onDoWork(Message... parameters) {

        if (parameters == null || parameters.length < 1) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "message is null");
            return false;
        }

        // 新建http get请求的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.HTTP_GET);

        // 设置调用的方法名
        communication.setTaskName("MessDelete.aspx");
        Log.i(LOG_TAG + "onDoWork", "task name is MessDelete.aspx");

        // 执行删除失败的消息列表索引
        List<Integer> messageList = new ArrayList<>();

        for (int i = 0; i < parameters.length; i++) {

            Log.i(LOG_TAG + "onDoWork", "this message id is " + parameters[i].getMessageId());

            // 新建消息删除对象
            MessageDeleteData data = new MessageDeleteData();

            // 传入参数
            data.setMessage(parameters[i]);
            // 发送请求
            //noinspection unchecked
            communication.Request(data.serialization());
            if (data.parse(communication.Response())) {
                // 删除成功
                Log.i(LOG_TAG + "onDoWork", "delete success message id is " + parameters[i].getMessageId());
            } else {
                // 删除失败，将失败的消息索引加入队列
                messageList.add(i);
                Log.d(LOG_TAG + "onDoWork", "delete failed message id is " + parameters[i].getMessageId());
            }
        }

        // 设置返回结果
        setResult(messageList);

        return true;
    }
}
