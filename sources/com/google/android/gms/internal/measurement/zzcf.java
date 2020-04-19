package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzbt.zzd;
import java.io.IOException;

public final class zzcf extends zzip<zzcf> {
    private static volatile zzcf[] zzxh;
    public Integer count;
    public String name;
    public zzd[] zzxi;
    public Long zzxj;
    public Long zzxk;

    public static zzcf[] zzjf() {
        if (zzxh == null) {
            synchronized (zzit.zzanl) {
                if (zzxh == null) {
                    zzxh = new zzcf[0];
                }
            }
        }
        return zzxh;
    }

    public zzcf() {
        this.zzxi = new zzd[0];
        this.name = null;
        this.zzxj = null;
        this.zzxk = null;
        this.count = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcf)) {
            return false;
        }
        zzcf zzcf = (zzcf) obj;
        if (!zzit.equals((Object[]) this.zzxi, (Object[]) zzcf.zzxi)) {
            return false;
        }
        String str = this.name;
        if (str == null) {
            if (zzcf.name != null) {
                return false;
            }
        } else if (!str.equals(zzcf.name)) {
            return false;
        }
        Long l = this.zzxj;
        if (l == null) {
            if (zzcf.zzxj != null) {
                return false;
            }
        } else if (!l.equals(zzcf.zzxj)) {
            return false;
        }
        Long l2 = this.zzxk;
        if (l2 == null) {
            if (zzcf.zzxk != null) {
                return false;
            }
        } else if (!l2.equals(zzcf.zzxk)) {
            return false;
        }
        Integer num = this.count;
        if (num == null) {
            if (zzcf.count != null) {
                return false;
            }
        } else if (!num.equals(zzcf.count)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzcf.zzand == null || zzcf.zzand.isEmpty();
        }
        return this.zzand.equals(zzcf.zzand);
    }

    public final int hashCode() {
        int hashCode = (((getClass().getName().hashCode() + 527) * 31) + zzit.hashCode((Object[]) this.zzxi)) * 31;
        String str = this.name;
        int i = 0;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        Long l = this.zzxj;
        int hashCode3 = (hashCode2 + (l == null ? 0 : l.hashCode())) * 31;
        Long l2 = this.zzxk;
        int hashCode4 = (hashCode3 + (l2 == null ? 0 : l2.hashCode())) * 31;
        Integer num = this.count;
        int hashCode5 = (hashCode4 + (num == null ? 0 : num.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i = this.zzand.hashCode();
        }
        return hashCode5 + i;
    }

    public final void zza(zzin zzin) throws IOException {
        zzd[] zzdArr = this.zzxi;
        if (zzdArr != null && zzdArr.length > 0) {
            int i = 0;
            while (true) {
                zzd[] zzdArr2 = this.zzxi;
                if (i >= zzdArr2.length) {
                    break;
                }
                zzd zzd = zzdArr2[i];
                if (zzd != null) {
                    zzin.zze(1, zzd);
                }
                i++;
            }
        }
        String str = this.name;
        if (str != null) {
            zzin.zzb(2, str);
        }
        Long l = this.zzxj;
        if (l != null) {
            zzin.zzi(3, l.longValue());
        }
        Long l2 = this.zzxk;
        if (l2 != null) {
            zzin.zzi(4, l2.longValue());
        }
        Integer num = this.count;
        if (num != null) {
            zzin.zzc(5, num.intValue());
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int zzja = super.zzja();
        zzd[] zzdArr = this.zzxi;
        if (zzdArr != null && zzdArr.length > 0) {
            int i = 0;
            while (true) {
                zzd[] zzdArr2 = this.zzxi;
                if (i >= zzdArr2.length) {
                    break;
                }
                zzd zzd = zzdArr2[i];
                if (zzd != null) {
                    zzja += zzeg.zzc(1, (zzgh) zzd);
                }
                i++;
            }
        }
        String str = this.name;
        if (str != null) {
            zzja += zzin.zzc(2, str);
        }
        Long l = this.zzxj;
        if (l != null) {
            zzja += zzin.zzd(3, l.longValue());
        }
        Long l2 = this.zzxk;
        if (l2 != null) {
            zzja += zzin.zzd(4, l2.longValue());
        }
        Integer num = this.count;
        return num != null ? zzja + zzin.zzg(5, num.intValue()) : zzja;
    }

    public static zzcf zze(byte[] bArr) throws zziu {
        return (zzcf) zziv.zza(new zzcf(), bArr);
    }

    public final /* synthetic */ zziv zza(zzim zzim) throws IOException {
        while (true) {
            int zzkj = zzim.zzkj();
            if (zzkj == 0) {
                return this;
            }
            if (zzkj == 10) {
                int zzb = zziy.zzb(zzim, 10);
                zzd[] zzdArr = this.zzxi;
                int length = zzdArr == null ? 0 : zzdArr.length;
                zzd[] zzdArr2 = new zzd[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzxi, 0, zzdArr2, 0, length);
                }
                while (length < zzdArr2.length - 1) {
                    zzdArr2[length] = (zzd) zzim.zza(zzd.zzgs());
                    zzim.zzkj();
                    length++;
                }
                zzdArr2[length] = (zzd) zzim.zza(zzd.zzgs());
                this.zzxi = zzdArr2;
            } else if (zzkj == 18) {
                this.name = zzim.readString();
            } else if (zzkj == 24) {
                this.zzxj = Long.valueOf(zzim.zzlc());
            } else if (zzkj == 32) {
                this.zzxk = Long.valueOf(zzim.zzlc());
            } else if (zzkj == 40) {
                this.count = Integer.valueOf(zzim.zzlb());
            } else if (!super.zza(zzim, zzkj)) {
                return this;
            }
        }
    }
}
