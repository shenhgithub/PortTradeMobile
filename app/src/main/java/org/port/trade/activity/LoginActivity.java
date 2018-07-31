package org.port.trade.activity;
/**
 * Created by 超悟空 on 2015/1/22.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.androidpn.client.ServiceManager;
import org.mobile.model.work.WorkBack;
import org.mobile.util.ContextUtil;
import org.port.trade.R;
import org.port.trade.util.Config;
import org.port.trade.util.MemoryBroadcast;
import org.port.trade.util.MemoryConfig;
import org.port.trade.work.CheckLogin;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 登录界面
 *
 * @author 超悟空
 * @version 1.0 2015/1/22
 * @since 1.0
 */
public class LoginActivity extends Activity {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "LoginActivity.";

    /**
     * 用户名编辑框
     */
    private EditText userNameEditText = null;

    /**
     * 密码编辑框
     */
    private EditText passwordEditText = null;

    /**
     * 保存密码复选框
     */
    private CheckBox loginSaveCheck = null;

    /**
     * 自动登陆复选框
     */
    private CheckBox loginAutoCheck = null;

    /**
     * 进度条
     */
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        // 重置用户参数
        MemoryConfig.getConfig().Reset();

        // 发送登录状态改变广播
        MemoryBroadcast.sendBroadcast(ContextUtil.getContext(), MemoryBroadcast.MEMORY_STATE_LOGIN);

        // 初始化界面
        init();
    }

    /**
     * 初始化界面
     */
    private void init() {
        // 初始化复选框
        initCheck();
        // 初始化编辑框
        initEdit();
    }

    /**
     * 初始化复选框
     */
    private void initCheck() {
        // 获取复选框
        loginSaveCheck = (CheckBox) findViewById(R.id.loginSave);
        loginAutoCheck = (CheckBox) findViewById(R.id.loginAuto);

        // 设置复选框初状态
        loginSaveCheck.setChecked(Config.getConfig().isLoginSave());
        loginAutoCheck.setChecked(Config.getConfig().isLoginAuto());

        // 设置监听器使两个复选框联动
        loginSaveCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    loginAutoCheck.setChecked(false);
                }
            }
        });

        loginAutoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loginSaveCheck.setChecked(true);
                }
            }
        });
    }

    /**
     * 初始化编辑框
     */
    private void initEdit() {
        // 文本框初始化
        userNameEditText = (EditText) findViewById(R.id.userName);
        passwordEditText = (EditText) findViewById(R.id.password);

        // 尝试填充数据
        if (Config.getConfig().getUserName() != null) {
            // 填充用户
            userNameEditText.setText(Config.getConfig().getUserName());

            if (loginSaveCheck.isChecked()) {
                // 记住密码状态，填充密码
                passwordEditText.setText(Config.getConfig().getPassword());
            } else {
                // 让密码框拥有焦点
                setSoftInput(passwordEditText);
            }
        } else {
            // 让用户名框拥有焦点
            setSoftInput(userNameEditText);
        }
    }

    /**
     * 设置指定编辑框获取焦点并弹出软键盘
     *
     * @param editText 要获取焦点的编辑框
     */
    private void setSoftInput(final EditText editText) {

        // 获取焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        // 延迟弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 600);
    }

    /**
     * 登录按钮
     *
     * @param view 按钮
     */
    public void LoginButton(View view) {

        // 获取用户名和密码
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // 判断是否输入用户名和密码
        if (userName.length() == 0 || password.length() == 0) {
            return;
        }

        // 进行登录验证
        CheckLogin login = new CheckLogin();

        // 设置回调监听
        login.setWorkBackListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                // 关闭进度条
                progressDialog.cancel();

                if (state) {
                    // 登录成功

                    // 判断是否已绑定设备
                    if (MemoryConfig.getConfig().isDeviceBinding()) {
                        // 启动推送通知
                        initNotify();
                    }

                    // 保存当前设置
                    Config config = Config.getConfig();
                    config.setLoginAuto(loginAutoCheck.isChecked());
                    config.setLoginSave(loginSaveCheck.isChecked());

                    // 检查是否要保存用户名和密码
                    if (loginSaveCheck.isChecked()) {
                        config.setUserName(userNameEditText.getText().toString());
                        config.setPassword(passwordEditText.getText().toString());
                    }

                    // 保存设置
                    config.Save();

                    LoginActivity.this.finish();
                } else {
                    // 登录失败
                    Dialog dialog = new Dialog(LoginActivity.this);
                    dialog.setTitle(data);
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }
        });

        // 打开旋转进度条
        startProgressDialog();

        // 执行登录任务
        login.beginExecute(userName, password);

    }

    /**
     * 打开进度条
     */
    private void startProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置提醒
        progressDialog.setMessage(getString(R.string.login_loading));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 启动推送通知
     */
    private void initNotify() {
        Log.i(LOG_TAG + "initNotify", "android push open");
        // 启动推送服务
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.ic_launcher);
        serviceManager.startService();
    }
}
