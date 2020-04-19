package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzip;
import java.io.IOException;

public abstract class zzip<M extends zzip<M>> extends zziv {
    protected zzir zzand;

    /* access modifiers changed from: protected */
    public int zzja() {
        if (this.zzand == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.zzand.size(); i2++) {
            i += this.zzand.zzbn(i2).zzja();
        }
        return i;
    }

    public void zza(zzin zzin) throws IOException {
        if (this.zzand != null) {
            for (int i = 0; i < this.zzand.size(); i++) {
                this.zzand.zzbn(i).zza(zzin);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final boolean zza(zzim zzim, int i) throws IOException {
        int position = zzim.getPosition();
        if (!zzim.zzv(i)) {
            return false;
        }
        int i2 = i >>> 3;
        zzix zzix = new zzix(i, zzim.zzt(position, zzim.getPosition() - position));
        zzis zzis = null;
        zzir zzir = this.zzand;
        if (zzir == null) {
            this.zzand = new zzir();
        } else {
            zzis = zzir.zzbm(i2);
        }
        if (zzis == null) {
            zzis = new zzis();
            this.zzand.zza(i2, zzis);
        }
        zzis.zza(zzix);
        return true;
    }

    public final /* synthetic */ zziv zzpe() throws CloneNotSupportedException {
        return (zzip) clone();
    }

    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzip zzip = (zzip) super.clone();
        zzit.zza(this, zzip);
        return zzip;
    }
}
