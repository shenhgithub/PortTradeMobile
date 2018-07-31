package org.port.trade.factory;
/**
 * Created by 超悟空 on 2015/3/4.
 */


import android.support.v4.app.Fragment;

/**
 * 功能查询模块的Fragment生产工厂接口，实现的查询功能片段需要在这里注册，
 * 提供每个模块的访问机制，初始化数据等等，在这里注册设置这些参数
 *
 * @author 超悟空
 * @version 1.0 2015/3/4
 * @since 1.0
 */
public interface BaseFunctionFactory {

    /**
     * 用于产生一个对应的功能查询布局的片段
     *
     * @param position 选中的功能位置
     *
     * @return 查询部分的片段
     */
    Fragment instantiateFunctionSearchFragment(int position);

    /**
     * 判断对应功能使用是否必须登录
     *
     * @param position 选中的功能位置
     *
     * @return true表示必须登录
     */
    boolean isMustLogin(int position);

    /**
     * 判断是否必须进行扫码认证
     *
     * @param position 选中的功能位置
     *
     * @return true表示必须扫码认证
     */
    boolean isMustScan(int position);
}
