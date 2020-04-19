package com.zipow.videobox.util;

import androidx.annotation.Nullable;

public class ZMFacebookUtils {
    @Nullable
    public static String getVCardFileName(int i, String str) {
        return getVCardFileName(i, str, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x003a  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x004e  */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getVCardFileName(int r5, @androidx.annotation.Nullable java.lang.String r6, boolean r7) {
        /*
            if (r6 == 0) goto L_0x0062
            int r0 = r6.length()
            if (r0 != 0) goto L_0x0009
            goto L_0x0062
        L_0x0009:
            r0 = 1
            r1 = 45
            if (r5 != 0) goto L_0x0025
            int r2 = r6.indexOf(r1)
            r3 = 64
            int r3 = r6.indexOf(r3)
            if (r2 < 0) goto L_0x0025
            if (r3 < 0) goto L_0x0025
            if (r2 < r3) goto L_0x001f
            goto L_0x0025
        L_0x001f:
            int r2 = r2 + r0
            java.lang.String r2 = r6.substring(r2, r3)
            goto L_0x0026
        L_0x0025:
            r2 = r6
        L_0x0026:
            java.lang.StringBuffer r3 = new java.lang.StringBuffer
            r3.<init>()
            java.lang.String r4 = com.zipow.cmmlib.AppUtil.getDataPath()
            r3.append(r4)
            if (r7 == 0) goto L_0x003a
            java.lang.String r7 = "/conf_avatar_"
            r3.append(r7)
            goto L_0x003f
        L_0x003a:
            java.lang.String r7 = "/avatar_"
            r3.append(r7)
        L_0x003f:
            r7 = 0
            char r6 = r6.charAt(r7)
            if (r6 != r1) goto L_0x004e
            java.lang.String r6 = r2.substring(r0)
            r3.append(r6)
            goto L_0x0051
        L_0x004e:
            r3.append(r2)
        L_0x0051:
            r6 = 95
            r3.append(r6)
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r3.append(r5)
            java.lang.String r5 = r3.toString()
            return r5
        L_0x0062:
            r5 = 0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ZMFacebookUtils.getVCardFileName(int, java.lang.String, boolean):java.lang.String");
    }
}
