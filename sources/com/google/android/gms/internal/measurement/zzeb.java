package com.google.android.gms.internal.measurement;

import java.io.IOException;

public abstract class zzeb {
    int zzach;
    int zzaci;
    private int zzacj;
    zzee zzack;
    private boolean zzacl;

    public static int zzaa(int i) {
        return (-(i & 1)) ^ (i >>> 1);
    }

    public static long zzap(long j) {
        return (-(j & 1)) ^ (j >>> 1);
    }

    public static zzeb zzd(byte[] bArr, int i, int i2) {
        return zza(bArr, i, i2, false);
    }

    public abstract double readDouble() throws IOException;

    public abstract float readFloat() throws IOException;

    public abstract String readString() throws IOException;

    public abstract <T extends zzgh> T zza(zzgs<T> zzgs, zzem zzem) throws IOException;

    public abstract int zzkj() throws IOException;

    public abstract long zzkk() throws IOException;

    public abstract long zzkl() throws IOException;

    public abstract int zzkm() throws IOException;

    public abstract long zzkn() throws IOException;

    public abstract int zzko() throws IOException;

    public abstract boolean zzkp() throws IOException;

    public abstract String zzkq() throws IOException;

    public abstract zzdp zzkr() throws IOException;

    public abstract int zzks() throws IOException;

    public abstract int zzkt() throws IOException;

    public abstract int zzku() throws IOException;

    public abstract long zzkv() throws IOException;

    public abstract int zzkw() throws IOException;

    public abstract long zzkx() throws IOException;

    /* access modifiers changed from: 0000 */
    public abstract long zzky() throws IOException;

    public abstract boolean zzkz() throws IOException;

    public abstract int zzla();

    public abstract void zzu(int i) throws zzfh;

    public abstract boolean zzv(int i) throws IOException;

    public abstract int zzx(int i) throws zzfh;

    public abstract void zzy(int i);

    public abstract void zzz(int i) throws IOException;

    static zzeb zza(byte[] bArr, int i, int i2, boolean z) {
        zzed zzed = new zzed(bArr, i, i2, false);
        try {
            zzed.zzx(i2);
            return zzed;
        } catch (zzfh e) {
            throw new IllegalArgumentException(e);
        }
    }

    private zzeb() {
        this.zzaci = 100;
        this.zzacj = Integer.MAX_VALUE;
        this.zzacl = false;
    }

    public final int zzw(int i) {
        if (i >= 0) {
            int i2 = this.zzaci;
            this.zzaci = i;
            return i2;
        }
        StringBuilder sb = new StringBuilder(47);
        sb.append("Recursion limit cannot be negative: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
}
