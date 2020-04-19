package com.google.android.gms.measurement.internal;

import com.google.android.gms.measurement.AppMeasurement.UserProperty;

public class zzcz {
    public static final String[] zzpc = {"firebase_last_notification", "first_open_time", "first_visit_time", "last_deep_link_referrer", "user_id", "first_open_after_install", "lifetime_user_engagement", "non_personalized_ads", "session_number", "ga_session_number", "session_id", "ga_session_id"};
    public static final String[] zzpd = {UserProperty.FIREBASE_LAST_NOTIFICATION, "_fot", "_fvt", "_ldl", "_id", "_fi", "_lte", "_npa", "_sno", "_sno", "_sid", "_sid"};

    protected zzcz() {
    }

    public static String zzbh(String str) {
        return zzeb.zza(str, zzpc, zzpd);
    }
}
