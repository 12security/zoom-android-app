package com.google.android.gms.internal.measurement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public abstract class zzcw<T> {
    @SuppressLint({"StaticFieldLeak"})
    private static Context zzno = null;
    private static final Object zzzt = new Object();
    private static boolean zzzu = false;
    private static final AtomicInteger zzzx = new AtomicInteger();
    private final String name;
    private volatile T zzje;
    private final zzdc zzzv;
    private final T zzzw;
    private volatile int zzzy;

    public static void zzq(Context context) {
        synchronized (zzzt) {
            Context applicationContext = context.getApplicationContext();
            if (applicationContext != null) {
                context = applicationContext;
            }
            if (zzno != context) {
                synchronized (zzcl.class) {
                    zzcl.zzzi.clear();
                }
                synchronized (zzdd.class) {
                    zzdd.zzaai.clear();
                }
                synchronized (zzcs.class) {
                    zzcs.zzzq = null;
                }
                zzzx.incrementAndGet();
                zzno = context;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract T zzc(Object obj);

    static void zzjp() {
        zzzx.incrementAndGet();
    }

    private zzcw(zzdc zzdc, String str, T t) {
        this.zzzy = -1;
        if (zzdc.zzaaa != null) {
            this.zzzv = zzdc;
            this.name = str;
            this.zzzw = t;
            return;
        }
        throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
    }

    private final String zzce(String str) {
        if (str != null && str.isEmpty()) {
            return this.name;
        }
        String valueOf = String.valueOf(str);
        String valueOf2 = String.valueOf(this.name);
        return valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
    }

    public final String zzjq() {
        return zzce(this.zzzv.zzaac);
    }

    public final T getDefaultValue() {
        return this.zzzw;
    }

    public final T get() {
        int i = zzzx.get();
        if (this.zzzy < i) {
            synchronized (this) {
                if (this.zzzy < i) {
                    if (zzno != null) {
                        zzdc zzdc = this.zzzv;
                        T zzjr = zzjr();
                        if (zzjr == null) {
                            zzjr = zzjs();
                            if (zzjr == null) {
                                zzjr = this.zzzw;
                            }
                        }
                        this.zzje = zzjr;
                        this.zzzy = i;
                    } else {
                        throw new IllegalStateException("Must call PhenotypeFlag.init() first");
                    }
                }
            }
        }
        return this.zzje;
    }

    @Nullable
    private final T zzjr() {
        zzcp zzcp;
        zzdc zzdc = this.zzzv;
        String str = (String) zzcs.zzp(zzno).zzca("gms:phenotype:phenotype_flag:debug_bypass_phenotype");
        if (!(str != null && zzci.zzyv.matcher(str).matches())) {
            if (this.zzzv.zzaaa != null) {
                zzdc zzdc2 = this.zzzv;
                zzcp = zzcl.zza(zzno.getContentResolver(), this.zzzv.zzaaa);
            } else {
                Context context = zzno;
                zzdc zzdc3 = this.zzzv;
                zzcp = zzdd.zze(context, null);
            }
            if (zzcp != null) {
                Object zzca = zzcp.zzca(zzjq());
                if (zzca != null) {
                    return zzc(zzca);
                }
            }
        } else {
            String str2 = "PhenotypeFlag";
            String str3 = "Bypass reading Phenotype values for flag: ";
            String valueOf = String.valueOf(zzjq());
            Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        return null;
    }

    @Nullable
    private final T zzjs() {
        zzdc zzdc = this.zzzv;
        Object zzca = zzcs.zzp(zzno).zzca(zzce(this.zzzv.zzaab));
        if (zzca != null) {
            return zzc(zzca);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static zzcw<Long> zza(zzdc zzdc, String str, long j) {
        return new zzcx(zzdc, str, Long.valueOf(j));
    }

    /* access modifiers changed from: private */
    public static zzcw<Integer> zza(zzdc zzdc, String str, int i) {
        return new zzcy(zzdc, str, Integer.valueOf(i));
    }

    /* access modifiers changed from: private */
    public static zzcw<Boolean> zza(zzdc zzdc, String str, boolean z) {
        return new zzcz(zzdc, str, Boolean.valueOf(z));
    }

    /* access modifiers changed from: private */
    public static zzcw<Double> zza(zzdc zzdc, String str, double d) {
        return new zzda(zzdc, str, Double.valueOf(d));
    }

    /* access modifiers changed from: private */
    public static zzcw<String> zza(zzdc zzdc, String str, String str2) {
        return new zzdb(zzdc, str, str2);
    }

    /* synthetic */ zzcw(zzdc zzdc, String str, Object obj, zzcx zzcx) {
        this(zzdc, str, obj);
    }
}
