package org.port.trade.fragment.function.cargo.forwarding;
/**
 * Created by 超悟空 on 2015/3/12.
 */

import org.port.trade.fragment.function.BaseWithCompleteFragment;

/**
 * 新路桥公司作业计划的查询布局片段
 *
 * @author 超悟空
 * @version 1.0 2015/3/12
 * @since 1.0
 */
public class NewLandBridgeWorkPlanFragment extends BaseWithCompleteFragment {
    /**
     * 本查询功能要加载的网页地址格式串
     */
    private static final String url = "http://218.92.115.55/M_Hmw/Business/hyyy/NewLandBridgeWorkPlan.html?info=%s+%s";

    @Override
    protected String getFormatUrl() {
        return url;
    }
}
