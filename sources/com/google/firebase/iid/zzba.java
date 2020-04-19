package com.google.firebase.iid;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.GuardedBy;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.io.IOException;
import java.util.Map;

final class zzba {
    @GuardedBy("itself")
    private final zzax zzar;
    @GuardedBy("this")
    private int zzdw = 0;
    @GuardedBy("this")
    private final Map<Integer, TaskCompletionSource<Void>> zzdx = new ArrayMap();

    zzba(zzax zzax) {
        this.zzar = zzax;
    }

    /* access modifiers changed from: 0000 */
    public final synchronized Task<Void> zza(String str) {
        String zzak;
        TaskCompletionSource taskCompletionSource;
        int i;
        synchronized (this.zzar) {
            zzak = this.zzar.zzak();
            zzax zzax = this.zzar;
            StringBuilder sb = new StringBuilder(String.valueOf(zzak).length() + 1 + String.valueOf(str).length());
            sb.append(zzak);
            sb.append(PreferencesConstants.COOKIE_DELIMITER);
            sb.append(str);
            zzax.zzh(sb.toString());
        }
        taskCompletionSource = new TaskCompletionSource();
        Map<Integer, TaskCompletionSource<Void>> map = this.zzdx;
        if (TextUtils.isEmpty(zzak)) {
            i = 0;
        } else {
            i = zzak.split(PreferencesConstants.COOKIE_DELIMITER).length - 1;
        }
        map.put(Integer.valueOf(this.zzdw + i), taskCompletionSource);
        return taskCompletionSource.getTask();
    }

    /* access modifiers changed from: 0000 */
    public final synchronized boolean zzaq() {
        return zzar() != null;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        if (zza(r5, r0) != false) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0020, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r2 = (com.google.android.gms.tasks.TaskCompletionSource) r4.zzdx.remove(java.lang.Integer.valueOf(r4.zzdw));
        zzn(r0);
        r4.zzdw++;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        if (r2 == null) goto L_0x0000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        r2.setResult(null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0016, code lost:
        return true;
     */
    @androidx.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean zzc(com.google.firebase.iid.FirebaseInstanceId r5) {
        /*
            r4 = this;
        L_0x0000:
            monitor-enter(r4)
            java.lang.String r0 = r4.zzar()     // Catch:{ all -> 0x0042 }
            r1 = 1
            if (r0 != 0) goto L_0x0017
            boolean r5 = com.google.firebase.iid.FirebaseInstanceId.zzm()     // Catch:{ all -> 0x0042 }
            if (r5 == 0) goto L_0x0015
            java.lang.String r5 = "FirebaseInstanceId"
            java.lang.String r0 = "topic sync succeeded"
            android.util.Log.d(r5, r0)     // Catch:{ all -> 0x0042 }
        L_0x0015:
            monitor-exit(r4)     // Catch:{ all -> 0x0042 }
            return r1
        L_0x0017:
            monitor-exit(r4)     // Catch:{ all -> 0x0042 }
            boolean r2 = zza(r5, r0)
            if (r2 != 0) goto L_0x0020
            r5 = 0
            return r5
        L_0x0020:
            monitor-enter(r4)
            java.util.Map<java.lang.Integer, com.google.android.gms.tasks.TaskCompletionSource<java.lang.Void>> r2 = r4.zzdx     // Catch:{ all -> 0x003f }
            int r3 = r4.zzdw     // Catch:{ all -> 0x003f }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x003f }
            java.lang.Object r2 = r2.remove(r3)     // Catch:{ all -> 0x003f }
            com.google.android.gms.tasks.TaskCompletionSource r2 = (com.google.android.gms.tasks.TaskCompletionSource) r2     // Catch:{ all -> 0x003f }
            r4.zzn(r0)     // Catch:{ all -> 0x003f }
            int r0 = r4.zzdw     // Catch:{ all -> 0x003f }
            int r0 = r0 + r1
            r4.zzdw = r0     // Catch:{ all -> 0x003f }
            monitor-exit(r4)     // Catch:{ all -> 0x003f }
            if (r2 == 0) goto L_0x0000
            r0 = 0
            r2.setResult(r0)
            goto L_0x0000
        L_0x003f:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003f }
            throw r5
        L_0x0042:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0042 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzba.zzc(com.google.firebase.iid.FirebaseInstanceId):boolean");
    }

    @GuardedBy("this")
    @Nullable
    private final String zzar() {
        String zzak;
        synchronized (this.zzar) {
            zzak = this.zzar.zzak();
        }
        if (!TextUtils.isEmpty(zzak)) {
            String[] split = zzak.split(PreferencesConstants.COOKIE_DELIMITER);
            if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                return split[1];
            }
        }
        return null;
    }

    private final synchronized boolean zzn(String str) {
        synchronized (this.zzar) {
            String zzak = this.zzar.zzak();
            String valueOf = String.valueOf(PreferencesConstants.COOKIE_DELIMITER);
            String valueOf2 = String.valueOf(str);
            if (!zzak.startsWith(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf))) {
                return false;
            }
            String valueOf3 = String.valueOf(PreferencesConstants.COOKIE_DELIMITER);
            String valueOf4 = String.valueOf(str);
            this.zzar.zzh(zzak.substring((valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3)).length()));
            return true;
        }
    }

    @WorkerThread
    private static boolean zza(FirebaseInstanceId firebaseInstanceId, String str) {
        String[] split = str.split("!");
        if (split.length == 2) {
            String str2 = split[0];
            String str3 = split[1];
            char c = 65535;
            try {
                int hashCode = str2.hashCode();
                if (hashCode != 83) {
                    if (hashCode == 85) {
                        if (str2.equals("U")) {
                            c = 1;
                        }
                    }
                } else if (str2.equals(ExifInterface.LATITUDE_SOUTH)) {
                    c = 0;
                }
                switch (c) {
                    case 0:
                        firebaseInstanceId.zzb(str3);
                        if (FirebaseInstanceId.zzm()) {
                            Log.d("FirebaseInstanceId", "subscribe operation succeeded");
                            break;
                        }
                        break;
                    case 1:
                        firebaseInstanceId.zzc(str3);
                        if (FirebaseInstanceId.zzm()) {
                            Log.d("FirebaseInstanceId", "unsubscribe operation succeeded");
                            break;
                        }
                        break;
                }
            } catch (IOException e) {
                String str4 = "FirebaseInstanceId";
                String str5 = "Topic sync failed: ";
                String valueOf = String.valueOf(e.getMessage());
                Log.e(str4, valueOf.length() != 0 ? str5.concat(valueOf) : new String(str5));
                return false;
            }
        }
        return true;
    }
}
