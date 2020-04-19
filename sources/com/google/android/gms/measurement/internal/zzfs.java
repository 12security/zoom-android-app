package com.google.android.gms.measurement.internal;

abstract class zzfs extends zzfr {
    private boolean zzce;

    zzfs(zzft zzft) {
        super(zzft);
        this.zzkt.zzb(this);
    }

    /* access modifiers changed from: protected */
    public abstract boolean zzak();

    /* access modifiers changed from: 0000 */
    public final boolean isInitialized() {
        return this.zzce;
    }

    /* access modifiers changed from: protected */
    public final void zzah() {
        if (!isInitialized()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    public final void zzai() {
        if (!this.zzce) {
            zzak();
            this.zzkt.zzgh();
            this.zzce = true;
            return;
        }
        throw new IllegalStateException("Can't initialize twice");
    }
}
