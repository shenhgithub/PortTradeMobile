package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/2/5.
 */

import android.util.Log;

import org.mobile.model.data.IDataModel;
import org.port.trade.util.parser.SoapXmlCollection;
import org.port.trade.util.parser.SoapXmlParser;
import org.port.trade.util.parser.SoapXmlResultState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息详细内容数据模型
 *
 * @author 超悟空
 * @version 1.0 2015/2/5
 * @since 1.0
 */
public class MessageContentData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MessageContentData.";

    /**
     * 消息ID
     */
    private int messageId = -1;

    /**
     * 消息对象
     */
    private Message message = null;

    /**
     * 消息内容
     */
    private String messageContent = null;

    /**
     * 设置消息ID
     *
     * @param messageId 消息ID
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * 设置消息对象
     *
     * @param message 消息对象
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * 获取消息对象，
     * 需要在请求前调用{@link #setMessage(Message)}设置消息对象，
     * 在请求完成后调用该方法可以获得填充了消息内容{@link Message#getMessageContent()}的消息对象
     *
     * @return 消息对象
     */
    public Message getMessage() {
        return message;
    }

    /**
     * 获取得到的消息内容
     *
     * @return 内容字符串
     */
    public String getMessageContent() {
        return messageContent;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        // 新建要发送的参数集
        Map<String, String> dataMap = new HashMap<>();

        if (messageId > -1) {
            dataMap.put("MessId", String.valueOf(messageId));
            Log.i(LOG_TAG + "serialization", "message id is " + messageId);
        } else {
            if (message != null) {
                dataMap.put("MessId", String.valueOf(message.getMessageId()));
                Log.i(LOG_TAG + "serialization", "message id is " + message.getMessageId());
            }
        }

        return dataMap;
    }

    @Override
    public boolean parse(Object data) {
        Log.i(LOG_TAG + "parse", "parse start");

        // 得到解析后数据
        Map<String, Object> response = SoapXmlParser.Parser(this.getClass(), data);

        // 新建结果数据分析器
        SoapXmlResultState soapXmlResultState = new SoapXmlResultState(this.getClass(), response);

        if (!soapXmlResultState.isResult()) {
            // 请求失败
            Log.d(LOG_TAG + "parse", "soapXmlResultState is error");
            return false;
        }

        // 请求成功，开始取值
        List<Map<String, String>> dataList = SoapXmlCollection.getDataList(this.getClass(), response);

        if (dataList == null) {
            // 数据不存在
            Log.d(LOG_TAG + "parse", "dataList is null");
            return false;
        }

        // 请求成功，取出数据
        // 获取结果集
        Map<String, String> dataMap = dataList.get(0);

        // 获取消息内容
        messageContent = dataMap.get("MESSAGE");
        // 存在消息对象则给对象赋值
        if (message != null) {
            message.setMessageContent(messageContent);
        }

        Log.d(LOG_TAG + "parse", "messageContent is " + messageContent);

        return true;
    }
}
