package com.google.android.gms.measurement.internal;

import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.internal.measurement.zze;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

final class zzbn implements Runnable {
    private final /* synthetic */ zze zzmg;
    private final /* synthetic */ ServiceConnection zzmh;
    private final /* synthetic */ zzbm zzmi;

    zzbn(zzbm zzbm, zze zze, ServiceConnection serviceConnection) {
        this.zzmi = zzbm;
        this.zzmg = zze;
        this.zzmh = serviceConnection;
    }

    public final void run() {
        zzbl zzbl = this.zzmi.zzmf;
        String zza = this.zzmi.packageName;
        zze zze = this.zzmg;
        ServiceConnection serviceConnection = this.zzmh;
        Bundle zza2 = zzbl.zza(zza, zze);
        zzbl.zzl.zzac().zzq();
        if (zza2 != null) {
            long j = zza2.getLong("install_begin_timestamp_seconds", 0) * 1000;
            if (j == 0) {
                zzbl.zzl.zzad().zzda().zzaq("Service response is missing Install Referrer install timestamp");
            } else {
                String string = zza2.getString("install_referrer");
                if (string == null || string.isEmpty()) {
                    zzbl.zzl.zzad().zzda().zzaq("No referrer defined in install referrer response");
                } else {
                    zzbl.zzl.zzad().zzdi().zza("InstallReferrer API result", string);
                    zzgd zzab = zzbl.zzl.zzab();
                    String str = "?";
                    String valueOf = String.valueOf(string);
                    Bundle zza3 = zzab.zza(Uri.parse(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)));
                    if (zza3 == null) {
                        zzbl.zzl.zzad().zzda().zzaq("No campaign params defined in install referrer result");
                    } else {
                        String string2 = zza3.getString(Param.MEDIUM);
                        if (string2 != null && !"(not set)".equalsIgnoreCase(string2) && !"organic".equalsIgnoreCase(string2)) {
                            long j2 = zza2.getLong("referrer_click_timestamp_seconds", 0) * 1000;
                            if (j2 == 0) {
                                zzbl.zzl.zzad().zzda().zzaq("Install Referrer is missing click timestamp for ad campaign");
                            } else {
                                zza3.putLong("click_timestamp", j2);
                            }
                        }
                        if (j == zzbl.zzl.zzae().zzlh.get()) {
                            zzbl.zzl.zzag();
                            zzbl.zzl.zzad().zzdi().zzaq("Campaign has already been logged");
                        } else {
                            zzbl.zzl.zzae().zzlh.set(j);
                            zzbl.zzl.zzag();
                            zzbl.zzl.zzad().zzdi().zza("Logging Install Referrer campaign from sdk with ", "referrer API");
                            zza3.putString("_cis", "referrer API");
                            zzbl.zzl.zzs().logEvent("auto", "_cmp", zza3);
                        }
                    }
                }
            }
        }
        if (serviceConnection != null) {
            ConnectionTracker.getInstance().unbindService(zzbl.zzl.getContext(), serviceConnection);
        }
    }
}
