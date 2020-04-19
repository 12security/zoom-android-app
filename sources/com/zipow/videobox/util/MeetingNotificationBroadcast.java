package com.zipow.videobox.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.ptapp.IncomingCallManager;
import p021us.zoom.androidlib.util.StringUtil;

public class MeetingNotificationBroadcast extends BroadcastReceiver {
    public static final String ACTION_MEETING_CALL_ACCEPT = "us.zoom.videomeetings.intent.action.MEETING_ACCEPT";
    public static final String ACTION_MEETING_CALL_DELINE = "us.zoom.videomeetings.intent.action.MEETING_DELINE";
    public static final String ACTION_MEETING_CALL_NOTIFI = "us.zoom.videomeetings.intent.action.MEETING_NOTIFI";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (context != null && !StringUtil.isEmptyOrNull(action)) {
            if (action.equals(ACTION_MEETING_CALL_ACCEPT)) {
                IncomingCallManager.getInstance().acceptCall(context, false);
            } else if (action.equals(ACTION_MEETING_CALL_DELINE)) {
                if (IncomingCallManager.getInstance().declineCall()) {
                    NotificationMgr.removeNotification(context, 11);
                }
            } else if (action.equals(ACTION_MEETING_CALL_NOTIFI)) {
                String stringExtra = intent.getStringExtra(IntegrationActivity.ARG_CALL_BODY);
                String stringExtra2 = intent.getStringExtra(IntegrationActivity.ARG_CALL_CAPTION);
                if (!StringUtil.isEmptyOrNull(stringExtra) && !StringUtil.isEmptyOrNull(stringExtra2)) {
                    ZmPtUtils.handleActionNosIncomingCall(stringExtra, stringExtra2);
                }
            }
        }
    }
}
