package org.port.trade.fragment.function.shipping.agency;
/**
 * Created by 超悟空 on 2015/3/10.
 */

import org.port.trade.fragment.function.NoUserIdFragment;

/**
 * 泊位船舶查询的查询布局片段
 *
 * @author 超悟空
 * @version 1.0 2015/3/10
 * @since 1.0
 */
public class BerthShipFragment extends NoUserIdFragment {

    /**
     * 本查询功能要加载的网页地址格式串
     */
    private static final String url = "http://218.92.115.55/M_Hmw/Business/cdyy/BerthShip.html";

    @Override
    protected String getFormatUrl() {
        return url;
    }
}
