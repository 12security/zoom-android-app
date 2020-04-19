package p021us.zoom.androidlib.util;

import java.util.Locale;

/* renamed from: us.zoom.androidlib.util.DefaultNameAbbrGenerator */
public class DefaultNameAbbrGenerator implements INameAbbrGenerator {
    public String getNameAbbreviation(String str, Locale locale) {
        String str2;
        if (str == null) {
            return "";
        }
        String[] split = str.split("\\s");
        if (split.length >= 2) {
            if (!StringUtil.isAllAssii(split[0]) || !StringUtil.isAllAssii(split[1])) {
                String str3 = "";
                if (split[0].length() > 0) {
                    str3 = String.valueOf(split[0].charAt(0));
                }
                str2 = String.valueOf(str3);
            } else {
                String str4 = "";
                String str5 = "";
                if (split[0].length() > 0) {
                    str4 = String.valueOf(split[0].charAt(0));
                }
                if (split[1].length() > 0) {
                    str5 = String.valueOf(split[1].charAt(0));
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str4);
                sb.append(str5);
                str2 = sb.toString();
            }
        } else if (str.length() == 0) {
            str2 = "";
        } else {
            str2 = String.valueOf(str.charAt(0));
        }
        return str2.toUpperCase(CompatUtils.getLocalDefault());
    }
}
