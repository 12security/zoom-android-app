package com.google.android.gms.measurement.internal;

final class zzdu implements Runnable {
    private final /* synthetic */ boolean zzaz;
    private final /* synthetic */ zzdd zzpm;

    zzdu(zzdd zzdd, boolean z) {
        this.zzpm = zzdd;
        this.zzaz = z;
    }

    public final void run() {
        boolean isEnabled = this.zzpm.zzl.isEnabled();
        boolean zzeq = this.zzpm.zzl.zzeq();
        this.zzpm.zzl.zza(this.zzaz);
        if (zzeq == this.zzaz) {
            this.zzpm.zzl.zzad().zzdi().zza("Default data collection state already set to", Boolean.valueOf(this.zzaz));
        }
        if (this.zzpm.zzl.isEnabled() == isEnabled || this.zzpm.zzl.isEnabled() != this.zzpm.zzl.zzeq()) {
            this.zzpm.zzl.zzad().zzdf().zza("Default data collection is different than actual status", Boolean.valueOf(this.zzaz), Boolean.valueOf(isEnabled));
        }
        this.zzpm.zzfa();
    }
}
