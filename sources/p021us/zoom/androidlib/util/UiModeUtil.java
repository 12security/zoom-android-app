package p021us.zoom.androidlib.util;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;

/* renamed from: us.zoom.androidlib.util.UiModeUtil */
public class UiModeUtil {
    public static boolean isInDesktopMode(Context context) {
        boolean z = false;
        if (context == null) {
            return false;
        }
        if (isSamsungDesktopModeEnable(context)) {
            return true;
        }
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService("uimode");
        if (uiModeManager == null) {
            return false;
        }
        if (uiModeManager.getCurrentModeType() == 2) {
            z = true;
        }
        return z;
    }

    private static boolean isSamsungDesktopModeEnable(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration == null) {
            return false;
        }
        try {
            Class cls = configuration.getClass();
            if (cls.getField("SEM_DESKTOP_MODE_ENABLED").getInt(cls) == cls.getField("semDesktopModeEnabled").getInt(configuration)) {
                return true;
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
        }
        return false;
    }
}
