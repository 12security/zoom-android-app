package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;

final class zzfo {
    private long startTime;
    private final Clock zzaa;

    public zzfo(Clock clock) {
        Preconditions.checkNotNull(clock);
        this.zzaa = clock;
    }

    public final void start() {
        this.startTime = this.zzaa.elapsedRealtime();
    }

    public final void clear() {
        this.startTime = 0;
    }

    public final boolean zzae(long j) {
        if (this.startTime != 0 && this.zzaa.elapsedRealtime() - this.startTime < 3600000) {
            return false;
        }
        return true;
    }
}
