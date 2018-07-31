package org.port.trade.activity;
/**
 * Created by 超悟空 on 2015/3/6.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.mobile.model.fragment.BaseIncludeWebFragment;
import org.port.trade.R;

/**
 * 常用功能的Activity
 *
 * @author 超悟空
 * @version 1.0 2015/3/6
 * @since 1.0
 */
public class CommonFunctionActivity extends ActionBarActivity {

    /**
     * 标题栏的标题文本
     */
    private TextView toolbarTitleTextView = null;

    /**
     * 用于显示常用功能的包含WebView的片段
     */
    private BaseIncludeWebFragment contentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_function);

        // 初始化布局
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {

        // 初始化标题栏
        initToolbar();

        // 初始化内容展示片段
        initFunctionContentFragment();
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        // 得到Toolbar标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // 得到标题文本控件
        toolbarTitleTextView = (TextView) findViewById(R.id.toolbar_title);

        // 关联ActionBar
        setSupportActionBar(toolbar);

        // 取消原actionBar标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 设置标题
        setTitle(R.string.action_common_function);

        // 显示后退
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 导航回父activity
                // goBackParentActivity();
                // 与返回键相同
                onBackPressed();
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

    /**
     * 初始化内容展示片段
     */
    private void initFunctionContentFragment() {
        // 获取内容片段
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_common_function_webView_fragment);

        // 转换为包含WebView的片段
        if (fragment instanceof BaseIncludeWebFragment) {
            contentFragment = (BaseIncludeWebFragment) fragment;
        }
    }

    @Override
    public void onBackPressed() {
        // 如果网页可以后退则优先执行网页后退
        if (contentFragment == null || !contentFragment.onGoBack()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        // 设置标题
        toolbarTitleTextView.setText(title);
    }
}
