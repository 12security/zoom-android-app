package com.google.android.gms.measurement.internal;

final class zzav implements Runnable {
    private final /* synthetic */ int zzka;
    private final /* synthetic */ String zzkb;
    private final /* synthetic */ Object zzkc;
    private final /* synthetic */ Object zzkd;
    private final /* synthetic */ Object zzke;
    private final /* synthetic */ zzau zzkf;

    zzav(zzau zzau, int i, String str, Object obj, Object obj2, Object obj3) {
        this.zzkf = zzau;
        this.zzka = i;
        this.zzkb = str;
        this.zzkc = obj;
        this.zzkd = obj2;
        this.zzke = obj3;
    }

    public final void run() {
        zzbf zzae = this.zzkf.zzl.zzae();
        if (zzae.isInitialized()) {
            if (this.zzkf.zzjp == 0) {
                if (this.zzkf.zzaf().zzbp()) {
                    zzau zzau = this.zzkf;
                    zzau.zzag();
                    zzau.zzjp = 'C';
                } else {
                    zzau zzau2 = this.zzkf;
                    zzau2.zzag();
                    zzau2.zzjp = 'c';
                }
            }
            if (this.zzkf.zzt < 0) {
                zzau zzau3 = this.zzkf;
                zzau3.zzt = zzau3.zzaf().zzav();
            }
            char charAt = "01VDIWEA?".charAt(this.zzka);
            char zza = this.zzkf.zzjp;
            long zzb = this.zzkf.zzt;
            String zza2 = zzau.zza(true, this.zzkb, this.zzkc, this.zzkd, this.zzke);
            StringBuilder sb = new StringBuilder(String.valueOf(zza2).length() + 24);
            sb.append("2");
            sb.append(charAt);
            sb.append(zza);
            sb.append(zzb);
            sb.append(":");
            sb.append(zza2);
            String sb2 = sb.toString();
            if (sb2.length() > 1024) {
                sb2 = this.zzkb.substring(0, 1024);
            }
            zzae.zzla.zzc(sb2, 1);
            return;
        }
        this.zzkf.zza(6, "Persisted config not initialized. Not logging error/warn");
    }
}
