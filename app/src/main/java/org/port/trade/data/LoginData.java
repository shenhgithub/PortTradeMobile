package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/1/24.
 */

import android.util.Log;

import org.mobile.model.data.IDataModel;
import org.port.trade.notify.NotifyConfig;
import org.port.trade.util.parser.SoapXmlCollection;
import org.port.trade.util.parser.SoapXmlParser;
import org.port.trade.util.parser.SoapXmlResultState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录的数据模型类
 *
 * @author 超悟空
 * @version 1.0 2015/1/24
 * @since 1.0
 */
public class LoginData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "LoginData.";

    /**
     * 用户输入的用户名
     */
    private String userName = null;

    /**
     * 用户输入的密码
     */
    private String password = null;

    /**
     * 登录成功后返回的用户唯一标识符
     */
    private String userID = null;

    /**
     * 登录结果消息字符串
     */
    private String message = null;

    /**
     * 部门标识
     */
    private String codeDepartment = null;

    /**
     * 公司标识
     */
    private String codeCompany = null;

    /**
     * 设备绑定状态
     */
    private boolean deviceBinding = false;

    /**
     * 登录结果
     */
    private boolean login = false;

    /**
     * 获取用户的标识符
     *
     * @return 返回标识符字符串
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户密码
     *
     * @param password 密码字符串
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名字符串
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取登录结果的消息串
     *
     * @return 返回字符串
     */
    public String getMessage() {
        return message;
    }

    /**
     * 判断是否成功登录
     *
     * @return 成功返回true，失败返回false
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * 获取当前部门编号
     *
     * @return 返回部门编号串
     */
    public String getCodeDepartment() {
        return codeDepartment;
    }

    /**
     * 获取当前公司编号
     *
     * @return 返回公司编号串
     */
    public String getCodeCompany() {
        return codeCompany;
    }

    /**
     * 获取设备绑定状态
     *
     * @return 标记是否已绑定
     */
    public boolean isDeviceBinding() {
        return deviceBinding;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");
        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入用户名和密码
        dataMap.put("logogram", userName);
        Log.i(LOG_TAG + "serialization", "logogram is " + userName);
        dataMap.put("password", password);
        Log.i(LOG_TAG + "serialization", "password is " + password);

        // 加入推送用户名（设备ID）
        dataMap.put("deviceID", NotifyConfig.getNotifyConfig().getNotifyUserName());
        Log.i(LOG_TAG + "serialization", "deviceID is " + NotifyConfig.getNotifyConfig().getNotifyUserName());

        // 加入设备类型
        dataMap.put("DeviceType", "Android");
        Log.i(LOG_TAG + "serialization", "DeviceType is Android");

        // 加入snsToken
        dataMap.put("snsToken", null);
        Log.i(LOG_TAG + "serialization", "snsToken is null");

        return dataMap;
    }

    @Override
    public boolean parse(Object data) {
        Log.i(LOG_TAG + "parse", "parse start");

        // 得到解析后数据
        Map<String, Object> response = SoapXmlParser.Parser(this.getClass(), data);

        // 新建结果数据分析器
        SoapXmlResultState soapXmlResultState = new SoapXmlResultState(this.getClass(), response);

        // 配置结果消息
        this.message = soapXmlResultState.getMessage();
        Log.i(LOG_TAG + "parse", "message is " + this.message);

        if (!soapXmlResultState.isAnalyzeResult()) {
            // 不能正常取到值，可能是服务器序列化方式改变
            Log.d(LOG_TAG + "parse", "server error");
            return false;
        }

        // 设置登录状态
        this.login = soapXmlResultState.isResult();
        Log.i(LOG_TAG + "parse", "login result " + this.login);

        // 尝试取值
        List<Map<String, String>> dataList = SoapXmlCollection.getDataList(this.getClass(), response);

        if (dataList != null) {
            // 不为空说明登录成功，开始取值
            Log.i(LOG_TAG + "parse", "get result data begin");

            // 获取结果集
            Map<String, String> dataMap = dataList.get(0);

            if (dataMap != null) {
                // 获取用户信息
                this.userID = dataMap.get("CODE_USER");
                Log.i(LOG_TAG + "parse", "userID is " + this.userID);
                this.codeDepartment = dataMap.get("CODE_DEPARTMENT");
                Log.i(LOG_TAG + "parse", "codeDepartment is " + this.codeDepartment);
                this.codeCompany = dataMap.get("CODE_COMPANY");
                Log.i(LOG_TAG + "parse", "codeCompany is " + this.codeCompany);
                this.deviceBinding = !dataMap.get("DEVICEBINDING").equals("0");
                Log.i(LOG_TAG + "parse", "deviceBinding is " + this.deviceBinding);
            } else {
                Log.d(LOG_TAG + "parse", "dataMap is null");
            }
        } else {
            Log.d(LOG_TAG + "parse", "dataList is null");
        }
        return true;
    }

}
