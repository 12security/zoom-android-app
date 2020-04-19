package com.zipow.videobox.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.ZoomApplication;
import p021us.zoom.androidlib.util.OsUtil;

public class ActivityStartHelper {
    private static final String TAG = "com.zipow.videobox.util.ActivityStartHelper";

    private static boolean isAtFront(Context context) {
        if (context != null) {
            Context applicationContext = context.getApplicationContext();
            if (applicationContext instanceof ZoomApplication) {
                return ((ZoomApplication) applicationContext).isAtFront();
            }
        }
        return false;
    }

    public static void startActivityForResult(Fragment fragment, Intent intent, int i) {
        if (intent != null && fragment != null) {
            fragment.startActivityForResult(intent, i);
        }
    }

    public static boolean startActivityForResult(Activity activity, Intent intent, int i) {
        if (intent == null || activity == null) {
            return false;
        }
        try {
            activity.startActivityForResult(intent, i);
            return true;
        } catch (ActivityNotFoundException unused) {
            return false;
        }
    }

    public static void startActivityForeground(Context context, Intent intent) {
        if (intent != null && context != null) {
            context.startActivity(intent);
        }
    }

    public static void startActivity(Context context, Intent intent, String str, Object obj) {
        if (intent != null && context != null) {
            if (isAtFront(context) || !OsUtil.isAtLeastQ()) {
                context.startActivity(intent);
            } else {
                NotificationMgr.startNotification(context, intent, str, obj);
            }
        }
    }
}
