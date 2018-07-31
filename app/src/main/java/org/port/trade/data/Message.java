package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/2/4.
 */

import java.io.Serializable;

/**
 * 存放一条消息的数据结构
 *
 * @author 超悟空
 * @version 1.0 2015/2/4
 * @since 1.0
 */
public class Message implements Serializable {

    /**
     * 消息ID
     */
    private int messageId = -1;

    /**
     * 消息发送者
     */
    private String sendUser = null;

    /**
     * 消息全部内容
     */
    private String messageContent = null;

    /**
     * 消息摘要，用于在列表中显示
     */
    private String messageAbstract = null;

    /**
     * 消息发送时间
     */
    private String messageTime = null;

    /**
     * 已读未读标识，true为已读
     */
    private boolean readState = false;

    /**
     * 删除标识，true为已删除
     */
    private boolean deleteState = false;

    /**
     * 获取消息ID
     *
     * @return 消息ID
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * 设置消息ID
     *
     * @param messageId 消息ID
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取消息发送者
     *
     * @return 发送者
     */
    public String getSendUser() {
        return sendUser;
    }

    /**
     * 设置消息发送者
     *
     * @param sendUser 消息发送者
     */
    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    /**
     * 获取完整消息内容
     *
     * @return 内容字符串
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * 设置消息内容
     *
     * @param messageContent 内容字符串
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * 获取消息摘要
     *
     * @return 摘要字符串
     */
    public String getMessageAbstract() {
        return messageAbstract;
    }

    /**
     * 设置消息摘要
     *
     * @param messageAbstract 摘要字符串
     */
    public void setMessageAbstract(String messageAbstract) {
        this.messageAbstract = messageAbstract;
    }

    /**
     * 获取消息发送时间
     *
     * @return 时间字符串
     */
    public String getMessageTime() {
        return messageTime;
    }

    /**
     * 设置消息发送时间
     *
     * @param messageTime 时间字符串
     */
    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    /**
     * 判断消息是否已读
     *
     * @return true表示已读
     */
    public boolean isReadState() {
        return readState;
    }

    /**
     * 设置消息已读未读状态
     *
     * @param readState true表示已读，false表示未读
     */
    public void setReadState(boolean readState) {
        this.readState = readState;
    }

    /**
     * 判断该条消息是否已被删除
     *
     * @return true为已删除
     */
    public boolean isDeleteState() {
        return deleteState;
    }

    /**
     * 标记该条消息为删除或未删除
     *
     * @param deleteState 状态标识
     */
    public void setDeleteState(boolean deleteState) {
        this.deleteState = deleteState;
    }
}
