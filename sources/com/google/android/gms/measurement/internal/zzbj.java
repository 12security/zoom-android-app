package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences.Editor;
import android.util.Pair;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;

public final class zzbj {
    private final /* synthetic */ zzbf zzly;
    @VisibleForTesting
    private final String zzma;
    private final String zzmb;
    private final String zzmc;
    private final long zzmd;

    private zzbj(zzbf zzbf, String str, long j) {
        this.zzly = zzbf;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkArgument(j > 0);
        this.zzma = String.valueOf(str).concat(":start");
        this.zzmb = String.valueOf(str).concat(":count");
        this.zzmc = String.valueOf(str).concat(":value");
        this.zzmd = j;
    }

    @WorkerThread
    private final void zzea() {
        this.zzly.zzq();
        long currentTimeMillis = this.zzly.zzz().currentTimeMillis();
        Editor edit = this.zzly.zzdr().edit();
        edit.remove(this.zzmb);
        edit.remove(this.zzmc);
        edit.putLong(this.zzma, currentTimeMillis);
        edit.apply();
    }

    @WorkerThread
    public final void zzc(String str, long j) {
        this.zzly.zzq();
        if (zzec() == 0) {
            zzea();
        }
        if (str == null) {
            str = "";
        }
        long j2 = this.zzly.zzdr().getLong(this.zzmb, 0);
        if (j2 <= 0) {
            Editor edit = this.zzly.zzdr().edit();
            edit.putString(this.zzmc, str);
            edit.putLong(this.zzmb, 1);
            edit.apply();
            return;
        }
        long j3 = j2 + 1;
        boolean z = (this.zzly.zzab().zzgl().nextLong() & Long.MAX_VALUE) < Long.MAX_VALUE / j3;
        Editor edit2 = this.zzly.zzdr().edit();
        if (z) {
            edit2.putString(this.zzmc, str);
        }
        edit2.putLong(this.zzmb, j3);
        edit2.apply();
    }

    @WorkerThread
    public final Pair<String, Long> zzeb() {
        long j;
        this.zzly.zzq();
        this.zzly.zzq();
        long zzec = zzec();
        if (zzec == 0) {
            zzea();
            j = 0;
        } else {
            j = Math.abs(zzec - this.zzly.zzz().currentTimeMillis());
        }
        long j2 = this.zzmd;
        if (j < j2) {
            return null;
        }
        if (j > (j2 << 1)) {
            zzea();
            return null;
        }
        String string = this.zzly.zzdr().getString(this.zzmc, null);
        long j3 = this.zzly.zzdr().getLong(this.zzmb, 0);
        zzea();
        if (string == null || j3 <= 0) {
            return zzbf.zzky;
        }
        return new Pair<>(string, Long.valueOf(j3));
    }

    @WorkerThread
    private final long zzec() {
        return this.zzly.zzdr().getLong(this.zzma, 0);
    }
}
