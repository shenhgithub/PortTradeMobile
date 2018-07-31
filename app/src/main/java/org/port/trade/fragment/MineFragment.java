package org.port.trade.fragment;
/**
 * Created by 超悟空 on 2015/1/24.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.mobile.common.function.CheckUpdate;
import org.mobile.model.operate.BackHandle;
import org.mobile.util.ApplicationVersion;
import org.port.trade.R;
import org.port.trade.activity.LoginActivity;
import org.port.trade.activity.PasswordChangeActivity;
import org.port.trade.activity.PersonalInfoActivity;
import org.port.trade.function.DeviceBinding;
import org.port.trade.function.dialog.NoNetworkDialog;
import org.port.trade.function.dialog.SimpleDialog;
import org.port.trade.notify.NotifySettingsActivity;
import org.port.trade.util.MemoryBroadcast;
import org.port.trade.util.MemoryConfig;
import org.port.trade.util.StaticValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的页的Fragment片段
 *
 * @author 超悟空
 * @version 1.0 2015/1/24
 * @since 1.0
 */
public class MineFragment extends Fragment implements AdapterView.OnItemClickListener, BackHandle {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MineFragment.";

    /**
     * 功能标题的取值标签，用于数据适配器中
     */
    private static final String FUNCTION_TITLE = "function_title";

    /**
     * 功能备注的取值标签，用于数据适配器中
     */
    private static final String FUNCTION_REMARK = "function_remark";

    /**
     * 功能图标的取值标签，用于数据适配器中
     */
    private static final String FUNCTION_IMAGE = "function_image";

    /**
     * 列表使用的数据适配器
     */
    private SimpleAdapter adapter = null;

    /**
     * 数据适配器的元数据
     */
    private List<Map<String, Object>> adapterDataList = null;

    /**
     * 本地广播管理器
     */
    private LocalBroadcastManager localBroadcastManager = null;

    /**
     * 全局临时变量改变的广播接收者
     */
    private MemoryConfigChangeReceiver memoryConfigChangeReceiver = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 当前功能片段布局
        View rootView = inflater.inflate(R.layout.mine_fragment, container, false);

        // 初始化功能布局
        initListView(rootView);
        // 注册接收者
        registerReceivers();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 注销接收者
        unregisterReceivers();
    }

    /**
     * 全局临时变量改变的广播接收者，
     * 用于实时改变本页的列表布局
     *
     * @author 超悟空
     * @version 1.0 2015/1/31
     * @since 1.0
     */
    private class MemoryConfigChangeReceiver extends BroadcastReceiver {

        /**
         * 得到本接收者监听的动作集合
         *
         * @return 填充完毕的意图集合
         */
        public final IntentFilter getRegisterIntentFilter() {
            // 新建动作集合
            IntentFilter filter = new IntentFilter();
            // 登录状态监听
            filter.addAction(MemoryBroadcast.MEMORY_STATE_LOGIN);
            // 设备绑定状态监听
            filter.addAction(MemoryBroadcast.MEMORY_STATE_DEVICE_BINDING);

            return filter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取动作字符串
            String action = intent.getAction();
            Log.i(LOG_TAG + "MemoryConfigChangeReceiver.onReceive", "action is " + action);

            switch (action) {
                case MemoryBroadcast.MEMORY_STATE_LOGIN:
                    // 登录状态的改变
                    // 设置登录按钮标题
                    changeLoginButton();
                    // 设置绑定按钮标题
                    changeDeviceBindingButton();
                    break;
                case MemoryBroadcast.MEMORY_STATE_DEVICE_BINDING:
                    // 设备绑定状态的改变
                    // 设置绑定按钮标题
                    changeDeviceBindingButton();
                    break;
            }

            // 通知改变
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 注册广播接收者
     */
    private void registerReceivers() {

        // 新建本地广播管理器
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());

        // 新建接收者
        memoryConfigChangeReceiver = new MemoryConfigChangeReceiver();

        // 注册接收者
        localBroadcastManager.registerReceiver(memoryConfigChangeReceiver, memoryConfigChangeReceiver.getRegisterIntentFilter());
    }

    /**
     * 注销广播接收者
     */
    private void unregisterReceivers() {

        if (localBroadcastManager == null) {
            return;
        }

        if (memoryConfigChangeReceiver != null) {
            localBroadcastManager.unregisterReceiver(memoryConfigChangeReceiver);
        }
    }

    /**
     * 初始化功能表格布局
     *
     * @param rootView 根布局
     */
    private void initListView(View rootView) {

        // 片段中的列表布局
        ListView listView = (ListView) rootView.findViewById(R.id.other_listView);

        // 获取列表元数据
        adapterDataList = getFunctionTitle();

        // 列表使用的数据适配器
        adapter = new SimpleAdapter(getActivity(), adapterDataList, R.layout.mine_function_item, new String[]{FUNCTION_TITLE , FUNCTION_REMARK , FUNCTION_IMAGE}, new int[]{R.id.mine_function_item_textView , R.id.mine_function_item_textView_right , R.id.mine_function_item_imageView});

        // 设置适配器
        listView.setAdapter(adapter);

        // 设置监听器
        listView.setOnItemClickListener(this);
    }

    /**
     * 生成功能项标签资源的数据源
     *
     * @return 返回SimpleAdapter适配器使用的数据源
     */
    private List<Map<String, Object>> getFunctionTitle() {
        // 加载功能项
        List<Map<String, Object>> dataList = new ArrayList<>();

        String[] functionTitle = getActivity().getResources().getStringArray(R.array.other_function_title);
        for (int i = 0; i < functionTitle.length; i++) {
            // 新建一个功能项标签
            Map<String, Object> function = new HashMap<>();

            // 添加标签资源
            // 添加标题
            function.put(FUNCTION_TITLE, functionTitle[i]);
            // 添加备注
            function.put(FUNCTION_REMARK, getRemark(i));
            // 添加图标
            function.put(FUNCTION_IMAGE, getLeftImage(i));

            // 将标签加入数据源
            dataList.add(function);
        }
        return dataList;
    }

    /**
     * 获取一个功能左侧的标识图标
     *
     * @param position 当前功能索引
     *
     * @return 图片的ID
     */
    private int getLeftImage(int position) {
        switch (position) {
            case 0:
                // 登录
                return R.drawable.login;
            case 1:
                // 绑定设备
                return R.drawable.device_binding;
            case 2:
                // 个人信息
                return R.drawable.personal_info;
            case 3:
                // 密码修改
                return R.drawable.password_change;
            case 4:
                // 检查更新
                return R.drawable.check_update;
            default:
                return R.drawable.ic_launcher;
        }
    }

    /**
     * 获取一个备注内容
     *
     * @param position 当前功能索引
     *
     * @return 备注内容串，没有返回null
     */
    private String getRemark(int position) {
        switch (position) {
            case 1:
                // 绑定设备
                return getDeviceBindingRemark();
            case 4:
                // 检查更新
                return getUpdateVersionRemark();
            case 5:
                // 关于
                //return getAboutRemark();
            default:
                return null;
        }
    }

    /**
     * 得到关于的备注
     *
     * @return 备注串
     */
    private String getAboutRemark() {
        try {
            // 包信息
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);

            // 返回版本名
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG + "getAboutRemark", "error message is " + e.getMessage());
            return null;
        }
    }

    /**
     * 得到检查更新备注
     *
     * @return 备注串
     */
    private String getUpdateVersionRemark() {
        if (ApplicationVersion.getVersionManager().isLatestVersion()) {
            // 最新版
            return getString(R.string.update_now_version_already_latest);
        } else {
            // 有新版
            return getString(R.string.update_now_version_has_latest);
        }
    }

    /**
     * 得到设备绑定备注
     *
     * @return 备注串
     */
    private String getDeviceBindingRemark() {
        if (!MemoryConfig.getConfig().isLogin()) {
            // 未登录
            return getString(R.string.not_login);
        } else {
            if (MemoryConfig.getConfig().isDeviceBinding()) {
                // 已绑定
                return getString(R.string.device_already_bound);
            } else {
                // 未绑定
                return getString(R.string.device_unbound);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // 设置登录按钮标题
        changeLoginButton();
        // 设置绑定按钮标题
        changeDeviceBindingButton();

        // 通知改变
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置绑定按钮标题
     */
    private void changeDeviceBindingButton() {

        // 新建一个功能项标签
        Map<String, Object> function = new HashMap<>();

        // 判断绑定状态
        if (!MemoryConfig.getConfig().isDeviceBinding()) {
            // 当前未绑定
            function.put(FUNCTION_TITLE, getActivity().getString(R.string.device_binding_title));
        } else {
            // 当前已绑定
            function.put(FUNCTION_TITLE, getActivity().getString(R.string.device_unbinding_title));
        }

        // 加入备注内容
        function.put(FUNCTION_REMARK, getDeviceBindingRemark());
        // 添加图标
        function.put(FUNCTION_IMAGE, R.drawable.device_binding);

        // 替换标签
        adapterDataList.set(1, function);
    }

    /**
     * 重设登录功能外观
     */
    private void changeLoginButton() {
        // 新建一个功能项标签
        Map<String, Object> function = new HashMap<>();

        // 判断登录状态
        if (MemoryConfig.getConfig().isLogin()) {
            // 已登录，更换登录按钮为登录后的内容
            // 添加标题
            function.put(FUNCTION_TITLE, getString(R.string.login_reset));

            // 添加图标
            function.put(FUNCTION_IMAGE, R.drawable.logout);
        } else {
            // 未登录，更换为登录按钮为未登录的内容
            // 添加标题
            function.put(FUNCTION_TITLE, getString(R.string.login_button));

            // 添加图标
            function.put(FUNCTION_IMAGE, R.drawable.login);
        }
        // 替换标签
        adapterDataList.set(0, function);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                // 登录
                doLogin();
                return;
            case 1:
                // 设备绑定或解绑
                doDeviceBinding();
                return;
            case 2:
                // 个人信息
                doPersonalInfo();
                return;
            case 3:
                // 密码修改
                doPasswordChange();
                return;
            case 4:
                // 检查更新
                doCheckUpdate();
                return;
            case 5:
                // 设置
                doSetting();
                return;
            case 6:
                // 退出
                doExit();
                return;
            default:
        }
    }

    /**
     * 执行个人信息操作
     */
    private void doPersonalInfo() {

        // 判断是否有网络
        if (!MemoryConfig.getConfig().isOpenNetwork()) {
            // 提示无网络
            NoNetworkDialog.showNoNetworkDialog(getActivity());
            return;
        }

        // 判断是否已登录
        if (!MemoryConfig.getConfig().isLogin()) {
            Log.i(LOG_TAG + "checkPassword", "not login");
            // 弹出提示窗
            SimpleDialog.showDialog(getActivity(), getString(R.string.login_alert));
            return;
        }

        // 新建意图,跳转到个人信息页面
        Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
        // 执行跳转
        startActivity(intent);
    }

    /**
     * 执行密码修改操作
     */
    private void doPasswordChange() {

        // 判断是否有网络
        if (!MemoryConfig.getConfig().isOpenNetwork()) {
            // 提示无网络
            NoNetworkDialog.showNoNetworkDialog(getActivity());
            return;
        }

        // 判断是否已登录
        if (!MemoryConfig.getConfig().isLogin()) {
            Log.i(LOG_TAG + "checkPassword", "not login");
            // 弹出提示窗
            SimpleDialog.showDialog(getActivity(), getString(R.string.login_alert));
            return;
        }

        // 新建意图,跳转到密码修改页面
        Intent intent = new Intent(getActivity(), PasswordChangeActivity.class);
        // 执行跳转
        startActivity(intent);
    }

    /**
     * 执行退出操作
     */
    private void doExit() {
        System.exit(0);
    }

    /**
     * 执行登录操作
     */
    private void doLogin() {

        // 网络可用时执行
        if (MemoryConfig.getConfig().isOpenNetwork()) {

            // 新建意图,跳转到登录页面
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            // 执行跳转
            startActivity(intent);
        } else {
            // 提示无网络
            NoNetworkDialog.showNoNetworkDialog(getActivity());
        }
    }

    /**
     * 执行设备绑定解绑操作
     */
    private void doDeviceBinding() {

        // 判断是否有网络
        if (!MemoryConfig.getConfig().isOpenNetwork()) {
            // 提示无网络
            NoNetworkDialog.showNoNetworkDialog(getActivity());
            return;
        }

        // 判断是否已登录
        if (!MemoryConfig.getConfig().isLogin()) {
            Log.i(LOG_TAG + "checkPassword", "not login");
            // 弹出提示窗
            SimpleDialog.showDialog(getActivity(), getString(R.string.login_alert));
            return;
        }

        // 新建设备绑定解绑功能对象
        DeviceBinding deviceBinding = new DeviceBinding(getActivity());
        // 执行功能
        deviceBinding.invoke();
    }

    /**
     * 执行设置操作
     */
    private void doSetting() {
        // 新建意图,跳转到设置页面
        Intent intent = new Intent(getActivity(), NotifySettingsActivity.class);
        // 执行跳转
        startActivity(intent);
    }

    /**
     * 执行检查更新
     */
    private void doCheckUpdate() {
        // 网络可用时执行
        if (MemoryConfig.getConfig().isOpenNetwork()) {
            // 新建检查更新功能
            CheckUpdate checkUpdate = new CheckUpdate(getActivity(), StaticValue.APP_CODE);
            // 执行功能
            checkUpdate.checkWithSpinner();
        } else {
            // 提示无网络
            NoNetworkDialog.showNoNetworkDialog(getActivity());
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
