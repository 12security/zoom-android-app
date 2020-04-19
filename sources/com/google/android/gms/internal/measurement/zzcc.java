package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzbl.zzb.C4565zzb;
import java.io.IOException;

public final class zzcc extends zzip<zzcc> {
    public C4565zzb zzws;
    public String zzwt;
    public Boolean zzwu;
    public String[] zzwv;

    public zzcc() {
        this.zzws = null;
        this.zzwt = null;
        this.zzwu = null;
        this.zzwv = zziy.zzanv;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcc)) {
            return false;
        }
        zzcc zzcc = (zzcc) obj;
        C4565zzb zzb = this.zzws;
        if (zzb == null) {
            if (zzcc.zzws != null) {
                return false;
            }
        } else if (!zzb.equals(zzcc.zzws)) {
            return false;
        }
        String str = this.zzwt;
        if (str == null) {
            if (zzcc.zzwt != null) {
                return false;
            }
        } else if (!str.equals(zzcc.zzwt)) {
            return false;
        }
        Boolean bool = this.zzwu;
        if (bool == null) {
            if (zzcc.zzwu != null) {
                return false;
            }
        } else if (!bool.equals(zzcc.zzwu)) {
            return false;
        }
        if (!zzit.equals((Object[]) this.zzwv, (Object[]) zzcc.zzwv)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzcc.zzand == null || zzcc.zzand.isEmpty();
        }
        return this.zzand.equals(zzcc.zzand);
    }

    public final int hashCode() {
        int hashCode = (getClass().getName().hashCode() + 527) * 31;
        C4565zzb zzb = this.zzws;
        int i = 0;
        int hashCode2 = (hashCode + (zzb == null ? 0 : zzb.hashCode())) * 31;
        String str = this.zzwt;
        int hashCode3 = (hashCode2 + (str == null ? 0 : str.hashCode())) * 31;
        Boolean bool = this.zzwu;
        int hashCode4 = (((hashCode3 + (bool == null ? 0 : bool.hashCode())) * 31) + zzit.hashCode((Object[]) this.zzwv)) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i = this.zzand.hashCode();
        }
        return hashCode4 + i;
    }

    public final void zza(zzin zzin) throws IOException {
        C4565zzb zzb = this.zzws;
        if (!(zzb == null || zzb == null)) {
            zzin.zzc(1, zzb.zzgp());
        }
        String str = this.zzwt;
        if (str != null) {
            zzin.zzb(2, str);
        }
        Boolean bool = this.zzwu;
        if (bool != null) {
            zzin.zzb(3, bool.booleanValue());
        }
        String[] strArr = this.zzwv;
        if (strArr != null && strArr.length > 0) {
            int i = 0;
            while (true) {
                String[] strArr2 = this.zzwv;
                if (i >= strArr2.length) {
                    break;
                }
                String str2 = strArr2[i];
                if (str2 != null) {
                    zzin.zzb(4, str2);
                }
                i++;
            }
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int zzja = super.zzja();
        C4565zzb zzb = this.zzws;
        if (!(zzb == null || zzb == null)) {
            zzja += zzin.zzg(1, zzb.zzgp());
        }
        String str = this.zzwt;
        if (str != null) {
            zzja += zzin.zzc(2, str);
        }
        Boolean bool = this.zzwu;
        if (bool != null) {
            bool.booleanValue();
            zzja += zzin.zzaj(3) + 1;
        }
        String[] strArr = this.zzwv;
        if (strArr == null || strArr.length <= 0) {
            return zzja;
        }
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            String[] strArr2 = this.zzwv;
            if (i >= strArr2.length) {
                return zzja + i2 + (i3 * 1);
            }
            String str2 = strArr2[i];
            if (str2 != null) {
                i3++;
                i2 += zzin.zzcp(str2);
            }
            i++;
        }
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
                        case 5:
                        case 6:
                            this.zzws = C4565zzb.zzg(zzlb);
                            break;
                        default:
                            zzim.zzbj(position);
                            zza(zzim, zzkj);
                            break;
                    }
                } else if (zzkj == 18) {
                    this.zzwt = zzim.readString();
                } else if (zzkj == 24) {
                    this.zzwu = Boolean.valueOf(zzim.zzkp());
                } else if (zzkj == 34) {
                    int zzb = zziy.zzb(zzim, 34);
                    String[] strArr = this.zzwv;
                    int length = strArr == null ? 0 : strArr.length;
                    String[] strArr2 = new String[(zzb + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzwv, 0, strArr2, 0, length);
                    }
                    while (length < strArr2.length - 1) {
                        strArr2[length] = zzim.readString();
                        zzim.zzkj();
                        length++;
                    }
                    strArr2[length] = zzim.readString();
                    this.zzwv = strArr2;
                } else if (!super.zza(zzim, zzkj)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }
}
