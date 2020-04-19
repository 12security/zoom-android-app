package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.net.Uri;
import javax.annotation.Nullable;

public final class zzdc {
    /* access modifiers changed from: private */
    public final Uri zzaaa;
    /* access modifiers changed from: private */
    public final String zzaab;
    /* access modifiers changed from: private */
    public final String zzaac;
    private final boolean zzaad;
    private final boolean zzaae;
    private final boolean zzaaf;
    private final boolean zzaag;
    @Nullable
    private final zzdf<Context, Boolean> zzaah;
    private final String zzzz;

    public zzdc(Uri uri) {
        this(null, uri, "", "", false, false, false, false, null);
    }

    private zzdc(String str, Uri uri, String str2, String str3, boolean z, boolean z2, boolean z3, boolean z4, @Nullable zzdf<Context, Boolean> zzdf) {
        this.zzzz = null;
        this.zzaaa = uri;
        this.zzaab = str2;
        this.zzaac = str3;
        this.zzaad = false;
        this.zzaae = false;
        this.zzaaf = false;
        this.zzaag = false;
        this.zzaah = null;
    }

    public final zzcw<Long> zze(String str, long j) {
        return zzcw.zza(this, str, j);
    }

    public final zzcw<Boolean> zzb(String str, boolean z) {
        return zzcw.zza(this, str, z);
    }

    public final zzcw<Integer> zza(String str, int i) {
        return zzcw.zza(this, str, i);
    }

    public final zzcw<Double> zza(String str, double d) {
        return zzcw.zza(this, str, d);
    }

    public final zzcw<String> zzt(String str, String str2) {
        return zzcw.zza(this, str, str2);
    }
}
