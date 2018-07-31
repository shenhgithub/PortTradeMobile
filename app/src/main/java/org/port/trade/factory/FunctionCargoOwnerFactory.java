package org.port.trade.factory;
/**
 * Created by 超悟空 on 2015/3/4.
 */

import android.support.v4.app.Fragment;

import org.port.trade.fragment.function.cargo.owner.CargoBalanceFragment;
import org.port.trade.fragment.function.cargo.owner.CargoExchangeSituationFragment;
import org.port.trade.fragment.function.cargo.owner.CargoInSituationFragment;
import org.port.trade.fragment.function.cargo.owner.CargoOutSituationFragment;
import org.port.trade.fragment.function.shipping.agency.AnchorShipFragment;
import org.port.trade.fragment.function.shipping.agency.BerthShipFragment;
import org.port.trade.fragment.function.shipping.agency.IndeedShipFragment;
import org.port.trade.fragment.function.shipping.agency.MoveShipFragment;
import org.port.trade.fragment.function.shipping.agency.PlanedShipFragment;

/**
 * 货主功能工厂类
 *
 * @author 超悟空
 * @version 1.0 2015/3/4
 * @since 1.0
 */
public class FunctionCargoOwnerFactory implements BaseFunctionFactory {

    /**
     * 自身的静态实例
     */
    private static FunctionCargoOwnerFactory factory = new FunctionCargoOwnerFactory();

    /**
     * 构造方法
     */
    private FunctionCargoOwnerFactory() {
    }

    /**
     * 获取当前的货主工厂实例
     *
     * @return 静态实例对象
     */
    public static FunctionCargoOwnerFactory getFactory() {
        return factory;
    }

    @Override
    public Fragment instantiateFunctionSearchFragment(int position) {
        switch (position) {
            case 0:
                // 货物进港动态信息
                return new CargoInSituationFragment();
            case 1:
                // 货物出港动态信息
                return new CargoOutSituationFragment();
            case 2:
                // 货物兑动态信息
                return new CargoExchangeSituationFragment();
            case 3:
                // 货物港内结存信息
                return new CargoBalanceFragment();
            case 4:
                // 确报船舶查询
                return new IndeedShipFragment();
            case 5:
                // 锚地船舶查询
                return new AnchorShipFragment();
            case 6:
                // 泊位船舶查询
                return new BerthShipFragment();
            case 7:
                // 已做计划船舶信息列表
                return new PlanedShipFragment();
            case 8:
                // 需要移泊船舶信息列表
                return new MoveShipFragment();
            default:
                return null;
        }
    }

    @Override
    public boolean isMustLogin(int position) {
        switch (position) {
            case 5:
            case 6:
            case 7:
            case 8:
                // 不需要登录
                return false;
            default:
                // 默认要求登录
                return true;
        }
    }

    @Override
    public boolean isMustScan(int position) {
        return false;
    }
}
