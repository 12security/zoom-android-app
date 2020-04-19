package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez.zze;

final class zzgw implements zzgf {
    private final int flags;
    private final String info;
    private final Object[] zzajb;
    private final zzgh zzaje;

    zzgw(zzgh zzgh, String str, Object[] objArr) {
        this.zzaje = zzgh;
        this.info = str;
        this.zzajb = objArr;
        char charAt = str.charAt(0);
        if (charAt < 55296) {
            this.flags = charAt;
            return;
        }
        char c = charAt & 8191;
        int i = 13;
        int i2 = 1;
        while (true) {
            int i3 = i2 + 1;
            char charAt2 = str.charAt(i2);
            if (charAt2 >= 55296) {
                c |= (charAt2 & 8191) << i;
                i += 13;
                i2 = i3;
            } else {
                this.flags = c | (charAt2 << i);
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final String zzob() {
        return this.info;
    }

    /* access modifiers changed from: 0000 */
    public final Object[] zzoc() {
        return this.zzajb;
    }

    public final zzgh zznu() {
        return this.zzaje;
    }

    public final int zzns() {
        return (this.flags & 1) == 1 ? zze.zzahc : zze.zzahd;
    }

    public final boolean zznt() {
        return (this.flags & 2) == 2;
    }
}
