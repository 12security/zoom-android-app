package com.google.api.client.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateTime implements Serializable {
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final Pattern RFC3339_PATTERN = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})([Tt](\\d{2}):(\\d{2}):(\\d{2})(\\.\\d+)?)?([Zz]|([+-])(\\d{2}):(\\d{2}))?");
    private static final long serialVersionUID = 1;
    private final boolean dateOnly;
    private final int tzShift;
    private final long value;

    public DateTime(Date date, TimeZone timeZone) {
        this(false, date.getTime(), timeZone == null ? null : Integer.valueOf(timeZone.getOffset(date.getTime()) / 60000));
    }

    public DateTime(long j) {
        this(false, j, null);
    }

    public DateTime(Date date) {
        this(date.getTime());
    }

    public DateTime(long j, int i) {
        this(false, j, Integer.valueOf(i));
    }

    public DateTime(boolean z, long j, Integer num) {
        this.dateOnly = z;
        this.value = j;
        int i = z ? 0 : num == null ? TimeZone.getDefault().getOffset(j) / 60000 : num.intValue();
        this.tzShift = i;
    }

    public DateTime(String str) {
        DateTime parseRfc3339 = parseRfc3339(str);
        this.dateOnly = parseRfc3339.dateOnly;
        this.value = parseRfc3339.value;
        this.tzShift = parseRfc3339.tzShift;
    }

    public long getValue() {
        return this.value;
    }

    public boolean isDateOnly() {
        return this.dateOnly;
    }

    public int getTimeZoneShift() {
        return this.tzShift;
    }

    public String toStringRfc3339() {
        StringBuilder sb = new StringBuilder();
        GregorianCalendar gregorianCalendar = new GregorianCalendar(GMT);
        gregorianCalendar.setTimeInMillis(this.value + (((long) this.tzShift) * 60000));
        appendInt(sb, gregorianCalendar.get(1), 4);
        sb.append('-');
        appendInt(sb, gregorianCalendar.get(2) + 1, 2);
        sb.append('-');
        appendInt(sb, gregorianCalendar.get(5), 2);
        if (!this.dateOnly) {
            sb.append('T');
            appendInt(sb, gregorianCalendar.get(11), 2);
            sb.append(':');
            appendInt(sb, gregorianCalendar.get(12), 2);
            sb.append(':');
            appendInt(sb, gregorianCalendar.get(13), 2);
            if (gregorianCalendar.isSet(14)) {
                sb.append('.');
                appendInt(sb, gregorianCalendar.get(14), 3);
            }
            int i = this.tzShift;
            if (i == 0) {
                sb.append('Z');
            } else {
                if (i > 0) {
                    sb.append('+');
                } else {
                    sb.append('-');
                    i = -i;
                }
                int i2 = i / 60;
                int i3 = i % 60;
                appendInt(sb, i2, 2);
                sb.append(':');
                appendInt(sb, i3, 2);
            }
        }
        return sb.toString();
    }

    public String toString() {
        return toStringRfc3339();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DateTime)) {
            return false;
        }
        DateTime dateTime = (DateTime) obj;
        if (!(this.dateOnly == dateTime.dateOnly && this.value == dateTime.value && this.tzShift == dateTime.tzShift)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        long[] jArr = new long[3];
        jArr[0] = this.value;
        jArr[1] = this.dateOnly ? 1 : 0;
        jArr[2] = (long) this.tzShift;
        return Arrays.hashCode(jArr);
    }

    public static DateTime parseRfc3339(String str) throws NumberFormatException {
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        String str2 = str;
        Matcher matcher = RFC3339_PATTERN.matcher(str2);
        if (matcher.matches()) {
            int parseInt = Integer.parseInt(matcher.group(1));
            int parseInt2 = Integer.parseInt(matcher.group(2)) - 1;
            int parseInt3 = Integer.parseInt(matcher.group(3));
            boolean z2 = matcher.group(4) != null;
            String group = matcher.group(9);
            boolean z3 = group != null;
            Integer num = null;
            if (!z3 || z2) {
                if (z2) {
                    int parseInt4 = Integer.parseInt(matcher.group(5));
                    int parseInt5 = Integer.parseInt(matcher.group(6));
                    int parseInt6 = Integer.parseInt(matcher.group(7));
                    if (matcher.group(8) != null) {
                        z = z2;
                        i = (int) (((double) ((float) Integer.parseInt(matcher.group(8).substring(1)))) / Math.pow(10.0d, (double) (matcher.group(8).substring(1).length() - 3)));
                        i3 = parseInt5;
                        i2 = parseInt6;
                        i4 = parseInt4;
                    } else {
                        z = z2;
                        i3 = parseInt5;
                        i2 = parseInt6;
                        i = 0;
                        i4 = parseInt4;
                    }
                } else {
                    z = z2;
                    i4 = 0;
                    i3 = 0;
                    i2 = 0;
                    i = 0;
                }
                GregorianCalendar gregorianCalendar = new GregorianCalendar(GMT);
                gregorianCalendar.set(parseInt, parseInt2, parseInt3, i4, i3, i2);
                gregorianCalendar.set(14, i);
                long timeInMillis = gregorianCalendar.getTimeInMillis();
                if (z && z3) {
                    if (Character.toUpperCase(group.charAt(0)) == 'Z') {
                        i5 = 0;
                    } else {
                        int parseInt7 = (Integer.parseInt(matcher.group(11)) * 60) + Integer.parseInt(matcher.group(12));
                        int i6 = matcher.group(10).charAt(0) == '-' ? -parseInt7 : parseInt7;
                        timeInMillis -= ((long) i6) * 60000;
                        i5 = i6;
                    }
                    num = Integer.valueOf(i5);
                }
                return new DateTime(!z, timeInMillis, num);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid date/time format, cannot specify time zone shift without specifying time: ");
            sb.append(str2);
            throw new NumberFormatException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Invalid date/time format: ");
        sb2.append(str2);
        throw new NumberFormatException(sb2.toString());
    }

    private static void appendInt(StringBuilder sb, int i, int i2) {
        if (i < 0) {
            sb.append('-');
            i = -i;
        }
        int i3 = i2;
        int i4 = i;
        while (i4 > 0) {
            i4 /= 10;
            i3--;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            sb.append('0');
        }
        if (i != 0) {
            sb.append(i);
        }
    }
}
