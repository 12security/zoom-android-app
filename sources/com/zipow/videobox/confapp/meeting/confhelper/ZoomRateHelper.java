package com.zipow.videobox.confapp.meeting.confhelper;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;

public class ZoomRateHelper {
    private static final String GOOD_QUALITY_MEETING_TIMES = "good_quality_meeting_times";
    public static final String GOOGLE_PLAY = "com.android.vending";

    @NonNull
    public static String getGoodQualityMeetingTimesKey(@Nullable Context context) {
        if (context == null) {
            return GOOD_QUALITY_MEETING_TIMES;
        }
        try {
            String str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            StringBuilder sb = new StringBuilder();
            sb.append("good_quality_meeting_times_");
            sb.append(str);
            return sb.toString();
        } catch (Exception unused) {
            return GOOD_QUALITY_MEETING_TIMES;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        r2 = new android.content.Intent("android.intent.action.VIEW");
        r3 = new java.lang.StringBuilder();
        r3.append("https://play.google.com/store/apps/details?id=");
        r3.append(r0);
        r2.setData(android.net.Uri.parse(r3.toString()));
        r2.addFlags(268435456);
        com.zipow.videobox.util.ActivityStartHelper.startActivityForeground(r5, r2);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x003b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void launchGooglePlayAppDetail(@androidx.annotation.Nullable android.content.Context r5) {
        /*
            if (r5 != 0) goto L_0x0003
            return
        L_0x0003:
            java.lang.String r0 = r5.getPackageName()
            r1 = 268435456(0x10000000, float:2.5243549E-29)
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x003b }
            if (r2 == 0) goto L_0x0010
            return
        L_0x0010:
            android.content.Intent r2 = new android.content.Intent     // Catch:{ Exception -> 0x003b }
            java.lang.String r3 = "android.intent.action.VIEW"
            r2.<init>(r3)     // Catch:{ Exception -> 0x003b }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x003b }
            r3.<init>()     // Catch:{ Exception -> 0x003b }
            java.lang.String r4 = "https://play.google.com/store/apps/details?id="
            r3.append(r4)     // Catch:{ Exception -> 0x003b }
            r3.append(r0)     // Catch:{ Exception -> 0x003b }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x003b }
            android.net.Uri r3 = android.net.Uri.parse(r3)     // Catch:{ Exception -> 0x003b }
            r2.setData(r3)     // Catch:{ Exception -> 0x003b }
            r2.addFlags(r1)     // Catch:{ Exception -> 0x003b }
            java.lang.String r3 = "com.android.vending"
            r2.setPackage(r3)     // Catch:{ Exception -> 0x003b }
            com.zipow.videobox.util.ActivityStartHelper.startActivityForeground(r5, r2)     // Catch:{ Exception -> 0x003b }
            goto L_0x0060
        L_0x003b:
            android.content.Intent r2 = new android.content.Intent     // Catch:{ Exception -> 0x0060 }
            java.lang.String r3 = "android.intent.action.VIEW"
            r2.<init>(r3)     // Catch:{ Exception -> 0x0060 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0060 }
            r3.<init>()     // Catch:{ Exception -> 0x0060 }
            java.lang.String r4 = "https://play.google.com/store/apps/details?id="
            r3.append(r4)     // Catch:{ Exception -> 0x0060 }
            r3.append(r0)     // Catch:{ Exception -> 0x0060 }
            java.lang.String r0 = r3.toString()     // Catch:{ Exception -> 0x0060 }
            android.net.Uri r0 = android.net.Uri.parse(r0)     // Catch:{ Exception -> 0x0060 }
            r2.setData(r0)     // Catch:{ Exception -> 0x0060 }
            r2.addFlags(r1)     // Catch:{ Exception -> 0x0060 }
            com.zipow.videobox.util.ActivityStartHelper.startActivityForeground(r5, r2)     // Catch:{ Exception -> 0x0060 }
        L_0x0060:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.confapp.meeting.confhelper.ZoomRateHelper.launchGooglePlayAppDetail(android.content.Context):void");
    }

    public int getMeetingScore() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (videoObj == null || audioObj == null) {
            return 0;
        }
        return videoObj.getMeetingScore();
    }

    public long getMeetingElapsedMinute() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            return 0;
        }
        return confStatusObj.getMeetingElapsedTimeInSecs() / 60;
    }
}
