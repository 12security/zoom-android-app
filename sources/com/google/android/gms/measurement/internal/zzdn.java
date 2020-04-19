package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.AtomicReference;

final class zzdn implements Runnable {
    private final /* synthetic */ String zzao;
    private final /* synthetic */ String zzav;
    private final /* synthetic */ String zzdk;
    private final /* synthetic */ AtomicReference zzpl;
    private final /* synthetic */ zzdd zzpm;

    zzdn(zzdd zzdd, AtomicReference atomicReference, String str, String str2, String str3) {
        this.zzpm = zzdd;
        this.zzpl = atomicReference;
        this.zzdk = str;
        this.zzao = str2;
        this.zzav = str3;
    }

    public final void run() {
        this.zzpm.zzl.zzu().zza(this.zzpl, this.zzdk, this.zzao, this.zzav);
    }
}
