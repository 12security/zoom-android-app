package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;

final class zzgc {
    final String name;
    final String origin;
    final Object value;
    final String zzcf;
    final long zzsx;

    zzgc(String str, String str2, String str3, long j, Object obj) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str3);
        Preconditions.checkNotNull(obj);
        this.zzcf = str;
        this.origin = str2;
        this.name = str3;
        this.zzsx = j;
        this.value = obj;
    }
}
