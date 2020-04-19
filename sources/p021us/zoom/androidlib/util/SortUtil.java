package p021us.zoom.androidlib.util;

import com.zipow.videobox.util.TextCommandHelper;
import java.util.Locale;

/* renamed from: us.zoom.androidlib.util.SortUtil */
public class SortUtil {
    private SortUtil() {
    }

    public static String getSortKey(String str, Locale locale) {
        if (str == null || str.length() == 0) {
            return "!";
        }
        if (locale == null) {
            return str;
        }
        String sortKey = PinyinUtil.getSortKey(str);
        if (sortKey.length() == 0) {
            return sortKey;
        }
        char groupChar = getGroupChar(sortKey);
        if ('#' == groupChar) {
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(groupChar));
            sb.append(sortKey);
            sortKey = sb.toString();
        }
        return sortKey;
    }

    private static char getGroupChar(String str) {
        if (str.length() == 0) {
            return TextCommandHelper.CHANNEL_CMD_CHAR;
        }
        char charAt = str.charAt(0);
        if (charAt >= 'a' && charAt <= 'z') {
            return (char) (charAt - ' ');
        }
        if (charAt < 'A' || charAt > 'Z') {
            return TextCommandHelper.CHANNEL_CMD_CHAR;
        }
        return charAt;
    }

    public static int fastCompare(String str, String str2) {
        int length = str.length();
        int length2 = str2.length();
        int min = Math.min(length, length2);
        for (int i = 0; i < min; i++) {
            char charAt = str.charAt(i);
            char charAt2 = str2.charAt(i);
            if (charAt != charAt2) {
                return charAt - charAt2;
            }
        }
        return length - length2;
    }
}
