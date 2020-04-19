package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

final /* synthetic */ class zzde implements OnSharedPreferenceChangeListener {
    private final zzdd zzaal;

    zzde(zzdd zzdd) {
        this.zzaal = zzdd;
    }

    public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        this.zzaal.zza(sharedPreferences, str);
    }
}
