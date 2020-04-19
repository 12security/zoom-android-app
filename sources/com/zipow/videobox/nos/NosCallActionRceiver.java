package com.zipow.videobox.nos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IncomingCallManager;
import com.zipow.videobox.util.NotificationMgr;

public class NosCallActionRceiver extends BroadcastReceiver {
    public void onReceive(Context context, @Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (action.equals(NotificationMgr.ZOOM_NOS_CALL_ACCEPT_ACTION)) {
                handleAcceptAction(context, intent);
            } else if (action.equals(NotificationMgr.ZOOM_NOS_CALL_CANCEL_ACTION)) {
                handleCancelAction(context);
            }
        }
    }

    private void handleCancelAction(@Nullable Context context) {
        if (context != null) {
            NotificationMgr.removeNosCallNotification(context);
        }
    }

    private void handleAcceptAction(@Nullable Context context, @Nullable Intent intent) {
        if (!(context == null || intent == null)) {
            String stringExtra = intent.getStringExtra(NotificationMgr.KEY_NOS_CALL_MESSAGE_BODY);
            intent.getIntExtra(NotificationMgr.KEY_NOS_CALL_MESSAGE_TYPE, 0);
            if (!TextUtils.isEmpty(stringExtra)) {
                String[] split = stringExtra.split(";");
                String str = "";
                String str2 = "";
                if (split.length >= 10) {
                    String str3 = split[0];
                    str = split[1];
                    str2 = split[2];
                    String str4 = split[3];
                    String str5 = split[4];
                    String str6 = split[5];
                    String str7 = split[6];
                    String str8 = split[7];
                    String str9 = split[8];
                    String str10 = split[9];
                }
                try {
                    Long.parseLong(str);
                    Long.parseLong(str2);
                } catch (Exception unused) {
                }
                IncomingCallManager instance = IncomingCallManager.getInstance();
                instance.initialize(context);
                instance.acceptCall(context, true);
            }
        }
    }
}
