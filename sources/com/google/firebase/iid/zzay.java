package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.google.android.gms.common.util.VisibleForTesting;
import javax.annotation.Nullable;

@VisibleForTesting
final class zzay extends BroadcastReceiver {
    @Nullable
    private zzaz zzdp;

    public zzay(zzaz zzaz) {
        this.zzdp = zzaz;
    }

    public final void zzam() {
        if (FirebaseInstanceId.zzm()) {
            Log.d("FirebaseInstanceId", "Connectivity change received registered");
        }
        this.zzdp.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public final void onReceive(Context context, Intent intent) {
        zzaz zzaz = this.zzdp;
        if (zzaz != null && zzaz.zzao()) {
            if (FirebaseInstanceId.zzm()) {
                Log.d("FirebaseInstanceId", "Connectivity changed. Starting background sync.");
            }
            FirebaseInstanceId.zza((Runnable) this.zzdp, 0);
            this.zzdp.getContext().unregisterReceiver(this);
            this.zzdp = null;
        }
    }
}
