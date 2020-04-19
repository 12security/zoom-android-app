package com.onedrive.sdk.serializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class CalendarSerializer {
    private CalendarSerializer() {
    }

    public static Calendar deserialize(String str) throws ParseException {
        String str2;
        String str3;
        if (str.indexOf(90) != -1) {
            str2 = "Z";
            str = str.replace("Z", "+0000");
        } else {
            str2 = "";
        }
        if (str.contains(".")) {
            String substring = str.substring(str.indexOf(".") + 1, str.indexOf("+"));
            if (substring.length() > 3) {
                String substring2 = substring.substring(0, 3);
                StringBuilder sb = new StringBuilder();
                sb.append(str.substring(0, str.indexOf(".") + 1));
                sb.append(substring2);
                sb.append(str.substring(str.indexOf("+")));
                str = sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("yyyy-MM-dd'T'HH:mm:ss.SSS");
            sb2.append(str2);
            str3 = sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("yyyy-MM-dd'T'HH:mm:ss");
            sb3.append(str2);
            str3 = sb3.toString();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str3);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        Date parse = simpleDateFormat.parse(str);
        Calendar instance = Calendar.getInstance();
        instance.setTime(parse);
        return instance;
    }

    public static String serialize(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'SSS'Z'", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(calendar.getTime());
    }
}
