package org.port.trade.work;
/**
 * Created by 超悟空 on 2015/8/27.
 */

import android.util.Log;

import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.mobile.network.factory.CommunicationFactory;
import org.mobile.network.factory.NetworkType;
import org.port.trade.data.ScanInfoData;

import java.util.Map;

/**
 * 通过港通卡获取相应数据
 *
 * @author 超悟空
 * @version 1.0 2015/8/27
 * @since 1.0
 */
public class PullScanInfo extends WorkModel<String, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PullScanInfo.";

    @Override
    protected boolean onDoWork(String... parameters) {

        if (parameters == null || parameters.length < 2) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "number is null");
            return false;
        }

        // 新建http get请求的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.HTTP_GET);

        // 新建扫描信息数据对象
        ScanInfoData data = new ScanInfoData();
        // 传入参数
        data.setNumber(parameters[0]);

        // 设置调用的方法名
        communication.setTaskName(parameters[1]);
        Log.i(LOG_TAG + "onDoWork", "task name is " + parameters[1]);

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (data.parse(communication.Response())) {
            // 执行成功
            Log.i(LOG_TAG + "onDoWork", "get data success");
            // 设置结果数据
            setResult(data.getResultList());
            return true;
        } else {
            // 执行成功
            Log.i(LOG_TAG + "onDoWork", "get data download failed");
            return false;
        }
    }
}
