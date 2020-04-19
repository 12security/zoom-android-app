package org.apache.http.impl.cookie;

import java.util.BitSet;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class LaxExpiresHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
    private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("^([0-9]{1,2})([^0-9].*)?$");
    private static final BitSet DELIMS;
    private static final Map<String, Integer> MONTHS;
    private static final Pattern MONTH_PATTERN = Pattern.compile("^(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)(.*)?$", 2);
    private static final Pattern TIME_PATTERN = Pattern.compile("^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})([^0-9].*)?$");
    static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final Pattern YEAR_PATTERN = Pattern.compile("^([0-9]{2,4})([^0-9].*)?$");

    public String getAttributeName() {
        return ClientCookie.EXPIRES_ATTR;
    }

    static {
        BitSet bitSet = new BitSet();
        bitSet.set(9);
        for (int i = 32; i <= 47; i++) {
            bitSet.set(i);
        }
        for (int i2 = 59; i2 <= 64; i2++) {
            bitSet.set(i2);
        }
        for (int i3 = 91; i3 <= 96; i3++) {
            bitSet.set(i3);
        }
        for (int i4 = 123; i4 <= 126; i4++) {
            bitSet.set(i4);
        }
        DELIMS = bitSet;
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(12);
        concurrentHashMap.put("jan", Integer.valueOf(0));
        concurrentHashMap.put("feb", Integer.valueOf(1));
        concurrentHashMap.put("mar", Integer.valueOf(2));
        concurrentHashMap.put("apr", Integer.valueOf(3));
        concurrentHashMap.put("may", Integer.valueOf(4));
        concurrentHashMap.put("jun", Integer.valueOf(5));
        concurrentHashMap.put("jul", Integer.valueOf(6));
        concurrentHashMap.put("aug", Integer.valueOf(7));
        concurrentHashMap.put("sep", Integer.valueOf(8));
        concurrentHashMap.put("oct", Integer.valueOf(9));
        concurrentHashMap.put("nov", Integer.valueOf(10));
        concurrentHashMap.put("dec", Integer.valueOf(11));
        MONTHS = concurrentHashMap;
    }

    public void parse(SetCookie setCookie, String str) throws MalformedCookieException {
        int i;
        SetCookie setCookie2 = setCookie;
        String str2 = str;
        Args.notNull(setCookie2, "Cookie");
        int i2 = 0;
        ParserCursor parserCursor = new ParserCursor(0, str.length());
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        int i3 = 0;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        while (true) {
            try {
                if (parserCursor.atEnd()) {
                    break;
                }
                skipDelims(str2, parserCursor);
                sb.setLength(i2);
                copyContent(str2, parserCursor, sb);
                if (sb.length() == 0) {
                    break;
                }
                if (!z) {
                    Matcher matcher = TIME_PATTERN.matcher(sb);
                    if (matcher.matches()) {
                        i5 = Integer.parseInt(matcher.group(1));
                        i6 = Integer.parseInt(matcher.group(2));
                        i7 = Integer.parseInt(matcher.group(3));
                        i2 = 0;
                        z = true;
                    }
                }
                if (!z2) {
                    Matcher matcher2 = DAY_OF_MONTH_PATTERN.matcher(sb);
                    if (matcher2.matches()) {
                        i4 = Integer.parseInt(matcher2.group(1));
                        i2 = 0;
                        z2 = true;
                    }
                }
                if (!z3) {
                    Matcher matcher3 = MONTH_PATTERN.matcher(sb);
                    if (matcher3.matches()) {
                        i8 = ((Integer) MONTHS.get(matcher3.group(1).toLowerCase(Locale.ROOT))).intValue();
                        i2 = 0;
                        z3 = true;
                    }
                }
                if (!z4) {
                    Matcher matcher4 = YEAR_PATTERN.matcher(sb);
                    if (matcher4.matches()) {
                        i3 = Integer.parseInt(matcher4.group(1));
                        i2 = 0;
                        z4 = true;
                    }
                }
                i2 = 0;
            } catch (NumberFormatException unused) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid 'expires' attribute: ");
                sb2.append(str2);
                throw new MalformedCookieException(sb2.toString());
            }
        }
        if (!z || !z2 || !z3 || !z4) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Invalid 'expires' attribute: ");
            sb3.append(str2);
            throw new MalformedCookieException(sb3.toString());
        }
        if (i3 >= 70 && i3 <= 99) {
            i3 += 1900;
        }
        if (i3 < 0 || i3 > 69) {
            i = 1;
        } else {
            i3 += 2000;
            i = 1;
        }
        if (i4 < i || i4 > 31 || i3 < 1601 || i5 > 23 || i6 > 59 || i7 > 59) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Invalid 'expires' attribute: ");
            sb4.append(str2);
            throw new MalformedCookieException(sb4.toString());
        }
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(UTC);
        instance.setTimeInMillis(0);
        instance.set(13, i7);
        instance.set(12, i6);
        instance.set(11, i5);
        instance.set(5, i4);
        instance.set(2, i8);
        instance.set(1, i3);
        setCookie2.setExpiryDate(instance.getTime());
    }

    private void skipDelims(CharSequence charSequence, ParserCursor parserCursor) {
        int pos = parserCursor.getPos();
        int upperBound = parserCursor.getUpperBound();
        for (int pos2 = parserCursor.getPos(); pos2 < upperBound; pos2++) {
            if (!DELIMS.get(charSequence.charAt(pos2))) {
                break;
            }
            pos++;
        }
        parserCursor.updatePos(pos);
    }

    private void copyContent(CharSequence charSequence, ParserCursor parserCursor, StringBuilder sb) {
        int pos = parserCursor.getPos();
        int upperBound = parserCursor.getUpperBound();
        for (int pos2 = parserCursor.getPos(); pos2 < upperBound; pos2++) {
            char charAt = charSequence.charAt(pos2);
            if (DELIMS.get(charAt)) {
                break;
            }
            pos++;
            sb.append(charAt);
        }
        parserCursor.updatePos(pos);
    }
}
