package org.port.trade.factory;
/**
 * Created by 超悟空 on 2015/4/1.
 */

import android.support.v4.app.Fragment;

import org.port.trade.fragment.function.cargo.forwarding.LbListFragment;
import org.port.trade.fragment.function.shipping.agency.AnchorShipFragment;
import org.port.trade.fragment.function.shipping.agency.BerthShipFragment;
import org.port.trade.fragment.function.shipping.agency.MoveShipFragment;
import org.port.trade.fragment.function.shipping.agency.PlanedShipFragment;

/**
 * 公共应用工厂类
 *
 * @author 超悟空
 * @version 1.0 2015/4/1
 * @since 1.0
 */
public class FunctionPublicFactory implements BaseFunctionFactory {
    /**
     * 自身的静态实例
     */
    private static FunctionPublicFactory factory = new FunctionPublicFactory();

    /**
     * 构造方法
     */
    private FunctionPublicFactory() {
    }

    /**
     * 获取当前的公共应用工厂实例
     *
     * @return 静态实例对象
     */
    public static FunctionPublicFactory getFactory() {
        return factory;
    }

    @Override
    public Fragment instantiateFunctionSearchFragment(int position) {
        switch (position) {
            case 0:
                // 锚地船舶查询
                return new AnchorShipFragment();
            case 1:
                // 泊位船舶查询
                return new BerthShipFragment();
            case 2:
                // 已做计划船舶信息列表
                return new PlanedShipFragment();
            case 3:
                // 需要移泊船舶信息列表
                return new MoveShipFragment();
            case 4:
                // 磅单查询
                return new LbListFragment();
            default:
                return null;
        }
    }

    @Override
    public boolean isMustLogin(int position) {
        switch (position) {
            default:
                // 默认不需要登录
                return false;
        }
    }

    @Override
    public boolean isMustScan(int position) {
        return false;
    }
}
