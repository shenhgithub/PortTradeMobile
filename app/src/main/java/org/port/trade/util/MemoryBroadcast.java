package org.port.trade.util;
/**
 * Created by 超悟空 on 2015/1/31.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * 运行时临时数据监听的广播工具，
 * 用于当{@link org.port.trade.util.MemoryConfig}中数据变化时发送通知
 *
 * @author 超悟空
 * @version 1.0 2015/1/31
 * @since 1.0
 */
public class MemoryBroadcast {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MemoryBroadcast.";

    /**
     * 全部参数状态
     */
    public static final String MEMORY_STATE_ALL = "org.port.trade.util.MemoryConfig.all";

    /**
     * 登录状态
     */
    public static final String MEMORY_STATE_LOGIN = "org.port.trade.util.MemoryConfig.login";

    /**
     * 用户标识状态
     */
    public static final String MEMORY_STATE_USER_ID = "org.port.trade.util.MemoryConfig.userID";

    /**
     * 部门标识状态
     */
    public static final String MEMORY_STATE_CODE_DEPARTMENT = "org.port.trade.util.MemoryConfig.codeDepartment";

    /**
     * 公司标识状态
     */
    public static final String MEMORY_STATE_CODE_COMPANY = "org.port.trade.util.MemoryConfig.codeCompany";

    /**
     * 设备绑定状态
     */
    public static final String MEMORY_STATE_DEVICE_BINDING = "org.port.trade.util.MemoryConfig.deviceBinding";

    /**
     * 应用版本状态
     */
    public static final String MEMORY_STATE_VERSION = "org.port.trade.util.MemoryConfig.latestVersion";

    /**
     * 消息列表请求状态
     */
    public static final String MEMORY_STATE_MESSAGE_REQUEST_STATE = "org.port.trade.util.MemoryConfig.messageRequestState";

    /**
     * 发送广播
     *
     * @param context 上下文
     * @param action  动作字符串
     */
    public static void sendBroadcast(Context context, String action) {
        Log.i(LOG_TAG + "sendBroadcast", "send action is " + action);
        sendBroadcast(context, new Intent(action));
    }

    /**
     * 发送广播
     *
     * @param context 上下文
     * @param intent  包含一组动作字符串的意图
     */
    public static void sendBroadcast(Context context, Intent intent) {
        Log.i(LOG_TAG + "sendBroadcast", "send intent");
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        lbm.sendBroadcast(intent);
    }
}
