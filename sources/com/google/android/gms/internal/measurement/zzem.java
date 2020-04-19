package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez.zzd;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class zzem {
    private static volatile boolean zzadj = false;
    private static final Class<?> zzadk = zzlr();
    private static volatile zzem zzadl;
    static final zzem zzadm = new zzem(true);
    private final Map<zza, zzd<?, ?>> zzadn;

    static final class zza {
        private final int number;
        private final Object object;

        zza(Object obj, int i) {
            this.object = obj;
            this.number = i;
        }

        public final int hashCode() {
            return (System.identityHashCode(this.object) * 65535) + this.number;
        }

        public final boolean equals(Object obj) {
            if (!(obj instanceof zza)) {
                return false;
            }
            zza zza = (zza) obj;
            if (this.object == zza.object && this.number == zza.number) {
                return true;
            }
            return false;
        }
    }

    private static Class<?> zzlr() {
        try {
            return Class.forName("com.google.protobuf.Extension");
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    public static zzem zzls() {
        return zzel.zzlp();
    }

    public static zzem zzlt() {
        zzem zzem = zzadl;
        if (zzem == null) {
            synchronized (zzem.class) {
                zzem = zzadl;
                if (zzem == null) {
                    zzem = zzel.zzlq();
                    zzadl = zzem;
                }
            }
        }
        return zzem;
    }

    static zzem zzlq() {
        return zzex.zza(zzem.class);
    }

    public final <ContainingType extends zzgh> zzd<ContainingType, ?> zza(ContainingType containingtype, int i) {
        return (zzd) this.zzadn.get(new zza(containingtype, i));
    }

    zzem() {
        this.zzadn = new HashMap();
    }

    private zzem(boolean z) {
        this.zzadn = Collections.emptyMap();
    }
}
