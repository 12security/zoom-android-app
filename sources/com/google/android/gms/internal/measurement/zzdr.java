package com.google.android.gms.internal.measurement;

import java.util.Comparator;

final class zzdr implements Comparator<zzdp> {
    zzdr() {
    }

    public final /* synthetic */ int compare(Object obj, Object obj2) {
        zzdp zzdp = (zzdp) obj;
        zzdp zzdp2 = (zzdp) obj2;
        zzdw zzdw = (zzdw) zzdp.iterator();
        zzdw zzdw2 = (zzdw) zzdp2.iterator();
        while (zzdw.hasNext() && zzdw2.hasNext()) {
            int compare = Integer.compare(zzdp.zza(zzdw.nextByte()), zzdp.zza(zzdw2.nextByte()));
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(zzdp.size(), zzdp2.size());
    }
}
