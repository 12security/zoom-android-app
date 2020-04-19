package p021us.zoom.androidlib.util;

import java.io.File;

/* renamed from: us.zoom.androidlib.util.RootCheckUtils */
public class RootCheckUtils {
    private static final String TAG = "us.zoom.androidlib.util.RootCheckUtils";
    private static boolean g_continueToUseWhenRooted = false;

    public static final boolean isRooted() {
        return checkRootMethod1() && checkRootMethod2();
    }

    public static final void testIsRooted() {
        checkRootMethod1();
        checkRootMethod2();
    }

    private static boolean checkRootMethod1() {
        for (String file : new String[]{"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"}) {
            if (new File(file).exists()) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkRootMethod2() {
        try {
            Process exec = Runtime.getRuntime().exec("su");
            if (exec != null) {
                exec.destroy();
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean continueToUseWhenRooted() {
        return g_continueToUseWhenRooted;
    }

    public static void setContinueToUseWhenRooted(boolean z) {
        g_continueToUseWhenRooted = z;
    }
}
