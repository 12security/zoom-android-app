package com.zipow.videobox.view.sip;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

public class DescriptionUtil {
    public static String getNameContentDescription(@Nullable TextView textView) {
        return textView == null ? "" : getNameContentDescription(textView.getContext(), textView.getText().toString());
    }

    public static String getNameContentDescription(Context context, @NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return StringUtil.digitJoin(str.split(""), PreferencesConstants.COOKIE_DELIMITER);
    }

    @NonNull
    public static String getTimeContentDescription(@Nullable TextView textView) {
        if (textView == null) {
            return "";
        }
        return getTimeContentDescription(textView.getContext(), textView.getText().toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005d, code lost:
        r0.append(r6[2]);
        r0.append(r5.getString(p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_hour_67408));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0074, code lost:
        if ("00".equals(r6[1]) == false) goto L_0x0085;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0076, code lost:
        r0.append(r6[0]);
        r0.append(r5.getString(p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_second_67408));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0085, code lost:
        r0.append(r6[1]);
        r0.append(r5.getString(p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_minute_67408));
        r0.append(r6[0]);
        r0.append(r5.getString(p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_second_67408));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a5, code lost:
        return r0.toString();
     */
    @androidx.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getTimeContentDescription(@androidx.annotation.NonNull android.content.Context r5, @androidx.annotation.Nullable java.lang.String r6) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L_0x0009
            java.lang.String r5 = ""
            return r5
        L_0x0009:
            java.lang.String r0 = "-"
            boolean r0 = r6.contains(r0)
            if (r0 == 0) goto L_0x0019
            java.lang.String r0 = "-"
            java.lang.String r1 = ""
            java.lang.String r6 = r6.replaceAll(r0, r1)
        L_0x0019:
            java.lang.String r0 = ":"
            boolean r0 = r6.contains(r0)
            if (r0 != 0) goto L_0x0022
            return r6
        L_0x0022:
            java.lang.String r0 = ":"
            java.lang.String[] r0 = r6.split(r0)
            r1 = 0
            r2 = 0
        L_0x002a:
            int r3 = r0.length
            r4 = 1
            int r3 = r3 - r4
            if (r2 >= r3) goto L_0x003b
            r3 = r0[r2]
            boolean r3 = android.text.TextUtils.isDigitsOnly(r3)
            if (r3 != 0) goto L_0x0038
            return r6
        L_0x0038:
            int r2 = r2 + 1
            goto L_0x002a
        L_0x003b:
            java.lang.String[] r6 = p021us.zoom.androidlib.util.StringUtil.reverseArray(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            if (r6 != 0) goto L_0x0049
            java.lang.String r5 = ""
            return r5
        L_0x0049:
            int r2 = r6.length
            switch(r2) {
                case 2: goto L_0x006c;
                case 3: goto L_0x005d;
                case 4: goto L_0x004e;
                default: goto L_0x004d;
            }
        L_0x004d:
            goto L_0x00a1
        L_0x004e:
            r2 = 3
            r2 = r6[r2]
            r0.append(r2)
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_day_67408
            java.lang.String r2 = r5.getString(r2)
            r0.append(r2)
        L_0x005d:
            r2 = 2
            r2 = r6[r2]
            r0.append(r2)
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_hour_67408
            java.lang.String r2 = r5.getString(r2)
            r0.append(r2)
        L_0x006c:
            java.lang.String r2 = "00"
            r3 = r6[r4]
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0085
            r6 = r6[r1]
            r0.append(r6)
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_second_67408
            java.lang.String r5 = r5.getString(r6)
            r0.append(r5)
            goto L_0x00a1
        L_0x0085:
            r2 = r6[r4]
            r0.append(r2)
            int r2 = p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_minute_67408
            java.lang.String r2 = r5.getString(r2)
            r0.append(r2)
            r6 = r6[r1]
            r0.append(r6)
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_sip_accessbility_second_67408
            java.lang.String r5 = r5.getString(r6)
            r0.append(r5)
        L_0x00a1:
            java.lang.String r5 = r0.toString()
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.sip.DescriptionUtil.getTimeContentDescription(android.content.Context, java.lang.String):java.lang.String");
    }

    public static String getPhoneNumberContentDescription(@Nullable TextView textView) {
        return textView == null ? "" : getPhoneNumberContentDescription(textView.getText().toString());
    }

    public static String getPhoneNumberContentDescription(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return StringUtil.digitJoin(str.split(""), PreferencesConstants.COOKIE_DELIMITER);
    }
}
