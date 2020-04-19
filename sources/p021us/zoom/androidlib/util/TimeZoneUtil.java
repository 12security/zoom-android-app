package p021us.zoom.androidlib.util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.util.TimeZoneUtil */
public class TimeZoneUtil {
    private static final int HOURS_1 = 3600000;
    public static final String KEY_CITY_DISPLAYNAME = "name";
    public static final String KEY_GMT = "gmt";
    public static final String KEY_ID = "id";
    public static final String KEY_OFFSET = "offset";
    public static final String XMLTAG_TIMEZONE = "timezone";

    public static List<HashMap<String, Object>> getZones(Context context) {
        XmlResourceParser xml;
        Throwable th;
        ArrayList arrayList = new ArrayList();
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        try {
            xml = context.getResources().getXml(C4409R.C4413xml.timezones);
            while (xml.next() != 2) {
            }
            xml.next();
            while (xml.getEventType() != 3) {
                while (xml.getEventType() != 2) {
                    if (xml.getEventType() == 1) {
                        if (xml != null) {
                            xml.close();
                        }
                        return arrayList;
                    }
                    xml.next();
                }
                if (xml.getName().equals("timezone")) {
                    addItem(arrayList, xml.getAttributeValue(0), xml.nextText(), timeInMillis);
                }
                while (xml.getEventType() != 3) {
                    xml.next();
                }
                xml.next();
            }
            if (xml != null) {
                xml.close();
            }
        } catch (Exception unused) {
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        return arrayList;
        throw th;
    }

    private static void addItem(List<HashMap<String, Object>> list, String str, String str2, long j) {
        HashMap hashMap = new HashMap();
        hashMap.put("id", str);
        hashMap.put("name", str2);
        int offset = getOffset(str);
        hashMap.put(KEY_GMT, getGMT(offset));
        hashMap.put("offset", Integer.valueOf(offset));
        list.add(hashMap);
    }

    public static String getFullName(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        TimeZone timeZone = TimeZone.getTimeZone(str);
        if (timeZone == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getGMT(str));
        sb.append(", ");
        sb.append(timeZone.getDisplayName());
        return sb.toString();
    }

    public static String getCityName(List<HashMap<String, Object>> list, String str) {
        if (list == null || StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        for (HashMap hashMap : list) {
            if (str.equals(hashMap.get("id"))) {
                return (String) hashMap.get("name");
            }
        }
        return "";
    }

    public static String getGMT(String str) {
        return getGMT(getOffset(str));
    }

    public static String getGMT(int i) {
        int abs = Math.abs(i);
        StringBuilder sb = new StringBuilder();
        sb.append("GMT");
        if (i < 0) {
            sb.append('-');
        } else {
            sb.append('+');
        }
        sb.append(abs / HOURS_1);
        sb.append(':');
        int i2 = (abs / 60000) % 60;
        if (i2 < 10) {
            sb.append('0');
        }
        sb.append(i2);
        return sb.toString();
    }

    public static int getOffset(String str) {
        return TimeZone.getTimeZone(str).getOffset(Calendar.getInstance().getTimeInMillis());
    }

    public static TimeZone getTimeZoneById(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return TimeZone.getDefault();
        }
        TimeZone timeZone = TimeZone.getDefault();
        try {
            timeZone = TimeZone.getTimeZone(str);
        } catch (Exception unused) {
        }
        return timeZone;
    }

    public static boolean isProbablyInChina() {
        try {
            String gmt = getGMT(TimeZone.getDefault().getID());
            if (gmt.startsWith("GMT+8") || gmt.startsWith("GMT+9") || gmt.startsWith("GMT+7") || gmt.startsWith("GMT+6") || gmt.startsWith("GMT+5")) {
                return true;
            }
            return false;
        } catch (Exception unused) {
            return true;
        }
    }
}
