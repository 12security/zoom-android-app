package p021us.zoom.androidlib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.text.format.Time;
import android.util.TimeFormatException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import p021us.zoom.androidlib.util.AndroidAppUtil.EventRepeatType;

@TargetApi(14)
/* renamed from: us.zoom.androidlib.util.MeetingEventLoader */
public class MeetingEventLoader {
    private static final String TAG = "MeetingEventLoader";
    private Context mContext = null;
    private EventLoader mEventLoader = null;

    /* renamed from: us.zoom.androidlib.util.MeetingEventLoader$MeetingCalendarEvent */
    public static class MeetingCalendarEvent {
        public long beginTime = 0;
        public long endTime = 0;
        public long eventId = -1;
        public String location;
        public long meetingID = 0;
        public String meetingPwd = null;
        public String meetingUri = null;
        public int repeatCount = 0;
        public EventRepeatType repeatType = EventRepeatType.NONE;
        public String rrule;
        public String topic;
        public long untilTime = 0;
    }

    /* renamed from: us.zoom.androidlib.util.MeetingEventLoader$MeetingDomainFilter */
    public interface MeetingDomainFilter {
        boolean isAccepted(String str);
    }

    public MeetingEventLoader(Context context) {
        if (context != null) {
            this.mContext = context;
            this.mEventLoader = new EventLoader(context);
            return;
        }
        throw new RuntimeException("context is null");
    }

    public void startBackgroundThread() {
        this.mEventLoader.startBackgroundThread();
    }

    public void stopBackgroundThread() {
        this.mEventLoader.stopBackgroundThread();
    }

    public void queryMeetingsByDate(int i, ArrayList<MeetingCalendarEvent> arrayList, long j, MeetingDomainFilter meetingDomainFilter, Runnable runnable, Runnable runnable2) {
        long j2 = j;
        if (this.mContext != null) {
            Time time = new Time(Time.getCurrentTimezone());
            time.set(j2);
            int julianDay = Time.getJulianDay(j2, time.gmtoff);
            ArrayList arrayList2 = new ArrayList();
            EventLoader eventLoader = this.mEventLoader;
            final ArrayList<MeetingCalendarEvent> arrayList3 = arrayList;
            final ArrayList arrayList4 = arrayList2;
            final MeetingDomainFilter meetingDomainFilter2 = meetingDomainFilter;
            final Runnable runnable3 = runnable;
            C44551 r0 = new Runnable() {
                public void run() {
                    MeetingEventLoader.this.parseMeetingEvents(arrayList3, arrayList4, meetingDomainFilter2, runnable3);
                }
            };
            eventLoader.loadEventsInBackground(i, arrayList2, julianDay, r0, runnable2);
        }
    }

    /* access modifiers changed from: private */
    public void parseMeetingEvents(ArrayList<MeetingCalendarEvent> arrayList, ArrayList<Event> arrayList2, MeetingDomainFilter meetingDomainFilter, Runnable runnable) {
        String str;
        String str2;
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            Event event = (Event) it.next();
            String str3 = null;
            String charSequence = event.location != null ? event.location.toString() : null;
            CharSequence charSequence2 = event.description != null ? event.description.toString() : null;
            if (charSequence == null || !charSequence.matches("https://.+/j/([0-9]+)([?&]pwd=(\\S[^&\\s\\n<]+))*")) {
                if (charSequence2 != null) {
                    Pattern compile = Pattern.compile("https://.+/j/([0-9]+)([?&]pwd=(\\S[^&\\s\\n<]+))*");
                    if (compile != null) {
                        Matcher matcher = compile.matcher(charSequence2);
                        if (matcher.find()) {
                            String group = matcher.group(0);
                            str = matcher.group(1);
                            String str4 = group;
                            str2 = matcher.group(3);
                            charSequence = str4;
                        }
                    }
                }
                charSequence = null;
                str2 = null;
                str = null;
            } else {
                str2 = null;
                str = null;
            }
            if (charSequence != null && meetingDomainFilter.isAccepted(Uri.parse(charSequence).getHost())) {
                MeetingCalendarEvent meetingCalendarEvent = new MeetingCalendarEvent();
                meetingCalendarEvent.eventId = event.f509id;
                meetingCalendarEvent.meetingUri = charSequence;
                meetingCalendarEvent.meetingPwd = str2;
                if (str != null) {
                    try {
                        meetingCalendarEvent.meetingID = Long.parseLong(str);
                        meetingCalendarEvent.beginTime = event.startMillis;
                        meetingCalendarEvent.endTime = event.endMillis;
                        meetingCalendarEvent.location = event.location.toString();
                        if (event.title != null) {
                            str3 = event.title.toString();
                        }
                        meetingCalendarEvent.topic = str3;
                        meetingCalendarEvent.rrule = event.rrule;
                        if (event.rrule != null) {
                            EventRecurrence eventRecurrence = new EventRecurrence();
                            eventRecurrence.parse(event.rrule);
                            switch (eventRecurrence.freq) {
                                case 4:
                                    meetingCalendarEvent.repeatType = EventRepeatType.DAILY;
                                    parseRepeatCountAndUntil(meetingCalendarEvent, eventRecurrence);
                                    break;
                                case 5:
                                    if (eventRecurrence.wkst == 0) {
                                        if (eventRecurrence.interval <= 2) {
                                            if (eventRecurrence.interval != 2) {
                                                meetingCalendarEvent.repeatType = EventRepeatType.WEEKLY;
                                                parseRepeatCountAndUntil(meetingCalendarEvent, eventRecurrence);
                                                break;
                                            } else {
                                                meetingCalendarEvent.repeatType = EventRepeatType.BIWEEKLY;
                                                parseRepeatCountAndUntil(meetingCalendarEvent, eventRecurrence);
                                                break;
                                            }
                                        } else {
                                            meetingCalendarEvent.repeatType = EventRepeatType.UNKNOWN;
                                            break;
                                        }
                                    } else {
                                        meetingCalendarEvent.repeatType = EventRepeatType.UNKNOWN;
                                        break;
                                    }
                                case 6:
                                    meetingCalendarEvent.repeatType = EventRepeatType.MONTHLY;
                                    parseRepeatCountAndUntil(meetingCalendarEvent, eventRecurrence);
                                    break;
                                case 7:
                                    meetingCalendarEvent.repeatType = EventRepeatType.YEARLY;
                                    parseRepeatCountAndUntil(meetingCalendarEvent, eventRecurrence);
                                    break;
                                default:
                                    meetingCalendarEvent.repeatType = EventRepeatType.UNKNOWN;
                                    break;
                            }
                        }
                        arrayList.add(meetingCalendarEvent);
                    } catch (Exception unused) {
                    }
                }
            }
        }
        runnable.run();
    }

    private void parseRepeatCountAndUntil(MeetingCalendarEvent meetingCalendarEvent, EventRecurrence eventRecurrence) {
        meetingCalendarEvent.repeatCount = eventRecurrence.count;
        if (eventRecurrence.until != null) {
            try {
                Time time = new Time();
                time.parse(eventRecurrence.until);
                meetingCalendarEvent.untilTime = time.toMillis(false);
            } catch (TimeFormatException unused) {
            }
        }
    }
}
