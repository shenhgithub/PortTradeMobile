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
 * 获取个人信息的数据模型类
 *
 * @author 超悟空
 * @version 1.0 2015/3/24
 * @since 1.0
 */
public class PersonalInfoData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PersonalInfoData.";

    /**
     * 用户唯一标识符
     */
    private String userID = null;

    /**
     * 用户个人信息数据对象
     */
    private PersonalInfo personalInfo = null;

    /**
     * 设置用户标识符
     *
     * @param userID 用户标识字符串
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 获取用户个人信息数据
     *
     * @return 个人信息数据对象
     */
    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入用户标识
        dataMap.put("userId", userID);
        Log.i(LOG_TAG + "serialization", "userId is " + userID);

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

        if (resultString == null || resultString.equals("0,0,0")) {
            // 请求失败或用户信息不存在
            Log.d(LOG_TAG + "parse", "data type is error");
            return false;
        }

        // 提取个人信息
        String[] info = resultString.split(",");
        Log.i(LOG_TAG + "parse", "info count is " + info.length);

        personalInfo = new PersonalInfo();

        personalInfo.setUserID(userID);
        personalInfo.setPhone(info.length > 0 ? info[0] : null);
        Log.i(LOG_TAG + "parse", "phone is " + personalInfo.getPhone());
        personalInfo.setMobile(info.length > 1 ? info[1] : null);
        Log.i(LOG_TAG + "parse", "mobile is " + personalInfo.getMobile());
        personalInfo.setEmail(info.length > 2 ? info[2] : null);
        Log.i(LOG_TAG + "parse", "email is " + personalInfo.getEmail());

        return true;
    }
}
