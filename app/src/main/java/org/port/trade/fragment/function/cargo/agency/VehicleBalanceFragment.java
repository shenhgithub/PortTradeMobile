package org.port.trade.fragment.function.cargo.agency;
/**
 * Created by 超悟空 on 2015/3/6.
 */

import org.port.trade.fragment.function.BaseWithCompanyFragment;

/**
 * 汽车衡量码单查询的查询布局片段
 *
 * @author 超悟空
 * @version 1.0 2015/3/6
 * @since 1.0
 */
public class VehicleBalanceFragment extends BaseWithCompanyFragment {

    /**
     * 本查询功能要加载的网页地址格式串
     */
    private static final String url = "http://218.92.115.55/M_Hmw/Business/hdyy/VehicleBalance.html?info=%s+%s";

    @Override
    protected String getFormatUrl() {
        return url;
    }
}
