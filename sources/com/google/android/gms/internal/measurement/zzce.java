package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzbr.zza;
import java.io.IOException;

public final class zzce extends zzip<zzce> {
    public String zzch;
    public Long zzxa;
    private Integer zzxb;
    public zza[] zzxc;
    public zzcd[] zzxd;
    public zzbx[] zzxe;
    private String zzxf;
    public Boolean zzxg;

    public zzce() {
        this.zzxa = null;
        this.zzch = null;
        this.zzxb = null;
        this.zzxc = new zza[0];
        this.zzxd = zzcd.zzje();
        this.zzxe = zzbx.zziz();
        this.zzxf = null;
        this.zzxg = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzce)) {
            return false;
        }
        zzce zzce = (zzce) obj;
        Long l = this.zzxa;
        if (l == null) {
            if (zzce.zzxa != null) {
                return false;
            }
        } else if (!l.equals(zzce.zzxa)) {
            return false;
        }
        String str = this.zzch;
        if (str == null) {
            if (zzce.zzch != null) {
                return false;
            }
        } else if (!str.equals(zzce.zzch)) {
            return false;
        }
        Integer num = this.zzxb;
        if (num == null) {
            if (zzce.zzxb != null) {
                return false;
            }
        } else if (!num.equals(zzce.zzxb)) {
            return false;
        }
        if (!zzit.equals((Object[]) this.zzxc, (Object[]) zzce.zzxc) || !zzit.equals((Object[]) this.zzxd, (Object[]) zzce.zzxd) || !zzit.equals((Object[]) this.zzxe, (Object[]) zzce.zzxe)) {
            return false;
        }
        String str2 = this.zzxf;
        if (str2 == null) {
            if (zzce.zzxf != null) {
                return false;
            }
        } else if (!str2.equals(zzce.zzxf)) {
            return false;
        }
        Boolean bool = this.zzxg;
        if (bool == null) {
            if (zzce.zzxg != null) {
                return false;
            }
        } else if (!bool.equals(zzce.zzxg)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzce.zzand == null || zzce.zzand.isEmpty();
        }
        return this.zzand.equals(zzce.zzand);
    }

    public final int hashCode() {
        int hashCode = (getClass().getName().hashCode() + 527) * 31;
        Long l = this.zzxa;
        int i = 0;
        int hashCode2 = (hashCode + (l == null ? 0 : l.hashCode())) * 31;
        String str = this.zzch;
        int hashCode3 = (hashCode2 + (str == null ? 0 : str.hashCode())) * 31;
        Integer num = this.zzxb;
        int hashCode4 = (((((((hashCode3 + (num == null ? 0 : num.hashCode())) * 31) + zzit.hashCode((Object[]) this.zzxc)) * 31) + zzit.hashCode((Object[]) this.zzxd)) * 31) + zzit.hashCode((Object[]) this.zzxe)) * 31;
        String str2 = this.zzxf;
        int hashCode5 = (hashCode4 + (str2 == null ? 0 : str2.hashCode())) * 31;
        Boolean bool = this.zzxg;
        int hashCode6 = (hashCode5 + (bool == null ? 0 : bool.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i = this.zzand.hashCode();
        }
        return hashCode6 + i;
    }

    public final void zza(zzin zzin) throws IOException {
        Long l = this.zzxa;
        if (l != null) {
            zzin.zzi(1, l.longValue());
        }
        String str = this.zzch;
        if (str != null) {
            zzin.zzb(2, str);
        }
        Integer num = this.zzxb;
        if (num != null) {
            zzin.zzc(3, num.intValue());
        }
        zza[] zzaArr = this.zzxc;
        int i = 0;
        if (zzaArr != null && zzaArr.length > 0) {
            int i2 = 0;
            while (true) {
                zza[] zzaArr2 = this.zzxc;
                if (i2 >= zzaArr2.length) {
                    break;
                }
                zza zza = zzaArr2[i2];
                if (zza != null) {
                    zzin.zze(4, zza);
                }
                i2++;
            }
        }
        zzcd[] zzcdArr = this.zzxd;
        if (zzcdArr != null && zzcdArr.length > 0) {
            int i3 = 0;
            while (true) {
                zzcd[] zzcdArr2 = this.zzxd;
                if (i3 >= zzcdArr2.length) {
                    break;
                }
                zzcd zzcd = zzcdArr2[i3];
                if (zzcd != null) {
                    zzin.zza(5, zzcd);
                }
                i3++;
            }
        }
        zzbx[] zzbxArr = this.zzxe;
        if (zzbxArr != null && zzbxArr.length > 0) {
            while (true) {
                zzbx[] zzbxArr2 = this.zzxe;
                if (i >= zzbxArr2.length) {
                    break;
                }
                zzbx zzbx = zzbxArr2[i];
                if (zzbx != null) {
                    zzin.zza(6, zzbx);
                }
                i++;
            }
        }
        String str2 = this.zzxf;
        if (str2 != null) {
            zzin.zzb(7, str2);
        }
        Boolean bool = this.zzxg;
        if (bool != null) {
            zzin.zzb(8, bool.booleanValue());
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int zzja = super.zzja();
        Long l = this.zzxa;
        if (l != null) {
            zzja += zzin.zzd(1, l.longValue());
        }
        String str = this.zzch;
        if (str != null) {
            zzja += zzin.zzc(2, str);
        }
        Integer num = this.zzxb;
        if (num != null) {
            zzja += zzin.zzg(3, num.intValue());
        }
        zza[] zzaArr = this.zzxc;
        int i = 0;
        if (zzaArr != null && zzaArr.length > 0) {
            int i2 = zzja;
            int i3 = 0;
            while (true) {
                zza[] zzaArr2 = this.zzxc;
                if (i3 >= zzaArr2.length) {
                    break;
                }
                zza zza = zzaArr2[i3];
                if (zza != null) {
                    i2 += zzeg.zzc(4, (zzgh) zza);
                }
                i3++;
            }
            zzja = i2;
        }
        zzcd[] zzcdArr = this.zzxd;
        if (zzcdArr != null && zzcdArr.length > 0) {
            int i4 = zzja;
            int i5 = 0;
            while (true) {
                zzcd[] zzcdArr2 = this.zzxd;
                if (i5 >= zzcdArr2.length) {
                    break;
                }
                zzcd zzcd = zzcdArr2[i5];
                if (zzcd != null) {
                    i4 += zzin.zzb(5, (zziv) zzcd);
                }
                i5++;
            }
            zzja = i4;
        }
        zzbx[] zzbxArr = this.zzxe;
        if (zzbxArr != null && zzbxArr.length > 0) {
            while (true) {
                zzbx[] zzbxArr2 = this.zzxe;
                if (i >= zzbxArr2.length) {
                    break;
                }
                zzbx zzbx = zzbxArr2[i];
                if (zzbx != null) {
                    zzja += zzin.zzb(6, (zziv) zzbx);
                }
                i++;
            }
        }
        String str2 = this.zzxf;
        if (str2 != null) {
            zzja += zzin.zzc(7, str2);
        }
        Boolean bool = this.zzxg;
        if (bool == null) {
            return zzja;
        }
        bool.booleanValue();
        return zzja + zzin.zzaj(8) + 1;
    }

    public final /* synthetic */ zziv zza(zzim zzim) throws IOException {
        while (true) {
            int zzkj = zzim.zzkj();
            if (zzkj == 0) {
                return this;
            }
            if (zzkj == 8) {
                this.zzxa = Long.valueOf(zzim.zzlc());
            } else if (zzkj == 18) {
                this.zzch = zzim.readString();
            } else if (zzkj == 24) {
                this.zzxb = Integer.valueOf(zzim.zzlb());
            } else if (zzkj == 34) {
                int zzb = zziy.zzb(zzim, 34);
                zza[] zzaArr = this.zzxc;
                int length = zzaArr == null ? 0 : zzaArr.length;
                zza[] zzaArr2 = new zza[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzxc, 0, zzaArr2, 0, length);
                }
                while (length < zzaArr2.length - 1) {
                    zzaArr2[length] = (zza) zzim.zza(zza.zzgs());
                    zzim.zzkj();
                    length++;
                }
                zzaArr2[length] = (zza) zzim.zza(zza.zzgs());
                this.zzxc = zzaArr2;
            } else if (zzkj == 42) {
                int zzb2 = zziy.zzb(zzim, 42);
                zzcd[] zzcdArr = this.zzxd;
                int length2 = zzcdArr == null ? 0 : zzcdArr.length;
                zzcd[] zzcdArr2 = new zzcd[(zzb2 + length2)];
                if (length2 != 0) {
                    System.arraycopy(this.zzxd, 0, zzcdArr2, 0, length2);
                }
                while (length2 < zzcdArr2.length - 1) {
                    zzcdArr2[length2] = new zzcd();
                    zzim.zza((zziv) zzcdArr2[length2]);
                    zzim.zzkj();
                    length2++;
                }
                zzcdArr2[length2] = new zzcd();
                zzim.zza((zziv) zzcdArr2[length2]);
                this.zzxd = zzcdArr2;
            } else if (zzkj == 50) {
                int zzb3 = zziy.zzb(zzim, 50);
                zzbx[] zzbxArr = this.zzxe;
                int length3 = zzbxArr == null ? 0 : zzbxArr.length;
                zzbx[] zzbxArr2 = new zzbx[(zzb3 + length3)];
                if (length3 != 0) {
                    System.arraycopy(this.zzxe, 0, zzbxArr2, 0, length3);
                }
                while (length3 < zzbxArr2.length - 1) {
                    zzbxArr2[length3] = new zzbx();
                    zzim.zza((zziv) zzbxArr2[length3]);
                    zzim.zzkj();
                    length3++;
                }
                zzbxArr2[length3] = new zzbx();
                zzim.zza((zziv) zzbxArr2[length3]);
                this.zzxe = zzbxArr2;
            } else if (zzkj == 58) {
                this.zzxf = zzim.readString();
            } else if (zzkj == 64) {
                this.zzxg = Boolean.valueOf(zzim.zzkp());
            } else if (!super.zza(zzim, zzkj)) {
                return this;
            }
        }
    }
}
