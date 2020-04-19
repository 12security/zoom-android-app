package com.google.android.gms.internal.measurement;

import java.util.Arrays;

final class zzix {
    final int tag;
    final byte[] zzacg;

    zzix(int i, byte[] bArr) {
        this.tag = i;
        this.zzacg = bArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzix)) {
            return false;
        }
        zzix zzix = (zzix) obj;
        return this.tag == zzix.tag && Arrays.equals(this.zzacg, zzix.zzacg);
    }

    public final int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzacg);
    }
}
