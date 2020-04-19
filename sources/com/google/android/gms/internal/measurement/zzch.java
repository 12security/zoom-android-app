package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzbt.zza;
import com.google.android.gms.internal.measurement.zzbt.zze;
import com.google.android.gms.internal.measurement.zzbt.zzh;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.ptapp.XmppError;
import java.io.IOException;

public final class zzch extends zzip<zzch> {
    private static volatile zzch[] zzxm;
    public String zzcf;
    public String zzcg;
    public String zzch;
    public String zzcj;
    public String zzcn;
    public String zzcp;
    public String zzdn;
    public String zzex;
    public String zzxf;
    public Integer zzxn;
    public zzcf[] zzxo;
    public zzh[] zzxp;
    public Long zzxq;
    public Long zzxr;
    public Long zzxs;
    public Long zzxt;
    public Long zzxu;
    public String zzxv;
    public String zzxw;
    public String zzxx;
    public Integer zzxy;
    public Long zzxz;
    public Long zzya;
    public String zzyb;
    public Boolean zzyc;
    public Long zzyd;
    public Integer zzye;
    public Boolean zzyf;
    public zza[] zzyg;
    public Integer zzyh;
    private Integer zzyi;
    private Integer zzyj;
    public String zzyk;
    public Long zzyl;
    public Long zzym;
    public String zzyn;
    private String zzyo;
    public Integer zzyp;
    public zze zzyq;
    public int[] zzyr;
    public Long zzys;
    public Long zzyt;

    public static zzch[] zzjg() {
        if (zzxm == null) {
            synchronized (zzit.zzanl) {
                if (zzxm == null) {
                    zzxm = new zzch[0];
                }
            }
        }
        return zzxm;
    }

    public zzch() {
        this.zzxn = null;
        this.zzxo = zzcf.zzjf();
        this.zzxp = new zzh[0];
        this.zzxq = null;
        this.zzxr = null;
        this.zzxs = null;
        this.zzxt = null;
        this.zzxu = null;
        this.zzxv = null;
        this.zzxw = null;
        this.zzxx = null;
        this.zzex = null;
        this.zzxy = null;
        this.zzcp = null;
        this.zzcf = null;
        this.zzcn = null;
        this.zzxz = null;
        this.zzya = null;
        this.zzyb = null;
        this.zzyc = null;
        this.zzcg = null;
        this.zzyd = null;
        this.zzye = null;
        this.zzdn = null;
        this.zzch = null;
        this.zzyf = null;
        this.zzyg = new zza[0];
        this.zzcj = null;
        this.zzyh = null;
        this.zzyi = null;
        this.zzyj = null;
        this.zzyk = null;
        this.zzyl = null;
        this.zzym = null;
        this.zzyn = null;
        this.zzyo = null;
        this.zzyp = null;
        this.zzxf = null;
        this.zzyq = null;
        this.zzyr = zziy.zzaiy;
        this.zzys = null;
        this.zzyt = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzch)) {
            return false;
        }
        zzch zzch2 = (zzch) obj;
        Integer num = this.zzxn;
        if (num == null) {
            if (zzch2.zzxn != null) {
                return false;
            }
        } else if (!num.equals(zzch2.zzxn)) {
            return false;
        }
        if (!zzit.equals((Object[]) this.zzxo, (Object[]) zzch2.zzxo) || !zzit.equals((Object[]) this.zzxp, (Object[]) zzch2.zzxp)) {
            return false;
        }
        Long l = this.zzxq;
        if (l == null) {
            if (zzch2.zzxq != null) {
                return false;
            }
        } else if (!l.equals(zzch2.zzxq)) {
            return false;
        }
        Long l2 = this.zzxr;
        if (l2 == null) {
            if (zzch2.zzxr != null) {
                return false;
            }
        } else if (!l2.equals(zzch2.zzxr)) {
            return false;
        }
        Long l3 = this.zzxs;
        if (l3 == null) {
            if (zzch2.zzxs != null) {
                return false;
            }
        } else if (!l3.equals(zzch2.zzxs)) {
            return false;
        }
        Long l4 = this.zzxt;
        if (l4 == null) {
            if (zzch2.zzxt != null) {
                return false;
            }
        } else if (!l4.equals(zzch2.zzxt)) {
            return false;
        }
        Long l5 = this.zzxu;
        if (l5 == null) {
            if (zzch2.zzxu != null) {
                return false;
            }
        } else if (!l5.equals(zzch2.zzxu)) {
            return false;
        }
        String str = this.zzxv;
        if (str == null) {
            if (zzch2.zzxv != null) {
                return false;
            }
        } else if (!str.equals(zzch2.zzxv)) {
            return false;
        }
        String str2 = this.zzxw;
        if (str2 == null) {
            if (zzch2.zzxw != null) {
                return false;
            }
        } else if (!str2.equals(zzch2.zzxw)) {
            return false;
        }
        String str3 = this.zzxx;
        if (str3 == null) {
            if (zzch2.zzxx != null) {
                return false;
            }
        } else if (!str3.equals(zzch2.zzxx)) {
            return false;
        }
        String str4 = this.zzex;
        if (str4 == null) {
            if (zzch2.zzex != null) {
                return false;
            }
        } else if (!str4.equals(zzch2.zzex)) {
            return false;
        }
        Integer num2 = this.zzxy;
        if (num2 == null) {
            if (zzch2.zzxy != null) {
                return false;
            }
        } else if (!num2.equals(zzch2.zzxy)) {
            return false;
        }
        String str5 = this.zzcp;
        if (str5 == null) {
            if (zzch2.zzcp != null) {
                return false;
            }
        } else if (!str5.equals(zzch2.zzcp)) {
            return false;
        }
        String str6 = this.zzcf;
        if (str6 == null) {
            if (zzch2.zzcf != null) {
                return false;
            }
        } else if (!str6.equals(zzch2.zzcf)) {
            return false;
        }
        String str7 = this.zzcn;
        if (str7 == null) {
            if (zzch2.zzcn != null) {
                return false;
            }
        } else if (!str7.equals(zzch2.zzcn)) {
            return false;
        }
        Long l6 = this.zzxz;
        if (l6 == null) {
            if (zzch2.zzxz != null) {
                return false;
            }
        } else if (!l6.equals(zzch2.zzxz)) {
            return false;
        }
        Long l7 = this.zzya;
        if (l7 == null) {
            if (zzch2.zzya != null) {
                return false;
            }
        } else if (!l7.equals(zzch2.zzya)) {
            return false;
        }
        String str8 = this.zzyb;
        if (str8 == null) {
            if (zzch2.zzyb != null) {
                return false;
            }
        } else if (!str8.equals(zzch2.zzyb)) {
            return false;
        }
        Boolean bool = this.zzyc;
        if (bool == null) {
            if (zzch2.zzyc != null) {
                return false;
            }
        } else if (!bool.equals(zzch2.zzyc)) {
            return false;
        }
        String str9 = this.zzcg;
        if (str9 == null) {
            if (zzch2.zzcg != null) {
                return false;
            }
        } else if (!str9.equals(zzch2.zzcg)) {
            return false;
        }
        Long l8 = this.zzyd;
        if (l8 == null) {
            if (zzch2.zzyd != null) {
                return false;
            }
        } else if (!l8.equals(zzch2.zzyd)) {
            return false;
        }
        Integer num3 = this.zzye;
        if (num3 == null) {
            if (zzch2.zzye != null) {
                return false;
            }
        } else if (!num3.equals(zzch2.zzye)) {
            return false;
        }
        String str10 = this.zzdn;
        if (str10 == null) {
            if (zzch2.zzdn != null) {
                return false;
            }
        } else if (!str10.equals(zzch2.zzdn)) {
            return false;
        }
        String str11 = this.zzch;
        if (str11 == null) {
            if (zzch2.zzch != null) {
                return false;
            }
        } else if (!str11.equals(zzch2.zzch)) {
            return false;
        }
        Boolean bool2 = this.zzyf;
        if (bool2 == null) {
            if (zzch2.zzyf != null) {
                return false;
            }
        } else if (!bool2.equals(zzch2.zzyf)) {
            return false;
        }
        if (!zzit.equals((Object[]) this.zzyg, (Object[]) zzch2.zzyg)) {
            return false;
        }
        String str12 = this.zzcj;
        if (str12 == null) {
            if (zzch2.zzcj != null) {
                return false;
            }
        } else if (!str12.equals(zzch2.zzcj)) {
            return false;
        }
        Integer num4 = this.zzyh;
        if (num4 == null) {
            if (zzch2.zzyh != null) {
                return false;
            }
        } else if (!num4.equals(zzch2.zzyh)) {
            return false;
        }
        Integer num5 = this.zzyi;
        if (num5 == null) {
            if (zzch2.zzyi != null) {
                return false;
            }
        } else if (!num5.equals(zzch2.zzyi)) {
            return false;
        }
        Integer num6 = this.zzyj;
        if (num6 == null) {
            if (zzch2.zzyj != null) {
                return false;
            }
        } else if (!num6.equals(zzch2.zzyj)) {
            return false;
        }
        String str13 = this.zzyk;
        if (str13 == null) {
            if (zzch2.zzyk != null) {
                return false;
            }
        } else if (!str13.equals(zzch2.zzyk)) {
            return false;
        }
        Long l9 = this.zzyl;
        if (l9 == null) {
            if (zzch2.zzyl != null) {
                return false;
            }
        } else if (!l9.equals(zzch2.zzyl)) {
            return false;
        }
        Long l10 = this.zzym;
        if (l10 == null) {
            if (zzch2.zzym != null) {
                return false;
            }
        } else if (!l10.equals(zzch2.zzym)) {
            return false;
        }
        String str14 = this.zzyn;
        if (str14 == null) {
            if (zzch2.zzyn != null) {
                return false;
            }
        } else if (!str14.equals(zzch2.zzyn)) {
            return false;
        }
        String str15 = this.zzyo;
        if (str15 == null) {
            if (zzch2.zzyo != null) {
                return false;
            }
        } else if (!str15.equals(zzch2.zzyo)) {
            return false;
        }
        Integer num7 = this.zzyp;
        if (num7 == null) {
            if (zzch2.zzyp != null) {
                return false;
            }
        } else if (!num7.equals(zzch2.zzyp)) {
            return false;
        }
        String str16 = this.zzxf;
        if (str16 == null) {
            if (zzch2.zzxf != null) {
                return false;
            }
        } else if (!str16.equals(zzch2.zzxf)) {
            return false;
        }
        zze zze = this.zzyq;
        if (zze == null) {
            if (zzch2.zzyq != null) {
                return false;
            }
        } else if (!zze.equals(zzch2.zzyq)) {
            return false;
        }
        if (!zzit.equals(this.zzyr, zzch2.zzyr)) {
            return false;
        }
        Long l11 = this.zzys;
        if (l11 == null) {
            if (zzch2.zzys != null) {
                return false;
            }
        } else if (!l11.equals(zzch2.zzys)) {
            return false;
        }
        Long l12 = this.zzyt;
        if (l12 == null) {
            if (zzch2.zzyt != null) {
                return false;
            }
        } else if (!l12.equals(zzch2.zzyt)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzch2.zzand == null || zzch2.zzand.isEmpty();
        }
        return this.zzand.equals(zzch2.zzand);
    }

    public final int hashCode() {
        int i;
        int hashCode = (getClass().getName().hashCode() + 527) * 31;
        Integer num = this.zzxn;
        int i2 = 0;
        int hashCode2 = (((((hashCode + (num == null ? 0 : num.hashCode())) * 31) + zzit.hashCode((Object[]) this.zzxo)) * 31) + zzit.hashCode((Object[]) this.zzxp)) * 31;
        Long l = this.zzxq;
        int hashCode3 = (hashCode2 + (l == null ? 0 : l.hashCode())) * 31;
        Long l2 = this.zzxr;
        int hashCode4 = (hashCode3 + (l2 == null ? 0 : l2.hashCode())) * 31;
        Long l3 = this.zzxs;
        int hashCode5 = (hashCode4 + (l3 == null ? 0 : l3.hashCode())) * 31;
        Long l4 = this.zzxt;
        int hashCode6 = (hashCode5 + (l4 == null ? 0 : l4.hashCode())) * 31;
        Long l5 = this.zzxu;
        int hashCode7 = (hashCode6 + (l5 == null ? 0 : l5.hashCode())) * 31;
        String str = this.zzxv;
        int hashCode8 = (hashCode7 + (str == null ? 0 : str.hashCode())) * 31;
        String str2 = this.zzxw;
        int hashCode9 = (hashCode8 + (str2 == null ? 0 : str2.hashCode())) * 31;
        String str3 = this.zzxx;
        int hashCode10 = (hashCode9 + (str3 == null ? 0 : str3.hashCode())) * 31;
        String str4 = this.zzex;
        int hashCode11 = (hashCode10 + (str4 == null ? 0 : str4.hashCode())) * 31;
        Integer num2 = this.zzxy;
        int hashCode12 = (hashCode11 + (num2 == null ? 0 : num2.hashCode())) * 31;
        String str5 = this.zzcp;
        int hashCode13 = (hashCode12 + (str5 == null ? 0 : str5.hashCode())) * 31;
        String str6 = this.zzcf;
        int hashCode14 = (hashCode13 + (str6 == null ? 0 : str6.hashCode())) * 31;
        String str7 = this.zzcn;
        int hashCode15 = (hashCode14 + (str7 == null ? 0 : str7.hashCode())) * 31;
        Long l6 = this.zzxz;
        int hashCode16 = (hashCode15 + (l6 == null ? 0 : l6.hashCode())) * 31;
        Long l7 = this.zzya;
        int hashCode17 = (hashCode16 + (l7 == null ? 0 : l7.hashCode())) * 31;
        String str8 = this.zzyb;
        int hashCode18 = (hashCode17 + (str8 == null ? 0 : str8.hashCode())) * 31;
        Boolean bool = this.zzyc;
        int hashCode19 = (hashCode18 + (bool == null ? 0 : bool.hashCode())) * 31;
        String str9 = this.zzcg;
        int hashCode20 = (hashCode19 + (str9 == null ? 0 : str9.hashCode())) * 31;
        Long l8 = this.zzyd;
        int hashCode21 = (hashCode20 + (l8 == null ? 0 : l8.hashCode())) * 31;
        Integer num3 = this.zzye;
        int hashCode22 = (hashCode21 + (num3 == null ? 0 : num3.hashCode())) * 31;
        String str10 = this.zzdn;
        int hashCode23 = (hashCode22 + (str10 == null ? 0 : str10.hashCode())) * 31;
        String str11 = this.zzch;
        int hashCode24 = (hashCode23 + (str11 == null ? 0 : str11.hashCode())) * 31;
        Boolean bool2 = this.zzyf;
        int hashCode25 = (((hashCode24 + (bool2 == null ? 0 : bool2.hashCode())) * 31) + zzit.hashCode((Object[]) this.zzyg)) * 31;
        String str12 = this.zzcj;
        int hashCode26 = (hashCode25 + (str12 == null ? 0 : str12.hashCode())) * 31;
        Integer num4 = this.zzyh;
        int hashCode27 = (hashCode26 + (num4 == null ? 0 : num4.hashCode())) * 31;
        Integer num5 = this.zzyi;
        int hashCode28 = (hashCode27 + (num5 == null ? 0 : num5.hashCode())) * 31;
        Integer num6 = this.zzyj;
        int hashCode29 = (hashCode28 + (num6 == null ? 0 : num6.hashCode())) * 31;
        String str13 = this.zzyk;
        int hashCode30 = (hashCode29 + (str13 == null ? 0 : str13.hashCode())) * 31;
        Long l9 = this.zzyl;
        int hashCode31 = (hashCode30 + (l9 == null ? 0 : l9.hashCode())) * 31;
        Long l10 = this.zzym;
        int hashCode32 = (hashCode31 + (l10 == null ? 0 : l10.hashCode())) * 31;
        String str14 = this.zzyn;
        int hashCode33 = (hashCode32 + (str14 == null ? 0 : str14.hashCode())) * 31;
        String str15 = this.zzyo;
        int hashCode34 = (hashCode33 + (str15 == null ? 0 : str15.hashCode())) * 31;
        Integer num7 = this.zzyp;
        int hashCode35 = (hashCode34 + (num7 == null ? 0 : num7.hashCode())) * 31;
        String str16 = this.zzxf;
        int hashCode36 = hashCode35 + (str16 == null ? 0 : str16.hashCode());
        zze zze = this.zzyq;
        int i3 = hashCode36 * 31;
        if (zze == null) {
            i = 0;
        } else {
            i = zze.hashCode();
        }
        int hashCode37 = (((i3 + i) * 31) + zzit.hashCode(this.zzyr)) * 31;
        Long l11 = this.zzys;
        int hashCode38 = (hashCode37 + (l11 == null ? 0 : l11.hashCode())) * 31;
        Long l12 = this.zzyt;
        int hashCode39 = (hashCode38 + (l12 == null ? 0 : l12.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i2 = this.zzand.hashCode();
        }
        return hashCode39 + i2;
    }

    public final void zza(zzin zzin) throws IOException {
        Integer num = this.zzxn;
        if (num != null) {
            zzin.zzc(1, num.intValue());
        }
        zzcf[] zzcfArr = this.zzxo;
        if (zzcfArr != null && zzcfArr.length > 0) {
            int i = 0;
            while (true) {
                zzcf[] zzcfArr2 = this.zzxo;
                if (i >= zzcfArr2.length) {
                    break;
                }
                zzcf zzcf2 = zzcfArr2[i];
                if (zzcf2 != null) {
                    zzin.zza(2, zzcf2);
                }
                i++;
            }
        }
        zzh[] zzhArr = this.zzxp;
        if (zzhArr != null && zzhArr.length > 0) {
            int i2 = 0;
            while (true) {
                zzh[] zzhArr2 = this.zzxp;
                if (i2 >= zzhArr2.length) {
                    break;
                }
                zzh zzh = zzhArr2[i2];
                if (zzh != null) {
                    zzin.zze(3, zzh);
                }
                i2++;
            }
        }
        Long l = this.zzxq;
        if (l != null) {
            zzin.zzi(4, l.longValue());
        }
        Long l2 = this.zzxr;
        if (l2 != null) {
            zzin.zzi(5, l2.longValue());
        }
        Long l3 = this.zzxs;
        if (l3 != null) {
            zzin.zzi(6, l3.longValue());
        }
        Long l4 = this.zzxu;
        if (l4 != null) {
            zzin.zzi(7, l4.longValue());
        }
        String str = this.zzxv;
        if (str != null) {
            zzin.zzb(8, str);
        }
        String str2 = this.zzxw;
        if (str2 != null) {
            zzin.zzb(9, str2);
        }
        String str3 = this.zzxx;
        if (str3 != null) {
            zzin.zzb(10, str3);
        }
        String str4 = this.zzex;
        if (str4 != null) {
            zzin.zzb(11, str4);
        }
        Integer num2 = this.zzxy;
        if (num2 != null) {
            zzin.zzc(12, num2.intValue());
        }
        String str5 = this.zzcp;
        if (str5 != null) {
            zzin.zzb(13, str5);
        }
        String str6 = this.zzcf;
        if (str6 != null) {
            zzin.zzb(14, str6);
        }
        String str7 = this.zzcn;
        if (str7 != null) {
            zzin.zzb(16, str7);
        }
        Long l5 = this.zzxz;
        if (l5 != null) {
            zzin.zzi(17, l5.longValue());
        }
        Long l6 = this.zzya;
        if (l6 != null) {
            zzin.zzi(18, l6.longValue());
        }
        String str8 = this.zzyb;
        if (str8 != null) {
            zzin.zzb(19, str8);
        }
        Boolean bool = this.zzyc;
        if (bool != null) {
            zzin.zzb(20, bool.booleanValue());
        }
        String str9 = this.zzcg;
        if (str9 != null) {
            zzin.zzb(21, str9);
        }
        Long l7 = this.zzyd;
        if (l7 != null) {
            zzin.zzi(22, l7.longValue());
        }
        Integer num3 = this.zzye;
        if (num3 != null) {
            zzin.zzc(23, num3.intValue());
        }
        String str10 = this.zzdn;
        if (str10 != null) {
            zzin.zzb(24, str10);
        }
        String str11 = this.zzch;
        if (str11 != null) {
            zzin.zzb(25, str11);
        }
        Long l8 = this.zzxt;
        if (l8 != null) {
            zzin.zzi(26, l8.longValue());
        }
        Boolean bool2 = this.zzyf;
        if (bool2 != null) {
            zzin.zzb(28, bool2.booleanValue());
        }
        zza[] zzaArr = this.zzyg;
        if (zzaArr != null && zzaArr.length > 0) {
            int i3 = 0;
            while (true) {
                zza[] zzaArr2 = this.zzyg;
                if (i3 >= zzaArr2.length) {
                    break;
                }
                zza zza = zzaArr2[i3];
                if (zza != null) {
                    zzin.zze(29, zza);
                }
                i3++;
            }
        }
        String str12 = this.zzcj;
        if (str12 != null) {
            zzin.zzb(30, str12);
        }
        Integer num4 = this.zzyh;
        if (num4 != null) {
            zzin.zzc(31, num4.intValue());
        }
        Integer num5 = this.zzyi;
        if (num5 != null) {
            zzin.zzc(32, num5.intValue());
        }
        Integer num6 = this.zzyj;
        if (num6 != null) {
            zzin.zzc(33, num6.intValue());
        }
        String str13 = this.zzyk;
        if (str13 != null) {
            zzin.zzb(34, str13);
        }
        Long l9 = this.zzyl;
        if (l9 != null) {
            zzin.zzi(35, l9.longValue());
        }
        Long l10 = this.zzym;
        if (l10 != null) {
            zzin.zzi(36, l10.longValue());
        }
        String str14 = this.zzyn;
        if (str14 != null) {
            zzin.zzb(37, str14);
        }
        String str15 = this.zzyo;
        if (str15 != null) {
            zzin.zzb(38, str15);
        }
        Integer num7 = this.zzyp;
        if (num7 != null) {
            zzin.zzc(39, num7.intValue());
        }
        String str16 = this.zzxf;
        if (str16 != null) {
            zzin.zzb(41, str16);
        }
        zze zze = this.zzyq;
        if (zze != null) {
            zzin.zze(44, zze);
        }
        int[] iArr = this.zzyr;
        if (iArr != null && iArr.length > 0) {
            int i4 = 0;
            while (true) {
                int[] iArr2 = this.zzyr;
                if (i4 >= iArr2.length) {
                    break;
                }
                int i5 = iArr2[i4];
                zzin.zzb(45, 0);
                zzin.zzbl(i5);
                i4++;
            }
        }
        Long l11 = this.zzys;
        if (l11 != null) {
            zzin.zzi(46, l11.longValue());
        }
        Long l12 = this.zzyt;
        if (l12 != null) {
            zzin.zzi(47, l12.longValue());
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int[] iArr;
        int zzja = super.zzja();
        Integer num = this.zzxn;
        if (num != null) {
            zzja += zzin.zzg(1, num.intValue());
        }
        zzcf[] zzcfArr = this.zzxo;
        int i = 0;
        if (zzcfArr != null && zzcfArr.length > 0) {
            int i2 = zzja;
            int i3 = 0;
            while (true) {
                zzcf[] zzcfArr2 = this.zzxo;
                if (i3 >= zzcfArr2.length) {
                    break;
                }
                zzcf zzcf2 = zzcfArr2[i3];
                if (zzcf2 != null) {
                    i2 += zzin.zzb(2, (zziv) zzcf2);
                }
                i3++;
            }
            zzja = i2;
        }
        zzh[] zzhArr = this.zzxp;
        if (zzhArr != null && zzhArr.length > 0) {
            int i4 = zzja;
            int i5 = 0;
            while (true) {
                zzh[] zzhArr2 = this.zzxp;
                if (i5 >= zzhArr2.length) {
                    break;
                }
                zzh zzh = zzhArr2[i5];
                if (zzh != null) {
                    i4 += zzeg.zzc(3, (zzgh) zzh);
                }
                i5++;
            }
            zzja = i4;
        }
        Long l = this.zzxq;
        if (l != null) {
            zzja += zzin.zzd(4, l.longValue());
        }
        Long l2 = this.zzxr;
        if (l2 != null) {
            zzja += zzin.zzd(5, l2.longValue());
        }
        Long l3 = this.zzxs;
        if (l3 != null) {
            zzja += zzin.zzd(6, l3.longValue());
        }
        Long l4 = this.zzxu;
        if (l4 != null) {
            zzja += zzin.zzd(7, l4.longValue());
        }
        String str = this.zzxv;
        if (str != null) {
            zzja += zzin.zzc(8, str);
        }
        String str2 = this.zzxw;
        if (str2 != null) {
            zzja += zzin.zzc(9, str2);
        }
        String str3 = this.zzxx;
        if (str3 != null) {
            zzja += zzin.zzc(10, str3);
        }
        String str4 = this.zzex;
        if (str4 != null) {
            zzja += zzin.zzc(11, str4);
        }
        Integer num2 = this.zzxy;
        if (num2 != null) {
            zzja += zzin.zzg(12, num2.intValue());
        }
        String str5 = this.zzcp;
        if (str5 != null) {
            zzja += zzin.zzc(13, str5);
        }
        String str6 = this.zzcf;
        if (str6 != null) {
            zzja += zzin.zzc(14, str6);
        }
        String str7 = this.zzcn;
        if (str7 != null) {
            zzja += zzin.zzc(16, str7);
        }
        Long l5 = this.zzxz;
        if (l5 != null) {
            zzja += zzin.zzd(17, l5.longValue());
        }
        Long l6 = this.zzya;
        if (l6 != null) {
            zzja += zzin.zzd(18, l6.longValue());
        }
        String str8 = this.zzyb;
        if (str8 != null) {
            zzja += zzin.zzc(19, str8);
        }
        Boolean bool = this.zzyc;
        if (bool != null) {
            bool.booleanValue();
            zzja += zzin.zzaj(20) + 1;
        }
        String str9 = this.zzcg;
        if (str9 != null) {
            zzja += zzin.zzc(21, str9);
        }
        Long l7 = this.zzyd;
        if (l7 != null) {
            zzja += zzin.zzd(22, l7.longValue());
        }
        Integer num3 = this.zzye;
        if (num3 != null) {
            zzja += zzin.zzg(23, num3.intValue());
        }
        String str10 = this.zzdn;
        if (str10 != null) {
            zzja += zzin.zzc(24, str10);
        }
        String str11 = this.zzch;
        if (str11 != null) {
            zzja += zzin.zzc(25, str11);
        }
        Long l8 = this.zzxt;
        if (l8 != null) {
            zzja += zzin.zzd(26, l8.longValue());
        }
        Boolean bool2 = this.zzyf;
        if (bool2 != null) {
            bool2.booleanValue();
            zzja += zzin.zzaj(28) + 1;
        }
        zza[] zzaArr = this.zzyg;
        if (zzaArr != null && zzaArr.length > 0) {
            int i6 = zzja;
            int i7 = 0;
            while (true) {
                zza[] zzaArr2 = this.zzyg;
                if (i7 >= zzaArr2.length) {
                    break;
                }
                zza zza = zzaArr2[i7];
                if (zza != null) {
                    i6 += zzeg.zzc(29, (zzgh) zza);
                }
                i7++;
            }
            zzja = i6;
        }
        String str12 = this.zzcj;
        if (str12 != null) {
            zzja += zzin.zzc(30, str12);
        }
        Integer num4 = this.zzyh;
        if (num4 != null) {
            zzja += zzin.zzg(31, num4.intValue());
        }
        Integer num5 = this.zzyi;
        if (num5 != null) {
            zzja += zzin.zzg(32, num5.intValue());
        }
        Integer num6 = this.zzyj;
        if (num6 != null) {
            zzja += zzin.zzg(33, num6.intValue());
        }
        String str13 = this.zzyk;
        if (str13 != null) {
            zzja += zzin.zzc(34, str13);
        }
        Long l9 = this.zzyl;
        if (l9 != null) {
            zzja += zzin.zzd(35, l9.longValue());
        }
        Long l10 = this.zzym;
        if (l10 != null) {
            zzja += zzin.zzd(36, l10.longValue());
        }
        String str14 = this.zzyn;
        if (str14 != null) {
            zzja += zzin.zzc(37, str14);
        }
        String str15 = this.zzyo;
        if (str15 != null) {
            zzja += zzin.zzc(38, str15);
        }
        Integer num7 = this.zzyp;
        if (num7 != null) {
            zzja += zzin.zzg(39, num7.intValue());
        }
        String str16 = this.zzxf;
        if (str16 != null) {
            zzja += zzin.zzc(41, str16);
        }
        zze zze = this.zzyq;
        if (zze != null) {
            zzja += zzeg.zzc(44, (zzgh) zze);
        }
        int[] iArr2 = this.zzyr;
        if (iArr2 != null && iArr2.length > 0) {
            int i8 = 0;
            while (true) {
                iArr = this.zzyr;
                if (i >= iArr.length) {
                    break;
                }
                i8 += zzin.zzar(iArr[i]);
                i++;
            }
            zzja = zzja + i8 + (iArr.length * 2);
        }
        Long l11 = this.zzys;
        if (l11 != null) {
            zzja += zzin.zzd(46, l11.longValue());
        }
        Long l12 = this.zzyt;
        return l12 != null ? zzja + zzin.zzd(47, l12.longValue()) : zzja;
    }

    public static zzch zzf(byte[] bArr) throws zziu {
        return (zzch) zziv.zza(new zzch(), bArr);
    }

    public final /* synthetic */ zziv zza(zzim zzim) throws IOException {
        while (true) {
            int zzkj = zzim.zzkj();
            switch (zzkj) {
                case 0:
                    return this;
                case 8:
                    this.zzxn = Integer.valueOf(zzim.zzlb());
                    break;
                case 18:
                    int zzb = zziy.zzb(zzim, 18);
                    zzcf[] zzcfArr = this.zzxo;
                    int length = zzcfArr == null ? 0 : zzcfArr.length;
                    zzcf[] zzcfArr2 = new zzcf[(zzb + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzxo, 0, zzcfArr2, 0, length);
                    }
                    while (length < zzcfArr2.length - 1) {
                        zzcfArr2[length] = new zzcf();
                        zzim.zza((zziv) zzcfArr2[length]);
                        zzim.zzkj();
                        length++;
                    }
                    zzcfArr2[length] = new zzcf();
                    zzim.zza((zziv) zzcfArr2[length]);
                    this.zzxo = zzcfArr2;
                    break;
                case 26:
                    int zzb2 = zziy.zzb(zzim, 26);
                    zzh[] zzhArr = this.zzxp;
                    int length2 = zzhArr == null ? 0 : zzhArr.length;
                    zzh[] zzhArr2 = new zzh[(zzb2 + length2)];
                    if (length2 != 0) {
                        System.arraycopy(this.zzxp, 0, zzhArr2, 0, length2);
                    }
                    while (length2 < zzhArr2.length - 1) {
                        zzhArr2[length2] = (zzh) zzim.zza(zzh.zzgs());
                        zzim.zzkj();
                        length2++;
                    }
                    zzhArr2[length2] = (zzh) zzim.zza(zzh.zzgs());
                    this.zzxp = zzhArr2;
                    break;
                case 32:
                    this.zzxq = Long.valueOf(zzim.zzlc());
                    break;
                case 40:
                    this.zzxr = Long.valueOf(zzim.zzlc());
                    break;
                case 48:
                    this.zzxs = Long.valueOf(zzim.zzlc());
                    break;
                case 56:
                    this.zzxu = Long.valueOf(zzim.zzlc());
                    break;
                case 66:
                    this.zzxv = zzim.readString();
                    break;
                case 74:
                    this.zzxw = zzim.readString();
                    break;
                case 82:
                    this.zzxx = zzim.readString();
                    break;
                case 90:
                    this.zzex = zzim.readString();
                    break;
                case 96:
                    this.zzxy = Integer.valueOf(zzim.zzlb());
                    break;
                case 106:
                    this.zzcp = zzim.readString();
                    break;
                case 114:
                    this.zzcf = zzim.readString();
                    break;
                case 130:
                    this.zzcn = zzim.readString();
                    break;
                case 136:
                    this.zzxz = Long.valueOf(zzim.zzlc());
                    break;
                case 144:
                    this.zzya = Long.valueOf(zzim.zzlc());
                    break;
                case 154:
                    this.zzyb = zzim.readString();
                    break;
                case 160:
                    this.zzyc = Boolean.valueOf(zzim.zzkp());
                    break;
                case DummyPolicyIDType.zPolicy_SetRingSpeakerID /*170*/:
                    this.zzcg = zzim.readString();
                    break;
                case DummyPolicyIDType.zPolicy_SetCameraAlias /*176*/:
                    this.zzyd = Long.valueOf(zzim.zzlc());
                    break;
                case DummyPolicyIDType.zPolicy_SetMessengerDNDParam /*184*/:
                    this.zzye = Integer.valueOf(zzim.zzlb());
                    break;
                case DummyPolicyIDType.zPolicy_SetShortCuts_Switch_SpeakerView /*194*/:
                    this.zzdn = zzim.readString();
                    break;
                case 202:
                    this.zzch = zzim.readString();
                    break;
                case DummyPolicyIDType.zPolicy_SetShortCuts_Show_Or_Hide_Chat /*208*/:
                    this.zzxt = Long.valueOf(zzim.zzlc());
                    break;
                case DummyPolicyIDType.zPolicy_SetShortCuts_JumpToSession /*224*/:
                    this.zzyf = Boolean.valueOf(zzim.zzkp());
                    break;
                case DummyPolicyIDType.zPolicy_KeepSignedIn /*234*/:
                    int zzb3 = zziy.zzb(zzim, DummyPolicyIDType.zPolicy_KeepSignedIn);
                    zza[] zzaArr = this.zzyg;
                    int length3 = zzaArr == null ? 0 : zzaArr.length;
                    zza[] zzaArr2 = new zza[(zzb3 + length3)];
                    if (length3 != 0) {
                        System.arraycopy(this.zzyg, 0, zzaArr2, 0, length3);
                    }
                    while (length3 < zzaArr2.length - 1) {
                        zzaArr2[length3] = (zza) zzim.zza(zza.zzgs());
                        zzim.zzkj();
                        length3++;
                    }
                    zzaArr2[length3] = (zza) zzim.zza(zza.zzgs());
                    this.zzyg = zzaArr2;
                    break;
                case 242:
                    this.zzcj = zzim.readString();
                    break;
                case 248:
                    this.zzyh = Integer.valueOf(zzim.zzlb());
                    break;
                case 256:
                    this.zzyi = Integer.valueOf(zzim.zzlb());
                    break;
                case 264:
                    this.zzyj = Integer.valueOf(zzim.zzlb());
                    break;
                case 274:
                    this.zzyk = zzim.readString();
                    break;
                case 280:
                    this.zzyl = Long.valueOf(zzim.zzlc());
                    break;
                case 288:
                    this.zzym = Long.valueOf(zzim.zzlc());
                    break;
                case 298:
                    this.zzyn = zzim.readString();
                    break;
                case XmppError.XmppError_JidMalformed /*306*/:
                    this.zzyo = zzim.readString();
                    break;
                case XmppError.XmppError_RemoteServerNotFound /*312*/:
                    this.zzyp = Integer.valueOf(zzim.zzlb());
                    break;
                case 330:
                    this.zzxf = zzim.readString();
                    break;
                case 354:
                    zze zze = (zze) zzim.zza(zze.zzgs());
                    zze zze2 = this.zzyq;
                    if (zze2 != null) {
                        zze = (zze) ((zzez) ((zze.zza) ((zze.zza) zze2.zzmh()).zza(zze)).zzmr());
                    }
                    this.zzyq = zze;
                    break;
                case 360:
                    int zzb4 = zziy.zzb(zzim, 360);
                    int[] iArr = this.zzyr;
                    int length4 = iArr == null ? 0 : iArr.length;
                    int[] iArr2 = new int[(zzb4 + length4)];
                    if (length4 != 0) {
                        System.arraycopy(this.zzyr, 0, iArr2, 0, length4);
                    }
                    while (length4 < iArr2.length - 1) {
                        iArr2[length4] = zzim.zzlb();
                        zzim.zzkj();
                        length4++;
                    }
                    iArr2[length4] = zzim.zzlb();
                    this.zzyr = iArr2;
                    break;
                case 362:
                    int zzx = zzim.zzx(zzim.zzlb());
                    int position = zzim.getPosition();
                    int i = 0;
                    while (zzim.zzpd() > 0) {
                        zzim.zzlb();
                        i++;
                    }
                    zzim.zzbj(position);
                    int[] iArr3 = this.zzyr;
                    int length5 = iArr3 == null ? 0 : iArr3.length;
                    int[] iArr4 = new int[(i + length5)];
                    if (length5 != 0) {
                        System.arraycopy(this.zzyr, 0, iArr4, 0, length5);
                    }
                    while (length5 < iArr4.length) {
                        iArr4[length5] = zzim.zzlb();
                        length5++;
                    }
                    this.zzyr = iArr4;
                    zzim.zzy(zzx);
                    break;
                case 368:
                    this.zzys = Long.valueOf(zzim.zzlc());
                    break;
                case 376:
                    this.zzyt = Long.valueOf(zzim.zzlc());
                    break;
                default:
                    if (super.zza(zzim, zzkj)) {
                        break;
                    } else {
                        return this;
                    }
            }
        }
    }
}
