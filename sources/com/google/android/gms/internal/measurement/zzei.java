package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez.zze;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class zzei implements zzil {
    private final zzeg zzacf;

    public static zzei zza(zzeg zzeg) {
        if (zzeg.zzacw != null) {
            return zzeg.zzacw;
        }
        return new zzei(zzeg);
    }

    private zzei(zzeg zzeg) {
        this.zzacf = (zzeg) zzfb.zza(zzeg, "output");
    }

    public final int zzln() {
        return zze.zzahf;
    }

    public final void zzm(int i, int i2) throws IOException {
        this.zzacf.zzf(i, i2);
    }

    public final void zzi(int i, long j) throws IOException {
        this.zzacf.zza(i, j);
    }

    public final void zzj(int i, long j) throws IOException {
        this.zzacf.zzc(i, j);
    }

    public final void zza(int i, float f) throws IOException {
        this.zzacf.zza(i, f);
    }

    public final void zza(int i, double d) throws IOException {
        this.zzacf.zza(i, d);
    }

    public final void zzn(int i, int i2) throws IOException {
        this.zzacf.zzc(i, i2);
    }

    public final void zza(int i, long j) throws IOException {
        this.zzacf.zza(i, j);
    }

    public final void zzc(int i, int i2) throws IOException {
        this.zzacf.zzc(i, i2);
    }

    public final void zzc(int i, long j) throws IOException {
        this.zzacf.zzc(i, j);
    }

    public final void zzf(int i, int i2) throws IOException {
        this.zzacf.zzf(i, i2);
    }

    public final void zzb(int i, boolean z) throws IOException {
        this.zzacf.zzb(i, z);
    }

    public final void zzb(int i, String str) throws IOException {
        this.zzacf.zzb(i, str);
    }

    public final void zza(int i, zzdp zzdp) throws IOException {
        this.zzacf.zza(i, zzdp);
    }

    public final void zzd(int i, int i2) throws IOException {
        this.zzacf.zzd(i, i2);
    }

    public final void zze(int i, int i2) throws IOException {
        this.zzacf.zze(i, i2);
    }

    public final void zzb(int i, long j) throws IOException {
        this.zzacf.zzb(i, j);
    }

    public final void zza(int i, Object obj, zzgy zzgy) throws IOException {
        this.zzacf.zza(i, (zzgh) obj, zzgy);
    }

    public final void zzb(int i, Object obj, zzgy zzgy) throws IOException {
        zzeg zzeg = this.zzacf;
        zzgh zzgh = (zzgh) obj;
        zzeg.zzb(i, 3);
        zzgy.zza(zzgh, zzeg.zzacw);
        zzeg.zzb(i, 4);
    }

    public final void zzas(int i) throws IOException {
        this.zzacf.zzb(i, 3);
    }

    public final void zzat(int i) throws IOException {
        this.zzacf.zzb(i, 4);
    }

    public final void zza(int i, Object obj) throws IOException {
        if (obj instanceof zzdp) {
            this.zzacf.zzb(i, (zzdp) obj);
        } else {
            this.zzacf.zzb(i, (zzgh) obj);
        }
    }

    public final void zza(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzak(((Integer) list.get(i4)).intValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzaf(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzc(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzb(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzan(((Integer) list.get(i4)).intValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzai(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzf(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzc(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzat(((Long) list.get(i4)).longValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzaq(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zza(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zzd(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzau(((Long) list.get(i4)).longValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzaq(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zza(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zze(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzaw(((Long) list.get(i4)).longValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzas(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzc(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zzf(int i, List<Float> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzb(((Float) list.get(i4)).floatValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zza(((Float) list.get(i2)).floatValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zza(i, ((Float) list.get(i2)).floatValue());
            i2++;
        }
    }

    public final void zzg(int i, List<Double> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zze(((Double) list.get(i4)).doubleValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzd(((Double) list.get(i2)).doubleValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zza(i, ((Double) list.get(i2)).doubleValue());
            i2++;
        }
    }

    public final void zzh(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzap(((Integer) list.get(i4)).intValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzaf(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzc(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzi(int i, List<Boolean> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzn(((Boolean) list.get(i4)).booleanValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzm(((Boolean) list.get(i2)).booleanValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzb(i, ((Boolean) list.get(i2)).booleanValue());
            i2++;
        }
    }

    public final void zza(int i, List<String> list) throws IOException {
        int i2 = 0;
        if (list instanceof zzfq) {
            zzfq zzfq = (zzfq) list;
            while (i2 < list.size()) {
                Object zzaw = zzfq.zzaw(i2);
                if (zzaw instanceof String) {
                    this.zzacf.zzb(i, (String) zzaw);
                } else {
                    this.zzacf.zza(i, (zzdp) zzaw);
                }
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzb(i, (String) list.get(i2));
            i2++;
        }
    }

    public final void zzb(int i, List<zzdp> list) throws IOException {
        for (int i2 = 0; i2 < list.size(); i2++) {
            this.zzacf.zza(i, (zzdp) list.get(i2));
        }
    }

    public final void zzj(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzal(((Integer) list.get(i4)).intValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzag(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzd(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzk(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzao(((Integer) list.get(i4)).intValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzai(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzf(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzl(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzax(((Long) list.get(i4)).longValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzas(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzc(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zzm(int i, List<Integer> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzam(((Integer) list.get(i4)).intValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzah(((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zze(i, ((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    public final void zzn(int i, List<Long> list, boolean z) throws IOException {
        int i2 = 0;
        if (z) {
            this.zzacf.zzb(i, 2);
            int i3 = 0;
            for (int i4 = 0; i4 < list.size(); i4++) {
                i3 += zzeg.zzav(((Long) list.get(i4)).longValue());
            }
            this.zzacf.zzag(i3);
            while (i2 < list.size()) {
                this.zzacf.zzar(((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        while (i2 < list.size()) {
            this.zzacf.zzb(i, ((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    public final void zza(int i, List<?> list, zzgy zzgy) throws IOException {
        for (int i2 = 0; i2 < list.size(); i2++) {
            zza(i, list.get(i2), zzgy);
        }
    }

    public final void zzb(int i, List<?> list, zzgy zzgy) throws IOException {
        for (int i2 = 0; i2 < list.size(); i2++) {
            zzb(i, list.get(i2), zzgy);
        }
    }

    public final <K, V> void zza(int i, zzga<K, V> zzga, Map<K, V> map) throws IOException {
        for (Entry entry : map.entrySet()) {
            this.zzacf.zzb(i, 2);
            this.zzacf.zzag(zzfz.zza(zzga, entry.getKey(), entry.getValue()));
            zzfz.zza(this.zzacf, zzga, entry.getKey(), entry.getValue());
        }
    }
}
