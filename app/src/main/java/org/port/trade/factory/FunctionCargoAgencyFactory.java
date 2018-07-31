package org.port.trade.factory;
/**
 * Created by 超悟空 on 2015/3/4.
 */

import android.support.v4.app.Fragment;

import org.port.trade.fragment.function.cargo.agency.CargoInFragment;
import org.port.trade.fragment.function.cargo.agency.CargoOutFragment;
import org.port.trade.fragment.function.cargo.agency.CargoStockFragment;
import org.port.trade.fragment.function.cargo.agency.GoodsBillFragment;
import org.port.trade.fragment.function.cargo.agency.JobConsignFragment;
import org.port.trade.fragment.function.cargo.agency.NoShipBusinessConsignFragment;
import org.port.trade.fragment.function.cargo.agency.ShipBusinessConsignFragment;
import org.port.trade.fragment.function.cargo.agency.VehicleBalanceFragment;
import org.port.trade.fragment.function.cargo.agency.VehicleTransportFragment;

/**
 * 货代功能的工厂类
 *
 * @author 超悟空
 * @version 1.0 2015/3/4
 * @since 1.0
 */
public class FunctionCargoAgencyFactory implements BaseFunctionFactory {

    /**
     * 自身的静态实例
     */
    private static FunctionCargoAgencyFactory factory = new FunctionCargoAgencyFactory();

    /**
     * 构造方法
     */
    private FunctionCargoAgencyFactory() {
    }

    /**
     * 获取当前的货代工厂实例
     *
     * @return 静态实例对象
     */
    public static FunctionCargoAgencyFactory getFactory() {
        return factory;
    }

    @Override
    public Fragment instantiateFunctionSearchFragment(int position) {
        switch (position) {
            case 0:
                // 选中索引0，票货查询
                return new GoodsBillFragment();
            case 1:
                // 选中索引1，业务大委托有船作业查询
                return new ShipBusinessConsignFragment();
            case 2:
                // 选中索引2，业务大委托无船作业查询
                return new NoShipBusinessConsignFragment();
            case 3:
                // 选中索引3，作业委托查询
                return new JobConsignFragment();
            case 4:
                // 选中索引4，车辆运输查询
                return new VehicleTransportFragment();
            case 5:
                // 选中索引5，汽车衡量码单查询
                return new VehicleBalanceFragment();
            case 6:
                // 选中索引6，货物进港查询
                return new CargoInFragment();
            case 7:
                // 选中索引7，货物出港查询
                return new CargoOutFragment();
            case 8:
                // 选中索引8，货物港内结存查询
                return new CargoStockFragment();
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
