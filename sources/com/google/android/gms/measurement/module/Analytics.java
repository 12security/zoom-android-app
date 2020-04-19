package com.google.android.gms.measurement.module;

import android.content.Context;
import androidx.annotation.Keep;
import androidx.annotation.RequiresPermission;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.internal.measurement.zzy;
import com.google.android.gms.measurement.internal.zzby;
import com.google.android.gms.measurement.internal.zzcx;
import com.google.android.gms.measurement.internal.zzcy;

@ShowFirstParty
public class Analytics {
    @ShowFirstParty
    @KeepForSdk
    public static final String CRASH_ORIGIN = "crash";
    @ShowFirstParty
    @KeepForSdk
    public static final String FCM_ORIGIN = "fcm";
    @ShowFirstParty
    @KeepForSdk
    public static final String FIAM_ORIGIN = "fiam";
    private static volatile Analytics zzth;
    private final zzby zzl;

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

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WAKE_LOCK"})
    @ShowFirstParty
    @Keep
    public static Analytics getInstance(Context context) {
        if (zzth == null) {
            synchronized (Analytics.class) {
                if (zzth == null) {
                    zzth = new Analytics(zzby.zza(context, (zzy) null));
                }
            }
        }
        return zzth;
    }

    private Analytics(zzby zzby) {
        Preconditions.checkNotNull(zzby);
        this.zzl = zzby;
    }
}
