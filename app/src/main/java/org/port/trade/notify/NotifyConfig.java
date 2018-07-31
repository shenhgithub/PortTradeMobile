package org.port.trade.notify;
/**
 * Created by 超悟空 on 2015/1/27.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.githang.android.apnbb.Constants;
import com.githang.android.apnbb.UUIDUtil;

import org.mobile.util.ContextUtil;

/**
 * 保存推送工具的各项参数
 *
 * @author 超悟空
 * @version 1.0 2015/1/27
 * @since 1.0
 */
public class NotifyConfig {

    /**
     * 自身静态对象
     */
    private static NotifyConfig notifyConfig = null;

    /**
     * 配置持久化工具
     */
    private SharedPreferences sharedPrefs = null;

    /**
     * 推送注册的用户名
     */
    private String notifyUserName = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    private NotifyConfig(Context context) {
        // 得到androidpn的配置文件
        sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        init(context);
    }

    /**
     * 数据初始化
     *
     * @param context 上下文
     */
    private void init(Context context) {
        if (sharedPrefs != null) {
            // 推送注册的用户名
            notifyUserName = sharedPrefs.getString(Constants.XMPP_USERNAME, UUIDUtil.getID(context));
        }
    }

    /**
     * 获取推送参数对象
     *
     * @return 全局对象
     */
    public static NotifyConfig getNotifyConfig() {
        if (notifyConfig == null) {
            notifyConfig = new NotifyConfig(ContextUtil.getContext());
        }
        return notifyConfig;
    }

    /**
     * 获取推送注册的用户名
     *
     * @return 36位16进制串，带有4个"-"，没有则返回null
     */
    public String getNotifyUserName() {
        return notifyUserName;
    }
}
