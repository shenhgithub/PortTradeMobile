package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/1/28.
 */

import android.util.Log;

import org.mobile.model.data.IDataModel;
import org.mobile.parser.HttpResponseHttpEntityToStringParser;
import org.port.trade.notify.NotifyConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备绑定的数据模型类
 *
 * @author 超悟空
 * @version 1.0 2015/1/28
 * @since 1.0
 */
public class DeviceBindData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DeviceBindData.";

    /**
     * 用户唯一标识符
     */
    private String userID = null;

    /**
     * 标识是否绑定设备
     */
    private boolean binding = false;

    /**
     * 设置用户标识符
     *
     * @param userID 用户标识字符串
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 设置绑定状态，
     * 用于绑定或解绑
     *
     * @param binding 绑定设为true，解绑设为false
     */
    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入用户标识
        dataMap.put("usercode", userID);
        Log.i(LOG_TAG + "serialization", "usercode is " + userID);
        // 加入设备ID
        dataMap.put("deviceToken", NotifyConfig.getNotifyConfig().getNotifyUserName());
        Log.i(LOG_TAG + "serialization", "deviceToken is " + NotifyConfig.getNotifyConfig().getNotifyUserName());
        // 加入设备类型
        dataMap.put("DeviceType", "Android");
        Log.i(LOG_TAG + "serialization", "DeviceType is Android");
        // 加入绑定状态请求
        dataMap.put("isbinding", binding ? "yes" : "no");
        Log.i(LOG_TAG + "serialization", "isbinding is " + binding);

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

        if (resultString == null || (!resultString.trim().equals("绑定成功！") && !resultString.trim().equals("解绑成功！"))) {
            // 返回空表示解析失败，数据类型不匹配
            Log.d(LOG_TAG + "parse", "data type is error");
            return false;
        } else {
            // 返回值为"true"表明执行成功
            return true;
        }
    }
}
