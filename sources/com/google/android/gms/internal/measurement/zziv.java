package com.google.android.gms.internal.measurement;

import java.io.IOException;

public abstract class zziv {
    protected volatile int zzanm = -1;

    public abstract zziv zza(zzim zzim) throws IOException;

    public void zza(zzin zzin) throws IOException {
    }

    /* access modifiers changed from: protected */
    public int zzja() {
        return 0;
    }

    public final int zzly() {
        int zzja = zzja();
        this.zzanm = zzja;
        return zzja;
    }

    public static final byte[] zzb(zziv zziv) {
        byte[] bArr = new byte[zziv.zzly()];
        try {
            zzin zzk = zzin.zzk(bArr, 0, bArr.length);
            zziv.zza(zzk);
            zzk.zzlk();
            return bArr;
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public static final <T extends zziv> T zza(T t, byte[] bArr) throws zziu {
        return zza(t, bArr, 0, bArr.length);
    }

    private static final <T extends zziv> T zza(T t, byte[] bArr, int i, int i2) throws zziu {
        try {
            zzim zzj = zzim.zzj(bArr, 0, i2);
            t.zza(zzj);
            zzj.zzu(0);
            return t;
        } catch (zziu e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
        }
    }

    public String toString() {
        return zziw.zzc(this);
    }

    /* renamed from: zzpe */
    public zziv clone() throws CloneNotSupportedException {
        return (zziv) super.clone();
    }
}
