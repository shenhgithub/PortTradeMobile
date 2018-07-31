package org.port.trade.notify;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.androidpn.client.LogUtil;
import org.port.trade.R;
import org.port.trade.db.androidpn.DBConsts;
import org.port.trade.db.androidpn.DBIQOperator;


public class NotifyListActivity extends ActionBarActivity {
    private static final String LOG_TAG = LogUtil.makeLogTag(NotifyListActivity.class);

    /**
     * 本应用入口包名
     */
    private static final String PACKAGE_NAME = "org.port.trade.activity";

    /**
     * 本应用入口类名
     */
    private static final String CLASS_NAME = "org.port.trade.activity.SplashActivity";

    private DBIQOperator operator;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_list);
        ListView listView = (ListView) findViewById(R.id.notify_list);
        operator = new DBIQOperator(this);
        cursor = operator.queryAll();
        CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_notify_item, cursor, new String[]{DBConsts.IQ_TITLE , DBConsts.IQ_MSG , DBConsts.IQ_TIME}, new int[]{R.id.notify_list_title , R.id.notify_list_msg , R.id.notify_list_time});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                NotifyIQ iq = operator.queryIQBy_Id(id);
                // startActivity(new Intent(NotifyListActivity.this, NotifyDetailActivity.class).putExtra(Constants.INTENT_EXTRA_IQ, iq));

                // 启动本应用程序
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(PACKAGE_NAME, CLASS_NAME);
                intent.setComponent(cn);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        cursor.close();
        operator.closeDB();
        super.onDestroy();
    }
}
