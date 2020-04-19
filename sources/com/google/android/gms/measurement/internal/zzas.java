package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.util.concurrent.atomic.AtomicReference;

public final class zzas extends zzcu {
    private static final AtomicReference<String[]> zzjm = new AtomicReference<>();
    private static final AtomicReference<String[]> zzjn = new AtomicReference<>();
    private static final AtomicReference<String[]> zzjo = new AtomicReference<>();

    zzas(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    private final boolean zzcz() {
        zzag();
        return this.zzl.zzel() && this.zzl.zzad().isLoggable(3);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzal(String str) {
        if (str == null) {
            return null;
        }
        if (!zzcz()) {
            return str;
        }
        return zza(str, zzcx.zzoz, zzcx.zzoy, zzjm);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzam(String str) {
        if (str == null) {
            return null;
        }
        if (!zzcz()) {
            return str;
        }
        return zza(str, zzcy.zzpb, zzcy.zzpa, zzjn);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzan(String str) {
        if (str == null) {
            return null;
        }
        if (!zzcz()) {
            return str;
        }
        if (!str.startsWith("_exp_")) {
            return zza(str, zzcz.zzpd, zzcz.zzpc, zzjo);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("experiment_id");
        sb.append("(");
        sb.append(str);
        sb.append(")");
        return sb.toString();
    }

    @Nullable
    private static String zza(String str, String[] strArr, String[] strArr2, AtomicReference<String[]> atomicReference) {
        String str2;
        Preconditions.checkNotNull(strArr);
        Preconditions.checkNotNull(strArr2);
        Preconditions.checkNotNull(atomicReference);
        Preconditions.checkArgument(strArr.length == strArr2.length);
        for (int i = 0; i < strArr.length; i++) {
            if (zzgd.zzs(str, strArr[i])) {
                synchronized (atomicReference) {
                    String[] strArr3 = (String[]) atomicReference.get();
                    if (strArr3 == null) {
                        strArr3 = new String[strArr2.length];
                        atomicReference.set(strArr3);
                    }
                    if (strArr3[i] == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(strArr2[i]);
                        sb.append("(");
                        sb.append(strArr[i]);
                        sb.append(")");
                        strArr3[i] = sb.toString();
                    }
                    str2 = strArr3[i];
                }
                return str2;
            }
        }
        return str;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzb(zzaj zzaj) {
        if (zzaj == null) {
            return null;
        }
        if (!zzcz()) {
            return zzaj.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("origin=");
        sb.append(zzaj.origin);
        sb.append(",name=");
        sb.append(zzal(zzaj.name));
        sb.append(",params=");
        sb.append(zzb(zzaj.zzfd));
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zza(zzae zzae) {
        if (zzae == null) {
            return null;
        }
        if (!zzcz()) {
            return zzae.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Event{appId='");
        sb.append(zzae.zzcf);
        sb.append("', name='");
        sb.append(zzal(zzae.name));
        sb.append("', params=");
        sb.append(zzb(zzae.zzfd));
        sb.append("}");
        return sb.toString();
    }

    @Nullable
    private final String zzb(zzag zzag) {
        if (zzag == null) {
            return null;
        }
        if (!zzcz()) {
            return zzag.toString();
        }
        return zzc(zzag.zzct());
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final String zzc(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (!zzcz()) {
            return bundle.toString();
        }
        StringBuilder sb = new StringBuilder();
        for (String str : bundle.keySet()) {
            if (sb.length() != 0) {
                sb.append(", ");
            } else {
                sb.append("Bundle[{");
            }
            sb.append(zzam(str));
            sb.append("=");
            sb.append(bundle.get(str));
        }
        sb.append("}]");
        return sb.toString();
    }

    public final /* bridge */ /* synthetic */ void zzn() {
        super.zzn();
    }

    public final /* bridge */ /* synthetic */ void zzo() {
        super.zzo();
    }

    public final /* bridge */ /* synthetic */ void zzp() {
        super.zzp();
    }

    public final /* bridge */ /* synthetic */ void zzq() {
        super.zzq();
    }

    public final /* bridge */ /* synthetic */ zzad zzy() {
        return super.zzy();
    }

    public final /* bridge */ /* synthetic */ Clock zzz() {
        return super.zzz();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzas zzaa() {
        return super.zzaa();
    }

    public final /* bridge */ /* synthetic */ zzgd zzab() {
        return super.zzab();
    }

    public final /* bridge */ /* synthetic */ zzbt zzac() {
        return super.zzac();
    }

    public final /* bridge */ /* synthetic */ zzau zzad() {
        return super.zzad();
    }

    public final /* bridge */ /* synthetic */ zzbf zzae() {
        return super.zzae();
    }

    public final /* bridge */ /* synthetic */ zzt zzaf() {
        return super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzq zzag() {
        return super.zzag();
    }
}
