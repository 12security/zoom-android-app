package com.google.android.gms.measurement.internal;

abstract class zzf extends zze {
    private boolean zzce;

    zzf(zzby zzby) {
        super(zzby);
        this.zzl.zzb(this);
    }

    /* access modifiers changed from: protected */
    public abstract boolean zzak();

    /* access modifiers changed from: protected */
    public void zzal() {
    }

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
        if (this.zzce) {
            throw new IllegalStateException("Can't initialize twice");
        } else if (!zzak()) {
            this.zzl.zzes();
            this.zzce = true;
        }
    }

    public final void zzaj() {
        if (!this.zzce) {
            zzal();
            this.zzl.zzes();
            this.zzce = true;
            return;
        }
        throw new IllegalStateException("Can't initialize twice");
    }
}
