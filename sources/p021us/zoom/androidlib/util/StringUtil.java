package p021us.zoom.androidlib.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* renamed from: us.zoom.androidlib.util.StringUtil */
public class StringUtil {
    public static final int MID_FORMAT_TYPE_34X = 1;
    public static final int MID_FORMAT_TYPE_43X = 2;
    public static final int MID_FORMAT_TYPE_DEFAULT = 0;
    private static final String TAG = "StringUtil";

    /* renamed from: us.zoom.androidlib.util.StringUtil$SpanCompare */
    static class SpanCompare<T> implements Comparator<T> {
        private Spanned mSpanned;

        public SpanCompare(Spanned spanned) {
            this.mSpanned = spanned;
        }

        public int compare(T t, T t2) {
            if (this.mSpanned == null) {
                return 0;
            }
            if (t == null && t2 == null) {
                return 0;
            }
            if (t == null) {
                return -1;
            }
            if (t2 == null) {
                return 1;
            }
            return this.mSpanned.getSpanStart(t) - this.mSpanned.getSpanStart(t2);
        }
    }

    public static String emptyIfNull(@Nullable String str) {
        return str == null ? "" : str;
    }

    public static String safeString(String str) {
        return str == null ? "" : str;
    }

    public static boolean isEmptyOrNull(@Nullable String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmptyOrSpace(@Nullable String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isSameString(String str, String str2) {
        if (str == null && str2 == null) {
            return true;
        }
        if ((str != null && str2 == null) || str == null || str == null) {
            return false;
        }
        return str.equals(str2);
    }

    public static boolean isSameStringForNotAllowNull(String str, String str2) {
        boolean isEmptyOrNull = isEmptyOrNull(str);
        boolean isEmptyOrNull2 = isEmptyOrNull(str2);
        if (isEmptyOrNull || isEmptyOrNull2) {
            return false;
        }
        return str.equals(str2);
    }

    @Nullable
    public static Map<String, String> parseNameValues(String str) {
        if (str == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (String split : str.split("\\s*;\\s*")) {
            String[] split2 = split.split("=");
            if (split2.length == 2) {
                hashMap.put(split2[0], split2[1]);
            }
        }
        return hashMap;
    }

    public static Object bytes2String(byte[] bArr) {
        return new String(bArr, CompatUtils.getStardardCharSetUTF8());
    }

    public static String formatConfNumber(String str) {
        return formatConfNumber(str, 0);
    }

    public static String formatConfNumber(String str, int i) {
        return formatConfNumber(str, '-', i);
    }

    public static String formatConfNumber(String str, char c) {
        return formatConfNumber(str, c, 0);
    }

    public static String formatConfNumber(String str, char c, int i) {
        StringBuilder sb = new StringBuilder(str);
        if (sb.length() < 9) {
            return str;
        }
        switch (i) {
            case 1:
                sb.insert(7, c);
                sb.insert(3, c);
                break;
            case 2:
                sb.insert(7, c);
                sb.insert(4, c);
                break;
            default:
                if (sb.length() > 10) {
                    sb.insert(7, c);
                    sb.insert(3, c);
                    break;
                } else {
                    sb.insert(6, c);
                    sb.insert(3, c);
                    break;
                }
        }
        return sb.toString();
    }

    public static String formatConfNumber(long j) {
        return formatConfNumber(j, 0);
    }

    public static String formatConfNumber(long j, int i) {
        return formatConfNumber(j, '-', i);
    }

    public static String formatConfNumber(long j, char c) {
        return formatConfNumber(j, c, 0);
    }

    public static String formatConfNumber(long j, char c, int i) {
        return formatConfNumber(String.valueOf(j), c, i);
    }

    public static String ellipseString(@Nullable String str, int i) {
        if (str == null || str.length() <= i) {
            return str;
        }
        String substring = str.substring(0, i - 4);
        StringBuilder sb = new StringBuilder();
        sb.append(substring);
        sb.append("...");
        return sb.toString();
    }

    public static String ellipseString(String str, int i, TextPaint textPaint) {
        while (textPaint.measureText(str) > ((float) i) && str.length() > 3) {
            String substring = str.substring(0, str.length() - 4);
            StringBuilder sb = new StringBuilder();
            sb.append(substring);
            sb.append("...");
            str = sb.toString();
        }
        return str;
    }

    public static boolean isValidEmailAddress(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    }

    public static boolean isValidDomain(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String trim = str.trim();
        if (trim.contains(".") && !trim.startsWith(".") && !trim.endsWith(".") && !trim.contains("..")) {
            z = true;
        }
        return z;
    }

    public static boolean containsAsianCharacter(String str) {
        return (str == null || str.length() == str.getBytes(CompatUtils.getStardardCharSetUTF8()).length) ? false : true;
    }

    public static boolean endsWithAsianCharacter(String str) {
        return containsAsianCharacter(str.substring(str.length() - 1));
    }

    public static boolean startsWithAsianCharacter(String str) {
        return containsAsianCharacter(str.substring(0, 1));
    }

    public static String formatPersonName(String str, String str2) {
        return formatPersonName(str, str2, null);
    }

    public static String formatPersonName(String str, String str2, String str3) {
        String str4 = str == null ? "" : str;
        String str5 = str2 == null ? "" : str2;
        String trim = str4.trim();
        String trim2 = str5.trim();
        if (trim2.length() == 0) {
            return trim;
        }
        if (trim.length() == 0) {
            return trim2;
        }
        if (!Locale.CHINA.getCountry().equalsIgnoreCase(str3)) {
            StringBuilder sb = new StringBuilder();
            sb.append(trim);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(trim2);
            return sb.toString();
        } else if (!containsAsianCharacter(trim) && !containsAsianCharacter(trim2)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(trim);
            sb2.append(OAuth.SCOPE_DELIMITER);
            sb2.append(trim2);
            return sb2.toString();
        } else if (endsWithAsianCharacter(str2) || startsWithAsianCharacter(str)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(trim2);
            sb3.append(trim);
            return sb3.toString();
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(trim2);
            sb4.append(OAuth.SCOPE_DELIMITER);
            sb4.append(trim);
            return sb4.toString();
        }
    }

    public static boolean isAllAssii(String str) {
        for (char c : str.toCharArray()) {
            if (c > 255) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    public static List<String> getUrls(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Matcher matcher = Pattern.compile("\\b(https?://|www\\.)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", 2).matcher(charSequence);
        while (matcher.find()) {
            arrayList.add(matcher.group());
        }
        return arrayList;
    }

    @Nullable
    public static <T> T[] getSortedSpans(Spanned spanned, Class<T> cls) {
        if (spanned == null || cls == null) {
            return null;
        }
        T[] spans = spanned.getSpans(0, spanned.length(), cls);
        Arrays.sort(spans, new SpanCompare(spanned));
        return spans;
    }

    @Nullable
    public static CharSequence compatNewLineForAllOS(CharSequence charSequence) {
        if (charSequence == null) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
        for (int i = 0; i < charSequence.length(); i++) {
            char charAt = charSequence.charAt(i);
            if (charAt == 8232) {
                spannableStringBuilder.replace(i, i + 1, FontStyleHelper.SPLITOR);
            } else if (charAt == 13 && (i == charSequence.length() - 1 || charSequence.charAt(i + 1) != 10)) {
                spannableStringBuilder.replace(i, i + 1, FontStyleHelper.SPLITOR);
            }
        }
        return spannableStringBuilder;
    }

    public static String digitJoin(String[] strArr, String str) {
        if (strArr == null) {
            return "";
        }
        int length = strArr.length - 1;
        if (length == -1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            sb.append(strArr[i]);
            if (i == length) {
                return sb.toString();
            }
            if (TextUtils.isDigitsOnly(strArr[i])) {
                sb.append(str);
            }
            i++;
        }
    }

    @Nullable
    public static String[] reverseArray(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = strArr[(strArr.length - i) - 1];
        }
        return strArr2;
    }

    public static int utf8ToUtf16Index(String str, int i) {
        int i2 = 0;
        if (isEmptyOrNull(str) || i == 0) {
            return 0;
        }
        try {
            byte[] bArr = new byte[i];
            System.arraycopy(str.getBytes(CompatUtils.getStardardCharSetUTF8()), 0, bArr, 0, i);
            i2 = new String(bArr, CompatUtils.getStardardCharSetUTF8()).length();
        } catch (Exception unused) {
        }
        return i2;
    }

    public static byte[] charArray2UTF8ByteArrayWithClearance(char[] cArr) {
        byte[] array = CompatUtils.getStardardCharSetUTF8().encode(CharBuffer.wrap(cArr)).array();
        Arrays.fill(cArr, 0);
        return array;
    }
}
