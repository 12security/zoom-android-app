package p021us.zoom.androidlib.util;

import java.util.Locale;

/* renamed from: us.zoom.androidlib.util.ZMLocaleUtils */
public class ZMLocaleUtils {
    public static boolean isEnglishLanguage() {
        Locale localDefault = CompatUtils.getLocalDefault();
        if (localDefault == null) {
            return true;
        }
        String language = localDefault.getLanguage();
        if (StringUtil.isEmptyOrNull(language) || !language.trim().toLowerCase().equals("en")) {
            return false;
        }
        return true;
    }

    public static boolean isChineseLanguage() {
        Locale localDefault = CompatUtils.getLocalDefault();
        if (localDefault == null) {
            return true;
        }
        String language = localDefault.getLanguage();
        if (StringUtil.isEmptyOrNull(language) || !language.trim().toLowerCase().equals("zh")) {
            return false;
        }
        return true;
    }
}
