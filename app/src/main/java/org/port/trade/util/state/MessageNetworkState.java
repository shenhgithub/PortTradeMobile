package org.port.trade.util.state;
/**
 * Created by 超悟空 on 2015/2/9.
 */

/**
 * 消息网络请求的状态标识
 *
 * @author 超悟空
 * @version 1.0 2015/2/9
 * @since 1.0
 */
public interface MessageNetworkState {

    /**
     * 空闲状态
     */
    public static final int IDLE = 0;

    /**
     * 正在尝试请求一条最新消息
     */
    public static final int TRY_REQUESTING = 1;

    /**
     * 获取一条最新消息完成
     */
    public static final int TRY_REQUESTED = 2;

    /**
     * 循环下载最新消息
     */
    public static final int DOWNLOAD_MESSAGE_LOOP = 3;

    /**
     * 新消息下载完成
     */
    public static final int DOWNLOAD_MESSAGE_END = 4;

    /**
     * 消息请求完成
     */
    public static final int MESSAGE_LOAD_END = 5;

    /**
     * 错误状态，表示无法继续执行和进行新的请求
     */
    public static final int ERROR = -1;
}
