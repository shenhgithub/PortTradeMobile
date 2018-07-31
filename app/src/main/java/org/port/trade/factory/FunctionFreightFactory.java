package org.port.trade.factory;
/**
 * Created by 超悟空 on 2015/3/4.
 */

import android.support.v4.app.Fragment;

import org.port.trade.fragment.function.cargo.forwarding.NewLandBridgeWorkPlanFragment;
import org.port.trade.fragment.function.cargo.forwarding.NoPassedTransportationFragment;
import org.port.trade.fragment.function.cargo.forwarding.PassedTransportationFragment;
import org.port.trade.fragment.function.cargo.forwarding.VehicleBalanceFragment;
import org.port.trade.fragment.function.cargo.forwarding.VehicleDeclarationFragment;
import org.port.trade.fragment.function.cargo.forwarding.VehicleRegistrationFragment;

/**
 * 货运功能工厂类
 *
 * @author 超悟空
 * @version 1.0 2015/3/4
 * @since 1.0
 */
public class FunctionFreightFactory implements BaseFunctionFactory {
    /**
     * 自身的静态实例
     */
    private static FunctionFreightFactory factory = new FunctionFreightFactory();

    /**
     * 构造方法
     */
    private FunctionFreightFactory() {
    }

    /**
     * 获取当前的货运工厂实例
     *
     * @return 静态实例对象
     */
    public static FunctionFreightFactory getFactory() {
        return factory;
    }

    @Override
    public Fragment instantiateFunctionSearchFragment(int position) {
        switch (position) {
            case 0:
                // 汽车衡量码单查询
                return new VehicleBalanceFragment();
            case 1:
                // 网上申报未导入车队车辆
                return new VehicleDeclarationFragment();
            case 2:
                // 已导入车队车辆
                return new VehicleRegistrationFragment();
            case 3:
                // 新路桥公司作业计划
                return new NewLandBridgeWorkPlanFragment();
            case 4:
                // 已放行运输列表
                return new PassedTransportationFragment();
            case 5:
                // 已提交未放行运输列表
                return new NoPassedTransportationFragment();
            default:
                return null;
        }
    }

    @Override
    public boolean isMustLogin(int position) {
        switch (position) {
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
