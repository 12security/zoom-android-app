package p021us.zoom.androidlib.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import androidx.annotation.Nullable;
import java.util.Locale;

/* renamed from: us.zoom.androidlib.util.LanguageUtil */
public class LanguageUtil {
    public static final String KEY_APP_LOCALE_ID = "app_locale_id";
    public static final String LOCALE_PREFERENCE_NAME = "app_locale_config";

    public static void setAppLocale(Context context, Locale locale) {
        if (context != null && locale != null) {
            Editor edit = context.getSharedPreferences(LOCALE_PREFERENCE_NAME, 0).edit();
            String country = locale.getCountry();
            String language = locale.getLanguage();
            String str = "";
            if (language.length() == 0) {
                language = str;
            } else if (country.length() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(language);
                sb.append("_");
                sb.append(country);
                language = sb.toString();
            }
            edit.putString(KEY_APP_LOCALE_ID, language);
            edit.commit();
        }
    }

    @Nullable
    public static Locale getAppLocale(Context context) {
        Locale locale = null;
        if (context == null) {
            return null;
        }
        String string = context.getSharedPreferences(LOCALE_PREFERENCE_NAME, 0).getString(KEY_APP_LOCALE_ID, null);
        if (!StringUtil.isEmptyOrNull(string)) {
            if (string.contains("_")) {
                String[] split = string.split("_", 2);
                locale = new Locale(split[0], split[1]);
            } else {
                locale = new Locale(string);
            }
        }
        return locale;
    }
}
