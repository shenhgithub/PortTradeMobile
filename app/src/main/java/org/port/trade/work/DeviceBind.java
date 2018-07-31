package org.port.trade.work;
/**
 * Created by 超悟空 on 2015/1/29.
 */

import android.util.Log;

import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.mobile.util.ContextUtil;
import org.port.trade.data.DeviceBindData;
import org.port.trade.util.MemoryBroadcast;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.NetworkType;
import org.port.trade.util.communication.CommunicationFactory;

/**
 * 设备绑定解绑任务类
 *
 * @author 超悟空
 * @version 1.0 2015/1/29
 * @since 1.0
 */
public class DeviceBind extends WorkModel<Boolean, Boolean> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DeviceBind.";

    @Override
    protected boolean onDoWork(Boolean[] parameters) {

        if (parameters == null || parameters.length < 1) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "binding state is null");
            return false;
        }

        // 新建http get请求的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.HTTP_GET);

        // 新建绑定数据对象
        DeviceBindData data = new DeviceBindData();

        // 传入参数
        data.setUserID(MemoryConfig.getConfig().getUserID());
        data.setBinding(parameters[0]);

        // 设置调用的方法名
        communication.setTaskName("devicebinding.aspx");
        Log.i(LOG_TAG + "onDoWork", "task name is devicebinding.aspx");

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (data.parse(communication.Response())) {
            // 执行成功
            Log.i(LOG_TAG + "onDoWork", "binding success");
            // 改变绑定状态
            MemoryConfig.getConfig().setDeviceBinding(!MemoryConfig.getConfig().isDeviceBinding());

            // 发送设备绑定状态广播
            MemoryBroadcast.sendBroadcast(ContextUtil.getContext(), MemoryBroadcast.MEMORY_STATE_DEVICE_BINDING);
            return true;
        } else {
            // 执行失败
            Log.i(LOG_TAG + "onDoWork", "binding failed");
            // 发送设备绑定状态广播
            MemoryBroadcast.sendBroadcast(ContextUtil.getContext(), MemoryBroadcast.MEMORY_STATE_DEVICE_BINDING);
            return false;
        }
    }
}
