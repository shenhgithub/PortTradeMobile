package org.port.trade.activity;
/**
 * Created by 超悟空 on 2015/3/3.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.mobile.model.fragment.BaseIncludeWebFragment;
import org.mobile.model.operate.DataGetHandle;
import org.port.trade.R;
import org.port.trade.fragment.function.FunctionErrorSearchFragment;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.StaticValue;

/**
 * 功能三级内容界面
 *
 * @author 超悟空
 * @version 1.0 2015/3/3
 * @since 1.0
 */
public class FunctionContentActivity extends ActionBarActivity {

    /**
     * 日志前缀
     */
    private static final String LOG_TAG = "FunctionContentActivity.";

    /**
     * 标题栏的标题文本
     */
    private TextView toolbarTitleTextView = null;

    /**
     * 用于显示查询结果内容的包含WebView的片段
     */
    private BaseIncludeWebFragment contentFragment = null;

    /**
     * 保存功能查询布局片段的数据提供接口对象，用于提取查询网址
     */
    private DataGetHandle<String> functionSearchFragment = null;

    /**
     * 是否第一次加载本页面，默认为true
     */
    private boolean isFirstOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_content);

        // 初始化布局
        initView();

        // 将首次加载标记置为true
        isFirstOpen = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 判断是否为第一次加载完毕
        if (isFirstOpen) {
            // 默认执行一次查询
            searchButtonClick();
        }

        // 标记设为非第一次加载
        isFirstOpen = false;
    }

    /**
     * 初始化布局
     */
    private void initView() {

        // 初始化标题栏
        initToolbar();

        // 初始化功能查询片段
        initFunctionFragment();

        // 初始化内容展示片段
        initFunctionContentFragment();

    }

    /**
     * 初始化内容展示片段
     */
    private void initFunctionContentFragment() {
        // 获取内容片段
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_function_content_webView_fragment);

        // 转换为包含WebView的片段
        if (fragment instanceof BaseIncludeWebFragment) {
            contentFragment = (BaseIncludeWebFragment) fragment;
        }
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

        // 显示后退
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(R.drawable.back_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 导航回父activity
                goBackParentActivity();
                // 与返回键相同
                //onBackPressed();
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
     * 初始化功能查询片段
     */
    private void initFunctionFragment() {
        // 获取意图
        Intent intent = getIntent();

        // 提取数据
        // 提取当前功能标题，并设为标题栏标题
        setTitle(intent.getCharSequenceExtra(StaticValue.FUNCTION_ITEM_TITLE));

        // 片段管理器
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 提取功能的查询片段
        Fragment functionFragment = MemoryConfig.getConfig().getFunctionSearchFragment();

        // 替换默认片段为当前功能
        if (functionFragment != null) {
            // 功能布局存在
            Log.i(LOG_TAG + "initFunctionFragment", "function fragment is " + functionFragment.toString());
            fragmentManager.beginTransaction().replace(R.id.activity_function_content_search_frameLayout, functionFragment).commit();
            // 尝试转换为数据提供接口
            if (functionFragment instanceof DataGetHandle) {
                // 转为String泛型的数据提供接口
                //noinspection unchecked
                functionSearchFragment = (DataGetHandle<String>) functionFragment;
            }
        } else {
            // 功能布局不存在，加载一个错误提示
            Fragment errorFragment = new FunctionErrorSearchFragment();
            Log.d(LOG_TAG + "initFunctionFragment", "function not exist, load " + errorFragment.toString());
            fragmentManager.beginTransaction().replace(R.id.activity_function_content_search_frameLayout, errorFragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_function_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search:
                // 查询按钮
                Log.i(LOG_TAG + "onOptionsItemSelected", "click menu_search");
                searchButtonClick();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * 点击查询按钮，执行查询网页加载
     */
    private void searchButtonClick() {
        if (functionSearchFragment != null) {
            // 获取地址
            String url = functionSearchFragment.getData();

            if (url != null) {
                Log.i(LOG_TAG + "menu_search", "url is -->" + url);
                // 让WebView片段加载此url
                if (contentFragment != null) {
                    contentFragment.reloadUrl(url);
                }
            } else {
                Log.d(LOG_TAG + "menu_search", "url is null");
            }
        } else {
            Log.d(LOG_TAG + "menu_search", "functionSearchFragment is null");
        }
    }
}
