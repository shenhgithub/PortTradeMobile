package org.port.trade.util.communication;
/**
 * Created by 超悟空 on 2015/1/24.
 */

import org.mobile.network.communication.HttpClientGetCommunication;
import org.mobile.network.communication.HttpClientPostCommunication;
import org.mobile.network.communication.ICommunication;
import org.mobile.network.communication.SoapCommunication;
import org.mobile.util.ContextUtil;
import org.port.trade.R;
import org.port.trade.util.NetworkType;
import org.port.trade.util.StaticValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 通讯对象工厂
 *
 * @author 超悟空
 * @version 1.0 2015/1/24
 * @since 1.0
 */
public class CommunicationFactory {

    /**
     * SOAP通讯对象,含服务器公钥
     */
    private static SoapCommunication soapCommunicationHasPublicKey = null;

    /**
     * SOAP通讯对象,不含服务器公钥
     */
    private static SoapCommunication soapCommunicationNoPublicKey = null;

    /**
     * HttpGet请求对象
     */
    private static HttpClientGetCommunication httpClientGetCommunication = null;

    /**
     * HttpPost请求对象
     */
    private static HttpClientPostCommunication httpClientPostCommunication = null;

    /**
     * 创建通讯工具对象
     *
     * @param networkType 网络工具类型
     *
     * @return 初始化完成的通讯工具
     */
    public static ICommunication Create(NetworkType networkType) {

        switch (networkType) {
            case WEBSERVICE_HAS_PUBLIC_KEY:
                // 有服务器公钥的SOAP对象
                if (soapCommunicationHasPublicKey == null) {
                    soapCommunicationHasPublicKey = initSoapCommunication(networkType);
                }
                return soapCommunicationHasPublicKey;
            case WEBSERVICE_NO_PUBLIC_KEY:
                // 无服务器公钥的SOAP对象
                if (soapCommunicationNoPublicKey == null) {
                    soapCommunicationNoPublicKey = initSoapCommunication(networkType);
                }
                return soapCommunicationNoPublicKey;
            case HTTP_GET:
                // HttpGet请求对象
                if (httpClientGetCommunication == null) {
                    httpClientGetCommunication = initHttpClientGetCommunication();
                }
                return httpClientGetCommunication;
            case HTTP_POST:
                // HttpPost请求对象
                if (httpClientPostCommunication == null) {
                    httpClientPostCommunication = initHttpClientPostCommunication();
                }
                return httpClientPostCommunication;
            default:
                throw new UnsupportedOperationException("指定协议未实现");
        }
    }

    /**
     * 初始化SOAP对象
     *
     * @param networkType 网络工具类型
     *
     * @return 初始化完成的SOAP对象
     */
    private static SoapCommunication initSoapCommunication(NetworkType networkType) {
        // 新建SOAP对象
        SoapCommunication soapCommunication = new SoapCommunication();

        // 设置url和超时时间
        soapCommunication.setUrl(StaticValue.WEBSERVICE_URL, ContextUtil.getContext().getResources().getInteger(R.integer.webservice_timeout));

        // 其他设置
        switch (networkType) {
            case WEBSERVICE_HAS_PUBLIC_KEY:
                // 有服务器公钥的SOAP对象
                // 公钥MAP
                Map<String, String> publicKey = new HashMap<>();
                // 加入公钥
                publicKey.put(StaticValue.WEBSERVICE_PUBLIC_KEY, StaticValue.WEBSERVICE_PUBLIC_VALUE);
                // 将公钥键值对加入SOAP对象
                soapCommunication.setPreParameters(publicKey);
                break;
            default:
                break;
        }

        return soapCommunication;
    }

    /**
     * 初始化HttpGet请求对象
     *
     * @return 初始化完成的HttpGet对象
     */
    private static HttpClientGetCommunication initHttpClientGetCommunication() {

        // 新建HttpGet请求对象
        HttpClientGetCommunication httpClient = new HttpClientGetCommunication();
        // 设置根地址
        httpClient.setUrlRoot(StaticValue.HTTP_GET_URL_ROOT);
        // 设置超时时间
        httpClient.setTimeout(ContextUtil.getContext().getResources().getInteger(R.integer.http_get_timeout));

        return httpClient;
    }

    /**
     * 初始化HttpPost请求对象
     *
     * @return 初始化完成的HttpPost对象
     */
    private static HttpClientPostCommunication initHttpClientPostCommunication() {
        // 新建HttpPost请求对象
        HttpClientPostCommunication httpClient = new HttpClientPostCommunication();
        // 设置根地址
        httpClient.setUrlRoot(StaticValue.HTTP_POST_URL_ROOT);
        // 设置超时时间
        httpClient.setTimeout(ContextUtil.getContext().getResources().getInteger(R.integer.http_post_timeout));

        return httpClient;
    }
}
