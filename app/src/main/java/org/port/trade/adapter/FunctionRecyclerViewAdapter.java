package org.port.trade.adapter;
/**
 * Created by 悟空 on 2015/3/2.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mobile.model.operate.OnItemClickListenerForRecyclerViewItem;
import org.port.trade.R;
import org.port.trade.util.StaticValue;

import java.util.List;
import java.util.Map;

/**
 * 功能列表的数据适配器
 *
 * @author 超悟空
 * @version 1.0 2015/3/2
 * @since 1.0
 */
public class FunctionRecyclerViewAdapter extends RecyclerView.Adapter<FunctionItemViewHolder> {

    /**
     * 功能列表，包含每项功能的图片和标题
     */
    private List<Map<String, Object>> functionList = null;

    /**
     * Item点击事件监听器
     */
    private OnItemClickListenerForRecyclerViewItem<List<Map<String, Object>>, FunctionItemViewHolder> onItemClickListener = null;

    /**
     * 构造函数
     *
     * @param functionList 初始化功能列表
     */
    public FunctionRecyclerViewAdapter(List<Map<String, Object>> functionList) {
        this.functionList = functionList;
    }

    /**
     * 设置Item点击事件监听器
     *
     * @param onItemClickListener 监听器对象
     */
    public void setOnItemClickListener(OnItemClickListenerForRecyclerViewItem<List<Map<String, Object>>, FunctionItemViewHolder> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FunctionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建Item根布局
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.function_item, parent, false);

        // 创建Item布局管理器
        return new FunctionItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FunctionItemViewHolder holder, int position) {

        // 获取功能项内容
        Map<String, Object> functionMap = functionList.get(position);

        // 为Item布局绑定数据
        holder.leftImage.setImageResource((Integer) functionMap.get(StaticValue.FUNCTION_ITEM_IMAGE_ID));
        holder.functionTitle.setText((String) functionMap.get(StaticValue.FUNCTION_ITEM_TITLE));

        if (onItemClickListener != null) {
            // 设置点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(functionList, holder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }
}
