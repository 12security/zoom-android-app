package com.dropbox.core.stone;

import com.fasterxml.jackson.core.JsonFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

final class Util {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final JsonFactory JSON = new JsonFactory();
    private static final int LONG_FORMAT_LENGTH = "yyyy-MM-dd'T'HH:mm:ss'Z'".replace("'", "").length();
    private static final int SHORT_FORMAT_LENGTH = DATE_FORMAT.replace("'", "").length();
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    Util() {
    }

    public static String formatTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        simpleDateFormat.setCalendar(new GregorianCalendar(UTC));
        return simpleDateFormat.format(date);
    }

    public static Date parseTimestamp(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat;
        int length = str.length();
        if (length == LONG_FORMAT_LENGTH) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        } else if (length == SHORT_FORMAT_LENGTH) {
            simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("timestamp has unexpected format: '");
            sb.append(str);
            sb.append("'");
            throw new ParseException(sb.toString(), 0);
        }
        simpleDateFormat.setCalendar(new GregorianCalendar(UTC));
        return simpleDateFormat.parse(str);
    }
}
