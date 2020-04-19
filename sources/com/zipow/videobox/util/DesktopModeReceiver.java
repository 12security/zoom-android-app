package com.zipow.videobox.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.Nullable;

public class DesktopModeReceiver extends BroadcastReceiver {
    private static final String SEM_ACTION_ENTER_KNOX_DESKTOP_MODE = "android.app.action.ENTER_KNOX_DESKTOP_MODE";
    private static final String SEM_ACTION_EXIT_KNOX_DESKTOP_MODE = "android.app.action.EXIT_KNOX_DESKTOP_MODE";
    @Nullable
    private DesktopModeListener mListener = null;

    public interface DesktopModeListener {
        void onDesktopModeChange(boolean z);
    }

    public void setListener(@Nullable DesktopModeListener desktopModeListener) {
        this.mListener = desktopModeListener;
    }

    public void registerReceiver(@Nullable Context context) {
        if (context != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SEM_ACTION_ENTER_KNOX_DESKTOP_MODE);
            intentFilter.addAction(SEM_ACTION_EXIT_KNOX_DESKTOP_MODE);
            context.registerReceiver(this, intentFilter);
        }
    }

    public void unregisterReceiver(@Nullable Context context) {
        if (context != null) {
            context.unregisterReceiver(this);
        }
    }

    public void onReceive(Context context, @Nullable Intent intent) {
        if (intent != null && this.mListener != null) {
            String action = intent.getAction();
            if (SEM_ACTION_ENTER_KNOX_DESKTOP_MODE.equals(action)) {
                this.mListener.onDesktopModeChange(true);
            } else if (SEM_ACTION_EXIT_KNOX_DESKTOP_MODE.equals(action)) {
                this.mListener.onDesktopModeChange(false);
            }
        }
    }
}
