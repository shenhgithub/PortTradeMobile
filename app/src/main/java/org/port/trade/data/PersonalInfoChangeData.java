package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/3/24.
 */

import android.util.Log;

import org.mobile.model.data.IDataModel;
import org.mobile.parser.HttpResponseHttpEntityToStringParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息修改的数据模型类
 *
 * @author 超悟空
 * @version 1.0 2015/3/24
 * @since 1.0
 */
public class PersonalInfoChangeData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PersonalInfoChangeData.";

    /**
     * 用户个人信息数据对象
     */
    private PersonalInfo personalInfo = null;

    /**
     * 回显消息
     */
    private String message = null;

    /**
     * 设置用户个人信息
     *
     * @param personalInfo 个人信息数据对象
     */
    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    /**
     * 获取服务器回显消息
     *
     * @return 消息字符串
     */
    public String getMessage() {
        return message;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        if (personalInfo == null) {
            // 未设置参数
            Log.d(LOG_TAG + "serialization", "personal info is null");
            return null;
        }

        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入用户标识
        dataMap.put("userId", personalInfo.getUserID());
        Log.i(LOG_TAG + "serialization", "userId is " + personalInfo.getUserID());

        // 加入电话号
        dataMap.put("phone", personalInfo.getPhone());
        Log.i(LOG_TAG + "serialization", "phone is " + personalInfo.getPhone());

        // 加入手机号
        dataMap.put("mobile", personalInfo.getMobile());
        Log.i(LOG_TAG + "serialization", "mobile is " + personalInfo.getMobile());

        // 加入邮箱号
        dataMap.put("email", personalInfo.getEmail());
        Log.i(LOG_TAG + "serialization", "email is " + personalInfo.getEmail());

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

        if (resultString == null || !resultString.trim().equals("更新成功！")) {
            // 执行失败
            Log.d(LOG_TAG + "parse", "data type is error");
            return false;
        } else {
            // 执行成功
            return true;
        }
    }
}
