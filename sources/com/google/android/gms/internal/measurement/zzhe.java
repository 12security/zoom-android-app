package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.Map.Entry;

final class zzhe extends zzhk {
    private final /* synthetic */ zzhb zzaki;

    private zzhe(zzhb zzhb) {
        this.zzaki = zzhb;
        super(zzhb, null);
    }

    public final Iterator<Entry<K, V>> iterator() {
        return new zzhd(this.zzaki, null);
    }

    /* synthetic */ zzhe(zzhb zzhb, zzhc zzhc) {
        this(zzhb);
    }
}
