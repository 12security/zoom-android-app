package com.davemorrissey.labs.subscaleview.decoder;

import android.graphics.Bitmap.Config;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class SkiaImageDecoder implements ImageDecoder {
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String FILE_PREFIX = "file://";
    private static final String RESOURCE_PREFIX = "android.resource://";
    private final Config bitmapConfig;

    @Keep
    public SkiaImageDecoder() {
        this(null);
    }

    public SkiaImageDecoder(@Nullable Config config) {
        Config preferredBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
        if (config != null) {
            this.bitmapConfig = config;
        } else if (preferredBitmapConfig != null) {
            this.bitmapConfig = preferredBitmapConfig;
        } else {
            this.bitmapConfig = Config.RGB_565;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ca A[SYNTHETIC, Splitter:B:41:0x00ca] */
    @androidx.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap decode(android.content.Context r9, @androidx.annotation.NonNull android.net.Uri r10) throws java.lang.Exception {
        /*
            r8 = this;
            java.lang.String r0 = r10.toString()
            android.graphics.BitmapFactory$Options r1 = new android.graphics.BitmapFactory$Options
            r1.<init>()
            android.graphics.Bitmap$Config r2 = r8.bitmapConfig
            r1.inPreferredConfig = r2
            java.lang.String r2 = "android.resource://"
            boolean r2 = r0.startsWith(r2)
            if (r2 == 0) goto L_0x0079
            java.lang.String r0 = r10.getAuthority()
            java.lang.String r2 = r9.getPackageName()
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x0028
            android.content.res.Resources r2 = r9.getResources()
            goto L_0x0030
        L_0x0028:
            android.content.pm.PackageManager r2 = r9.getPackageManager()
            android.content.res.Resources r2 = r2.getResourcesForApplication(r0)
        L_0x0030:
            java.util.List r10 = r10.getPathSegments()
            int r3 = r10.size()
            r4 = 2
            r5 = 1
            r6 = 0
            if (r3 != r4) goto L_0x0058
            java.lang.Object r4 = r10.get(r6)
            java.lang.String r4 = (java.lang.String) r4
            java.lang.String r7 = "drawable"
            boolean r4 = r4.equals(r7)
            if (r4 == 0) goto L_0x0058
            java.lang.Object r10 = r10.get(r5)
            java.lang.String r10 = (java.lang.String) r10
            java.lang.String r3 = "drawable"
            int r6 = r2.getIdentifier(r10, r3, r0)
            goto L_0x0070
        L_0x0058:
            if (r3 != r5) goto L_0x0070
            java.lang.Object r0 = r10.get(r6)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isDigitsOnly(r0)
            if (r0 == 0) goto L_0x0070
            java.lang.Object r10 = r10.get(r6)     // Catch:{ NumberFormatException -> 0x0070 }
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ NumberFormatException -> 0x0070 }
            int r6 = java.lang.Integer.parseInt(r10)     // Catch:{ NumberFormatException -> 0x0070 }
        L_0x0070:
            android.content.res.Resources r9 = r9.getResources()
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeResource(r9, r6, r1)
            goto L_0x00b9
        L_0x0079:
            java.lang.String r2 = "file:///android_asset/"
            boolean r2 = r0.startsWith(r2)
            r3 = 0
            if (r2 == 0) goto L_0x0095
            r10 = 22
            java.lang.String r10 = r0.substring(r10)
            android.content.res.AssetManager r9 = r9.getAssets()
            java.io.InputStream r9 = r9.open(r10)
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeStream(r9, r3, r1)
            goto L_0x00b9
        L_0x0095:
            java.lang.String r2 = "file://"
            boolean r2 = r0.startsWith(r2)
            if (r2 == 0) goto L_0x00a7
            r9 = 7
            java.lang.String r9 = r0.substring(r9)
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeFile(r9, r1)
            goto L_0x00b9
        L_0x00a7:
            android.content.ContentResolver r9 = r9.getContentResolver()     // Catch:{ all -> 0x00c7 }
            java.io.InputStream r9 = r9.openInputStream(r10)     // Catch:{ all -> 0x00c7 }
            android.graphics.Bitmap r10 = android.graphics.BitmapFactory.decodeStream(r9, r3, r1)     // Catch:{ all -> 0x00c4 }
            if (r9 == 0) goto L_0x00b8
            r9.close()     // Catch:{ Exception -> 0x00b8 }
        L_0x00b8:
            r9 = r10
        L_0x00b9:
            if (r9 == 0) goto L_0x00bc
            return r9
        L_0x00bc:
            java.lang.RuntimeException r9 = new java.lang.RuntimeException
            java.lang.String r10 = "Skia image region decoder returned null bitmap - image format may not be supported"
            r9.<init>(r10)
            throw r9
        L_0x00c4:
            r10 = move-exception
            r3 = r9
            goto L_0x00c8
        L_0x00c7:
            r10 = move-exception
        L_0x00c8:
            if (r3 == 0) goto L_0x00cd
            r3.close()     // Catch:{ Exception -> 0x00cd }
        L_0x00cd:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.davemorrissey.labs.subscaleview.decoder.SkiaImageDecoder.decode(android.content.Context, android.net.Uri):android.graphics.Bitmap");
    }
}
