package com.google.android.gms.internal.measurement;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.GuardedBy;
import androidx.collection.ArrayMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class zzcl implements zzcp {
    @GuardedBy("ConfigurationContentLoader.class")
    static final Map<Uri, zzcl> zzzi = new ArrayMap();
    private static final String[] zzzn = {"key", "value"};
    private final Uri uri;
    private final ContentResolver zzzj;
    private final Object zzzk = new Object();
    private volatile Map<String, String> zzzl;
    @GuardedBy("this")
    private final List<zzco> zzzm = new ArrayList();

    private zzcl(ContentResolver contentResolver, Uri uri2) {
        this.zzzj = contentResolver;
        this.uri = uri2;
        this.zzzj.registerContentObserver(uri2, false, new zzcn(this, null));
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(5:5|6|7|8|9)|12|13) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x001a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.internal.measurement.zzcl zza(android.content.ContentResolver r3, android.net.Uri r4) {
        /*
            java.lang.Class<com.google.android.gms.internal.measurement.zzcl> r0 = com.google.android.gms.internal.measurement.zzcl.class
            monitor-enter(r0)
            java.util.Map<android.net.Uri, com.google.android.gms.internal.measurement.zzcl> r1 = zzzi     // Catch:{ all -> 0x001c }
            java.lang.Object r1 = r1.get(r4)     // Catch:{ all -> 0x001c }
            com.google.android.gms.internal.measurement.zzcl r1 = (com.google.android.gms.internal.measurement.zzcl) r1     // Catch:{ all -> 0x001c }
            if (r1 != 0) goto L_0x001a
            com.google.android.gms.internal.measurement.zzcl r2 = new com.google.android.gms.internal.measurement.zzcl     // Catch:{ SecurityException -> 0x001a }
            r2.<init>(r3, r4)     // Catch:{ SecurityException -> 0x001a }
            java.util.Map<android.net.Uri, com.google.android.gms.internal.measurement.zzcl> r3 = zzzi     // Catch:{ SecurityException -> 0x0019 }
            r3.put(r4, r2)     // Catch:{ SecurityException -> 0x0019 }
            r1 = r2
            goto L_0x001a
        L_0x0019:
            r1 = r2
        L_0x001a:
            monitor-exit(r0)     // Catch:{ all -> 0x001c }
            return r1
        L_0x001c:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001c }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzcl.zza(android.content.ContentResolver, android.net.Uri):com.google.android.gms.internal.measurement.zzcl");
    }

    public final Map<String, String> zzjj() {
        Map<String, String> map = this.zzzl;
        if (map == null) {
            synchronized (this.zzzk) {
                map = this.zzzl;
                if (map == null) {
                    map = zzjl();
                    this.zzzl = map;
                }
            }
        }
        if (map != null) {
            return map;
        }
        return Collections.emptyMap();
    }

    public final void zzjk() {
        synchronized (this.zzzk) {
            this.zzzl = null;
            zzcw.zzjp();
        }
        synchronized (this) {
            for (zzco zzjo : this.zzzm) {
                zzjo.zzjo();
            }
        }
    }

    private final Map<String, String> zzjl() {
        try {
            return (Map) zzcq.zza(new zzcm(this));
        } catch (SQLiteException | IllegalStateException | SecurityException unused) {
            Log.e("ConfigurationContentLoader", "PhenotypeFlag unable to load ContentProvider, using default values");
            return null;
        }
    }

    public final /* synthetic */ Object zzca(String str) {
        return (String) zzjj().get(str);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Map zzjm() {
        Map map;
        Cursor query = this.zzzj.query(this.uri, zzzn, null, null, null);
        if (query == null) {
            return Collections.emptyMap();
        }
        try {
            int count = query.getCount();
            if (count == 0) {
                return Collections.emptyMap();
            }
            if (count <= 256) {
                map = new ArrayMap(count);
            } else {
                map = new HashMap(count, 1.0f);
            }
            while (query.moveToNext()) {
                map.put(query.getString(0), query.getString(1));
            }
            query.close();
            return map;
        } finally {
            query.close();
        }
    }
}
