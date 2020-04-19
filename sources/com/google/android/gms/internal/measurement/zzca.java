package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzbl.zza.zzb;
import java.io.IOException;

public final class zzca extends zzip<zzca> {
    public zzb zzwk;
    public Boolean zzwl;
    public String zzwm;
    public String zzwn;
    public String zzwo;

    public zzca() {
        this.zzwk = null;
        this.zzwl = null;
        this.zzwm = null;
        this.zzwn = null;
        this.zzwo = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzca)) {
            return false;
        }
        zzca zzca = (zzca) obj;
        zzb zzb = this.zzwk;
        if (zzb == null) {
            if (zzca.zzwk != null) {
                return false;
            }
        } else if (!zzb.equals(zzca.zzwk)) {
            return false;
        }
        Boolean bool = this.zzwl;
        if (bool == null) {
            if (zzca.zzwl != null) {
                return false;
            }
        } else if (!bool.equals(zzca.zzwl)) {
            return false;
        }
        String str = this.zzwm;
        if (str == null) {
            if (zzca.zzwm != null) {
                return false;
            }
        } else if (!str.equals(zzca.zzwm)) {
            return false;
        }
        String str2 = this.zzwn;
        if (str2 == null) {
            if (zzca.zzwn != null) {
                return false;
            }
        } else if (!str2.equals(zzca.zzwn)) {
            return false;
        }
        String str3 = this.zzwo;
        if (str3 == null) {
            if (zzca.zzwo != null) {
                return false;
            }
        } else if (!str3.equals(zzca.zzwo)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzca.zzand == null || zzca.zzand.isEmpty();
        }
        return this.zzand.equals(zzca.zzand);
    }

    public final int hashCode() {
        int hashCode = (getClass().getName().hashCode() + 527) * 31;
        zzb zzb = this.zzwk;
        int i = 0;
        int hashCode2 = (hashCode + (zzb == null ? 0 : zzb.hashCode())) * 31;
        Boolean bool = this.zzwl;
        int hashCode3 = (hashCode2 + (bool == null ? 0 : bool.hashCode())) * 31;
        String str = this.zzwm;
        int hashCode4 = (hashCode3 + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.zzwn;
        int hashCode5 = (hashCode4 + (str2 == null ? 0 : str2.hashCode())) * 31;
        String str3 = this.zzwo;
        int hashCode6 = (hashCode5 + (str3 == null ? 0 : str3.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i = this.zzand.hashCode();
        }
        return hashCode6 + i;
    }

    public final void zza(zzin zzin) throws IOException {
        zzb zzb = this.zzwk;
        if (!(zzb == null || zzb == null)) {
            zzin.zzc(1, zzb.zzgp());
        }
        Boolean bool = this.zzwl;
        if (bool != null) {
            zzin.zzb(2, bool.booleanValue());
        }
        String str = this.zzwm;
        if (str != null) {
            zzin.zzb(3, str);
        }
        String str2 = this.zzwn;
        if (str2 != null) {
            zzin.zzb(4, str2);
        }
        String str3 = this.zzwo;
        if (str3 != null) {
            zzin.zzb(5, str3);
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int zzja = super.zzja();
        zzb zzb = this.zzwk;
        if (!(zzb == null || zzb == null)) {
            zzja += zzin.zzg(1, zzb.zzgp());
        }
        Boolean bool = this.zzwl;
        if (bool != null) {
            bool.booleanValue();
            zzja += zzin.zzaj(2) + 1;
        }
        String str = this.zzwm;
        if (str != null) {
            zzja += zzin.zzc(3, str);
        }
        String str2 = this.zzwn;
        if (str2 != null) {
            zzja += zzin.zzc(4, str2);
        }
        String str3 = this.zzwo;
        return str3 != null ? zzja + zzin.zzc(5, str3) : zzja;
    }

    public final /* synthetic */ zziv zza(zzim zzim) throws IOException {
        while (true) {
            int zzkj = zzim.zzkj();
            if (zzkj != 0) {
                if (zzkj == 8) {
                    int position = zzim.getPosition();
                    int zzlb = zzim.zzlb();
                    switch (zzlb) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            this.zzwk = zzb.zze(zzlb);
                            break;
                        default:
                            zzim.zzbj(position);
                            zza(zzim, zzkj);
                            break;
                    }
                } else if (zzkj == 16) {
                    this.zzwl = Boolean.valueOf(zzim.zzkp());
                } else if (zzkj == 26) {
                    this.zzwm = zzim.readString();
                } else if (zzkj == 34) {
                    this.zzwn = zzim.readString();
                } else if (zzkj == 42) {
                    this.zzwo = zzim.readString();
                } else if (!super.zza(zzim, zzkj)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }
}
