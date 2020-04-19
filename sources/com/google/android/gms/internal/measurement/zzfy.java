package com.google.android.gms.internal.measurement;

final class zzfy implements zzgg {
    private zzgg[] zzair;

    zzfy(zzgg... zzggArr) {
        this.zzair = zzggArr;
    }

    public final boolean zzb(Class<?> cls) {
        for (zzgg zzb : this.zzair) {
            if (zzb.zzb(cls)) {
                return true;
            }
        }
        return false;
    }

    public final zzgf zzc(Class<?> cls) {
        zzgg[] zzggArr;
        for (zzgg zzgg : this.zzair) {
            if (zzgg.zzb(cls)) {
                return zzgg.zzc(cls);
            }
        }
        String str = "No factory is available for message type: ";
        String valueOf = String.valueOf(cls.getName());
        throw new UnsupportedOperationException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }
}
