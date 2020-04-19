package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;

final class zzaf {
    final String name;
    final String zzcf;
    final long zzfe;
    final long zzff;
    final long zzfg;
    final long zzfh;
    final Long zzfi;
    final Long zzfj;
    final Long zzfk;
    final Boolean zzfl;

    zzaf(String str, String str2, long j, long j2, long j3, long j4, Long l, Long l2, Long l3, Boolean bool) {
        long j5 = j;
        long j6 = j2;
        long j7 = j4;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        boolean z = true;
        Preconditions.checkArgument(j5 >= 0);
        Preconditions.checkArgument(j6 >= 0);
        if (j7 < 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.zzcf = str;
        this.name = str2;
        this.zzfe = j5;
        this.zzff = j6;
        this.zzfg = j3;
        this.zzfh = j7;
        this.zzfi = l;
        this.zzfj = l2;
        this.zzfk = l3;
        this.zzfl = bool;
    }

    /* access modifiers changed from: 0000 */
    public final zzaf zzw(long j) {
        zzaf zzaf = new zzaf(this.zzcf, this.name, this.zzfe, this.zzff, j, this.zzfh, this.zzfi, this.zzfj, this.zzfk, this.zzfl);
        return zzaf;
    }

    /* access modifiers changed from: 0000 */
    public final zzaf zza(long j, long j2) {
        zzaf zzaf = new zzaf(this.zzcf, this.name, this.zzfe, this.zzff, this.zzfg, j, Long.valueOf(j2), this.zzfj, this.zzfk, this.zzfl);
        return zzaf;
    }

    /* access modifiers changed from: 0000 */
    public final zzaf zza(Long l, Long l2, Boolean bool) {
        zzaf zzaf = new zzaf(this.zzcf, this.name, this.zzfe, this.zzff, this.zzfg, this.zzfh, this.zzfi, l, l2, (bool == null || bool.booleanValue()) ? bool : null);
        return zzaf;
    }
}
