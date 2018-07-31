package org.port.trade.fragment.function;
/**
 * Created by 超悟空 on 2015/8/27.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.port.trade.R;

import java.util.List;
import java.util.Map;

/**
 * 仅包含列表的片段基类
 *
 * @author 超悟空
 * @version 1.0 2015/8/27
 * @since 1.0
 */
public abstract class BaseOnlyListFragment extends Fragment implements AdapterView
        .OnItemClickListener {

    /**
     * 列表使用的数据适配器
     */
    private SimpleAdapter adapter = null;

    /**
     * 数据适配器的元数据
     */
    private List<Map<String, Object>> adapterDataList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        // 根布局
        View rootView = inflater.inflate(R.layout.fragment_only_list, container, false);

        // 创建列表
        ListView listView = (ListView) rootView.findViewById(R.id.fragment_only_list_listView);

        adapterDataList = onCreateAdapterData();

        adapter = onCreateAdapter(adapterDataList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        onAsyncLoadData();

        return rootView;
    }

    /**
     * 异步加载和显示数据，在列表配置完成后调用
     */
    protected abstract void onAsyncLoadData();

    /**
     * 创建列表数据源
     *
     * @return 数据源对象
     */
    protected abstract List<Map<String, Object>> onCreateAdapterData();

    /**
     * 创建列表数据适配器
     *
     * @param adapterDataList 数据源对象
     *
     * @return 适配器对象
     */
    protected abstract SimpleAdapter onCreateAdapter(List<Map<String, Object>> adapterDataList);

    /**
     * 获取当前使用的列表适配器
     *
     * @return 适配器对象
     */
    protected SimpleAdapter getAdapter() {
        return adapter;
    }

    /**
     * 获取当前使用的列表数据集
     *
     * @return 数据集对象
     */
    protected List<Map<String, Object>> getAdapterDataList() {
        return adapterDataList;
    }
}
