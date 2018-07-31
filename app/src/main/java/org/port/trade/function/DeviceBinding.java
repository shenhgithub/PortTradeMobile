package org.port.trade.function;
/**
 * Created by 超悟空 on 2015/2/2.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import org.mobile.model.work.WorkBack;
import org.port.trade.R;
import org.port.trade.util.MemoryConfig;
import org.port.trade.work.DeviceBind;

/**
 * 设备绑定解绑功能类
 *
 * @author 超悟空
 * @version 1.0 2015/2/2
 * @since 1.0
 */
public class DeviceBinding {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DeviceBinding.";

    /**
     * 上下文
     */
    private Context context = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public DeviceBinding(Context context) {
        this.context = context;
    }

    /**
     * 执行功能
     */
    public void invoke() {
        Log.i(LOG_TAG + "invoke", "device binding is invoked");

        if (!MemoryConfig.getConfig().isLogin()) {
            // 未登录
            Dialog dialog = new Dialog(context);
            dialog.setTitle(R.string.login_alert);
            dialog.setCancelable(true);
            dialog.show();
            return;
        }


        // 弹出确认对话框
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (MemoryConfig.getConfig().isDeviceBinding()) {
            // 设备当前已绑定，显示解绑提示
            dialog.setTitle(R.string.device_unbinding_title).setMessage(R.string.device_unbinding_alert);
        } else {
            // 设备未绑定，显示绑定提示
            dialog.setTitle(R.string.device_binding_title).setMessage(R.string.device_binding_alert);
        }

        // 设置确定监听器
        dialog.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 新建绑定任务
                DeviceBind deviceBind = new DeviceBind();

                // 新建旋转进度
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                if (MemoryConfig.getConfig().isDeviceBinding()) {
                    // 设备当前已绑定，显示解绑提示
                    progressDialog.setMessage(context.getString(R.string.device_unbinding_loading));
                } else {
                    // 设备未绑定，显示绑定提示
                    progressDialog.setMessage(context.getString(R.string.device_binding_loading));
                }
                progressDialog.setCancelable(true);

                // 设置任务结束回调
                deviceBind.setWorkBackListener(new WorkBack<Boolean>() {
                    @Override
                    public void doEndWork(boolean state, Boolean data) {
                        // 关闭进度条
                        progressDialog.cancel();
                        handleDeviceBindingResult(state);
                    }
                });

                // 显示旋转进度
                progressDialog.show();

                // 执行任务，参数为与当前相反的绑定状态
                deviceBind.beginExecute(!MemoryConfig.getConfig().isDeviceBinding());
            }
        });

        // 设置取消监听器，无操作
        dialog.setNegativeButton(R.string.button_cancel, null);

        // 显示提示框
        dialog.show();
    }

    /**
     * 绑定解绑操作结果处理
     *
     * @param state 执行结果
     */
    private void handleDeviceBindingResult(boolean state) {
        // 提示框
        Dialog dialog = new Dialog(context);

        // 判断任务执行结果
        if (state) {
            // 执行成功

            // 判断当前绑定解绑状态
            if (MemoryConfig.getConfig().isDeviceBinding()) {
                // 绑定成功
                dialog.setTitle(R.string.device_binding_success);
            } else {
                // 解绑成功
                dialog.setTitle(R.string.device_unbinding_success);
            }
        } else {
            // 执行失败
            // 判断计划执行的绑定解绑操作
            if (!MemoryConfig.getConfig().isDeviceBinding()) {
                // 绑定失败
                dialog.setTitle(R.string.device_binding_failed);
            } else {
                // 解绑失败
                dialog.setTitle(R.string.device_unbinding_failed);
            }
        }

        // 显示提示框
        dialog.setCancelable(true);
        dialog.show();
    }
}
