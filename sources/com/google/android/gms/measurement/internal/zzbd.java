package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;

class zzbd extends BroadcastReceiver {
    @VisibleForTesting
    private static final String zzks = "com.google.android.gms.measurement.internal.zzbd";
    /* access modifiers changed from: private */
    public final zzft zzkt;
    private boolean zzku;
    private boolean zzkv;

    zzbd(zzft zzft) {
        Preconditions.checkNotNull(zzft);
        this.zzkt = zzft;
    }

    @MainThread
    public void onReceive(Context context, Intent intent) {
        this.zzkt.zzfy();
        String action = intent.getAction();
        this.zzkt.zzad().zzdi().zza("NetworkBroadcastReceiver received action", action);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            boolean zzdl = this.zzkt.zzfu().zzdl();
            if (this.zzkv != zzdl) {
                this.zzkv = zzdl;
                this.zzkt.zzac().zza((Runnable) new zzbe(this, zzdl));
            }
            return;
        }
        this.zzkt.zzad().zzdd().zza("NetworkBroadcastReceiver received unknown action", action);
    }

    @WorkerThread
    public final void zzdq() {
        this.zzkt.zzfy();
        this.zzkt.zzac().zzq();
        if (!this.zzku) {
            this.zzkt.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.zzkv = this.zzkt.zzfu().zzdl();
            this.zzkt.zzad().zzdi().zza("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.zzkv));
            this.zzku = true;
        }
    }

    @WorkerThread
    public final void unregister() {
        this.zzkt.zzfy();
        this.zzkt.zzac().zzq();
        this.zzkt.zzac().zzq();
        if (this.zzku) {
            this.zzkt.zzad().zzdi().zzaq("Unregistering connectivity change receiver");
            this.zzku = false;
            this.zzkv = false;
            try {
                this.zzkt.getContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                this.zzkt.zzad().zzda().zza("Failed to unregister the network broadcast receiver", e);
            }
        }
    }
}
