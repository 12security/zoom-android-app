package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class zzft extends zzfr {
    private static final Class<?> zzaim = Collections.unmodifiableList(Collections.emptyList()).getClass();

    private zzft() {
        super();
    }

    /* access modifiers changed from: 0000 */
    public final <L> List<L> zza(Object obj, long j) {
        return zza(obj, j, 10);
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(Object obj, long j) {
        Object obj2;
        List list = (List) zzhw.zzp(obj, j);
        if (list instanceof zzfq) {
            obj2 = ((zzfq) list).zznh();
        } else if (!zzaim.isAssignableFrom(list.getClass())) {
            if (!(list instanceof zzgt) || !(list instanceof zzfg)) {
                obj2 = Collections.unmodifiableList(list);
            } else {
                zzfg zzfg = (zzfg) list;
                if (zzfg.zzjy()) {
                    zzfg.zzjz();
                }
                return;
            }
        } else {
            return;
        }
        zzhw.zza(obj, j, obj2);
    }

    private static <L> List<L> zza(Object obj, long j, int i) {
        List<L> list;
        List<L> zzc = zzc(obj, j);
        if (zzc.isEmpty()) {
            if (zzc instanceof zzfq) {
                list = new zzfp<>(i);
            } else if (!(zzc instanceof zzgt) || !(zzc instanceof zzfg)) {
                list = new ArrayList<>(i);
            } else {
                list = ((zzfg) zzc).zzq(i);
            }
            zzhw.zza(obj, j, (Object) list);
            return list;
        } else if (zzaim.isAssignableFrom(zzc.getClass())) {
            ArrayList arrayList = new ArrayList(zzc.size() + i);
            arrayList.addAll(zzc);
            zzhw.zza(obj, j, (Object) arrayList);
            return arrayList;
        } else if (zzc instanceof zzht) {
            zzfp zzfp = new zzfp(zzc.size() + i);
            zzfp.addAll((zzht) zzc);
            zzhw.zza(obj, j, (Object) zzfp);
            return zzfp;
        } else if (!(zzc instanceof zzgt) || !(zzc instanceof zzfg)) {
            return zzc;
        } else {
            zzfg zzfg = (zzfg) zzc;
            if (zzfg.zzjy()) {
                return zzc;
            }
            zzfg zzq = zzfg.zzq(zzc.size() + i);
            zzhw.zza(obj, j, (Object) zzq);
            return zzq;
        }
    }

    /* access modifiers changed from: 0000 */
    public final <E> void zza(Object obj, Object obj2, long j) {
        List zzc = zzc(obj2, j);
        List zza = zza(obj, j, zzc.size());
        int size = zza.size();
        int size2 = zzc.size();
        if (size > 0 && size2 > 0) {
            zza.addAll(zzc);
        }
        if (size > 0) {
            zzc = zza;
        }
        zzhw.zza(obj, j, (Object) zzc);
    }

    private static <E> List<E> zzc(Object obj, long j) {
        return (List) zzhw.zzp(obj, j);
    }
}
