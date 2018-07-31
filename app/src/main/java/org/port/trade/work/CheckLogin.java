package org.port.trade.work;
/**
 * Created by 超悟空 on 2015/1/24.
 */

import android.util.Log;

import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.mobile.util.ContextUtil;
import org.port.trade.data.LoginData;
import org.port.trade.util.MemoryBroadcast;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.NetworkType;
import org.port.trade.util.communication.CommunicationFactory;

/**
 * 登录检查任务类
 *
 * @author 超悟空
 * @version 1.0 2015/1/24
 * @since 1.0
 */
public class CheckLogin extends WorkModel<String, String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CheckLogin.";

    @Override
    protected boolean onDoWork(String... parameters) {
        if (parameters == null || parameters.length < 2 || parameters[0] == null || parameters[1] == null) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "userName or password is null");
            // 设置失败原因
            setResult("userName or password is null");
            // 发送登录状态改变广播
            MemoryBroadcast.sendBroadcast(ContextUtil.getContext(), MemoryBroadcast.MEMORY_STATE_LOGIN);
            return false;
        }

        // 新建带有服务器公钥的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.WEBSERVICE_HAS_PUBLIC_KEY);

        // 新建登录数据对象
        LoginData data = new LoginData();
        data.setUserName(parameters[0]);
        data.setPassword(parameters[1]);

        // 设置调用的方法名
        communication.setTaskName("GetLogin");
        Log.i(LOG_TAG + "onDoWork", "task name is GetLogin");

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (!data.parse(communication.Response()) || !data.isLogin()) {
            // 解析失败或登录失败
            // 设置失败原因
            setResult(data.getMessage());
            // 发送登录状态改变广播
            MemoryBroadcast.sendBroadcast(ContextUtil.getContext(), MemoryBroadcast.MEMORY_STATE_LOGIN);
            return false;
        } else {
            // 登录成功的处理
            // 设置回显结果
            setResult(data.getMessage());
            // 设置全局临时参数
            MemoryConfig config = MemoryConfig.getConfig();
            config.setLogin(true);
            config.setUserID(data.getUserID());
            config.setCodeDepartment(data.getCodeDepartment());
            config.setCodeCompany(data.getCodeCompany());
            config.setDeviceBinding(data.isDeviceBinding());

            // 发送登录状态改变广播
            MemoryBroadcast.sendBroadcast(ContextUtil.getContext(), MemoryBroadcast.MEMORY_STATE_LOGIN);
            return true;
        }
    }
}
