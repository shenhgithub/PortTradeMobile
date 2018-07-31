package org.port.trade.work;
/**
 * Created by 超悟空 on 2015/3/23.
 */

import android.util.Log;

import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.port.trade.data.PasswordChangeData;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.NetworkType;
import org.port.trade.util.communication.CommunicationFactory;

/**
 * 修改密码任务类
 *
 * @author 超悟空
 * @version 1.0 2015/3/23
 * @since 1.0
 */
public class ChangePassword extends WorkModel<String, String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "ChangePassword.";

    @Override
    protected boolean onDoWork(String... parameters) {
        if (parameters == null || parameters.length < 2) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "password is null");
            return false;
        }

        // 新建http get请求的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.HTTP_GET);

        // 新建密码数据对象
        PasswordChangeData data = new PasswordChangeData();

        // 传入参数
        data.setUserID(MemoryConfig.getConfig().getUserID());
        data.setOldPassword(parameters[0]);
        data.setNewPassword(parameters[1]);

        // 设置调用的方法名
        communication.setTaskName("ChangePassword.aspx");
        Log.i(LOG_TAG + "onDoWork", "task name is ChangePassword.aspx");

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (data.parse(communication.Response())) {
            // 执行成功
            Log.i(LOG_TAG + "onDoWork", "change password success");
            // 设置回显消息
            setResult(data.getMessage());
            return true;
        } else {
            // 执行失败
            Log.i(LOG_TAG + "onDoWork", "change password failed");
            // 设置回显消息
            setResult(data.getMessage());
            return false;
        }
    }
}
