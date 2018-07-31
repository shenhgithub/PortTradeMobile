package org.port.trade.util.parser;
/**
 * Created by 超悟空 on 2015/2/5.
 */

import android.util.Log;

import org.mobile.parser.SoapResponseXmlToMapParser;
import org.port.trade.util.StaticValue;

import java.util.Map;

/**
 * 本项目使用的Webservice结果XML的解析器
 *
 * @author 超悟空
 * @version 1.0 2015/2/5
 * @since 1.0
 */
public class SoapXmlParser {

    /**
     * 解析数据
     *
     * @param dataModelClass 执行解析的数据模型类
     * @param data           要解析的数据
     *
     * @return 解析后的数据集
     */
    public static Map<String, Object> Parser(Class dataModelClass, Object data) {
        if (data == null) {
            Log.e(dataModelClass.getName() + ".parse", "data is null");
            return null;
        }

        // 新建解析器
        SoapResponseXmlToMapParser parser = new SoapResponseXmlToMapParser();

        // 配置解析器
        parser.setRootKey(StaticValue.ROOT_KEY);
        parser.setCollectionKey(StaticValue.COLLECTION_KEY);
        parser.setRowKey(StaticValue.ROW_KEY);

        // 得到解析数据
        return parser.DataParser(data);
    }
}
