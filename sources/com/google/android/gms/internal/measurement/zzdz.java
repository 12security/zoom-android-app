package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.nio.charset.Charset;

class zzdz extends zzdy {
    protected final byte[] zzacg;

    zzdz(byte[] bArr) {
        if (bArr != null) {
            this.zzacg = bArr;
            return;
        }
        throw new NullPointerException();
    }

    /* access modifiers changed from: protected */
    public int zzkg() {
        return 0;
    }

    public byte zzr(int i) {
        return this.zzacg[i];
    }

    /* access modifiers changed from: 0000 */
    public byte zzs(int i) {
        return this.zzacg[i];
    }

    public int size() {
        return this.zzacg.length;
    }

    public final zzdp zza(int i, int i2) {
        int zzb = zzb(0, i2, size());
        if (zzb == 0) {
            return zzdp.zzaby;
        }
        return new zzdu(this.zzacg, zzkg(), zzb);
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzdo zzdo) throws IOException {
        zzdo.zza(this.zzacg, zzkg(), size());
    }

    /* access modifiers changed from: protected */
    public final String zza(Charset charset) {
        return new String(this.zzacg, zzkg(), size(), charset);
    }

    public final boolean zzke() {
        int zzkg = zzkg();
        return zzhy.zzf(this.zzacg, zzkg, size() + zzkg);
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzdp) || size() != ((zzdp) obj).size()) {
            return false;
        }
        if (size() == 0) {
            return true;
        }
        if (!(obj instanceof zzdz)) {
            return obj.equals(this);
        }
        zzdz zzdz = (zzdz) obj;
        int zzkf = zzkf();
        int zzkf2 = zzdz.zzkf();
        if (zzkf == 0 || zzkf2 == 0 || zzkf == zzkf2) {
            return zza(zzdz, 0, size());
        }
        return false;
    }

    /* access modifiers changed from: 0000 */
    public final boolean zza(zzdp zzdp, int i, int i2) {
        if (i2 > zzdp.size()) {
            int size = size();
            StringBuilder sb = new StringBuilder(40);
            sb.append("Length too large: ");
            sb.append(i2);
            sb.append(size);
            throw new IllegalArgumentException(sb.toString());
        } else if (i2 > zzdp.size()) {
            int size2 = zzdp.size();
            StringBuilder sb2 = new StringBuilder(59);
            sb2.append("Ran off end of other: 0, ");
            sb2.append(i2);
            sb2.append(", ");
            sb2.append(size2);
            throw new IllegalArgumentException(sb2.toString());
        } else if (!(zzdp instanceof zzdz)) {
            return zzdp.zza(0, i2).equals(zza(0, i2));
        } else {
            zzdz zzdz = (zzdz) zzdp;
            byte[] bArr = this.zzacg;
            byte[] bArr2 = zzdz.zzacg;
            int zzkg = zzkg() + i2;
            int zzkg2 = zzkg();
            int zzkg3 = zzdz.zzkg();
            while (zzkg2 < zzkg) {
                if (bArr[zzkg2] != bArr2[zzkg3]) {
                    return false;
                }
                zzkg2++;
                zzkg3++;
            }
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public final int zza(int i, int i2, int i3) {
        return zzfb.zza(i, this.zzacg, zzkg(), i3);
    }
}
