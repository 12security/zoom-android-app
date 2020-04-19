package com.zipow.videobox.view.sip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.annotation.NonNull;

public class SipHeadsetButtonReceiver extends BroadcastReceiver {
    @NonNull
    private static String TAG = "SipHeadsetButtonReceiver";

    public void onReceive(Context context, @NonNull Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.MEDIA_BUTTON".equals(action)) {
            KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
            if (keyEvent != null) {
                int keyCode = keyEvent.getKeyCode();
                if (keyCode != 79) {
                    switch (keyCode) {
                    }
                }
            }
        } else if ("android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED".equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                for (String str : extras.keySet()) {
                }
            }
        }
    }
}
