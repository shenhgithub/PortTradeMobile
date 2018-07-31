package org.port.trade.work;
/**
 * Created by 超悟空 on 2015/3/24.
 */

import android.util.Log;

import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.port.trade.data.PersonalInfo;
import org.port.trade.data.PersonalInfoChangeData;
import org.port.trade.util.NetworkType;
import org.port.trade.util.communication.CommunicationFactory;

/**
 * 修改个人信息
 *
 * @author 超悟空
 * @version 1.0 2015/3/24
 * @since 1.0
 */
public class ChangePersonalInfo extends WorkModel<PersonalInfo, String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "ChangePersonalInfo.";

    @Override
    protected boolean onDoWork(PersonalInfo... parameters) {
        if (parameters == null || parameters.length < 1) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "message is null");
            return false;
        }

        // 新建http get请求的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.HTTP_GET);

        // 新建个人信息修改数据对象
        PersonalInfoChangeData data = new PersonalInfoChangeData();

        // 传入参数
        data.setPersonalInfo(parameters[0]);

        // 设置调用的方法名
        communication.setTaskName("UpdatePersonDetailData.aspx");
        Log.i(LOG_TAG + "onDoWork", "task name is UpdatePersonDetailData.aspx");

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (data.parse(communication.Response())) {
            // 执行成功
            Log.i(LOG_TAG + "onDoWork", "change personal info success");
            // 设置回显消息
            setResult(data.getMessage());
            return true;
        } else {
            // 执行失败
            Log.i(LOG_TAG + "onDoWork", "change personal info failed");
            // 设置回显消息
            setResult(data.getMessage());
            return false;
        }
    }
}
