package com.google.android.gms.internal.measurement;

import java.util.NoSuchElementException;

final class zzdq extends zzds {
    private final int limit = this.zzacc.size();
    private int position = 0;
    private final /* synthetic */ zzdp zzacc;

    zzdq(zzdp zzdp) {
        this.zzacc = zzdp;
    }

    public final boolean hasNext() {
        return this.position < this.limit;
    }

    public final byte nextByte() {
        int i = this.position;
        if (i < this.limit) {
            this.position = i + 1;
            return this.zzacc.zzs(i);
        }
        throw new NoSuchElementException();
    }
}
