/*
 * @(#)CursorUtil.java		       Project:UniversityTimetable
 * Date:2013-1-21
 *
 * Copyright (c) 2013 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.port.trade.util;

import android.database.Cursor;

/**
 * 根据列名获取属性值的工具类。
 *
 * @author 超悟空
 * @version 1.0 2015/2/6
 * @since 1.0
 */
public class CursorUtil {

    /**
     * 以字符串返回请求的列的值
     *
     * @param c      游标对象
     * @param column 请求的列名
     *
     * @return 字符串
     */
    public static String getString(Cursor c, String column) {
        return c.getString(c.getColumnIndex(column));
    }

    /**
     * 以整型返回请求的列的值
     *
     * @param c      游标对象
     * @param column 请求的列名
     *
     * @return 整数
     */
    public static int getInt(Cursor c, String column) {
        return c.getInt(c.getColumnIndex(column));
    }

    /**
     * 以短整型返回请求的列的值
     *
     * @param c      游标对象
     * @param column 请求的列名
     *
     * @return 整数
     */
    public static short getShort(Cursor c, String column) {
        return c.getShort(c.getColumnIndex(column));
    }

    /**
     * 以长整型返回请求的列的值
     *
     * @param c      游标对象
     * @param column 请求的列名
     *
     * @return 整数
     */
    public static long getLong(Cursor c, String column) {
        return c.getLong(c.getColumnIndex(column));
    }

    /**
     * 以浮点型返回请求的列的值。
     *
     * @param c      游标对象
     * @param column 请求的列名
     *
     * @return 单精度浮点数
     */
    public static float getFloat(Cursor c, String column) {
        return c.getFloat(c.getColumnIndex(column));
    }

    /**
     * 以双精度浮点型返回请求的列的值。
     *
     * @param c      游标对象
     * @param column 请求的列名
     *
     * @return 双精度浮点数
     */
    public static double getDouble(Cursor c, String column) {
        return c.getDouble(c.getColumnIndex(column));
    }

    /**
     * 以二进制大对象类型返回请求的列的值。
     *
     * @param c      游标对象
     * @param column 请求的列名
     *
     * @return 二进制数据
     */
    public static byte[] getBlob(Cursor c, String column) {
        return c.getBlob(c.getColumnIndex(column));
    }

    /**
     * 以布尔型返回请求的列值，该列必须是0或1的整数
     *
     * @param c      游标对象
     * @param column 请求的列名
     *
     * @return 列值为1返回true，列值为0返回false
     */
    public static boolean getBoolean(Cursor c, String column) {
        return c.getInt(c.getColumnIndex(column)) == 1;
    }
}
