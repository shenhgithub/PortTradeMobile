package org.port.trade.db.message;
/**
 * Created by 超悟空 on 2015/2/6.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.port.trade.data.Message;
import org.port.trade.util.CursorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息表的数据库操作类
 *
 * @author 超悟空
 * @version 1.0 2015/2/6
 * @since 1.0
 */
public class MessageOperator {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MessageOperator.";

    /**
     * 数据库读取对象
     */
    private SQLiteDatabase dbReader = null;

    /**
     * 消息数据库对象
     */
    private MessageSQLite messageSQLite = null;

    /**
     * 表名后缀，这里用于给不同用户建立不同的数据表
     */
    private String tableNameSuffix = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public MessageOperator(Context context, String tableNameSuffix) {
        this.tableNameSuffix = tableNameSuffix;
        Log.i(LOG_TAG + "MessageOperator", "table name suffix is " + tableNameSuffix);
        this.messageSQLite = new MessageSQLite(context);
        createTable();
    }

    /**
     * 建表，如果不存在指定表名后缀{@link #tableNameSuffix}的表则新建
     */
    private void createTable() {
        Log.i(LOG_TAG + "createTable", "createTable() is invoked");
        // 消息表创建语句
        String messageTableCreate = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY, %s INTEGER NOT NULL UNIQUE,  %s NVARCHAR(40), %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER)", MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, MessageConst._ID, MessageConst.MESSAGE_ID, MessageConst.MESSAGE_SEND_USER, MessageConst.MESSAGE_ABSTRACT, MessageConst.MESSAGE_CONTENT, MessageConst.MESSAGE_TIME, MessageConst.MESSAGE_READ_STATE, MessageConst.MESSAGE_DELETE_STATE);

        // 创建表
        Log.i(LOG_TAG + "createTable", "create table sql is " + messageTableCreate);
        this.messageSQLite.getWritableDatabase().execSQL(messageTableCreate);
    }

    /**
     * 保存一条消息
     *
     * @param message 消息对象
     *
     * @return 保存成功返回行ID，否则返回-1
     */
    public long save(Message message) {
        Log.i(LOG_TAG + "save", "save(Message) is invoked");

        if (message == null) {
            Log.i(LOG_TAG + "save", "message is null");
            return -1;
        }
        Log.i(LOG_TAG + "save", "message id is " + message.getMessageId());
        // 数据库值对象
        ContentValues cv = getContentValues(message);

        // 得到数据库写对象
        SQLiteDatabase dbWriter = messageSQLite.getWritableDatabase();
        // 写入数据
        long newRowId = dbWriter.insert(MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, null, cv);

        Log.i(LOG_TAG + "save", "new row id is " + newRowId);

        dbWriter.close();

        return newRowId;
    }

    /**
     * 填充一个数据库值对象
     *
     * @param message 要填充的消息
     *
     * @return 一行记录
     */
    private ContentValues getContentValues(Message message) {
        // 数据库值对象
        ContentValues cv = new ContentValues();
        cv.put(MessageConst.MESSAGE_ID, message.getMessageId());
        cv.put(MessageConst.MESSAGE_SEND_USER, message.getSendUser());
        cv.put(MessageConst.MESSAGE_ABSTRACT, message.getMessageAbstract());
        cv.put(MessageConst.MESSAGE_CONTENT, message.getMessageContent());
        cv.put(MessageConst.MESSAGE_TIME, message.getMessageTime());
        cv.put(MessageConst.MESSAGE_READ_STATE, message.isReadState());
        cv.put(MessageConst.MESSAGE_DELETE_STATE, message.isDeleteState());
        return cv;
    }

    /**
     * 保存一组消息
     *
     * @param messageList 消息队列
     *
     * @return 成功插入的行ID集合
     */
    public List<Long> save(List<Message> messageList) {
        Log.i(LOG_TAG + "save", "save(List<Message>) is invoked");

        // 得到数据库写对象
        SQLiteDatabase dbWriter = messageSQLite.getWritableDatabase();

        // 将要返回的新消息ID
        List<Long> newRowIds = new ArrayList<>();

        if (messageList != null) {
            Log.i(LOG_TAG + "save", "message list count is " + messageList.size());

            for (Message message : messageList) {
                Log.i(LOG_TAG + "save", "message id is " + message.getMessageId());
                // 数据库值对象
                ContentValues cv = getContentValues(message);

                // 写入数据
                long newRowId = dbWriter.insert(MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, null, cv);
                // 插入成功则保存行ID
                if (newRowId > -1) {
                    newRowIds.add(newRowId);
                }
                Log.i(LOG_TAG + "save", "new row id is " + newRowId);
            }
        } else {
            Log.d(LOG_TAG + "save", "message list is null");
        }

        dbWriter.close();

        return newRowIds;
    }

    /**
     * 更新一条消息
     *
     * @param message 要更新的消息对象
     */
    public void update(Message message) {
        Log.i(LOG_TAG + "update", "update(Message) is invoked");

        if (message == null) {
            Log.i(LOG_TAG + "update", "message is null");
            return;
        }

        // 得到数据库写对象
        SQLiteDatabase dbWriter = messageSQLite.getWritableDatabase();

        // 数据库值对象
        ContentValues cv = getContentValues(message);

        // 定位消息行的where子句，根据消息ID
        String whereSql = MessageConst.MESSAGE_ID + " = ?";

        // where子句的参数
        String[] whereArgs = new String[]{String.valueOf(message.getMessageId())};

        // 执行更新
        int rowCount = dbWriter.update(MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, cv, whereSql, whereArgs);

        Log.i(LOG_TAG + "update", "update row count is " + rowCount);

        dbWriter.close();
    }

    /**
     * 删除一条消息
     *
     * @param message 要删除的消息对象
     */
    public void delete(Message message) {
        Log.i(LOG_TAG + "delete", "delete(Message) is invoked");

        if (message == null) {
            Log.i(LOG_TAG + "delete", "message is null");
            return;
        }

        // 得到数据库写对象
        SQLiteDatabase dbWriter = messageSQLite.getWritableDatabase();

        // 定位消息行的where子句，根据消息ID
        String whereSql = MessageConst.MESSAGE_ID + " = ?";

        // where子句的参数
        String[] whereArgs = new String[]{String.valueOf(message.getMessageId())};

        // 执行删除
        int rowCount = dbWriter.delete(MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, whereSql, whereArgs);

        Log.i(LOG_TAG + "delete", "delete row count is " + rowCount);

        dbWriter.close();
    }

    /**
     * 查询所有消息数据
     *
     * @return 置于开始位置的游标
     */
    private Cursor queryAll() {
        Log.i(LOG_TAG + "queryAll", "queryAll() is invoked");

        dbReader = messageSQLite.getReadableDatabase();

        // 查询全部数据的sql，不包括已删除的消息
        String sql = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s where %s=0 ORDER BY %s desc", MessageConst.MESSAGE_ID, MessageConst.MESSAGE_SEND_USER, MessageConst.MESSAGE_ABSTRACT, MessageConst.MESSAGE_CONTENT, MessageConst.MESSAGE_TIME, MessageConst.MESSAGE_READ_STATE, MessageConst.MESSAGE_DELETE_STATE, MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, MessageConst.MESSAGE_DELETE_STATE, MessageConst.MESSAGE_ID);
        Log.i(LOG_TAG + "queryAll", "SQL is " + sql);
        // 执行查询并得到游标
        Cursor cursor = dbReader.rawQuery(sql, null);
        Log.i(LOG_TAG + "queryAll", "message count is " + cursor.getCount());
        return cursor;
    }

    /**
     * 查询所有已删除消息消息数据
     *
     * @return 置于开始位置的游标
     */
    private Cursor queryDelete() {
        Log.i(LOG_TAG + "queryDelete", "queryDelete() is invoked");

        dbReader = messageSQLite.getReadableDatabase();

        // 查询全部已删除数据的sql
        String sql = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s where %s=1 ORDER BY %s desc", MessageConst.MESSAGE_ID, MessageConst.MESSAGE_SEND_USER, MessageConst.MESSAGE_ABSTRACT, MessageConst.MESSAGE_CONTENT, MessageConst.MESSAGE_TIME, MessageConst.MESSAGE_READ_STATE, MessageConst.MESSAGE_DELETE_STATE, MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, MessageConst.MESSAGE_DELETE_STATE, MessageConst.MESSAGE_ID);
        Log.i(LOG_TAG + "queryDelete", "SQL is " + sql);
        // 执行查询并得到游标
        Cursor cursor = dbReader.rawQuery(sql, null);
        Log.i(LOG_TAG + "queryDelete", "message count is " + cursor.getCount());
        return cursor;
    }

    /**
     * 查询所有消息
     *
     * @return 消息列表
     */
    public List<Message> queryAllForMessage() {
        Log.i(LOG_TAG + "queryAllForMessage", "queryAllForMessage() is invoked");

        // 获取全部数据的游标
        Cursor cursor = queryAll();

        return getMessageList(cursor);
    }

    /**
     * 查询已删除的消息
     *
     * @return 消息列表
     */
    public List<Message> queryDeleteForMessage() {
        Log.i(LOG_TAG + "queryDeleteForMessage", "queryDeleteForMessage() is invoked");
        // 获取全部数据的游标
        Cursor cursor = queryDelete();

        return getMessageList(cursor);
    }

    /**
     * 装填一组消息
     *
     * @param cursor 数据库游标
     *
     * @return 消息队列
     */
    private List<Message> getMessageList(Cursor cursor) {
        Log.i(LOG_TAG + "getMessageList", "getMessageList(Cursor) is invoked");

        // 获取全部列索引
        int messageIdIndex = cursor.getColumnIndex(MessageConst.MESSAGE_ID);
        int messageSendUserIndex = cursor.getColumnIndex(MessageConst.MESSAGE_SEND_USER);
        int messageAbstractIndex = cursor.getColumnIndex(MessageConst.MESSAGE_ABSTRACT);
        int messageContentIndex = cursor.getColumnIndex(MessageConst.MESSAGE_CONTENT);
        int messageTimeIndex = cursor.getColumnIndex(MessageConst.MESSAGE_TIME);
        int messageReadStateIndex = cursor.getColumnIndex(MessageConst.MESSAGE_READ_STATE);
        int messageDeleteStateIndex = cursor.getColumnIndex(MessageConst.MESSAGE_DELETE_STATE);

        // 新建消息列表
        List<Message> messageList = new ArrayList<>();

        // 填充数据
        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setMessageId(cursor.getInt(messageIdIndex));
            message.setSendUser(cursor.getString(messageSendUserIndex));
            message.setMessageAbstract(cursor.getString(messageAbstractIndex));
            message.setMessageContent(cursor.getString(messageContentIndex));
            message.setMessageTime(cursor.getString(messageTimeIndex));
            message.setReadState(cursor.getInt(messageReadStateIndex) == 1);
            message.setDeleteState(cursor.getInt(messageDeleteStateIndex) == 1);
            messageList.add(message);
            Log.i(LOG_TAG + "getMessageList", "read a message id is " + message.getMessageId());
        }
        Log.i(LOG_TAG + "getMessageList", "message list count is " + messageList.size());

        cursor.close();
        dbReader.close();
        return messageList;
    }

    /**
     * 按消息ID查询消息
     *
     * @param MessageId 消息ID
     *
     * @return 消息对象，没有返回null
     */
    public Message queryByMessageId(int MessageId) {
        Log.i(LOG_TAG + "queryByMessageId", "queryByMessageId(int) is invoked");

        // 按消息ID查询的sql
        String sql = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s", MessageConst.MESSAGE_ID, MessageConst.MESSAGE_SEND_USER, MessageConst.MESSAGE_ABSTRACT, MessageConst.MESSAGE_CONTENT, MessageConst.MESSAGE_TIME, MessageConst.MESSAGE_READ_STATE, MessageConst.MESSAGE_DELETE_STATE, MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, MessageConst.MESSAGE_ID, MessageId);
        Log.i(LOG_TAG + "queryByMessageId", "SQL is " + sql);
        return getSingleMessage(sql);
    }

    /**
     * 装填一条消息
     *
     * @param sql SQL语句
     *
     * @return 消息对象
     */
    private Message getSingleMessage(String sql) {
        Log.i(LOG_TAG + "getSingleMessage", "getSingleMessage(String) is invoked");

        Log.i(LOG_TAG + "getSingleMessage", "sql is " + sql);

        // 获取读取对象
        dbReader = messageSQLite.getReadableDatabase();

        // 执行查询并得到游标
        Cursor cursor = dbReader.rawQuery(sql, null);

        Message message = null;
        // 有结果则取值
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            message = new Message();
            message.setMessageId(CursorUtil.getInt(cursor, MessageConst.MESSAGE_ID));
            message.setSendUser(CursorUtil.getString(cursor, MessageConst.MESSAGE_SEND_USER));
            message.setMessageAbstract(CursorUtil.getString(cursor, MessageConst.MESSAGE_ABSTRACT));
            message.setMessageContent(CursorUtil.getString(cursor, MessageConst.MESSAGE_CONTENT));
            message.setMessageTime(CursorUtil.getString(cursor, MessageConst.MESSAGE_TIME));
            message.setReadState(CursorUtil.getBoolean(cursor, MessageConst.MESSAGE_READ_STATE));
            message.setDeleteState(CursorUtil.getBoolean(cursor, MessageConst.MESSAGE_DELETE_STATE));
            Log.d(LOG_TAG + "getSingleMessage", "this message id is " + message.getMessageId());
        }
        cursor.close();
        dbReader.close();

        return message;
    }

    /**
     * 按数据库行ID查询消息
     *
     * @param id 指定行ID
     *
     * @return 消息对象
     */
    public Message queryById(long id) {
        Log.i(LOG_TAG + "queryById", "queryById(long) is invoked");

        // 按ID查询的sql
        String sql = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s = %s", MessageConst.MESSAGE_ID, MessageConst.MESSAGE_SEND_USER, MessageConst.MESSAGE_ABSTRACT, MessageConst.MESSAGE_CONTENT, MessageConst.MESSAGE_TIME, MessageConst.MESSAGE_READ_STATE, MessageConst.MESSAGE_DELETE_STATE, MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, MessageConst._ID, id);
        Log.i(LOG_TAG + "queryById", "SQL is " + sql);
        return getSingleMessage(sql);
    }

    /**
     * 按数据库行ID段查询消息队列，
     * 要求startID小于等于endID
     *
     * @param startID 起始ID
     * @param endID   结束ID
     *
     * @return 消息队列
     */
    public List<Message> queryByIds(long startID, long endID) {
        Log.i(LOG_TAG + "queryByIds", "queryByIds(long,long) is invoked");

        dbReader = messageSQLite.getReadableDatabase();

        // 查询全部数据的sql
        String sql = String.format("SELECT %s, %s, %s, %s, %s, %s, %s FROM %s WHERE %s>=%s AND %s<%s ORDER BY %s desc", MessageConst.MESSAGE_ID, MessageConst.MESSAGE_SEND_USER, MessageConst.MESSAGE_ABSTRACT, MessageConst.MESSAGE_CONTENT, MessageConst.MESSAGE_TIME, MessageConst.MESSAGE_READ_STATE, MessageConst.MESSAGE_DELETE_STATE, MessageConst.MESSAGE_TABLE_NAME + tableNameSuffix, MessageConst._ID, startID, MessageConst._ID, endID, MessageConst.MESSAGE_ID);

        Log.i(LOG_TAG + "queryByIds", "SQL is " + sql);

        // 执行查询并得到游标
        Cursor cursor = dbReader.rawQuery(sql, null);
        cursor.moveToFirst();

        Log.i(LOG_TAG + "queryByIds", "message count is " + cursor.getCount());

        return getMessageList(cursor);
    }

    /**
     * 关闭数据库
     */
    public void close() {
        Log.i(LOG_TAG + "close", "close() is invoked");

        if (dbReader != null && dbReader.isOpen()) {
            dbReader.close();
        }
        if (messageSQLite != null) {
            messageSQLite.close();
        }
    }
}
