package org.port.trade.fragment.function;
/**
 * Created by 超悟空 on 2015/3/10.
 */

import android.support.v4.app.Fragment;

import org.mobile.model.operate.DataGetHandle;
import org.port.trade.util.MemoryConfig;

/**
 * 仅传入用户ID的查询Fragment片段基类
 *
 * @author 超悟空
 * @version 1.0 2015/3/10
 * @since 1.0
 */
public abstract class OnlyUserIdFragment extends Fragment implements DataGetHandle<String> {

    /**
     * 此处提供带有完成状态查询参数的网页地址
     *
     * @return 网页url
     */
    @Override
    public String getData() {

        // 是否满足查询条件
        if (MemoryConfig.getConfig().isLogin()) {
            // 返回完整的查询地址
            return String.format(getFormatUrl(), MemoryConfig.getConfig().getUserID());
        } else {
            // 条件不完整
            return null;
        }
    }

    /**
     * 本查询功能要加载的网页地址格式串，填充位置用%s表示
     *
     * @return 待填充的格式化地址串
     */
    protected abstract String getFormatUrl();
}
