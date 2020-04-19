package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzcb extends zzip<zzcb> {
    private static volatile zzcb[] zzwp;
    public Boolean zzvx;
    public Boolean zzvy;
    public Integer zzwa;
    public String zzwq;
    public zzbz zzwr;

    public static zzcb[] zzjd() {
        if (zzwp == null) {
            synchronized (zzit.zzanl) {
                if (zzwp == null) {
                    zzwp = new zzcb[0];
                }
            }
        }
        return zzwp;
    }

    public zzcb() {
        this.zzwa = null;
        this.zzwq = null;
        this.zzwr = null;
        this.zzvx = null;
        this.zzvy = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcb)) {
            return false;
        }
        zzcb zzcb = (zzcb) obj;
        Integer num = this.zzwa;
        if (num == null) {
            if (zzcb.zzwa != null) {
                return false;
            }
        } else if (!num.equals(zzcb.zzwa)) {
            return false;
        }
        String str = this.zzwq;
        if (str == null) {
            if (zzcb.zzwq != null) {
                return false;
            }
        } else if (!str.equals(zzcb.zzwq)) {
            return false;
        }
        zzbz zzbz = this.zzwr;
        if (zzbz == null) {
            if (zzcb.zzwr != null) {
                return false;
            }
        } else if (!zzbz.equals(zzcb.zzwr)) {
            return false;
        }
        Boolean bool = this.zzvx;
        if (bool == null) {
            if (zzcb.zzvx != null) {
                return false;
            }
        } else if (!bool.equals(zzcb.zzvx)) {
            return false;
        }
        Boolean bool2 = this.zzvy;
        if (bool2 == null) {
            if (zzcb.zzvy != null) {
                return false;
            }
        } else if (!bool2.equals(zzcb.zzvy)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzcb.zzand == null || zzcb.zzand.isEmpty();
        }
        return this.zzand.equals(zzcb.zzand);
    }

    public final int hashCode() {
        int i;
        int hashCode = (getClass().getName().hashCode() + 527) * 31;
        Integer num = this.zzwa;
        int i2 = 0;
        int hashCode2 = (hashCode + (num == null ? 0 : num.hashCode())) * 31;
        String str = this.zzwq;
        int hashCode3 = hashCode2 + (str == null ? 0 : str.hashCode());
        zzbz zzbz = this.zzwr;
        int i3 = hashCode3 * 31;
        if (zzbz == null) {
            i = 0;
        } else {
            i = zzbz.hashCode();
        }
        int i4 = (i3 + i) * 31;
        Boolean bool = this.zzvx;
        int hashCode4 = (i4 + (bool == null ? 0 : bool.hashCode())) * 31;
        Boolean bool2 = this.zzvy;
        int hashCode5 = (hashCode4 + (bool2 == null ? 0 : bool2.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i2 = this.zzand.hashCode();
        }
        return hashCode5 + i2;
    }

    public final void zza(zzin zzin) throws IOException {
        Integer num = this.zzwa;
        if (num != null) {
            zzin.zzc(1, num.intValue());
        }
        String str = this.zzwq;
        if (str != null) {
            zzin.zzb(2, str);
        }
        zzbz zzbz = this.zzwr;
        if (zzbz != null) {
            zzin.zza(3, zzbz);
        }
        Boolean bool = this.zzvx;
        if (bool != null) {
            zzin.zzb(4, bool.booleanValue());
        }
        Boolean bool2 = this.zzvy;
        if (bool2 != null) {
            zzin.zzb(5, bool2.booleanValue());
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int zzja = super.zzja();
        Integer num = this.zzwa;
        if (num != null) {
            zzja += zzin.zzg(1, num.intValue());
        }
        String str = this.zzwq;
        if (str != null) {
            zzja += zzin.zzc(2, str);
        }
        zzbz zzbz = this.zzwr;
        if (zzbz != null) {
            zzja += zzin.zzb(3, (zziv) zzbz);
        }
        Boolean bool = this.zzvx;
        if (bool != null) {
            bool.booleanValue();
            zzja += zzin.zzaj(4) + 1;
        }
        Boolean bool2 = this.zzvy;
        if (bool2 == null) {
            return zzja;
        }
        bool2.booleanValue();
        return zzja + zzin.zzaj(5) + 1;
    }

    public final /* synthetic */ zziv zza(zzim zzim) throws IOException {
        while (true) {
            int zzkj = zzim.zzkj();
            if (zzkj == 0) {
                return this;
            }
            if (zzkj == 8) {
                this.zzwa = Integer.valueOf(zzim.zzlb());
            } else if (zzkj == 18) {
                this.zzwq = zzim.readString();
            } else if (zzkj == 26) {
                if (this.zzwr == null) {
                    this.zzwr = new zzbz();
                }
                zzim.zza((zziv) this.zzwr);
            } else if (zzkj == 32) {
                this.zzvx = Boolean.valueOf(zzim.zzkp());
            } else if (zzkj == 40) {
                this.zzvy = Boolean.valueOf(zzim.zzkp());
            } else if (!super.zza(zzim, zzkj)) {
                return this;
            }
        }
    }
}
