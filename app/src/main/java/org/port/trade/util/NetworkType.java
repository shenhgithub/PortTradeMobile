package org.port.trade.util;
/**
 * Created by 超悟空 on 2015/1/24.
 */

/**
 * 网络工具类型枚举
 *
 * @author 超悟空
 * @version 1.0 2015/1/24
 * @since 1.0
 */
public enum NetworkType {

    /**
     * 包含服务器公钥的webservice请求
     */
    WEBSERVICE_HAS_PUBLIC_KEY,
    /**
     * 不含服务器公钥的webservice请求
     */
    WEBSERVICE_NO_PUBLIC_KEY,
    /**
     * http post类型的请求
     */
    HTTP_POST,
    /**
     * http get类型的请求
     */
    HTTP_GET,
    /**
     * 下载
     */
    DOWNLOAD,
    /**
     * 上传
     */
    UPDATE;
}
