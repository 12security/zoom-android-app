package com.zipow.videobox.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.ImageCache;
import p021us.zoom.androidlib.util.ImageCache.Key;

public class ZMBitmapFactory {
    private static final String TAG = "ZMBitmapFactory";

    @Nullable
    public static Bitmap decodeFile(@Nullable String str) {
        return decodeFile(str, false);
    }

    @Nullable
    public static Bitmap decodeFile(@Nullable String str, Config config) {
        return decodeFile(str, -1, false, false, config);
    }

    @Nullable
    public static Bitmap decodeFile(@Nullable String str, Config config, int i) {
        return decodeFile(str, -1, false, false, config, i);
    }

    @Nullable
    public static Bitmap decodeFile(@Nullable String str, int i) {
        return decodeFile(str, i, false);
    }

    @Nullable
    public static Bitmap decodeFile(@Nullable String str, boolean z) {
        return decodeFile(str, -1, z);
    }

    @Nullable
    public static Bitmap decodeFile(@Nullable String str, int i, boolean z) {
        return decodeFile(str, i, true, z);
    }

    @Nullable
    public static Bitmap decodeFile(@Nullable String str, int i, boolean z, boolean z2) {
        return decodeFile(str, i, z, z2, Config.ARGB_8888);
    }

    @Nullable
    public static Bitmap decodeFile(@Nullable String str, int i, boolean z, boolean z2, Config config) {
        return decodeFile(str, i, z, z2, config, 1);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:23|24|25|(2:35|27)) */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r6.inSampleSize++;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x004b, code lost:
        return null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x003f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap decodeFile(@androidx.annotation.Nullable java.lang.String r5, int r6, boolean r7, boolean r8, @androidx.annotation.Nullable android.graphics.Bitmap.Config r9, int r10) {
        /*
            boolean r0 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r5)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            java.io.File r0 = new java.io.File
            r0.<init>(r5)
            boolean r2 = r0.exists()
            if (r2 != 0) goto L_0x0014
            return r1
        L_0x0014:
            long r2 = r0.lastModified()
            if (r7 == 0) goto L_0x001f
            android.graphics.Bitmap r4 = getCachedBitmap(r5, r1, r2)
            goto L_0x0020
        L_0x001f:
            r4 = r1
        L_0x0020:
            if (r4 != 0) goto L_0x0061
            if (r7 == 0) goto L_0x0026
            if (r8 != 0) goto L_0x0061
        L_0x0026:
            if (r6 > 0) goto L_0x004c
            android.graphics.BitmapFactory$Options r6 = new android.graphics.BitmapFactory$Options
            r6.<init>()
            r8 = 1
            if (r10 > 0) goto L_0x0033
            r6.inSampleSize = r8
            goto L_0x0035
        L_0x0033:
            r6.inSampleSize = r10
        L_0x0035:
            if (r9 == 0) goto L_0x0039
            r6.inPreferredConfig = r9
        L_0x0039:
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeFile(r5, r6)     // Catch:{ OutOfMemoryError -> 0x003f }
            r4 = r6
            goto L_0x005a
        L_0x003f:
            int r9 = r6.inSampleSize     // Catch:{ Exception -> 0x004b }
            int r9 = r9 + r8
            r6.inSampleSize = r9     // Catch:{ Exception -> 0x004b }
            int r9 = r6.inSampleSize     // Catch:{ Exception -> 0x004b }
            r10 = 32
            if (r9 <= r10) goto L_0x0039
            return r1
        L_0x004b:
            return r1
        L_0x004c:
            com.zipow.videobox.VideoBoxApplication r8 = com.zipow.videobox.VideoBoxApplication.getInstance()
            android.net.Uri r9 = android.net.Uri.fromFile(r0)
            r10 = 0
            android.graphics.Bitmap r6 = com.zipow.videobox.util.ImageUtil.translateImageAsSmallBitmapInArea(r8, r9, r6, r10)
            r4 = r6
        L_0x005a:
            if (r4 == 0) goto L_0x0061
            if (r7 == 0) goto L_0x0061
            cacheBitmap(r5, r1, r4, r2)
        L_0x0061:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ZMBitmapFactory.decodeFile(java.lang.String, int, boolean, boolean, android.graphics.Bitmap$Config, int):android.graphics.Bitmap");
    }

    public static void cacheBitmap(@Nullable String str, @Nullable String str2, @Nullable Bitmap bitmap, long j) {
        if ((str != null || str2 != null) && bitmap != null) {
            ImageCache.getInstance().putBitmap(new Key(str, str2, j), bitmap);
        }
    }

    @Nullable
    public static Bitmap getCachedBitmap(@Nullable String str, @Nullable String str2, long j) {
        if (str == null && str2 == null) {
            return null;
        }
        return ImageCache.getInstance().getBitmap(Key.getFlyweightInstance(str, str2, j));
    }
}
