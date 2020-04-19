package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.GuardedBy;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;

public final class zzau extends zzcu {
    /* access modifiers changed from: private */
    public char zzjp = 0;
    @GuardedBy("this")
    private String zzjq;
    private final zzaw zzjr = new zzaw(this, 6, false, false);
    private final zzaw zzjs = new zzaw(this, 6, true, false);
    private final zzaw zzjt = new zzaw(this, 6, false, true);
    private final zzaw zzju = new zzaw(this, 5, false, false);
    private final zzaw zzjv = new zzaw(this, 5, true, false);
    private final zzaw zzjw = new zzaw(this, 5, false, true);
    private final zzaw zzjx = new zzaw(this, 4, false, false);
    private final zzaw zzjy = new zzaw(this, 3, false, false);
    private final zzaw zzjz = new zzaw(this, 2, false, false);
    /* access modifiers changed from: private */
    public long zzt = -1;

    zzau(zzby zzby) {
        super(zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    public final zzaw zzda() {
        return this.zzjr;
    }

    public final zzaw zzdb() {
        return this.zzjs;
    }

    public final zzaw zzdc() {
        return this.zzjt;
    }

    public final zzaw zzdd() {
        return this.zzju;
    }

    public final zzaw zzde() {
        return this.zzjv;
    }

    public final zzaw zzdf() {
        return this.zzjw;
    }

    public final zzaw zzdg() {
        return this.zzjx;
    }

    public final zzaw zzdh() {
        return this.zzjy;
    }

    public final zzaw zzdi() {
        return this.zzjz;
    }

    protected static Object zzao(String str) {
        if (str == null) {
            return null;
        }
        return new zzax(str);
    }

    /* access modifiers changed from: protected */
    public final void zza(int i, boolean z, boolean z2, String str, Object obj, Object obj2, Object obj3) {
        if (!z && isLoggable(i)) {
            zza(i, zza(false, str, obj, obj2, obj3));
        }
        if (!z2 && i >= 5) {
            Preconditions.checkNotNull(str);
            zzbt zzek = this.zzl.zzek();
            if (zzek == null) {
                zza(6, "Scheduler not set. Not logging error/warn");
            } else if (!zzek.isInitialized()) {
                zza(6, "Scheduler not initialized. Not logging error/warn");
            } else {
                if (i < 0) {
                    i = 0;
                }
                zzav zzav = new zzav(this, i >= 9 ? 8 : i, str, obj, obj2, obj3);
                zzek.zza((Runnable) zzav);
            }
        }
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public final boolean isLoggable(int i) {
        return Log.isLoggable(zzdj(), i);
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public final void zza(int i, String str) {
        Log.println(i, zzdj(), str);
    }

    @VisibleForTesting
    private final String zzdj() {
        String str;
        synchronized (this) {
            if (this.zzjq == null) {
                if (this.zzl.zzeo() != null) {
                    this.zzjq = this.zzl.zzeo();
                } else {
                    this.zzjq = zzt.zzbo();
                }
            }
            str = this.zzjq;
        }
        return str;
    }

    static String zza(boolean z, String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            str = "";
        }
        String zza = zza(z, obj);
        String zza2 = zza(z, obj2);
        String zza3 = zza(z, obj3);
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(zza)) {
            sb.append(str2);
            sb.append(zza);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zza2)) {
            sb.append(str2);
            sb.append(zza2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zza3)) {
            sb.append(str2);
            sb.append(zza3);
        }
        return sb.toString();
    }

    @VisibleForTesting
    private static String zza(boolean z, Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof Integer) {
            obj = Long.valueOf((long) ((Integer) obj).intValue());
        }
        int i = 0;
        if (obj instanceof Long) {
            if (!z) {
                return String.valueOf(obj);
            }
            Long l = (Long) obj;
            if (Math.abs(l.longValue()) < 100) {
                return String.valueOf(obj);
            }
            String str = String.valueOf(obj).charAt(0) == '-' ? "-" : "";
            String valueOf = String.valueOf(Math.abs(l.longValue()));
            long round = Math.round(Math.pow(10.0d, (double) (valueOf.length() - 1)));
            long round2 = Math.round(Math.pow(10.0d, (double) valueOf.length()) - 1.0d);
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 43 + String.valueOf(str).length());
            sb.append(str);
            sb.append(round);
            sb.append("...");
            sb.append(str);
            sb.append(round2);
            return sb.toString();
        } else if (obj instanceof Boolean) {
            return String.valueOf(obj);
        } else {
            if (obj instanceof Throwable) {
                Throwable th = (Throwable) obj;
                StringBuilder sb2 = new StringBuilder(z ? th.getClass().getName() : th.toString());
                String zzap = zzap(zzby.class.getCanonicalName());
                StackTraceElement[] stackTrace = th.getStackTrace();
                int length = stackTrace.length;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    StackTraceElement stackTraceElement = stackTrace[i];
                    if (!stackTraceElement.isNativeMethod()) {
                        String className = stackTraceElement.getClassName();
                        if (className != null && zzap(className).equals(zzap)) {
                            sb2.append(": ");
                            sb2.append(stackTraceElement);
                            break;
                        }
                    }
                    i++;
                }
                return sb2.toString();
            } else if (obj instanceof zzax) {
                return ((zzax) obj).zzki;
            } else {
                if (z) {
                    return "-";
                }
                return String.valueOf(obj);
            }
        }
    }

    private static String zzap(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf == -1) {
            return str;
        }
        return str.substring(0, lastIndexOf);
    }

    public final String zzdk() {
        Pair<String, Long> zzeb = zzae().zzla.zzeb();
        if (zzeb == null || zzeb == zzbf.zzky) {
            return null;
        }
        String valueOf = String.valueOf(zzeb.second);
        String str = (String) zzeb.first;
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 1 + String.valueOf(str).length());
        sb.append(valueOf);
        sb.append(":");
        sb.append(str);
        return sb.toString();
    }

    public final /* bridge */ /* synthetic */ void zzn() {
        super.zzn();
    }

    public final /* bridge */ /* synthetic */ void zzo() {
        super.zzo();
    }

    public final /* bridge */ /* synthetic */ void zzp() {
        super.zzp();
    }

    public final /* bridge */ /* synthetic */ void zzq() {
        super.zzq();
    }

    public final /* bridge */ /* synthetic */ zzad zzy() {
        return super.zzy();
    }

    public final /* bridge */ /* synthetic */ Clock zzz() {
        return super.zzz();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzas zzaa() {
        return super.zzaa();
    }

    public final /* bridge */ /* synthetic */ zzgd zzab() {
        return super.zzab();
    }

    public final /* bridge */ /* synthetic */ zzbt zzac() {
        return super.zzac();
    }

    public final /* bridge */ /* synthetic */ zzau zzad() {
        return super.zzad();
    }

    public final /* bridge */ /* synthetic */ zzbf zzae() {
        return super.zzae();
    }

    public final /* bridge */ /* synthetic */ zzt zzaf() {
        return super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzq zzag() {
        return super.zzag();
    }
}
