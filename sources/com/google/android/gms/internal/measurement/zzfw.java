package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez.zze;

final class zzfw implements zzgz {
    private static final zzgg zzaiq = new zzfx();
    private final zzgg zzaip;

    public zzfw() {
        this(new zzfy(zzey.zzmf(), zznl()));
    }

    private zzfw(zzgg zzgg) {
        this.zzaip = (zzgg) zzfb.zza(zzgg, "messageInfoFactory");
    }

    public final <T> zzgy<T> zze(Class<T> cls) {
        zzha.zzg(cls);
        zzgf zzc = this.zzaip.zzc(cls);
        if (zzc.zznt()) {
            if (zzez.class.isAssignableFrom(cls)) {
                return zzgn.zza(zzha.zzof(), zzep.zzlv(), zzc.zznu());
            }
            return zzgn.zza(zzha.zzod(), zzep.zzlw(), zzc.zznu());
        } else if (zzez.class.isAssignableFrom(cls)) {
            if (zza(zzc)) {
                return zzgl.zza(cls, zzc, zzgr.zznx(), zzfr.zznj(), zzha.zzof(), zzep.zzlv(), zzge.zznq());
            }
            return zzgl.zza(cls, zzc, zzgr.zznx(), zzfr.zznj(), zzha.zzof(), null, zzge.zznq());
        } else if (zza(zzc)) {
            return zzgl.zza(cls, zzc, zzgr.zznw(), zzfr.zzni(), zzha.zzod(), zzep.zzlw(), zzge.zznp());
        } else {
            return zzgl.zza(cls, zzc, zzgr.zznw(), zzfr.zzni(), zzha.zzoe(), null, zzge.zznp());
        }
    }

    private static boolean zza(zzgf zzgf) {
        return zzgf.zzns() == zze.zzahc;
    }

    private static zzgg zznl() {
        try {
            return (zzgg) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception unused) {
            return zzaiq;
        }
    }
}
