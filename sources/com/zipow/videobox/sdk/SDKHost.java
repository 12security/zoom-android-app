package com.zipow.videobox.sdk;

import android.content.Context;
import android.content.Intent;

public class SDKHost {
    private static final String ACTION_ZOOM_MEETING_STATUS = "us.zoom.videomeetings.intent.action.MEETING_STATUS";
    private static final String KEY_STATUS = "status";

    public static void broadcastMeetingStatus(Context context, int i) {
        Intent intent = new Intent();
        intent.setAction(ACTION_ZOOM_MEETING_STATUS);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.putExtra("status", i);
        context.sendBroadcast(intent);
    }
}
