package org.port.trade.data;
/**
 * Created by 超悟空 on 2015/8/27.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.model.data.IDataModel;
import org.mobile.parser.HttpResponseHttpEntityToStringParser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基本信息数据模型
 *
 * @author 超悟空
 * @version 1.0 2015/8/27
 * @since 1.0
 */
public class ScanInfoData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "ScanInfoData.";

    /**
     * 卡片编号
     */
    private String number = null;

    /**
     * 识别类型
     */
    private String recognizeMethod = "QR";

    /**
     * 结果数据集
     */
    private Map<String, String> resultList = null;

    /**
     * 设置卡片编号
     *
     * @param number 用户标识字符串
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 获取结果数据集
     *
     * @return 结果数据集
     */
    public Map<String, String> getResultList() {
        return resultList;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入用户标识
        dataMap.put("No", number);
        Log.i(LOG_TAG + "serialization", "No is " + number);

        // 加入识别类型
        dataMap.put("RecognizeMethod", recognizeMethod);
        Log.i(LOG_TAG + "serialization", "recognizeMethod is " + recognizeMethod);

        return dataMap;
    }

    @Override
    public boolean parse(Object data) {
        Log.i(LOG_TAG + "parse", "parse start");

        if (data == null) {
            // 通信异常
            Log.d(LOG_TAG + "parse", "data is null");
            return false;
        }

        // 新建解析器
        HttpResponseHttpEntityToStringParser parser = new HttpResponseHttpEntityToStringParser();

        // 获取结果字符串
        String resultString = parser.DataParser(data);
        Log.i(LOG_TAG + "parse", "result string is " + resultString);

        // 将结果转换为JSON对象
        try {
            JSONObject jsonObject = new JSONObject(resultString);

            resultList = new LinkedHashMap<>();

            if (!jsonObject.isNull("Order")) {
                // 有排序键
                String[] keys = jsonObject.getString("Order").split("\\+");
                for (String key : keys) {
                    resultList.put(key, jsonObject.getString(key));
                }
            } else {
                // 没有排序键
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    resultList.put(key, jsonObject.getString(key));
                }
            }

            Log.i(LOG_TAG + "parse", "parse end,result count is " + resultList.size());
            return true;

        } catch (JSONException e) {
            Log.e(LOG_TAG + "parse", "JSONException " + e.getMessage());
            return false;
        }
    }
}
