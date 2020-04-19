package com.box.androidsdk.content.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import p021us.zoom.androidlib.util.TimeUtil;

public final class BoxDateFormat {
    private static final ThreadLocal<DateFormat> THREAD_LOCAL_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        /* access modifiers changed from: protected */
        public DateFormat initialValue() {
            return new SimpleDateFormat(TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z);
        }
    };
    private static final ThreadLocal<DateFormat> THREAD_LOCAL_HEADER_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        /* access modifiers changed from: protected */
        public DateFormat initialValue() {
            return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        }
    };
    private static final ThreadLocal<DateFormat> THREAD_LOCAL_ROUND_TO_DAY_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        /* access modifiers changed from: protected */
        public DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private BoxDateFormat() {
    }

    public static Date parse(String str) throws ParseException {
        return ((DateFormat) THREAD_LOCAL_DATE_FORMAT.get()).parse(str);
    }

    public static String format(Date date) {
        String format2 = ((DateFormat) THREAD_LOCAL_DATE_FORMAT.get()).format(date);
        StringBuilder sb = new StringBuilder();
        sb.append(format2.substring(0, 22));
        sb.append(":");
        sb.append(format2.substring(22));
        return sb.toString();
    }

    public static Date parseRoundToDay(String str) throws ParseException {
        return ((DateFormat) THREAD_LOCAL_ROUND_TO_DAY_DATE_FORMAT.get()).parse(str);
    }

    public static String formatRoundToDay(Date date) {
        return ((DateFormat) THREAD_LOCAL_ROUND_TO_DAY_DATE_FORMAT.get()).format(date);
    }

    public static Date parseHeaderDate(String str) throws ParseException {
        return ((DateFormat) THREAD_LOCAL_HEADER_DATE_FORMAT.get()).parse(str);
    }

    public static String getTimeRangeString(Date date, Date date2) {
        if (date == null && date2 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (date != null) {
            sb.append(format(date));
        }
        sb.append(PreferencesConstants.COOKIE_DELIMITER);
        if (date2 != null) {
            sb.append(format(date2));
        }
        return sb.toString();
    }

    public static Date[] getTimeRangeDates(String str) {
        if (SdkUtils.isEmptyString(str)) {
            return null;
        }
        String[] split = str.split(PreferencesConstants.COOKIE_DELIMITER);
        Date[] dateArr = new Date[2];
        try {
            dateArr[0] = parse(split[0]);
        } catch (ArrayIndexOutOfBoundsException | ParseException unused) {
        }
        try {
            dateArr[1] = parse(split[1]);
        } catch (ArrayIndexOutOfBoundsException | ParseException unused2) {
        }
        return dateArr;
    }

    public static Date convertToDay(Date date) throws ParseException {
        Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        instance.setTime(date);
        return parseRoundToDay(formatRoundToDay(instance.getTime()));
    }
}
