package org.port.trade.fragment.function;
/**
 * Created by 超悟空 on 2015/3/5.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.port.trade.R;

/**
 * 功能查询界面中默认加载的提示错误的查询布局片段
 *
 * @author 超悟空
 * @version 1.0 2015/3/5
 * @since 1.0
 */
public class FunctionErrorSearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.function_content_search_view_error, container, false);
    }
}
