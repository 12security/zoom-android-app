package com.zipow.videobox.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;

public class QueryRequestReceiver extends BroadcastReceiver {
    private static final String ACTION_QUERY_MEETING_STATUS = "us.zoom.videomeetings.intent.action.QUERY_MEETING_STATUS";
    @NonNull
    private Handler mHandler = new Handler();

    public void onReceive(@NonNull final Context context, @NonNull Intent intent) {
        if (ACTION_QUERY_MEETING_STATUS.equalsIgnoreCase(intent.getAction())) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    QueryRequestReceiver.this.broadcastMeetingStatus(context);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void broadcastMeetingStatus(@NonNull Context context) {
        if (!Mainboard.getMainboard().isInitialized()) {
            SDKHost.broadcastMeetingStatus(context, 0);
            return;
        }
        switch (PTApp.getInstance().getCallStatus()) {
            case 1:
                SDKHost.broadcastMeetingStatus(context, 1);
                break;
            case 2:
                SDKHost.broadcastMeetingStatus(context, 2);
                break;
            default:
                SDKHost.broadcastMeetingStatus(context, 0);
                break;
        }
    }
}
