package org.port.trade.activity;
/**
 * Created by 悟空 on 2015/2/28.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.port.trade.adapter.FunctionItemDivider;
import org.port.trade.adapter.FunctionItemViewHolder;
import org.port.trade.adapter.FunctionRecyclerViewAdapter;
import org.mobile.model.operate.OnItemClickListenerForRecyclerViewItem;
import org.port.trade.R;
import org.port.trade.factory.BaseFunctionFactory;
import org.port.trade.factory.FunctionCarOwnerFactory;
import org.port.trade.factory.FunctionCargoAgencyFactory;
import org.port.trade.factory.FunctionCargoOwnerFactory;
import org.port.trade.factory.FunctionFreightFactory;
import org.port.trade.factory.FunctionPublicFactory;
import org.port.trade.factory.FunctionShippingAgencyFactory;
import org.port.trade.function.dialog.NoNetworkDialog;
import org.port.trade.scan.activity.CaptureActivity;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.StaticValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能二级列表界面
 *
 * @author 超悟空
 * @version 1.0 2015/2/28
 * @since 1.0
 */
public class FunctionListActivity extends ActionBarActivity {

    /**
     * 日志前缀
     */
    private static final String LOG_TAG = "FunctionListActivity.";

    /**
     * 标题栏的标题文本
     */
    private TextView toolbarTitleTextView = null;

    /**
     * 功能项的数据适配器
     */
    private FunctionRecyclerViewAdapter adapter = null;

    /**
     * 要加载的功能项
     */
    private List<Map<String, Object>> functionDataList = null;

    /**
     * 当前加载的功能ID
     */
    private int functionId = 0;

    /**
     * 当前加载的功能名称
     */
    private String functionTag = null;

    /**
     * 扫描结果
     */
    private String scanResult = null;

    /**
     * 标题栏的扫描结果文本
     */
    private MenuItem scanMenuItem = null;

    /**
     * 具体功能项实例工厂
     */
    private BaseFunctionFactory functionFactory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_list);

        // 确定要加载的功能和功能项
        setFunction();

        // 初始化布局
        initView();
    }

    /**
     * 确定要加载的功能和功能项
     */
    private void setFunction() {
        // 得到功能ID
        this.functionId = getIntent().getIntExtra(StaticValue.FUNCTION_BUTTON_ID, this.functionId);

        // 要加载的功能项
        functionDataList = null;

        // 选择并加载对应功能数据
        switch (functionId) {
            case 1:
                // 货代
                functionTag = getString(R.string.function_tag_1);
                functionFactory = FunctionCargoAgencyFactory.getFactory();
                functionDataList = getFunctionDataList(R.array.function_1_title, R.drawable
                        .function_item_1);
                break;
            case 2:
                // 船代
                functionTag = getString(R.string.function_tag_2);
                functionFactory = FunctionShippingAgencyFactory.getFactory();
                functionDataList = getFunctionDataList(R.array.function_2_title, R.drawable
                        .function_item_2);
                break;
            case 3:
                // 货主
                functionTag = getString(R.string.function_tag_3);
                functionFactory = FunctionCargoOwnerFactory.getFactory();
                functionDataList = getFunctionDataList(R.array.function_3_title, R.drawable
                        .function_item_3);
                break;
            case 4:
                // 货运
                functionTag = getString(R.string.function_tag_4);
                functionFactory = FunctionFreightFactory.getFactory();
                functionDataList = getFunctionDataList(R.array.function_4_title, R.drawable
                        .function_item_4);
                break;
            case 5:
                // 车主
                functionTag = getString(R.string.function_tag_5);
                functionFactory = FunctionCarOwnerFactory.getFactory();
                functionDataList = getFunctionDataList(R.array.function_5_title, R.drawable
                        .function_item_5);
                break;
            case 6:
                // 公共
                functionTag = getString(R.string.function_tag_6);
                functionFactory = FunctionPublicFactory.getFactory();
                functionDataList = getFunctionDataList(R.array.function_6_title, R.drawable
                        .function_item_6);
                break;
            default:
                // 功能不存在
                Log.d(LOG_TAG + "setFunction", "functionId is null");
                break;
        }

        Log.i(LOG_TAG + "setFunction", "now function is " + functionTag);

        // 初始化功能项数据适配器
        if (functionDataList != null) {
            adapter = new FunctionRecyclerViewAdapter(functionDataList);
        }
    }

    /**
     * 获取功能项数据列表
     *
     * @param attrsId 功能标题数组ID
     * @param imageId Item图片ID
     *
     * @return 功能项数据列表
     */
    private List<Map<String, Object>> getFunctionDataList(int attrsId, int imageId) {
        // 加载功能项
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 功能项标题数组
        String[] functionTitle = getResources().getStringArray(attrsId);

        for (String nowTitle : functionTitle) {
            // 新建一个功能项标签
            Map<String, Object> function = new HashMap<>();

            // 添加功能项图片
            function.put(StaticValue.FUNCTION_ITEM_IMAGE_ID, imageId);
            // 添加功能项标题
            function.put(StaticValue.FUNCTION_ITEM_TITLE, nowTitle);

            // 将标签加入数据源
            dataList.add(function);
        }

        Log.i(LOG_TAG + "getFunctionDataList", "function count is " + dataList.size());

        return dataList;
    }

    /**
     * 初始化布局
     */
    private void initView() {

        // 初始化标题栏
        initToolbar();

        // 初始化RecyclerView布局
        initRecyclerView();
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

        // 设置标题为当前功能
        setTitle(functionTag);

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

    /**
     * 初始化RecyclerView布局
     */
    private void initRecyclerView() {
        // 加载列表布局

        // 功能RecyclerView列表对象
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.function_recyclerView);

        // 设置分割线
        recyclerView.addItemDecoration(new FunctionItemDivider(this));

        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 创建列表的布局管理器
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        // 设置为固定容量
        recyclerView.setHasFixedSize(true);

        // 设置监听器
        if (adapter != null) {
            adapter.setOnItemClickListener(new OnItemClickListenerForRecyclerViewItem<List<Map<String, Object>>, FunctionItemViewHolder>() {
                @Override
                public void onClick(List<Map<String, Object>> dataSource, FunctionItemViewHolder
                        holder) {

                    Log.i(LOG_TAG + "adapter.onClick", "position is " + holder.getPosition());
                    Log.i(LOG_TAG + "adapter.onClick", "function title is " + holder
                            .functionTitle.getText());

                    // 检测网络状态
                    if (!MemoryConfig.getConfig().isOpenNetwork()) {
                        // 无网络显示提示
                        NoNetworkDialog.showNoNetworkDialog(FunctionListActivity.this);
                        return;
                    }
                    jumpFunctionActivity(holder.functionTitle.getText().toString(), holder
                            .getPosition());


                }
            });
        }

        // 添加数据适配器
        recyclerView.setAdapter(adapter);
    }

    /**
     * 跳转到功能界面
     *
     * @param functionName 选中的功能名
     * @param position     选中的功能位置
     */
    private void jumpFunctionActivity(String functionName, int position) {
        // 检测是否需要扫码
        if (functionFactory.isMustScan(position) && scanResult == null) {
            startActivityForResult(new Intent(FunctionListActivity.this, CaptureActivity.class),
                    functionId);
        } else {

            // 一个跳转意图
            Intent intent = getJumpIntent(functionName, position);

            // 跳转
            FunctionListActivity.this.startActivity(intent);
        }
    }

    /**
     * 获取跳转意图
     *
     * @param functionName 选中的功能名
     * @param position     选中的功能位置
     *
     * @return 组织完毕的意图
     */
    private Intent getJumpIntent(String functionName, int position) {
        // 一个跳转意图
        Intent intent;

        // 检测是否需要登录
        if (functionFactory.isMustLogin(position) && !MemoryConfig.getConfig().isLogin()) {
            // 需要登录但未登录则跳转到登录界面
            intent = getLoginIntent();
            return intent;
        }
        // 不需要登录或已登录则跳转到功能界面
        intent = getFunctionIntent(functionName, position);
        return intent;
    }

    /**
     * 获取要跳转到查询功能的意图
     *
     * @param functionName 选中的功能名
     * @param position     选中的功能位置
     *
     * @return 组织完毕的意图
     */
    private Intent getFunctionIntent(String functionName, int position) {
        // 新建跳转意图
        Intent intent = new Intent(this, FunctionContentActivity.class);

        // 得到要加载的功能查询布局片段
        Fragment fragment = functionFactory.instantiateFunctionSearchFragment(position);

        // 尝试传入参数
        if (scanResult != null) {
            Bundle bundle = fragment.getArguments();
            if (bundle != null) {
                bundle.putString(StaticValue.EXTERNAL_NUMBER_TAG, scanResult);
            }
        }

        // 将要传递的布局片段对象引用保留到全局栈
        MemoryConfig.getConfig().setFunctionSearchFragment(fragment);

        // 传入当前功能名
        intent.putExtra(StaticValue.FUNCTION_ITEM_TITLE, functionName);
        return intent;
    }

    /**
     * 获取登录跳转意图，未登录状态需跳转到登录界面
     *
     * @return 组织完毕的意图
     */
    private Intent getLoginIntent() {
        // 新建跳转意图
        return new Intent(this, LoginActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (this.functionId == 5) {
            getMenuInflater().inflate(R.menu.menu_car, menu);
            scanMenuItem = menu.findItem(R.id.result_text);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id) {
            case R.id.scan:
                // 扫码
                startActivityForResult(new Intent(this, CaptureActivity.class), this.functionId);
                break;
            case R.id.result_text:
                // 港通卡信息
                jumpFunctionActivity((String) functionDataList.get(7).get(StaticValue
                        .FUNCTION_ITEM_TITLE), 7);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();

            String resultString = bundle.getString(CaptureActivity.SCAN_RESULT_TAG);
            Log.i(LOG_TAG + "onActivityResult", "result string is " + resultString);
            if (resultString != null) {
                int start = resultString.lastIndexOf("info=");
                if (start >= 0) {
                    this.scanResult = resultString.substring(start + 5, start + 6 + 5);
                    Log.i(LOG_TAG + "onActivityResult", "scan result is " + scanResult);
                }
            }

            if (this.scanResult != null && this.scanResult.length() > 0) {
                this.scanMenuItem.setTitle(this.scanResult);
                this.scanMenuItem.setVisible(true);
            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /**
         * 在onCreateOptionsMenu执行后，菜单被显示前调用；如果菜单已经被创建，则在菜单显示前被调用。 同样的，
         * 返回true则显示该menu,false 则不显示; （可以通过此方法动态的改变菜单的状态，比如加载不同的菜单等） TODO
         * Auto-generated method stub
         */
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        // 设置标题
        toolbarTitleTextView.setText(title);
    }
}
