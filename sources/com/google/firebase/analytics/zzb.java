package com.google.firebase.analytics;

import android.os.Bundle;
import com.google.android.gms.internal.measurement.zzaa;
import com.google.android.gms.measurement.internal.zzda;
import com.google.android.gms.measurement.internal.zzdb;
import com.google.android.gms.measurement.internal.zzdy;
import java.util.List;
import java.util.Map;

final class zzb implements zzdy {
    private final /* synthetic */ zzaa zzaas;

    zzb(zzaa zzaa) {
        this.zzaas = zzaa;
    }

    public final void logEventInternal(String str, String str2, Bundle bundle) {
        this.zzaas.logEventInternal(str, str2, bundle);
    }

    public final void logEventInternalNoInterceptor(String str, String str2, Bundle bundle, long j) {
        this.zzaas.logEventInternalNoInterceptor(str, str2, bundle, j);
    }

    public final void setUserPropertyInternal(String str, String str2, Object obj) {
        this.zzaas.setUserPropertyInternal(str, str2, obj);
    }

    public final void setMeasurementEnabled(boolean z) {
        this.zzaas.setMeasurementEnabled(z);
    }

    public final void setDataCollectionEnabled(boolean z) {
        this.zzaas.setDataCollectionEnabled(z);
    }

    public final Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        return this.zzaas.getUserProperties(str, str2, z);
    }

    public final void zza(zzda zzda) {
        this.zzaas.zza(zzda);
    }

    public final void zza(zzdb zzdb) {
        this.zzaas.zza(zzdb);
    }

    public final void zzb(zzdb zzdb) {
        this.zzaas.zzb(zzdb);
    }

    public final String getCurrentScreenName() {
        return this.zzaas.getCurrentScreenName();
    }

    public final String getCurrentScreenClass() {
        return this.zzaas.getCurrentScreenClass();
    }

    public final String zzj() {
        return this.zzaas.zzj();
    }

    public final String getGmpAppId() {
        return this.zzaas.getGmpAppId();
    }

    public final long generateEventId() {
        return this.zzaas.generateEventId();
    }

    public final void beginAdUnitExposure(String str) {
        this.zzaas.beginAdUnitExposure(str);
    }

    public final void endAdUnitExposure(String str) {
        this.zzaas.endAdUnitExposure(str);
    }

    public final void setConditionalUserProperty(Bundle bundle) {
        this.zzaas.setConditionalUserProperty(bundle);
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        this.zzaas.clearConditionalUserProperty(str, str2, bundle);
    }

    public final List<Bundle> getConditionalUserProperties(String str, String str2) {
        return this.zzaas.getConditionalUserProperties(str, str2);
    }

    public final int getMaxUserProperties(String str) {
        return this.zzaas.getMaxUserProperties(str);
    }

    public final Object zzb(int i) {
        return this.zzaas.zzb(i);
    }
}
