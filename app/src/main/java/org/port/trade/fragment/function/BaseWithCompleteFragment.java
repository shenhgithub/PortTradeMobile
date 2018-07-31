package org.port.trade.fragment.function;
/**
 * Created by 超悟空 on 2015/3/6.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.mobile.model.operate.DataGetHandle;
import org.port.trade.R;
import org.port.trade.util.MemoryConfig;

/**
 * 带有完成状态选择的查询Fragment片段基类
 *
 * @author 超悟空
 * @version 1.0 2015/3/6
 * @since 1.0
 */
public abstract class BaseWithCompleteFragment extends Fragment implements DataGetHandle<String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseWithCompleteFragment.";

    /**
     * 完成状态选择控件
     */
    private Spinner completeMarkSelect = null;

    /**
     * 生成与选择器对应的完成状态代码
     */
    private String[] completeMarkCode = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取根布局
        View rootView = inflater.inflate(R.layout.fragment_search_function_with_complete, container, false);

        // 初始化布局
        initView(rootView);

        return rootView;
    }

    /**
     * 初始化布局
     *
     * @param rootView 根布局
     */
    private void initView(View rootView) {
        // 完成状态选择控件
        completeMarkSelect = (Spinner) rootView.findViewById(R.id.complete_mark_select_spinner);
        // 生成与选择器对应的完成状态代码
        completeMarkCode = getResources().getStringArray(R.array.complete_mark_code);

    }

    /**
     * 此处提供带有完成状态查询参数的网页地址
     *
     * @return 网页url
     */
    @Override
    public String getData() {

        // 选择的完成状态位置
        int completeMarkSelectPosition = completeMarkSelect.getSelectedItemPosition();
        Log.i(LOG_TAG + "getData", "complete mark select position is " + completeMarkSelectPosition);

        // 是否满足查询条件
        if (completeMarkSelectPosition > -1 && MemoryConfig.getConfig().isLogin()) {
            // 返回完整的查询地址
            return String.format(getFormatUrl(), MemoryConfig.getConfig().getUserID(), completeMarkCode[completeMarkSelectPosition]);
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
