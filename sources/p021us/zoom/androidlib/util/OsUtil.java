package p021us.zoom.androidlib.util;

import android.os.Build.VERSION;

/* renamed from: us.zoom.androidlib.util.OsUtil */
public class OsUtil {
    public static boolean isAtLeastICS_MR1() {
        return true;
    }

    public static boolean isAtLeastJB() {
        return VERSION.SDK_INT >= 16;
    }

    public static boolean isAtLeastJB_MR1() {
        return VERSION.SDK_INT >= 17;
    }

    public static boolean isAtLeastJB_MR2() {
        return VERSION.SDK_INT >= 18;
    }

    public static boolean isAtLeastKLP() {
        return VERSION.SDK_INT >= 19;
    }

    public static boolean isAtLeastL() {
        return VERSION.SDK_INT >= 21;
    }

    public static boolean isAtLeastL_MR1() {
        return VERSION.SDK_INT >= 22;
    }

    public static boolean isAtLeastM() {
        return VERSION.SDK_INT >= 23;
    }

    public static boolean isAtLeastN() {
        return VERSION.SDK_INT >= 24;
    }

    public static boolean isAtLeastN_MR1() {
        return VERSION.SDK_INT >= 25;
    }

    public static boolean isAtLeastO() {
        return VERSION.SDK_INT >= 26;
    }

    public static boolean isAtLeastP() {
        return VERSION.SDK_INT >= 28;
    }

    public static boolean isAtLeastQ() {
        return VERSION.SDK_INT >= 29;
    }
}
