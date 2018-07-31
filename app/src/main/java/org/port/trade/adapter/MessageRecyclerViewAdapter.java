package org.port.trade.adapter;
/**
 * Created by 超悟空 on 2015/2/6.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mobile.model.operate.OnItemClickListenerForRecyclerViewItem;
import org.mobile.model.operate.OnItemLongClickListenerForRecyclerViewItem;
import org.port.trade.R;
import org.port.trade.data.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表的数据适配器
 *
 * @author 超悟空
 * @version 1.0 2015/2/6
 * @since 1.0
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageItemViewHolder> {

    /**
     * 消息列表
     */
    private List<Message> messageList = null;

    /**
     * 点击事件监听器
     */
    private OnItemClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder> onItemClickListener = null;

    /**
     * 长按事件监听器
     */
    private OnItemLongClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder> onItemLongClickListener = null;

    /**
     * 空构造函数
     */
    public MessageRecyclerViewAdapter() {
        messageList = new ArrayList<>();
    }

    /**
     * 构造函数
     *
     * @param messageList 初始化数据集
     */
    public MessageRecyclerViewAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    /**
     * 首次设置消息数据集
     *
     * @param messageList 消息列表
     */
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    /**
     * 获取当前的消息数据集
     *
     * @return 消息列表
     */
    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * 在列表头部添加一条消息
     *
     * @param message 消息对象
     */
    public void add(Message message) {
        this.messageList.add(message);
        this.notifyItemInserted(0);
    }

    /**
     * 在列表头部添加一组消息
     *
     * @param messageList 消息队列
     */
    public void add(List<Message> messageList) {
        this.messageList.addAll(messageList);
        this.notifyItemRangeInserted(0, messageList.size());
    }

    /**
     * 在指定位置插入一条消息
     *
     * @param position 位置索引
     * @param message  消息对象
     */
    public void add(int position, Message message) {
        this.messageList.add(position, message);
        this.notifyItemInserted(position);
    }

    /**
     * 在指定位置插入一组消息
     *
     * @param position    位置索引
     * @param messageList 消息队列
     */
    public void add(int position, List<Message> messageList) {
        this.messageList.addAll(position, messageList);
        this.notifyItemRangeInserted(position, messageList.size());
    }

    /**
     * 删除指定位置的一条消息
     *
     * @param position 位置索引
     *
     * @return 被删除的消息对象
     */
    public Message remove(int position) {
        // 要移除的消息对象
        Message message = this.messageList.remove(position);
        this.notifyItemRemoved(position);
        return message;
    }

    /**
     * 移除删除状态标记
     *
     * @param position 位置索引
     */
    public void removeDeleteState(int position) {
        this.messageList.get(position).setDeleteState(false);
        this.notifyItemChanged(position);
    }

    /**
     * 设置列表项Item的点击事件监听器
     *
     * @param onItemClickListener 监听器对象
     */
    public void setOnItemClickListener(OnItemClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置列表项Item的长按事件监听器
     *
     * @param onItemLongClickListener 监听器对象
     */
    public void setOnItemLongClickListener(OnItemLongClickListenerForRecyclerViewItem<List<Message>, MessageItemViewHolder> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public MessageItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // 创建Item根布局
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, viewGroup, false);

        // 创建Item布局管理器
        return new MessageItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MessageItemViewHolder messageItemViewHolder, int position) {
        // 为Item布局绑定数据
        messageItemViewHolder.deleteMark.setImageResource(messageList.get(position).isDeleteState() ? R.color.blue : R.color.white);
        messageItemViewHolder.readMark.setImageResource(messageList.get(position).isReadState() ? R.color.white : R.drawable.message_read_mark);
        messageItemViewHolder.sendUser.setText(messageList.get(position).getSendUser());
        messageItemViewHolder.messageAbstract.setText(messageList.get(position).getMessageAbstract());
        messageItemViewHolder.messageTime.setText(messageList.get(position).getMessageTime());

        if (onItemClickListener != null) {
            // 设置点击事件
            messageItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(messageList, messageItemViewHolder);
                }
            });
        }

        if (onItemLongClickListener != null) {
            // 设置长按事件
            messageItemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClickListener.onLongClick(messageList, messageItemViewHolder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
