package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/4/1.
 */

import android.util.Log;

import org.mobile.model.data.IDataModel;
import org.mobile.parser.HttpResponseHttpEntityToStringParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息列表消息删除模型类
 *
 * @author 超悟空
 * @version 1.0 2015/4/1
 * @since 1.0
 */
public class MessageDeleteData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MessageDeleteData.";

    /**
     * 消息对象
     */
    private Message message = null;

    /**
     * 设置消息对象
     *
     * @param message 消息对象
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        // 新建要发送的参数集
        Map<String, String> dataMap = new HashMap<>();

        if (message != null) {
            dataMap.put("messId", String.valueOf(message.getMessageId()));
            Log.i(LOG_TAG + "serialization", "message id is " + message.getMessageId());
        }

        return dataMap;
    }

    @Override
    public boolean parse(Object data) {
        Log.i(LOG_TAG + "parse", "parse start");

        if (data == null) {
            // 通信异常
            Log.d(LOG_TAG + "parse", "data is null");
            return false;
        }

        // 新建解析器
        HttpResponseHttpEntityToStringParser parser = new HttpResponseHttpEntityToStringParser();

        // 获取结果字符串
        String resultString = parser.DataParser(data);
        Log.i(LOG_TAG + "parse", "result string is " + resultString);

        if (resultString == null || !resultString.trim().equals("删除成功！")) {
            // 执行失败
            Log.d(LOG_TAG + "parse", "data type is error");
            return false;
        } else {
            // 执行成功
            return true;
        }
    }
}
