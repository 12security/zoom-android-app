package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.PackageManagerWrapper;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zze;
import java.util.List;

public final class zzbl {
    final zzby zzl;

    zzbl(zzby zzby) {
        this.zzl = zzby;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzaw(String str) {
        if (str == null || str.isEmpty()) {
            this.zzl.zzad().zzdg().zzaq("Install Referrer Reporter was called with invalid app package name");
            return;
        }
        this.zzl.zzac().zzq();
        if (!zzee()) {
            this.zzl.zzad().zzdg().zzaq("Install Referrer Reporter is not available");
            return;
        }
        this.zzl.zzad().zzdg().zzaq("Install Referrer Reporter is initializing");
        zzbm zzbm = new zzbm(this, str);
        this.zzl.zzac().zzq();
        Intent intent = new Intent("com.google.android.finsky.BIND_GET_INSTALL_REFERRER_SERVICE");
        intent.setComponent(new ComponentName("com.android.vending", "com.google.android.finsky.externalreferrer.GetInstallReferrerService"));
        PackageManager packageManager = this.zzl.getContext().getPackageManager();
        if (packageManager == null) {
            this.zzl.zzad().zzdd().zzaq("Failed to obtain Package Manager to verify binding conditions");
            return;
        }
        List queryIntentServices = packageManager.queryIntentServices(intent, 0);
        if (queryIntentServices == null || queryIntentServices.isEmpty()) {
            this.zzl.zzad().zzdg().zzaq("Play Service for fetching Install Referrer is unavailable on device");
            return;
        }
        ResolveInfo resolveInfo = (ResolveInfo) queryIntentServices.get(0);
        if (resolveInfo.serviceInfo != null) {
            String str2 = resolveInfo.serviceInfo.packageName;
            if (resolveInfo.serviceInfo.name == null || !"com.android.vending".equals(str2) || !zzee()) {
                this.zzl.zzad().zzdg().zzaq("Play Store missing or incompatible. Version 8.3.73 or later required");
            } else {
                try {
                    this.zzl.zzad().zzdg().zza("Install Referrer Service is", ConnectionTracker.getInstance().bindService(this.zzl.getContext(), new Intent(intent), zzbm, 1) ? "available" : "not available");
                } catch (Exception e) {
                    this.zzl.zzad().zzda().zza("Exception occurred while binding to Install Referrer Service", e.getMessage());
                }
            }
        }
    }

    @VisibleForTesting
    private final boolean zzee() {
        boolean z = false;
        try {
            PackageManagerWrapper packageManager = Wrappers.packageManager(this.zzl.getContext());
            if (packageManager == null) {
                this.zzl.zzad().zzdg().zzaq("Failed to retrieve Package Manager to check Play Store compatibility");
                return false;
            }
            if (packageManager.getPackageInfo("com.android.vending", 128).versionCode >= 80837300) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            this.zzl.zzad().zzdg().zza("Failed to retrieve Play Store version", e);
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    @Nullable
    @VisibleForTesting
    public final Bundle zza(String str, zze zze) {
        this.zzl.zzac().zzq();
        if (zze == null) {
            this.zzl.zzad().zzdd().zzaq("Attempting to use Install Referrer Service while it is not initialized");
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("package_name", str);
        try {
            Bundle zza = zze.zza(bundle);
            if (zza != null) {
                return zza;
            }
            this.zzl.zzad().zzda().zzaq("Install Referrer Service returned a null response");
            return null;
        } catch (Exception e) {
            this.zzl.zzad().zzda().zza("Exception occurred while retrieving the Install Referrer", e.getMessage());
            return null;
        }
    }
}
