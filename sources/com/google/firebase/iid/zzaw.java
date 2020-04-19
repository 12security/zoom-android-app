package com.google.firebase.iid;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

final class zzaw {
    private static final long zzdk = TimeUnit.DAYS.toMillis(7);
    private final long timestamp;
    final String zzbx;
    private final String zzdl;

    private zzaw(String str, String str2, long j) {
        this.zzbx = str;
        this.zzdl = str2;
        this.timestamp = j;
    }

    static zzaw zzf(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.startsWith("{")) {
            return new zzaw(str, null, 0);
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new zzaw(jSONObject.getString("token"), jSONObject.getString("appVersion"), jSONObject.getLong("timestamp"));
        } catch (JSONException e) {
            String valueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 23);
            sb.append("Failed to parse token: ");
            sb.append(valueOf);
            Log.w("FirebaseInstanceId", sb.toString());
            return null;
        }
    }

    static String zza(String str, String str2, long j) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("token", str);
            jSONObject.put("appVersion", str2);
            jSONObject.put("timestamp", j);
            return jSONObject.toString();
        } catch (JSONException e) {
            String valueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 24);
            sb.append("Failed to encode token: ");
            sb.append(valueOf);
            Log.w("FirebaseInstanceId", sb.toString());
            return null;
        }
    }

    static String zzb(@Nullable zzaw zzaw) {
        if (zzaw == null) {
            return null;
        }
        return zzaw.zzbx;
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzg(String str) {
        return System.currentTimeMillis() > this.timestamp + zzdk || !str.equals(this.zzdl);
    }
}
