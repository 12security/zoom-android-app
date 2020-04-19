package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.annotation.concurrent.GuardedBy;

public final class zzau {
    private static zzau zzdd;
    @GuardedBy("serviceClassNames")
    private final SimpleArrayMap<String, String> zzde = new SimpleArrayMap<>();
    private Boolean zzdf = null;
    private Boolean zzdg = null;
    final Queue<Intent> zzdh = new ArrayDeque();
    private final Queue<Intent> zzdi = new ArrayDeque();

    public static synchronized zzau zzai() {
        zzau zzau;
        synchronized (zzau.class) {
            if (zzdd == null) {
                zzdd = new zzau();
            }
            zzau = zzdd;
        }
        return zzau;
    }

    private zzau() {
    }

    public static void zzb(Context context, Intent intent) {
        context.sendBroadcast(zza(context, "com.google.firebase.INSTANCE_ID_EVENT", intent));
    }

    public static void zzc(Context context, Intent intent) {
        context.sendBroadcast(zza(context, "com.google.firebase.MESSAGING_EVENT", intent));
    }

    private static Intent zza(Context context, String str, Intent intent) {
        Intent intent2 = new Intent(context, FirebaseInstanceIdReceiver.class);
        intent2.setAction(str);
        intent2.putExtra("wrapped_intent", intent);
        return intent2;
    }

    public final Intent zzaj() {
        return (Intent) this.zzdi.poll();
    }

    public final int zzb(Context context, String str, Intent intent) {
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String str2 = "FirebaseInstanceId";
            String str3 = "Starting service: ";
            String valueOf = String.valueOf(str);
            Log.d(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != -842411455) {
            if (hashCode == 41532704 && str.equals("com.google.firebase.MESSAGING_EVENT")) {
                c = 1;
            }
        } else if (str.equals("com.google.firebase.INSTANCE_ID_EVENT")) {
            c = 0;
        }
        switch (c) {
            case 0:
                this.zzdh.offer(intent);
                break;
            case 1:
                this.zzdi.offer(intent);
                break;
            default:
                String str4 = "FirebaseInstanceId";
                String str5 = "Unknown service action: ";
                String valueOf2 = String.valueOf(str);
                Log.w(str4, valueOf2.length() != 0 ? str5.concat(valueOf2) : new String(str5));
                return 500;
        }
        Intent intent2 = new Intent(str);
        intent2.setPackage(context.getPackageName());
        return zzd(context, intent2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00de A[Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e3 A[Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00f0 A[Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00fa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int zzd(android.content.Context r6, android.content.Intent r7) {
        /*
            r5 = this;
            androidx.collection.SimpleArrayMap<java.lang.String, java.lang.String> r0 = r5.zzde
            monitor-enter(r0)
            androidx.collection.SimpleArrayMap<java.lang.String, java.lang.String> r1 = r5.zzde     // Catch:{ all -> 0x012f }
            java.lang.String r2 = r7.getAction()     // Catch:{ all -> 0x012f }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x012f }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x012f }
            monitor-exit(r0)     // Catch:{ all -> 0x012f }
            if (r1 != 0) goto L_0x00ac
            android.content.pm.PackageManager r0 = r6.getPackageManager()
            r1 = 0
            android.content.pm.ResolveInfo r0 = r0.resolveService(r7, r1)
            if (r0 == 0) goto L_0x00a4
            android.content.pm.ServiceInfo r1 = r0.serviceInfo
            if (r1 != 0) goto L_0x0023
            goto L_0x00a4
        L_0x0023:
            android.content.pm.ServiceInfo r0 = r0.serviceInfo
            java.lang.String r1 = r6.getPackageName()
            java.lang.String r2 = r0.packageName
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x006e
            java.lang.String r1 = r0.name
            if (r1 != 0) goto L_0x0036
            goto L_0x006e
        L_0x0036:
            java.lang.String r0 = r0.name
            java.lang.String r1 = "."
            boolean r1 = r0.startsWith(r1)
            if (r1 == 0) goto L_0x005c
            java.lang.String r1 = r6.getPackageName()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            java.lang.String r0 = java.lang.String.valueOf(r0)
            int r2 = r0.length()
            if (r2 == 0) goto L_0x0057
            java.lang.String r0 = r1.concat(r0)
            goto L_0x005c
        L_0x0057:
            java.lang.String r0 = new java.lang.String
            r0.<init>(r1)
        L_0x005c:
            r1 = r0
            androidx.collection.SimpleArrayMap<java.lang.String, java.lang.String> r2 = r5.zzde
            monitor-enter(r2)
            androidx.collection.SimpleArrayMap<java.lang.String, java.lang.String> r0 = r5.zzde     // Catch:{ all -> 0x006b }
            java.lang.String r3 = r7.getAction()     // Catch:{ all -> 0x006b }
            r0.put(r3, r1)     // Catch:{ all -> 0x006b }
            monitor-exit(r2)     // Catch:{ all -> 0x006b }
            goto L_0x00ac
        L_0x006b:
            r6 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x006b }
            throw r6
        L_0x006e:
            java.lang.String r1 = "FirebaseInstanceId"
            java.lang.String r2 = r0.packageName
            java.lang.String r0 = r0.name
            java.lang.String r3 = java.lang.String.valueOf(r2)
            int r3 = r3.length()
            int r3 = r3 + 94
            java.lang.String r4 = java.lang.String.valueOf(r0)
            int r4 = r4.length()
            int r3 = r3 + r4
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>(r3)
            java.lang.String r3 = "Error resolving target intent service, skipping classname enforcement. Resolved service was: "
            r4.append(r3)
            r4.append(r2)
            java.lang.String r2 = "/"
            r4.append(r2)
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            android.util.Log.e(r1, r0)
            goto L_0x00d8
        L_0x00a4:
            java.lang.String r0 = "FirebaseInstanceId"
            java.lang.String r1 = "Failed to resolve target intent service, skipping classname enforcement"
            android.util.Log.e(r0, r1)
            goto L_0x00d8
        L_0x00ac:
            java.lang.String r0 = "FirebaseInstanceId"
            r2 = 3
            boolean r0 = android.util.Log.isLoggable(r0, r2)
            if (r0 == 0) goto L_0x00d1
            java.lang.String r0 = "FirebaseInstanceId"
            java.lang.String r2 = "Restricting intent to a specific service: "
            java.lang.String r3 = java.lang.String.valueOf(r1)
            int r4 = r3.length()
            if (r4 == 0) goto L_0x00c8
            java.lang.String r2 = r2.concat(r3)
            goto L_0x00ce
        L_0x00c8:
            java.lang.String r3 = new java.lang.String
            r3.<init>(r2)
            r2 = r3
        L_0x00ce:
            android.util.Log.d(r0, r2)
        L_0x00d1:
            java.lang.String r0 = r6.getPackageName()
            r7.setClassName(r0, r1)
        L_0x00d8:
            boolean r0 = r5.zzd(r6)     // Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }
            if (r0 == 0) goto L_0x00e3
            android.content.ComponentName r6 = androidx.legacy.content.WakefulBroadcastReceiver.startWakefulService(r6, r7)     // Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }
            goto L_0x00ee
        L_0x00e3:
            android.content.ComponentName r6 = r6.startService(r7)     // Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }
            java.lang.String r7 = "FirebaseInstanceId"
            java.lang.String r0 = "Missing wake lock permission, service start may be delayed"
            android.util.Log.d(r7, r0)     // Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }
        L_0x00ee:
            if (r6 != 0) goto L_0x00fa
            java.lang.String r6 = "FirebaseInstanceId"
            java.lang.String r7 = "Error while delivering the message: ServiceIntent not found."
            android.util.Log.e(r6, r7)     // Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }
            r6 = 404(0x194, float:5.66E-43)
            return r6
        L_0x00fa:
            r6 = -1
            return r6
        L_0x00fc:
            r6 = move-exception
            java.lang.String r7 = "FirebaseInstanceId"
            java.lang.String r6 = java.lang.String.valueOf(r6)
            java.lang.String r0 = java.lang.String.valueOf(r6)
            int r0 = r0.length()
            int r0 = r0 + 45
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>(r0)
            java.lang.String r0 = "Failed to start service while in background: "
            r1.append(r0)
            r1.append(r6)
            java.lang.String r6 = r1.toString()
            android.util.Log.e(r7, r6)
            r6 = 402(0x192, float:5.63E-43)
            return r6
        L_0x0124:
            r6 = move-exception
            java.lang.String r7 = "FirebaseInstanceId"
            java.lang.String r0 = "Error while delivering the message to the serviceIntent"
            android.util.Log.e(r7, r0, r6)
            r6 = 401(0x191, float:5.62E-43)
            return r6
        L_0x012f:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x012f }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzau.zzd(android.content.Context, android.content.Intent):int");
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzd(Context context) {
        if (this.zzdf == null) {
            this.zzdf = Boolean.valueOf(context.checkCallingOrSelfPermission("android.permission.WAKE_LOCK") == 0);
        }
        if (!this.zzdf.booleanValue() && Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Missing Permission: android.permission.WAKE_LOCK this should normally be included by the manifest merger, but may needed to be manually added to your manifest");
        }
        return this.zzdf.booleanValue();
    }

    /* access modifiers changed from: 0000 */
    public final boolean zze(Context context) {
        if (this.zzdg == null) {
            this.zzdg = Boolean.valueOf(context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0);
        }
        if (!this.zzdf.booleanValue() && Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Missing Permission: android.permission.ACCESS_NETWORK_STATE this should normally be included by the manifest merger, but may needed to be manually added to your manifest");
        }
        return this.zzdg.booleanValue();
    }
}
