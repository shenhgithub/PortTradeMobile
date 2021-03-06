package org.port.trade.fragment.function;
/**
 * Created by 超悟空 on 2015/3/12.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.mobile.model.operate.DataGetHandle;
import org.port.trade.R;
import org.port.trade.util.MemoryConfig;

/**
 * 带有车牌号输入的查询Fragment片段基类
 *
 * @author 超悟空
 * @version 1.0 2015/3/12
 * @since 1.0
 */
public abstract class BaseWithPlateFragment extends Fragment implements DataGetHandle<String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseWithPlateFragment.";

    /**
     * 车牌号输入框
     */
    private EditText plateNumberInput = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取根布局
        View rootView = inflater.inflate(R.layout.fragment_search_function_with_plate, container, false);

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
        // 车牌号输入框
        plateNumberInput = (EditText) rootView.findViewById(R.id.plate_number_input_editText);
    }

    /**
     * 此处提供带有车牌号查询参数的网页地址
     *
     * @return 网页url
     */
    @Override
    public String getData() {

        /**
         * 输入的车牌号
         */
        String plateNumber = plateNumberInput.getText().toString();
        Log.i(LOG_TAG + "getData", "plate number is " + plateNumber);

        // 是否满足查询条件
        if (MemoryConfig.getConfig().isLogin()) {
            // 返回完整的查询地址
            return String.format(getFormatUrl(), MemoryConfig.getConfig().getUserID(), plateNumber.trim().toUpperCase());
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
