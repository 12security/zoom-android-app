package com.google.android.gms.measurement.internal;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzbt.zzc;
import com.google.android.gms.internal.measurement.zzbt.zzd;
import com.google.android.gms.internal.measurement.zzbt.zze;
import com.google.android.gms.internal.measurement.zzbt.zzh;
import com.google.android.gms.internal.measurement.zzbt.zzh.zza;
import com.google.android.gms.internal.measurement.zzcf;
import com.google.android.gms.internal.measurement.zzcg;
import com.google.android.gms.internal.measurement.zzch;
import com.google.android.gms.internal.measurement.zzez;
import com.google.android.gms.internal.measurement.zzin;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

final class zzea extends zzfs {
    public zzea(zzft zzft) {
        super(zzft);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    @WorkerThread
    public final byte[] zzb(@NonNull zzaj zzaj, @Size(min = 1) String str) {
        Integer num;
        zzch zzch;
        zzg zzg;
        zzcg zzcg;
        Bundle bundle;
        zzaf zzaf;
        long j;
        zzgc zzgc;
        zzaj zzaj2 = zzaj;
        String str2 = str;
        zzq();
        this.zzl.zzn();
        Preconditions.checkNotNull(zzaj);
        Preconditions.checkNotEmpty(str);
        if (!zzaf().zze(str2, zzal.zzir)) {
            zzad().zzdh().zza("Generating ScionPayload disabled. packageName", str2);
            return new byte[0];
        } else if ("_iap".equals(zzaj2.name) || "_iapx".equals(zzaj2.name)) {
            zzcg zzcg2 = new zzcg();
            zzdo().beginTransaction();
            try {
                zzg zzae = zzdo().zzae(str2);
                if (zzae == null) {
                    zzad().zzdh().zza("Log and bundle not available. package_name", str2);
                    return new byte[0];
                } else if (!zzae.isMeasurementEnabled()) {
                    zzad().zzdh().zza("Log and bundle disabled. package_name", str2);
                    byte[] bArr = new byte[0];
                    zzdo().endTransaction();
                    return bArr;
                } else {
                    zzch zzch2 = new zzch();
                    zzcg2.zzxl = new zzch[]{zzch2};
                    zzch2.zzxn = Integer.valueOf(1);
                    zzch2.zzxv = "android";
                    zzch2.zzcf = zzae.zzan();
                    zzch2.zzcp = zzae.zzau();
                    zzch2.zzcn = zzae.zzas();
                    long zzat = zzae.zzat();
                    if (zzat == -2147483648L) {
                        num = null;
                    } else {
                        num = Integer.valueOf((int) zzat);
                    }
                    zzch2.zzyh = num;
                    zzch2.zzxz = Long.valueOf(zzae.zzav());
                    zzch2.zzys = Long.valueOf(zzae.zzax());
                    zzch2.zzch = zzae.getGmpAppId();
                    if (TextUtils.isEmpty(zzch2.zzch)) {
                        zzch2.zzxf = zzae.zzao();
                    }
                    zzch2.zzyd = Long.valueOf(zzae.zzaw());
                    if (this.zzl.isEnabled() && zzt.zzbv() && zzaf().zzk(zzch2.zzcf)) {
                        zzch2.zzyn = null;
                    }
                    Pair zzar = zzae().zzar(zzae.zzan());
                    if (zzae.zzbl() && zzar != null && !TextUtils.isEmpty((CharSequence) zzar.first)) {
                        zzch2.zzyb = zzo((String) zzar.first, Long.toString(zzaj2.zzfp));
                        zzch2.zzyc = (Boolean) zzar.second;
                    }
                    zzy().zzah();
                    zzch2.zzxx = Build.MODEL;
                    zzy().zzah();
                    zzch2.zzxw = VERSION.RELEASE;
                    zzch2.zzxy = Integer.valueOf((int) zzy().zzco());
                    zzch2.zzex = zzy().zzcp();
                    try {
                        zzch2.zzcg = zzo(zzae.getAppInstanceId(), Long.toString(zzaj2.zzfp));
                        zzch2.zzcj = zzae.getFirebaseInstanceId();
                        String zzan = zzae.zzan();
                        List zzad = zzdo().zzad(zzan);
                        if (zzaf().zzm(zzan)) {
                            Iterator it = zzad.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    zzgc = null;
                                    break;
                                }
                                zzgc = (zzgc) it.next();
                                if ("_lte".equals(zzgc.name)) {
                                    break;
                                }
                            }
                            if (zzgc == null || zzgc.value == null) {
                                zzgc zzgc2 = new zzgc(zzan, "auto", "_lte", zzz().currentTimeMillis(), Long.valueOf(0));
                                zzad.add(zzgc2);
                                zzdo().zza(zzgc2);
                            }
                        }
                        if (zzaf().zze(zzan, zzal.zzin)) {
                            zzfz zzdm = zzdm();
                            zzdm.zzad().zzdi().zzaq("Checking account type status for ad personalization signals");
                            if (zzdm.zzy().zzcs()) {
                                String zzan2 = zzae.zzan();
                                if (zzae.zzbl() && zzdm.zzdp().zzbc(zzan2)) {
                                    zzdm.zzad().zzdh().zzaq("Turning off ad personalization due to account type");
                                    Iterator it2 = zzad.iterator();
                                    while (true) {
                                        if (!it2.hasNext()) {
                                            break;
                                        } else if ("_npa".equals(((zzgc) it2.next()).name)) {
                                            it2.remove();
                                            break;
                                        }
                                    }
                                    zzgc zzgc3 = new zzgc(zzan2, "auto", "_npa", zzdm.zzz().currentTimeMillis(), Long.valueOf(1));
                                    zzad.add(zzgc3);
                                }
                            }
                        }
                        zzh[] zzhArr = new zzh[zzad.size()];
                        for (int i = 0; i < zzad.size(); i++) {
                            zza zzan3 = zzh.zziu().zzby(((zzgc) zzad.get(i)).name).zzan(((zzgc) zzad.get(i)).zzsx);
                            zzdm().zza(zzan3, ((zzgc) zzad.get(i)).value);
                            zzhArr[i] = (zzh) ((zzez) zzan3.zzmr());
                        }
                        zzch2.zzxp = zzhArr;
                        Bundle zzct = zzaj2.zzfd.zzct();
                        zzct.putLong("_c", 1);
                        zzad().zzdh().zzaq("Marking in-app purchase as real-time");
                        zzct.putLong("_r", 1);
                        zzct.putString("_o", zzaj2.origin);
                        if (zzab().zzbt(zzch2.zzcf)) {
                            zzab().zza(zzct, "_dbg", (Object) Long.valueOf(1));
                            zzab().zza(zzct, "_r", (Object) Long.valueOf(1));
                        }
                        zzaf zzc = zzdo().zzc(str2, zzaj2.name);
                        if (zzc == null) {
                            zzch = zzch2;
                            zzcg = zzcg2;
                            zzg = zzae;
                            bundle = zzct;
                            zzaf zzaf2 = new zzaf(str, zzaj2.name, 0, 0, zzaj2.zzfp, 0, null, null, null, null);
                            zzaf = zzaf2;
                            j = 0;
                        } else {
                            zzch = zzch2;
                            zzcg = zzcg2;
                            zzg = zzae;
                            bundle = zzct;
                            j = zzc.zzfg;
                            zzaf = zzc.zzw(zzaj2.zzfp);
                        }
                        zzdo().zza(zzaf);
                        zzae zzae2 = new zzae(this.zzl, zzaj2.origin, str, zzaj2.name, zzaj2.zzfp, j, bundle);
                        zzcf zzcf = new zzcf();
                        zzch zzch3 = zzch;
                        zzch3.zzxo = new zzcf[]{zzcf};
                        zzcf.zzxj = Long.valueOf(zzae2.timestamp);
                        zzcf.name = zzae2.name;
                        zzcf.zzxk = Long.valueOf(zzae2.zzfc);
                        zzcf.zzxi = new zzd[zzae2.zzfd.size()];
                        Iterator it3 = zzae2.zzfd.iterator();
                        int i2 = 0;
                        while (it3.hasNext()) {
                            String str3 = (String) it3.next();
                            zzd.zza zzbw = zzd.zzht().zzbw(str3);
                            zzdm().zza(zzbw, zzae2.zzfd.get(str3));
                            int i3 = i2 + 1;
                            zzcf.zzxi[i2] = (zzd) ((zzez) zzbw.zzmr());
                            i2 = i3;
                        }
                        zzch3.zzyq = (zze) ((zzez) zze.zzhy().zzb(zzc.zzhi().zzai(zzaf.zzfe).zzbu(zzaj2.name)).zzmr());
                        zzch3.zzyg = zzdn().zza(zzg.zzan(), (zzcf[]) null, zzch3.zzxp);
                        zzch3.zzxr = zzcf.zzxj;
                        zzch3.zzxs = zzcf.zzxj;
                        long zzar2 = zzg.zzar();
                        zzch3.zzxu = zzar2 != 0 ? Long.valueOf(zzar2) : null;
                        long zzaq = zzg.zzaq();
                        if (zzaq != 0) {
                            zzar2 = zzaq;
                        }
                        zzch3.zzxt = zzar2 != 0 ? Long.valueOf(zzar2) : null;
                        zzg.zzbb();
                        zzch3.zzye = Integer.valueOf((int) zzg.zzay());
                        zzch3.zzya = Long.valueOf(zzaf().zzav());
                        zzch3.zzxq = Long.valueOf(zzz().currentTimeMillis());
                        zzch3.zzyf = Boolean.TRUE;
                        zzg zzg2 = zzg;
                        zzg2.zze(zzch3.zzxr.longValue());
                        zzg2.zzf(zzch3.zzxs.longValue());
                        zzdo().zza(zzg2);
                        zzdo().setTransactionSuccessful();
                        zzdo().endTransaction();
                        try {
                            byte[] bArr2 = new byte[zzcg.zzly()];
                            zzin zzk = zzin.zzk(bArr2, 0, bArr2.length);
                            zzcg.zza(zzk);
                            zzk.zzlk();
                            return zzdm().zzc(bArr2);
                        } catch (IOException e) {
                            zzad().zzda().zza("Data loss. Failed to bundle and serialize. appId", zzau.zzao(str), e);
                            return null;
                        }
                    } catch (SecurityException e2) {
                        zzad().zzdh().zza("app instance id encryption failed", e2.getMessage());
                        byte[] bArr3 = new byte[0];
                        zzdo().endTransaction();
                        return bArr3;
                    }
                }
            } catch (SecurityException e3) {
                zzad().zzdh().zza("Resettable device id encryption failed", e3.getMessage());
                return new byte[0];
            } finally {
                zzdo().endTransaction();
            }
        } else {
            zzad().zzdh().zza("Generating a payload for this event is not available. package_name, event_name", str2, zzaj2.name);
            return null;
        }
    }

    private static String zzo(String str, String str2) {
        throw new SecurityException("This implementation should not be used.");
    }
}
