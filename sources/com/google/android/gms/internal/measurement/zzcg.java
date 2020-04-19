package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzcg extends zzip<zzcg> {
    public zzch[] zzxl;

    public zzcg() {
        this.zzxl = zzch.zzjg();
        this.zzand = null;
        this.zzanm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcg)) {
            return false;
        }
        zzcg zzcg = (zzcg) obj;
        if (!zzit.equals((Object[]) this.zzxl, (Object[]) zzcg.zzxl)) {
            return false;
        }
        if (this.zzand == null || this.zzand.isEmpty()) {
            return zzcg.zzand == null || zzcg.zzand.isEmpty();
        }
        return this.zzand.equals(zzcg.zzand);
    }

    public final int hashCode() {
        return ((((getClass().getName().hashCode() + 527) * 31) + zzit.hashCode((Object[]) this.zzxl)) * 31) + ((this.zzand == null || this.zzand.isEmpty()) ? 0 : this.zzand.hashCode());
    }

    public final void zza(zzin zzin) throws IOException {
        zzch[] zzchArr = this.zzxl;
        if (zzchArr != null && zzchArr.length > 0) {
            int i = 0;
            while (true) {
                zzch[] zzchArr2 = this.zzxl;
                if (i >= zzchArr2.length) {
                    break;
                }
                zzch zzch = zzchArr2[i];
                if (zzch != null) {
                    zzin.zza(1, zzch);
                }
                i++;
            }
        }
        super.zza(zzin);
    }

    /* access modifiers changed from: protected */
    public final int zzja() {
        int zzja = super.zzja();
        zzch[] zzchArr = this.zzxl;
        if (zzchArr != null && zzchArr.length > 0) {
            int i = 0;
            while (true) {
                zzch[] zzchArr2 = this.zzxl;
                if (i >= zzchArr2.length) {
                    break;
                }
                zzch zzch = zzchArr2[i];
                if (zzch != null) {
                    zzja += zzin.zzb(1, (zziv) zzch);
                }
                i++;
            }
        }
        return zzja;
    }

    public final /* synthetic */ zziv zza(zzim zzim) throws IOException {
        while (true) {
            int zzkj = zzim.zzkj();
            if (zzkj == 0) {
                return this;
            }
            if (zzkj == 10) {
                int zzb = zziy.zzb(zzim, 10);
                zzch[] zzchArr = this.zzxl;
                int length = zzchArr == null ? 0 : zzchArr.length;
                zzch[] zzchArr2 = new zzch[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzxl, 0, zzchArr2, 0, length);
                }
                while (length < zzchArr2.length - 1) {
                    zzchArr2[length] = new zzch();
                    zzim.zza((zziv) zzchArr2[length]);
                    zzim.zzkj();
                    length++;
                }
                zzchArr2[length] = new zzch();
                zzim.zza((zziv) zzchArr2[length]);
                this.zzxl = zzchArr2;
            } else if (!super.zza(zzim, zzkj)) {
                return this;
            }
        }
    }
}
