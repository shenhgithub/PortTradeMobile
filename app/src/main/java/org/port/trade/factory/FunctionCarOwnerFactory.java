package org.port.trade.factory;
/**
 * Created by 超悟空 on 2015/3/4.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.port.trade.fragment.function.KeyValueListFragment;
import org.port.trade.fragment.function.cargo.forwarding.VehicleDeclarationFragment;
import org.port.trade.fragment.function.cargo.forwarding.VehicleRegistrationFragment;
import org.port.trade.fragment.function.shipping.agency.AnchorShipFragment;
import org.port.trade.fragment.function.shipping.agency.BerthShipFragment;
import org.port.trade.fragment.function.shipping.agency.IndeedShipFragment;
import org.port.trade.fragment.function.shipping.agency.MoveShipFragment;
import org.port.trade.fragment.function.shipping.agency.PlanedShipFragment;
import org.port.trade.util.StaticValue;

/**
 * 车主功能工厂类
 *
 * @author 超悟空
 * @version 1.0 2015/3/4
 * @since 1.0
 */
public class FunctionCarOwnerFactory implements BaseFunctionFactory {
    /**
     * 自身的静态实例
     */
    private static FunctionCarOwnerFactory factory = new FunctionCarOwnerFactory();

    /**
     * 构造方法
     */
    private FunctionCarOwnerFactory() {
    }

    /**
     * 获取当前的车主工厂实例
     *
     * @return 静态实例对象
     */
    public static FunctionCarOwnerFactory getFactory() {
        return factory;
    }

    @Override
    public Fragment instantiateFunctionSearchFragment(int position) {
        Fragment fragment;
        Bundle bundle;
        switch (position) {
            case 0:
                // 网上申报未导入车队车辆
                return new VehicleDeclarationFragment();
            case 1:
                // 已导入车队车辆
                return new VehicleRegistrationFragment();
            case 2:
                // 确报船舶查询
                return new IndeedShipFragment();
            case 3:
                // 锚地船舶查询
                return new AnchorShipFragment();
            case 4:
                // 泊位船舶查询
                return new BerthShipFragment();
            case 5:
                // 已做计划船舶信息列表
                return new PlanedShipFragment();
            case 6:
                // 需要移泊船舶信息列表
                return new MoveShipFragment();
            case 7:
                // 港通卡基本信息
                fragment = new KeyValueListFragment();
                bundle = new Bundle();
                bundle.putString(StaticValue.SCAN_INFO_TASK_TAG, StaticValue.GET_BASIC_INFO_URL);
                fragment.setArguments(bundle);
                return fragment;
            case 8:
                // 运输申报
                fragment = new KeyValueListFragment();
                bundle = new Bundle();
                bundle.putString(StaticValue.SCAN_INFO_TASK_TAG, StaticValue
                        .GET_TRANSPORT_DECLARE_URL);
                fragment.setArguments(bundle);
                return fragment;
            case 9:
                // 港区通行
                fragment = new KeyValueListFragment();
                bundle = new Bundle();
                bundle.putString(StaticValue.SCAN_INFO_TASK_TAG, StaticValue.GET_HARBOUR_PASS_URL);
                fragment.setArguments(bundle);
                return fragment;
            case 10:
                // 过磅记录
                fragment = new KeyValueListFragment();
                bundle = new Bundle();
                bundle.putString(StaticValue.SCAN_INFO_TASK_TAG, StaticValue.GET_WEIGH_RECORD_URL);
                fragment.setArguments(bundle);
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public boolean isMustLogin(int position) {
        switch (position) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                // 不需要登录
                return false;
            default:
                // 默认要求登录
                return true;
        }
    }

    @Override
    public boolean isMustScan(int position) {
        switch (position) {
            case 7:
            case 8:
            case 9:
            case 10:
                // 需要扫码
                return true;
            default:
                // 默认不需要扫码
                return false;
        }
    }
}
