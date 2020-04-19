package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzcd extends zzip<zzcd> {
    private static volatile zzcd[] zzww;
    public String name;
    public Boolean zzwx;
    public Boolean zzwy;
    public Integer zzwz;

    public static zzcd[] zzje() {
        if (zzww == null) {
            synchronized (zzit.zzanl) {
                if (zzww == null) {
                    zzww = new zzcd[0];
                }
            }
        }
        return zzww;
    }

    public zzcd() {
        this.name = null;
        this.zzwx = null;
        this.zzwy = null;
        this.zzwz = null;
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcd)) {
            return false;
        }
        zzcd zzcd = (zzcd) obj;
        String str = this.name;
        if (str == null) {
            if (zzcd.name != null) {
                return false;
            }
        } else if (!str.equals(zzcd.name)) {
            return false;
        }
        Boolean bool = this.zzwx;
        if (bool == null) {
            if (zzcd.zzwx != null) {
                return false;
            }
        } else if (!bool.equals(zzcd.zzwx)) {
            return false;
        }
        Boolean bool2 = this.zzwy;
        if (bool2 == null) {
            if (zzcd.zzwy != null) {
                return false;
            }
        } else if (!bool2.equals(zzcd.zzwy)) {
            return false;
        }
        Integer num = this.zzwz;
        if (num == null) {
            if (zzcd.zzwz != null) {
                return false;
            }
        } else if (!num.equals(zzcd.zzwz)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzcd.zzand == null || zzcd.zzand.isEmpty();
        }
        return this.zzand.equals(zzcd.zzand);
    }

    public final int hashCode() {
        int hashCode = (getClass().getName().hashCode() + 527) * 31;
        String str = this.name;
        int i = 0;
        int hashCode2 = (hashCode + (str == null ? 0 : str.hashCode())) * 31;
        Boolean bool = this.zzwx;
        int hashCode3 = (hashCode2 + (bool == null ? 0 : bool.hashCode())) * 31;
        Boolean bool2 = this.zzwy;
        int hashCode4 = (hashCode3 + (bool2 == null ? 0 : bool2.hashCode())) * 31;
        Integer num = this.zzwz;
        int hashCode5 = (hashCode4 + (num == null ? 0 : num.hashCode())) * 31;
        if (this.zzand != null && !this.zzand.isEmpty()) {
            i = this.zzand.hashCode();
        }
        return hashCode5 + i;
    }

    public final void zza(zzin zzin) throws IOException {
        String str = this.name;
        if (str != null) {
            zzin.zzb(1, str);
        }
        Boolean bool = this.zzwx;
        if (bool != null) {
            zzin.zzb(2, bool.booleanValue());
        }
        Boolean bool2 = this.zzwy;
        if (bool2 != null) {
            zzin.zzb(3, bool2.booleanValue());
        }
        Integer num = this.zzwz;
        if (num != null) {
            zzin.zzc(4, num.intValue());
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int zzja = super.zzja();
        String str = this.name;
        if (str != null) {
            zzja += zzin.zzc(1, str);
        }
        Boolean bool = this.zzwx;
        if (bool != null) {
            bool.booleanValue();
            zzja += zzin.zzaj(2) + 1;
        }
        Boolean bool2 = this.zzwy;
        if (bool2 != null) {
            bool2.booleanValue();
            zzja += zzin.zzaj(3) + 1;
        }
        Integer num = this.zzwz;
        return num != null ? zzja + zzin.zzg(4, num.intValue()) : zzja;
    }

    public final /* synthetic */ zziv zza(zzim zzim) throws IOException {
        while (true) {
            int zzkj = zzim.zzkj();
            if (zzkj == 0) {
                return this;
            }
            if (zzkj == 10) {
                this.name = zzim.readString();
            } else if (zzkj == 16) {
                this.zzwx = Boolean.valueOf(zzim.zzkp());
            } else if (zzkj == 24) {
                this.zzwy = Boolean.valueOf(zzim.zzkp());
            } else if (zzkj == 32) {
                this.zzwz = Integer.valueOf(zzim.zzlb());
            } else if (!super.zza(zzim, zzkj)) {
                return this;
            }
        }
    }
}
