package com.zipow.videobox.sip.server;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.NotificationMgr.NotificationItem;

public class CmmPBXMessageNotificationManager {
    public static void onNewMessageReceived(@Nullable Context context, @Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4) {
        if (context != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            NotificationMgr.showPBXMessageNotification(context, str, str4, new NotificationItem(str2, str3));
        }
    }
}
