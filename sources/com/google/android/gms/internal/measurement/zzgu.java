package com.google.android.gms.internal.measurement;

import com.box.androidsdk.content.BoxApiMetadata;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class zzgu {
    private static final zzgu zzajt = new zzgu();
    private final zzgz zzaju = new zzfw();
    private final ConcurrentMap<Class<?>, zzgy<?>> zzajv = new ConcurrentHashMap();

    public static zzgu zznz() {
        return zzajt;
    }

    public final <T> zzgy<T> zzf(Class<T> cls) {
        zzfb.zza(cls, "messageType");
        zzgy<T> zzgy = (zzgy) this.zzajv.get(cls);
        if (zzgy != null) {
            return zzgy;
        }
        zzgy<T> zze = this.zzaju.zze(cls);
        zzfb.zza(cls, "messageType");
        zzfb.zza(zze, BoxApiMetadata.BOX_API_METADATA_SCHEMA);
        zzgy zzgy2 = (zzgy) this.zzajv.putIfAbsent(cls, zze);
        return zzgy2 != null ? zzgy2 : zze;
    }

    public final <T> zzgy<T> zzv(T t) {
        return zzf(t.getClass());
    }

    private zzgu() {
    }
}
