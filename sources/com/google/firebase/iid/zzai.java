package com.google.firebase.iid;

import android.os.Bundle;

final class zzai extends zzal<Void> {
    zzai(int i, int i2, Bundle bundle) {
        super(i, 2, bundle);
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzab() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(Bundle bundle) {
        if (bundle.getBoolean("ack", false)) {
            finish(null);
        } else {
            zza(new zzak(4, "Invalid response to one way request"));
        }
    }
}