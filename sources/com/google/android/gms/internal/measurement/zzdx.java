package com.google.android.gms.internal.measurement;

final class zzdx {
    private final byte[] buffer;
    private final zzeg zzacf;

    private zzdx(int i) {
        this.buffer = new byte[i];
        this.zzacf = zzeg.zzh(this.buffer);
    }

    public final zzdp zzkh() {
        this.zzacf.zzlk();
        return new zzdz(this.buffer);
    }

    public final zzeg zzki() {
        return this.zzacf;
    }

    /* synthetic */ zzdx(int i, zzdq zzdq) {
        this(i);
    }
}
