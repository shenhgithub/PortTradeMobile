package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/3/23.
 */

import android.util.Log;

import org.mobile.model.data.IDataModel;
import org.mobile.parser.HttpResponseHttpEntityToStringParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码修改的数据模型类
 *
 * @author 超悟空
 * @version 1.0 2015/3/23
 * @since 1.0
 */
public class PasswordChangeData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PasswordChangeData.";

    /**
     * 用户唯一标识符
     */
    private String userID = null;

    /**
     * 原密码
     */
    private String oldPassword = null;

    /**
     * 新密码
     */
    private String newPassword = null;

    /**
     * 得到的回显消息
     */
    private String message = null;

    /**
     * 设置用户标识符
     *
     * @param userID 用户标识字符串
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 设置原密码
     *
     * @param oldPassword 密码
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * 设置新密码
     *
     * @param newPassword 密码
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * 获取服务器返回的结果消息
     *
     * @return 消息字符串
     */
    public String getMessage() {
        return message;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入用户标识
        dataMap.put("userId", userID);
        Log.i(LOG_TAG + "serialization", "userId is " + userID);

        // 加入原密码
        dataMap.put("oldPassword", oldPassword);
        Log.i(LOG_TAG + "serialization", "oldPassword is " + oldPassword);

        // 加入新密码
        dataMap.put("newPassword", newPassword);
        Log.i(LOG_TAG + "serialization", "newPassword is " + newPassword);

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

        // 设置回显消息
        this.message = resultString;

        if (resultString == null || !resultString.trim().equals("修改成功！")) {
            // 执行失败
            Log.d(LOG_TAG + "parse", "data type is error");
            return false;
        } else {
            // 执行成功
            return true;
        }
    }
}
