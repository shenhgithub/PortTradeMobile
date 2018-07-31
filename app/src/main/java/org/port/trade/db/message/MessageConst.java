package org.port.trade.db.message;
/**
 * Created by 超悟空 on 2015/2/6.
 */

/**
 * 消息数据库的常量类
 *
 * @author 超悟空
 * @version 1.0 2015/2/6
 * @since 1.0
 */
public interface MessageConst {

    /**
     * ID列
     */
    public static final String _ID = "_id";

    /**
     * 数据库名
     */
    public static final String DB_NAME = "PortTrade.db";

    /**
     * 数据库版本
     */
    public static final int DB_VERSION = 1;

    /**
     * 消息表名
     */
    public static final String MESSAGE_TABLE_NAME = "message_";

    /**
     * 消息ID列名
     */
    public static final String MESSAGE_ID = "messageId";

    /**
     * 消息发送者列名
     */
    public static final String MESSAGE_SEND_USER = "messageSendUser";

    /**
     * 消息摘要列名
     */
    public static final String MESSAGE_ABSTRACT = "messageAbstract";

    /**
     * 消息内容列名
     */
    public static final String MESSAGE_CONTENT = "messageContent";

    /**
     * 消息发送时间列明名
     */
    public static final String MESSAGE_TIME = "messageTime";

    /**
     * 消息已读未读状态列名
     */
    public static final String MESSAGE_READ_STATE = "messageReadState";

    /**
     * 消息删除状态列
     */
    public static final String MESSAGE_DELETE_STATE = "messageDeleteState";
}
