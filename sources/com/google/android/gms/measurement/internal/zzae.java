package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import java.util.Iterator;

public final class zzae {
    final String name;
    private final String origin;
    final long timestamp;
    final String zzcf;
    final long zzfc;
    final zzag zzfd;

    private zzae(zzby zzby, String str, String str2, String str3, long j, long j2, zzag zzag) {
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotEmpty(str3);
        Preconditions.checkNotNull(zzag);
        this.zzcf = str2;
        this.name = str3;
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.origin = str;
        this.timestamp = j;
        this.zzfc = j2;
        long j3 = this.zzfc;
        if (j3 != 0 && j3 > this.timestamp) {
            zzby.zzad().zzdd().zza("Event created with reverse previous/current timestamps. appId, name", zzau.zzao(str2), zzau.zzao(str3));
        }
        this.zzfd = zzag;
    }

    zzae(zzby zzby, String str, String str2, String str3, long j, long j2, Bundle bundle) {
        zzag zzag;
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotEmpty(str3);
        this.zzcf = str2;
        this.name = str3;
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        this.origin = str;
        this.timestamp = j;
        this.zzfc = j2;
        long j3 = this.zzfc;
        if (j3 != 0 && j3 > this.timestamp) {
            zzby.zzad().zzdd().zza("Event created with reverse previous/current timestamps. appId", zzau.zzao(str2));
        }
        if (bundle == null || bundle.isEmpty()) {
            zzag = new zzag(new Bundle());
        } else {
            Bundle bundle2 = new Bundle(bundle);
            Iterator it = bundle2.keySet().iterator();
            while (it.hasNext()) {
                String str4 = (String) it.next();
                if (str4 == null) {
                    zzby.zzad().zzda().zzaq("Param name can't be null");
                    it.remove();
                } else {
                    Object zzb = zzby.zzab().zzb(str4, bundle2.get(str4));
                    if (zzb == null) {
                        zzby.zzad().zzdd().zza("Param value can't be null", zzby.zzaa().zzam(str4));
                        it.remove();
                    } else {
                        zzby.zzab().zza(bundle2, str4, zzb);
                    }
                }
            }
            zzag = new zzag(bundle2);
        }
        this.zzfd = zzag;
    }

    /* access modifiers changed from: 0000 */
    public final zzae zza(zzby zzby, long j) {
        zzae zzae = new zzae(zzby, this.origin, this.zzcf, this.name, this.timestamp, j, this.zzfd);
        return zzae;
    }

    public final String toString() {
        String str = this.zzcf;
        String str2 = this.name;
        String valueOf = String.valueOf(this.zzfd);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 33 + String.valueOf(str2).length() + String.valueOf(valueOf).length());
        sb.append("Event{appId='");
        sb.append(str);
        sb.append("', name='");
        sb.append(str2);
        sb.append("', params=");
        sb.append(valueOf);
        sb.append('}');
        return sb.toString();
    }
}
