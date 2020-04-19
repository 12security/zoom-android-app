package com.google.android.gms.measurement.internal;

import android.os.Binder;
import android.text.TextUtils;
import androidx.annotation.BinderThread;
import androidx.annotation.Nullable;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.GoogleSignatureVerifier;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.UidVerifier;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public final class zzca extends zzan {
    /* access modifiers changed from: private */
    public final zzft zzkt;
    private Boolean zzoq;
    @Nullable
    private String zzor;

    public zzca(zzft zzft) {
        this(zzft, null);
    }

    private zzca(zzft zzft, @Nullable String str) {
        Preconditions.checkNotNull(zzft);
        this.zzkt = zzft;
        this.zzor = null;
    }

    @BinderThread
    public final void zzb(zzm zzm) {
        zzb(zzm, false);
        zzc((Runnable) new zzcb(this, zzm));
    }

    @BinderThread
    public final void zza(zzaj zzaj, zzm zzm) {
        Preconditions.checkNotNull(zzaj);
        zzb(zzm, false);
        zzc((Runnable) new zzcl(this, zzaj, zzm));
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public final zzaj zzb(zzaj zzaj, zzm zzm) {
        boolean z = false;
        if (!(!"_cmp".equals(zzaj.name) || zzaj.zzfd == null || zzaj.zzfd.size() == 0)) {
            String string = zzaj.zzfd.getString("_cis");
            if (!TextUtils.isEmpty(string) && (("referrer broadcast".equals(string) || "referrer API".equals(string)) && this.zzkt.zzaf().zzw(zzm.packageName))) {
                z = true;
            }
        }
        if (!z) {
            return zzaj;
        }
        this.zzkt.zzad().zzdg().zza("Event has been filtered ", zzaj.toString());
        zzaj zzaj2 = new zzaj("_cmpx", zzaj.zzfd, zzaj.origin, zzaj.zzfp);
        return zzaj2;
    }

    @BinderThread
    public final void zza(zzaj zzaj, String str, String str2) {
        Preconditions.checkNotNull(zzaj);
        Preconditions.checkNotEmpty(str);
        zza(str, true);
        zzc((Runnable) new zzcm(this, zzaj, str));
    }

    @BinderThread
    public final byte[] zza(zzaj zzaj, String str) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzaj);
        zza(str, true);
        this.zzkt.zzad().zzdh().zza("Log and bundle. event", this.zzkt.zzaa().zzal(zzaj.name));
        long nanoTime = this.zzkt.zzz().nanoTime() / 1000000;
        try {
            byte[] bArr = (byte[]) this.zzkt.zzac().zzb((Callable<V>) new zzcn<V>(this, zzaj, str)).get();
            if (bArr == null) {
                this.zzkt.zzad().zzda().zza("Log and bundle returned null. appId", zzau.zzao(str));
                bArr = new byte[0];
            }
            this.zzkt.zzad().zzdh().zza("Log and bundle processed. event, size, time_ms", this.zzkt.zzaa().zzal(zzaj.name), Integer.valueOf(bArr.length), Long.valueOf((this.zzkt.zzz().nanoTime() / 1000000) - nanoTime));
            return bArr;
        } catch (InterruptedException | ExecutionException e) {
            this.zzkt.zzad().zzda().zza("Failed to log and bundle. appId, event, error", zzau.zzao(str), this.zzkt.zzaa().zzal(zzaj.name), e);
            return null;
        }
    }

    @BinderThread
    public final void zza(zzga zzga, zzm zzm) {
        Preconditions.checkNotNull(zzga);
        zzb(zzm, false);
        if (zzga.getValue() == null) {
            zzc((Runnable) new zzco(this, zzga, zzm));
        } else {
            zzc((Runnable) new zzcp(this, zzga, zzm));
        }
    }

    @BinderThread
    public final List<zzga> zza(zzm zzm, boolean z) {
        zzb(zzm, false);
        try {
            List<zzgc> list = (List) this.zzkt.zzac().zza((Callable<V>) new zzcq<V>(this, zzm)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzgc zzgc : list) {
                if (z || !zzgd.zzbs(zzgc.name)) {
                    arrayList.add(new zzga(zzgc));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzkt.zzad().zzda().zza("Failed to get user attributes. appId", zzau.zzao(zzm.packageName), e);
            return null;
        }
    }

    @BinderThread
    public final void zza(zzm zzm) {
        zzb(zzm, false);
        zzc((Runnable) new zzcr(this, zzm));
    }

    @BinderThread
    private final void zzb(zzm zzm, boolean z) {
        Preconditions.checkNotNull(zzm);
        zza(zzm.packageName, false);
        this.zzkt.zzab().zzr(zzm.zzch, zzm.zzcv);
    }

    @BinderThread
    private final void zza(String str, boolean z) {
        boolean z2;
        if (!TextUtils.isEmpty(str)) {
            if (z) {
                try {
                    if (this.zzoq == null) {
                        if (!"com.google.android.gms".equals(this.zzor) && !UidVerifier.isGooglePlayServicesUid(this.zzkt.getContext(), Binder.getCallingUid())) {
                            if (!GoogleSignatureVerifier.getInstance(this.zzkt.getContext()).isUidGoogleSigned(Binder.getCallingUid())) {
                                z2 = false;
                                this.zzoq = Boolean.valueOf(z2);
                            }
                        }
                        z2 = true;
                        this.zzoq = Boolean.valueOf(z2);
                    }
                    if (this.zzoq.booleanValue()) {
                        return;
                    }
                } catch (SecurityException e) {
                    this.zzkt.zzad().zzda().zza("Measurement Service called with invalid calling package. appId", zzau.zzao(str));
                    throw e;
                }
            }
            if (this.zzor == null && GooglePlayServicesUtilLight.uidHasPackageName(this.zzkt.getContext(), Binder.getCallingUid(), str)) {
                this.zzor = str;
            }
            if (!str.equals(this.zzor)) {
                throw new SecurityException(String.format("Unknown calling package name '%s'.", new Object[]{str}));
            }
            return;
        }
        this.zzkt.zzad().zzda().zzaq("Measurement Service called without app package");
        throw new SecurityException("Measurement Service called without app package");
    }

    @BinderThread
    public final void zza(long j, String str, String str2, String str3) {
        zzcs zzcs = new zzcs(this, str2, str3, str, j);
        zzc((Runnable) zzcs);
    }

    @BinderThread
    public final String zzc(zzm zzm) {
        zzb(zzm, false);
        return this.zzkt.zzh(zzm);
    }

    @BinderThread
    public final void zza(zzr zzr, zzm zzm) {
        Preconditions.checkNotNull(zzr);
        Preconditions.checkNotNull(zzr.zzdv);
        zzb(zzm, false);
        zzr zzr2 = new zzr(zzr);
        zzr2.packageName = zzm.packageName;
        if (zzr.zzdv.getValue() == null) {
            zzc((Runnable) new zzcc(this, zzr2, zzm));
        } else {
            zzc((Runnable) new zzcd(this, zzr2, zzm));
        }
    }

    @BinderThread
    public final void zzb(zzr zzr) {
        Preconditions.checkNotNull(zzr);
        Preconditions.checkNotNull(zzr.zzdv);
        zza(zzr.packageName, true);
        zzr zzr2 = new zzr(zzr);
        if (zzr.zzdv.getValue() == null) {
            zzc((Runnable) new zzce(this, zzr2));
        } else {
            zzc((Runnable) new zzcf(this, zzr2));
        }
    }

    @BinderThread
    public final List<zzga> zza(String str, String str2, boolean z, zzm zzm) {
        zzb(zzm, false);
        try {
            List<zzgc> list = (List) this.zzkt.zzac().zza((Callable<V>) new zzcg<V>(this, zzm, str, str2)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzgc zzgc : list) {
                if (z || !zzgd.zzbs(zzgc.name)) {
                    arrayList.add(new zzga(zzgc));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzkt.zzad().zzda().zza("Failed to get user attributes. appId", zzau.zzao(zzm.packageName), e);
            return Collections.emptyList();
        }
    }

    @BinderThread
    public final List<zzga> zza(String str, String str2, String str3, boolean z) {
        zza(str, true);
        try {
            List<zzgc> list = (List) this.zzkt.zzac().zza((Callable<V>) new zzch<V>(this, str, str2, str3)).get();
            ArrayList arrayList = new ArrayList(list.size());
            for (zzgc zzgc : list) {
                if (z || !zzgd.zzbs(zzgc.name)) {
                    arrayList.add(new zzga(zzgc));
                }
            }
            return arrayList;
        } catch (InterruptedException | ExecutionException e) {
            this.zzkt.zzad().zzda().zza("Failed to get user attributes. appId", zzau.zzao(str), e);
            return Collections.emptyList();
        }
    }

    @BinderThread
    public final List<zzr> zza(String str, String str2, zzm zzm) {
        zzb(zzm, false);
        try {
            return (List) this.zzkt.zzac().zza((Callable<V>) new zzci<V>(this, zzm, str, str2)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zzkt.zzad().zzda().zza("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }

    @BinderThread
    public final List<zzr> zzd(String str, String str2, String str3) {
        zza(str, true);
        try {
            return (List) this.zzkt.zzac().zza((Callable<V>) new zzcj<V>(this, str, str2, str3)).get();
        } catch (InterruptedException | ExecutionException e) {
            this.zzkt.zzad().zzda().zza("Failed to get conditional user properties", e);
            return Collections.emptyList();
        }
    }

    @BinderThread
    public final void zzd(zzm zzm) {
        zza(zzm.packageName, false);
        zzc((Runnable) new zzck(this, zzm));
    }

    @VisibleForTesting
    private final void zzc(Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        if (!((Boolean) zzal.zzhw.get(null)).booleanValue() || !this.zzkt.zzac().zzef()) {
            this.zzkt.zzac().zza(runnable);
        } else {
            runnable.run();
        }
    }
}
