package p021us.zoom.androidlib.util;

import android.content.Context;
import android.provider.Settings.System;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import androidx.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/* renamed from: us.zoom.androidlib.util.TimeUtil */
public class TimeUtil {
    public static final String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z_WITH_QUOTE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final long ONE_DAY_IN_MILLISECONDS = 86400000;
    public static final long ONE_DAY_IN_SECONDS = 86400;
    public static final long ONE_HOUR_IN_SECONDS = 3600;
    public static final long ONE_MINUTE_IN_SECONDS = 60;

    public static String formatTimeMin(long j) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(j));
    }

    public static String formatTime(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 2561);
    }

    public static String formatTimeCap(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 2817);
    }

    public static String formatDateTime(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 2577);
    }

    public static String formatYearMonthDay(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 20);
    }

    public static String formatDateWithSystem(Context context, long j) {
        String string = System.getString(context.getContentResolver(), "date_format");
        if (!StringUtil.isEmptyOrNull(string)) {
            return DateFormat.format(string, j).toString();
        }
        return DateUtils.formatDateTime(context, j, 133652);
    }

    public static String formatDateTimeCap(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 2833);
    }

    public static String formatDateTimeShort(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 17);
    }

    public static String formatDate(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 32794);
    }

    public static String formatDateWithYear(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 32790);
    }

    public static String formatFullDate(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 32791);
    }

    public static String formatTemplateDateTime(Context context, long j) {
        return DateUtils.formatDateTime(context, j, 2837);
    }

    public static boolean isSameDate(long j, long j2) {
        Date date = new Date(j);
        Date date2 = new Date(j2);
        return date.getYear() == date2.getYear() && date.getMonth() == date2.getMonth() && date.getDate() == date2.getDate();
    }

    public static boolean isToday(long j) {
        return isSameDate(j, System.currentTimeMillis());
    }

    public static boolean isTomorrow(long j) {
        return isSameDate(j, System.currentTimeMillis() + 86400000);
    }

    public static boolean isYesterday(long j) {
        return isSameDate(j, System.currentTimeMillis() - 86400000);
    }

    public static int dateDiff(long j, long j2) {
        return (int) ((getDayZeroTime(j) - getDayZeroTime(j2)) / 86400000);
    }

    public static int monthsDiff(long j, long j2) {
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.setTimeInMillis(j);
        instance2.setTimeInMillis(j2);
        return (yearsDiff(j, j2) * 12) + (instance2.get(2) - instance.get(2));
    }

    public static int yearsDiff(long j, long j2) {
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance.setTimeInMillis(j);
        instance2.setTimeInMillis(j2);
        return instance2.get(1) - instance.get(1);
    }

    public static long getDayZeroTime(long j) {
        long rawOffset = (long) TimeZone.getDefault().getRawOffset();
        return (((j + rawOffset) / 86400000) * 86400000) - rawOffset;
    }

    public static long stringToMilliseconds(String str, String... strArr) {
        if (StringUtil.isEmptyOrNull(str)) {
            return -1;
        }
        Date date = null;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strArr[i]);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                date = simpleDateFormat.parse(str);
                break;
            } catch (ParseException unused) {
                i++;
            }
        }
        if (date == null) {
            return -1;
        }
        return date.getTime();
    }

    @Nullable
    private static String format(java.text.DateFormat dateFormat, Calendar calendar) {
        if (dateFormat == null || calendar == null) {
            return null;
        }
        dateFormat.setCalendar(calendar);
        return dateFormat.format(calendar.getTime());
    }

    public static String formatDate(Context context, Calendar calendar) {
        java.text.DateFormat dateFormat;
        if (context == null) {
            dateFormat = java.text.DateFormat.getDateInstance();
        } else {
            dateFormat = DateFormat.getDateFormat(context);
        }
        return format(dateFormat, calendar);
    }

    public static String formatDateTime(Context context, Calendar calendar) {
        return format(java.text.DateFormat.getDateTimeInstance(), calendar);
    }

    public static String formatTime(Context context, Calendar calendar) {
        java.text.DateFormat dateFormat;
        if (context == null) {
            dateFormat = java.text.DateFormat.getTimeInstance();
        } else {
            dateFormat = DateFormat.getTimeFormat(context);
        }
        return format(dateFormat, calendar);
    }

    @Nullable
    private static String format(java.text.DateFormat dateFormat, Date date, TimeZone timeZone) {
        if (dateFormat == null || date == null) {
            return null;
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        if (timeZone != null) {
            instance.setTimeZone(timeZone);
        }
        return format(dateFormat, instance);
    }

    public static String formatDate(Context context, Date date, TimeZone timeZone) {
        java.text.DateFormat dateFormat;
        if (context == null) {
            dateFormat = java.text.DateFormat.getDateInstance();
        } else {
            dateFormat = DateFormat.getDateFormat(context);
        }
        return format(dateFormat, date, timeZone);
    }

    public static String formatDateTime(Context context, Date date, TimeZone timeZone) {
        return format(java.text.DateFormat.getDateTimeInstance(), date, timeZone);
    }

    public static String formatTime(Context context, Date date, TimeZone timeZone) {
        java.text.DateFormat dateFormat;
        if (context == null) {
            dateFormat = java.text.DateFormat.getTimeInstance();
        } else {
            dateFormat = DateFormat.getTimeFormat(context);
        }
        return format(dateFormat, date, timeZone);
    }

    public static boolean isInSameMonth(long j, long j2) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(j2);
        if (instance.get(1) == instance2.get(1) && instance.get(2) == instance2.get(2)) {
            return true;
        }
        return false;
    }

    public static String formateDuration(long j) {
        StringBuilder sb = new StringBuilder();
        if (j >= ONE_DAY_IN_SECONDS) {
            sb.append(String.valueOf(j / ONE_DAY_IN_SECONDS));
            sb.append(":");
            j %= ONE_DAY_IN_SECONDS;
        }
        if (j >= ONE_HOUR_IN_SECONDS) {
            sb.append(String.format("%02d", new Object[]{Long.valueOf(j / ONE_HOUR_IN_SECONDS)}));
            sb.append(":");
            j %= ONE_HOUR_IN_SECONDS;
        }
        if (j >= 60) {
            sb.append(String.format("%02d", new Object[]{Long.valueOf(j / 60)}));
            sb.append(":");
            j %= 60;
        } else {
            sb.append("00:");
        }
        sb.append(String.format("%02d", new Object[]{Integer.valueOf((int) j)}));
        return sb.toString();
    }

    public static String formateHourAmPm(long j) {
        return new SimpleDateFormat("hh:mm a").format(Long.valueOf(j));
    }
}
