package p021us.zoom.androidlib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.accessibility.CaptioningManager;
import java.util.Locale;

/* renamed from: us.zoom.androidlib.util.CaptionUtil */
public class CaptionUtil {
    private static final float DEFAULT_FONT_SCALE = 1.0f;
    public static final int DEFAULT_TEXT_SIZE = 16;

    public static CaptionStyleCompat getCaptionStyle(Context context) {
        if (VERSION.SDK_INT < 19 || !isCaptionEnabled(context)) {
            return CaptionStyleCompat.DEFAULT;
        }
        return getUserCaptionStyleV19(context);
    }

    public static float getCaptionFontScale(Context context) {
        return (VERSION.SDK_INT < 19 || !isCaptionEnabled(context)) ? DEFAULT_FONT_SCALE : getUserCaptionFontScaleV19(context);
    }

    public static boolean isCaptionEnabled(Context context) {
        if (VERSION.SDK_INT >= 19) {
            return isCaptionEnabledV19(context);
        }
        return false;
    }

    public static Locale getLocale(Context context) {
        if (VERSION.SDK_INT < 19 || !isCaptionEnabled(context)) {
            return CompatUtils.getLocalDefault();
        }
        return getLocaleV19(context);
    }

    @TargetApi(19)
    private static boolean isCaptionEnabledV19(Context context) {
        if (context == null) {
            return false;
        }
        CaptioningManager captioningManager = (CaptioningManager) context.getSystemService("captioning");
        if (captioningManager == null) {
            return false;
        }
        return captioningManager.isEnabled();
    }

    @TargetApi(19)
    private static Locale getLocaleV19(Context context) {
        CaptioningManager captioningManager = (CaptioningManager) context.getSystemService("captioning");
        if (captioningManager == null) {
            return CompatUtils.getLocalDefault();
        }
        return captioningManager.getLocale();
    }

    @TargetApi(19)
    private static CaptionStyleCompat getUserCaptionStyleV19(Context context) {
        CaptioningManager captioningManager = (CaptioningManager) context.getSystemService("captioning");
        if (captioningManager == null) {
            return CaptionStyleCompat.DEFAULT;
        }
        return CaptionStyleCompat.createFromCaptionStyle(captioningManager.getUserStyle());
    }

    @TargetApi(19)
    private static float getUserCaptionFontScaleV19(Context context) {
        CaptioningManager captioningManager = (CaptioningManager) context.getSystemService("captioning");
        if (captioningManager == null) {
            return DEFAULT_FONT_SCALE;
        }
        return captioningManager.getFontScale();
    }
}
