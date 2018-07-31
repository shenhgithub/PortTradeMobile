package org.port.trade.work;
/**
 * Created by 超悟空 on 2015/2/5.
 */

import android.util.Log;

import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.port.trade.data.Message;
import org.port.trade.data.MessageListData;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.NetworkType;
import org.port.trade.util.communication.CommunicationFactory;

import java.util.List;

/**
 * 得到消息列表
 *
 * @author 超悟空
 * @version 1.0 2015/2/5
 * @since 1.0
 */
public class PullMessageList extends WorkModel<Integer, List<Message>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PullMessageList.";

    @Override
    protected boolean onDoWork(Integer... parameters) {
        if (parameters == null || parameters.length < 2) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "index is null");
            return false;
        }

        // 新建带有服务器公钥的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.WEBSERVICE_HAS_PUBLIC_KEY);

        // 新建消息列表数据模型
        MessageListData data = new MessageListData();
        data.setMessageIndexStart(parameters[0]);
        data.setMessageIndexEnd(parameters[1]);
        data.setUserID(MemoryConfig.getConfig().getUserID());

        // 设置调用的方法名
        communication.setTaskName("SelectMessageAbstract");
        Log.i(LOG_TAG + "onDoWork", "task name is SelectMessageAbstract");

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (data.parse(communication.Response())) {
            // 设置消息列表到结果
            Log.i(LOG_TAG + "onDoWork", "get message list success");
            setResult(data.getMessageList());
            return true;
        } else {
            // 没有请求到数据
            Log.i(LOG_TAG + "onDoWork", "get message list failed");
            return false;
        }
    }
}
