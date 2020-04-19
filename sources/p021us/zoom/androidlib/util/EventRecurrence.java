package p021us.zoom.androidlib.util;

import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.HashMap;

/* renamed from: us.zoom.androidlib.util.EventRecurrence */
public class EventRecurrence {
    private static final boolean ALLOW_LOWER_CASE = false;
    public static final int DAILY = 4;

    /* renamed from: FR */
    public static final int f511FR = 2097152;
    public static final int HOURLY = 3;
    public static final int MINUTELY = 2;

    /* renamed from: MO */
    public static final int f512MO = 131072;
    public static final int MONTHLY = 6;
    private static final boolean ONLY_ONE_UNTIL_COUNT = false;
    private static final int PARSED_BYDAY = 128;
    private static final int PARSED_BYHOUR = 64;
    private static final int PARSED_BYMINUTE = 32;
    private static final int PARSED_BYMONTH = 2048;
    private static final int PARSED_BYMONTHDAY = 256;
    private static final int PARSED_BYSECOND = 16;
    private static final int PARSED_BYSETPOS = 4096;
    private static final int PARSED_BYWEEKNO = 1024;
    private static final int PARSED_BYYEARDAY = 512;
    private static final int PARSED_COUNT = 4;
    private static final int PARSED_FREQ = 1;
    private static final int PARSED_INTERVAL = 8;
    private static final int PARSED_UNTIL = 2;
    private static final int PARSED_WKST = 8192;

    /* renamed from: SA */
    public static final int f513SA = 4194304;
    public static final int SECONDLY = 1;

    /* renamed from: SU */
    public static final int f514SU = 65536;
    private static String TAG = "EventRecur";

    /* renamed from: TH */
    public static final int f515TH = 1048576;

    /* renamed from: TU */
    public static final int f516TU = 262144;
    private static final boolean VALIDATE_UNTIL = false;

    /* renamed from: WE */
    public static final int f517WE = 524288;
    public static final int WEEKLY = 5;
    public static final int YEARLY = 7;
    /* access modifiers changed from: private */
    public static final HashMap<String, Integer> sParseFreqMap = new HashMap<>();
    private static HashMap<String, PartParser> sParsePartMap = new HashMap<>();
    /* access modifiers changed from: private */
    public static final HashMap<String, Integer> sParseWeekdayMap = new HashMap<>();
    public int[] byday;
    public int bydayCount;
    public int[] bydayNum;
    public int[] byhour;
    public int byhourCount;
    public int[] byminute;
    public int byminuteCount;
    public int[] bymonth;
    public int bymonthCount;
    public int[] bymonthday;
    public int bymonthdayCount;
    public int[] bysecond;
    public int bysecondCount;
    public int[] bysetpos;
    public int bysetposCount;
    public int[] byweekno;
    public int byweeknoCount;
    public int[] byyearday;
    public int byyeardayCount;
    public int count;
    public int freq;
    public int interval;
    public Time startDate;
    public String until;
    public int wkst;

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$InvalidFormatException */
    public static class InvalidFormatException extends RuntimeException {
        InvalidFormatException(String str) {
            super(str);
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseByDay */
    private static class ParseByDay extends PartParser {
        private ParseByDay() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] iArr;
            int[] iArr2;
            int i = 1;
            if (str.indexOf(PreferencesConstants.COOKIE_DELIMITER) < 0) {
                iArr2 = new int[1];
                iArr = new int[1];
                parseWday(str, iArr2, iArr, 0);
            } else {
                String[] split = str.split(PreferencesConstants.COOKIE_DELIMITER);
                i = split.length;
                iArr2 = new int[i];
                iArr = new int[i];
                for (int i2 = 0; i2 < i; i2++) {
                    parseWday(split[i2], iArr2, iArr, i2);
                }
            }
            eventRecurrence.byday = iArr2;
            eventRecurrence.bydayNum = iArr;
            eventRecurrence.bydayCount = i;
            return 128;
        }

        private static void parseWday(String str, int[] iArr, int[] iArr2, int i) {
            String str2;
            int length = str.length() - 2;
            if (length > 0) {
                iArr2[i] = parseIntRange(str.substring(0, length), -53, 53, false);
                str2 = str.substring(length);
            } else {
                str2 = str;
            }
            Integer num = (Integer) EventRecurrence.sParseWeekdayMap.get(str2);
            if (num != null) {
                iArr[i] = num.intValue();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid BYDAY value: ");
            sb.append(str);
            throw new InvalidFormatException(sb.toString());
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseByHour */
    private static class ParseByHour extends PartParser {
        private ParseByHour() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] parseNumberList = parseNumberList(str, 0, 23, true);
            eventRecurrence.byhour = parseNumberList;
            eventRecurrence.byhourCount = parseNumberList.length;
            return 64;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseByMinute */
    private static class ParseByMinute extends PartParser {
        private ParseByMinute() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] parseNumberList = parseNumberList(str, 0, 59, true);
            eventRecurrence.byminute = parseNumberList;
            eventRecurrence.byminuteCount = parseNumberList.length;
            return 32;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseByMonth */
    private static class ParseByMonth extends PartParser {
        private ParseByMonth() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] parseNumberList = parseNumberList(str, 1, 12, false);
            eventRecurrence.bymonth = parseNumberList;
            eventRecurrence.bymonthCount = parseNumberList.length;
            return 2048;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseByMonthDay */
    private static class ParseByMonthDay extends PartParser {
        private ParseByMonthDay() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] parseNumberList = parseNumberList(str, -31, 31, false);
            eventRecurrence.bymonthday = parseNumberList;
            eventRecurrence.bymonthdayCount = parseNumberList.length;
            return 256;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseBySecond */
    private static class ParseBySecond extends PartParser {
        private ParseBySecond() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] parseNumberList = parseNumberList(str, 0, 59, true);
            eventRecurrence.bysecond = parseNumberList;
            eventRecurrence.bysecondCount = parseNumberList.length;
            return 16;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseBySetPos */
    private static class ParseBySetPos extends PartParser {
        private ParseBySetPos() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] parseNumberList = parseNumberList(str, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            eventRecurrence.bysetpos = parseNumberList;
            eventRecurrence.bysetposCount = parseNumberList.length;
            return 4096;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseByWeekNo */
    private static class ParseByWeekNo extends PartParser {
        private ParseByWeekNo() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] parseNumberList = parseNumberList(str, -53, 53, false);
            eventRecurrence.byweekno = parseNumberList;
            eventRecurrence.byweeknoCount = parseNumberList.length;
            return 1024;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseByYearDay */
    private static class ParseByYearDay extends PartParser {
        private ParseByYearDay() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            int[] parseNumberList = parseNumberList(str, -366, 366, false);
            eventRecurrence.byyearday = parseNumberList;
            eventRecurrence.byyeardayCount = parseNumberList.length;
            return 512;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseCount */
    private static class ParseCount extends PartParser {
        private ParseCount() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            eventRecurrence.count = parseIntRange(str, 0, Integer.MAX_VALUE, true);
            return 4;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseFreq */
    private static class ParseFreq extends PartParser {
        private ParseFreq() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            Integer num = (Integer) EventRecurrence.sParseFreqMap.get(str);
            if (num != null) {
                eventRecurrence.freq = num.intValue();
                return 1;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid FREQ value: ");
            sb.append(str);
            throw new InvalidFormatException(sb.toString());
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseInterval */
    private static class ParseInterval extends PartParser {
        private ParseInterval() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            eventRecurrence.interval = parseIntRange(str, 1, Integer.MAX_VALUE, false);
            return 8;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseUntil */
    private static class ParseUntil extends PartParser {
        private ParseUntil() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            eventRecurrence.until = str;
            return 2;
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$ParseWkst */
    private static class ParseWkst extends PartParser {
        private ParseWkst() {
        }

        public int parsePart(String str, EventRecurrence eventRecurrence) {
            Integer num = (Integer) EventRecurrence.sParseWeekdayMap.get(str);
            if (num != null) {
                eventRecurrence.wkst = num.intValue();
                return 8192;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid WKST value: ");
            sb.append(str);
            throw new InvalidFormatException(sb.toString());
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventRecurrence$PartParser */
    static abstract class PartParser {
        public abstract int parsePart(String str, EventRecurrence eventRecurrence);

        PartParser() {
        }

        public static int parseIntRange(String str, int i, int i2, boolean z) {
            try {
                if (str.charAt(0) == '+') {
                    str = str.substring(1);
                }
                int parseInt = Integer.parseInt(str);
                if (parseInt >= i && parseInt <= i2 && (parseInt != 0 || z)) {
                    return parseInt;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Integer value out of range: ");
                sb.append(str);
                throw new InvalidFormatException(sb.toString());
            } catch (NumberFormatException unused) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid integer value: ");
                sb2.append(str);
                throw new InvalidFormatException(sb2.toString());
            }
        }

        public static int[] parseNumberList(String str, int i, int i2, boolean z) {
            if (str.indexOf(PreferencesConstants.COOKIE_DELIMITER) < 0) {
                return new int[]{parseIntRange(str, i, i2, z)};
            }
            String[] split = str.split(PreferencesConstants.COOKIE_DELIMITER);
            int length = split.length;
            int[] iArr = new int[length];
            for (int i3 = 0; i3 < length; i3++) {
                iArr[i3] = parseIntRange(split[i3], i, i2, z);
            }
            return iArr;
        }
    }

    static {
        sParsePartMap.put("FREQ", new ParseFreq());
        sParsePartMap.put("UNTIL", new ParseUntil());
        sParsePartMap.put("COUNT", new ParseCount());
        sParsePartMap.put("INTERVAL", new ParseInterval());
        sParsePartMap.put("BYSECOND", new ParseBySecond());
        sParsePartMap.put("BYMINUTE", new ParseByMinute());
        sParsePartMap.put("BYHOUR", new ParseByHour());
        sParsePartMap.put("BYDAY", new ParseByDay());
        sParsePartMap.put("BYMONTHDAY", new ParseByMonthDay());
        sParsePartMap.put("BYYEARDAY", new ParseByYearDay());
        sParsePartMap.put("BYWEEKNO", new ParseByWeekNo());
        sParsePartMap.put("BYMONTH", new ParseByMonth());
        sParsePartMap.put("BYSETPOS", new ParseBySetPos());
        sParsePartMap.put("WKST", new ParseWkst());
        sParseFreqMap.put("SECONDLY", Integer.valueOf(1));
        sParseFreqMap.put("MINUTELY", Integer.valueOf(2));
        sParseFreqMap.put("HOURLY", Integer.valueOf(3));
        sParseFreqMap.put("DAILY", Integer.valueOf(4));
        sParseFreqMap.put("WEEKLY", Integer.valueOf(5));
        sParseFreqMap.put("MONTHLY", Integer.valueOf(6));
        sParseFreqMap.put("YEARLY", Integer.valueOf(7));
        sParseWeekdayMap.put("SU", Integer.valueOf(65536));
        sParseWeekdayMap.put("MO", Integer.valueOf(131072));
        sParseWeekdayMap.put("TU", Integer.valueOf(262144));
        sParseWeekdayMap.put("WE", Integer.valueOf(524288));
        sParseWeekdayMap.put("TH", Integer.valueOf(1048576));
        sParseWeekdayMap.put("FR", Integer.valueOf(2097152));
        sParseWeekdayMap.put("SA", Integer.valueOf(4194304));
    }

    public void setStartDate(Time time) {
        this.startDate = time;
    }

    public static int calendarDay2Day(int i) {
        switch (i) {
            case 1:
                return 65536;
            case 2:
                return 131072;
            case 3:
                return 262144;
            case 4:
                return 524288;
            case 5:
                return 1048576;
            case 6:
                return 2097152;
            case 7:
                return 4194304;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("bad day of week: ");
                sb.append(i);
                throw new RuntimeException(sb.toString());
        }
    }

    public static int timeDay2Day(int i) {
        switch (i) {
            case 0:
                return 65536;
            case 1:
                return 131072;
            case 2:
                return 262144;
            case 3:
                return 524288;
            case 4:
                return 1048576;
            case 5:
                return 2097152;
            case 6:
                return 4194304;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("bad day of week: ");
                sb.append(i);
                throw new RuntimeException(sb.toString());
        }
    }

    public static int day2TimeDay(int i) {
        if (i == 65536) {
            return 0;
        }
        if (i == 131072) {
            return 1;
        }
        if (i == 262144) {
            return 2;
        }
        if (i == 524288) {
            return 3;
        }
        if (i == 1048576) {
            return 4;
        }
        if (i == 2097152) {
            return 5;
        }
        if (i == 4194304) {
            return 6;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("bad day of week: ");
        sb.append(i);
        throw new RuntimeException(sb.toString());
    }

    public static int day2CalendarDay(int i) {
        if (i == 65536) {
            return 1;
        }
        if (i == 131072) {
            return 2;
        }
        if (i == 262144) {
            return 3;
        }
        if (i == 524288) {
            return 4;
        }
        if (i == 1048576) {
            return 5;
        }
        if (i == 2097152) {
            return 6;
        }
        if (i == 4194304) {
            return 7;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("bad day of week: ");
        sb.append(i);
        throw new RuntimeException(sb.toString());
    }

    private static String day2String(int i) {
        if (i == 65536) {
            return "SU";
        }
        if (i == 131072) {
            return "MO";
        }
        if (i == 262144) {
            return "TU";
        }
        if (i == 524288) {
            return "WE";
        }
        if (i == 1048576) {
            return "TH";
        }
        if (i == 2097152) {
            return "FR";
        }
        if (i == 4194304) {
            return "SA";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("bad day argument: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    private static void appendNumbers(StringBuilder sb, String str, int i, int[] iArr) {
        if (i > 0) {
            sb.append(str);
            int i2 = i - 1;
            for (int i3 = 0; i3 < i2; i3++) {
                sb.append(iArr[i3]);
                sb.append(PreferencesConstants.COOKIE_DELIMITER);
            }
            sb.append(iArr[i2]);
        }
    }

    private void appendByDay(StringBuilder sb, int i) {
        int i2 = this.bydayNum[i];
        if (i2 != 0) {
            sb.append(i2);
        }
        sb.append(day2String(this.byday[i]));
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FREQ=");
        switch (this.freq) {
            case 1:
                sb.append("SECONDLY");
                break;
            case 2:
                sb.append("MINUTELY");
                break;
            case 3:
                sb.append("HOURLY");
                break;
            case 4:
                sb.append("DAILY");
                break;
            case 5:
                sb.append("WEEKLY");
                break;
            case 6:
                sb.append("MONTHLY");
                break;
            case 7:
                sb.append("YEARLY");
                break;
        }
        if (!TextUtils.isEmpty(this.until)) {
            sb.append(";UNTIL=");
            sb.append(this.until);
        }
        if (this.count != 0) {
            sb.append(";COUNT=");
            sb.append(this.count);
        }
        if (this.interval != 0) {
            sb.append(";INTERVAL=");
            sb.append(this.interval);
        }
        if (this.wkst != 0) {
            sb.append(";WKST=");
            sb.append(day2String(this.wkst));
        }
        appendNumbers(sb, ";BYSECOND=", this.bysecondCount, this.bysecond);
        appendNumbers(sb, ";BYMINUTE=", this.byminuteCount, this.byminute);
        appendNumbers(sb, ";BYSECOND=", this.byhourCount, this.byhour);
        int i = this.bydayCount;
        if (i > 0) {
            sb.append(";BYDAY=");
            int i2 = i - 1;
            for (int i3 = 0; i3 < i2; i3++) {
                appendByDay(sb, i3);
                sb.append(PreferencesConstants.COOKIE_DELIMITER);
            }
            appendByDay(sb, i2);
        }
        appendNumbers(sb, ";BYMONTHDAY=", this.bymonthdayCount, this.bymonthday);
        appendNumbers(sb, ";BYYEARDAY=", this.byyeardayCount, this.byyearday);
        appendNumbers(sb, ";BYWEEKNO=", this.byweeknoCount, this.byweekno);
        appendNumbers(sb, ";BYMONTH=", this.bymonthCount, this.bymonth);
        appendNumbers(sb, ";BYSETPOS=", this.bysetposCount, this.bysetpos);
        return sb.toString();
    }

    public boolean repeatsOnEveryWeekDay() {
        if (this.freq != 5) {
            return false;
        }
        int i = this.bydayCount;
        if (i != 5) {
            return false;
        }
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = this.byday[i2];
            if (i3 == 65536 || i3 == 4194304) {
                return false;
            }
        }
        return true;
    }

    public boolean repeatsMonthlyOnDayCount() {
        if (this.freq == 6 && this.bydayCount == 1 && this.bymonthdayCount == 0 && this.bydayNum[0] > 0) {
            return true;
        }
        return false;
    }

    private static boolean arraysEqual(int[] iArr, int i, int[] iArr2, int i2) {
        if (i != i2) {
            return false;
        }
        for (int i3 = 0; i3 < i; i3++) {
            if (iArr[i3] != iArr2[i3]) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00d0, code lost:
        if (arraysEqual(r6.bysetpos, r6.bysetposCount, r7.bysetpos, r7.bysetposCount) != false) goto L_0x00d4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r6 != r7) goto L_0x0004
            return r0
        L_0x0004:
            boolean r1 = r7 instanceof p021us.zoom.androidlib.util.EventRecurrence
            r2 = 0
            if (r1 != 0) goto L_0x000a
            return r2
        L_0x000a:
            us.zoom.androidlib.util.EventRecurrence r7 = (p021us.zoom.androidlib.util.EventRecurrence) r7
            android.text.format.Time r1 = r6.startDate
            if (r1 != 0) goto L_0x0015
            android.text.format.Time r1 = r7.startDate
            if (r1 != 0) goto L_0x00d3
            goto L_0x001d
        L_0x0015:
            android.text.format.Time r3 = r7.startDate
            int r1 = android.text.format.Time.compare(r1, r3)
            if (r1 != 0) goto L_0x00d3
        L_0x001d:
            int r1 = r6.freq
            int r3 = r7.freq
            if (r1 != r3) goto L_0x00d3
            java.lang.String r1 = r6.until
            if (r1 != 0) goto L_0x002c
            java.lang.String r1 = r7.until
            if (r1 != 0) goto L_0x00d3
            goto L_0x0034
        L_0x002c:
            java.lang.String r3 = r7.until
            boolean r1 = r1.equals(r3)
            if (r1 == 0) goto L_0x00d3
        L_0x0034:
            int r1 = r6.count
            int r3 = r7.count
            if (r1 != r3) goto L_0x00d3
            int r1 = r6.interval
            int r3 = r7.interval
            if (r1 != r3) goto L_0x00d3
            int r1 = r6.wkst
            int r3 = r7.wkst
            if (r1 != r3) goto L_0x00d3
            int[] r1 = r6.bysecond
            int r3 = r6.bysecondCount
            int[] r4 = r7.bysecond
            int r5 = r7.bysecondCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.byminute
            int r3 = r6.byminuteCount
            int[] r4 = r7.byminute
            int r5 = r7.byminuteCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.byhour
            int r3 = r6.byhourCount
            int[] r4 = r7.byhour
            int r5 = r7.byhourCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.byday
            int r3 = r6.bydayCount
            int[] r4 = r7.byday
            int r5 = r7.bydayCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.bydayNum
            int r3 = r6.bydayCount
            int[] r4 = r7.bydayNum
            int r5 = r7.bydayCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.bymonthday
            int r3 = r6.bymonthdayCount
            int[] r4 = r7.bymonthday
            int r5 = r7.bymonthdayCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.byyearday
            int r3 = r6.byyeardayCount
            int[] r4 = r7.byyearday
            int r5 = r7.byyeardayCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.byweekno
            int r3 = r6.byweeknoCount
            int[] r4 = r7.byweekno
            int r5 = r7.byweeknoCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.bymonth
            int r3 = r6.bymonthCount
            int[] r4 = r7.bymonth
            int r5 = r7.bymonthCount
            boolean r1 = arraysEqual(r1, r3, r4, r5)
            if (r1 == 0) goto L_0x00d3
            int[] r1 = r6.bysetpos
            int r3 = r6.bysetposCount
            int[] r4 = r7.bysetpos
            int r7 = r7.bysetposCount
            boolean r7 = arraysEqual(r1, r3, r4, r7)
            if (r7 == 0) goto L_0x00d3
            goto L_0x00d4
        L_0x00d3:
            r0 = 0
        L_0x00d4:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.EventRecurrence.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    private void resetFields() {
        this.until = null;
        this.bysetposCount = 0;
        this.bymonthCount = 0;
        this.byweeknoCount = 0;
        this.byyeardayCount = 0;
        this.bymonthdayCount = 0;
        this.bydayCount = 0;
        this.byhourCount = 0;
        this.byminuteCount = 0;
        this.bysecondCount = 0;
        this.interval = 0;
        this.count = 0;
        this.freq = 0;
    }

    public void parse(String str) {
        resetFields();
        String[] split = str.split(";");
        int length = split.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            String str2 = split[i];
            int indexOf = str2.indexOf(61);
            if (indexOf > 0) {
                String substring = str2.substring(0, indexOf);
                String substring2 = str2.substring(indexOf + 1);
                if (substring2.length() != 0) {
                    PartParser partParser = (PartParser) sParsePartMap.get(substring);
                    if (partParser != null) {
                        int parsePart = partParser.parsePart(substring2, this);
                        if ((i2 & parsePart) == 0) {
                            i2 |= parsePart;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Part ");
                            sb.append(substring);
                            sb.append(" was specified twice");
                            throw new InvalidFormatException(sb.toString());
                        }
                    } else if (!substring.startsWith("X-")) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Couldn't find parser for ");
                        sb2.append(substring);
                        throw new InvalidFormatException(sb2.toString());
                    }
                    i++;
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Missing RHS in ");
                    sb3.append(str2);
                    throw new InvalidFormatException(sb3.toString());
                }
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Missing LHS in ");
                sb4.append(str2);
                throw new InvalidFormatException(sb4.toString());
            }
        }
        if ((i2 & 8192) == 0) {
            this.wkst = 131072;
        }
        if ((i2 & 1) == 0) {
            throw new InvalidFormatException("Must specify a FREQ value");
        } else if ((i2 & 6) == 6) {
            String str3 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Warning: rrule has both UNTIL and COUNT: ");
            sb5.append(str);
            Log.w(str3, sb5.toString());
        }
    }
}
