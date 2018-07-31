package org.port.trade.adapter;
/**
 * Created by 超悟空 on 2015/2/6.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.port.trade.R;

/**
 * 消息Item的View管理器
 *
 * @author 超悟空
 * @version 1.0 2015/2/6
 * @since 1.0
 */
public class MessageItemViewHolder extends RecyclerView.ViewHolder {

    /**
     * 删除选中标记
     */
    public ImageView deleteMark = null;

    /**
     * 删除选中标记
     */
    public ImageView readMark = null;

    /**
     * 消息左侧的图片
     */
    public ImageView leftImage = null;

    /**
     * 消息发送者文本
     */
    public TextView sendUser = null;

    /**
     * 消息摘要内容
     */
    public TextView messageAbstract = null;

    /**
     * 消息发送时间
     */
    public TextView messageTime = null;

    /**
     * 构造函数
     *
     * @param itemView 根布局
     */
    public MessageItemViewHolder(View itemView) {
        super(itemView);

        // 设置子控件
        deleteMark = (ImageView) itemView.findViewById(R.id.message_item_delete_mark);
        readMark = (ImageView) itemView.findViewById(R.id.message_item_read_mark);
        leftImage = (ImageView) itemView.findViewById(R.id.message_item_imageView);
        sendUser = (TextView) itemView.findViewById(R.id.message_item_send_user);
        messageAbstract = (TextView) itemView.findViewById(R.id.message_item_content);
        messageTime = (TextView) itemView.findViewById(R.id.message_item_time);
    }
}
