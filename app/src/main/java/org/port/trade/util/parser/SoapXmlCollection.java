package org.port.trade.util.parser;
/**
 * Created by 超悟空 on 2015/2/5.
 */

import android.util.Log;

import org.port.trade.util.StaticValue;

import java.util.List;
import java.util.Map;

/**
 * 用于获取Webservice请求结果的数据项集合的工具类
 *
 * @author 超悟空
 * @version 1.0 2015/2/5
 * @since 1.0
 */
public class SoapXmlCollection {

    /**
     * 获取Webservice请求结果的数据项集合
     *
     * @param dataModelClass 执行解析的数据模型类
     * @param data           要解析的数据，
     *                       内有标签为{@link org.port.trade.util.StaticValue#COLLECTION_KEY}的{@link java.util.List<java.lang.String,java.lang.String>}集合
     *
     * @return 数据项列表集合，如果该集合不存在或为空，则统一返回null
     */
    public static List<Map<String, String>> getDataList(Class dataModelClass, Map<String, Object> data) {
        // 判断是否存在数据
        if (!data.containsKey(StaticValue.COLLECTION_KEY)) {
            // 数据异常，没有拿到数据
            Log.e(dataModelClass.getName() + ".parse", "no " + StaticValue.COLLECTION_KEY);
            return null;
        }

        // 获取服务器返回的消息相关数据
        //noinspection unchecked
        List<Map<String, String>> dataList = (List<Map<String, String>>) data.get(StaticValue.COLLECTION_KEY);

        if (dataList == null || dataList.isEmpty()) {
            // 没有数据
            Log.e(dataModelClass.getName() + ".parse", StaticValue.COLLECTION_KEY + " is null");
            return null;
        }

        return dataList;
    }
}
