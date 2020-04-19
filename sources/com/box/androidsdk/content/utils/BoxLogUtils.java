package com.box.androidsdk.content.utils;

import android.util.Log;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class BoxLogUtils {
    public static boolean getIsLoggingEnabled() {
        return false;
    }

    /* renamed from: i */
    public static void m13i(String str, String str2) {
        if (getIsLoggingEnabled()) {
            Log.i(str, str2);
        }
    }

    /* renamed from: i */
    public static void m14i(String str, String str2, Map<String, String> map) {
        if (getIsLoggingEnabled() && map != null) {
            for (Entry entry : map.entrySet()) {
                Log.i(str, String.format(Locale.ENGLISH, "%s:  %s:%s", new Object[]{str2, entry.getKey(), entry.getValue()}));
            }
        }
    }

    /* renamed from: d */
    public static void m10d(String str, String str2) {
        if (getIsLoggingEnabled()) {
            Log.d(str, str2);
        }
    }

    /* renamed from: e */
    public static void m11e(String str, String str2) {
        if (getIsLoggingEnabled()) {
            Log.e(str, str2);
        }
    }

    /* renamed from: e */
    public static void m12e(String str, String str2, Throwable th) {
        if (getIsLoggingEnabled()) {
            Log.e(str, str2, th);
        }
    }
}
