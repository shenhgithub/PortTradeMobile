package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/2/4.
 */

import android.util.Log;

import org.mobile.model.data.IDataModel;
import org.port.trade.util.parser.SoapXmlCollection;
import org.port.trade.util.parser.SoapXmlParser;
import org.port.trade.util.parser.SoapXmlResultState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息列表数据接收模型类
 *
 * @author 超悟空
 * @version 1.0 2015/2/4
 * @since 1.0
 */
public class MessageListData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MessageListData.";

    /**
     * 发送者为系统消息时的发送者编号
     */
    private static final String SYSYEM_MESSAGE_CODE = "0";

    /**
     * 系统消息的实际显示名
     */
    private static final String SYSTEM_MESSAGE_NAME = "系统消息";

    /**
     * 消息列表
     */
    private List<Message> messageList = null;

    /**
     * 用户ID
     */
    private String userID = null;

    /**
     * 要获取的消息开始索引
     */
    private int messageIndexStart = -1;

    /**
     * 要获取的消息结束索引
     */
    private int messageIndexEnd = -1;

    /**
     * 设置用户ID
     *
     * @param userID 用户ID字符串
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 设置消息起索引
     *
     * @param messageIndexStart 从1开始的编号
     */
    public void setMessageIndexStart(int messageIndexStart) {
        this.messageIndexStart = messageIndexStart;
    }

    /**
     * 设置消息结束索引，
     * 需要大于{@link #setMessageIndexStart(int)}中设置的值，
     * 否则将获取不到数据
     *
     * @param messageIndexEnd 结束编号
     */
    public void setMessageIndexEnd(int messageIndexEnd) {
        this.messageIndexEnd = messageIndexEnd;
    }

    /**
     * 获取消息列表
     *
     * @return 包含摘要，不包含内容的消息列表
     */
    public List<Message> getMessageList() {
        return messageList;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        // 新建要发送的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入用户ID
        dataMap.put("UserId", userID);
        Log.i(LOG_TAG + "serialization", "UserId is " + userID);

        // 加入索引开始
        dataMap.put("minRow", String.valueOf(messageIndexStart));
        Log.i(LOG_TAG + "serialization", "minRow is " + messageIndexStart);

        // 加入索引结束
        dataMap.put("maxRow", String.valueOf(messageIndexEnd));
        Log.i(LOG_TAG + "serialization", "maxRow is " + messageIndexEnd);

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

        // 新建消息列表
        messageList = new ArrayList<>();

        // 取出全部数据
        for (Map<String, String> dataMap : dataList) {
            // 新建一条消息对象
            Message message = new Message();
            // 获取并设置消息内容
            message.setMessageId(Integer.parseInt(dataMap.get("MESSID")));
            message.setSendUser(SYSYEM_MESSAGE_CODE.equals(dataMap.get("SENDERID")) ? SYSTEM_MESSAGE_NAME : dataMap.get("SENDERID"));
            message.setMessageTime(dataMap.get("TIME"));
            message.setMessageAbstract(dataMap.get("MESSAGE"));
            message.setReadState(!dataMap.get("ISREAD").equals("0"));

            // 加入消息队列
            messageList.add(message);
        }
        Log.i(LOG_TAG + "parse", "message list count is " + messageList.size());

        return true;
    }
}
