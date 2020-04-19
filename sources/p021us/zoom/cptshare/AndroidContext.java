package p021us.zoom.cptshare;

import android.content.Context;
import androidx.annotation.Nullable;

/* renamed from: us.zoom.cptshare.AndroidContext */
public class AndroidContext {
    private static native void initAppPackageName(String str);

    static {
        System.loadLibrary("zoom_stlport");
        System.loadLibrary("cptshare");
    }

    public static void initialize(@Nullable Context context) {
        if (context != null) {
            String packageName = context.getPackageName();
            if (packageName != null) {
                initAppPackageName(packageName);
                return;
            }
            return;
        }
        throw new NullPointerException("context cannot be null");
    }
}
