package org.port.trade.fragment.function;
/**
 * Created by 超悟空 on 2015/8/27.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.mobile.model.work.WorkBack;
import org.port.trade.R;
import org.port.trade.util.StaticValue;
import org.port.trade.work.PullScanInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由键值对组成的列表片段
 *
 * @author 超悟空
 * @version 1.0 2015/8/27
 * @since 1.0
 */
public class KeyValueListFragment extends BaseOnlyListFragment {

    /**
     * key取值标签
     */
    private static final String KEY_TAG = "key";

    /**
     * value取值标签
     */
    private static final String VALUE_TAG = "value";

    @Override
    protected void onAsyncLoadData() {
        Bundle bundle = getArguments();

        String number = bundle.getString(StaticValue.EXTERNAL_NUMBER_TAG);
        String taskUrl = bundle.getString(StaticValue.SCAN_INFO_TASK_TAG);

        if (number == null) {
            Toast.makeText(getActivity(), R.string.scan_card_hint, Toast.LENGTH_SHORT).show();
            return;
        }

        PullScanInfo pullScanInfo = new PullScanInfo();

        pullScanInfo.setWorkBackListener(new WorkBack<Map<String, String>>() {
            @Override
            public void doEndWork(boolean state, Map<String, String> data) {
                if (state && data != null && data.size() > 0) {
                    fillList(data);
                }
            }
        });

        pullScanInfo.beginExecute(number, taskUrl);
    }

    /**
     * 填充列表数据
     *
     * @param data 数据集
     */
    private void fillList(Map<String, String> data) {

        for (Map.Entry<String, String> entry : data.entrySet()) {
            // 新建一条记录
            Map<String, Object> map = new HashMap<>();

            // 添加标签资源
            map.put(KEY_TAG, entry.getKey());
            map.put(VALUE_TAG, entry.getValue());

            // 加入数据列表
            getAdapterDataList().add(map);
        }

        // 通知改变
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected List<Map<String, Object>> onCreateAdapterData() {
        return new ArrayList<>();
    }

    @Override
    protected SimpleAdapter onCreateAdapter(List<Map<String, Object>> adapterDataList) {
        return new SimpleAdapter(getActivity(), adapterDataList, R.layout.only_list_item, new
                String[]{KEY_TAG , VALUE_TAG}, new int[]{R.id.only_list_item_left_textView , R.id
                .only_list_item_right_textView});
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
