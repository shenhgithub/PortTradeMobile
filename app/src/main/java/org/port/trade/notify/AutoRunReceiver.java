package org.port.trade.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.androidpn.client.LogUtil;
import org.androidpn.client.ServiceManager;
import org.port.trade.R;


public class AutoRunReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = LogUtil.makeLogTag(AutoRunReceiver.class);

    public AutoRunReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Log.d(LOG_TAG, "action_boot_completed");
            final ServiceManager serviceManager = new ServiceManager(context);
            serviceManager.setNotificationIcon(R.drawable.ic_launcher);
            serviceManager.startService();
        }
    }
}
