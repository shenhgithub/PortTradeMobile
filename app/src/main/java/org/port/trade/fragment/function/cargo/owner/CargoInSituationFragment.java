package org.port.trade.fragment.function.cargo.owner;
/**
 * Created by 超悟空 on 2015/3/10.
 */

import org.port.trade.fragment.function.OnlyUserIdFragment;

/**
 * 货物进港动态信息的查询布局片段
 *
 * @author 超悟空
 * @version 1.0 2015/3/10
 * @since 1.0
 */
public class CargoInSituationFragment extends OnlyUserIdFragment {

    /**
     * 本查询功能要加载的网页地址格式串
     */
    private static final String url = "http://218.92.115.55/M_Hmw/Business/hzyy/CargoIn.html?info=%s";

    @Override
    protected String getFormatUrl() {
        return url;
    }
}
