package com.google.android.gms.measurement;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.Size;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.internal.zzby;
import com.google.android.gms.measurement.internal.zzcw;
import com.google.android.gms.measurement.internal.zzcx;
import com.google.android.gms.measurement.internal.zzcy;
import com.google.android.gms.measurement.internal.zzcz;
import com.google.android.gms.measurement.internal.zzda;
import com.google.android.gms.measurement.internal.zzdb;
import com.google.android.gms.measurement.internal.zzdy;
import com.google.android.gms.measurement.internal.zzeb;
import com.google.android.gms.measurement.internal.zzga;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ShowFirstParty
@Deprecated
public class AppMeasurement {
    @ShowFirstParty
    @KeepForSdk
    public static final String CRASH_ORIGIN = "crash";
    @ShowFirstParty
    @KeepForSdk
    public static final String FCM_ORIGIN = "fcm";
    @ShowFirstParty
    @KeepForSdk
    public static final String FIAM_ORIGIN = "fiam";
    private static volatile AppMeasurement zzk;
    private final zzby zzl;
    private final zzdy zzm;
    private final boolean zzn;

    @ShowFirstParty
    @KeepForSdk
    public static class ConditionalUserProperty {
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public boolean mActive;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public String mAppId;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public long mCreationTimestamp;
        @Keep
        public String mExpiredEventName;
        @Keep
        public Bundle mExpiredEventParams;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public String mName;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public String mOrigin;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public long mTimeToLive;
        @Keep
        public String mTimedOutEventName;
        @Keep
        public Bundle mTimedOutEventParams;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public String mTriggerEventName;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public long mTriggerTimeout;
        @Keep
        public String mTriggeredEventName;
        @Keep
        public Bundle mTriggeredEventParams;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public long mTriggeredTimestamp;
        @ShowFirstParty
        @KeepForSdk
        @Keep
        public Object mValue;

        @KeepForSdk
        public ConditionalUserProperty() {
        }

        @KeepForSdk
        public ConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
            Preconditions.checkNotNull(conditionalUserProperty);
            this.mAppId = conditionalUserProperty.mAppId;
            this.mOrigin = conditionalUserProperty.mOrigin;
            this.mCreationTimestamp = conditionalUserProperty.mCreationTimestamp;
            this.mName = conditionalUserProperty.mName;
            Object obj = conditionalUserProperty.mValue;
            if (obj != null) {
                this.mValue = zzeb.zza(obj);
                if (this.mValue == null) {
                    this.mValue = conditionalUserProperty.mValue;
                }
            }
            this.mActive = conditionalUserProperty.mActive;
            this.mTriggerEventName = conditionalUserProperty.mTriggerEventName;
            this.mTriggerTimeout = conditionalUserProperty.mTriggerTimeout;
            this.mTimedOutEventName = conditionalUserProperty.mTimedOutEventName;
            Bundle bundle = conditionalUserProperty.mTimedOutEventParams;
            if (bundle != null) {
                this.mTimedOutEventParams = new Bundle(bundle);
            }
            this.mTriggeredEventName = conditionalUserProperty.mTriggeredEventName;
            Bundle bundle2 = conditionalUserProperty.mTriggeredEventParams;
            if (bundle2 != null) {
                this.mTriggeredEventParams = new Bundle(bundle2);
            }
            this.mTriggeredTimestamp = conditionalUserProperty.mTriggeredTimestamp;
            this.mTimeToLive = conditionalUserProperty.mTimeToLive;
            this.mExpiredEventName = conditionalUserProperty.mExpiredEventName;
            Bundle bundle3 = conditionalUserProperty.mExpiredEventParams;
            if (bundle3 != null) {
                this.mExpiredEventParams = new Bundle(bundle3);
            }
        }

        private ConditionalUserProperty(@NonNull Bundle bundle) {
            Preconditions.checkNotNull(bundle);
            this.mAppId = (String) zzcw.zza(bundle, "app_id", String.class, null);
            this.mOrigin = (String) zzcw.zza(bundle, "origin", String.class, null);
            this.mName = (String) zzcw.zza(bundle, "name", String.class, null);
            this.mValue = zzcw.zza(bundle, "value", Object.class, null);
            this.mTriggerEventName = (String) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, String.class, null);
            this.mTriggerTimeout = ((Long) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, Long.class, Long.valueOf(0))).longValue();
            this.mTimedOutEventName = (String) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_NAME, String.class, null);
            this.mTimedOutEventParams = (Bundle) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS, Bundle.class, null);
            this.mTriggeredEventName = (String) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_NAME, String.class, null);
            this.mTriggeredEventParams = (Bundle) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_PARAMS, Bundle.class, null);
            this.mTimeToLive = ((Long) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, Long.class, Long.valueOf(0))).longValue();
            this.mExpiredEventName = (String) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME, String.class, null);
            this.mExpiredEventParams = (Bundle) zzcw.zza(bundle, com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS, Bundle.class, null);
        }

        /* access modifiers changed from: private */
        public final Bundle zzf() {
            Bundle bundle = new Bundle();
            String str = this.mAppId;
            if (str != null) {
                bundle.putString("app_id", str);
            }
            String str2 = this.mOrigin;
            if (str2 != null) {
                bundle.putString("origin", str2);
            }
            String str3 = this.mName;
            if (str3 != null) {
                bundle.putString("name", str3);
            }
            Object obj = this.mValue;
            if (obj != null) {
                zzcw.zza(bundle, obj);
            }
            String str4 = this.mTriggerEventName;
            if (str4 != null) {
                bundle.putString(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGER_EVENT_NAME, str4);
            }
            bundle.putLong(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGER_TIMEOUT, this.mTriggerTimeout);
            String str5 = this.mTimedOutEventName;
            if (str5 != null) {
                bundle.putString(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_NAME, str5);
            }
            Bundle bundle2 = this.mTimedOutEventParams;
            if (bundle2 != null) {
                bundle.putBundle(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TIMED_OUT_EVENT_PARAMS, bundle2);
            }
            String str6 = this.mTriggeredEventName;
            if (str6 != null) {
                bundle.putString(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_NAME, str6);
            }
            Bundle bundle3 = this.mTriggeredEventParams;
            if (bundle3 != null) {
                bundle.putBundle(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_EVENT_PARAMS, bundle3);
            }
            bundle.putLong(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TIME_TO_LIVE, this.mTimeToLive);
            String str7 = this.mExpiredEventName;
            if (str7 != null) {
                bundle.putString(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_NAME, str7);
            }
            Bundle bundle4 = this.mExpiredEventParams;
            if (bundle4 != null) {
                bundle.putBundle(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.EXPIRED_EVENT_PARAMS, bundle4);
            }
            bundle.putLong(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.CREATION_TIMESTAMP, this.mCreationTimestamp);
            bundle.putBoolean(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.ACTIVE, this.mActive);
            bundle.putLong(com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty.TRIGGERED_TIMESTAMP, this.mTriggeredTimestamp);
            return bundle;
        }
    }

    @ShowFirstParty
    @KeepForSdk
    public static final class Event extends zzcx {
        @ShowFirstParty
        @KeepForSdk
        public static final String AD_REWARD = "_ar";
        @ShowFirstParty
        @KeepForSdk
        public static final String APP_EXCEPTION = "_ae";

        private Event() {
        }
    }

    @ShowFirstParty
    @KeepForSdk
    public interface EventInterceptor extends zzda {
        @WorkerThread
        @ShowFirstParty
        @KeepForSdk
        void interceptEvent(String str, String str2, Bundle bundle, long j);
    }

    @ShowFirstParty
    @KeepForSdk
    public interface OnEventListener extends zzdb {
        @WorkerThread
        @ShowFirstParty
        @KeepForSdk
        void onEvent(String str, String str2, Bundle bundle, long j);
    }

    @ShowFirstParty
    @KeepForSdk
    public static final class Param extends zzcy {
        @ShowFirstParty
        @KeepForSdk
        public static final String FATAL = "fatal";
        @ShowFirstParty
        @KeepForSdk
        public static final String TIMESTAMP = "timestamp";
        @ShowFirstParty
        @KeepForSdk
        public static final String TYPE = "type";

        private Param() {
        }
    }

    @ShowFirstParty
    @KeepForSdk
    public static final class UserProperty extends zzcz {
        @ShowFirstParty
        @KeepForSdk
        public static final String FIREBASE_LAST_NOTIFICATION = "_ln";

        private UserProperty() {
        }
    }

    @ShowFirstParty
    @Keep
    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WAKE_LOCK"})
    @Deprecated
    public static AppMeasurement getInstance(Context context) {
        return zza(context, null, null);
    }

    @VisibleForTesting
    private static AppMeasurement zza(Context context, String str, String str2) {
        if (zzk == null) {
            synchronized (AppMeasurement.class) {
                if (zzk == null) {
                    zzdy zzb = zzb(context, null);
                    if (zzb != null) {
                        zzk = new AppMeasurement(zzb);
                    } else {
                        zzk = new AppMeasurement(zzby.zza(context, null, null, null));
                    }
                }
            }
        }
        return zzk;
    }

    public static AppMeasurement zza(Context context, Bundle bundle) {
        if (zzk == null) {
            synchronized (AppMeasurement.class) {
                if (zzk == null) {
                    zzdy zzb = zzb(context, bundle);
                    if (zzb != null) {
                        zzk = new AppMeasurement(zzb);
                    } else {
                        zzk = new AppMeasurement(zzby.zza(context, null, null, bundle));
                    }
                }
            }
        }
        return zzk;
    }

    private static zzdy zzb(Context context, Bundle bundle) {
        try {
            try {
                return (zzdy) Class.forName("com.google.firebase.analytics.FirebaseAnalytics").getDeclaredMethod("getScionFrontendApiImplementation", new Class[]{Context.class, Bundle.class}).invoke(null, new Object[]{context, bundle});
            } catch (Exception unused) {
                return null;
            }
        } catch (ClassNotFoundException unused2) {
            return null;
        }
    }

    @KeepForSdk
    @Deprecated
    public void setMeasurementEnabled(boolean z) {
        if (this.zzn) {
            this.zzm.setMeasurementEnabled(z);
        } else {
            this.zzl.zzs().setMeasurementEnabled(z);
        }
    }

    public final void zza(boolean z) {
        if (this.zzn) {
            this.zzm.setDataCollectionEnabled(z);
        } else {
            this.zzl.zzs().zza(z);
        }
    }

    private AppMeasurement(zzby zzby) {
        Preconditions.checkNotNull(zzby);
        this.zzl = zzby;
        this.zzm = null;
        this.zzn = false;
    }

    private AppMeasurement(zzdy zzdy) {
        Preconditions.checkNotNull(zzdy);
        this.zzm = zzdy;
        this.zzl = null;
        this.zzn = true;
    }

    @ShowFirstParty
    @Keep
    public void logEventInternal(String str, String str2, Bundle bundle) {
        if (this.zzn) {
            this.zzm.logEventInternal(str, str2, bundle);
        } else {
            this.zzl.zzs().logEvent(str, str2, bundle);
        }
    }

    @ShowFirstParty
    @KeepForSdk
    public void logEventInternalNoInterceptor(String str, String str2, Bundle bundle, long j) {
        if (this.zzn) {
            this.zzm.logEventInternalNoInterceptor(str, str2, bundle, j);
        } else {
            this.zzl.zzs().logEvent(str, str2, bundle, true, false, j);
        }
    }

    @ShowFirstParty
    @KeepForSdk
    public void setUserPropertyInternal(String str, String str2, Object obj) {
        Preconditions.checkNotEmpty(str);
        if (this.zzn) {
            this.zzm.setUserPropertyInternal(str, str2, obj);
        } else {
            this.zzl.zzs().zzb(str, str2, obj, true);
        }
    }

    @WorkerThread
    @ShowFirstParty
    @KeepForSdk
    public Map<String, Object> getUserProperties(boolean z) {
        if (this.zzn) {
            return this.zzm.getUserProperties(null, null, z);
        }
        List<zzga> zzh = this.zzl.zzs().zzh(z);
        ArrayMap arrayMap = new ArrayMap(zzh.size());
        for (zzga zzga : zzh) {
            arrayMap.put(zzga.name, zzga.getValue());
        }
        return arrayMap;
    }

    @WorkerThread
    @ShowFirstParty
    @KeepForSdk
    public void setEventInterceptor(EventInterceptor eventInterceptor) {
        if (this.zzn) {
            this.zzm.zza((zzda) eventInterceptor);
        } else {
            this.zzl.zzs().zza((zzda) eventInterceptor);
        }
    }

    @ShowFirstParty
    @KeepForSdk
    public void registerOnMeasurementEventListener(OnEventListener onEventListener) {
        if (this.zzn) {
            this.zzm.zza((zzdb) onEventListener);
        } else {
            this.zzl.zzs().zza((zzdb) onEventListener);
        }
    }

    @ShowFirstParty
    @KeepForSdk
    public void unregisterOnMeasurementEventListener(OnEventListener onEventListener) {
        if (this.zzn) {
            this.zzm.zzb((zzdb) onEventListener);
        } else {
            this.zzl.zzs().zzb(onEventListener);
        }
    }

    @Keep
    @Nullable
    public String getCurrentScreenName() {
        if (this.zzn) {
            return this.zzm.getCurrentScreenName();
        }
        return this.zzl.zzs().getCurrentScreenName();
    }

    @Keep
    @Nullable
    public String getCurrentScreenClass() {
        if (this.zzn) {
            return this.zzm.getCurrentScreenClass();
        }
        return this.zzl.zzs().getCurrentScreenClass();
    }

    @Keep
    @Nullable
    public String getAppInstanceId() {
        if (this.zzn) {
            return this.zzm.zzj();
        }
        return this.zzl.zzs().zzj();
    }

    @Keep
    @Nullable
    public String getGmpAppId() {
        if (this.zzn) {
            return this.zzm.getGmpAppId();
        }
        return this.zzl.zzs().getGmpAppId();
    }

    @Keep
    public long generateEventId() {
        if (this.zzn) {
            return this.zzm.generateEventId();
        }
        return this.zzl.zzab().zzgk();
    }

    @Keep
    public void beginAdUnitExposure(@Size(min = 1) @NonNull String str) {
        if (this.zzn) {
            this.zzm.beginAdUnitExposure(str);
        } else {
            this.zzl.zzr().beginAdUnitExposure(str, this.zzl.zzz().elapsedRealtime());
        }
    }

    @Keep
    public void endAdUnitExposure(@Size(min = 1) @NonNull String str) {
        if (this.zzn) {
            this.zzm.endAdUnitExposure(str);
        } else {
            this.zzl.zzr().endAdUnitExposure(str, this.zzl.zzz().elapsedRealtime());
        }
    }

    @ShowFirstParty
    @KeepForSdk
    @Keep
    public void setConditionalUserProperty(@NonNull ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        if (this.zzn) {
            this.zzm.setConditionalUserProperty(conditionalUserProperty.zzf());
        } else {
            this.zzl.zzs().setConditionalUserProperty(conditionalUserProperty.zzf());
        }
    }

    /* access modifiers changed from: protected */
    @Keep
    @VisibleForTesting
    public void setConditionalUserPropertyAs(@NonNull ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        if (!this.zzn) {
            this.zzl.zzs().zzd(conditionalUserProperty.zzf());
            return;
        }
        throw new IllegalStateException("Unexpected call on client side");
    }

    @ShowFirstParty
    @KeepForSdk
    @Keep
    public void clearConditionalUserProperty(@Size(max = 24, min = 1) @NonNull String str, @Nullable String str2, @Nullable Bundle bundle) {
        if (this.zzn) {
            this.zzm.clearConditionalUserProperty(str, str2, bundle);
        } else {
            this.zzl.zzs().clearConditionalUserProperty(str, str2, bundle);
        }
    }

    /* access modifiers changed from: protected */
    @Keep
    @VisibleForTesting
    public void clearConditionalUserPropertyAs(@Size(min = 1) @NonNull String str, @Size(max = 24, min = 1) @NonNull String str2, @Nullable String str3, @Nullable Bundle bundle) {
        if (!this.zzn) {
            this.zzl.zzs().clearConditionalUserPropertyAs(str, str2, str3, bundle);
            return;
        }
        throw new IllegalStateException("Unexpected call on client side");
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    @Keep
    @VisibleForTesting
    public Map<String, Object> getUserProperties(@Nullable String str, @Size(max = 24, min = 1) @Nullable String str2, boolean z) {
        if (this.zzn) {
            return this.zzm.getUserProperties(str, str2, z);
        }
        return this.zzl.zzs().getUserProperties(str, str2, z);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    @Keep
    @VisibleForTesting
    public Map<String, Object> getUserPropertiesAs(@Size(min = 1) @NonNull String str, @Nullable String str2, @Size(max = 23, min = 1) @Nullable String str3, boolean z) {
        if (!this.zzn) {
            return this.zzl.zzs().getUserPropertiesAs(str, str2, str3, z);
        }
        throw new IllegalStateException("Unexpected call on client side");
    }

    @WorkerThread
    @ShowFirstParty
    @Keep
    @KeepForSdk
    public List<ConditionalUserProperty> getConditionalUserProperties(@Nullable String str, @Size(max = 23, min = 1) @Nullable String str2) {
        List<Bundle> list;
        int i;
        if (this.zzn) {
            list = this.zzm.getConditionalUserProperties(str, str2);
        } else {
            list = this.zzl.zzs().zzn(str, str2);
        }
        if (list == null) {
            i = 0;
        } else {
            i = list.size();
        }
        ArrayList arrayList = new ArrayList(i);
        for (Bundle conditionalUserProperty : list) {
            arrayList.add(new ConditionalUserProperty(conditionalUserProperty));
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    @Keep
    @VisibleForTesting
    public List<ConditionalUserProperty> getConditionalUserPropertiesAs(@Size(min = 1) @NonNull String str, @Nullable String str2, @Size(max = 23, min = 1) @Nullable String str3) {
        int i;
        if (!this.zzn) {
            ArrayList zze = this.zzl.zzs().zze(str, str2, str3);
            int i2 = 0;
            if (zze == null) {
                i = 0;
            } else {
                i = zze.size();
            }
            ArrayList arrayList = new ArrayList(i);
            ArrayList arrayList2 = zze;
            int size = arrayList2.size();
            while (i2 < size) {
                Object obj = arrayList2.get(i2);
                i2++;
                arrayList.add(new ConditionalUserProperty((Bundle) obj));
            }
            return arrayList;
        }
        throw new IllegalStateException("Unexpected call on client side");
    }

    @WorkerThread
    @ShowFirstParty
    @Keep
    @KeepForSdk
    public int getMaxUserProperties(@Size(min = 1) @NonNull String str) {
        if (this.zzn) {
            return this.zzm.getMaxUserProperties(str);
        }
        this.zzl.zzs();
        Preconditions.checkNotEmpty(str);
        return 25;
    }

    @KeepForSdk
    public Boolean getBoolean() {
        if (this.zzn) {
            return (Boolean) this.zzm.zzb(4);
        }
        return this.zzl.zzs().zzev();
    }

    @KeepForSdk
    public String getString() {
        if (this.zzn) {
            return (String) this.zzm.zzb(0);
        }
        return this.zzl.zzs().zzew();
    }

    @KeepForSdk
    public Long getLong() {
        if (this.zzn) {
            return (Long) this.zzm.zzb(1);
        }
        return this.zzl.zzs().zzex();
    }

    @KeepForSdk
    public Integer getInteger() {
        if (this.zzn) {
            return (Integer) this.zzm.zzb(3);
        }
        return this.zzl.zzs().zzey();
    }

    @KeepForSdk
    public Double getDouble() {
        if (this.zzn) {
            return (Double) this.zzm.zzb(2);
        }
        return this.zzl.zzs().zzez();
    }
}
