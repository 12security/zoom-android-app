package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.internal.measurement.zzo;
import com.google.android.gms.internal.measurement.zzq;
import com.google.android.gms.internal.measurement.zzt;
import com.google.android.gms.internal.measurement.zzw;
import com.google.android.gms.internal.measurement.zzy;
import java.util.Map;

@DynamiteApi
public class AppMeasurementDynamiteService extends zzo {
    private Map<Integer, zzdb> zzad = new ArrayMap();
    @VisibleForTesting
    zzby zzl = null;

    class zza implements zzda {
        private zzt zzdm;

        zza(zzt zzt) {
            this.zzdm = zzt;
        }

        public final void interceptEvent(String str, String str2, Bundle bundle, long j) {
            try {
                this.zzdm.onEvent(str, str2, bundle, j);
            } catch (RemoteException e) {
                AppMeasurementDynamiteService.this.zzl.zzad().zzdd().zza("Event interceptor threw exception", e);
            }
        }
    }

    class zzb implements zzdb {
        private zzt zzdm;

        zzb(zzt zzt) {
            this.zzdm = zzt;
        }

        public final void onEvent(String str, String str2, Bundle bundle, long j) {
            try {
                this.zzdm.onEvent(str, str2, bundle, j);
            } catch (RemoteException e) {
                AppMeasurementDynamiteService.this.zzl.zzad().zzdd().zza("Event listener threw exception", e);
            }
        }
    }

    private final void zzah() {
        if (this.zzl == null) {
            throw new IllegalStateException("Attempting to perform action before initialize.");
        }
    }

    public void initialize(IObjectWrapper iObjectWrapper, zzy zzy, long j) throws RemoteException {
        Context context = (Context) ObjectWrapper.unwrap(iObjectWrapper);
        zzby zzby = this.zzl;
        if (zzby == null) {
            this.zzl = zzby.zza(context, zzy);
        } else {
            zzby.zzad().zzdd().zzaq("Attempting to initialize multiple times");
        }
    }

    public void logEvent(String str, String str2, Bundle bundle, boolean z, boolean z2, long j) throws RemoteException {
        zzah();
        this.zzl.zzs().logEvent(str, str2, bundle, z, z2, j);
    }

    public void setUserProperty(String str, String str2, IObjectWrapper iObjectWrapper, boolean z, long j) throws RemoteException {
        zzah();
        this.zzl.zzs().zza(str, str2, ObjectWrapper.unwrap(iObjectWrapper), z, j);
    }

    public void setUserId(String str, long j) throws RemoteException {
        zzah();
        this.zzl.zzs().zza(null, "_id", str, true, j);
    }

    public void setCurrentScreen(IObjectWrapper iObjectWrapper, String str, String str2, long j) throws RemoteException {
        zzah();
        this.zzl.zzv().setCurrentScreen((Activity) ObjectWrapper.unwrap(iObjectWrapper), str, str2);
    }

    public void setMeasurementEnabled(boolean z, long j) throws RemoteException {
        zzah();
        this.zzl.zzs().setMeasurementEnabled(z);
    }

    public void resetAnalyticsData(long j) throws RemoteException {
        zzah();
        this.zzl.zzs().resetAnalyticsData(j);
    }

    public void setMinimumSessionDuration(long j) throws RemoteException {
        zzah();
        this.zzl.zzs().setMinimumSessionDuration(j);
    }

    public void setSessionTimeoutDuration(long j) throws RemoteException {
        zzah();
        this.zzl.zzs().setSessionTimeoutDuration(j);
    }

    public void getMaxUserProperties(String str, zzq zzq) throws RemoteException {
        zzah();
        this.zzl.zzs();
        Preconditions.checkNotEmpty(str);
        this.zzl.zzab().zza(zzq, 25);
    }

    public void getCurrentScreenName(zzq zzq) throws RemoteException {
        zzah();
        zza(zzq, this.zzl.zzs().getCurrentScreenName());
    }

    public void getCurrentScreenClass(zzq zzq) throws RemoteException {
        zzah();
        zza(zzq, this.zzl.zzs().getCurrentScreenClass());
    }

    public void getCachedAppInstanceId(zzq zzq) throws RemoteException {
        zzah();
        zza(zzq, this.zzl.zzs().zzj());
    }

    public void getAppInstanceId(zzq zzq) throws RemoteException {
        zzah();
        this.zzl.zzac().zza((Runnable) new zzh(this, zzq));
    }

    public void getGmpAppId(zzq zzq) throws RemoteException {
        zzah();
        zza(zzq, this.zzl.zzs().getGmpAppId());
    }

    public void generateEventId(zzq zzq) throws RemoteException {
        zzah();
        this.zzl.zzab().zza(zzq, this.zzl.zzab().zzgk());
    }

    public void beginAdUnitExposure(String str, long j) throws RemoteException {
        zzah();
        this.zzl.zzr().beginAdUnitExposure(str, j);
    }

    public void endAdUnitExposure(String str, long j) throws RemoteException {
        zzah();
        this.zzl.zzr().endAdUnitExposure(str, j);
    }

    public void initForTests(Map map) throws RemoteException {
        zzah();
    }

    public void logEventAndBundle(String str, String str2, Bundle bundle, zzq zzq, long j) throws RemoteException {
        zzah();
        Preconditions.checkNotEmpty(str2);
        String str3 = "app";
        (bundle != null ? new Bundle(bundle) : new Bundle()).putString("_o", str3);
        zzaj zzaj = new zzaj(str2, new zzag(bundle), str3, j);
        this.zzl.zzac().zza((Runnable) new zzi(this, zzq, zzaj, str));
    }

    public void onActivityStarted(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        zzah();
        zzdx zzdx = this.zzl.zzs().zzpf;
        if (zzdx != null) {
            this.zzl.zzs().zzeu();
            zzdx.onActivityStarted((Activity) ObjectWrapper.unwrap(iObjectWrapper));
        }
    }

    public void onActivityStopped(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        zzah();
        zzdx zzdx = this.zzl.zzs().zzpf;
        if (zzdx != null) {
            this.zzl.zzs().zzeu();
            zzdx.onActivityStopped((Activity) ObjectWrapper.unwrap(iObjectWrapper));
        }
    }

    public void onActivityCreated(IObjectWrapper iObjectWrapper, Bundle bundle, long j) throws RemoteException {
        zzah();
        zzdx zzdx = this.zzl.zzs().zzpf;
        this.zzl.zzad().zzdd().zzaq("Got on activity created");
        if (zzdx != null) {
            this.zzl.zzs().zzeu();
            zzdx.onActivityCreated((Activity) ObjectWrapper.unwrap(iObjectWrapper), bundle);
        }
    }

    public void onActivityDestroyed(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        zzah();
        zzdx zzdx = this.zzl.zzs().zzpf;
        if (zzdx != null) {
            this.zzl.zzs().zzeu();
            zzdx.onActivityDestroyed((Activity) ObjectWrapper.unwrap(iObjectWrapper));
        }
    }

    public void onActivityPaused(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        zzah();
        zzdx zzdx = this.zzl.zzs().zzpf;
        if (zzdx != null) {
            this.zzl.zzs().zzeu();
            zzdx.onActivityPaused((Activity) ObjectWrapper.unwrap(iObjectWrapper));
        }
    }

    public void onActivityResumed(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        zzah();
        zzdx zzdx = this.zzl.zzs().zzpf;
        if (zzdx != null) {
            this.zzl.zzs().zzeu();
            zzdx.onActivityResumed((Activity) ObjectWrapper.unwrap(iObjectWrapper));
        }
    }

    public void onActivitySaveInstanceState(IObjectWrapper iObjectWrapper, zzq zzq, long j) throws RemoteException {
        zzah();
        zzdx zzdx = this.zzl.zzs().zzpf;
        Bundle bundle = new Bundle();
        if (zzdx != null) {
            this.zzl.zzs().zzeu();
            zzdx.onActivitySaveInstanceState((Activity) ObjectWrapper.unwrap(iObjectWrapper), bundle);
        }
        try {
            zzq.zzb(bundle);
        } catch (RemoteException e) {
            this.zzl.zzad().zzdd().zza("Error returning bundle value to wrapper", e);
        }
    }

    public void performAction(Bundle bundle, zzq zzq, long j) throws RemoteException {
        zzah();
        zzq.zzb(null);
    }

    public void getUserProperties(String str, String str2, boolean z, zzq zzq) throws RemoteException {
        zzah();
        zzbt zzac = this.zzl.zzac();
        zzj zzj = new zzj(this, zzq, str, str2, z);
        zzac.zza((Runnable) zzj);
    }

    public void logHealthData(int i, String str, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, IObjectWrapper iObjectWrapper3) throws RemoteException {
        Object obj;
        Object obj2;
        zzah();
        Object obj3 = null;
        if (iObjectWrapper == null) {
            obj = null;
        } else {
            obj = ObjectWrapper.unwrap(iObjectWrapper);
        }
        if (iObjectWrapper2 == null) {
            obj2 = null;
        } else {
            obj2 = ObjectWrapper.unwrap(iObjectWrapper2);
        }
        if (iObjectWrapper3 != null) {
            obj3 = ObjectWrapper.unwrap(iObjectWrapper3);
        }
        this.zzl.zzad().zza(i, true, false, str, obj, obj2, obj3);
    }

    public void setEventInterceptor(zzt zzt) throws RemoteException {
        zzah();
        zzdd zzs = this.zzl.zzs();
        zza zza2 = new zza(zzt);
        zzs.zzo();
        zzs.zzah();
        zzs.zzac().zza((Runnable) new zzdk(zzs, zza2));
    }

    public void registerOnMeasurementEventListener(zzt zzt) throws RemoteException {
        zzah();
        zzdb zzdb = (zzdb) this.zzad.get(Integer.valueOf(zzt.mo23964id()));
        if (zzdb == null) {
            zzdb = new zzb(zzt);
            this.zzad.put(Integer.valueOf(zzt.mo23964id()), zzdb);
        }
        this.zzl.zzs().zza(zzdb);
    }

    public void unregisterOnMeasurementEventListener(zzt zzt) throws RemoteException {
        zzah();
        zzdb zzdb = (zzdb) this.zzad.remove(Integer.valueOf(zzt.mo23964id()));
        if (zzdb == null) {
            zzdb = new zzb(zzt);
        }
        this.zzl.zzs().zzb(zzdb);
    }

    public void setInstanceIdProvider(zzw zzw) throws RemoteException {
        zzah();
    }

    public void setConditionalUserProperty(Bundle bundle, long j) throws RemoteException {
        zzah();
        if (bundle == null) {
            this.zzl.zzad().zzda().zzaq("Conditional user property must not be null");
        } else {
            this.zzl.zzs().setConditionalUserProperty(bundle, j);
        }
    }

    public void clearConditionalUserProperty(String str, String str2, Bundle bundle) throws RemoteException {
        zzah();
        this.zzl.zzs().clearConditionalUserProperty(str, str2, bundle);
    }

    public void getConditionalUserProperties(String str, String str2, zzq zzq) throws RemoteException {
        zzah();
        this.zzl.zzac().zza((Runnable) new zzk(this, zzq, str, str2));
    }

    public void getTestFlag(zzq zzq, int i) throws RemoteException {
        zzah();
        switch (i) {
            case 0:
                this.zzl.zzab().zzb(zzq, this.zzl.zzs().zzew());
                return;
            case 1:
                this.zzl.zzab().zza(zzq, this.zzl.zzs().zzex().longValue());
                return;
            case 2:
                zzgd zzab = this.zzl.zzab();
                double doubleValue = this.zzl.zzs().zzez().doubleValue();
                Bundle bundle = new Bundle();
                bundle.putDouble("r", doubleValue);
                try {
                    zzq.zzb(bundle);
                    return;
                } catch (RemoteException e) {
                    zzab.zzl.zzad().zzdd().zza("Error returning double value to wrapper", e);
                    return;
                }
            case 3:
                this.zzl.zzab().zza(zzq, this.zzl.zzs().zzey().intValue());
                return;
            case 4:
                this.zzl.zzab().zza(zzq, this.zzl.zzs().zzev().booleanValue());
                break;
        }
    }

    private final void zza(zzq zzq, String str) {
        this.zzl.zzab().zzb(zzq, str);
    }

    public void setDataCollectionEnabled(boolean z) throws RemoteException {
        zzah();
        this.zzl.zzs().zza(z);
    }

    public void isDataCollectionEnabled(zzq zzq) throws RemoteException {
        zzah();
        this.zzl.zzac().zza((Runnable) new zzl(this, zzq));
    }
}
