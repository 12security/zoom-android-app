package com.google.android.gms.internal.measurement;

import java.util.List;

final class zzfu extends zzfr {
    private zzfu() {
        super();
    }

    /* access modifiers changed from: 0000 */
    public final <L> List<L> zza(Object obj, long j) {
        zzfg zzd = zzd(obj, j);
        if (zzd.zzjy()) {
            return zzd;
        }
        int size = zzd.size();
        zzfg zzq = zzd.zzq(size == 0 ? 10 : size << 1);
        zzhw.zza(obj, j, (Object) zzq);
        return zzq;
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(Object obj, long j) {
        zzd(obj, j).zzjz();
    }

    /* access modifiers changed from: 0000 */
    public final <E> void zza(Object obj, Object obj2, long j) {
        zzfg zzd = zzd(obj, j);
        zzfg zzd2 = zzd(obj2, j);
        int size = zzd.size();
        int size2 = zzd2.size();
        if (size > 0 && size2 > 0) {
            if (!zzd.zzjy()) {
                zzd = zzd.zzq(size2 + size);
            }
            zzd.addAll(zzd2);
        }
        if (size > 0) {
            zzd2 = zzd;
        }
        zzhw.zza(obj, j, (Object) zzd2);
    }

    private static <E> zzfg<E> zzd(Object obj, long j) {
        return (zzfg) zzhw.zzp(obj, j);
    }
}
