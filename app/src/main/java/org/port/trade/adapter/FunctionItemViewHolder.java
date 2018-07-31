package org.port.trade.adapter;
/**
 * Created by 悟空 on 2015/3/2.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.port.trade.R;

/**
 * 功能项Item的View管理器
 *
 * @author 超悟空
 * @version 1.0 2015/3/2
 * @since 1.0
 */
public class FunctionItemViewHolder extends RecyclerView.ViewHolder {

    /**
     * 功能项左侧的图片
     */
    public ImageView leftImage = null;

    /**
     * 功能项标题
     */
    public TextView functionTitle = null;

    public FunctionItemViewHolder(View itemView) {
        super(itemView);

        leftImage = (ImageView) itemView.findViewById(R.id.function_imageView);
        functionTitle = (TextView) itemView.findViewById(R.id.function_textView);
    }
}
