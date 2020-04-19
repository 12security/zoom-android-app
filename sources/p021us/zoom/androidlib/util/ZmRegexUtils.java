package p021us.zoom.androidlib.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.regex.Pattern;

/* renamed from: us.zoom.androidlib.util.ZmRegexUtils */
public class ZmRegexUtils {
    public static final String CHINA_MOBILE_NUMBER = "^[1][\\d]{10}$";

    public static boolean isValidForRegex(@NonNull String str, @Nullable String str2) {
        if (StringUtil.isEmptyOrNull(str2)) {
            return false;
        }
        return Pattern.compile(str).matcher(str2).matches();
    }
}
