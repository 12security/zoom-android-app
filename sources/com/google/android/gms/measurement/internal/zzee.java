package com.google.android.gms.measurement.internal;

import android.os.Bundle;

final class zzee implements Runnable {
    private final /* synthetic */ boolean zzqe;
    private final /* synthetic */ zzec zzqf;
    private final /* synthetic */ zzec zzqg;
    private final /* synthetic */ zzed zzqh;

    zzee(zzed zzed, boolean z, zzec zzec, zzec zzec2) {
        this.zzqh = zzed;
        this.zzqe = z;
        this.zzqf = zzec;
        this.zzqg = zzec2;
    }

    public final void run() {
        boolean z;
        boolean z2 = false;
        if (this.zzqh.zzaf().zzac(this.zzqh.zzt().zzan())) {
            z = this.zzqe && this.zzqh.zzpy != null;
            if (z) {
                zzed zzed = this.zzqh;
                zzed.zza(zzed.zzpy, true);
            }
        } else {
            if (this.zzqe && this.zzqh.zzpy != null) {
                zzed zzed2 = this.zzqh;
                zzed2.zza(zzed2.zzpy, true);
            }
            z = false;
        }
        zzec zzec = this.zzqf;
        if (zzec == null || zzec.zzpw != this.zzqg.zzpw || !zzgd.zzs(this.zzqf.zzpv, this.zzqg.zzpv) || !zzgd.zzs(this.zzqf.zzpu, this.zzqg.zzpu)) {
            z2 = true;
        }
        if (z2) {
            Bundle bundle = new Bundle();
            zzed.zza(this.zzqg, bundle, true);
            zzec zzec2 = this.zzqf;
            if (zzec2 != null) {
                if (zzec2.zzpu != null) {
                    bundle.putString("_pn", this.zzqf.zzpu);
                }
                bundle.putString("_pc", this.zzqf.zzpv);
                bundle.putLong("_pi", this.zzqf.zzpw);
            }
            if (this.zzqh.zzaf().zzac(this.zzqh.zzt().zzan()) && z) {
                long zzfq = this.zzqh.zzx().zzfq();
                if (zzfq > 0) {
                    this.zzqh.zzab().zzb(bundle, zzfq);
                }
            }
            this.zzqh.zzs().zza("auto", "_vs", bundle);
        }
        zzed zzed3 = this.zzqh;
        zzed3.zzpy = this.zzqg;
        zzed3.zzu().zza(this.zzqg);
    }
}
