package com.google.android.gms.measurement.internal;

import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;
import java.util.List;
import java.util.Map;

@WorkerThread
final class zzbb implements Runnable {
    private final String packageName;
    private final int status;
    private final zzba zzkk;
    private final Throwable zzkl;
    private final byte[] zzkm;
    private final Map<String, List<String>> zzkn;

    private zzbb(String str, zzba zzba, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        Preconditions.checkNotNull(zzba);
        this.zzkk = zzba;
        this.status = i;
        this.zzkl = th;
        this.zzkm = bArr;
        this.packageName = str;
        this.zzkn = map;
    }

    public final void run() {
        this.zzkk.zza(this.packageName, this.status, this.zzkl, this.zzkm, this.zzkn);
    }
}
