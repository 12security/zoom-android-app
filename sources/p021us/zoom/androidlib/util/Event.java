package p021us.zoom.androidlib.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.provider.CalendarContract.Instances;
import android.text.TextUtils;
import com.box.androidsdk.content.models.BoxItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@TargetApi(14)
/* renamed from: us.zoom.androidlib.util.Event */
public class Event implements Cloneable {
    private static final String ALLDAY_WHERE = "dispAllday=1";
    private static final String DISPLAY_AS_ALLDAY = "dispAllday";
    private static final String EVENTS_WHERE = "dispAllday=0";
    @SuppressLint({"InlinedApi"})
    public static final String[] EVENT_PROJECTION = {"title", "eventLocation", "allDay", "displayColor", "eventTimezone", "event_id", "begin", "end", "_id", "startDay", "endDay", "startMinute", "endMinute", "hasAlarm", "rrule", "rdate", "selfAttendeeStatus", "organizer", "guestsCanModify", "allDay=1 OR (end-begin)>=86400000 AS dispAllday", BoxItem.FIELD_DESCRIPTION};
    private static final boolean PROFILE = false;
    private static final int PROJECTION_ALL_DAY_INDEX = 2;
    private static final int PROJECTION_BEGIN_INDEX = 6;
    private static final int PROJECTION_COLOR_INDEX = 3;
    private static final int PROJECTION_DESCRIPTION = 20;
    private static final int PROJECTION_DISPLAY_AS_ALLDAY = 19;
    private static final int PROJECTION_END_DAY_INDEX = 10;
    private static final int PROJECTION_END_INDEX = 7;
    private static final int PROJECTION_END_MINUTE_INDEX = 12;
    private static final int PROJECTION_EVENT_ID_INDEX = 5;
    private static final int PROJECTION_GUESTS_CAN_INVITE_OTHERS_INDEX = 18;
    private static final int PROJECTION_HAS_ALARM_INDEX = 13;
    private static final int PROJECTION_LOCATION_INDEX = 1;
    private static final int PROJECTION_ORGANIZER_INDEX = 17;
    private static final int PROJECTION_RDATE_INDEX = 15;
    private static final int PROJECTION_RRULE_INDEX = 14;
    private static final int PROJECTION_SELF_ATTENDEE_STATUS_INDEX = 16;
    private static final int PROJECTION_START_DAY_INDEX = 9;
    private static final int PROJECTION_START_MINUTE_INDEX = 11;
    private static final int PROJECTION_TIMEZONE_INDEX = 4;
    private static final int PROJECTION_TITLE_INDEX = 0;
    private static final String SORT_ALLDAY_BY = "startDay ASC, endDay DESC, title ASC";
    private static final String SORT_EVENTS_BY = "begin ASC, end DESC, title ASC";
    private static final String TAG = "CalEvent";
    private static int mNoColorColor;
    private static String mNoTitleString;
    public boolean allDay;
    public float bottom;
    public int color;
    public CharSequence description;
    public int endDay;
    public long endMillis;
    public int endTime;
    public boolean guestsCanModify;
    public boolean hasAlarm;

    /* renamed from: id */
    public long f509id;
    public boolean isRepeating;
    public float left;
    public CharSequence location;
    private int mColumn;
    private int mMaxColumns;
    public Event nextDown;
    public Event nextLeft;
    public Event nextRight;
    public Event nextUp;
    public String organizer;
    public float right;
    public String rrule;
    public int selfAttendeeStatus;
    public int startDay;
    public long startMillis;
    public int startTime;
    public CharSequence title;
    public float top;

    public static int findFirstZeroBit(long j) {
        for (int i = 0; i < 64; i++) {
            if (((1 << i) & j) == 0) {
                return i;
            }
        }
        return 64;
    }

    public final void dump() {
    }

    static {
        if (!isJellybeanOrLater()) {
            EVENT_PROJECTION[3] = "calendar_color";
        }
    }

    private static boolean isJellybeanOrLater() {
        return VERSION.SDK_INT >= 16;
    }

    public final Object clone() throws CloneNotSupportedException {
        super.clone();
        Event event = new Event();
        event.title = this.title;
        event.color = this.color;
        event.location = this.location;
        event.allDay = this.allDay;
        event.startDay = this.startDay;
        event.endDay = this.endDay;
        event.startTime = this.startTime;
        event.endTime = this.endTime;
        event.startMillis = this.startMillis;
        event.endMillis = this.endMillis;
        event.hasAlarm = this.hasAlarm;
        event.isRepeating = this.isRepeating;
        event.selfAttendeeStatus = this.selfAttendeeStatus;
        event.organizer = this.organizer;
        event.guestsCanModify = this.guestsCanModify;
        event.rrule = this.rrule;
        event.description = this.description;
        return event;
    }

    public final void copyTo(Event event) {
        event.f509id = this.f509id;
        event.title = this.title;
        event.color = this.color;
        event.location = this.location;
        event.allDay = this.allDay;
        event.startDay = this.startDay;
        event.endDay = this.endDay;
        event.startTime = this.startTime;
        event.endTime = this.endTime;
        event.startMillis = this.startMillis;
        event.endMillis = this.endMillis;
        event.hasAlarm = this.hasAlarm;
        event.isRepeating = this.isRepeating;
        event.selfAttendeeStatus = this.selfAttendeeStatus;
        event.organizer = this.organizer;
        event.guestsCanModify = this.guestsCanModify;
        event.rrule = this.rrule;
        event.description = this.description;
    }

    public static final Event newInstance() {
        Event event = new Event();
        event.f509id = 0;
        event.title = null;
        event.color = 0;
        event.location = null;
        event.allDay = false;
        event.startDay = 0;
        event.endDay = 0;
        event.startTime = 0;
        event.endTime = 0;
        event.startMillis = 0;
        event.endMillis = 0;
        event.hasAlarm = false;
        event.isRepeating = false;
        event.selfAttendeeStatus = 0;
        event.rrule = null;
        event.description = null;
        return event;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0057  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void loadEvents(android.content.Context r10, java.util.ArrayList<p021us.zoom.androidlib.util.Event> r11, int r12, int r13, int r14, java.util.concurrent.atomic.AtomicInteger r15) {
        /*
            r11.clear()
            int r13 = r13 + r12
            int r13 = r13 + -1
            r7 = 0
            java.lang.String r4 = "dispAllday=0"
            java.lang.String r8 = "dispAllday=1"
            android.content.ContentResolver r0 = r10.getContentResolver()     // Catch:{ all -> 0x004e }
            java.lang.String[] r1 = EVENT_PROJECTION     // Catch:{ all -> 0x004e }
            r5 = 0
            java.lang.String r6 = "begin ASC, end DESC, title ASC"
            r2 = r12
            r3 = r13
            android.database.Cursor r9 = instancesQuery(r0, r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x004e }
            android.content.ContentResolver r0 = r10.getContentResolver()     // Catch:{ all -> 0x004c }
            java.lang.String[] r1 = EVENT_PROJECTION     // Catch:{ all -> 0x004c }
            r5 = 0
            java.lang.String r6 = "startDay ASC, endDay DESC, title ASC"
            r2 = r12
            r3 = r13
            r4 = r8
            android.database.Cursor r7 = instancesQuery(r0, r1, r2, r3, r4, r5, r6)     // Catch:{ all -> 0x004c }
            int r15 = r15.get()     // Catch:{ all -> 0x004c }
            if (r14 == r15) goto L_0x003b
            if (r9 == 0) goto L_0x0035
            r9.close()
        L_0x0035:
            if (r7 == 0) goto L_0x003a
            r7.close()
        L_0x003a:
            return
        L_0x003b:
            buildEventsFromCursor(r11, r9, r10, r12, r13)     // Catch:{ all -> 0x004c }
            buildEventsFromCursor(r11, r7, r10, r12, r13)     // Catch:{ all -> 0x004c }
            if (r9 == 0) goto L_0x0046
            r9.close()
        L_0x0046:
            if (r7 == 0) goto L_0x004b
            r7.close()
        L_0x004b:
            return
        L_0x004c:
            r10 = move-exception
            goto L_0x0050
        L_0x004e:
            r10 = move-exception
            r9 = r7
        L_0x0050:
            if (r9 == 0) goto L_0x0055
            r9.close()
        L_0x0055:
            if (r7 == 0) goto L_0x005a
            r7.close()
        L_0x005a:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: p021us.zoom.androidlib.util.Event.loadEvents(android.content.Context, java.util.ArrayList, int, int, int, java.util.concurrent.atomic.AtomicInteger):void");
    }

    private static final Cursor instancesQuery(ContentResolver contentResolver, String[] strArr, int i, int i2, String str, String[] strArr2, String str2) {
        String[] strArr3;
        String str3;
        String[] strArr4 = strArr2;
        String str4 = "visible=?";
        String[] strArr5 = {"1"};
        String str5 = "begin ASC";
        Builder buildUpon = Instances.CONTENT_BY_DAY_URI.buildUpon();
        ContentUris.appendId(buildUpon, (long) i);
        ContentUris.appendId(buildUpon, (long) i2);
        if (TextUtils.isEmpty(str)) {
            str3 = str4;
            strArr3 = strArr5;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(str);
            sb.append(") AND ");
            sb.append(str4);
            String sb2 = sb.toString();
            if (strArr4 == null || strArr4.length <= 0) {
                str3 = sb2;
                strArr3 = strArr5;
            } else {
                String[] strArr6 = (String[]) Arrays.copyOf(strArr4, strArr4.length + 1);
                strArr6[strArr6.length - 1] = strArr5[0];
                strArr3 = strArr6;
                str3 = sb2;
            }
        }
        return contentResolver.query(buildUpon.build(), strArr, str3, strArr3, str2 == null ? str5 : str2);
    }

    public static void buildEventsFromCursor(ArrayList<Event> arrayList, Cursor cursor, Context context, int i, int i2) {
        if (cursor != null && arrayList != null && cursor.getCount() != 0) {
            context.getResources();
            mNoTitleString = "";
            mNoColorColor = -16777216;
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                Event generateEventFromCursor = generateEventFromCursor(cursor);
                if (generateEventFromCursor.startDay <= i2 && generateEventFromCursor.endDay >= i) {
                    arrayList.add(generateEventFromCursor);
                }
            }
        }
    }

    private static Event generateEventFromCursor(Cursor cursor) {
        Event event = new Event();
        event.f509id = cursor.getLong(5);
        event.title = cursor.getString(0);
        event.location = cursor.getString(1);
        event.allDay = cursor.getInt(2) != 0;
        event.organizer = cursor.getString(17);
        event.guestsCanModify = cursor.getInt(18) != 0;
        CharSequence charSequence = event.title;
        if (charSequence == null || charSequence.length() == 0) {
            event.title = mNoTitleString;
        }
        if (!cursor.isNull(3)) {
            event.color = -16777216;
        } else {
            event.color = mNoColorColor;
        }
        long j = cursor.getLong(6);
        long j2 = cursor.getLong(7);
        event.startMillis = j;
        event.startTime = cursor.getInt(11);
        event.startDay = cursor.getInt(9);
        event.endMillis = j2;
        event.endTime = cursor.getInt(12);
        event.endDay = cursor.getInt(10);
        event.hasAlarm = cursor.getInt(13) != 0;
        String string = cursor.getString(14);
        String string2 = cursor.getString(15);
        if (!TextUtils.isEmpty(string) || !TextUtils.isEmpty(string2)) {
            event.isRepeating = true;
        } else {
            event.isRepeating = false;
        }
        event.rrule = string;
        event.selfAttendeeStatus = cursor.getInt(16);
        event.description = cursor.getString(20);
        return event;
    }

    static void computePositions(ArrayList<Event> arrayList, long j) {
        if (arrayList != null) {
            doComputePositions(arrayList, j, false);
            doComputePositions(arrayList, j, true);
        }
    }

    private static void doComputePositions(ArrayList<Event> arrayList, long j, boolean z) {
        long j2;
        Event event;
        boolean z2 = z;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        long j3 = j < 0 ? 0 : j;
        Iterator it = arrayList.iterator();
        long j4 = 0;
        int i = 0;
        while (it.hasNext()) {
            Event event2 = (Event) it.next();
            if (event2.drawAsAllday() == z2) {
                if (!z2) {
                    event = event2;
                    j2 = removeNonAlldayActiveEvents(event2, arrayList2.iterator(), j3, j4);
                } else {
                    event = event2;
                    j2 = removeAlldayActiveEvents(event, arrayList2.iterator(), j4);
                }
                if (arrayList2.isEmpty()) {
                    Iterator it2 = arrayList3.iterator();
                    while (it2.hasNext()) {
                        ((Event) it2.next()).setMaxColumns(i);
                    }
                    arrayList3.clear();
                    j2 = 0;
                    i = 0;
                }
                int findFirstZeroBit = findFirstZeroBit(j2);
                if (findFirstZeroBit == 64) {
                    findFirstZeroBit = 63;
                }
                j4 = j2 | (1 << findFirstZeroBit);
                event.setColumn(findFirstZeroBit);
                arrayList2.add(event);
                arrayList3.add(event);
                int size = arrayList2.size();
                if (i < size) {
                    i = size;
                }
            }
        }
        Iterator it3 = arrayList3.iterator();
        while (it3.hasNext()) {
            ((Event) it3.next()).setMaxColumns(i);
        }
    }

    private static long removeAlldayActiveEvents(Event event, Iterator<Event> it, long j) {
        while (it.hasNext()) {
            Event event2 = (Event) it.next();
            if (event2.endDay < event.startDay) {
                j &= ~(1 << event2.getColumn());
                it.remove();
            }
        }
        return j;
    }

    private static long removeNonAlldayActiveEvents(Event event, Iterator<Event> it, long j, long j2) {
        long startMillis2 = event.getStartMillis();
        while (it.hasNext()) {
            Event event2 = (Event) it.next();
            if (event2.getStartMillis() + Math.max(event2.getEndMillis() - event2.getStartMillis(), j) <= startMillis2) {
                j2 &= ~(1 << event2.getColumn());
                it.remove();
            }
        }
        return j2;
    }

    public final boolean intersects(int i, int i2, int i3) {
        int i4 = this.endDay;
        if (i4 < i) {
            return false;
        }
        int i5 = this.startDay;
        if (i5 > i) {
            return false;
        }
        if (i4 == i) {
            int i6 = this.endTime;
            if (i6 < i2) {
                return false;
            }
            if (i6 == i2 && !(this.startTime == i6 && i5 == i4)) {
                return false;
            }
        }
        if (this.startDay != i || this.startTime <= i3) {
            return true;
        }
        return false;
    }

    public String getTitleAndLocation() {
        String charSequence = this.title.toString();
        CharSequence charSequence2 = this.location;
        if (charSequence2 == null) {
            return charSequence;
        }
        String charSequence3 = charSequence2.toString();
        if (charSequence.endsWith(charSequence3)) {
            return charSequence;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(charSequence);
        sb.append(", ");
        sb.append(charSequence3);
        return sb.toString();
    }

    public void setColumn(int i) {
        this.mColumn = i;
    }

    public int getColumn() {
        return this.mColumn;
    }

    public void setMaxColumns(int i) {
        this.mMaxColumns = i;
    }

    public int getMaxColumns() {
        return this.mMaxColumns;
    }

    public void setStartMillis(long j) {
        this.startMillis = j;
    }

    public long getStartMillis() {
        return this.startMillis;
    }

    public void setEndMillis(long j) {
        this.endMillis = j;
    }

    public long getEndMillis() {
        return this.endMillis;
    }

    public boolean drawAsAllday() {
        return this.allDay || this.endMillis - this.startMillis >= 86400000;
    }
}
