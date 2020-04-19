package org.apache.http.client.utils;

import java.util.StringTokenizer;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class Rfc3492Idn implements Idn {
    private static final String ACE_PREFIX = "xn--";
    private static final int base = 36;
    private static final int damp = 700;
    private static final char delimiter = '-';
    private static final int initial_bias = 72;
    private static final int initial_n = 128;
    private static final int skew = 38;
    private static final int tmax = 26;
    private static final int tmin = 1;

    private int adapt(int i, int i2, boolean z) {
        int i3;
        if (z) {
            i3 = i / damp;
        } else {
            i3 = i / 2;
        }
        int i4 = i3 + (i3 / i2);
        int i5 = 0;
        while (i4 > 455) {
            i4 /= 35;
            i5 += 36;
        }
        return i5 + ((i4 * 36) / (i4 + 38));
    }

    private int digit(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 'A';
        }
        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }
        if (c >= '0' && c <= '9') {
            return (c - '0') + 26;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("illegal digit: ");
        sb.append(c);
        throw new IllegalArgumentException(sb.toString());
    }

    public String toUnicode(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        StringTokenizer stringTokenizer = new StringTokenizer(str, ".");
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            if (sb.length() > 0) {
                sb.append('.');
            }
            if (nextToken.startsWith(ACE_PREFIX)) {
                nextToken = decode(nextToken.substring(4));
            }
            sb.append(nextToken);
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String decode(String str) {
        int i;
        StringBuilder sb = new StringBuilder(str.length());
        int lastIndexOf = str.lastIndexOf(45);
        int i2 = 128;
        int i3 = 72;
        if (lastIndexOf != -1) {
            sb.append(str.subSequence(0, lastIndexOf));
            str = str.substring(lastIndexOf + 1);
            i = 0;
        } else {
            i = 0;
        }
        while (!str.isEmpty()) {
            int i4 = i;
            int i5 = 1;
            int i6 = 36;
            while (!str.isEmpty()) {
                char charAt = str.charAt(0);
                str = str.substring(1);
                int digit = digit(charAt);
                i4 += digit * i5;
                int i7 = i6 <= i3 + 1 ? 1 : i6 >= i3 + 26 ? 26 : i6 - i3;
                if (digit < i7) {
                    break;
                }
                i5 *= 36 - i7;
                i6 += 36;
            }
            i3 = adapt(i4 - i, sb.length() + 1, i == 0);
            i2 += i4 / (sb.length() + 1);
            int length = i4 % (sb.length() + 1);
            sb.insert(length, (char) i2);
            i = length + 1;
        }
        return sb.toString();
    }
}
