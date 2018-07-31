package org.port.trade.adapter;
/**
 * Created by 悟空 on 2015/3/2.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 功能列表的Item分割线
 *
 * @author 超悟空
 * @version 1.0 2015/3/2
 * @since 1.0
 */
public class FunctionItemDivider extends RecyclerView.ItemDecoration {
    /**
     * 分割条绘图工具
     */
    private Drawable dividerDrawable = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public FunctionItemDivider(Context context) {
        dividerDrawable = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider}).getDrawable(0);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        for (int i = 0, size = parent.getChildCount(); i < size; i++) {
            View child = parent.getChildAt(i);
            FunctionItemViewHolder childViewHolder = (FunctionItemViewHolder) parent.getChildViewHolder(child);
            dividerDrawable.setBounds(childViewHolder.leftImage.getRight(), child.getBottom() - 1, child.getRight(), child.getBottom() + 1);
            dividerDrawable.draw(c);
        }

    }
}
