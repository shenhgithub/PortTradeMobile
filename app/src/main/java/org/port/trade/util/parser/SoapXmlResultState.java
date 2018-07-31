package org.port.trade.util.parser;
/**
 * Created by 超悟空 on 2015/2/5.
 */

import android.util.Log;

import org.mobile.util.ContextUtil;
import org.port.trade.R;

import java.util.Map;

/**
 * 分析Webservice返回的解析后的结果数据，
 * 判断请求是否成功
 *
 * @author 超悟空
 * @version 1.0 2015/2/5
 * @since 1.0
 */
public class SoapXmlResultState {

    /**
     * 数据异常的message
     */
    private final String ERROR_MESSAGE = ContextUtil.getContext().getString(R.string.error_message_webservice_request);

    /**
     * 执行解析的数据模型类名
     */
    private String className = null;

    /**
     * 服务器结果消息
     */
    private String message = null;

    /**
     * 请求执行结果
     */
    private boolean result = false;

    /**
     * 数据分析结果
     */
    private boolean analyzeResult = false;

    /**
     * 要解析的数据
     */
    private Map<String, Object> data = null;

    /**
     * 构造函数
     *
     * @param dataModelClass 执行解析的数据模型类
     * @param data           要解析的数据
     */
    public SoapXmlResultState(Class dataModelClass, Map<String, Object> data) {

        this.data = data;
        this.className = dataModelClass.getName();
    }

    /**
     * 判断请求结果
     *
     * @return 成功返回true，失败返回false
     */
    public boolean isResult() {
        if (message == null) {
            // 没有解析过则先解析
            analyze();
        }
        return result;
    }

    /**
     * 获取服务器返回的结果消息，
     * 如果数据不存在或数据异常则返回应用默认错误消息内容
     * {@link #ERROR_MESSAGE}
     *
     * @return 消息字符串
     */
    public String getMessage() {
        if (message == null) {
            // 没有解析过则先解析
            analyze();
        }
        return message;
    }

    /**
     * 判断数据分析是否成功，
     * 当传入的数据格式不符合分析器的规则时将返回false，
     * 比如请求到的结果数据存在格式异常等
     *
     * @return true表示分析完成
     */
    public boolean isAnalyzeResult() {
        if (message == null) {
            // 没有解析过则先解析
            analyze();
        }
        return analyzeResult;
    }

    private void analyze() {
        if (data == null || !data.containsKey("Success")) {
            // 不能正常取到值，可能是服务器序列化方式改变
            Log.e(className + ".parse", "no success");
            result = false;
            message = ERROR_MESSAGE;
            analyzeResult = false;
            return;
        }

        // 执行结果
        String resultState = (String) data.get("Success");

        if (resultState == null) {
            // 消息数据不存在或异常
            Log.d(className + ".parse", "resultState is error");
            result = false;
            message = ERROR_MESSAGE;
            analyzeResult = false;
            return;
        }

        // 将结果字符串转为无空格小写
        resultState = resultState.trim().toLowerCase();

        if (!resultState.equals("true") && !resultState.equals("false")) {
            // 结果串不是true或false，说明数据异常
            Log.d(className + ".parse", "resultState is error");
            result = false;
            message = ERROR_MESSAGE;
            analyzeResult = false;
            return;
        }

        // 请求执行完整的情况下
        result = resultState.equals("true");
        message = (String) data.get("Message");
        analyzeResult = true;
    }
}
