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
 * 带有公司选择的查询Fragment片段基类
 *
 * @author 超悟空
 * @version 1.0 2015/3/6
 * @since 1.0
 */
public abstract class BaseWithCompanyFragment extends Fragment implements DataGetHandle<String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseWithCompanyFragment.";

    /**
     * 公司选择控件
     */
    private Spinner companySelect = null;

    /**
     * 生成与选择器对应的公司代码
     */
    private String[] companyCodes = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取根布局
        View rootView = inflater.inflate(R.layout.fragment_search_function_with_company, container, false);

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
        // 公司选择控件
        companySelect = (Spinner) rootView.findViewById(R.id.company_select_spinner);
        // 生成与选择器对应的公司代码
        companyCodes = getResources().getStringArray(R.array.company_code);
    }

    /**
     * 此处提供带有公司查询参数的网页地址
     *
     * @return 网页url
     */
    @Override
    public String getData() {
        // 选择的公司位置
        int companySelectPosition = companySelect.getSelectedItemPosition();
        Log.i(LOG_TAG + "getData", "company select position is " + companySelectPosition);

        // 是否满足查询条件
        if (companySelectPosition > -1 && MemoryConfig.getConfig().isLogin()) {
            // 返回完整的查询地址
            return String.format(getFormatUrl(), companyCodes[companySelectPosition], MemoryConfig.getConfig().getUserID());
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
