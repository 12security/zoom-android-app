package com.google.android.gms.internal.measurement;

public final class zzbl {

    public static final class zza extends zzez<zza, C4564zza> implements zzgj {
        /* access modifiers changed from: private */
        public static final zza zztp = new zza();
        private static volatile zzgs<zza> zztq;
        private int zztj;
        private int zztk;
        private boolean zztl;
        private String zztm = "";
        private String zztn = "";
        private String zzto = "";

        /* renamed from: com.google.android.gms.internal.measurement.zzbl$zza$zza reason: collision with other inner class name */
        public static final class C4564zza extends com.google.android.gms.internal.measurement.zzez.zza<zza, C4564zza> implements zzgj {
            private C4564zza() {
                super(zza.zztp);
            }

            /* synthetic */ C4564zza(zzbm zzbm) {
                this();
            }
        }

        public enum zzb implements zzfc {
            UNKNOWN_COMPARISON_TYPE(0),
            LESS_THAN(1),
            GREATER_THAN(2),
            EQUAL(3),
            BETWEEN(4);
            
            private static final zzfd<zzb> zztw = null;
            private final int value;

            public final int zzgp() {
                return this.value;
            }

            public static zzb zze(int i) {
                switch (i) {
                    case 0:
                        return UNKNOWN_COMPARISON_TYPE;
                    case 1:
                        return LESS_THAN;
                    case 2:
                        return GREATER_THAN;
                    case 3:
                        return EQUAL;
                    case 4:
                        return BETWEEN;
                    default:
                        return null;
                }
            }

            public static zzfe zzgq() {
                return zzbo.zzty;
            }

            private zzb(int i) {
                this.value = i;
            }

            static {
                zztw = new zzbn();
            }
        }

        private zza() {
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbm.zzti[i - 1]) {
                case 1:
                    return new zza();
                case 2:
                    return new C4564zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zztk", zzb.zzgq(), "zztl", "zztm", "zztn", "zzto"};
                    return zza((zzgh) zztp, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0000\u0000\u0001\f\u0000\u0002\u0007\u0001\u0003\b\u0002\u0004\b\u0003\u0005\b\u0004", objArr);
                case 4:
                    return zztp;
                case 5:
                    zzgs<zza> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zza.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zztp);
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

        static {
            zzez.zza(zza.class, zztp);
        }
    }

    public static final class zzb extends zzez<zzb, zza> implements zzgj {
        private static volatile zzgs<zzb> zztq;
        /* access modifiers changed from: private */
        public static final zzb zzud = new zzb();
        private int zztj;
        private int zztz;
        private String zzua = "";
        private boolean zzub;
        private zzfg<String> zzuc = zzez.zzmj();

        public static final class zza extends com.google.android.gms.internal.measurement.zzez.zza<zzb, zza> implements zzgj {
            private zza() {
                super(zzb.zzud);
            }

            /* synthetic */ zza(zzbm zzbm) {
                this();
            }
        }

        /* renamed from: com.google.android.gms.internal.measurement.zzbl$zzb$zzb reason: collision with other inner class name */
        public enum C4565zzb implements zzfc {
            UNKNOWN_MATCH_TYPE(0),
            REGEXP(1),
            BEGINS_WITH(2),
            ENDS_WITH(3),
            PARTIAL(4),
            EXACT(5),
            IN_LIST(6);
            
            private static final zzfd<C4565zzb> zztw = null;
            private final int value;

            public final int zzgp() {
                return this.value;
            }

            public static C4565zzb zzg(int i) {
                switch (i) {
                    case 0:
                        return UNKNOWN_MATCH_TYPE;
                    case 1:
                        return REGEXP;
                    case 2:
                        return BEGINS_WITH;
                    case 3:
                        return ENDS_WITH;
                    case 4:
                        return PARTIAL;
                    case 5:
                        return EXACT;
                    case 6:
                        return IN_LIST;
                    default:
                        return null;
                }
            }

            public static zzfe zzgq() {
                return zzbq.zzty;
            }

            private C4565zzb(int i) {
                this.value = i;
            }

            static {
                zztw = new zzbp();
            }
        }

        private zzb() {
        }

        /* access modifiers changed from: protected */
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzbm.zzti[i - 1]) {
                case 1:
                    return new zzb();
                case 2:
                    return new zza(null);
                case 3:
                    Object[] objArr = {"zztj", "zztz", C4565zzb.zzgq(), "zzua", "zzub", "zzuc"};
                    return zza((zzgh) zzud, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0001\u0000\u0001\f\u0000\u0002\b\u0001\u0003\u0007\u0002\u0004\u001a", objArr);
                case 4:
                    return zzud;
                case 5:
                    zzgs<zzb> zzgs = zztq;
                    if (zzgs == null) {
                        synchronized (zzb.class) {
                            zzgs = zztq;
                            if (zzgs == null) {
                                zzgs = new com.google.android.gms.internal.measurement.zzez.zzb<>(zzud);
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

        static {
            zzez.zza(zzb.class, zzud);
        }
    }
}
