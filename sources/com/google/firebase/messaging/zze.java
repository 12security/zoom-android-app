package com.google.firebase.messaging;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.firebase_messaging.zzk;
import com.google.android.gms.internal.firebase_messaging.zzn;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.io.Closeable;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

final class zze implements Closeable {
    private final URL url;
    @Nullable
    private Task<Bitmap> zzef;
    @Nullable
    private volatile InputStream zzeg;

    @Nullable
    public static zze zzo(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new zze(new URL(str));
        } catch (MalformedURLException unused) {
            String str2 = "FirebaseMessaging";
            String str3 = "Not downloading image, bad URL: ";
            String valueOf = String.valueOf(str);
            Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            return null;
        }
    }

    private zze(URL url2) {
        this.url = url2;
    }

    public final void zza(Executor executor) {
        this.zzef = Tasks.call(executor, new zzd(this));
    }

    public final Task<Bitmap> getTask() {
        return (Task) Preconditions.checkNotNull(this.zzef);
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:28:0x00ad=Splitter:B:28:0x00ad, B:12:0x006f=Splitter:B:12:0x006f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.graphics.Bitmap zzat() throws java.io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = "FirebaseMessaging"
            java.net.URL r1 = r9.url
            java.lang.String r1 = java.lang.String.valueOf(r1)
            java.lang.String r2 = java.lang.String.valueOf(r1)
            int r2 = r2.length()
            int r2 = r2 + 22
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            java.lang.String r2 = "Starting download of: "
            r3.append(r2)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            android.util.Log.i(r0, r1)
            java.net.URL r0 = r9.url     // Catch:{ IOException -> 0x00bc }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ IOException -> 0x00bc }
            java.io.InputStream r0 = r0.getInputStream()     // Catch:{ IOException -> 0x00bc }
            r1 = 1048576(0x100000, double:5.180654E-318)
            r3 = 0
            java.io.InputStream r1 = com.google.android.gms.internal.firebase_messaging.zzj.zza(r0, r1)     // Catch:{ Throwable -> 0x00b3 }
            r9.zzeg = r0     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            android.graphics.Bitmap r2 = android.graphics.BitmapFactory.decodeStream(r1)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            if (r2 == 0) goto L_0x0078
            java.lang.String r4 = "FirebaseMessaging"
            r5 = 3
            boolean r4 = android.util.Log.isLoggable(r4, r5)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            if (r4 == 0) goto L_0x006f
            java.lang.String r4 = "FirebaseMessaging"
            java.net.URL r5 = r9.url     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r6 = java.lang.String.valueOf(r5)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            int r6 = r6.length()     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            int r6 = r6 + 31
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            r7.<init>(r6)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r6 = "Successfully downloaded image: "
            r7.append(r6)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            r7.append(r5)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r5 = r7.toString()     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            android.util.Log.d(r4, r5)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
        L_0x006f:
            zza(r3, r1)     // Catch:{ Throwable -> 0x00b3 }
            if (r0 == 0) goto L_0x0077
            zza(r3, r0)     // Catch:{ IOException -> 0x00bc }
        L_0x0077:
            return r2
        L_0x0078:
            java.net.URL r2 = r9.url     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r4 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            int r4 = r4.length()     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            int r4 = r4 + 24
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            r5.<init>(r4)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r4 = "Failed to decode image: "
            r5.append(r4)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            r5.append(r2)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r2 = r5.toString()     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.lang.String r4 = "FirebaseMessaging"
            android.util.Log.w(r4, r2)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            java.io.IOException r4 = new java.io.IOException     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            r4.<init>(r2)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            throw r4     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
        L_0x00a4:
            r2 = move-exception
            r4 = r3
            goto L_0x00ad
        L_0x00a7:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x00a9 }
        L_0x00a9:
            r4 = move-exception
            r8 = r4
            r4 = r2
            r2 = r8
        L_0x00ad:
            zza(r4, r1)     // Catch:{ Throwable -> 0x00b3 }
            throw r2     // Catch:{ Throwable -> 0x00b3 }
        L_0x00b1:
            r1 = move-exception
            goto L_0x00b6
        L_0x00b3:
            r1 = move-exception
            r3 = r1
            throw r3     // Catch:{ all -> 0x00b1 }
        L_0x00b6:
            if (r0 == 0) goto L_0x00bb
            zza(r3, r0)     // Catch:{ IOException -> 0x00bc }
        L_0x00bb:
            throw r1     // Catch:{ IOException -> 0x00bc }
        L_0x00bc:
            r0 = move-exception
            java.net.URL r1 = r9.url
            java.lang.String r1 = java.lang.String.valueOf(r1)
            java.lang.String r2 = java.lang.String.valueOf(r1)
            int r2 = r2.length()
            int r2 = r2 + 26
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            java.lang.String r2 = "Failed to download image: "
            r3.append(r2)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            java.lang.String r2 = "FirebaseMessaging"
            android.util.Log.w(r2, r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.zze.zzat():android.graphics.Bitmap");
    }

    public final void close() {
        zzk.zza(this.zzeg);
    }

    private static /* synthetic */ void zza(Throwable th, InputStream inputStream) {
        if (th != null) {
            try {
                inputStream.close();
            } catch (Throwable th2) {
                zzn.zza(th, th2);
            }
        } else {
            inputStream.close();
        }
    }
}
