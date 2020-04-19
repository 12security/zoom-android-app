package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzby extends zzip<zzby> {
    private static volatile zzby[] zzvz;
    public Boolean zzvx;
    public Boolean zzvy;
    public Integer zzwa;
    public String zzwb;
    public zzbz[] zzwc;
    private Boolean zzwd;
    public zzca zzwe;

    public static zzby[] zzjb() {
        if (zzvz == null) {
            synchronized (zzit.zzanl) {
                if (zzvz == null) {
                    zzvz = new zzby[0];
                }
            }
        }
        return zzvz;
    }

    public zzby() {
        this.zzwa = null;
        this.zzwb = null;
        this.zzwc = zzbz.zzjc();
        this.zzwd = null;
        this.zzwe = null;
        this.zzvx = null;
        this.zzvy = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzby)) {
            return false;
        }
        zzby zzby = (zzby) obj;
        Integer num = this.zzwa;
        if (num == null) {
            if (zzby.zzwa != null) {
                return false;
            }
        } else if (!num.equals(zzby.zzwa)) {
            return false;
        }
        String str = this.zzwb;
        if (str == null) {
            if (zzby.zzwb != null) {
                return false;
            }
        } else if (!str.equals(zzby.zzwb)) {
            return false;
        }
        if (!zzit.equals((Object[]) this.zzwc, (Object[]) zzby.zzwc)) {
            return false;
        }
        Boolean bool = this.zzwd;
        if (bool == null) {
            if (zzby.zzwd != null) {
                return false;
            }
        } else if (!bool.equals(zzby.zzwd)) {
            return false;
        }
        zzca zzca = this.zzwe;
        if (zzca == null) {
            if (zzby.zzwe != null) {
                return false;
            }
        } else if (!zzca.equals(zzby.zzwe)) {
            return false;
        }
        Boolean bool2 = this.zzvx;
        if (bool2 == null) {
            if (zzby.zzvx != null) {
                return false;
            }
        } else if (!bool2.equals(zzby.zzvx)) {
            return false;
        }
        Boolean bool3 = this.zzvy;
        if (bool3 == null) {
            if (zzby.zzvy != null) {
                return false;
            }
        } else if (!bool3.equals(zzby.zzvy)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzby.zzand == null || zzby.zzand.isEmpty();
        }
        return this.zzand.equals(zzby.zzand);
    }

    public final int hashCode() {
        int i;
        int hashCode = (getClass().getName().hashCode() + 527) * 31;
        Integer num = this.zzwa;
        int i2 = 0;
        int hashCode2 = (hashCode + (num == null ? 0 : num.hashCode())) * 31;
        String str = this.zzwb;
        int hashCode3 = (((hashCode2 + (str == null ? 0 : str.hashCode())) * 31) + zzit.hashCode((Object[]) this.zzwc)) * 31;
        Boolean bool = this.zzwd;
        int hashCode4 = hashCode3 + (bool == null ? 0 : bool.hashCode());
        zzca zzca = this.zzwe;
        int i3 = hashCode4 * 31;
        if (zzca == null) {
            i = 0;
        } else {
            i = zzca.hashCode();
        }
        int i4 = (i3 + i) * 31;
        Boolean bool2 = this.zzvx;
        int hashCode5 = (i4 + (bool2 == null ? 0 : bool2.hashCode())) * 31;
        Boolean bool3 = this.zzvy;
        int hashCode6 = (hashCode5 + (bool3 == null ? 0 : bool3.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i2 = this.zzand.hashCode();
        }
        return hashCode6 + i2;
    }

    public final void zza(zzin zzin) throws IOException {
        Integer num = this.zzwa;
        if (num != null) {
            zzin.zzc(1, num.intValue());
        }
        String str = this.zzwb;
        if (str != null) {
            zzin.zzb(2, str);
        }
        zzbz[] zzbzArr = this.zzwc;
        if (zzbzArr != null && zzbzArr.length > 0) {
            int i = 0;
            while (true) {
                zzbz[] zzbzArr2 = this.zzwc;
                if (i >= zzbzArr2.length) {
                    break;
                }
                zzbz zzbz = zzbzArr2[i];
                if (zzbz != null) {
                    zzin.zza(3, zzbz);
                }
                i++;
            }
        }
        Boolean bool = this.zzwd;
        if (bool != null) {
            zzin.zzb(4, bool.booleanValue());
        }
        zzca zzca = this.zzwe;
        if (zzca != null) {
            zzin.zza(5, zzca);
        }
        Boolean bool2 = this.zzvx;
        if (bool2 != null) {
            zzin.zzb(6, bool2.booleanValue());
        }
        Boolean bool3 = this.zzvy;
        if (bool3 != null) {
            zzin.zzb(7, bool3.booleanValue());
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
        String str = this.zzwb;
        if (str != null) {
            zzja += zzin.zzc(2, str);
        }
        zzbz[] zzbzArr = this.zzwc;
        if (zzbzArr != null && zzbzArr.length > 0) {
            int i = 0;
            while (true) {
                zzbz[] zzbzArr2 = this.zzwc;
                if (i >= zzbzArr2.length) {
                    break;
                }
                zzbz zzbz = zzbzArr2[i];
                if (zzbz != null) {
                    zzja += zzin.zzb(3, (zziv) zzbz);
                }
                i++;
            }
        }
        Boolean bool = this.zzwd;
        if (bool != null) {
            bool.booleanValue();
            zzja += zzin.zzaj(4) + 1;
        }
        zzca zzca = this.zzwe;
        if (zzca != null) {
            zzja += zzin.zzb(5, (zziv) zzca);
        }
        Boolean bool2 = this.zzvx;
        if (bool2 != null) {
            bool2.booleanValue();
            zzja += zzin.zzaj(6) + 1;
        }
        Boolean bool3 = this.zzvy;
        if (bool3 == null) {
            return zzja;
        }
        bool3.booleanValue();
        return zzja + zzin.zzaj(7) + 1;
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
                this.zzwb = zzim.readString();
            } else if (zzkj == 26) {
                int zzb = zziy.zzb(zzim, 26);
                zzbz[] zzbzArr = this.zzwc;
                int length = zzbzArr == null ? 0 : zzbzArr.length;
                zzbz[] zzbzArr2 = new zzbz[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzwc, 0, zzbzArr2, 0, length);
                }
                while (length < zzbzArr2.length - 1) {
                    zzbzArr2[length] = new zzbz();
                    zzim.zza((zziv) zzbzArr2[length]);
                    zzim.zzkj();
                    length++;
                }
                zzbzArr2[length] = new zzbz();
                zzim.zza((zziv) zzbzArr2[length]);
                this.zzwc = zzbzArr2;
            } else if (zzkj == 32) {
                this.zzwd = Boolean.valueOf(zzim.zzkp());
            } else if (zzkj == 42) {
                if (this.zzwe == null) {
                    this.zzwe = new zzca();
                }
                zzim.zza((zziv) this.zzwe);
            } else if (zzkj == 48) {
                this.zzvx = Boolean.valueOf(zzim.zzkp());
            } else if (zzkj == 56) {
                this.zzvy = Boolean.valueOf(zzim.zzkp());
            } else if (!super.zza(zzim, zzkj)) {
                return this;
            }
        }
    }
}
