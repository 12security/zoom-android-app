package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import androidx.annotation.GuardedBy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class zzdd implements zzcp {
    @GuardedBy("SharedPreferencesLoader.class")
    static final Map<String, zzdd> zzaai = new HashMap();
    private final SharedPreferences zzaaj;
    private final OnSharedPreferenceChangeListener zzaak = new zzde(this);
    private final Object zzzk = new Object();
    private volatile Map<String, ?> zzzl;
    @GuardedBy("this")
    private final List<zzco> zzzm = new ArrayList();

    static zzdd zze(Context context, String str) {
        zzdd zzdd;
        SharedPreferences sharedPreferences;
        if (!((!zzck.zzji() || str.startsWith("direct_boot:")) ? true : zzck.isUserUnlocked(context))) {
            return null;
        }
        synchronized (zzdd.class) {
            zzdd = (zzdd) zzaai.get(str);
            if (zzdd == null) {
                if (str.startsWith("direct_boot:")) {
                    if (zzck.zzji()) {
                        context = context.createDeviceProtectedStorageContext();
                    }
                    sharedPreferences = context.getSharedPreferences(str.substring(12), 0);
                } else {
                    sharedPreferences = context.getSharedPreferences(str, 0);
                }
                zzdd = new zzdd(sharedPreferences);
                zzaai.put(str, zzdd);
            }
        }
        return zzdd;
    }

    private zzdd(SharedPreferences sharedPreferences) {
        this.zzaaj = sharedPreferences;
        this.zzaaj.registerOnSharedPreferenceChangeListener(this.zzaak);
    }

    public final Object zzca(String str) {
        Map<String, ?> map = this.zzzl;
        if (map == null) {
            synchronized (this.zzzk) {
                map = this.zzzl;
                if (map == null) {
                    map = this.zzaaj.getAll();
                    this.zzzl = map;
                }
            }
        }
        if (map != null) {
            return map.get(str);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void zza(SharedPreferences sharedPreferences, String str) {
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
}
