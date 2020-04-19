package com.zipow.videobox.view.sip;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.kubi.KubiContract;

class HomeKeyMonitorReceiver extends BroadcastReceiver {
    final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    final String SYSTEM_DIALOG_REASON_KEY = KubiContract.EXTRA_REASON;
    @Nullable
    private SimpleHomekeyListener mHomekeyListener = null;

    public interface SimpleHomekeyListener {
        void onHomeKeyClick();
    }

    public HomeKeyMonitorReceiver(@Nullable SimpleHomekeyListener simpleHomekeyListener) {
        this.mHomekeyListener = simpleHomekeyListener;
    }

    /* access modifiers changed from: 0000 */
    public void register(@NonNull Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(this, intentFilter);
    }

    /* access modifiers changed from: 0000 */
    public void unregister(@NonNull Context context) {
        context.unregisterReceiver(this);
    }

    @SuppressLint({"MissingPermission"})
    public void onReceive(Context context, @Nullable Intent intent) {
        if (intent != null) {
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra(KubiContract.EXTRA_REASON);
                if (stringExtra != null && stringExtra.equals("homekey")) {
                    SimpleHomekeyListener simpleHomekeyListener = this.mHomekeyListener;
                    if (simpleHomekeyListener != null) {
                        simpleHomekeyListener.onHomeKeyClick();
                    }
                }
            }
        }
    }
}
