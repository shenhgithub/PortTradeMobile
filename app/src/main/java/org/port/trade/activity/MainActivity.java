package org.port.trade.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.mobile.common.function.CheckUpdate;
import org.mobile.common.view.SlidingTabLayout;
import org.mobile.model.operate.BackHandle;
import org.mobile.model.operate.TitleHandle;
import org.mobile.util.ApplicationVersion;
import org.port.trade.R;
import org.port.trade.adapter.ViewPagerAdapter;
import org.port.trade.util.StaticValue;


public class MainActivity extends ActionBarActivity {

    /**
     * 版本更新提示延迟时间
     */
    private static final long UPDATE_TIME = 2000;

    /**
     * 标题栏的标题文本
     */
    private TextView toolbarTitleTextView = null;

    /**
     * 滑动分页
     */
    private ViewPager viewPager = null;

    /**
     * ViewPager的Fragment适配器
     */
    private ViewPagerAdapter viewPagerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 加载界面
        initView();
        // 新版本提示
        showUpdate();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 初始化Toolbar
        initToolbar();
        // 初始化ViewPager和底部导航栏
        initViewPagerAndTab();
    }

    /**
     * 新版本提示
     */
    private void showUpdate() {

        if (!ApplicationVersion.getVersionManager().isLatestVersion()) {
            // 存在新版本，进行提示
            Handler handler = new Handler();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    CheckUpdate checkUpdate = new CheckUpdate(MainActivity.this, StaticValue.APP_CODE);
                    checkUpdate.checkInBackground();
                }
            };
            handler.postDelayed(runnable, UPDATE_TIME);
        }
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
    }

    /**
     * 初始化ViewPager和底部导航栏
     */
    private void initViewPagerAndTab() {
        // 底部导航栏
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(viewPagerAdapter);

        // 设置SlidingTab的自定义标签布局
        slidingTabLayout.setCustomTabView(R.layout.tab_item, R.id.tab_textView, R.id.tab_imageView, viewPagerAdapter.getImageIds());

        // 设置底部滚动条颜色
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.toolbar_color));

        // 设置页切换监听器
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 变更标题
                setTitle(getChangedPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 装载ViewPager
        slidingTabLayout.setViewPager(viewPager);
    }

    /**
     * 获取页切换时变更的标题
     *
     * @param position 页序号
     *
     * @return 标题字符串
     */
    private CharSequence getChangedPageTitle(int position) {

        // 优先尝试使用嵌套的子布局提供的标题
        // 获取当前显示的对象
        Object currentObject = viewPagerAdapter.instantiateItem(viewPager, position);
        // 判断是否实现了标题操作接口
        if (currentObject instanceof TitleHandle) {
            // 转换为标题操作接口
            TitleHandle titleHandle = (TitleHandle) currentObject;
            // 获取标题
            String title = titleHandle.getTitle();
            if (title != null && title.length() > 0) {
                // 不为空则返回该标题
                return title;
            }
        }

        // 如果子布局标题获取失败则使用底部导航栏标题
        return viewPagerAdapter.getPageTitle(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // 获取当前显示的对象
        Object currentObject = viewPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());

        // 判断是否实现了返回操作接口
        if (currentObject instanceof BackHandle) {
            // 转换为返回操作接口
            BackHandle backHandled = (BackHandle) currentObject;
            // 执行返回动作
            if (!backHandled.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
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
