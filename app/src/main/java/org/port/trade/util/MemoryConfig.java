package org.port.trade.util;
/**
 * Created by 超悟空 on 2015/1/27.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;

import org.mobile.model.config.TemporaryConfigModel;
import org.mobile.util.ContextUtil;
import org.port.trade.util.state.MessageNetworkState;

/**
 * 程序运行时的全局临时变量
 *
 * @author 超悟空
 * @version 1.0 2015/1/27
 * @since 1.0
 */
public class MemoryConfig extends TemporaryConfigModel {

    /**
     * 自身的静态对象
     */
    private static MemoryConfig config = null;

    /**
     * 标记是否已登录
     */
    private boolean login = false;

    /**
     * 用户标识
     */
    private String userID = null;

    /**
     * 部门标识
     */
    private String codeDepartment = null;

    /**
     * 公司标识
     */
    private String codeCompany = null;

    /**
     * 标识设备是否已绑定
     */
    private boolean deviceBinding = false;

    /**
     * 消息请求执行状态，默认为空闲状态
     */
    private int messageRequestState = MessageNetworkState.IDLE;

    /**
     * 保存将要加载的查询功能的查询布局片段，
     * 用于Activity间快速传递Fragment对象
     */
    private Fragment functionSearchFragment = null;

    /**
     * 私有构造函数
     */
    private MemoryConfig() {
        super();
    }

    /**
     * 获取全局临时数据对象
     *
     * @return 数据对象
     */
    public static MemoryConfig getConfig() {
        if (config == null) {
            config = new MemoryConfig();
        }
        return config;
    }

    @Override
    protected void onCreate() {

        // 初始化用户参数
        setLogin(false);
        setUserID(null);
        setCodeDepartment(null);
        setCodeCompany(null);
        setDeviceBinding(false);
        // 初始化消息请求状态
        setMessageRequestState(MessageNetworkState.IDLE);

        // 初始化功能片段引用对象
        setFunctionSearchFragment(null);
    }

    @Override
    protected void onRefresh() {

    }

    /**
     * 判断是否登录
     *
     * @return 返回状态
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * 设置登录状态
     *
     * @param flag 状态标识
     */
    public synchronized void setLogin(boolean flag) {
        this.login = flag;
    }

    /**
     * 获取用户标识
     *
     * @return 用户标识串
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户标识
     *
     * @param userID 用户标识串
     */
    public synchronized void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * 获取部门标识
     *
     * @return 部门标识串
     */
    public String getCodeDepartment() {
        return codeDepartment;
    }

    /**
     * 设置部门标识
     *
     * @param codeDepartment 部门标识串
     */
    public void setCodeDepartment(String codeDepartment) {
        this.codeDepartment = codeDepartment;
    }

    /**
     * 获取公司标识
     *
     * @return 公司标识串
     */
    public String getCodeCompany() {
        return codeCompany;
    }

    /**
     * 设置公司标识
     *
     * @param codeCompany 公司标识串
     */
    public void setCodeCompany(String codeCompany) {
        this.codeCompany = codeCompany;
    }

    /**
     * 判断设备是否已绑定
     *
     * @return 绑定状态
     */
    public boolean isDeviceBinding() {
        return deviceBinding;
    }

    /**
     * 设置设备绑定状态
     *
     * @param deviceBinding 状态标识
     */
    public void setDeviceBinding(boolean deviceBinding) {
        this.deviceBinding = deviceBinding;
    }

    /**
     * 获取消息执行状态
     *
     * @return 标识值
     */
    public int getMessageRequestState() {
        return messageRequestState;
    }

    /**
     * 设置消息执行状态
     *
     * @param messageRequestState 状态标识
     */
    public synchronized void setMessageRequestState(int messageRequestState) {
        this.messageRequestState = messageRequestState;
    }

    /**
     * 获取要加载的功能查询布局片段，
     * 在{@link org.port.trade.activity.FunctionContentActivity#initFunctionFragment()}中被取出使用
     *
     * @return 静态变量保存的引用对象
     */
    public Fragment getFunctionSearchFragment() {
        return functionSearchFragment;
    }

    /**
     * 对查询布局片段赋值，
     * 在{@link org.port.trade.activity.FunctionListActivity#initRecyclerView()}中的Item点击事件被赋值为子Activity要加载的Fragment，
     * 在{@link org.port.trade.activity.FunctionContentActivity#initFunctionFragment()}中被提取并显示
     *
     * @param functionSearchFragment 要传递的片段引用
     */
    public void setFunctionSearchFragment(Fragment functionSearchFragment) {
        this.functionSearchFragment = functionSearchFragment;
    }

    /**
     * 对网络连接状态进行判断
     *
     * @return true , 可用； false， 不可用
     */
    public boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) ContextUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isAvailable();
    }
}
