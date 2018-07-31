package org.port.trade.db.message;
/**
 * Created by 超悟空 on 2015/2/6.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 消息数据库的创建与升级
 *
 * @author 超悟空
 * @version 1.0 2015/2/6
 * @since 1.0
 */
public class MessageSQLite extends SQLiteOpenHelper {

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public MessageSQLite(Context context) {
        super(context, MessageConst.DB_NAME, null, MessageConst.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
