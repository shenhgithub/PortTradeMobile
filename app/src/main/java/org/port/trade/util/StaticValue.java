package org.port.trade.util;
/**
 * Created by 超悟空 on 2015/2/4.
 */

/**
 * 存放应用使用的全局静态常量
 *
 * @author 超悟空
 * @version 1.0 2015/2/4
 * @since 1.0
 */
public interface StaticValue {

    /**
     * Webservice结果XML数据的根标签名
     */
    public static final String ROOT_KEY = "Root";

    /**
     * Webservice结果XML数据的集合标签名
     */
    public static final String COLLECTION_KEY = "Table";

    /**
     * Webservice结果XML数据的数据行标签名
     */
    public static final String ROW_KEY = "Row";

    /**
     * Webservice地址
     */
    public static final String WEBSERVICE_URL = "http://218.92.115.55/M_Hmw/ServiceHMW.asmx";

    /**
     * Webservice服务器公钥参数名
     */
    public static final String WEBSERVICE_PUBLIC_KEY = "token";

    /**
     * Webservice服务器公钥值
     */
    public static final String WEBSERVICE_PUBLIC_VALUE = "MV4FGbDeCY/c0E5Xh9k8Mg==";

    /**
     * 主页地址
     */
    public static final String INDEX_URL = "http://218.92.115.55/M_Hmw/index.html";

    /**
     * HTTP GET请求根地址
     */
    public static final String HTTP_GET_URL_ROOT = "http://218.92.115.55/M_Hmw/getService";

    /**
     * HTTP POST请求根地址
     */
    public static final String HTTP_POST_URL_ROOT = "http://218.92.115.55/M_Hmw/postService";

    /**
     * 常用功能地址
     */
    public static final String COMMON_FUNCTION_URL = "http://218.92.115.55/M_Hmw/Function/cygn" +
            ".html";

    /**
     * 消息在Intent中传递使用的标签
     */
    public static final String MESSAGE_INTENT_TAG = "message_intent";

    /**
     * 功能ID在Intent中传递使用的标签
     */
    public static final String FUNCTION_BUTTON_ID = "function_id";

    /**
     * 用于功能项列表适配器图片ID取值标签
     */
    public static final String FUNCTION_ITEM_IMAGE_ID = "function_item_image_id";

    /**
     * 用于功能项列表适配器功能标题取值标签
     */
    public static final String FUNCTION_ITEM_TITLE = "function_item_title";

    /**
     * 用于从FragmentManager中提取当前功能片段
     */
    public static final String FUNCTION_FRAGMENT_TAG = "function_fragment_tag";

    /**
     * 应用代码
     */
    public static final String APP_CODE = "HMW";

    /**
     * 传递外部编号使用的标签
     */
    String EXTERNAL_NUMBER_TAG = "external_number_tag";

    /**
     * 传递要执行的扫描相关信息获取任务的地址的取值标签
     */
    String SCAN_INFO_TASK_TAG = "scan_info_task_tag";

    /**
     * 获取港通卡基本信息地址
     */
    String GET_BASIC_INFO_URL = "http://218.92.115.55/M_Hmw/GetService/Scan/GetBasicInfo.aspx";

    /**
     * 获取运输申报数据地址
     */
    String GET_TRANSPORT_DECLARE_URL = "http://218.92.115" +
            ".55/M_Hmw/GetService/Scan/GetTransportDeclare.aspx";

    /**
     * 获取港区通行数据地址
     */
    String GET_HARBOUR_PASS_URL = "http://218.92.115.55/M_Hmw/GetService/Scan/GetHarbourPass.aspx";

    /**
     * 获取过磅记录数据地址
     */
    String GET_WEIGH_RECORD_URL = "http://218.92.115.55/M_Hmw/GetService/Scan/GetWeighRecord.aspx";
}
