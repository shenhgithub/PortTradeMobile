package org.port.trade.factory;
/**
 * Created by 超悟空 on 2015/3/4.
 */

import android.support.v4.app.Fragment;

import org.port.trade.fragment.function.shipping.agency.AnchorShipFragment;
import org.port.trade.fragment.function.shipping.agency.BerthShipFragment;
import org.port.trade.fragment.function.shipping.agency.CommunicationFeeFragment;
import org.port.trade.fragment.function.shipping.agency.ForeShipFragment;
import org.port.trade.fragment.function.shipping.agency.IndeedShipFragment;
import org.port.trade.fragment.function.shipping.agency.MoveShipFragment;
import org.port.trade.fragment.function.shipping.agency.PilotageFeeFragment;
import org.port.trade.fragment.function.shipping.agency.PlanedShipFragment;

/**
 * 船代功能的工厂类
 *
 * @author 超悟空
 * @version 1.0 2015/3/4
 * @since 1.0
 */
public class FunctionShippingAgencyFactory implements BaseFunctionFactory {
    /**
     * 自身的静态实例
     */
    private static FunctionShippingAgencyFactory factory = new FunctionShippingAgencyFactory();

    /**
     * 构造方法
     */
    private FunctionShippingAgencyFactory() {
    }

    /**
     * 获取当前的船代工厂实例
     *
     * @return 静态实例对象
     */
    public static FunctionShippingAgencyFactory getFactory() {
        return factory;
    }

    @Override
    public Fragment instantiateFunctionSearchFragment(int position) {
        switch (position) {
            case 0:
                // 预报船舶查询
                return new ForeShipFragment();
            case 1:
                // 确报船舶查询
                return new IndeedShipFragment();
            case 2:
                // 锚地船舶查询
                return new AnchorShipFragment();
            case 3:
                // 泊位船舶查询
                return new BerthShipFragment();
            case 4:
                // 已做计划船舶信息列表
                return new PlanedShipFragment();
            case 5:
                // 需要移泊船舶信息列表
                return new MoveShipFragment();
            case 6:
                // 引航费用信息查询
                return new PilotageFeeFragment();
            case 7:
                // 高频话费查询
                return new CommunicationFeeFragment();
            default:
                return null;
        }
    }

    @Override
    public boolean isMustLogin(int position) {
        switch (position) {
            case 2:
            case 3:
            case 4:
            case 5:
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