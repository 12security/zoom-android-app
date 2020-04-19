package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.MainThread;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzy;

public final class zzbo {
    private final zzbr zzmj;

    public zzbo(zzbr zzbr) {
        Preconditions.checkNotNull(zzbr);
        this.zzmj = zzbr;
    }

    public static boolean zzl(Context context) {
        Preconditions.checkNotNull(context);
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            ActivityInfo receiverInfo = packageManager.getReceiverInfo(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementReceiver"), 0);
            if (receiverInfo != null && receiverInfo.enabled) {
                return true;
            }
            return false;
        } catch (NameNotFoundException unused) {
        }
    }

    @MainThread
    public final void onReceive(Context context, Intent intent) {
        zzby zza = zzby.zza(context, (zzy) null);
        zzau zzad = zza.zzad();
        if (intent == null) {
            zzad.zzdd().zzaq("Receiver called with null intent");
            return;
        }
        zza.zzag();
        String action = intent.getAction();
        zzad.zzdi().zza("Local receiver got", action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            Intent className = new Intent().setClassName(context, "com.google.android.gms.measurement.AppMeasurementService");
            className.setAction("com.google.android.gms.measurement.UPLOAD");
            zzad.zzdi().zzaq("Starting wakeful intent.");
            this.zzmj.doStartService(context, className);
            return;
        }
        if ("com.android.vending.INSTALL_REFERRER".equals(action)) {
            try {
                zza.zzac().zza((Runnable) new zzbp(this, zza, zzad));
            } catch (Exception e) {
                zzad.zzdd().zza("Install Referrer Reporter encountered a problem", e);
            }
            PendingResult doGoAsync = this.zzmj.doGoAsync();
            String stringExtra = intent.getStringExtra("referrer");
            if (stringExtra == null) {
                zzad.zzdi().zzaq("Install referrer extras are null");
                if (doGoAsync != null) {
                    doGoAsync.finish();
                }
                return;
            }
            zzad.zzdg().zza("Install referrer extras are", stringExtra);
            if (!stringExtra.contains("?")) {
                String str = "?";
                String valueOf = String.valueOf(stringExtra);
                stringExtra = valueOf.length() != 0 ? str.concat(valueOf) : new String(str);
            }
            Bundle zza2 = zza.zzab().zza(Uri.parse(stringExtra));
            if (zza2 == null) {
                zzad.zzdi().zzaq("No campaign defined in install referrer broadcast");
                if (doGoAsync != null) {
                    doGoAsync.finish();
                }
            } else {
                long longExtra = intent.getLongExtra("referrer_timestamp_seconds", 0) * 1000;
                if (longExtra == 0) {
                    zzad.zzdd().zzaq("Install referrer is missing timestamp");
                }
                zzbt zzac = zza.zzac();
                zzbq zzbq = new zzbq(this, zza, longExtra, zza2, context, zzad, doGoAsync);
                zzac.zza((Runnable) zzbq);
            }
        }
    }
}
