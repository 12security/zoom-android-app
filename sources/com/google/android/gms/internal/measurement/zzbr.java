package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzez.zzb;
import com.google.android.gms.internal.measurement.zzez.zze;

public final class zzbr {

    public static final class zza extends zzez<zza, C4566zza> implements zzgj {
        private static volatile zzgs<zza> zztq;
        /* access modifiers changed from: private */
        public static final zza zzuo = new zza();
        private int zztj;
        private String zzum = "";
        private String zzun = "";

        /* renamed from: com.google.android.gms.internal.measurement.zzbr$zza$zza reason: collision with other inner class name */
        public static final class C4566zza extends com.google.android.gms.internal.measurement.zzez.zza<zza, C4566zza> implements zzgj {
            private C4566zza() {
                super(zza.zzuo);
            }

            /* synthetic */ C4566zza(zzbs zzbs) {
                this();
            }
        }

        private zza() {
        }

        public final String getKey() {
            return this.zzum;
        }

        public final String getValue() {
            return this.zzun;
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbs.zzti[i - 1]) {
                case 1:
                    return new zza();
                case 2:
                    return new C4566zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zzum", "zzun"};
                    return zza((zzgh) zzuo, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001\b\u0000\u0002\b\u0001", objArr);
                case 4:
                    return zzuo;
                case 5:
                    zzgs<zza> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zza.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new zzb<>(zzuo);
                                zztq = zzgs;
                            }
                        }
                    }
                    return zzgs;
                case 6:
                    return Byte.valueOf(1);
                case 7:
                    return null;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public static zzgs<zza> zzgs() {
            return (zzgs) zzuo.zza(zze.zzaha, (Object) null, (Object) null);
        }

        static {
            zzez.zza(zza.class, zzuo);
        }
    }
}
