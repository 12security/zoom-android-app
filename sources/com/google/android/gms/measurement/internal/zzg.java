package com.google.android.gms.measurement.internal;

import android.text.TextUtils;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;

final class zzg {
    private final String zzcf;
    private String zzcg;
    private String zzch;
    private String zzci;
    private String zzcj;
    private long zzck;
    private long zzcl;
    private long zzcm;
    private String zzcn;
    private long zzco;
    private String zzcp;
    private long zzcq;
    private boolean zzcr;
    private long zzcs;
    private boolean zzct;
    private boolean zzcu;
    private String zzcv;
    private Boolean zzcw;
    private long zzcx;
    private long zzcy;
    private long zzcz;
    private long zzda;
    private long zzdb;
    private long zzdc;
    private String zzdd;
    private boolean zzde;
    private long zzdf;
    private long zzdg;
    private final zzby zzl;
    private long zzt;
    private long zzu;

    @WorkerThread
    zzg(zzby zzby, String str) {
        Preconditions.checkNotNull(zzby);
        Preconditions.checkNotEmpty(str);
        this.zzl = zzby;
        this.zzcf = str;
        this.zzl.zzac().zzq();
    }

    @WorkerThread
    public final void zzam() {
        this.zzl.zzac().zzq();
        this.zzde = false;
    }

    @WorkerThread
    public final String zzan() {
        this.zzl.zzac().zzq();
        return this.zzcf;
    }

    @WorkerThread
    public final String getAppInstanceId() {
        this.zzl.zzac().zzq();
        return this.zzcg;
    }

    @WorkerThread
    public final void zza(String str) {
        this.zzl.zzac().zzq();
        this.zzde |= !zzgd.zzs(this.zzcg, str);
        this.zzcg = str;
    }

    @WorkerThread
    public final String getGmpAppId() {
        this.zzl.zzac().zzq();
        return this.zzch;
    }

    @WorkerThread
    public final void zzb(String str) {
        this.zzl.zzac().zzq();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.zzde |= !zzgd.zzs(this.zzch, str);
        this.zzch = str;
    }

    @WorkerThread
    public final String zzao() {
        this.zzl.zzac().zzq();
        return this.zzcv;
    }

    @WorkerThread
    public final void zzc(String str) {
        this.zzl.zzac().zzq();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.zzde |= !zzgd.zzs(this.zzcv, str);
        this.zzcv = str;
    }

    @WorkerThread
    public final String zzap() {
        this.zzl.zzac().zzq();
        return this.zzci;
    }

    @WorkerThread
    public final void zzd(String str) {
        this.zzl.zzac().zzq();
        this.zzde |= !zzgd.zzs(this.zzci, str);
        this.zzci = str;
    }

    @WorkerThread
    public final String getFirebaseInstanceId() {
        this.zzl.zzac().zzq();
        return this.zzcj;
    }

    @WorkerThread
    public final void zze(String str) {
        this.zzl.zzac().zzq();
        this.zzde |= !zzgd.zzs(this.zzcj, str);
        this.zzcj = str;
    }

    @WorkerThread
    public final long zzaq() {
        this.zzl.zzac().zzq();
        return this.zzcl;
    }

    @WorkerThread
    public final void zze(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzcl != j;
        this.zzcl = j;
    }

    @WorkerThread
    public final long zzar() {
        this.zzl.zzac().zzq();
        return this.zzcm;
    }

    @WorkerThread
    public final void zzf(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzcm != j;
        this.zzcm = j;
    }

    @WorkerThread
    public final String zzas() {
        this.zzl.zzac().zzq();
        return this.zzcn;
    }

    @WorkerThread
    public final void zzf(String str) {
        this.zzl.zzac().zzq();
        this.zzde |= !zzgd.zzs(this.zzcn, str);
        this.zzcn = str;
    }

    @WorkerThread
    public final long zzat() {
        this.zzl.zzac().zzq();
        return this.zzco;
    }

    @WorkerThread
    public final void zzg(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzco != j;
        this.zzco = j;
    }

    @WorkerThread
    public final String zzau() {
        this.zzl.zzac().zzq();
        return this.zzcp;
    }

    @WorkerThread
    public final void zzg(String str) {
        this.zzl.zzac().zzq();
        this.zzde |= !zzgd.zzs(this.zzcp, str);
        this.zzcp = str;
    }

    @WorkerThread
    public final long zzav() {
        this.zzl.zzac().zzq();
        return this.zzt;
    }

    @WorkerThread
    public final void zzh(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzt != j;
        this.zzt = j;
    }

    @WorkerThread
    public final long zzaw() {
        this.zzl.zzac().zzq();
        return this.zzcq;
    }

    @WorkerThread
    public final void zzi(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzcq != j;
        this.zzcq = j;
    }

    @WorkerThread
    public final long zzax() {
        this.zzl.zzac().zzq();
        return this.zzu;
    }

    @WorkerThread
    public final void zzj(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzu != j;
        this.zzu = j;
    }

    @WorkerThread
    public final boolean isMeasurementEnabled() {
        this.zzl.zzac().zzq();
        return this.zzcr;
    }

    @WorkerThread
    public final void setMeasurementEnabled(boolean z) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzcr != z;
        this.zzcr = z;
    }

    @WorkerThread
    public final void zzk(long j) {
        boolean z = true;
        Preconditions.checkArgument(j >= 0);
        this.zzl.zzac().zzq();
        boolean z2 = this.zzde;
        if (this.zzck == j) {
            z = false;
        }
        this.zzde = z | z2;
        this.zzck = j;
    }

    @WorkerThread
    public final long zzay() {
        this.zzl.zzac().zzq();
        return this.zzck;
    }

    @WorkerThread
    public final long zzaz() {
        this.zzl.zzac().zzq();
        return this.zzdf;
    }

    @WorkerThread
    public final void zzl(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzdf != j;
        this.zzdf = j;
    }

    @WorkerThread
    public final long zzba() {
        this.zzl.zzac().zzq();
        return this.zzdg;
    }

    @WorkerThread
    public final void zzm(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzdg != j;
        this.zzdg = j;
    }

    @WorkerThread
    public final void zzbb() {
        this.zzl.zzac().zzq();
        long j = this.zzck + 1;
        if (j > 2147483647L) {
            this.zzl.zzad().zzdd().zza("Bundle index overflow. appId", zzau.zzao(this.zzcf));
            j = 0;
        }
        this.zzde = true;
        this.zzck = j;
    }

    @WorkerThread
    public final long zzbc() {
        this.zzl.zzac().zzq();
        return this.zzcx;
    }

    @WorkerThread
    public final void zzn(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzcx != j;
        this.zzcx = j;
    }

    @WorkerThread
    public final long zzbd() {
        this.zzl.zzac().zzq();
        return this.zzcy;
    }

    @WorkerThread
    public final void zzo(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzcy != j;
        this.zzcy = j;
    }

    @WorkerThread
    public final long zzbe() {
        this.zzl.zzac().zzq();
        return this.zzcz;
    }

    @WorkerThread
    public final void zzp(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzcz != j;
        this.zzcz = j;
    }

    @WorkerThread
    public final long zzbf() {
        this.zzl.zzac().zzq();
        return this.zzda;
    }

    @WorkerThread
    public final void zzq(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzda != j;
        this.zzda = j;
    }

    @WorkerThread
    public final long zzbg() {
        this.zzl.zzac().zzq();
        return this.zzdc;
    }

    @WorkerThread
    public final void zzr(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzdc != j;
        this.zzdc = j;
    }

    @WorkerThread
    public final long zzbh() {
        this.zzl.zzac().zzq();
        return this.zzdb;
    }

    @WorkerThread
    public final void zzs(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzdb != j;
        this.zzdb = j;
    }

    @WorkerThread
    public final String zzbi() {
        this.zzl.zzac().zzq();
        return this.zzdd;
    }

    @WorkerThread
    public final String zzbj() {
        this.zzl.zzac().zzq();
        String str = this.zzdd;
        zzh((String) null);
        return str;
    }

    @WorkerThread
    public final void zzh(String str) {
        this.zzl.zzac().zzq();
        this.zzde |= !zzgd.zzs(this.zzdd, str);
        this.zzdd = str;
    }

    @WorkerThread
    public final long zzbk() {
        this.zzl.zzac().zzq();
        return this.zzcs;
    }

    @WorkerThread
    public final void zzt(long j) {
        this.zzl.zzac().zzq();
        this.zzde |= this.zzcs != j;
        this.zzcs = j;
    }

    @WorkerThread
    public final boolean zzbl() {
        this.zzl.zzac().zzq();
        return this.zzct;
    }

    @WorkerThread
    public final void zzb(boolean z) {
        this.zzl.zzac().zzq();
        this.zzde = this.zzct != z;
        this.zzct = z;
    }

    @WorkerThread
    public final boolean zzbm() {
        this.zzl.zzac().zzq();
        return this.zzcu;
    }

    @WorkerThread
    public final void zzc(boolean z) {
        this.zzl.zzac().zzq();
        this.zzde = this.zzcu != z;
        this.zzcu = z;
    }

    @WorkerThread
    public final Boolean zzbn() {
        this.zzl.zzac().zzq();
        return this.zzcw;
    }

    @WorkerThread
    public final void zza(Boolean bool) {
        this.zzl.zzac().zzq();
        this.zzde = !zzgd.zza(this.zzcw, bool);
        this.zzcw = bool;
    }
}
