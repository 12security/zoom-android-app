package com.google.android.gms.internal.measurement;

final class zzdu extends zzdz {
    private final int zzacd;
    private final int zzace;

    zzdu(byte[] bArr, int i, int i2) {
        super(bArr);
        zzb(i, i + i2, bArr.length);
        this.zzacd = i;
        this.zzace = i2;
    }

    public final byte zzr(int i) {
        int size = size();
        if (((size - (i + 1)) | i) >= 0) {
            return this.zzacg[this.zzacd + i];
        }
        if (i < 0) {
            StringBuilder sb = new StringBuilder(22);
            sb.append("Index < 0: ");
            sb.append(i);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder(40);
        sb2.append("Index > length: ");
        sb2.append(i);
        sb2.append(", ");
        sb2.append(size);
        throw new ArrayIndexOutOfBoundsException(sb2.toString());
    }

    /* access modifiers changed from: 0000 */
    public final byte zzs(int i) {
        return this.zzacg[this.zzacd + i];
    }

    public final int size() {
        return this.zzace;
    }

    /* access modifiers changed from: protected */
    public final int zzkg() {
        return this.zzacd;
    }
}
