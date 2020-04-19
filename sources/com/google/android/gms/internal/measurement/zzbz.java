package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzbz extends zzip<zzbz> {
    private static volatile zzbz[] zzwf;
    public zzcc zzwg;
    public zzca zzwh;
    public Boolean zzwi;
    public String zzwj;

    public static zzbz[] zzjc() {
        if (zzwf == null) {
            synchronized (zzit.zzanl) {
                if (zzwf == null) {
                    zzwf = new zzbz[0];
                }
            }
        }
        return zzwf;
    }

    public zzbz() {
        this.zzwg = null;
        this.zzwh = null;
        this.zzwi = null;
        this.zzwj = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzbz)) {
            return false;
        }
        zzbz zzbz = (zzbz) obj;
        zzcc zzcc = this.zzwg;
        if (zzcc == null) {
            if (zzbz.zzwg != null) {
                return false;
            }
        } else if (!zzcc.equals(zzbz.zzwg)) {
            return false;
        }
        zzca zzca = this.zzwh;
        if (zzca == null) {
            if (zzbz.zzwh != null) {
                return false;
            }
        } else if (!zzca.equals(zzbz.zzwh)) {
            return false;
        }
        Boolean bool = this.zzwi;
        if (bool == null) {
            if (zzbz.zzwi != null) {
                return false;
            }
        } else if (!bool.equals(zzbz.zzwi)) {
            return false;
        }
        String str = this.zzwj;
        if (str == null) {
            if (zzbz.zzwj != null) {
                return false;
            }
        } else if (!str.equals(zzbz.zzwj)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzbz.zzand == null || zzbz.zzand.isEmpty();
        }
        return this.zzand.equals(zzbz.zzand);
    }

    public final int hashCode() {
        int i;
        int i2;
        int hashCode = getClass().getName().hashCode() + 527;
        zzcc zzcc = this.zzwg;
        int i3 = hashCode * 31;
        int i4 = 0;
        if (zzcc == null) {
            i = 0;
        } else {
            i = zzcc.hashCode();
        }
        int i5 = i3 + i;
        zzca zzca = this.zzwh;
        int i6 = i5 * 31;
        if (zzca == null) {
            i2 = 0;
        } else {
            i2 = zzca.hashCode();
        }
        int i7 = (i6 + i2) * 31;
        Boolean bool = this.zzwi;
        int hashCode2 = (i7 + (bool == null ? 0 : bool.hashCode())) * 31;
        String str = this.zzwj;
        int hashCode3 = (hashCode2 + (str == null ? 0 : str.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i4 = this.zzand.hashCode();
        }
        return hashCode3 + i4;
    }

    public final void zza(zzin zzin) throws IOException {
        zzcc zzcc = this.zzwg;
        if (zzcc != null) {
            zzin.zza(1, zzcc);
        }
        zzca zzca = this.zzwh;
        if (zzca != null) {
            zzin.zza(2, zzca);
        }
        Boolean bool = this.zzwi;
        if (bool != null) {
            zzin.zzb(3, bool.booleanValue());
        }
        String str = this.zzwj;
        if (str != null) {
            zzin.zzb(4, str);
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int zzja = super.zzja();
        zzcc zzcc = this.zzwg;
        if (zzcc != null) {
            zzja += zzin.zzb(1, (zziv) zzcc);
        }
        zzca zzca = this.zzwh;
        if (zzca != null) {
            zzja += zzin.zzb(2, (zziv) zzca);
        }
        Boolean bool = this.zzwi;
        if (bool != null) {
            bool.booleanValue();
            zzja += zzin.zzaj(3) + 1;
        }
        String str = this.zzwj;
        return str != null ? zzja + zzin.zzc(4, str) : zzja;
    }

    public final /* synthetic */ zziv zza(zzim zzim) throws IOException {
        while (true) {
            int zzkj = zzim.zzkj();
            if (zzkj == 0) {
                return this;
            }
            if (zzkj == 10) {
                if (this.zzwg == null) {
                    this.zzwg = new zzcc();
                }
                zzim.zza((zziv) this.zzwg);
            } else if (zzkj == 18) {
                if (this.zzwh == null) {
                    this.zzwh = new zzca();
                }
                zzim.zza((zziv) this.zzwh);
            } else if (zzkj == 24) {
                this.zzwi = Boolean.valueOf(zzim.zzkp());
            } else if (zzkj == 34) {
                this.zzwj = zzim.readString();
            } else if (!super.zza(zzim, zzkj)) {
                return this;
            }
        }
    }
}
