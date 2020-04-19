package com.google.android.gms.measurement.internal;

public final class zzaw {
    private final int priority;
    private final /* synthetic */ zzau zzkf;
    private final boolean zzkg;
    private final boolean zzkh;

    zzaw(zzau zzau, int i, boolean z, boolean z2) {
        this.zzkf = zzau;
        this.priority = i;
        this.zzkg = z;
        this.zzkh = z2;
    }

    public final void zzaq(String str) {
        this.zzkf.zza(this.priority, this.zzkg, this.zzkh, str, null, null, null);
    }

    public final void zza(String str, Object obj) {
        this.zzkf.zza(this.priority, this.zzkg, this.zzkh, str, obj, null, null);
    }

    public final void zza(String str, Object obj, Object obj2) {
        this.zzkf.zza(this.priority, this.zzkg, this.zzkh, str, obj, obj2, null);
    }

    public final void zza(String str, Object obj, Object obj2, Object obj3) {
        this.zzkf.zza(this.priority, this.zzkg, this.zzkh, str, obj, obj2, obj3);
    }
}
