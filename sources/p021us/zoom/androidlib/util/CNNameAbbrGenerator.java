package p021us.zoom.androidlib.util;

import java.util.Locale;

/* renamed from: us.zoom.androidlib.util.CNNameAbbrGenerator */
public class CNNameAbbrGenerator implements INameAbbrGenerator {
    public String getNameAbbreviation(String str, Locale locale) {
        String str2;
        if (str == null) {
            return "";
        }
        String[] split = str.split("\\s");
        if (split.length >= 2) {
            if (!StringUtil.isAllAssii(split[0]) || !StringUtil.isAllAssii(split[1])) {
                str2 = getLast2Chars(split[split.length - 1]);
            } else {
                String str3 = "";
                String str4 = "";
                if (split[0].length() > 0) {
                    str3 = String.valueOf(split[0].charAt(0));
                }
                if (split[1].length() > 0) {
                    str4 = String.valueOf(split[1].charAt(0));
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(str4);
                str2 = sb.toString();
            }
        } else if (str.length() == 0) {
            str2 = "";
        } else {
            str2 = getLast2Chars(str);
        }
        return str2.toUpperCase(CompatUtils.getLocalDefault());
    }

    private String getLast2Chars(String str) {
        int length = str.length() - 2;
        if (length < 0) {
            length = 0;
        }
        return str.substring(length);
    }
}
