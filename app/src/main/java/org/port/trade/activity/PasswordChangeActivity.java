package org.port.trade.activity;
/**
 * Created by 超悟空 on 2015/3/23.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.mobile.model.work.WorkBack;
import org.port.trade.R;
import org.port.trade.function.dialog.SimpleDialog;
import org.port.trade.util.Config;
import org.port.trade.util.MemoryConfig;
import org.port.trade.work.ChangePassword;

/**
 * 密码修改Activity
 *
 * @author 超悟空
 * @version 1.0 2015/3/23
 * @since 1.0
 */
public class PasswordChangeActivity extends ActionBarActivity {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PasswordChangeActivity.";

    /**
     * 标题栏的标题文本
     */
    private TextView toolbarTitleTextView = null;

    /**
     * 进度条
     */
    private ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        // 加载界面
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 初始化Toolbar
        initToolbar();

        // 初始化修改确认按钮
        initButton();
    }

    /**
     * 初始化修改确认按钮
     */
    private void initButton() {

        // 获取确认按钮
        Button changePasswordButton = (Button) findViewById(R.id.activity_password_change_button);

        // 设置点击事件
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行修改
                doChangePassword();
            }
        });
    }

    /**
     * 确认修改密码操作
     */
    private void doChangePassword() {
        Log.i(LOG_TAG + "doChangePassword", "change password start");

        // 获取原密码输入框
        EditText oldPasswordEditText = (EditText) findViewById(R.id.activity_password_change_old_password);

        // 获取新密码输入框
        EditText newPasswordEditText = (EditText) findViewById(R.id.activity_password_change_new_password);

        // 获取二次确认密码框
        EditText reenterPasswordEditText = (EditText) findViewById(R.id.activity_password_change_reenter_password);

        // 得到原密码字符串
        String oldPassword = oldPasswordEditText.getText().toString();
        Log.i(LOG_TAG + "doChangePassword", "old password is " + oldPassword);

        // 得到新密码字符串
        final String newPassword = newPasswordEditText.getText().toString();
        Log.i(LOG_TAG + "doChangePassword", "new password is " + newPassword);

        // 得到二次确认密码字符串
        String reenterPassword = reenterPasswordEditText.getText().toString();
        Log.i(LOG_TAG + "doChangePassword", "reenter password is " + reenterPassword);

        // 密码合法性校验
        if (!checkPassword(oldPassword, newPassword, reenterPassword)) {
            Log.i(LOG_TAG + "doChangePassword", "password is error");
            return;
        }

        // 新建修改密码任务
        ChangePassword changePassword = new ChangePassword();

        // 设置回调监听
        changePassword.setWorkBackListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                // 关闭进度条
                progressDialog.cancel();

                // 弹出结果提示窗
                SimpleDialog.showDialog(PasswordChangeActivity.this, data);

                if (state && Config.getConfig().isLoginSave()) {
                    // 修改成功且用户需要保存密码
                    // 保存新密码
                    Config.getConfig().setPassword(newPassword);
                    Config.getConfig().Save();
                }
            }
        });

        // 打开旋转进度条
        startProgressDialog();

        // 执行密码修改
        changePassword.beginExecute(oldPassword, newPassword);
    }

    /**
     * 密码合法性校验
     *
     * @param oldPassword     原密码
     * @param newPassword     新密码
     * @param reenterPassword 二次确认密码
     *
     * @return 合法返回true
     */
    private boolean checkPassword(String oldPassword, String newPassword, String reenterPassword) {

        // 判断是否已登录
        if (!MemoryConfig.getConfig().isLogin()) {
            Log.i(LOG_TAG + "checkPassword", "not login");
            // 弹出提示窗
            SimpleDialog.showDialog(this, getString(R.string.login_alert));
            return false;
        }

        // 判断是否输入原密码
        if (oldPassword.length() == 0) {
            Log.i(LOG_TAG + "checkPassword", "old password is null");
            // 弹出提示窗
            SimpleDialog.showDialog(this, getString(R.string.password_change_no_old_password));
            return false;
        }

        // 判断是否输入新密码
        if (newPassword.length() == 0) {
            Log.i(LOG_TAG + "checkPassword", "new password is null");
            // 弹出提示窗
            SimpleDialog.showDialog(this, getString(R.string.password_change_no_new_password));
            return false;
        }

        // 判断是否输入二次确认密码
        if (reenterPassword.length() == 0) {
            Log.i(LOG_TAG + "checkPassword", "reenter password is null");
            // 弹出提示窗
            SimpleDialog.showDialog(this, getString(R.string.password_change_no_reenter_password));
            return false;
        }

        // 判断新旧密码是否相同
        if (newPassword.equals(oldPassword)) {
            // 新旧密码相同
            Log.i(LOG_TAG + "checkPassword", "same old password new password");
            // 弹出提示窗
            SimpleDialog.showDialog(this, getString(R.string.password_change_same));
            return false;
        }

        // 判断两次新密码是否相同
        if (!newPassword.equals(reenterPassword)) {
            // 两次输入不相同
            Log.i(LOG_TAG + "checkPassword", "double password is different");
            // 弹出提示窗
            SimpleDialog.showDialog(this, getString(R.string.password_change_different));
            return false;
        }

        return true;
    }

    /**
     * 打开进度条
     */
    private void startProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置提醒
        progressDialog.setMessage(getString(R.string.password_change_loading));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        // 得到Toolbar标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 得到标题文本
        toolbarTitleTextView = (TextView) findViewById(R.id.toolbar_title);

        // 关联ActionBar
        setSupportActionBar(toolbar);

        // 取消原actionBar标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 设置标题
        setTitle(R.string.mine_function_password_change);

        // 显示后退
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 导航回父activity
                goBackParentActivity();
            }
        });
    }

    /**
     * 导航回父activity
     */
    private void goBackParentActivity() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
        } else {
            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        // 设置标题
        toolbarTitleTextView.setText(title);
    }
}
