package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzbx extends zzip<zzbx> {
    private static volatile zzbx[] zzvt;
    public Integer zzvu;
    public zzcb[] zzvv;
    public zzby[] zzvw;
    private Boolean zzvx;
    private Boolean zzvy;

    public static zzbx[] zziz() {
        if (zzvt == null) {
            synchronized (zzit.zzanl) {
                if (zzvt == null) {
                    zzvt = new zzbx[0];
                }
            }
        }
        return zzvt;
    }

    public zzbx() {
        this.zzvu = null;
        this.zzvv = zzcb.zzjd();
        this.zzvw = zzby.zzjb();
        this.zzvx = null;
        this.zzvy = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzbx)) {
            return false;
        }
        zzbx zzbx = (zzbx) obj;
        Integer num = this.zzvu;
        if (num == null) {
            if (zzbx.zzvu != null) {
                return false;
            }
        } else if (!num.equals(zzbx.zzvu)) {
            return false;
        }
        if (!zzit.equals((Object[]) this.zzvv, (Object[]) zzbx.zzvv) || !zzit.equals((Object[]) this.zzvw, (Object[]) zzbx.zzvw)) {
            return false;
        }
        Boolean bool = this.zzvx;
        if (bool == null) {
            if (zzbx.zzvx != null) {
                return false;
            }
        } else if (!bool.equals(zzbx.zzvx)) {
            return false;
        }
        Boolean bool2 = this.zzvy;
        if (bool2 == null) {
            if (zzbx.zzvy != null) {
                return false;
            }
        } else if (!bool2.equals(zzbx.zzvy)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzbx.zzand == null || zzbx.zzand.isEmpty();
        }
        return this.zzand.equals(zzbx.zzand);
    }

    public final int hashCode() {
        int hashCode = (getClass().getName().hashCode() + 527) * 31;
        Integer num = this.zzvu;
        int i = 0;
        int hashCode2 = (((((hashCode + (num == null ? 0 : num.hashCode())) * 31) + zzit.hashCode((Object[]) this.zzvv)) * 31) + zzit.hashCode((Object[]) this.zzvw)) * 31;
        Boolean bool = this.zzvx;
        int hashCode3 = (hashCode2 + (bool == null ? 0 : bool.hashCode())) * 31;
        Boolean bool2 = this.zzvy;
        int hashCode4 = (hashCode3 + (bool2 == null ? 0 : bool2.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i = this.zzand.hashCode();
        }
        return hashCode4 + i;
    }

    public final void zza(zzin zzin) throws IOException {
        Integer num = this.zzvu;
        if (num != null) {
            zzin.zzc(1, num.intValue());
        }
        zzcb[] zzcbArr = this.zzvv;
        int i = 0;
        if (zzcbArr != null && zzcbArr.length > 0) {
            int i2 = 0;
            while (true) {
                zzcb[] zzcbArr2 = this.zzvv;
                if (i2 >= zzcbArr2.length) {
                    break;
                }
                zzcb zzcb = zzcbArr2[i2];
                if (zzcb != null) {
                    zzin.zza(2, zzcb);
                }
                i2++;
            }
        }
        zzby[] zzbyArr = this.zzvw;
        if (zzbyArr != null && zzbyArr.length > 0) {
            while (true) {
                zzby[] zzbyArr2 = this.zzvw;
                if (i >= zzbyArr2.length) {
                    break;
                }
                zzby zzby = zzbyArr2[i];
                if (zzby != null) {
                    zzin.zza(3, zzby);
                }
                i++;
            }
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
        Integer num = this.zzvu;
        if (num != null) {
            zzja += zzin.zzg(1, num.intValue());
        }
        zzcb[] zzcbArr = this.zzvv;
        int i = 0;
        if (zzcbArr != null && zzcbArr.length > 0) {
            int i2 = zzja;
            int i3 = 0;
            while (true) {
                zzcb[] zzcbArr2 = this.zzvv;
                if (i3 >= zzcbArr2.length) {
                    break;
                }
                zzcb zzcb = zzcbArr2[i3];
                if (zzcb != null) {
                    i2 += zzin.zzb(2, (zziv) zzcb);
                }
                i3++;
            }
            zzja = i2;
        }
        zzby[] zzbyArr = this.zzvw;
        if (zzbyArr != null && zzbyArr.length > 0) {
            while (true) {
                zzby[] zzbyArr2 = this.zzvw;
                if (i >= zzbyArr2.length) {
                    break;
                }
                zzby zzby = zzbyArr2[i];
                if (zzby != null) {
                    zzja += zzin.zzb(3, (zziv) zzby);
                }
                i++;
            }
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
                this.zzvu = Integer.valueOf(zzim.zzlb());
            } else if (zzkj == 18) {
                int zzb = zziy.zzb(zzim, 18);
                zzcb[] zzcbArr = this.zzvv;
                int length = zzcbArr == null ? 0 : zzcbArr.length;
                zzcb[] zzcbArr2 = new zzcb[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzvv, 0, zzcbArr2, 0, length);
                }
                while (length < zzcbArr2.length - 1) {
                    zzcbArr2[length] = new zzcb();
                    zzim.zza((zziv) zzcbArr2[length]);
                    zzim.zzkj();
                    length++;
                }
                zzcbArr2[length] = new zzcb();
                zzim.zza((zziv) zzcbArr2[length]);
                this.zzvv = zzcbArr2;
            } else if (zzkj == 26) {
                int zzb2 = zziy.zzb(zzim, 26);
                zzby[] zzbyArr = this.zzvw;
                int length2 = zzbyArr == null ? 0 : zzbyArr.length;
                zzby[] zzbyArr2 = new zzby[(zzb2 + length2)];
                if (length2 != 0) {
                    System.arraycopy(this.zzvw, 0, zzbyArr2, 0, length2);
                }
                while (length2 < zzbyArr2.length - 1) {
                    zzbyArr2[length2] = new zzby();
                    zzim.zza((zziv) zzbyArr2[length2]);
                    zzim.zzkj();
                    length2++;
                }
                zzbyArr2[length2] = new zzby();
                zzim.zza((zziv) zzbyArr2[length2]);
                this.zzvw = zzbyArr2;
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
