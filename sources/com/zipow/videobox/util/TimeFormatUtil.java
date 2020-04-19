package com.zipow.videobox.util;

import android.content.Context;
import androidx.annotation.NonNull;
import java.util.HashMap;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.template.Template;
import p021us.zoom.videomeetings.C4558R;

public class TimeFormatUtil {
    public static String formatTime(Context context, long j) {
        return TimeUtil.formatTime(context, j);
    }

    public static String formatDate(Context context, long j, boolean z) {
        if (z) {
            return TimeUtil.formatDateWithYear(context, j);
        }
        return TimeUtil.formatDate(context, j);
    }

    public static String formatDateTime(@NonNull Context context, long j, boolean z) {
        return formatDateTime(context, j, z, true);
    }

    public static String formatDateTime(@NonNull Context context, long j, boolean z, boolean z2) {
        if (z2) {
            int dateDiff = TimeUtil.dateDiff(j, System.currentTimeMillis());
            if (dateDiff == 0) {
                HashMap hashMap = new HashMap();
                hashMap.put("time", formatTime(context, j));
                return new Template(context.getString(C4558R.string.zm_today_time)).format(hashMap);
            } else if (dateDiff == 1) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("time", formatTime(context, j));
                return new Template(context.getString(C4558R.string.zm_tomorrow_time)).format(hashMap2);
            } else if (dateDiff == -1) {
                HashMap hashMap3 = new HashMap();
                hashMap3.put("time", formatTime(context, j));
                return new Template(context.getString(C4558R.string.zm_yesterday_time)).format(hashMap3);
            }
        }
        HashMap hashMap4 = new HashMap();
        hashMap4.put("date", formatDate(context, j, z));
        hashMap4.put("time", formatTime(context, j));
        return new Template(context.getString(C4558R.string.zm_date_time)).format(hashMap4);
    }
}
