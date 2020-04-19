package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.measurement.zzbt.zzd;
import com.google.android.gms.internal.measurement.zzce;
import com.google.android.gms.internal.measurement.zzcf;
import com.google.android.gms.internal.measurement.zzch;
import com.google.android.gms.internal.measurement.zzez;
import com.google.android.gms.internal.measurement.zzy;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class zzft implements zzcv {
    private static volatile zzft zzrt;
    private boolean zzce;
    private final zzby zzl;
    private zzbs zzru;
    private zzay zzrv;
    private zzw zzrw;
    private zzbd zzrx;
    private zzfp zzry;
    private zzo zzrz;
    private final zzfz zzsa;
    private zzea zzsb;
    private boolean zzsc;
    private boolean zzsd;
    @VisibleForTesting
    private long zzse;
    private List<Runnable> zzsf;
    private int zzsg;
    private int zzsh;
    private boolean zzsi;
    private boolean zzsj;
    private boolean zzsk;
    private FileLock zzsl;
    private FileChannel zzsm;
    private List<Long> zzsn;
    private List<Long> zzso;
    private long zzsp;

    class zza implements zzy {
        zzch zzst;
        List<Long> zzsu;
        List<zzcf> zzsv;
        private long zzsw;

        private zza() {
        }

        public final void zzb(zzch zzch) {
            Preconditions.checkNotNull(zzch);
            this.zzst = zzch;
        }

        public final boolean zza(long j, zzcf zzcf) {
            Preconditions.checkNotNull(zzcf);
            if (this.zzsv == null) {
                this.zzsv = new ArrayList();
            }
            if (this.zzsu == null) {
                this.zzsu = new ArrayList();
            }
            if (this.zzsv.size() > 0 && zza((zzcf) this.zzsv.get(0)) != zza(zzcf)) {
                return false;
            }
            long zzly = this.zzsw + ((long) zzcf.zzly());
            if (zzly >= ((long) Math.max(0, ((Integer) zzal.zzgl.get(null)).intValue()))) {
                return false;
            }
            this.zzsw = zzly;
            this.zzsv.add(zzcf);
            this.zzsu.add(Long.valueOf(j));
            if (this.zzsv.size() >= Math.max(1, ((Integer) zzal.zzgm.get(null)).intValue())) {
                return false;
            }
            return true;
        }

        private static long zza(zzcf zzcf) {
            return ((zzcf.zzxj.longValue() / 1000) / 60) / 60;
        }

        /* synthetic */ zza(zzft zzft, zzfu zzfu) {
            this();
        }
    }

    public static zzft zzm(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzrt == null) {
            synchronized (zzft.class) {
                if (zzrt == null) {
                    zzrt = new zzft(new zzfy(context));
                }
            }
        }
        return zzrt;
    }

    private zzft(zzfy zzfy) {
        this(zzfy, null);
    }

    private zzft(zzfy zzfy, zzby zzby) {
        this.zzce = false;
        Preconditions.checkNotNull(zzfy);
        this.zzl = zzby.zza(zzfy.zzno, (zzy) null);
        this.zzsp = -1;
        zzfz zzfz = new zzfz(this);
        zzfz.zzai();
        this.zzsa = zzfz;
        zzay zzay = new zzay(this);
        zzay.zzai();
        this.zzrv = zzay;
        zzbs zzbs = new zzbs(this);
        zzbs.zzai();
        this.zzru = zzbs;
        this.zzl.zzac().zza((Runnable) new zzfu(this, zzfy));
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zza(zzfy zzfy) {
        this.zzl.zzac().zzq();
        zzw zzw = new zzw(this);
        zzw.zzai();
        this.zzrw = zzw;
        this.zzl.zzaf().zza((zzv) this.zzru);
        zzo zzo = new zzo(this);
        zzo.zzai();
        this.zzrz = zzo;
        zzea zzea = new zzea(this);
        zzea.zzai();
        this.zzsb = zzea;
        zzfp zzfp = new zzfp(this);
        zzfp.zzai();
        this.zzry = zzfp;
        this.zzrx = new zzbd(this);
        if (this.zzsg != this.zzsh) {
            this.zzl.zzad().zzda().zza("Not all upload components initialized", Integer.valueOf(this.zzsg), Integer.valueOf(this.zzsh));
        }
        this.zzce = true;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void start() {
        this.zzl.zzac().zzq();
        zzdo().zzca();
        if (this.zzl.zzae().zzlb.get() == 0) {
            this.zzl.zzae().zzlb.set(this.zzl.zzz().currentTimeMillis());
        }
        zzgc();
    }

    public final zzq zzag() {
        return this.zzl.zzag();
    }

    public final zzt zzaf() {
        return this.zzl.zzaf();
    }

    public final zzau zzad() {
        return this.zzl.zzad();
    }

    public final zzbt zzac() {
        return this.zzl.zzac();
    }

    public final zzbs zzdp() {
        zza((zzfs) this.zzru);
        return this.zzru;
    }

    public final zzay zzfu() {
        zza((zzfs) this.zzrv);
        return this.zzrv;
    }

    public final zzw zzdo() {
        zza((zzfs) this.zzrw);
        return this.zzrw;
    }

    private final zzbd zzfv() {
        zzbd zzbd = this.zzrx;
        if (zzbd != null) {
            return zzbd;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    private final zzfp zzfw() {
        zza((zzfs) this.zzry);
        return this.zzry;
    }

    public final zzo zzdn() {
        zza((zzfs) this.zzrz);
        return this.zzrz;
    }

    public final zzea zzfx() {
        zza((zzfs) this.zzsb);
        return this.zzsb;
    }

    public final zzfz zzdm() {
        zza((zzfs) this.zzsa);
        return this.zzsa;
    }

    public final zzas zzaa() {
        return this.zzl.zzaa();
    }

    public final Context getContext() {
        return this.zzl.getContext();
    }

    public final Clock zzz() {
        return this.zzl.zzz();
    }

    public final zzgd zzab() {
        return this.zzl.zzab();
    }

    @WorkerThread
    private final void zzq() {
        this.zzl.zzac().zzq();
    }

    /* access modifiers changed from: 0000 */
    public final void zzfy() {
        if (!this.zzce) {
            throw new IllegalStateException("UploadController is not initialized");
        }
    }

    private static void zza(zzfs zzfs) {
        if (zzfs == null) {
            throw new IllegalStateException("Upload Component not created");
        } else if (!zzfs.isInitialized()) {
            String valueOf = String.valueOf(zzfs.getClass());
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
            sb.append("Component not initialized: ");
            sb.append(valueOf);
            throw new IllegalStateException(sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zze(zzm zzm) {
        zzq();
        zzfy();
        Preconditions.checkNotEmpty(zzm.packageName);
        zzg(zzm);
    }

    private final long zzfz() {
        long currentTimeMillis = this.zzl.zzz().currentTimeMillis();
        zzbf zzae = this.zzl.zzae();
        zzae.zzah();
        zzae.zzq();
        long j = zzae.zzlf.get();
        if (j == 0) {
            j = 1 + ((long) zzae.zzab().zzgl().nextInt(86400000));
            zzae.zzlf.set(j);
        }
        return ((((currentTimeMillis + j) / 1000) / 60) / 60) / 24;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzd(zzaj zzaj, String str) {
        zzaj zzaj2 = zzaj;
        String str2 = str;
        zzg zzae = zzdo().zzae(str2);
        if (zzae == null || TextUtils.isEmpty(zzae.zzas())) {
            this.zzl.zzad().zzdh().zza("No app data available; dropping event", str2);
            return;
        }
        Boolean zzc = zzc(zzae);
        if (zzc == null) {
            if (!"_ui".equals(zzaj2.name)) {
                this.zzl.zzad().zzdd().zza("Could not find package. appId", zzau.zzao(str));
            }
        } else if (!zzc.booleanValue()) {
            this.zzl.zzad().zzda().zza("App version does not match; dropping event. appId", zzau.zzao(str));
            return;
        }
        zzm zzm = r2;
        zzg zzg = zzae;
        zzm zzm2 = new zzm(str, zzae.getGmpAppId(), zzae.zzas(), zzae.zzat(), zzae.zzau(), zzae.zzav(), zzae.zzaw(), (String) null, zzae.isMeasurementEnabled(), false, zzg.getFirebaseInstanceId(), zzg.zzbk(), 0, 0, zzg.zzbl(), zzg.zzbm(), false, zzg.zzao(), zzg.zzbn(), zzg.zzax());
        zzc(zzaj2, zzm);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzc(zzaj zzaj, zzm zzm) {
        List<zzr> list;
        List<zzr> list2;
        List<zzr> list3;
        zzaj zzaj2 = zzaj;
        zzm zzm2 = zzm;
        Preconditions.checkNotNull(zzm);
        Preconditions.checkNotEmpty(zzm2.packageName);
        zzq();
        zzfy();
        String str = zzm2.packageName;
        long j = zzaj2.zzfp;
        if (zzdm().zze(zzaj2, zzm2)) {
            if (!zzm2.zzcr) {
                zzg(zzm2);
                return;
            }
            zzdo().beginTransaction();
            try {
                zzw zzdo = zzdo();
                Preconditions.checkNotEmpty(str);
                zzdo.zzq();
                zzdo.zzah();
                int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
                if (i < 0) {
                    zzdo.zzad().zzdd().zza("Invalid time querying timed out conditional properties", zzau.zzao(str), Long.valueOf(j));
                    list = Collections.emptyList();
                } else {
                    list = zzdo.zzb("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[]{str, String.valueOf(j)});
                }
                for (zzr zzr : list) {
                    if (zzr != null) {
                        this.zzl.zzad().zzdh().zza("User property timed out", zzr.packageName, this.zzl.zzaa().zzan(zzr.zzdv.name), zzr.zzdv.getValue());
                        if (zzr.zzdw != null) {
                            zzd(new zzaj(zzr.zzdw, j), zzm2);
                        }
                        zzdo().zzg(str, zzr.zzdv.name);
                    }
                }
                zzw zzdo2 = zzdo();
                Preconditions.checkNotEmpty(str);
                zzdo2.zzq();
                zzdo2.zzah();
                if (i < 0) {
                    zzdo2.zzad().zzdd().zza("Invalid time querying expired conditional properties", zzau.zzao(str), Long.valueOf(j));
                    list2 = Collections.emptyList();
                } else {
                    list2 = zzdo2.zzb("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[]{str, String.valueOf(j)});
                }
                ArrayList arrayList = new ArrayList(list2.size());
                for (zzr zzr2 : list2) {
                    if (zzr2 != null) {
                        this.zzl.zzad().zzdh().zza("User property expired", zzr2.packageName, this.zzl.zzaa().zzan(zzr2.zzdv.name), zzr2.zzdv.getValue());
                        zzdo().zzd(str, zzr2.zzdv.name);
                        if (zzr2.zzdy != null) {
                            arrayList.add(zzr2.zzdy);
                        }
                        zzdo().zzg(str, zzr2.zzdv.name);
                    }
                }
                ArrayList arrayList2 = arrayList;
                int size = arrayList2.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj = arrayList2.get(i2);
                    i2++;
                    zzd(new zzaj((zzaj) obj, j), zzm2);
                }
                zzw zzdo3 = zzdo();
                String str2 = zzaj2.name;
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotEmpty(str2);
                zzdo3.zzq();
                zzdo3.zzah();
                if (i < 0) {
                    zzdo3.zzad().zzdd().zza("Invalid time querying triggered conditional properties", zzau.zzao(str), zzdo3.zzaa().zzal(str2), Long.valueOf(j));
                    list3 = Collections.emptyList();
                } else {
                    list3 = zzdo3.zzb("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[]{str, str2, String.valueOf(j)});
                }
                ArrayList arrayList3 = new ArrayList(list3.size());
                for (zzr zzr3 : list3) {
                    if (zzr3 != null) {
                        zzga zzga = zzr3.zzdv;
                        zzgc zzgc = r4;
                        zzgc zzgc2 = new zzgc(zzr3.packageName, zzr3.origin, zzga.name, j, zzga.getValue());
                        if (zzdo().zza(zzgc)) {
                            this.zzl.zzad().zzdh().zza("User property triggered", zzr3.packageName, this.zzl.zzaa().zzan(zzgc.name), zzgc.value);
                        } else {
                            this.zzl.zzad().zzda().zza("Too many active user properties, ignoring", zzau.zzao(zzr3.packageName), this.zzl.zzaa().zzan(zzgc.name), zzgc.value);
                        }
                        if (zzr3.zzdx != null) {
                            arrayList3.add(zzr3.zzdx);
                        }
                        zzr3.zzdv = new zzga(zzgc);
                        zzr3.active = true;
                        zzdo().zza(zzr3);
                    }
                }
                zzd(zzaj, zzm);
                ArrayList arrayList4 = arrayList3;
                int size2 = arrayList4.size();
                int i3 = 0;
                while (i3 < size2) {
                    Object obj2 = arrayList4.get(i3);
                    i3++;
                    zzd(new zzaj((zzaj) obj2, j), zzm2);
                }
                zzdo().setTransactionSuccessful();
            } finally {
                zzdo().endTransaction();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:218:0x07fc A[Catch:{ IOException -> 0x0801, all -> 0x0871 }] */
    /* JADX WARNING: Removed duplicated region for block: B:223:0x082c A[Catch:{ IOException -> 0x0801, all -> 0x0871 }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0268 A[Catch:{ IOException -> 0x0801, all -> 0x0871 }] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x029f A[Catch:{ IOException -> 0x0801, all -> 0x0871 }] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x02f2 A[Catch:{ IOException -> 0x0801, all -> 0x0871 }] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x031f  */
    @androidx.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zzd(com.google.android.gms.measurement.internal.zzaj r26, com.google.android.gms.measurement.internal.zzm r27) {
        /*
            r25 = this;
            r1 = r25
            r2 = r26
            r3 = r27
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r27)
            java.lang.String r4 = r3.packageName
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r4)
            long r4 = java.lang.System.nanoTime()
            r25.zzq()
            r25.zzfy()
            java.lang.String r15 = r3.packageName
            com.google.android.gms.measurement.internal.zzfz r6 = r25.zzdm()
            boolean r6 = r6.zze(r2, r3)
            if (r6 != 0) goto L_0x0025
            return
        L_0x0025:
            boolean r6 = r3.zzcr
            if (r6 != 0) goto L_0x002d
            r1.zzg(r3)
            return
        L_0x002d:
            com.google.android.gms.measurement.internal.zzbs r6 = r25.zzdp()
            java.lang.String r7 = r2.name
            boolean r6 = r6.zzk(r15, r7)
            r14 = 0
            r21 = 1
            r13 = 0
            if (r6 == 0) goto L_0x00d8
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdd()
            java.lang.String r4 = "Dropping blacklisted event. appId"
            java.lang.Object r5 = com.google.android.gms.measurement.internal.zzau.zzao(r15)
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl
            com.google.android.gms.measurement.internal.zzas r6 = r6.zzaa()
            java.lang.String r7 = r2.name
            java.lang.String r6 = r6.zzal(r7)
            r3.zza(r4, r5, r6)
            com.google.android.gms.measurement.internal.zzbs r3 = r25.zzdp()
            boolean r3 = r3.zzbe(r15)
            if (r3 != 0) goto L_0x0070
            com.google.android.gms.measurement.internal.zzbs r3 = r25.zzdp()
            boolean r3 = r3.zzbf(r15)
            if (r3 == 0) goto L_0x0071
        L_0x0070:
            r14 = 1
        L_0x0071:
            if (r14 != 0) goto L_0x008e
            java.lang.String r3 = "_err"
            java.lang.String r4 = r2.name
            boolean r3 = r3.equals(r4)
            if (r3 != 0) goto L_0x008e
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl
            com.google.android.gms.measurement.internal.zzgd r6 = r3.zzab()
            r8 = 11
            java.lang.String r9 = "_ev"
            java.lang.String r10 = r2.name
            r11 = 0
            r7 = r15
            r6.zza(r7, r8, r9, r10, r11)
        L_0x008e:
            if (r14 == 0) goto L_0x00d7
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()
            com.google.android.gms.measurement.internal.zzg r2 = r2.zzae(r15)
            if (r2 == 0) goto L_0x00d7
            long r3 = r2.zzba()
            long r5 = r2.zzaz()
            long r3 = java.lang.Math.max(r3, r5)
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl
            com.google.android.gms.common.util.Clock r5 = r5.zzz()
            long r5 = r5.currentTimeMillis()
            long r5 = r5 - r3
            long r3 = java.lang.Math.abs(r5)
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Long> r5 = com.google.android.gms.measurement.internal.zzal.zzhc
            java.lang.Object r5 = r5.get(r13)
            java.lang.Long r5 = (java.lang.Long) r5
            long r5 = r5.longValue()
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 <= 0) goto L_0x00d7
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdh()
            java.lang.String r4 = "Fetching config for blacklisted app"
            r3.zzaq(r4)
            r1.zzb(r2)
        L_0x00d7:
            return
        L_0x00d8:
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl
            com.google.android.gms.measurement.internal.zzau r6 = r6.zzad()
            r12 = 2
            boolean r6 = r6.isLoggable(r12)
            if (r6 == 0) goto L_0x00fe
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl
            com.google.android.gms.measurement.internal.zzau r6 = r6.zzad()
            com.google.android.gms.measurement.internal.zzaw r6 = r6.zzdi()
            java.lang.String r7 = "Logging event"
            com.google.android.gms.measurement.internal.zzby r8 = r1.zzl
            com.google.android.gms.measurement.internal.zzas r8 = r8.zzaa()
            java.lang.String r8 = r8.zzb(r2)
            r6.zza(r7, r8)
        L_0x00fe:
            com.google.android.gms.measurement.internal.zzw r6 = r25.zzdo()
            r6.beginTransaction()
            r1.zzg(r3)     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = "_iap"
            java.lang.String r7 = r2.name     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.equals(r7)     // Catch:{ all -> 0x0871 }
            if (r6 != 0) goto L_0x0120
            java.lang.String r6 = "ecommerce_purchase"
            java.lang.String r7 = r2.name     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.equals(r7)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x011d
            goto L_0x0120
        L_0x011d:
            r14 = 2
            goto L_0x02ae
        L_0x0120:
            com.google.android.gms.measurement.internal.zzag r6 = r2.zzfd     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = "currency"
            java.lang.String r6 = r6.getString(r7)     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = "ecommerce_purchase"
            java.lang.String r8 = r2.name     // Catch:{ all -> 0x0871 }
            boolean r7 = r7.equals(r8)     // Catch:{ all -> 0x0871 }
            if (r7 == 0) goto L_0x0186
            com.google.android.gms.measurement.internal.zzag r7 = r2.zzfd     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = "value"
            java.lang.Double r7 = r7.zzaj(r8)     // Catch:{ all -> 0x0871 }
            double r7 = r7.doubleValue()     // Catch:{ all -> 0x0871 }
            r9 = 4696837146684686336(0x412e848000000000, double:1000000.0)
            double r7 = r7 * r9
            r16 = 0
            int r11 = (r7 > r16 ? 1 : (r7 == r16 ? 0 : -1))
            if (r11 != 0) goto L_0x015a
            com.google.android.gms.measurement.internal.zzag r7 = r2.zzfd     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = "value"
            java.lang.Long r7 = r7.getLong(r8)     // Catch:{ all -> 0x0871 }
            long r7 = r7.longValue()     // Catch:{ all -> 0x0871 }
            double r7 = (double) r7     // Catch:{ all -> 0x0871 }
            double r7 = r7 * r9
        L_0x015a:
            r9 = 4890909195324358656(0x43e0000000000000, double:9.223372036854776E18)
            int r9 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r9 > 0) goto L_0x016b
            r9 = -4332462841530417152(0xc3e0000000000000, double:-9.223372036854776E18)
            int r9 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r9 < 0) goto L_0x016b
            long r7 = java.lang.Math.round(r7)     // Catch:{ all -> 0x0871 }
            goto L_0x0192
        L_0x016b:
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r6 = r6.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r6 = r6.zzdd()     // Catch:{ all -> 0x0871 }
            java.lang.String r9 = "Data lost. Currency value is too big. appId"
            java.lang.Object r10 = com.google.android.gms.measurement.internal.zzau.zzao(r15)     // Catch:{ all -> 0x0871 }
            java.lang.Double r7 = java.lang.Double.valueOf(r7)     // Catch:{ all -> 0x0871 }
            r6.zza(r9, r10, r7)     // Catch:{ all -> 0x0871 }
            r6 = 0
            r14 = 2
            goto L_0x029d
        L_0x0186:
            com.google.android.gms.measurement.internal.zzag r7 = r2.zzfd     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = "value"
            java.lang.Long r7 = r7.getLong(r8)     // Catch:{ all -> 0x0871 }
            long r7 = r7.longValue()     // Catch:{ all -> 0x0871 }
        L_0x0192:
            boolean r9 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x0871 }
            if (r9 != 0) goto L_0x029b
            java.util.Locale r9 = java.util.Locale.US     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r6.toUpperCase(r9)     // Catch:{ all -> 0x0871 }
            java.lang.String r9 = "[A-Z]{3}"
            boolean r9 = r6.matches(r9)     // Catch:{ all -> 0x0871 }
            if (r9 == 0) goto L_0x0299
            java.lang.String r9 = "_ltv_"
            java.lang.String r9 = java.lang.String.valueOf(r9)     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = java.lang.String.valueOf(r6)     // Catch:{ all -> 0x0871 }
            int r10 = r6.length()     // Catch:{ all -> 0x0871 }
            if (r10 == 0) goto L_0x01bb
            java.lang.String r6 = r9.concat(r6)     // Catch:{ all -> 0x0871 }
            goto L_0x01c0
        L_0x01bb:
            java.lang.String r6 = new java.lang.String     // Catch:{ all -> 0x0871 }
            r6.<init>(r9)     // Catch:{ all -> 0x0871 }
        L_0x01c0:
            r9 = r6
            com.google.android.gms.measurement.internal.zzw r6 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgc r6 = r6.zze(r15, r9)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x01fa
            java.lang.Object r10 = r6.value     // Catch:{ all -> 0x0871 }
            boolean r10 = r10 instanceof java.lang.Long     // Catch:{ all -> 0x0871 }
            if (r10 != 0) goto L_0x01d2
            goto L_0x01fa
        L_0x01d2:
            java.lang.Object r6 = r6.value     // Catch:{ all -> 0x0871 }
            java.lang.Long r6 = (java.lang.Long) r6     // Catch:{ all -> 0x0871 }
            long r10 = r6.longValue()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgc r16 = new com.google.android.gms.measurement.internal.zzgc     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r2.origin     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r12 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.common.util.Clock r12 = r12.zzz()     // Catch:{ all -> 0x0871 }
            long r18 = r12.currentTimeMillis()     // Catch:{ all -> 0x0871 }
            long r10 = r10 + r7
            java.lang.Long r12 = java.lang.Long.valueOf(r10)     // Catch:{ all -> 0x0871 }
            r8 = r6
            r6 = r16
            r7 = r15
            r10 = r18
            r6.<init>(r7, r8, r9, r10, r12)     // Catch:{ all -> 0x0871 }
            r13 = r16
            r14 = 2
            goto L_0x025e
        L_0x01fa:
            com.google.android.gms.measurement.internal.zzw r6 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r10 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzt r10 = r10.zzaf()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Integer> r11 = com.google.android.gms.measurement.internal.zzal.zzhh     // Catch:{ all -> 0x0871 }
            int r10 = r10.zzb(r15, r11)     // Catch:{ all -> 0x0871 }
            int r10 = r10 + -1
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r15)     // Catch:{ all -> 0x0871 }
            r6.zzq()     // Catch:{ all -> 0x0871 }
            r6.zzah()     // Catch:{ all -> 0x0871 }
            android.database.sqlite.SQLiteDatabase r11 = r6.getWritableDatabase()     // Catch:{ SQLiteException -> 0x022f }
            java.lang.String r12 = "delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);"
            r13 = 3
            java.lang.String[] r13 = new java.lang.String[r13]     // Catch:{ SQLiteException -> 0x022f }
            r13[r14] = r15     // Catch:{ SQLiteException -> 0x022f }
            r13[r21] = r15     // Catch:{ SQLiteException -> 0x022f }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ SQLiteException -> 0x022f }
            r14 = 2
            r13[r14] = r10     // Catch:{ SQLiteException -> 0x022d }
            r11.execSQL(r12, r13)     // Catch:{ SQLiteException -> 0x022d }
            goto L_0x0243
        L_0x022d:
            r0 = move-exception
            goto L_0x0231
        L_0x022f:
            r0 = move-exception
            r14 = 2
        L_0x0231:
            r10 = r0
            com.google.android.gms.measurement.internal.zzau r6 = r6.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r6 = r6.zzda()     // Catch:{ all -> 0x0871 }
            java.lang.String r11 = "Error pruning currencies. appId"
            java.lang.Object r12 = com.google.android.gms.measurement.internal.zzau.zzao(r15)     // Catch:{ all -> 0x0871 }
            r6.zza(r11, r12, r10)     // Catch:{ all -> 0x0871 }
        L_0x0243:
            com.google.android.gms.measurement.internal.zzgc r13 = new com.google.android.gms.measurement.internal.zzgc     // Catch:{ all -> 0x0871 }
            java.lang.String r10 = r2.origin     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.common.util.Clock r6 = r6.zzz()     // Catch:{ all -> 0x0871 }
            long r11 = r6.currentTimeMillis()     // Catch:{ all -> 0x0871 }
            java.lang.Long r18 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x0871 }
            r6 = r13
            r7 = r15
            r8 = r10
            r10 = r11
            r12 = r18
            r6.<init>(r7, r8, r9, r10, r12)     // Catch:{ all -> 0x0871 }
        L_0x025e:
            com.google.android.gms.measurement.internal.zzw r6 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.zza(r13)     // Catch:{ all -> 0x0871 }
            if (r6 != 0) goto L_0x029c
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r6 = r6.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r6 = r6.zzda()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = "Too many unique user properties are set. Ignoring user property. appId"
            java.lang.Object r8 = com.google.android.gms.measurement.internal.zzau.zzao(r15)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r9 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzas r9 = r9.zzaa()     // Catch:{ all -> 0x0871 }
            java.lang.String r10 = r13.name     // Catch:{ all -> 0x0871 }
            java.lang.String r9 = r9.zzan(r10)     // Catch:{ all -> 0x0871 }
            java.lang.Object r10 = r13.value     // Catch:{ all -> 0x0871 }
            r6.zza(r7, r8, r9, r10)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r6 = r6.zzab()     // Catch:{ all -> 0x0871 }
            r8 = 9
            r9 = 0
            r10 = 0
            r11 = 0
            r7 = r15
            r6.zza(r7, r8, r9, r10, r11)     // Catch:{ all -> 0x0871 }
            goto L_0x029c
        L_0x0299:
            r14 = 2
            goto L_0x029c
        L_0x029b:
            r14 = 2
        L_0x029c:
            r6 = 1
        L_0x029d:
            if (r6 != 0) goto L_0x02ae
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            r2.setTransactionSuccessful()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()
            r2.endTransaction()
            return
        L_0x02ae:
            java.lang.String r6 = r2.name     // Catch:{ all -> 0x0871 }
            boolean r18 = com.google.android.gms.measurement.internal.zzgd.zzbm(r6)     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = "_err"
            java.lang.String r7 = r2.name     // Catch:{ all -> 0x0871 }
            boolean r19 = r6.equals(r7)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r6 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            long r7 = r25.zzfz()     // Catch:{ all -> 0x0871 }
            r10 = 1
            r12 = 0
            r20 = 0
            r9 = r15
            r11 = r18
            r13 = r19
            r23 = r4
            r4 = 0
            r5 = 2
            r14 = r20
            com.google.android.gms.measurement.internal.zzx r6 = r6.zza(r7, r9, r10, r11, r12, r13, r14)     // Catch:{ all -> 0x0871 }
            long r7 = r6.zzem     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Integer> r9 = com.google.android.gms.measurement.internal.zzal.zzgn     // Catch:{ all -> 0x0871 }
            r13 = 0
            java.lang.Object r9 = r9.get(r13)     // Catch:{ all -> 0x0871 }
            java.lang.Integer r9 = (java.lang.Integer) r9     // Catch:{ all -> 0x0871 }
            int r9 = r9.intValue()     // Catch:{ all -> 0x0871 }
            long r9 = (long) r9     // Catch:{ all -> 0x0871 }
            long r7 = r7 - r9
            r9 = 1000(0x3e8, double:4.94E-321)
            r11 = 1
            r4 = 0
            int r14 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r14 <= 0) goto L_0x031f
            long r7 = r7 % r9
            int r2 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
            if (r2 != 0) goto L_0x0310
            com.google.android.gms.measurement.internal.zzby r2 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r2 = r2.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()     // Catch:{ all -> 0x0871 }
            java.lang.String r3 = "Data loss. Too many events logged. appId, count"
            java.lang.Object r4 = com.google.android.gms.measurement.internal.zzau.zzao(r15)     // Catch:{ all -> 0x0871 }
            long r5 = r6.zzem     // Catch:{ all -> 0x0871 }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x0871 }
            r2.zza(r3, r4, r5)     // Catch:{ all -> 0x0871 }
        L_0x0310:
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            r2.setTransactionSuccessful()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()
            r2.endTransaction()
            return
        L_0x031f:
            if (r18 == 0) goto L_0x0373
            long r7 = r6.zzel     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Integer> r14 = com.google.android.gms.measurement.internal.zzal.zzgp     // Catch:{ all -> 0x0871 }
            java.lang.Object r14 = r14.get(r13)     // Catch:{ all -> 0x0871 }
            java.lang.Integer r14 = (java.lang.Integer) r14     // Catch:{ all -> 0x0871 }
            int r14 = r14.intValue()     // Catch:{ all -> 0x0871 }
            long r13 = (long) r14     // Catch:{ all -> 0x0871 }
            long r7 = r7 - r13
            int r13 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r13 <= 0) goto L_0x0373
            long r7 = r7 % r9
            int r3 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
            if (r3 != 0) goto L_0x0353
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ all -> 0x0871 }
            java.lang.String r4 = "Data loss. Too many public events logged. appId, count"
            java.lang.Object r5 = com.google.android.gms.measurement.internal.zzau.zzao(r15)     // Catch:{ all -> 0x0871 }
            long r6 = r6.zzel     // Catch:{ all -> 0x0871 }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x0871 }
            r3.zza(r4, r5, r6)     // Catch:{ all -> 0x0871 }
        L_0x0353:
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r6 = r3.zzab()     // Catch:{ all -> 0x0871 }
            r8 = 16
            java.lang.String r9 = "_ev"
            java.lang.String r10 = r2.name     // Catch:{ all -> 0x0871 }
            r11 = 0
            r7 = r15
            r6.zza(r7, r8, r9, r10, r11)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            r2.setTransactionSuccessful()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()
            r2.endTransaction()
            return
        L_0x0373:
            if (r19 == 0) goto L_0x03c3
            long r7 = r6.zzeo     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r9 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzt r9 = r9.zzaf()     // Catch:{ all -> 0x0871 }
            java.lang.String r10 = r3.packageName     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Integer> r13 = com.google.android.gms.measurement.internal.zzal.zzgo     // Catch:{ all -> 0x0871 }
            int r9 = r9.zzb(r10, r13)     // Catch:{ all -> 0x0871 }
            r10 = 1000000(0xf4240, float:1.401298E-39)
            int r9 = java.lang.Math.min(r10, r9)     // Catch:{ all -> 0x0871 }
            r13 = 0
            int r9 = java.lang.Math.max(r13, r9)     // Catch:{ all -> 0x0871 }
            long r9 = (long) r9     // Catch:{ all -> 0x0871 }
            long r7 = r7 - r9
            int r9 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r9 <= 0) goto L_0x03c4
            int r2 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
            if (r2 != 0) goto L_0x03b4
            com.google.android.gms.measurement.internal.zzby r2 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r2 = r2.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzda()     // Catch:{ all -> 0x0871 }
            java.lang.String r3 = "Too many error events logged. appId, count"
            java.lang.Object r4 = com.google.android.gms.measurement.internal.zzau.zzao(r15)     // Catch:{ all -> 0x0871 }
            long r5 = r6.zzeo     // Catch:{ all -> 0x0871 }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x0871 }
            r2.zza(r3, r4, r5)     // Catch:{ all -> 0x0871 }
        L_0x03b4:
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            r2.setTransactionSuccessful()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()
            r2.endTransaction()
            return
        L_0x03c3:
            r13 = 0
        L_0x03c4:
            com.google.android.gms.measurement.internal.zzag r6 = r2.zzfd     // Catch:{ all -> 0x0871 }
            android.os.Bundle r14 = r6.zzct()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r6 = r6.zzab()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = "_o"
            java.lang.String r8 = r2.origin     // Catch:{ all -> 0x0871 }
            r6.zza(r14, r7, r8)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r6 = r6.zzab()     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.zzbt(r15)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x0401
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r6 = r6.zzab()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = "_dbg"
            java.lang.Long r8 = java.lang.Long.valueOf(r11)     // Catch:{ all -> 0x0871 }
            r6.zza(r14, r7, r8)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r6 = r6.zzab()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = "_r"
            java.lang.Long r8 = java.lang.Long.valueOf(r11)     // Catch:{ all -> 0x0871 }
            r6.zza(r14, r7, r8)     // Catch:{ all -> 0x0871 }
        L_0x0401:
            java.lang.String r6 = "_s"
            java.lang.String r7 = r2.name     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.equals(r7)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x043a
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzt r6 = r6.zzaf()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = r3.packageName     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.zzz(r7)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x043a
            com.google.android.gms.measurement.internal.zzw r6 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = r3.packageName     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = "_sno"
            com.google.android.gms.measurement.internal.zzgc r6 = r6.zze(r7, r8)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x043a
            java.lang.Object r7 = r6.value     // Catch:{ all -> 0x0871 }
            boolean r7 = r7 instanceof java.lang.Long     // Catch:{ all -> 0x0871 }
            if (r7 == 0) goto L_0x043a
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r7 = r7.zzab()     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = "_sno"
            java.lang.Object r6 = r6.value     // Catch:{ all -> 0x0871 }
            r7.zza(r14, r8, r6)     // Catch:{ all -> 0x0871 }
        L_0x043a:
            java.lang.String r6 = "_s"
            java.lang.String r7 = r2.name     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.equals(r7)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x0472
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzt r6 = r6.zzaf()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = r3.packageName     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r8 = com.google.android.gms.measurement.internal.zzal.zzij     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.zze(r7, r8)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x0470
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzt r6 = r6.zzaf()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = r3.packageName     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.zzz(r7)     // Catch:{ all -> 0x0871 }
            if (r6 != 0) goto L_0x046e
            com.google.android.gms.measurement.internal.zzga r6 = new com.google.android.gms.measurement.internal.zzga     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = "_sno"
            r11 = 0
            r6.<init>(r7, r4, r11)     // Catch:{ all -> 0x0871 }
            r1.zzc(r6, r3)     // Catch:{ all -> 0x0871 }
            goto L_0x0473
        L_0x046e:
            r11 = 0
            goto L_0x0473
        L_0x0470:
            r11 = 0
            goto L_0x0473
        L_0x0472:
            r11 = 0
        L_0x0473:
            com.google.android.gms.measurement.internal.zzw r6 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            long r6 = r6.zzaf(r15)     // Catch:{ all -> 0x0871 }
            int r8 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r8 <= 0) goto L_0x0496
            com.google.android.gms.measurement.internal.zzby r8 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r8 = r8.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzdd()     // Catch:{ all -> 0x0871 }
            java.lang.String r9 = "Data lost. Too many events stored on disk, deleted. appId"
            java.lang.Object r10 = com.google.android.gms.measurement.internal.zzau.zzao(r15)     // Catch:{ all -> 0x0871 }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x0871 }
            r8.zza(r9, r10, r6)     // Catch:{ all -> 0x0871 }
        L_0x0496:
            com.google.android.gms.measurement.internal.zzae r12 = new com.google.android.gms.measurement.internal.zzae     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = r2.origin     // Catch:{ all -> 0x0871 }
            java.lang.String r10 = r2.name     // Catch:{ all -> 0x0871 }
            r16 = r14
            long r13 = r2.zzfp     // Catch:{ all -> 0x0871 }
            r19 = 0
            r6 = r12
            r9 = r15
            r22 = r11
            r2 = r12
            r11 = r13
            r22 = 0
            r13 = r19
            r4 = r15
            r15 = r16
            r6.<init>(r7, r8, r9, r10, r11, r13, r15)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r5 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r2.name     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaf r5 = r5.zzc(r4, r6)     // Catch:{ all -> 0x0871 }
            if (r5 != 0) goto L_0x0526
            com.google.android.gms.measurement.internal.zzw r5 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            long r5 = r5.zzai(r4)     // Catch:{ all -> 0x0871 }
            r7 = 500(0x1f4, double:2.47E-321)
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 < 0) goto L_0x050c
            if (r18 == 0) goto L_0x050c
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = "Too many event names used, ignoring event. appId, name, supported count"
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r4)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzas r7 = r7.zzaa()     // Catch:{ all -> 0x0871 }
            java.lang.String r2 = r2.name     // Catch:{ all -> 0x0871 }
            java.lang.String r2 = r7.zzal(r2)     // Catch:{ all -> 0x0871 }
            r7 = 500(0x1f4, float:7.0E-43)
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ all -> 0x0871 }
            r3.zza(r5, r6, r2, r7)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r2 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r6 = r2.zzab()     // Catch:{ all -> 0x0871 }
            r8 = 8
            r9 = 0
            r10 = 0
            r11 = 0
            r7 = r4
            r6.zza(r7, r8, r9, r10, r11)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()
            r2.endTransaction()
            return
        L_0x050c:
            com.google.android.gms.measurement.internal.zzaf r5 = new com.google.android.gms.measurement.internal.zzaf     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = r2.name     // Catch:{ all -> 0x0871 }
            r9 = 0
            r11 = 0
            long r13 = r2.timestamp     // Catch:{ all -> 0x0871 }
            r15 = 0
            r17 = 0
            r18 = 0
            r19 = 0
            r20 = 0
            r6 = r5
            r7 = r4
            r6.<init>(r7, r8, r9, r11, r13, r15, r17, r18, r19, r20)     // Catch:{ all -> 0x0871 }
            goto L_0x0535
        L_0x0526:
            com.google.android.gms.measurement.internal.zzby r4 = r1.zzl     // Catch:{ all -> 0x0871 }
            long r6 = r5.zzfg     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzae r12 = r2.zza(r4, r6)     // Catch:{ all -> 0x0871 }
            long r6 = r12.timestamp     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaf r5 = r5.zzw(r6)     // Catch:{ all -> 0x0871 }
            r2 = r12
        L_0x0535:
            com.google.android.gms.measurement.internal.zzw r4 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            r4.zza(r5)     // Catch:{ all -> 0x0871 }
            r25.zzq()     // Catch:{ all -> 0x0871 }
            r25.zzfy()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r2)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r27)     // Catch:{ all -> 0x0871 }
            java.lang.String r4 = r2.zzcf     // Catch:{ all -> 0x0871 }
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r4)     // Catch:{ all -> 0x0871 }
            java.lang.String r4 = r2.zzcf     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = r3.packageName     // Catch:{ all -> 0x0871 }
            boolean r4 = r4.equals(r5)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.common.internal.Preconditions.checkArgument(r4)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.internal.measurement.zzch r4 = new com.google.android.gms.internal.measurement.zzch     // Catch:{ all -> 0x0871 }
            r4.<init>()     // Catch:{ all -> 0x0871 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r21)     // Catch:{ all -> 0x0871 }
            r4.zzxn = r5     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = "android"
            r4.zzxv = r5     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = r3.packageName     // Catch:{ all -> 0x0871 }
            r4.zzcf = r5     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = r3.zzcp     // Catch:{ all -> 0x0871 }
            r4.zzcp = r5     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = r3.zzcn     // Catch:{ all -> 0x0871 }
            r4.zzcn = r5     // Catch:{ all -> 0x0871 }
            long r5 = r3.zzco     // Catch:{ all -> 0x0871 }
            r7 = -2147483648(0xffffffff80000000, double:NaN)
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 != 0) goto L_0x057e
            r13 = 0
            goto L_0x0585
        L_0x057e:
            long r5 = r3.zzco     // Catch:{ all -> 0x0871 }
            int r5 = (int) r5     // Catch:{ all -> 0x0871 }
            java.lang.Integer r13 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0871 }
        L_0x0585:
            r4.zzyh = r13     // Catch:{ all -> 0x0871 }
            long r5 = r3.zzt     // Catch:{ all -> 0x0871 }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x0871 }
            r4.zzxz = r5     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = r3.zzch     // Catch:{ all -> 0x0871 }
            r4.zzch = r5     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzt r5 = r5.zzaf()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r6 = com.google.android.gms.measurement.internal.zzal.zziv     // Catch:{ all -> 0x0871 }
            boolean r5 = r5.zza(r6)     // Catch:{ all -> 0x0871 }
            if (r5 == 0) goto L_0x05a9
            java.lang.String r5 = r4.zzch     // Catch:{ all -> 0x0871 }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ all -> 0x0871 }
            if (r5 == 0) goto L_0x05ad
        L_0x05a9:
            java.lang.String r5 = r3.zzcv     // Catch:{ all -> 0x0871 }
            r4.zzxf = r5     // Catch:{ all -> 0x0871 }
        L_0x05ad:
            long r5 = r3.zzcq     // Catch:{ all -> 0x0871 }
            r7 = 0
            int r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r5 != 0) goto L_0x05b7
            r13 = 0
            goto L_0x05bd
        L_0x05b7:
            long r5 = r3.zzcq     // Catch:{ all -> 0x0871 }
            java.lang.Long r13 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x0871 }
        L_0x05bd:
            r4.zzyd = r13     // Catch:{ all -> 0x0871 }
            long r5 = r3.zzu     // Catch:{ all -> 0x0871 }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x0871 }
            r4.zzys = r5     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzt r5 = r5.zzaf()     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r3.packageName     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r7 = com.google.android.gms.measurement.internal.zzal.zziq     // Catch:{ all -> 0x0871 }
            boolean r5 = r5.zze(r6, r7)     // Catch:{ all -> 0x0871 }
            if (r5 == 0) goto L_0x05e1
            com.google.android.gms.measurement.internal.zzfz r5 = r25.zzdm()     // Catch:{ all -> 0x0871 }
            int[] r5 = r5.zzgj()     // Catch:{ all -> 0x0871 }
            r4.zzyr = r5     // Catch:{ all -> 0x0871 }
        L_0x05e1:
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzbf r5 = r5.zzae()     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r3.packageName     // Catch:{ all -> 0x0871 }
            android.util.Pair r5 = r5.zzar(r6)     // Catch:{ all -> 0x0871 }
            if (r5 == 0) goto L_0x060a
            java.lang.Object r6 = r5.first     // Catch:{ all -> 0x0871 }
            java.lang.CharSequence r6 = (java.lang.CharSequence) r6     // Catch:{ all -> 0x0871 }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x0871 }
            if (r6 != 0) goto L_0x060a
            boolean r6 = r3.zzct     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x0667
            java.lang.Object r6 = r5.first     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ all -> 0x0871 }
            r4.zzyb = r6     // Catch:{ all -> 0x0871 }
            java.lang.Object r5 = r5.second     // Catch:{ all -> 0x0871 }
            java.lang.Boolean r5 = (java.lang.Boolean) r5     // Catch:{ all -> 0x0871 }
            r4.zzyc = r5     // Catch:{ all -> 0x0871 }
            goto L_0x0667
        L_0x060a:
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzad r5 = r5.zzy()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            android.content.Context r6 = r6.getContext()     // Catch:{ all -> 0x0871 }
            boolean r5 = r5.zzj(r6)     // Catch:{ all -> 0x0871 }
            if (r5 != 0) goto L_0x0667
            boolean r5 = r3.zzcu     // Catch:{ all -> 0x0871 }
            if (r5 == 0) goto L_0x0667
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            android.content.Context r5 = r5.getContext()     // Catch:{ all -> 0x0871 }
            android.content.ContentResolver r5 = r5.getContentResolver()     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = "android_id"
            java.lang.String r5 = android.provider.Settings.Secure.getString(r5, r6)     // Catch:{ all -> 0x0871 }
            if (r5 != 0) goto L_0x064a
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r5 = r5.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzdd()     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = "null secure ID. appId"
            java.lang.String r7 = r4.zzcf     // Catch:{ all -> 0x0871 }
            java.lang.Object r7 = com.google.android.gms.measurement.internal.zzau.zzao(r7)     // Catch:{ all -> 0x0871 }
            r5.zza(r6, r7)     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = "null"
            goto L_0x0665
        L_0x064a:
            boolean r6 = r5.isEmpty()     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x0665
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r6 = r6.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r6 = r6.zzdd()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = "empty secure ID. appId"
            java.lang.String r8 = r4.zzcf     // Catch:{ all -> 0x0871 }
            java.lang.Object r8 = com.google.android.gms.measurement.internal.zzau.zzao(r8)     // Catch:{ all -> 0x0871 }
            r6.zza(r7, r8)     // Catch:{ all -> 0x0871 }
        L_0x0665:
            r4.zzyk = r5     // Catch:{ all -> 0x0871 }
        L_0x0667:
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzad r5 = r5.zzy()     // Catch:{ all -> 0x0871 }
            r5.zzah()     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = android.os.Build.MODEL     // Catch:{ all -> 0x0871 }
            r4.zzxx = r5     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzad r5 = r5.zzy()     // Catch:{ all -> 0x0871 }
            r5.zzah()     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = android.os.Build.VERSION.RELEASE     // Catch:{ all -> 0x0871 }
            r4.zzxw = r5     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzad r5 = r5.zzy()     // Catch:{ all -> 0x0871 }
            long r5 = r5.zzco()     // Catch:{ all -> 0x0871 }
            int r5 = (int) r5     // Catch:{ all -> 0x0871 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0871 }
            r4.zzxy = r5     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzad r5 = r5.zzy()     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = r5.zzcp()     // Catch:{ all -> 0x0871 }
            r4.zzex = r5     // Catch:{ all -> 0x0871 }
            r5 = 0
            r4.zzya = r5     // Catch:{ all -> 0x0871 }
            r4.zzxq = r5     // Catch:{ all -> 0x0871 }
            r4.zzxr = r5     // Catch:{ all -> 0x0871 }
            r4.zzxs = r5     // Catch:{ all -> 0x0871 }
            long r6 = r3.zzcs     // Catch:{ all -> 0x0871 }
            java.lang.Long r6 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x0871 }
            r4.zzym = r6     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.isEnabled()     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x06bf
            boolean r6 = com.google.android.gms.measurement.internal.zzt.zzbv()     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x06bf
            r4.zzyn = r5     // Catch:{ all -> 0x0871 }
        L_0x06bf:
            com.google.android.gms.measurement.internal.zzw r5 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r3.packageName     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzg r5 = r5.zzae(r6)     // Catch:{ all -> 0x0871 }
            if (r5 != 0) goto L_0x0734
            com.google.android.gms.measurement.internal.zzg r5 = new com.google.android.gms.measurement.internal.zzg     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = r3.packageName     // Catch:{ all -> 0x0871 }
            r5.<init>(r6, r7)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgd r6 = r6.zzab()     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r6.zzgn()     // Catch:{ all -> 0x0871 }
            r5.zza(r6)     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r3.zzcj     // Catch:{ all -> 0x0871 }
            r5.zze(r6)     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r3.zzch     // Catch:{ all -> 0x0871 }
            r5.zzb(r6)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzbf r6 = r6.zzae()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = r3.packageName     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r6.zzas(r7)     // Catch:{ all -> 0x0871 }
            r5.zzd(r6)     // Catch:{ all -> 0x0871 }
            r6 = 0
            r5.zzk(r6)     // Catch:{ all -> 0x0871 }
            r5.zze(r6)     // Catch:{ all -> 0x0871 }
            r5.zzf(r6)     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r3.zzcn     // Catch:{ all -> 0x0871 }
            r5.zzf(r6)     // Catch:{ all -> 0x0871 }
            long r6 = r3.zzco     // Catch:{ all -> 0x0871 }
            r5.zzg(r6)     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = r3.zzcp     // Catch:{ all -> 0x0871 }
            r5.zzg(r6)     // Catch:{ all -> 0x0871 }
            long r6 = r3.zzt     // Catch:{ all -> 0x0871 }
            r5.zzh(r6)     // Catch:{ all -> 0x0871 }
            long r6 = r3.zzcq     // Catch:{ all -> 0x0871 }
            r5.zzi(r6)     // Catch:{ all -> 0x0871 }
            boolean r6 = r3.zzcr     // Catch:{ all -> 0x0871 }
            r5.setMeasurementEnabled(r6)     // Catch:{ all -> 0x0871 }
            long r6 = r3.zzcs     // Catch:{ all -> 0x0871 }
            r5.zzt(r6)     // Catch:{ all -> 0x0871 }
            long r6 = r3.zzu     // Catch:{ all -> 0x0871 }
            r5.zzj(r6)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r6 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            r6.zza(r5)     // Catch:{ all -> 0x0871 }
        L_0x0734:
            java.lang.String r6 = r5.getAppInstanceId()     // Catch:{ all -> 0x0871 }
            r4.zzcg = r6     // Catch:{ all -> 0x0871 }
            java.lang.String r5 = r5.getFirebaseInstanceId()     // Catch:{ all -> 0x0871 }
            r4.zzcj = r5     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r5 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            java.lang.String r3 = r3.packageName     // Catch:{ all -> 0x0871 }
            java.util.List r3 = r5.zzad(r3)     // Catch:{ all -> 0x0871 }
            int r5 = r3.size()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r5 = new com.google.android.gms.internal.measurement.zzbt.zzh[r5]     // Catch:{ all -> 0x0871 }
            r4.zzxp = r5     // Catch:{ all -> 0x0871 }
            r5 = 0
        L_0x0753:
            int r6 = r3.size()     // Catch:{ all -> 0x0871 }
            if (r5 >= r6) goto L_0x0793
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r6 = com.google.android.gms.internal.measurement.zzbt.zzh.zziu()     // Catch:{ all -> 0x0871 }
            java.lang.Object r7 = r3.get(r5)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgc r7 = (com.google.android.gms.measurement.internal.zzgc) r7     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = r7.name     // Catch:{ all -> 0x0871 }
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r6 = r6.zzby(r7)     // Catch:{ all -> 0x0871 }
            java.lang.Object r7 = r3.get(r5)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgc r7 = (com.google.android.gms.measurement.internal.zzgc) r7     // Catch:{ all -> 0x0871 }
            long r7 = r7.zzsx     // Catch:{ all -> 0x0871 }
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r6 = r6.zzan(r7)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzfz r7 = r25.zzdm()     // Catch:{ all -> 0x0871 }
            java.lang.Object r8 = r3.get(r5)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzgc r8 = (com.google.android.gms.measurement.internal.zzgc) r8     // Catch:{ all -> 0x0871 }
            java.lang.Object r8 = r8.value     // Catch:{ all -> 0x0871 }
            r7.zza(r6, r8)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r7 = r4.zzxp     // Catch:{ all -> 0x0871 }
            com.google.android.gms.internal.measurement.zzgh r6 = r6.zzmr()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.internal.measurement.zzez r6 = (com.google.android.gms.internal.measurement.zzez) r6     // Catch:{ all -> 0x0871 }
            com.google.android.gms.internal.measurement.zzbt$zzh r6 = (com.google.android.gms.internal.measurement.zzbt.zzh) r6     // Catch:{ all -> 0x0871 }
            r7[r5] = r6     // Catch:{ all -> 0x0871 }
            int r5 = r5 + 1
            goto L_0x0753
        L_0x0793:
            com.google.android.gms.measurement.internal.zzw r3 = r25.zzdo()     // Catch:{ IOException -> 0x0801 }
            long r3 = r3.zza(r4)     // Catch:{ IOException -> 0x0801 }
            com.google.android.gms.measurement.internal.zzw r5 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzag r6 = r2.zzfd     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x07f5
            com.google.android.gms.measurement.internal.zzag r6 = r2.zzfd     // Catch:{ all -> 0x0871 }
            java.util.Iterator r6 = r6.iterator()     // Catch:{ all -> 0x0871 }
        L_0x07a9:
            boolean r7 = r6.hasNext()     // Catch:{ all -> 0x0871 }
            if (r7 == 0) goto L_0x07bf
            java.lang.Object r7 = r6.next()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = "_r"
            boolean r7 = r8.equals(r7)     // Catch:{ all -> 0x0871 }
            if (r7 == 0) goto L_0x07a9
            r6 = 1
            goto L_0x07f6
        L_0x07bf:
            com.google.android.gms.measurement.internal.zzbs r6 = r25.zzdp()     // Catch:{ all -> 0x0871 }
            java.lang.String r7 = r2.zzcf     // Catch:{ all -> 0x0871 }
            java.lang.String r8 = r2.name     // Catch:{ all -> 0x0871 }
            boolean r6 = r6.zzl(r7, r8)     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzw r7 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            long r8 = r25.zzfz()     // Catch:{ all -> 0x0871 }
            java.lang.String r10 = r2.zzcf     // Catch:{ all -> 0x0871 }
            r11 = 0
            r12 = 0
            r13 = 0
            r14 = 0
            r15 = 0
            com.google.android.gms.measurement.internal.zzx r7 = r7.zza(r8, r10, r11, r12, r13, r14, r15)     // Catch:{ all -> 0x0871 }
            if (r6 == 0) goto L_0x07f5
            long r6 = r7.zzep     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r8 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzt r8 = r8.zzaf()     // Catch:{ all -> 0x0871 }
            java.lang.String r9 = r2.zzcf     // Catch:{ all -> 0x0871 }
            int r8 = r8.zzi(r9)     // Catch:{ all -> 0x0871 }
            long r8 = (long) r8     // Catch:{ all -> 0x0871 }
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x07f5
            r6 = 1
            goto L_0x07f6
        L_0x07f5:
            r6 = 0
        L_0x07f6:
            boolean r3 = r5.zza(r2, r3, r6)     // Catch:{ all -> 0x0871 }
            if (r3 == 0) goto L_0x0818
            r3 = 0
            r1.zzse = r3     // Catch:{ all -> 0x0871 }
            goto L_0x0818
        L_0x0801:
            r0 = move-exception
            r3 = r0
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r5 = r5.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzda()     // Catch:{ all -> 0x0871 }
            java.lang.String r6 = "Data loss. Failed to insert raw event metadata. appId"
            java.lang.String r4 = r4.zzcf     // Catch:{ all -> 0x0871 }
            java.lang.Object r4 = com.google.android.gms.measurement.internal.zzau.zzao(r4)     // Catch:{ all -> 0x0871 }
            r5.zza(r6, r4, r3)     // Catch:{ all -> 0x0871 }
        L_0x0818:
            com.google.android.gms.measurement.internal.zzw r3 = r25.zzdo()     // Catch:{ all -> 0x0871 }
            r3.setTransactionSuccessful()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0871 }
            r4 = 2
            boolean r3 = r3.isLoggable(r4)     // Catch:{ all -> 0x0871 }
            if (r3 == 0) goto L_0x0845
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdi()     // Catch:{ all -> 0x0871 }
            java.lang.String r4 = "Event recorded"
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0871 }
            com.google.android.gms.measurement.internal.zzas r5 = r5.zzaa()     // Catch:{ all -> 0x0871 }
            java.lang.String r2 = r5.zza(r2)     // Catch:{ all -> 0x0871 }
            r3.zza(r4, r2)     // Catch:{ all -> 0x0871 }
        L_0x0845:
            com.google.android.gms.measurement.internal.zzw r2 = r25.zzdo()
            r2.endTransaction()
            r25.zzgc()
            com.google.android.gms.measurement.internal.zzby r2 = r1.zzl
            com.google.android.gms.measurement.internal.zzau r2 = r2.zzad()
            com.google.android.gms.measurement.internal.zzaw r2 = r2.zzdi()
            java.lang.String r3 = "Background event processing time, ms"
            long r4 = java.lang.System.nanoTime()
            long r4 = r4 - r23
            r6 = 500000(0x7a120, double:2.47033E-318)
            long r4 = r4 + r6
            r6 = 1000000(0xf4240, double:4.940656E-318)
            long r4 = r4 / r6
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            r2.zza(r3, r4)
            return
        L_0x0871:
            r0 = move-exception
            r2 = r0
            com.google.android.gms.measurement.internal.zzw r3 = r25.zzdo()
            r3.endTransaction()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzft.zzd(com.google.android.gms.measurement.internal.zzaj, com.google.android.gms.measurement.internal.zzm):void");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:96|97) */
    /* JADX WARNING: Code restructure failed: missing block: B:97:?, code lost:
        r1.zzl.zzad().zzda().zza("Failed to parse upload URL. Not uploading. appId", com.google.android.gms.measurement.internal.zzau.zzao(r5), r9);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:96:0x02ca */
    @androidx.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzga() {
        /*
            r17 = this;
            r1 = r17
            r17.zzq()
            r17.zzfy()
            r0 = 1
            r1.zzsk = r0
            r2 = 0
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0306 }
            r3.zzag()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzeg r3 = r3.zzu()     // Catch:{ all -> 0x0306 }
            java.lang.Boolean r3 = r3.zzfi()     // Catch:{ all -> 0x0306 }
            if (r3 != 0) goto L_0x0032
            com.google.android.gms.measurement.internal.zzby r0 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzau r0 = r0.zzad()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdd()     // Catch:{ all -> 0x0306 }
            java.lang.String r3 = "Upload data called on the client side before use of service was decided"
            r0.zzaq(r3)     // Catch:{ all -> 0x0306 }
            r1.zzsk = r2
            r17.zzgd()
            return
        L_0x0032:
            boolean r3 = r3.booleanValue()     // Catch:{ all -> 0x0306 }
            if (r3 == 0) goto L_0x004d
            com.google.android.gms.measurement.internal.zzby r0 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzau r0 = r0.zzad()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ all -> 0x0306 }
            java.lang.String r3 = "Upload called in the client side when service should be used"
            r0.zzaq(r3)     // Catch:{ all -> 0x0306 }
            r1.zzsk = r2
            r17.zzgd()
            return
        L_0x004d:
            long r3 = r1.zzse     // Catch:{ all -> 0x0306 }
            r5 = 0
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 <= 0) goto L_0x005e
            r17.zzgc()     // Catch:{ all -> 0x0306 }
            r1.zzsk = r2
            r17.zzgd()
            return
        L_0x005e:
            r17.zzq()     // Catch:{ all -> 0x0306 }
            java.util.List<java.lang.Long> r3 = r1.zzsn     // Catch:{ all -> 0x0306 }
            if (r3 == 0) goto L_0x0067
            r3 = 1
            goto L_0x0068
        L_0x0067:
            r3 = 0
        L_0x0068:
            if (r3 == 0) goto L_0x007f
            com.google.android.gms.measurement.internal.zzby r0 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzau r0 = r0.zzad()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdi()     // Catch:{ all -> 0x0306 }
            java.lang.String r3 = "Uploading requested multiple times"
            r0.zzaq(r3)     // Catch:{ all -> 0x0306 }
            r1.zzsk = r2
            r17.zzgd()
            return
        L_0x007f:
            com.google.android.gms.measurement.internal.zzay r3 = r17.zzfu()     // Catch:{ all -> 0x0306 }
            boolean r3 = r3.zzdl()     // Catch:{ all -> 0x0306 }
            if (r3 != 0) goto L_0x00a1
            com.google.android.gms.measurement.internal.zzby r0 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzau r0 = r0.zzad()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzdi()     // Catch:{ all -> 0x0306 }
            java.lang.String r3 = "Network not connected, ignoring upload request"
            r0.zzaq(r3)     // Catch:{ all -> 0x0306 }
            r17.zzgc()     // Catch:{ all -> 0x0306 }
            r1.zzsk = r2
            r17.zzgd()
            return
        L_0x00a1:
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.common.util.Clock r3 = r3.zzz()     // Catch:{ all -> 0x0306 }
            long r3 = r3.currentTimeMillis()     // Catch:{ all -> 0x0306 }
            long r7 = com.google.android.gms.measurement.internal.zzt.zzbt()     // Catch:{ all -> 0x0306 }
            long r7 = r3 - r7
            r9 = 0
            r1.zzd(r9, r7)     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzbf r7 = r7.zzae()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzbi r7 = r7.zzlb     // Catch:{ all -> 0x0306 }
            long r7 = r7.get()     // Catch:{ all -> 0x0306 }
            int r5 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r5 == 0) goto L_0x00de
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzau r5 = r5.zzad()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzdh()     // Catch:{ all -> 0x0306 }
            java.lang.String r6 = "Uploading events. Elapsed time since last upload attempt (ms)"
            long r7 = r3 - r7
            long r7 = java.lang.Math.abs(r7)     // Catch:{ all -> 0x0306 }
            java.lang.Long r7 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x0306 }
            r5.zza(r6, r7)     // Catch:{ all -> 0x0306 }
        L_0x00de:
            com.google.android.gms.measurement.internal.zzw r5 = r17.zzdo()     // Catch:{ all -> 0x0306 }
            java.lang.String r5 = r5.zzby()     // Catch:{ all -> 0x0306 }
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch:{ all -> 0x0306 }
            r7 = -1
            if (r6 != 0) goto L_0x02de
            long r10 = r1.zzsp     // Catch:{ all -> 0x0306 }
            int r6 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
            if (r6 != 0) goto L_0x00fe
            com.google.android.gms.measurement.internal.zzw r6 = r17.zzdo()     // Catch:{ all -> 0x0306 }
            long r6 = r6.zzcf()     // Catch:{ all -> 0x0306 }
            r1.zzsp = r6     // Catch:{ all -> 0x0306 }
        L_0x00fe:
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzt r6 = r6.zzaf()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Integer> r7 = com.google.android.gms.measurement.internal.zzal.zzgj     // Catch:{ all -> 0x0306 }
            int r6 = r6.zzb(r5, r7)     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzt r7 = r7.zzaf()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Integer> r8 = com.google.android.gms.measurement.internal.zzal.zzgk     // Catch:{ all -> 0x0306 }
            int r7 = r7.zzb(r5, r8)     // Catch:{ all -> 0x0306 }
            int r7 = java.lang.Math.max(r2, r7)     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzw r8 = r17.zzdo()     // Catch:{ all -> 0x0306 }
            java.util.List r6 = r8.zza(r5, r6, r7)     // Catch:{ all -> 0x0306 }
            boolean r7 = r6.isEmpty()     // Catch:{ all -> 0x0306 }
            if (r7 != 0) goto L_0x0300
            java.util.Iterator r7 = r6.iterator()     // Catch:{ all -> 0x0306 }
        L_0x012c:
            boolean r8 = r7.hasNext()     // Catch:{ all -> 0x0306 }
            if (r8 == 0) goto L_0x0147
            java.lang.Object r8 = r7.next()     // Catch:{ all -> 0x0306 }
            android.util.Pair r8 = (android.util.Pair) r8     // Catch:{ all -> 0x0306 }
            java.lang.Object r8 = r8.first     // Catch:{ all -> 0x0306 }
            com.google.android.gms.internal.measurement.zzch r8 = (com.google.android.gms.internal.measurement.zzch) r8     // Catch:{ all -> 0x0306 }
            java.lang.String r10 = r8.zzyb     // Catch:{ all -> 0x0306 }
            boolean r10 = android.text.TextUtils.isEmpty(r10)     // Catch:{ all -> 0x0306 }
            if (r10 != 0) goto L_0x012c
            java.lang.String r7 = r8.zzyb     // Catch:{ all -> 0x0306 }
            goto L_0x0148
        L_0x0147:
            r7 = r9
        L_0x0148:
            if (r7 == 0) goto L_0x0173
            r8 = 0
        L_0x014b:
            int r10 = r6.size()     // Catch:{ all -> 0x0306 }
            if (r8 >= r10) goto L_0x0173
            java.lang.Object r10 = r6.get(r8)     // Catch:{ all -> 0x0306 }
            android.util.Pair r10 = (android.util.Pair) r10     // Catch:{ all -> 0x0306 }
            java.lang.Object r10 = r10.first     // Catch:{ all -> 0x0306 }
            com.google.android.gms.internal.measurement.zzch r10 = (com.google.android.gms.internal.measurement.zzch) r10     // Catch:{ all -> 0x0306 }
            java.lang.String r11 = r10.zzyb     // Catch:{ all -> 0x0306 }
            boolean r11 = android.text.TextUtils.isEmpty(r11)     // Catch:{ all -> 0x0306 }
            if (r11 != 0) goto L_0x0170
            java.lang.String r10 = r10.zzyb     // Catch:{ all -> 0x0306 }
            boolean r10 = r10.equals(r7)     // Catch:{ all -> 0x0306 }
            if (r10 != 0) goto L_0x0170
            java.util.List r6 = r6.subList(r2, r8)     // Catch:{ all -> 0x0306 }
            goto L_0x0173
        L_0x0170:
            int r8 = r8 + 1
            goto L_0x014b
        L_0x0173:
            com.google.android.gms.internal.measurement.zzcg r7 = new com.google.android.gms.internal.measurement.zzcg     // Catch:{ all -> 0x0306 }
            r7.<init>()     // Catch:{ all -> 0x0306 }
            int r8 = r6.size()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.internal.measurement.zzch[] r8 = new com.google.android.gms.internal.measurement.zzch[r8]     // Catch:{ all -> 0x0306 }
            r7.zzxl = r8     // Catch:{ all -> 0x0306 }
            java.util.ArrayList r8 = new java.util.ArrayList     // Catch:{ all -> 0x0306 }
            int r10 = r6.size()     // Catch:{ all -> 0x0306 }
            r8.<init>(r10)     // Catch:{ all -> 0x0306 }
            boolean r10 = com.google.android.gms.measurement.internal.zzt.zzbv()     // Catch:{ all -> 0x0306 }
            if (r10 == 0) goto L_0x019d
            com.google.android.gms.measurement.internal.zzby r10 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzt r10 = r10.zzaf()     // Catch:{ all -> 0x0306 }
            boolean r10 = r10.zzk(r5)     // Catch:{ all -> 0x0306 }
            if (r10 == 0) goto L_0x019d
            r10 = 1
            goto L_0x019e
        L_0x019d:
            r10 = 0
        L_0x019e:
            r11 = 0
        L_0x019f:
            com.google.android.gms.internal.measurement.zzch[] r12 = r7.zzxl     // Catch:{ all -> 0x0306 }
            int r12 = r12.length     // Catch:{ all -> 0x0306 }
            if (r11 >= r12) goto L_0x021f
            com.google.android.gms.internal.measurement.zzch[] r12 = r7.zzxl     // Catch:{ all -> 0x0306 }
            java.lang.Object r13 = r6.get(r11)     // Catch:{ all -> 0x0306 }
            android.util.Pair r13 = (android.util.Pair) r13     // Catch:{ all -> 0x0306 }
            java.lang.Object r13 = r13.first     // Catch:{ all -> 0x0306 }
            com.google.android.gms.internal.measurement.zzch r13 = (com.google.android.gms.internal.measurement.zzch) r13     // Catch:{ all -> 0x0306 }
            r12[r11] = r13     // Catch:{ all -> 0x0306 }
            java.lang.Object r12 = r6.get(r11)     // Catch:{ all -> 0x0306 }
            android.util.Pair r12 = (android.util.Pair) r12     // Catch:{ all -> 0x0306 }
            java.lang.Object r12 = r12.second     // Catch:{ all -> 0x0306 }
            java.lang.Long r12 = (java.lang.Long) r12     // Catch:{ all -> 0x0306 }
            r8.add(r12)     // Catch:{ all -> 0x0306 }
            com.google.android.gms.internal.measurement.zzch[] r12 = r7.zzxl     // Catch:{ all -> 0x0306 }
            r12 = r12[r11]     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzby r13 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzt r13 = r13.zzaf()     // Catch:{ all -> 0x0306 }
            long r13 = r13.zzav()     // Catch:{ all -> 0x0306 }
            java.lang.Long r13 = java.lang.Long.valueOf(r13)     // Catch:{ all -> 0x0306 }
            r12.zzya = r13     // Catch:{ all -> 0x0306 }
            com.google.android.gms.internal.measurement.zzch[] r12 = r7.zzxl     // Catch:{ all -> 0x0306 }
            r12 = r12[r11]     // Catch:{ all -> 0x0306 }
            java.lang.Long r13 = java.lang.Long.valueOf(r3)     // Catch:{ all -> 0x0306 }
            r12.zzxq = r13     // Catch:{ all -> 0x0306 }
            com.google.android.gms.internal.measurement.zzch[] r12 = r7.zzxl     // Catch:{ all -> 0x0306 }
            r12 = r12[r11]     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzby r13 = r1.zzl     // Catch:{ all -> 0x0306 }
            r13.zzag()     // Catch:{ all -> 0x0306 }
            java.lang.Boolean r13 = java.lang.Boolean.valueOf(r2)     // Catch:{ all -> 0x0306 }
            r12.zzyf = r13     // Catch:{ all -> 0x0306 }
            if (r10 != 0) goto L_0x01f4
            com.google.android.gms.internal.measurement.zzch[] r12 = r7.zzxl     // Catch:{ all -> 0x0306 }
            r12 = r12[r11]     // Catch:{ all -> 0x0306 }
            r12.zzyn = r9     // Catch:{ all -> 0x0306 }
        L_0x01f4:
            com.google.android.gms.measurement.internal.zzby r12 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzt r12 = r12.zzaf()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r13 = com.google.android.gms.measurement.internal.zzal.zziu     // Catch:{ all -> 0x0306 }
            boolean r12 = r12.zze(r5, r13)     // Catch:{ all -> 0x0306 }
            if (r12 == 0) goto L_0x021c
            com.google.android.gms.internal.measurement.zzch[] r12 = r7.zzxl     // Catch:{ all -> 0x0306 }
            r12 = r12[r11]     // Catch:{ all -> 0x0306 }
            byte[] r12 = com.google.android.gms.internal.measurement.zzch.zzb(r12)     // Catch:{ all -> 0x0306 }
            com.google.android.gms.internal.measurement.zzch[] r13 = r7.zzxl     // Catch:{ all -> 0x0306 }
            r13 = r13[r11]     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzfz r14 = r17.zzdm()     // Catch:{ all -> 0x0306 }
            long r14 = r14.zza(r12)     // Catch:{ all -> 0x0306 }
            java.lang.Long r12 = java.lang.Long.valueOf(r14)     // Catch:{ all -> 0x0306 }
            r13.zzyt = r12     // Catch:{ all -> 0x0306 }
        L_0x021c:
            int r11 = r11 + 1
            goto L_0x019f
        L_0x021f:
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzau r6 = r6.zzad()     // Catch:{ all -> 0x0306 }
            r10 = 2
            boolean r6 = r6.isLoggable(r10)     // Catch:{ all -> 0x0306 }
            if (r6 == 0) goto L_0x0235
            com.google.android.gms.measurement.internal.zzfz r6 = r17.zzdm()     // Catch:{ all -> 0x0306 }
            java.lang.String r6 = r6.zzb(r7)     // Catch:{ all -> 0x0306 }
            goto L_0x0236
        L_0x0235:
            r6 = r9
        L_0x0236:
            com.google.android.gms.measurement.internal.zzfz r10 = r17.zzdm()     // Catch:{ all -> 0x0306 }
            byte[] r14 = r10.zza(r7)     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.String> r10 = com.google.android.gms.measurement.internal.zzal.zzgt     // Catch:{ all -> 0x0306 }
            java.lang.Object r9 = r10.get(r9)     // Catch:{ all -> 0x0306 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ all -> 0x0306 }
            java.net.URL r13 = new java.net.URL     // Catch:{ MalformedURLException -> 0x02ca }
            r13.<init>(r9)     // Catch:{ MalformedURLException -> 0x02ca }
            boolean r10 = r8.isEmpty()     // Catch:{ MalformedURLException -> 0x02ca }
            if (r10 != 0) goto L_0x0253
            r10 = 1
            goto L_0x0254
        L_0x0253:
            r10 = 0
        L_0x0254:
            com.google.android.gms.common.internal.Preconditions.checkArgument(r10)     // Catch:{ MalformedURLException -> 0x02ca }
            java.util.List<java.lang.Long> r10 = r1.zzsn     // Catch:{ MalformedURLException -> 0x02ca }
            if (r10 == 0) goto L_0x026b
            com.google.android.gms.measurement.internal.zzby r8 = r1.zzl     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzau r8 = r8.zzad()     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzda()     // Catch:{ MalformedURLException -> 0x02ca }
            java.lang.String r10 = "Set uploading progress before finishing the previous upload"
            r8.zzaq(r10)     // Catch:{ MalformedURLException -> 0x02ca }
            goto L_0x0272
        L_0x026b:
            java.util.ArrayList r10 = new java.util.ArrayList     // Catch:{ MalformedURLException -> 0x02ca }
            r10.<init>(r8)     // Catch:{ MalformedURLException -> 0x02ca }
            r1.zzsn = r10     // Catch:{ MalformedURLException -> 0x02ca }
        L_0x0272:
            com.google.android.gms.measurement.internal.zzby r8 = r1.zzl     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzbf r8 = r8.zzae()     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzbi r8 = r8.zzlc     // Catch:{ MalformedURLException -> 0x02ca }
            r8.set(r3)     // Catch:{ MalformedURLException -> 0x02ca }
            java.lang.String r3 = "?"
            com.google.android.gms.internal.measurement.zzch[] r4 = r7.zzxl     // Catch:{ MalformedURLException -> 0x02ca }
            int r4 = r4.length     // Catch:{ MalformedURLException -> 0x02ca }
            if (r4 <= 0) goto L_0x028a
            com.google.android.gms.internal.measurement.zzch[] r3 = r7.zzxl     // Catch:{ MalformedURLException -> 0x02ca }
            r3 = r3[r2]     // Catch:{ MalformedURLException -> 0x02ca }
            java.lang.String r3 = r3.zzcf     // Catch:{ MalformedURLException -> 0x02ca }
        L_0x028a:
            com.google.android.gms.measurement.internal.zzby r4 = r1.zzl     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzau r4 = r4.zzad()     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzdi()     // Catch:{ MalformedURLException -> 0x02ca }
            java.lang.String r7 = "Uploading data. app, uncompressed size, data"
            int r8 = r14.length     // Catch:{ MalformedURLException -> 0x02ca }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ MalformedURLException -> 0x02ca }
            r4.zza(r7, r3, r8, r6)     // Catch:{ MalformedURLException -> 0x02ca }
            r1.zzsj = r0     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzay r11 = r17.zzfu()     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzfv r0 = new com.google.android.gms.measurement.internal.zzfv     // Catch:{ MalformedURLException -> 0x02ca }
            r0.<init>(r1, r5)     // Catch:{ MalformedURLException -> 0x02ca }
            r11.zzq()     // Catch:{ MalformedURLException -> 0x02ca }
            r11.zzah()     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r13)     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r14)     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r0)     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzbt r3 = r11.zzac()     // Catch:{ MalformedURLException -> 0x02ca }
            com.google.android.gms.measurement.internal.zzbc r4 = new com.google.android.gms.measurement.internal.zzbc     // Catch:{ MalformedURLException -> 0x02ca }
            r15 = 0
            r10 = r4
            r12 = r5
            r16 = r0
            r10.<init>(r11, r12, r13, r14, r15, r16)     // Catch:{ MalformedURLException -> 0x02ca }
            r3.zzb(r4)     // Catch:{ MalformedURLException -> 0x02ca }
            goto L_0x0300
        L_0x02ca:
            com.google.android.gms.measurement.internal.zzby r0 = r1.zzl     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzau r0 = r0.zzad()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzaw r0 = r0.zzda()     // Catch:{ all -> 0x0306 }
            java.lang.String r3 = "Failed to parse upload URL. Not uploading. appId"
            java.lang.Object r4 = com.google.android.gms.measurement.internal.zzau.zzao(r5)     // Catch:{ all -> 0x0306 }
            r0.zza(r3, r4, r9)     // Catch:{ all -> 0x0306 }
            goto L_0x0300
        L_0x02de:
            r1.zzsp = r7     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzw r0 = r17.zzdo()     // Catch:{ all -> 0x0306 }
            long r5 = com.google.android.gms.measurement.internal.zzt.zzbt()     // Catch:{ all -> 0x0306 }
            long r3 = r3 - r5
            java.lang.String r0 = r0.zzu(r3)     // Catch:{ all -> 0x0306 }
            boolean r3 = android.text.TextUtils.isEmpty(r0)     // Catch:{ all -> 0x0306 }
            if (r3 != 0) goto L_0x0300
            com.google.android.gms.measurement.internal.zzw r3 = r17.zzdo()     // Catch:{ all -> 0x0306 }
            com.google.android.gms.measurement.internal.zzg r0 = r3.zzae(r0)     // Catch:{ all -> 0x0306 }
            if (r0 == 0) goto L_0x0300
            r1.zzb(r0)     // Catch:{ all -> 0x0306 }
        L_0x0300:
            r1.zzsk = r2
            r17.zzgd()
            return
        L_0x0306:
            r0 = move-exception
            r1.zzsk = r2
            r17.zzgd()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzft.zzga():void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0040, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0041, code lost:
        r4 = r1;
        r22 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0046, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x025b, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:144:0x025c, code lost:
        r6 = r3;
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0047, code lost:
        r6 = null;
        r7 = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0040 A[ExcHandler: all (th java.lang.Throwable), PHI: r3 
      PHI: (r3v199 android.database.Cursor) = (r3v194 android.database.Cursor), (r3v194 android.database.Cursor), (r3v194 android.database.Cursor), (r3v202 android.database.Cursor), (r3v202 android.database.Cursor), (r3v202 android.database.Cursor), (r3v202 android.database.Cursor), (r3v0 android.database.Cursor), (r3v0 android.database.Cursor) binds: [B:45:0x00e0, B:51:0x00ed, B:52:?, B:23:0x007f, B:29:0x008c, B:31:0x0090, B:32:?, B:9:0x0031, B:10:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:9:0x0031] */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x027e A[SYNTHETIC, Splitter:B:153:0x027e] */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x0285 A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x0293 A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:200:0x03a7 A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:201:0x03a9 A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:203:0x03ac A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x03ad A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:267:0x0611 A[ADDED_TO_REGION, Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:306:0x06dd A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:334:0x0760 A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:383:0x08d0 A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:389:0x08ec A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:390:0x0909 A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:393:0x090f A[Catch:{ IOException -> 0x0227, all -> 0x0f2c }] */
    /* JADX WARNING: Removed duplicated region for block: B:617:0x0f0e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0f26 A[SYNTHETIC, Splitter:B:625:0x0f26] */
    /* JADX WARNING: Removed duplicated region for block: B:660:0x08e9 A[SYNTHETIC] */
    @androidx.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzd(java.lang.String r47, long r48) {
        /*
            r46 = this;
            r1 = r46
            com.google.android.gms.measurement.internal.zzw r2 = r46.zzdo()
            r2.beginTransaction()
            com.google.android.gms.measurement.internal.zzft$zza r2 = new com.google.android.gms.measurement.internal.zzft$zza     // Catch:{ all -> 0x0f2c }
            r3 = 0
            r2.<init>(r1, r3)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzw r4 = r46.zzdo()     // Catch:{ all -> 0x0f2c }
            long r5 = r1.zzsp     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r2)     // Catch:{ all -> 0x0f2c }
            r4.zzq()     // Catch:{ all -> 0x0f2c }
            r4.zzah()     // Catch:{ all -> 0x0f2c }
            r8 = -1
            r10 = 2
            r11 = 0
            r12 = 1
            android.database.sqlite.SQLiteDatabase r15 = r4.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            boolean r13 = android.text.TextUtils.isEmpty(r3)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            if (r13 == 0) goto L_0x009f
            int r13 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1))
            if (r13 == 0) goto L_0x004c
            java.lang.String[] r14 = new java.lang.String[r10]     // Catch:{ SQLiteException -> 0x0046, all -> 0x0040 }
            java.lang.String r16 = java.lang.String.valueOf(r5)     // Catch:{ SQLiteException -> 0x0046, all -> 0x0040 }
            r14[r11] = r16     // Catch:{ SQLiteException -> 0x0046, all -> 0x0040 }
            java.lang.String r16 = java.lang.String.valueOf(r48)     // Catch:{ SQLiteException -> 0x0046, all -> 0x0040 }
            r14[r12] = r16     // Catch:{ SQLiteException -> 0x0046, all -> 0x0040 }
            goto L_0x0054
        L_0x0040:
            r0 = move-exception
            r4 = r1
            r22 = r3
            goto L_0x0264
        L_0x0046:
            r0 = move-exception
            r6 = r3
            r7 = r6
        L_0x0049:
            r3 = r0
            goto L_0x026b
        L_0x004c:
            java.lang.String[] r14 = new java.lang.String[r12]     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            java.lang.String r16 = java.lang.String.valueOf(r48)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            r14[r11] = r16     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
        L_0x0054:
            if (r13 == 0) goto L_0x0059
            java.lang.String r13 = "rowid <= ? and "
            goto L_0x005b
        L_0x0059:
            java.lang.String r13 = ""
        L_0x005b:
            java.lang.String r16 = java.lang.String.valueOf(r13)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            int r7 = r16.length()     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            int r7 = r7 + 148
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            r3.<init>(r7)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            java.lang.String r7 = "select app_id, metadata_fingerprint from raw_events where "
            r3.append(r7)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            r3.append(r13)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            java.lang.String r7 = "app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;"
            r3.append(r7)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            java.lang.String r3 = r3.toString()     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            android.database.Cursor r3 = r15.rawQuery(r3, r14)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            boolean r7 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x025b, all -> 0x0040 }
            if (r7 != 0) goto L_0x008c
            if (r3 == 0) goto L_0x0281
            r3.close()     // Catch:{ all -> 0x0f2c }
            goto L_0x0281
        L_0x008c:
            java.lang.String r7 = r3.getString(r11)     // Catch:{ SQLiteException -> 0x025b, all -> 0x0040 }
            java.lang.String r13 = r3.getString(r12)     // Catch:{ SQLiteException -> 0x009c, all -> 0x0040 }
            r3.close()     // Catch:{ SQLiteException -> 0x009c, all -> 0x0040 }
            r23 = r3
            r3 = r7
            r7 = r13
            goto L_0x00f8
        L_0x009c:
            r0 = move-exception
            r6 = r3
            goto L_0x0049
        L_0x009f:
            int r3 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1))
            if (r3 == 0) goto L_0x00b0
            java.lang.String[] r7 = new java.lang.String[r10]     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            r13 = 0
            r7[r11] = r13     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            java.lang.String r13 = java.lang.String.valueOf(r5)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            r7[r12] = r13     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            r13 = r7
            goto L_0x00b5
        L_0x00b0:
            r7 = 0
            java.lang.String[] r13 = new java.lang.String[]{r7}     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
        L_0x00b5:
            if (r3 == 0) goto L_0x00ba
            java.lang.String r3 = " and rowid <= ?"
            goto L_0x00bc
        L_0x00ba:
            java.lang.String r3 = ""
        L_0x00bc:
            java.lang.String r7 = java.lang.String.valueOf(r3)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            int r7 = r7.length()     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            int r7 = r7 + 84
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            r14.<init>(r7)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            java.lang.String r7 = "select metadata_fingerprint from raw_events where app_id = ?"
            r14.append(r7)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            r14.append(r3)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            java.lang.String r3 = " order by rowid limit 1;"
            r14.append(r3)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            java.lang.String r3 = r14.toString()     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            android.database.Cursor r3 = r15.rawQuery(r3, r13)     // Catch:{ SQLiteException -> 0x0267, all -> 0x0260 }
            boolean r7 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x025b, all -> 0x0040 }
            if (r7 != 0) goto L_0x00ed
            if (r3 == 0) goto L_0x0281
            r3.close()     // Catch:{ all -> 0x0f2c }
            goto L_0x0281
        L_0x00ed:
            java.lang.String r13 = r3.getString(r11)     // Catch:{ SQLiteException -> 0x025b, all -> 0x0040 }
            r3.close()     // Catch:{ SQLiteException -> 0x025b, all -> 0x0040 }
            r23 = r3
            r7 = r13
            r3 = 0
        L_0x00f8:
            java.lang.String r14 = "raw_events_metadata"
            java.lang.String r13 = "metadata"
            java.lang.String[] r16 = new java.lang.String[]{r13}     // Catch:{ SQLiteException -> 0x0255, all -> 0x0250 }
            java.lang.String r17 = "app_id = ? and metadata_fingerprint = ?"
            java.lang.String[] r13 = new java.lang.String[r10]     // Catch:{ SQLiteException -> 0x0255, all -> 0x0250 }
            r13[r11] = r3     // Catch:{ SQLiteException -> 0x0255, all -> 0x0250 }
            r13[r12] = r7     // Catch:{ SQLiteException -> 0x0255, all -> 0x0250 }
            r18 = 0
            r19 = 0
            java.lang.String r20 = "rowid"
            java.lang.String r21 = "2"
            r24 = r13
            r13 = r15
            r25 = r15
            r15 = r16
            r16 = r17
            r17 = r24
            android.database.Cursor r15 = r13.query(r14, r15, r16, r17, r18, r19, r20, r21)     // Catch:{ SQLiteException -> 0x0255, all -> 0x0250 }
            boolean r13 = r15.moveToFirst()     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            if (r13 != 0) goto L_0x0148
            com.google.android.gms.measurement.internal.zzau r5 = r4.zzad()     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzda()     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            java.lang.String r6 = "Raw event metadata record is missing. appId"
            java.lang.Object r7 = com.google.android.gms.measurement.internal.zzau.zzao(r3)     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            r5.zza(r6, r7)     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            if (r15 == 0) goto L_0x0281
            r15.close()     // Catch:{ all -> 0x0f2c }
            goto L_0x0281
        L_0x013d:
            r0 = move-exception
            r4 = r1
            r22 = r15
            goto L_0x0264
        L_0x0143:
            r0 = move-exception
            r7 = r3
            r6 = r15
            goto L_0x0049
        L_0x0148:
            byte[] r13 = r15.getBlob(r11)     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            com.google.android.gms.internal.measurement.zzch r13 = com.google.android.gms.internal.measurement.zzch.zzf(r13)     // Catch:{ IOException -> 0x0227 }
            boolean r14 = r15.moveToNext()     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            if (r14 == 0) goto L_0x0167
            com.google.android.gms.measurement.internal.zzau r14 = r4.zzad()     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            com.google.android.gms.measurement.internal.zzaw r14 = r14.zzdd()     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            java.lang.String r10 = "Get multiple raw event metadata records, expected one. appId"
            java.lang.Object r12 = com.google.android.gms.measurement.internal.zzau.zzao(r3)     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            r14.zza(r10, r12)     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
        L_0x0167:
            r15.close()     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            r2.zzb(r13)     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            int r10 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1))
            if (r10 == 0) goto L_0x0187
            java.lang.String r10 = "app_id = ? and metadata_fingerprint = ? and rowid <= ?"
            r12 = 3
            java.lang.String[] r13 = new java.lang.String[r12]     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            r13[r11] = r3     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            r12 = 1
            r13[r12] = r7     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            r6 = 2
            r13[r6] = r5     // Catch:{ SQLiteException -> 0x0143, all -> 0x013d }
            r16 = r10
            r17 = r13
            goto L_0x0195
        L_0x0187:
            java.lang.String r5 = "app_id = ? and metadata_fingerprint = ?"
            r6 = 2
            java.lang.String[] r10 = new java.lang.String[r6]     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            r10[r11] = r3     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            r6 = 1
            r10[r6] = r7     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            r16 = r5
            r17 = r10
        L_0x0195:
            java.lang.String r14 = "raw_events"
            java.lang.String r5 = "rowid"
            java.lang.String r6 = "name"
            java.lang.String r7 = "timestamp"
            java.lang.String r10 = "data"
            java.lang.String[] r5 = new java.lang.String[]{r5, r6, r7, r10}     // Catch:{ SQLiteException -> 0x024b, all -> 0x0245 }
            r18 = 0
            r19 = 0
            java.lang.String r20 = "rowid"
            r21 = 0
            r13 = r25
            r6 = r15
            r15 = r5
            android.database.Cursor r5 = r13.query(r14, r15, r16, r17, r18, r19, r20, r21)     // Catch:{ SQLiteException -> 0x0243, all -> 0x0241 }
            boolean r6 = r5.moveToFirst()     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            if (r6 != 0) goto L_0x01d1
            com.google.android.gms.measurement.internal.zzau r6 = r4.zzad()     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            com.google.android.gms.measurement.internal.zzaw r6 = r6.zzdd()     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            java.lang.String r7 = "Raw event data disappeared while in transaction. appId"
            java.lang.Object r10 = com.google.android.gms.measurement.internal.zzau.zzao(r3)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            r6.zza(r7, r10)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            if (r5 == 0) goto L_0x0281
            r5.close()     // Catch:{ all -> 0x0f2c }
            goto L_0x0281
        L_0x01d1:
            long r6 = r5.getLong(r11)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            r10 = 3
            byte[] r12 = r5.getBlob(r10)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            com.google.android.gms.internal.measurement.zzcf r10 = com.google.android.gms.internal.measurement.zzcf.zze(r12)     // Catch:{ IOException -> 0x01fd }
            r12 = 1
            java.lang.String r13 = r5.getString(r12)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            r10.name = r13     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            r12 = 2
            long r13 = r5.getLong(r12)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            java.lang.Long r12 = java.lang.Long.valueOf(r13)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            r10.zzxj = r12     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            boolean r6 = r2.zza(r6, r10)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            if (r6 != 0) goto L_0x0210
            if (r5 == 0) goto L_0x0281
            r5.close()     // Catch:{ all -> 0x0f2c }
            goto L_0x0281
        L_0x01fd:
            r0 = move-exception
            r6 = r0
            com.google.android.gms.measurement.internal.zzau r7 = r4.zzad()     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            com.google.android.gms.measurement.internal.zzaw r7 = r7.zzda()     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            java.lang.String r10 = "Data loss. Failed to merge raw event. appId"
            java.lang.Object r12 = com.google.android.gms.measurement.internal.zzau.zzao(r3)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            r7.zza(r10, r12, r6)     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
        L_0x0210:
            boolean r6 = r5.moveToNext()     // Catch:{ SQLiteException -> 0x0222, all -> 0x021d }
            if (r6 != 0) goto L_0x01d1
            if (r5 == 0) goto L_0x0281
            r5.close()     // Catch:{ all -> 0x0f2c }
            goto L_0x0281
        L_0x021d:
            r0 = move-exception
            r4 = r1
            r22 = r5
            goto L_0x0264
        L_0x0222:
            r0 = move-exception
            r7 = r3
            r6 = r5
            goto L_0x0049
        L_0x0227:
            r0 = move-exception
            r6 = r15
            r5 = r0
            com.google.android.gms.measurement.internal.zzau r7 = r4.zzad()     // Catch:{ SQLiteException -> 0x0243, all -> 0x0241 }
            com.google.android.gms.measurement.internal.zzaw r7 = r7.zzda()     // Catch:{ SQLiteException -> 0x0243, all -> 0x0241 }
            java.lang.String r10 = "Data loss. Failed to merge raw event metadata. appId"
            java.lang.Object r12 = com.google.android.gms.measurement.internal.zzau.zzao(r3)     // Catch:{ SQLiteException -> 0x0243, all -> 0x0241 }
            r7.zza(r10, r12, r5)     // Catch:{ SQLiteException -> 0x0243, all -> 0x0241 }
            if (r6 == 0) goto L_0x0281
            r6.close()     // Catch:{ all -> 0x0f2c }
            goto L_0x0281
        L_0x0241:
            r0 = move-exception
            goto L_0x0247
        L_0x0243:
            r0 = move-exception
            goto L_0x024d
        L_0x0245:
            r0 = move-exception
            r6 = r15
        L_0x0247:
            r4 = r1
            r22 = r6
            goto L_0x0264
        L_0x024b:
            r0 = move-exception
            r6 = r15
        L_0x024d:
            r7 = r3
            goto L_0x0049
        L_0x0250:
            r0 = move-exception
            r4 = r1
            r22 = r23
            goto L_0x0264
        L_0x0255:
            r0 = move-exception
            r7 = r3
            r6 = r23
            goto L_0x0049
        L_0x025b:
            r0 = move-exception
            r6 = r3
            r7 = 0
            goto L_0x0049
        L_0x0260:
            r0 = move-exception
            r4 = r1
            r22 = 0
        L_0x0264:
            r1 = r0
            goto L_0x0f24
        L_0x0267:
            r0 = move-exception
            r3 = r0
            r6 = 0
            r7 = 0
        L_0x026b:
            com.google.android.gms.measurement.internal.zzau r4 = r4.zzad()     // Catch:{ all -> 0x0f1f }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ all -> 0x0f1f }
            java.lang.String r5 = "Data loss. Error selecting raw event. appId"
            java.lang.Object r7 = com.google.android.gms.measurement.internal.zzau.zzao(r7)     // Catch:{ all -> 0x0f1f }
            r4.zza(r5, r7, r3)     // Catch:{ all -> 0x0f1f }
            if (r6 == 0) goto L_0x0281
            r6.close()     // Catch:{ all -> 0x0f2c }
        L_0x0281:
            java.util.List<com.google.android.gms.internal.measurement.zzcf> r3 = r2.zzsv     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x0290
            java.util.List<com.google.android.gms.internal.measurement.zzcf> r3 = r2.zzsv     // Catch:{ all -> 0x0f2c }
            boolean r3 = r3.isEmpty()     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x028e
            goto L_0x0290
        L_0x028e:
            r3 = 0
            goto L_0x0291
        L_0x0290:
            r3 = 1
        L_0x0291:
            if (r3 != 0) goto L_0x0f0e
            com.google.android.gms.internal.measurement.zzch r3 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.util.List<com.google.android.gms.internal.measurement.zzcf> r4 = r2.zzsv     // Catch:{ all -> 0x0f2c }
            int r4 = r4.size()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzcf[] r4 = new com.google.android.gms.internal.measurement.zzcf[r4]     // Catch:{ all -> 0x0f2c }
            r3.zzxo = r4     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzby r4 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzt r4 = r4.zzaf()     // Catch:{ all -> 0x0f2c }
            java.lang.String r5 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            boolean r4 = r4.zzm(r5)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzt r5 = r5.zzaf()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r6 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = r6.zzcf     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r7 = com.google.android.gms.measurement.internal.zzal.zzim     // Catch:{ all -> 0x0f2c }
            boolean r5 = r5.zze(r6, r7)     // Catch:{ all -> 0x0f2c }
            r8 = 0
            r9 = 0
            r10 = 0
            r12 = 0
            r13 = 0
            r14 = 0
        L_0x02c2:
            java.util.List<com.google.android.gms.internal.measurement.zzcf> r6 = r2.zzsv     // Catch:{ all -> 0x0f2c }
            int r6 = r6.size()     // Catch:{ all -> 0x0f2c }
            r18 = r12
            if (r10 >= r6) goto L_0x07c3
            java.util.List<com.google.android.gms.internal.measurement.zzcf> r6 = r2.zzsv     // Catch:{ all -> 0x0f2c }
            java.lang.Object r6 = r6.get(r10)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzcf r6 = (com.google.android.gms.internal.measurement.zzcf) r6     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzbs r7 = r46.zzdp()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r11 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = r11.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r12 = r6.name     // Catch:{ all -> 0x0f2c }
            boolean r7 = r7.zzk(r11, r12)     // Catch:{ all -> 0x0f2c }
            if (r7 == 0) goto L_0x0355
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r7 = r7.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r7 = r7.zzdd()     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = "Dropping blacklisted raw event. appId"
            com.google.android.gms.internal.measurement.zzch r12 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r12 = r12.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.Object r12 = com.google.android.gms.measurement.internal.zzau.zzao(r12)     // Catch:{ all -> 0x0f2c }
            r23 = r10
            com.google.android.gms.measurement.internal.zzby r10 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzas r10 = r10.zzaa()     // Catch:{ all -> 0x0f2c }
            r25 = r13
            java.lang.String r13 = r6.name     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = r10.zzal(r13)     // Catch:{ all -> 0x0f2c }
            r7.zza(r11, r12, r10)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzbs r7 = r46.zzdp()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r10 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = r10.zzcf     // Catch:{ all -> 0x0f2c }
            boolean r7 = r7.zzbe(r10)     // Catch:{ all -> 0x0f2c }
            if (r7 != 0) goto L_0x032a
            com.google.android.gms.measurement.internal.zzbs r7 = r46.zzdp()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r10 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = r10.zzcf     // Catch:{ all -> 0x0f2c }
            boolean r7 = r7.zzbf(r10)     // Catch:{ all -> 0x0f2c }
            if (r7 == 0) goto L_0x0328
            goto L_0x032a
        L_0x0328:
            r7 = 0
            goto L_0x032b
        L_0x032a:
            r7 = 1
        L_0x032b:
            if (r7 != 0) goto L_0x0350
            java.lang.String r7 = "_err"
            java.lang.String r10 = r6.name     // Catch:{ all -> 0x0f2c }
            boolean r7 = r7.equals(r10)     // Catch:{ all -> 0x0f2c }
            if (r7 != 0) goto L_0x0350
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzgd r26 = r7.zzab()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r7 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r7 = r7.zzcf     // Catch:{ all -> 0x0f2c }
            r28 = 11
            java.lang.String r29 = "_ev"
            java.lang.String r6 = r6.name     // Catch:{ all -> 0x0f2c }
            r31 = 0
            r27 = r7
            r30 = r6
            r26.zza(r27, r28, r29, r30, r31)     // Catch:{ all -> 0x0f2c }
        L_0x0350:
            r12 = r18
            r13 = 3
            goto L_0x07bc
        L_0x0355:
            r23 = r10
            r25 = r13
            com.google.android.gms.measurement.internal.zzbs r7 = r46.zzdp()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r10 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = r10.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = r6.name     // Catch:{ all -> 0x0f2c }
            boolean r10 = r7.zzl(r10, r11)     // Catch:{ all -> 0x0f2c }
            if (r10 != 0) goto L_0x03b4
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            java.lang.String r7 = r6.name     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r7)     // Catch:{ all -> 0x0f2c }
            int r12 = r7.hashCode()     // Catch:{ all -> 0x0f2c }
            r13 = 94660(0x171c4, float:1.32647E-40)
            if (r12 == r13) goto L_0x0399
            r13 = 95025(0x17331, float:1.33158E-40)
            if (r12 == r13) goto L_0x038f
            r13 = 95027(0x17333, float:1.33161E-40)
            if (r12 == r13) goto L_0x0385
            goto L_0x03a3
        L_0x0385:
            java.lang.String r12 = "_ui"
            boolean r7 = r7.equals(r12)     // Catch:{ all -> 0x0f2c }
            if (r7 == 0) goto L_0x03a3
            r7 = 1
            goto L_0x03a4
        L_0x038f:
            java.lang.String r12 = "_ug"
            boolean r7 = r7.equals(r12)     // Catch:{ all -> 0x0f2c }
            if (r7 == 0) goto L_0x03a3
            r7 = 2
            goto L_0x03a4
        L_0x0399:
            java.lang.String r12 = "_in"
            boolean r7 = r7.equals(r12)     // Catch:{ all -> 0x0f2c }
            if (r7 == 0) goto L_0x03a3
            r7 = 0
            goto L_0x03a4
        L_0x03a3:
            r7 = -1
        L_0x03a4:
            switch(r7) {
                case 0: goto L_0x03a9;
                case 1: goto L_0x03a9;
                case 2: goto L_0x03a9;
                default: goto L_0x03a7;
            }     // Catch:{ all -> 0x0f2c }
        L_0x03a7:
            r7 = 0
            goto L_0x03aa
        L_0x03a9:
            r7 = 1
        L_0x03aa:
            if (r7 == 0) goto L_0x03ad
            goto L_0x03b4
        L_0x03ad:
            r28 = r3
            r11 = r8
            r29 = r14
            goto L_0x0601
        L_0x03b4:
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            if (r7 != 0) goto L_0x03bd
            r7 = 0
            com.google.android.gms.internal.measurement.zzbt$zzd[] r12 = new com.google.android.gms.internal.measurement.zzbt.zzd[r7]     // Catch:{ all -> 0x0f2c }
            r6.zzxi = r12     // Catch:{ all -> 0x0f2c }
        L_0x03bd:
            r12 = 0
            r13 = 0
            r19 = 0
        L_0x03c1:
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            int r7 = r7.length     // Catch:{ all -> 0x0f2c }
            if (r12 >= r7) goto L_0x0411
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            r7 = r7[r12]     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez$zza r7 = r7.zzmh()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez$zza r7 = (com.google.android.gms.internal.measurement.zzez.zza) r7     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r7 = (com.google.android.gms.internal.measurement.zzbt.zzd.zza) r7     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = "_c"
            r28 = r3
            java.lang.String r3 = r7.getName()     // Catch:{ all -> 0x0f2c }
            boolean r3 = r11.equals(r3)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x03e9
            r29 = r14
            r14 = 1
            r7.zzaj(r14)     // Catch:{ all -> 0x0f2c }
            r13 = 1
            goto L_0x03fe
        L_0x03e9:
            r29 = r14
            java.lang.String r3 = "_r"
            java.lang.String r11 = r7.getName()     // Catch:{ all -> 0x0f2c }
            boolean r3 = r3.equals(r11)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x03fe
            r14 = 1
            r7.zzaj(r14)     // Catch:{ all -> 0x0f2c }
            r19 = 1
        L_0x03fe:
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzgh r7 = r7.zzmr()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez r7 = (com.google.android.gms.internal.measurement.zzez) r7     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd r7 = (com.google.android.gms.internal.measurement.zzbt.zzd) r7     // Catch:{ all -> 0x0f2c }
            r3[r12] = r7     // Catch:{ all -> 0x0f2c }
            int r12 = r12 + 1
            r3 = r28
            r14 = r29
            goto L_0x03c1
        L_0x0411:
            r28 = r3
            r29 = r14
            if (r13 != 0) goto L_0x0460
            if (r10 == 0) goto L_0x0460
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdi()     // Catch:{ all -> 0x0f2c }
            java.lang.String r7 = "Marking event as conversion"
            com.google.android.gms.measurement.internal.zzby r11 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzas r11 = r11.zzaa()     // Catch:{ all -> 0x0f2c }
            java.lang.String r12 = r6.name     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = r11.zzal(r12)     // Catch:{ all -> 0x0f2c }
            r3.zza(r7, r11)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            int r7 = r7.length     // Catch:{ all -> 0x0f2c }
            r11 = 1
            int r7 = r7 + r11
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r3, r7)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = (com.google.android.gms.internal.measurement.zzbt.zzd[]) r3     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r7 = com.google.android.gms.internal.measurement.zzbt.zzd.zzht()     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = "_c"
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r7 = r7.zzbw(r11)     // Catch:{ all -> 0x0f2c }
            r11 = 1
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r7 = r7.zzaj(r11)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzgh r7 = r7.zzmr()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez r7 = (com.google.android.gms.internal.measurement.zzez) r7     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd r7 = (com.google.android.gms.internal.measurement.zzbt.zzd) r7     // Catch:{ all -> 0x0f2c }
            int r11 = r3.length     // Catch:{ all -> 0x0f2c }
            r12 = 1
            int r11 = r11 - r12
            r3[r11] = r7     // Catch:{ all -> 0x0f2c }
            r6.zzxi = r3     // Catch:{ all -> 0x0f2c }
        L_0x0460:
            if (r19 != 0) goto L_0x04a9
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdi()     // Catch:{ all -> 0x0f2c }
            java.lang.String r7 = "Marking event as real-time"
            com.google.android.gms.measurement.internal.zzby r11 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzas r11 = r11.zzaa()     // Catch:{ all -> 0x0f2c }
            java.lang.String r12 = r6.name     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = r11.zzal(r12)     // Catch:{ all -> 0x0f2c }
            r3.zza(r7, r11)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            int r7 = r7.length     // Catch:{ all -> 0x0f2c }
            r11 = 1
            int r7 = r7 + r11
            java.lang.Object[] r3 = java.util.Arrays.copyOf(r3, r7)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = (com.google.android.gms.internal.measurement.zzbt.zzd[]) r3     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r7 = com.google.android.gms.internal.measurement.zzbt.zzd.zzht()     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = "_r"
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r7 = r7.zzbw(r11)     // Catch:{ all -> 0x0f2c }
            r11 = 1
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r7 = r7.zzaj(r11)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzgh r7 = r7.zzmr()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez r7 = (com.google.android.gms.internal.measurement.zzez) r7     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd r7 = (com.google.android.gms.internal.measurement.zzbt.zzd) r7     // Catch:{ all -> 0x0f2c }
            int r11 = r3.length     // Catch:{ all -> 0x0f2c }
            r12 = 1
            int r11 = r11 - r12
            r3[r11] = r7     // Catch:{ all -> 0x0f2c }
            r6.zzxi = r3     // Catch:{ all -> 0x0f2c }
        L_0x04a9:
            com.google.android.gms.measurement.internal.zzw r31 = r46.zzdo()     // Catch:{ all -> 0x0f2c }
            long r32 = r46.zzfz()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r3 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r3 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            r35 = 0
            r36 = 0
            r37 = 0
            r38 = 0
            r39 = 1
            r34 = r3
            com.google.android.gms.measurement.internal.zzx r3 = r31.zza(r32, r34, r35, r36, r37, r38, r39)     // Catch:{ all -> 0x0f2c }
            long r11 = r3.zzep     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzt r3 = r3.zzaf()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r7 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r7 = r7.zzcf     // Catch:{ all -> 0x0f2c }
            int r3 = r3.zzi(r7)     // Catch:{ all -> 0x0f2c }
            long r13 = (long) r3     // Catch:{ all -> 0x0f2c }
            int r3 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r3 <= 0) goto L_0x0514
            r3 = 0
        L_0x04db:
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            int r7 = r7.length     // Catch:{ all -> 0x0f2c }
            if (r3 >= r7) goto L_0x0511
            java.lang.String r7 = "_r"
            com.google.android.gms.internal.measurement.zzbt$zzd[] r11 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            r11 = r11[r3]     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = r11.getName()     // Catch:{ all -> 0x0f2c }
            boolean r7 = r7.equals(r11)     // Catch:{ all -> 0x0f2c }
            if (r7 == 0) goto L_0x050e
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            int r7 = r7.length     // Catch:{ all -> 0x0f2c }
            r11 = 1
            int r7 = r7 - r11
            com.google.android.gms.internal.measurement.zzbt$zzd[] r11 = new com.google.android.gms.internal.measurement.zzbt.zzd[r7]     // Catch:{ all -> 0x0f2c }
            if (r3 <= 0) goto L_0x04ff
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            r12 = 0
            java.lang.System.arraycopy(r7, r12, r11, r12, r3)     // Catch:{ all -> 0x0f2c }
        L_0x04ff:
            int r12 = r11.length     // Catch:{ all -> 0x0f2c }
            if (r3 >= r12) goto L_0x050b
            com.google.android.gms.internal.measurement.zzbt$zzd[] r12 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            int r13 = r3 + 1
            int r14 = r11.length     // Catch:{ all -> 0x0f2c }
            int r14 = r14 - r3
            java.lang.System.arraycopy(r12, r13, r11, r3, r14)     // Catch:{ all -> 0x0f2c }
        L_0x050b:
            r6.zzxi = r11     // Catch:{ all -> 0x0f2c }
            goto L_0x0511
        L_0x050e:
            int r3 = r3 + 1
            goto L_0x04db
        L_0x0511:
            r12 = r18
            goto L_0x0515
        L_0x0514:
            r12 = 1
        L_0x0515:
            java.lang.String r3 = r6.name     // Catch:{ all -> 0x0f2c }
            boolean r3 = com.google.android.gms.measurement.internal.zzgd.zzbm(r3)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x05fe
            if (r10 == 0) goto L_0x05fe
            com.google.android.gms.measurement.internal.zzw r31 = r46.zzdo()     // Catch:{ all -> 0x0f2c }
            long r32 = r46.zzfz()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r3 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r3 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            r35 = 0
            r36 = 0
            r37 = 1
            r38 = 0
            r39 = 0
            r34 = r3
            com.google.android.gms.measurement.internal.zzx r3 = r31.zza(r32, r34, r35, r36, r37, r38, r39)     // Catch:{ all -> 0x0f2c }
            long r13 = r3.zzen     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzt r3 = r3.zzaf()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r11 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r11 = r11.zzcf     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Integer> r15 = com.google.android.gms.measurement.internal.zzal.zzgq     // Catch:{ all -> 0x0f2c }
            int r3 = r3.zzb(r11, r15)     // Catch:{ all -> 0x0f2c }
            r11 = r8
            long r7 = (long) r3     // Catch:{ all -> 0x0f2c }
            int r3 = (r13 > r7 ? 1 : (r13 == r7 ? 0 : -1))
            if (r3 <= 0) goto L_0x05fb
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdd()     // Catch:{ all -> 0x0f2c }
            java.lang.String r7 = "Too many conversions. Not logging as conversion. appId"
            com.google.android.gms.internal.measurement.zzch r8 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r8.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.Object r8 = com.google.android.gms.measurement.internal.zzau.zzao(r8)     // Catch:{ all -> 0x0f2c }
            r3.zza(r7, r8)     // Catch:{ all -> 0x0f2c }
            r3 = 0
            r7 = 0
            r8 = 0
            r13 = -1
        L_0x056e:
            com.google.android.gms.internal.measurement.zzbt$zzd[] r14 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            int r14 = r14.length     // Catch:{ all -> 0x0f2c }
            if (r3 >= r14) goto L_0x05a1
            com.google.android.gms.internal.measurement.zzbt$zzd[] r14 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            r14 = r14[r3]     // Catch:{ all -> 0x0f2c }
            java.lang.String r15 = "_c"
            r18 = r12
            java.lang.String r12 = r14.getName()     // Catch:{ all -> 0x0f2c }
            boolean r12 = r15.equals(r12)     // Catch:{ all -> 0x0f2c }
            if (r12 == 0) goto L_0x058f
            com.google.android.gms.internal.measurement.zzez$zza r8 = r14.zzmh()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez$zza r8 = (com.google.android.gms.internal.measurement.zzez.zza) r8     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r8 = (com.google.android.gms.internal.measurement.zzbt.zzd.zza) r8     // Catch:{ all -> 0x0f2c }
            r13 = r3
            goto L_0x059c
        L_0x058f:
            java.lang.String r12 = "_err"
            java.lang.String r14 = r14.getName()     // Catch:{ all -> 0x0f2c }
            boolean r12 = r12.equals(r14)     // Catch:{ all -> 0x0f2c }
            if (r12 == 0) goto L_0x059c
            r7 = 1
        L_0x059c:
            int r3 = r3 + 1
            r12 = r18
            goto L_0x056e
        L_0x05a1:
            r18 = r12
            if (r7 == 0) goto L_0x05c0
            if (r8 == 0) goto L_0x05c0
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            r7 = 1
            com.google.android.gms.internal.measurement.zzbt$zzd[] r12 = new com.google.android.gms.internal.measurement.zzbt.zzd[r7]     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzgh r7 = r8.zzmr()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez r7 = (com.google.android.gms.internal.measurement.zzez) r7     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd r7 = (com.google.android.gms.internal.measurement.zzbt.zzd) r7     // Catch:{ all -> 0x0f2c }
            r8 = 0
            r12[r8] = r7     // Catch:{ all -> 0x0f2c }
            java.lang.Object[] r3 = com.google.android.gms.common.util.ArrayUtils.removeAll(r3, r12)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = (com.google.android.gms.internal.measurement.zzbt.zzd[]) r3     // Catch:{ all -> 0x0f2c }
            r6.zzxi = r3     // Catch:{ all -> 0x0f2c }
            goto L_0x0601
        L_0x05c0:
            if (r8 == 0) goto L_0x05e3
            java.lang.Object r3 = r8.clone()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez$zza r3 = (com.google.android.gms.internal.measurement.zzez.zza) r3     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r3 = (com.google.android.gms.internal.measurement.zzbt.zzd.zza) r3     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "_err"
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r3 = r3.zzbw(r8)     // Catch:{ all -> 0x0f2c }
            r14 = 10
            com.google.android.gms.internal.measurement.zzbt$zzd$zza r3 = r3.zzaj(r14)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzgh r3 = r3.zzmr()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez r3 = (com.google.android.gms.internal.measurement.zzez) r3     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd r3 = (com.google.android.gms.internal.measurement.zzbt.zzd) r3     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r8 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            r8[r13] = r3     // Catch:{ all -> 0x0f2c }
            goto L_0x0601
        L_0x05e3:
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "Did not find conversion parameter. appId"
            com.google.android.gms.internal.measurement.zzch r12 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r12 = r12.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.Object r12 = com.google.android.gms.measurement.internal.zzau.zzao(r12)     // Catch:{ all -> 0x0f2c }
            r3.zza(r8, r12)     // Catch:{ all -> 0x0f2c }
            goto L_0x0601
        L_0x05fb:
            r18 = r12
            goto L_0x0601
        L_0x05fe:
            r11 = r8
            r18 = r12
        L_0x0601:
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzt r3 = r3.zzaf()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r8 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r8.zzcf     // Catch:{ all -> 0x0f2c }
            boolean r3 = r3.zzv(r8)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x06ca
            if (r10 == 0) goto L_0x06ca
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            r8 = 0
            r10 = -1
            r12 = -1
        L_0x0618:
            int r13 = r3.length     // Catch:{ all -> 0x0f2c }
            if (r8 >= r13) goto L_0x063d
            java.lang.String r13 = "value"
            r14 = r3[r8]     // Catch:{ all -> 0x0f2c }
            java.lang.String r14 = r14.getName()     // Catch:{ all -> 0x0f2c }
            boolean r13 = r13.equals(r14)     // Catch:{ all -> 0x0f2c }
            if (r13 == 0) goto L_0x062b
            r10 = r8
            goto L_0x063a
        L_0x062b:
            java.lang.String r13 = "currency"
            r14 = r3[r8]     // Catch:{ all -> 0x0f2c }
            java.lang.String r14 = r14.getName()     // Catch:{ all -> 0x0f2c }
            boolean r13 = r13.equals(r14)     // Catch:{ all -> 0x0f2c }
            if (r13 == 0) goto L_0x063a
            r12 = r8
        L_0x063a:
            int r8 = r8 + 1
            goto L_0x0618
        L_0x063d:
            r8 = -1
            if (r10 == r8) goto L_0x06c6
            r8 = r3[r10]     // Catch:{ all -> 0x0f2c }
            boolean r8 = r8.zzhn()     // Catch:{ all -> 0x0f2c }
            if (r8 != 0) goto L_0x0673
            r8 = r3[r10]     // Catch:{ all -> 0x0f2c }
            boolean r8 = r8.zzhq()     // Catch:{ all -> 0x0f2c }
            if (r8 != 0) goto L_0x0673
            com.google.android.gms.measurement.internal.zzby r8 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r8 = r8.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzdf()     // Catch:{ all -> 0x0f2c }
            java.lang.String r12 = "Value must be specified with a numeric type."
            r8.zzaq(r12)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = zza(r3, r10)     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "_c"
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = zza(r3, r8)     // Catch:{ all -> 0x0f2c }
            r8 = 18
            java.lang.String r10 = "value"
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = zza(r3, r8, r10)     // Catch:{ all -> 0x0f2c }
            r13 = 3
            goto L_0x06c7
        L_0x0673:
            r8 = -1
            if (r12 != r8) goto L_0x0679
            r8 = 1
            r13 = 3
            goto L_0x06a2
        L_0x0679:
            r8 = r3[r12]     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r8.zzhl()     // Catch:{ all -> 0x0f2c }
            int r12 = r8.length()     // Catch:{ all -> 0x0f2c }
            r13 = 3
            if (r12 == r13) goto L_0x0688
            r8 = 1
            goto L_0x06a2
        L_0x0688:
            r12 = 0
        L_0x0689:
            int r14 = r8.length()     // Catch:{ all -> 0x0f2c }
            if (r12 >= r14) goto L_0x06a1
            int r14 = r8.codePointAt(r12)     // Catch:{ all -> 0x0f2c }
            boolean r15 = java.lang.Character.isLetter(r14)     // Catch:{ all -> 0x0f2c }
            if (r15 != 0) goto L_0x069b
            r8 = 1
            goto L_0x06a2
        L_0x069b:
            int r14 = java.lang.Character.charCount(r14)     // Catch:{ all -> 0x0f2c }
            int r12 = r12 + r14
            goto L_0x0689
        L_0x06a1:
            r8 = 0
        L_0x06a2:
            if (r8 == 0) goto L_0x06c7
            com.google.android.gms.measurement.internal.zzby r8 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r8 = r8.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzdf()     // Catch:{ all -> 0x0f2c }
            java.lang.String r12 = "Value parameter discarded. You must also supply a 3-letter ISO_4217 currency code in the currency parameter."
            r8.zzaq(r12)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = zza(r3, r10)     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "_c"
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = zza(r3, r8)     // Catch:{ all -> 0x0f2c }
            r8 = 19
            java.lang.String r10 = "currency"
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = zza(r3, r8, r10)     // Catch:{ all -> 0x0f2c }
            goto L_0x06c7
        L_0x06c6:
            r13 = 3
        L_0x06c7:
            r6.zzxi = r3     // Catch:{ all -> 0x0f2c }
            goto L_0x06cb
        L_0x06ca:
            r13 = 3
        L_0x06cb:
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzt r3 = r3.zzaf()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r8 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r8.zzcf     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r10 = com.google.android.gms.measurement.internal.zzal.zzil     // Catch:{ all -> 0x0f2c }
            boolean r3 = r3.zze(r8, r10)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x074e
            java.lang.String r3 = "_e"
            java.lang.String r8 = r6.name     // Catch:{ all -> 0x0f2c }
            boolean r3 = r3.equals(r8)     // Catch:{ all -> 0x0f2c }
            r14 = 1000(0x3e8, double:4.94E-321)
            if (r3 == 0) goto L_0x0717
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            java.lang.String r3 = "_fr"
            com.google.android.gms.internal.measurement.zzbt$zzd r3 = com.google.android.gms.measurement.internal.zzfz.zza(r6, r3)     // Catch:{ all -> 0x0f2c }
            if (r3 != 0) goto L_0x074e
            if (r9 == 0) goto L_0x0715
            java.lang.Long r3 = r9.zzxj     // Catch:{ all -> 0x0f2c }
            long r10 = r3.longValue()     // Catch:{ all -> 0x0f2c }
            java.lang.Long r3 = r6.zzxj     // Catch:{ all -> 0x0f2c }
            long r19 = r3.longValue()     // Catch:{ all -> 0x0f2c }
            long r10 = r10 - r19
            long r10 = java.lang.Math.abs(r10)     // Catch:{ all -> 0x0f2c }
            int r3 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            if (r3 > 0) goto L_0x0715
            boolean r3 = r1.zza(r6, r9)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x0715
            r9 = 0
            r11 = 0
            goto L_0x074e
        L_0x0715:
            r11 = r6
            goto L_0x074e
        L_0x0717:
            java.lang.String r3 = "_vs"
            java.lang.String r8 = r6.name     // Catch:{ all -> 0x0f2c }
            boolean r3 = r3.equals(r8)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x074e
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            java.lang.String r3 = "_et"
            com.google.android.gms.internal.measurement.zzbt$zzd r3 = com.google.android.gms.measurement.internal.zzfz.zza(r6, r3)     // Catch:{ all -> 0x0f2c }
            if (r3 != 0) goto L_0x074e
            if (r11 == 0) goto L_0x074d
            java.lang.Long r3 = r11.zzxj     // Catch:{ all -> 0x0f2c }
            long r8 = r3.longValue()     // Catch:{ all -> 0x0f2c }
            java.lang.Long r3 = r6.zzxj     // Catch:{ all -> 0x0f2c }
            long r19 = r3.longValue()     // Catch:{ all -> 0x0f2c }
            long r8 = r8 - r19
            long r8 = java.lang.Math.abs(r8)     // Catch:{ all -> 0x0f2c }
            int r3 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1))
            if (r3 > 0) goto L_0x074d
            boolean r3 = r1.zza(r11, r6)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x074d
            r9 = 0
            r11 = 0
            goto L_0x074e
        L_0x074d:
            r9 = r6
        L_0x074e:
            if (r4 == 0) goto L_0x07ad
            if (r5 != 0) goto L_0x07ad
            java.lang.String r3 = "_e"
            java.lang.String r8 = r6.name     // Catch:{ all -> 0x0f2c }
            boolean r3 = r3.equals(r8)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x07ad
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x0796
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = r6.zzxi     // Catch:{ all -> 0x0f2c }
            int r3 = r3.length     // Catch:{ all -> 0x0f2c }
            if (r3 != 0) goto L_0x0766
            goto L_0x0796
        L_0x0766:
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            java.lang.String r3 = "_et"
            java.lang.Object r3 = com.google.android.gms.measurement.internal.zzfz.zzb(r6, r3)     // Catch:{ all -> 0x0f2c }
            java.lang.Long r3 = (java.lang.Long) r3     // Catch:{ all -> 0x0f2c }
            if (r3 != 0) goto L_0x078b
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdd()     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "Engagement event does not include duration. appId"
            com.google.android.gms.internal.measurement.zzch r10 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = r10.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.Object r10 = com.google.android.gms.measurement.internal.zzau.zzao(r10)     // Catch:{ all -> 0x0f2c }
            r3.zza(r8, r10)     // Catch:{ all -> 0x0f2c }
            goto L_0x07ad
        L_0x078b:
            long r14 = r3.longValue()     // Catch:{ all -> 0x0f2c }
            long r14 = r29 + r14
            r29 = r14
            r3 = r28
            goto L_0x07af
        L_0x0796:
            com.google.android.gms.measurement.internal.zzby r3 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdd()     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "Engagement event does not contain any parameters. appId"
            com.google.android.gms.internal.measurement.zzch r10 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = r10.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.Object r10 = com.google.android.gms.measurement.internal.zzau.zzao(r10)     // Catch:{ all -> 0x0f2c }
            r3.zza(r8, r10)     // Catch:{ all -> 0x0f2c }
        L_0x07ad:
            r3 = r28
        L_0x07af:
            com.google.android.gms.internal.measurement.zzcf[] r8 = r3.zzxo     // Catch:{ all -> 0x0f2c }
            int r10 = r25 + 1
            r8[r25] = r6     // Catch:{ all -> 0x0f2c }
            r25 = r10
            r8 = r11
            r12 = r18
            r14 = r29
        L_0x07bc:
            int r10 = r23 + 1
            r13 = r25
            r11 = 0
            goto L_0x02c2
        L_0x07c3:
            r25 = r13
            r29 = r14
            if (r5 == 0) goto L_0x0832
            r13 = r25
            r14 = r29
            r5 = 0
        L_0x07ce:
            if (r5 >= r13) goto L_0x082f
            com.google.android.gms.internal.measurement.zzcf[] r6 = r3.zzxo     // Catch:{ all -> 0x0f2c }
            r6 = r6[r5]     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "_e"
            java.lang.String r9 = r6.name     // Catch:{ all -> 0x0f2c }
            boolean r8 = r8.equals(r9)     // Catch:{ all -> 0x0f2c }
            if (r8 == 0) goto L_0x07fc
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "_fr"
            com.google.android.gms.internal.measurement.zzbt$zzd r8 = com.google.android.gms.measurement.internal.zzfz.zza(r6, r8)     // Catch:{ all -> 0x0f2c }
            if (r8 == 0) goto L_0x07fc
            com.google.android.gms.internal.measurement.zzcf[] r6 = r3.zzxo     // Catch:{ all -> 0x0f2c }
            int r8 = r5 + 1
            com.google.android.gms.internal.measurement.zzcf[] r9 = r3.zzxo     // Catch:{ all -> 0x0f2c }
            int r10 = r13 - r5
            r11 = 1
            int r10 = r10 - r11
            java.lang.System.arraycopy(r6, r8, r9, r5, r10)     // Catch:{ all -> 0x0f2c }
            int r13 = r13 + -1
            int r5 = r5 + -1
            r6 = 1
            goto L_0x082d
        L_0x07fc:
            if (r4 == 0) goto L_0x082c
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "_et"
            com.google.android.gms.internal.measurement.zzbt$zzd r6 = com.google.android.gms.measurement.internal.zzfz.zza(r6, r8)     // Catch:{ all -> 0x0f2c }
            if (r6 == 0) goto L_0x082c
            boolean r8 = r6.zzhn()     // Catch:{ all -> 0x0f2c }
            if (r8 == 0) goto L_0x0818
            long r8 = r6.zzho()     // Catch:{ all -> 0x0f2c }
            java.lang.Long r6 = java.lang.Long.valueOf(r8)     // Catch:{ all -> 0x0f2c }
            goto L_0x0819
        L_0x0818:
            r6 = 0
        L_0x0819:
            if (r6 == 0) goto L_0x082c
            long r8 = r6.longValue()     // Catch:{ all -> 0x0f2c }
            r10 = 0
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 <= 0) goto L_0x082c
            long r8 = r6.longValue()     // Catch:{ all -> 0x0f2c }
            long r14 = r14 + r8
            r6 = 1
            goto L_0x082d
        L_0x082c:
            r6 = 1
        L_0x082d:
            int r5 = r5 + r6
            goto L_0x07ce
        L_0x082f:
            r29 = r14
            goto L_0x0834
        L_0x0832:
            r13 = r25
        L_0x0834:
            java.util.List<com.google.android.gms.internal.measurement.zzcf> r5 = r2.zzsv     // Catch:{ all -> 0x0f2c }
            int r5 = r5.size()     // Catch:{ all -> 0x0f2c }
            if (r13 >= r5) goto L_0x0846
            com.google.android.gms.internal.measurement.zzcf[] r5 = r3.zzxo     // Catch:{ all -> 0x0f2c }
            java.lang.Object[] r5 = java.util.Arrays.copyOf(r5, r13)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzcf[] r5 = (com.google.android.gms.internal.measurement.zzcf[]) r5     // Catch:{ all -> 0x0f2c }
            r3.zzxo = r5     // Catch:{ all -> 0x0f2c }
        L_0x0846:
            if (r4 == 0) goto L_0x0927
            com.google.android.gms.measurement.internal.zzw r4 = r46.zzdo()     // Catch:{ all -> 0x0f2c }
            java.lang.String r5 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = "_lte"
            com.google.android.gms.measurement.internal.zzgc r4 = r4.zze(r5, r6)     // Catch:{ all -> 0x0f2c }
            if (r4 == 0) goto L_0x0881
            java.lang.Object r5 = r4.value     // Catch:{ all -> 0x0f2c }
            if (r5 != 0) goto L_0x085b
            goto L_0x0881
        L_0x085b:
            com.google.android.gms.measurement.internal.zzgc r5 = new com.google.android.gms.measurement.internal.zzgc     // Catch:{ all -> 0x0f2c }
            java.lang.String r9 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = "auto"
            java.lang.String r11 = "_lte"
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.common.util.Clock r6 = r6.zzz()     // Catch:{ all -> 0x0f2c }
            long r12 = r6.currentTimeMillis()     // Catch:{ all -> 0x0f2c }
            java.lang.Object r4 = r4.value     // Catch:{ all -> 0x0f2c }
            java.lang.Long r4 = (java.lang.Long) r4     // Catch:{ all -> 0x0f2c }
            long r14 = r4.longValue()     // Catch:{ all -> 0x0f2c }
            long r14 = r14 + r29
            java.lang.Long r14 = java.lang.Long.valueOf(r14)     // Catch:{ all -> 0x0f2c }
            r8 = r5
            r8.<init>(r9, r10, r11, r12, r14)     // Catch:{ all -> 0x0f2c }
            r4 = r5
            goto L_0x089e
        L_0x0881:
            com.google.android.gms.measurement.internal.zzgc r4 = new com.google.android.gms.measurement.internal.zzgc     // Catch:{ all -> 0x0f2c }
            java.lang.String r5 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r33 = "auto"
            java.lang.String r34 = "_lte"
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.common.util.Clock r6 = r6.zzz()     // Catch:{ all -> 0x0f2c }
            long r35 = r6.currentTimeMillis()     // Catch:{ all -> 0x0f2c }
            java.lang.Long r37 = java.lang.Long.valueOf(r29)     // Catch:{ all -> 0x0f2c }
            r31 = r4
            r32 = r5
            r31.<init>(r32, r33, r34, r35, r37)     // Catch:{ all -> 0x0f2c }
        L_0x089e:
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r5 = com.google.android.gms.internal.measurement.zzbt.zzh.zziu()     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = "_lte"
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r5 = r5.zzby(r6)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.common.util.Clock r6 = r6.zzz()     // Catch:{ all -> 0x0f2c }
            long r8 = r6.currentTimeMillis()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r5 = r5.zzan(r8)     // Catch:{ all -> 0x0f2c }
            java.lang.Object r6 = r4.value     // Catch:{ all -> 0x0f2c }
            java.lang.Long r6 = (java.lang.Long) r6     // Catch:{ all -> 0x0f2c }
            long r8 = r6.longValue()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r5 = r5.zzao(r8)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzgh r5 = r5.zzmr()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez r5 = (com.google.android.gms.internal.measurement.zzez) r5     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh r5 = (com.google.android.gms.internal.measurement.zzbt.zzh) r5     // Catch:{ all -> 0x0f2c }
            r6 = 0
        L_0x08cb:
            com.google.android.gms.internal.measurement.zzbt$zzh[] r8 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            int r8 = r8.length     // Catch:{ all -> 0x0f2c }
            if (r6 >= r8) goto L_0x08e9
            java.lang.String r8 = "_lte"
            com.google.android.gms.internal.measurement.zzbt$zzh[] r9 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            r9 = r9[r6]     // Catch:{ all -> 0x0f2c }
            java.lang.String r9 = r9.getName()     // Catch:{ all -> 0x0f2c }
            boolean r8 = r8.equals(r9)     // Catch:{ all -> 0x0f2c }
            if (r8 == 0) goto L_0x08e6
            com.google.android.gms.internal.measurement.zzbt$zzh[] r8 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            r8[r6] = r5     // Catch:{ all -> 0x0f2c }
            r6 = 1
            goto L_0x08ea
        L_0x08e6:
            int r6 = r6 + 1
            goto L_0x08cb
        L_0x08e9:
            r6 = 0
        L_0x08ea:
            if (r6 != 0) goto L_0x0909
            com.google.android.gms.internal.measurement.zzbt$zzh[] r6 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r8 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            int r8 = r8.length     // Catch:{ all -> 0x0f2c }
            r9 = 1
            int r8 = r8 + r9
            java.lang.Object[] r6 = java.util.Arrays.copyOf(r6, r8)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r6 = (com.google.android.gms.internal.measurement.zzbt.zzh[]) r6     // Catch:{ all -> 0x0f2c }
            r3.zzxp = r6     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r6 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r8 = r2.zzst     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r8 = r8.zzxp     // Catch:{ all -> 0x0f2c }
            int r8 = r8.length     // Catch:{ all -> 0x0f2c }
            r9 = 1
            int r8 = r8 - r9
            r6[r8] = r5     // Catch:{ all -> 0x0f2c }
            r5 = 0
            goto L_0x090b
        L_0x0909:
            r5 = 0
        L_0x090b:
            int r8 = (r29 > r5 ? 1 : (r29 == r5 ? 0 : -1))
            if (r8 <= 0) goto L_0x0927
            com.google.android.gms.measurement.internal.zzw r5 = r46.zzdo()     // Catch:{ all -> 0x0f2c }
            r5.zza(r4)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzby r5 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r5 = r5.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzdh()     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = "Updated lifetime engagement user property with value. Value"
            java.lang.Object r4 = r4.value     // Catch:{ all -> 0x0f2c }
            r5.zza(r6, r4)     // Catch:{ all -> 0x0f2c }
        L_0x0927:
            com.google.android.gms.measurement.internal.zzby r4 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzt r4 = r4.zzaf()     // Catch:{ all -> 0x0f2c }
            java.lang.String r5 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzal$zza<java.lang.Boolean> r6 = com.google.android.gms.measurement.internal.zzal.zzin     // Catch:{ all -> 0x0f2c }
            boolean r4 = r4.zze(r5, r6)     // Catch:{ all -> 0x0f2c }
            if (r4 == 0) goto L_0x09db
            com.google.android.gms.measurement.internal.zzfz r4 = r46.zzdm()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r5 = r4.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzdi()     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = "Checking account type status for ad personalization signals"
            r5.zzaq(r6)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzbs r5 = r4.zzdp()     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            boolean r5 = r5.zzbc(r6)     // Catch:{ all -> 0x0f2c }
            if (r5 == 0) goto L_0x09db
            com.google.android.gms.measurement.internal.zzw r5 = r4.zzdo()     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzg r5 = r5.zzae(r6)     // Catch:{ all -> 0x0f2c }
            if (r5 == 0) goto L_0x09db
            boolean r5 = r5.zzbl()     // Catch:{ all -> 0x0f2c }
            if (r5 == 0) goto L_0x09db
            com.google.android.gms.measurement.internal.zzad r5 = r4.zzy()     // Catch:{ all -> 0x0f2c }
            boolean r5 = r5.zzcs()     // Catch:{ all -> 0x0f2c }
            if (r5 == 0) goto L_0x09db
            com.google.android.gms.measurement.internal.zzau r5 = r4.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzdh()     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = "Turning off ad personalization due to account type"
            r5.zzaq(r6)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r5 = com.google.android.gms.internal.measurement.zzbt.zzh.zziu()     // Catch:{ all -> 0x0f2c }
            java.lang.String r6 = "_npa"
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r5 = r5.zzby(r6)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzad r4 = r4.zzy()     // Catch:{ all -> 0x0f2c }
            long r8 = r4.zzcq()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r4 = r5.zzan(r8)     // Catch:{ all -> 0x0f2c }
            r5 = 1
            com.google.android.gms.internal.measurement.zzbt$zzh$zza r4 = r4.zzao(r5)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzgh r4 = r4.zzmr()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzez r4 = (com.google.android.gms.internal.measurement.zzez) r4     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh r4 = (com.google.android.gms.internal.measurement.zzbt.zzh) r4     // Catch:{ all -> 0x0f2c }
            r5 = 0
        L_0x09a2:
            com.google.android.gms.internal.measurement.zzbt$zzh[] r6 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            int r6 = r6.length     // Catch:{ all -> 0x0f2c }
            if (r5 >= r6) goto L_0x09c0
            java.lang.String r6 = "_npa"
            com.google.android.gms.internal.measurement.zzbt$zzh[] r8 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            r8 = r8[r5]     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r8.getName()     // Catch:{ all -> 0x0f2c }
            boolean r6 = r6.equals(r8)     // Catch:{ all -> 0x0f2c }
            if (r6 == 0) goto L_0x09bd
            com.google.android.gms.internal.measurement.zzbt$zzh[] r6 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            r6[r5] = r4     // Catch:{ all -> 0x0f2c }
            r5 = 1
            goto L_0x09c1
        L_0x09bd:
            int r5 = r5 + 1
            goto L_0x09a2
        L_0x09c0:
            r5 = 0
        L_0x09c1:
            if (r5 != 0) goto L_0x09db
            com.google.android.gms.internal.measurement.zzbt$zzh[] r5 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r6 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            int r6 = r6.length     // Catch:{ all -> 0x0f2c }
            r8 = 1
            int r6 = r6 + r8
            java.lang.Object[] r5 = java.util.Arrays.copyOf(r5, r6)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r5 = (com.google.android.gms.internal.measurement.zzbt.zzh[]) r5     // Catch:{ all -> 0x0f2c }
            r3.zzxp = r5     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r5 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r6 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            int r6 = r6.length     // Catch:{ all -> 0x0f2c }
            r8 = 1
            int r6 = r6 - r8
            r5[r6] = r4     // Catch:{ all -> 0x0f2c }
        L_0x09db:
            java.lang.String r4 = r3.zzcf     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzh[] r5 = r3.zzxp     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzcf[] r6 = r3.zzxo     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r4)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzo r8 = r46.zzdn()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zza[] r4 = r8.zza(r4, r6, r5)     // Catch:{ all -> 0x0f2c }
            r3.zzyg = r4     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzby r4 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzt r4 = r4.zzaf()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r5 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r5 = r5.zzcf     // Catch:{ all -> 0x0f2c }
            boolean r4 = r4.zzl(r5)     // Catch:{ all -> 0x0f2c }
            if (r4 == 0) goto L_0x0d44
            java.util.HashMap r4 = new java.util.HashMap     // Catch:{ all -> 0x0d3e }
            r4.<init>()     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzcf[] r5 = r3.zzxo     // Catch:{ all -> 0x0d3e }
            int r5 = r5.length     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzcf[] r5 = new com.google.android.gms.internal.measurement.zzcf[r5]     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.measurement.internal.zzby r6 = r1.zzl     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.measurement.internal.zzgd r6 = r6.zzab()     // Catch:{ all -> 0x0d3e }
            java.security.SecureRandom r6 = r6.zzgl()     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzcf[] r8 = r3.zzxo     // Catch:{ all -> 0x0d3e }
            int r9 = r8.length     // Catch:{ all -> 0x0d3e }
            r10 = 0
            r11 = 0
        L_0x0a17:
            if (r10 >= r9) goto L_0x0d0d
            r12 = r8[r10]     // Catch:{ all -> 0x0d3e }
            java.lang.String r13 = r12.name     // Catch:{ all -> 0x0d3e }
            java.lang.String r14 = "_ep"
            boolean r13 = r13.equals(r14)     // Catch:{ all -> 0x0d3e }
            if (r13 == 0) goto L_0x0aaa
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            java.lang.String r13 = "_en"
            java.lang.Object r13 = com.google.android.gms.measurement.internal.zzfz.zzb(r12, r13)     // Catch:{ all -> 0x0f2c }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ all -> 0x0f2c }
            java.lang.Object r14 = r4.get(r13)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaf r14 = (com.google.android.gms.measurement.internal.zzaf) r14     // Catch:{ all -> 0x0f2c }
            if (r14 != 0) goto L_0x0a47
            com.google.android.gms.measurement.internal.zzw r14 = r46.zzdo()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r15 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r15 = r15.zzcf     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaf r14 = r14.zzc(r15, r13)     // Catch:{ all -> 0x0f2c }
            r4.put(r13, r14)     // Catch:{ all -> 0x0f2c }
        L_0x0a47:
            java.lang.Long r13 = r14.zzfj     // Catch:{ all -> 0x0f2c }
            if (r13 != 0) goto L_0x0a9d
            java.lang.Long r13 = r14.zzfk     // Catch:{ all -> 0x0f2c }
            long r26 = r13.longValue()     // Catch:{ all -> 0x0f2c }
            r19 = 1
            int r13 = (r26 > r19 ? 1 : (r26 == r19 ? 0 : -1))
            if (r13 <= 0) goto L_0x0a66
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r13 = r12.zzxi     // Catch:{ all -> 0x0f2c }
            java.lang.String r15 = "_sr"
            java.lang.Long r7 = r14.zzfk     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = com.google.android.gms.measurement.internal.zzfz.zza(r13, r15, r7)     // Catch:{ all -> 0x0f2c }
            r12.zzxi = r7     // Catch:{ all -> 0x0f2c }
        L_0x0a66:
            java.lang.Boolean r7 = r14.zzfl     // Catch:{ all -> 0x0f2c }
            if (r7 == 0) goto L_0x0a8b
            java.lang.Boolean r7 = r14.zzfl     // Catch:{ all -> 0x0f2c }
            boolean r7 = r7.booleanValue()     // Catch:{ all -> 0x0f2c }
            if (r7 == 0) goto L_0x0a88
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = r12.zzxi     // Catch:{ all -> 0x0f2c }
            java.lang.String r13 = "_efs"
            r23 = r8
            r14 = 1
            java.lang.Long r8 = java.lang.Long.valueOf(r14)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r7 = com.google.android.gms.measurement.internal.zzfz.zza(r7, r13, r8)     // Catch:{ all -> 0x0f2c }
            r12.zzxi = r7     // Catch:{ all -> 0x0f2c }
            goto L_0x0a8d
        L_0x0a88:
            r23 = r8
            goto L_0x0a8d
        L_0x0a8b:
            r23 = r8
        L_0x0a8d:
            int r7 = r11 + 1
            r5[r11] = r12     // Catch:{ all -> 0x0f2c }
            r28 = r3
            r20 = r6
            r11 = r7
            r25 = r9
            r26 = r10
            r10 = r2
            goto L_0x0cfd
        L_0x0a9d:
            r23 = r8
            r28 = r3
            r20 = r6
            r25 = r9
            r26 = r10
            r10 = r2
            goto L_0x0cfd
        L_0x0aaa:
            r23 = r8
            com.google.android.gms.measurement.internal.zzbs r7 = r46.zzdp()     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzch r8 = r2.zzst     // Catch:{ all -> 0x0d3e }
            java.lang.String r8 = r8.zzcf     // Catch:{ all -> 0x0d3e }
            long r7 = r7.zzbd(r8)     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.measurement.internal.zzby r13 = r1.zzl     // Catch:{ all -> 0x0d3e }
            r13.zzab()     // Catch:{ all -> 0x0d3e }
            java.lang.Long r13 = r12.zzxj     // Catch:{ all -> 0x0d3e }
            long r13 = r13.longValue()     // Catch:{ all -> 0x0d3e }
            long r13 = com.google.android.gms.measurement.internal.zzgd.zzc(r13, r7)     // Catch:{ all -> 0x0d3e }
            java.lang.String r15 = "_dbg"
            r25 = r9
            r20 = 1
            java.lang.Long r9 = java.lang.Long.valueOf(r20)     // Catch:{ all -> 0x0d3e }
            boolean r26 = android.text.TextUtils.isEmpty(r15)     // Catch:{ all -> 0x0d3e }
            if (r26 != 0) goto L_0x0b31
            if (r9 != 0) goto L_0x0ada
            goto L_0x0b31
        L_0x0ada:
            r28 = r3
            com.google.android.gms.internal.measurement.zzbt$zzd[] r3 = r12.zzxi     // Catch:{ all -> 0x0f2c }
            r26 = r10
            int r10 = r3.length     // Catch:{ all -> 0x0f2c }
            r29 = r7
            r7 = 0
        L_0x0ae4:
            if (r7 >= r10) goto L_0x0b2f
            r8 = r3[r7]     // Catch:{ all -> 0x0f2c }
            r27 = r3
            java.lang.String r3 = r8.getName()     // Catch:{ all -> 0x0f2c }
            boolean r3 = r15.equals(r3)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x0b2a
            boolean r3 = r9 instanceof java.lang.Long     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x0b06
            long r31 = r8.zzho()     // Catch:{ all -> 0x0f2c }
            java.lang.Long r3 = java.lang.Long.valueOf(r31)     // Catch:{ all -> 0x0f2c }
            boolean r3 = r9.equals(r3)     // Catch:{ all -> 0x0f2c }
            if (r3 != 0) goto L_0x0b26
        L_0x0b06:
            boolean r3 = r9 instanceof java.lang.String     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x0b14
            java.lang.String r3 = r8.zzhl()     // Catch:{ all -> 0x0f2c }
            boolean r3 = r9.equals(r3)     // Catch:{ all -> 0x0f2c }
            if (r3 != 0) goto L_0x0b26
        L_0x0b14:
            boolean r3 = r9 instanceof java.lang.Double     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x0b28
            double r7 = r8.zzhr()     // Catch:{ all -> 0x0f2c }
            java.lang.Double r3 = java.lang.Double.valueOf(r7)     // Catch:{ all -> 0x0f2c }
            boolean r3 = r9.equals(r3)     // Catch:{ all -> 0x0f2c }
            if (r3 == 0) goto L_0x0b28
        L_0x0b26:
            r3 = 1
            goto L_0x0b38
        L_0x0b28:
            r3 = 0
            goto L_0x0b38
        L_0x0b2a:
            int r7 = r7 + 1
            r3 = r27
            goto L_0x0ae4
        L_0x0b2f:
            r3 = 0
            goto L_0x0b38
        L_0x0b31:
            r28 = r3
            r29 = r7
            r26 = r10
            r3 = 0
        L_0x0b38:
            if (r3 != 0) goto L_0x0b49
            com.google.android.gms.measurement.internal.zzbs r3 = r46.zzdp()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r7 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r7 = r7.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r12.name     // Catch:{ all -> 0x0f2c }
            int r3 = r3.zzm(r7, r8)     // Catch:{ all -> 0x0f2c }
            goto L_0x0b4a
        L_0x0b49:
            r3 = 1
        L_0x0b4a:
            if (r3 > 0) goto L_0x0b6b
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r7 = r7.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r7 = r7.zzdd()     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "Sample rate must be positive. event, rate"
            java.lang.String r9 = r12.name     // Catch:{ all -> 0x0f2c }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0f2c }
            r7.zza(r8, r9, r3)     // Catch:{ all -> 0x0f2c }
            int r3 = r11 + 1
            r5[r11] = r12     // Catch:{ all -> 0x0f2c }
            r10 = r2
            r11 = r3
            r20 = r6
            goto L_0x0cfd
        L_0x0b6b:
            java.lang.String r7 = r12.name     // Catch:{ all -> 0x0d3e }
            java.lang.Object r7 = r4.get(r7)     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.measurement.internal.zzaf r7 = (com.google.android.gms.measurement.internal.zzaf) r7     // Catch:{ all -> 0x0d3e }
            if (r7 != 0) goto L_0x0bbf
            com.google.android.gms.measurement.internal.zzw r7 = r46.zzdo()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r8 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r8.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r9 = r12.name     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaf r7 = r7.zzc(r8, r9)     // Catch:{ all -> 0x0f2c }
            if (r7 != 0) goto L_0x0bbf
            com.google.android.gms.measurement.internal.zzby r7 = r1.zzl     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzau r7 = r7.zzad()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaw r7 = r7.zzdd()     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = "Event being bundled has no eventAggregate. appId, eventName"
            com.google.android.gms.internal.measurement.zzch r9 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r9 = r9.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = r12.name     // Catch:{ all -> 0x0f2c }
            r7.zza(r8, r9, r10)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaf r7 = new com.google.android.gms.measurement.internal.zzaf     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzch r8 = r2.zzst     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r8.zzcf     // Catch:{ all -> 0x0f2c }
            java.lang.String r9 = r12.name     // Catch:{ all -> 0x0f2c }
            r34 = 1
            r36 = 1
            java.lang.Long r10 = r12.zzxj     // Catch:{ all -> 0x0f2c }
            long r38 = r10.longValue()     // Catch:{ all -> 0x0f2c }
            r40 = 0
            r42 = 0
            r43 = 0
            r44 = 0
            r45 = 0
            r31 = r7
            r32 = r8
            r33 = r9
            r31.<init>(r32, r33, r34, r36, r38, r40, r42, r43, r44, r45)     // Catch:{ all -> 0x0f2c }
        L_0x0bbf:
            r46.zzdm()     // Catch:{ all -> 0x0d3e }
            java.lang.String r8 = "_eid"
            java.lang.Object r8 = com.google.android.gms.measurement.internal.zzfz.zzb(r12, r8)     // Catch:{ all -> 0x0d3e }
            java.lang.Long r8 = (java.lang.Long) r8     // Catch:{ all -> 0x0d3e }
            if (r8 == 0) goto L_0x0bce
            r9 = 1
            goto L_0x0bcf
        L_0x0bce:
            r9 = 0
        L_0x0bcf:
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)     // Catch:{ all -> 0x0d3e }
            r10 = 1
            if (r3 != r10) goto L_0x0bfc
            int r3 = r11 + 1
            r5[r11] = r12     // Catch:{ all -> 0x0f2c }
            boolean r8 = r9.booleanValue()     // Catch:{ all -> 0x0f2c }
            if (r8 == 0) goto L_0x0bf6
            java.lang.Long r8 = r7.zzfj     // Catch:{ all -> 0x0f2c }
            if (r8 != 0) goto L_0x0bec
            java.lang.Long r8 = r7.zzfk     // Catch:{ all -> 0x0f2c }
            if (r8 != 0) goto L_0x0bec
            java.lang.Boolean r8 = r7.zzfl     // Catch:{ all -> 0x0f2c }
            if (r8 == 0) goto L_0x0bf6
        L_0x0bec:
            r8 = 0
            com.google.android.gms.measurement.internal.zzaf r7 = r7.zza(r8, r8, r8)     // Catch:{ all -> 0x0f2c }
            java.lang.String r8 = r12.name     // Catch:{ all -> 0x0f2c }
            r4.put(r8, r7)     // Catch:{ all -> 0x0f2c }
        L_0x0bf6:
            r10 = r2
            r11 = r3
            r20 = r6
            goto L_0x0cfd
        L_0x0bfc:
            int r10 = r6.nextInt(r3)     // Catch:{ all -> 0x0d3e }
            if (r10 != 0) goto L_0x0c3f
            r46.zzdm()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r8 = r12.zzxi     // Catch:{ all -> 0x0f2c }
            java.lang.String r10 = "_sr"
            r47 = r2
            long r2 = (long) r3     // Catch:{ all -> 0x0f2c }
            java.lang.Long r15 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r8 = com.google.android.gms.measurement.internal.zzfz.zza(r8, r10, r15)     // Catch:{ all -> 0x0f2c }
            r12.zzxi = r8     // Catch:{ all -> 0x0f2c }
            int r8 = r11 + 1
            r5[r11] = r12     // Catch:{ all -> 0x0f2c }
            boolean r9 = r9.booleanValue()     // Catch:{ all -> 0x0f2c }
            if (r9 == 0) goto L_0x0c29
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x0f2c }
            r3 = 0
            com.google.android.gms.measurement.internal.zzaf r7 = r7.zza(r3, r2, r3)     // Catch:{ all -> 0x0f2c }
        L_0x0c29:
            java.lang.String r2 = r12.name     // Catch:{ all -> 0x0f2c }
            java.lang.Long r3 = r12.zzxj     // Catch:{ all -> 0x0f2c }
            long r9 = r3.longValue()     // Catch:{ all -> 0x0f2c }
            com.google.android.gms.measurement.internal.zzaf r3 = r7.zza(r9, r13)     // Catch:{ all -> 0x0f2c }
            r4.put(r2, r3)     // Catch:{ all -> 0x0f2c }
            r10 = r47
            r20 = r6
            r11 = r8
            goto L_0x0cfd
        L_0x0c3f:
            r47 = r2
            com.google.android.gms.measurement.internal.zzby r2 = r1.zzl     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.measurement.internal.zzt r2 = r2.zzaf()     // Catch:{ all -> 0x0d3e }
            r10 = r47
            com.google.android.gms.internal.measurement.zzch r15 = r10.zzst     // Catch:{ all -> 0x0d3e }
            java.lang.String r15 = r15.zzcf     // Catch:{ all -> 0x0d3e }
            boolean r2 = r2.zzx(r15)     // Catch:{ all -> 0x0d3e }
            if (r2 == 0) goto L_0x0c7d
            java.lang.Long r2 = r7.zzfi     // Catch:{ all -> 0x0d3e }
            if (r2 == 0) goto L_0x0c61
            java.lang.Long r2 = r7.zzfi     // Catch:{ all -> 0x0f2c }
            long r29 = r2.longValue()     // Catch:{ all -> 0x0f2c }
            r47 = r8
            r15 = r9
            goto L_0x0c75
        L_0x0c61:
            com.google.android.gms.measurement.internal.zzby r2 = r1.zzl     // Catch:{ all -> 0x0d3e }
            r2.zzab()     // Catch:{ all -> 0x0d3e }
            java.lang.Long r2 = r12.zzxk     // Catch:{ all -> 0x0d3e }
            long r1 = r2.longValue()     // Catch:{ all -> 0x0d3e }
            r47 = r8
            r15 = r9
            r8 = r29
            long r29 = com.google.android.gms.measurement.internal.zzgd.zzc(r1, r8)     // Catch:{ all -> 0x0d3e }
        L_0x0c75:
            int r1 = (r29 > r13 ? 1 : (r29 == r13 ? 0 : -1))
            if (r1 == 0) goto L_0x0c7b
            r1 = 1
            goto L_0x0c97
        L_0x0c7b:
            r1 = 0
            goto L_0x0c97
        L_0x0c7d:
            r47 = r8
            r15 = r9
            long r1 = r7.zzfh     // Catch:{ all -> 0x0d3e }
            java.lang.Long r8 = r12.zzxj     // Catch:{ all -> 0x0d3e }
            long r8 = r8.longValue()     // Catch:{ all -> 0x0d3e }
            long r8 = r8 - r1
            long r1 = java.lang.Math.abs(r8)     // Catch:{ all -> 0x0d3e }
            r8 = 86400000(0x5265c00, double:4.2687272E-316)
            int r1 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r1 < 0) goto L_0x0c96
            r1 = 1
            goto L_0x0c97
        L_0x0c96:
            r1 = 0
        L_0x0c97:
            if (r1 == 0) goto L_0x0ce9
            r46.zzdm()     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r1 = r12.zzxi     // Catch:{ all -> 0x0d3e }
            java.lang.String r2 = "_efs"
            r20 = r6
            r8 = 1
            java.lang.Long r6 = java.lang.Long.valueOf(r8)     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r1 = com.google.android.gms.measurement.internal.zzfz.zza(r1, r2, r6)     // Catch:{ all -> 0x0d3e }
            r12.zzxi = r1     // Catch:{ all -> 0x0d3e }
            r46.zzdm()     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r1 = r12.zzxi     // Catch:{ all -> 0x0d3e }
            java.lang.String r2 = "_sr"
            long r8 = (long) r3     // Catch:{ all -> 0x0d3e }
            java.lang.Long r3 = java.lang.Long.valueOf(r8)     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzbt$zzd[] r1 = com.google.android.gms.measurement.internal.zzfz.zza(r1, r2, r3)     // Catch:{ all -> 0x0d3e }
            r12.zzxi = r1     // Catch:{ all -> 0x0d3e }
            int r1 = r11 + 1
            r5[r11] = r12     // Catch:{ all -> 0x0d3e }
            boolean r2 = r15.booleanValue()     // Catch:{ all -> 0x0d3e }
            if (r2 == 0) goto L_0x0cd8
            java.lang.Long r2 = java.lang.Long.valueOf(r8)     // Catch:{ all -> 0x0d3e }
            r3 = 1
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r3)     // Catch:{ all -> 0x0d3e }
            r3 = 0
            com.google.android.gms.measurement.internal.zzaf r7 = r7.zza(r3, r2, r6)     // Catch:{ all -> 0x0d3e }
        L_0x0cd8:
            java.lang.String r2 = r12.name     // Catch:{ all -> 0x0d3e }
            java.lang.Long r3 = r12.zzxj     // Catch:{ all -> 0x0d3e }
            long r8 = r3.longValue()     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.measurement.internal.zzaf r3 = r7.zza(r8, r13)     // Catch:{ all -> 0x0d3e }
            r4.put(r2, r3)     // Catch:{ all -> 0x0d3e }
            r11 = r1
            goto L_0x0cfd
        L_0x0ce9:
            r20 = r6
            boolean r1 = r15.booleanValue()     // Catch:{ all -> 0x0d3e }
            if (r1 == 0) goto L_0x0cfd
            java.lang.String r1 = r12.name     // Catch:{ all -> 0x0d3e }
            r8 = r47
            r2 = 0
            com.google.android.gms.measurement.internal.zzaf r3 = r7.zza(r8, r2, r2)     // Catch:{ all -> 0x0d3e }
            r4.put(r1, r3)     // Catch:{ all -> 0x0d3e }
        L_0x0cfd:
            int r1 = r26 + 1
            r2 = r10
            r6 = r20
            r8 = r23
            r9 = r25
            r3 = r28
            r10 = r1
            r1 = r46
            goto L_0x0a17
        L_0x0d0d:
            r10 = r2
            r1 = r3
            com.google.android.gms.internal.measurement.zzcf[] r2 = r1.zzxo     // Catch:{ all -> 0x0d3e }
            int r2 = r2.length     // Catch:{ all -> 0x0d3e }
            if (r11 >= r2) goto L_0x0d1c
            java.lang.Object[] r2 = java.util.Arrays.copyOf(r5, r11)     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.internal.measurement.zzcf[] r2 = (com.google.android.gms.internal.measurement.zzcf[]) r2     // Catch:{ all -> 0x0d3e }
            r1.zzxo = r2     // Catch:{ all -> 0x0d3e }
        L_0x0d1c:
            java.util.Set r2 = r4.entrySet()     // Catch:{ all -> 0x0d3e }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0d3e }
        L_0x0d24:
            boolean r3 = r2.hasNext()     // Catch:{ all -> 0x0d3e }
            if (r3 == 0) goto L_0x0d46
            java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x0d3e }
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.measurement.internal.zzw r4 = r46.zzdo()     // Catch:{ all -> 0x0d3e }
            java.lang.Object r3 = r3.getValue()     // Catch:{ all -> 0x0d3e }
            com.google.android.gms.measurement.internal.zzaf r3 = (com.google.android.gms.measurement.internal.zzaf) r3     // Catch:{ all -> 0x0d3e }
            r4.zza(r3)     // Catch:{ all -> 0x0d3e }
            goto L_0x0d24
        L_0x0d3e:
            r0 = move-exception
            r1 = r0
            r4 = r46
            goto L_0x0f2f
        L_0x0d44:
            r10 = r2
            r1 = r3
        L_0x0d46:
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x0f0a }
            r1.zzxr = r2     // Catch:{ all -> 0x0f0a }
            r2 = -9223372036854775808
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ all -> 0x0f0a }
            r1.zzxs = r2     // Catch:{ all -> 0x0f0a }
            r2 = 0
        L_0x0d5a:
            com.google.android.gms.internal.measurement.zzcf[] r3 = r1.zzxo     // Catch:{ all -> 0x0f0a }
            int r3 = r3.length     // Catch:{ all -> 0x0f0a }
            if (r2 >= r3) goto L_0x0d8e
            com.google.android.gms.internal.measurement.zzcf[] r3 = r1.zzxo     // Catch:{ all -> 0x0d3e }
            r3 = r3[r2]     // Catch:{ all -> 0x0d3e }
            java.lang.Long r4 = r3.zzxj     // Catch:{ all -> 0x0d3e }
            long r4 = r4.longValue()     // Catch:{ all -> 0x0d3e }
            java.lang.Long r6 = r1.zzxr     // Catch:{ all -> 0x0d3e }
            long r6 = r6.longValue()     // Catch:{ all -> 0x0d3e }
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 >= 0) goto L_0x0d77
            java.lang.Long r4 = r3.zzxj     // Catch:{ all -> 0x0d3e }
            r1.zzxr = r4     // Catch:{ all -> 0x0d3e }
        L_0x0d77:
            java.lang.Long r4 = r3.zzxj     // Catch:{ all -> 0x0d3e }
            long r4 = r4.longValue()     // Catch:{ all -> 0x0d3e }
            java.lang.Long r6 = r1.zzxs     // Catch:{ all -> 0x0d3e }
            long r6 = r6.longValue()     // Catch:{ all -> 0x0d3e }
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 <= 0) goto L_0x0d8b
            java.lang.Long r3 = r3.zzxj     // Catch:{ all -> 0x0d3e }
            r1.zzxs = r3     // Catch:{ all -> 0x0d3e }
        L_0x0d8b:
            int r2 = r2 + 1
            goto L_0x0d5a
        L_0x0d8e:
            com.google.android.gms.internal.measurement.zzch r2 = r10.zzst     // Catch:{ all -> 0x0f0a }
            java.lang.String r2 = r2.zzcf     // Catch:{ all -> 0x0f0a }
            com.google.android.gms.measurement.internal.zzw r3 = r46.zzdo()     // Catch:{ all -> 0x0f0a }
            com.google.android.gms.measurement.internal.zzg r3 = r3.zzae(r2)     // Catch:{ all -> 0x0f0a }
            if (r3 != 0) goto L_0x0db6
            r4 = r46
            com.google.android.gms.measurement.internal.zzby r3 = r4.zzl     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzda()     // Catch:{ all -> 0x0f2a }
            java.lang.String r5 = "Bundling raw events w/o app info. appId"
            com.google.android.gms.internal.measurement.zzch r6 = r10.zzst     // Catch:{ all -> 0x0f2a }
            java.lang.String r6 = r6.zzcf     // Catch:{ all -> 0x0f2a }
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r6)     // Catch:{ all -> 0x0f2a }
            r3.zza(r5, r6)     // Catch:{ all -> 0x0f2a }
            goto L_0x0e14
        L_0x0db6:
            r4 = r46
            com.google.android.gms.internal.measurement.zzcf[] r5 = r1.zzxo     // Catch:{ all -> 0x0f2a }
            int r5 = r5.length     // Catch:{ all -> 0x0f2a }
            if (r5 <= 0) goto L_0x0e14
            long r5 = r3.zzar()     // Catch:{ all -> 0x0f2a }
            r7 = 0
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 == 0) goto L_0x0dcc
            java.lang.Long r7 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x0f2a }
            goto L_0x0dcd
        L_0x0dcc:
            r7 = 0
        L_0x0dcd:
            r1.zzxu = r7     // Catch:{ all -> 0x0f2a }
            long r7 = r3.zzaq()     // Catch:{ all -> 0x0f2a }
            r11 = 0
            int r9 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
            if (r9 != 0) goto L_0x0dda
            goto L_0x0ddb
        L_0x0dda:
            r5 = r7
        L_0x0ddb:
            int r7 = (r5 > r11 ? 1 : (r5 == r11 ? 0 : -1))
            if (r7 == 0) goto L_0x0de4
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x0f2a }
            goto L_0x0de5
        L_0x0de4:
            r5 = 0
        L_0x0de5:
            r1.zzxt = r5     // Catch:{ all -> 0x0f2a }
            r3.zzbb()     // Catch:{ all -> 0x0f2a }
            long r5 = r3.zzay()     // Catch:{ all -> 0x0f2a }
            int r5 = (int) r5     // Catch:{ all -> 0x0f2a }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0f2a }
            r1.zzye = r5     // Catch:{ all -> 0x0f2a }
            java.lang.Long r5 = r1.zzxr     // Catch:{ all -> 0x0f2a }
            long r5 = r5.longValue()     // Catch:{ all -> 0x0f2a }
            r3.zze(r5)     // Catch:{ all -> 0x0f2a }
            java.lang.Long r5 = r1.zzxs     // Catch:{ all -> 0x0f2a }
            long r5 = r5.longValue()     // Catch:{ all -> 0x0f2a }
            r3.zzf(r5)     // Catch:{ all -> 0x0f2a }
            java.lang.String r5 = r3.zzbj()     // Catch:{ all -> 0x0f2a }
            r1.zzdn = r5     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzw r5 = r46.zzdo()     // Catch:{ all -> 0x0f2a }
            r5.zza(r3)     // Catch:{ all -> 0x0f2a }
        L_0x0e14:
            com.google.android.gms.internal.measurement.zzcf[] r3 = r1.zzxo     // Catch:{ all -> 0x0f2a }
            int r3 = r3.length     // Catch:{ all -> 0x0f2a }
            if (r3 <= 0) goto L_0x0e69
            com.google.android.gms.measurement.internal.zzby r3 = r4.zzl     // Catch:{ all -> 0x0f2a }
            r3.zzag()     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzbs r3 = r46.zzdp()     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.internal.measurement.zzch r5 = r10.zzst     // Catch:{ all -> 0x0f2a }
            java.lang.String r5 = r5.zzcf     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.internal.measurement.zzce r3 = r3.zzay(r5)     // Catch:{ all -> 0x0f2a }
            if (r3 == 0) goto L_0x0e36
            java.lang.Long r5 = r3.zzxa     // Catch:{ all -> 0x0f2a }
            if (r5 != 0) goto L_0x0e31
            goto L_0x0e36
        L_0x0e31:
            java.lang.Long r3 = r3.zzxa     // Catch:{ all -> 0x0f2a }
            r1.zzyl = r3     // Catch:{ all -> 0x0f2a }
            goto L_0x0e60
        L_0x0e36:
            com.google.android.gms.internal.measurement.zzch r3 = r10.zzst     // Catch:{ all -> 0x0f2a }
            java.lang.String r3 = r3.zzch     // Catch:{ all -> 0x0f2a }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x0f2a }
            if (r3 == 0) goto L_0x0e49
            r5 = -1
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch:{ all -> 0x0f2a }
            r1.zzyl = r3     // Catch:{ all -> 0x0f2a }
            goto L_0x0e60
        L_0x0e49:
            com.google.android.gms.measurement.internal.zzby r3 = r4.zzl     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzau r3 = r3.zzad()     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzaw r3 = r3.zzdd()     // Catch:{ all -> 0x0f2a }
            java.lang.String r5 = "Did not find measurement config or missing version info. appId"
            com.google.android.gms.internal.measurement.zzch r6 = r10.zzst     // Catch:{ all -> 0x0f2a }
            java.lang.String r6 = r6.zzcf     // Catch:{ all -> 0x0f2a }
            java.lang.Object r6 = com.google.android.gms.measurement.internal.zzau.zzao(r6)     // Catch:{ all -> 0x0f2a }
            r3.zza(r5, r6)     // Catch:{ all -> 0x0f2a }
        L_0x0e60:
            com.google.android.gms.measurement.internal.zzw r3 = r46.zzdo()     // Catch:{ all -> 0x0f2a }
            r12 = r18
            r3.zza(r1, r12)     // Catch:{ all -> 0x0f2a }
        L_0x0e69:
            com.google.android.gms.measurement.internal.zzw r1 = r46.zzdo()     // Catch:{ all -> 0x0f2a }
            java.util.List<java.lang.Long> r3 = r10.zzsu     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r3)     // Catch:{ all -> 0x0f2a }
            r1.zzq()     // Catch:{ all -> 0x0f2a }
            r1.zzah()     // Catch:{ all -> 0x0f2a }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0f2a }
            java.lang.String r6 = "rowid in ("
            r5.<init>(r6)     // Catch:{ all -> 0x0f2a }
            r6 = 0
        L_0x0e80:
            int r7 = r3.size()     // Catch:{ all -> 0x0f2a }
            if (r6 >= r7) goto L_0x0e9d
            if (r6 == 0) goto L_0x0e8d
            java.lang.String r7 = ","
            r5.append(r7)     // Catch:{ all -> 0x0f2a }
        L_0x0e8d:
            java.lang.Object r7 = r3.get(r6)     // Catch:{ all -> 0x0f2a }
            java.lang.Long r7 = (java.lang.Long) r7     // Catch:{ all -> 0x0f2a }
            long r7 = r7.longValue()     // Catch:{ all -> 0x0f2a }
            r5.append(r7)     // Catch:{ all -> 0x0f2a }
            int r6 = r6 + 1
            goto L_0x0e80
        L_0x0e9d:
            java.lang.String r6 = ")"
            r5.append(r6)     // Catch:{ all -> 0x0f2a }
            android.database.sqlite.SQLiteDatabase r6 = r1.getWritableDatabase()     // Catch:{ all -> 0x0f2a }
            java.lang.String r7 = "raw_events"
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0f2a }
            r8 = 0
            int r5 = r6.delete(r7, r5, r8)     // Catch:{ all -> 0x0f2a }
            int r6 = r3.size()     // Catch:{ all -> 0x0f2a }
            if (r5 == r6) goto L_0x0ed0
            com.google.android.gms.measurement.internal.zzau r1 = r1.zzad()     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x0f2a }
            java.lang.String r6 = "Deleted fewer rows from raw events table than expected"
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x0f2a }
            int r3 = r3.size()     // Catch:{ all -> 0x0f2a }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0f2a }
            r1.zza(r6, r5, r3)     // Catch:{ all -> 0x0f2a }
        L_0x0ed0:
            com.google.android.gms.measurement.internal.zzw r1 = r46.zzdo()     // Catch:{ all -> 0x0f2a }
            android.database.sqlite.SQLiteDatabase r3 = r1.getWritableDatabase()     // Catch:{ all -> 0x0f2a }
            java.lang.String r5 = "delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)"
            r6 = 2
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ SQLiteException -> 0x0ee7 }
            r7 = 0
            r6[r7] = r2     // Catch:{ SQLiteException -> 0x0ee7 }
            r7 = 1
            r6[r7] = r2     // Catch:{ SQLiteException -> 0x0ee7 }
            r3.execSQL(r5, r6)     // Catch:{ SQLiteException -> 0x0ee7 }
            goto L_0x0efa
        L_0x0ee7:
            r0 = move-exception
            r3 = r0
            com.google.android.gms.measurement.internal.zzau r1 = r1.zzad()     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzda()     // Catch:{ all -> 0x0f2a }
            java.lang.String r5 = "Failed to remove unused event metadata. appId"
            java.lang.Object r2 = com.google.android.gms.measurement.internal.zzau.zzao(r2)     // Catch:{ all -> 0x0f2a }
            r1.zza(r5, r2, r3)     // Catch:{ all -> 0x0f2a }
        L_0x0efa:
            com.google.android.gms.measurement.internal.zzw r1 = r46.zzdo()     // Catch:{ all -> 0x0f2a }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzw r1 = r46.zzdo()
            r1.endTransaction()
            r1 = 1
            return r1
        L_0x0f0a:
            r0 = move-exception
            r4 = r46
            goto L_0x0f2e
        L_0x0f0e:
            r4 = r1
            com.google.android.gms.measurement.internal.zzw r1 = r46.zzdo()     // Catch:{ all -> 0x0f2a }
            r1.setTransactionSuccessful()     // Catch:{ all -> 0x0f2a }
            com.google.android.gms.measurement.internal.zzw r1 = r46.zzdo()
            r1.endTransaction()
            r1 = 0
            return r1
        L_0x0f1f:
            r0 = move-exception
            r4 = r1
            r1 = r0
            r22 = r6
        L_0x0f24:
            if (r22 == 0) goto L_0x0f29
            r22.close()     // Catch:{ all -> 0x0f2a }
        L_0x0f29:
            throw r1     // Catch:{ all -> 0x0f2a }
        L_0x0f2a:
            r0 = move-exception
            goto L_0x0f2e
        L_0x0f2c:
            r0 = move-exception
            r4 = r1
        L_0x0f2e:
            r1 = r0
        L_0x0f2f:
            com.google.android.gms.measurement.internal.zzw r2 = r46.zzdo()
            r2.endTransaction()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzft.zzd(java.lang.String, long):boolean");
    }

    private final boolean zza(zzcf zzcf, zzcf zzcf2) {
        Object obj;
        Preconditions.checkArgument("_e".equals(zzcf.name));
        zzdm();
        zzd zza2 = zzfz.zza(zzcf, "_sc");
        String str = null;
        if (zza2 == null) {
            obj = null;
        } else {
            obj = zza2.zzhl();
        }
        zzdm();
        zzd zza3 = zzfz.zza(zzcf2, "_pc");
        if (zza3 != null) {
            str = zza3.zzhl();
        }
        if (str == null || !str.equals(obj)) {
            return false;
        }
        zzdm();
        zzd zza4 = zzfz.zza(zzcf, "_et");
        if (!zza4.zzhn() || zza4.zzho() <= 0) {
            return true;
        }
        long zzho = zza4.zzho();
        zzdm();
        zzd zza5 = zzfz.zza(zzcf2, "_et");
        if (zza5 != null && zza5.zzho() > 0) {
            zzho += zza5.zzho();
        }
        zzdm();
        zzcf2.zzxi = zzfz.zza(zzcf2.zzxi, "_et", (Object) Long.valueOf(zzho));
        zzdm();
        zzcf.zzxi = zzfz.zza(zzcf.zzxi, "_fr", (Object) Long.valueOf(1));
        return true;
    }

    @VisibleForTesting
    private static zzd[] zza(zzd[] zzdArr, @NonNull String str) {
        int i = 0;
        while (true) {
            if (i >= zzdArr.length) {
                i = -1;
                break;
            } else if (str.equals(zzdArr[i].getName())) {
                break;
            } else {
                i++;
            }
        }
        if (i < 0) {
            return zzdArr;
        }
        return zza(zzdArr, i);
    }

    @VisibleForTesting
    private static zzd[] zza(zzd[] zzdArr, int i) {
        zzd[] zzdArr2 = new zzd[(zzdArr.length - 1)];
        if (i > 0) {
            System.arraycopy(zzdArr, 0, zzdArr2, 0, i);
        }
        if (i < zzdArr2.length) {
            System.arraycopy(zzdArr, i + 1, zzdArr2, i, zzdArr2.length - i);
        }
        return zzdArr2;
    }

    @VisibleForTesting
    private static zzd[] zza(zzd[] zzdArr, int i, String str) {
        for (zzd name : zzdArr) {
            if ("_err".equals(name.getName())) {
                return zzdArr;
            }
        }
        zzd[] zzdArr2 = new zzd[(zzdArr.length + 2)];
        System.arraycopy(zzdArr, 0, zzdArr2, 0, zzdArr.length);
        zzd zzd = (zzd) ((zzez) zzd.zzht().zzbw("_err").zzaj(Long.valueOf((long) i).longValue()).zzmr());
        zzd zzd2 = (zzd) ((zzez) zzd.zzht().zzbw("_ev").zzbx(str).zzmr());
        zzdArr2[zzdArr2.length - 2] = zzd;
        zzdArr2[zzdArr2.length - 1] = zzd2;
        return zzdArr2;
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: 0000 */
    @WorkerThread
    @VisibleForTesting
    public final void zza(int i, Throwable th, byte[] bArr, String str) {
        zzw zzdo;
        zzq();
        zzfy();
        if (bArr == null) {
            try {
                bArr = new byte[0];
            } catch (Throwable th2) {
                this.zzsj = false;
                zzgd();
                throw th2;
            }
        }
        List<Long> list = this.zzsn;
        this.zzsn = null;
        boolean z = true;
        if ((i == 200 || i == 204) && th == null) {
            try {
                this.zzl.zzae().zzlb.set(this.zzl.zzz().currentTimeMillis());
                this.zzl.zzae().zzlc.set(0);
                zzgc();
                this.zzl.zzad().zzdi().zza("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
                zzdo().beginTransaction();
                try {
                    for (Long l : list) {
                        try {
                            zzdo = zzdo();
                            long longValue = l.longValue();
                            zzdo.zzq();
                            zzdo.zzah();
                            if (zzdo.getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(longValue)}) != 1) {
                                throw new SQLiteException("Deleted fewer rows from queue than expected");
                            }
                        } catch (SQLiteException e) {
                            zzdo.zzad().zzda().zza("Failed to delete a bundle in a queue table", e);
                            throw e;
                        } catch (SQLiteException e2) {
                            if (this.zzso == null || !this.zzso.contains(l)) {
                                throw e2;
                            }
                        }
                    }
                    zzdo().setTransactionSuccessful();
                    zzdo().endTransaction();
                    this.zzso = null;
                    if (!zzfu().zzdl() || !zzgb()) {
                        this.zzsp = -1;
                        zzgc();
                    } else {
                        zzga();
                    }
                    this.zzse = 0;
                } catch (Throwable th3) {
                    zzdo().endTransaction();
                    throw th3;
                }
            } catch (SQLiteException e3) {
                this.zzl.zzad().zzda().zza("Database error while trying to delete uploaded bundles", e3);
                this.zzse = this.zzl.zzz().elapsedRealtime();
                this.zzl.zzad().zzdi().zza("Disable upload, time", Long.valueOf(this.zzse));
            }
        } else {
            this.zzl.zzad().zzdi().zza("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            this.zzl.zzae().zzlc.set(this.zzl.zzz().currentTimeMillis());
            if (i != 503) {
                if (i != 429) {
                    z = false;
                }
            }
            if (z) {
                this.zzl.zzae().zzld.set(this.zzl.zzz().currentTimeMillis());
            }
            if (this.zzl.zzaf().zzo(str)) {
                zzdo().zza(list);
            }
            zzgc();
        }
        this.zzsj = false;
        zzgd();
    }

    private final boolean zzgb() {
        zzq();
        zzfy();
        return zzdo().zzcd() || !TextUtils.isEmpty(zzdo().zzby());
    }

    @WorkerThread
    private final void zzb(zzg zzg) {
        Map map;
        zzq();
        if (!TextUtils.isEmpty(zzg.getGmpAppId()) || (zzt.zzbx() && !TextUtils.isEmpty(zzg.zzao()))) {
            zzt zzaf = this.zzl.zzaf();
            Builder builder = new Builder();
            String gmpAppId = zzg.getGmpAppId();
            if (TextUtils.isEmpty(gmpAppId) && zzt.zzbx()) {
                gmpAppId = zzg.zzao();
            }
            Builder encodedAuthority = builder.scheme((String) zzal.zzgh.get(null)).encodedAuthority((String) zzal.zzgi.get(null));
            String str = "config/app/";
            String valueOf = String.valueOf(gmpAppId);
            encodedAuthority.path(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).appendQueryParameter("app_instance_id", zzg.getAppInstanceId()).appendQueryParameter("platform", "android").appendQueryParameter("gmp_version", String.valueOf(zzaf.zzav()));
            String uri = builder.build().toString();
            try {
                URL url = new URL(uri);
                this.zzl.zzad().zzdi().zza("Fetching remote configuration", zzg.zzan());
                zzce zzay = zzdp().zzay(zzg.zzan());
                String zzaz = zzdp().zzaz(zzg.zzan());
                if (zzay == null || TextUtils.isEmpty(zzaz)) {
                    map = null;
                } else {
                    ArrayMap arrayMap = new ArrayMap();
                    arrayMap.put("If-Modified-Since", zzaz);
                    map = arrayMap;
                }
                this.zzsi = true;
                zzay zzfu = zzfu();
                String zzan = zzg.zzan();
                zzfw zzfw = new zzfw(this);
                zzfu.zzq();
                zzfu.zzah();
                Preconditions.checkNotNull(url);
                Preconditions.checkNotNull(zzfw);
                zzbt zzac = zzfu.zzac();
                zzbc zzbc = new zzbc(zzfu, zzan, url, null, map, zzfw);
                zzac.zzb((Runnable) zzbc);
            } catch (MalformedURLException unused) {
                this.zzl.zzad().zzda().zza("Failed to parse config URL. Not fetching. appId", zzau.zzao(zzg.zzan()), uri);
            }
        } else {
            zzb(zzg.zzan(), 204, null, null, null);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x013a A[Catch:{ all -> 0x018d, all -> 0x0196 }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x014a A[Catch:{ all -> 0x018d, all -> 0x0196 }] */
    @androidx.annotation.WorkerThread
    @com.google.android.gms.common.util.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzb(java.lang.String r7, int r8, java.lang.Throwable r9, byte[] r10, java.util.Map<java.lang.String, java.util.List<java.lang.String>> r11) {
        /*
            r6 = this;
            r6.zzq()
            r6.zzfy()
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r7)
            r0 = 0
            if (r10 != 0) goto L_0x000e
            byte[] r10 = new byte[r0]     // Catch:{ all -> 0x0196 }
        L_0x000e:
            com.google.android.gms.measurement.internal.zzby r1 = r6.zzl     // Catch:{ all -> 0x0196 }
            com.google.android.gms.measurement.internal.zzau r1 = r1.zzad()     // Catch:{ all -> 0x0196 }
            com.google.android.gms.measurement.internal.zzaw r1 = r1.zzdi()     // Catch:{ all -> 0x0196 }
            java.lang.String r2 = "onConfigFetched. Response size"
            int r3 = r10.length     // Catch:{ all -> 0x0196 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0196 }
            r1.zza(r2, r3)     // Catch:{ all -> 0x0196 }
            com.google.android.gms.measurement.internal.zzw r1 = r6.zzdo()     // Catch:{ all -> 0x0196 }
            r1.beginTransaction()     // Catch:{ all -> 0x0196 }
            com.google.android.gms.measurement.internal.zzw r1 = r6.zzdo()     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzg r1 = r1.zzae(r7)     // Catch:{ all -> 0x018d }
            r2 = 200(0xc8, float:2.8E-43)
            r3 = 304(0x130, float:4.26E-43)
            r4 = 1
            if (r8 == r2) goto L_0x003e
            r2 = 204(0xcc, float:2.86E-43)
            if (r8 == r2) goto L_0x003e
            if (r8 != r3) goto L_0x0042
        L_0x003e:
            if (r9 != 0) goto L_0x0042
            r2 = 1
            goto L_0x0043
        L_0x0042:
            r2 = 0
        L_0x0043:
            if (r1 != 0) goto L_0x005a
            com.google.android.gms.measurement.internal.zzby r8 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzau r8 = r8.zzad()     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzdd()     // Catch:{ all -> 0x018d }
            java.lang.String r9 = "App does not exist in onConfigFetched. appId"
            java.lang.Object r7 = com.google.android.gms.measurement.internal.zzau.zzao(r7)     // Catch:{ all -> 0x018d }
            r8.zza(r9, r7)     // Catch:{ all -> 0x018d }
            goto L_0x0179
        L_0x005a:
            r5 = 404(0x194, float:5.66E-43)
            if (r2 != 0) goto L_0x00ca
            if (r8 != r5) goto L_0x0061
            goto L_0x00ca
        L_0x0061:
            com.google.android.gms.measurement.internal.zzby r10 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.common.util.Clock r10 = r10.zzz()     // Catch:{ all -> 0x018d }
            long r10 = r10.currentTimeMillis()     // Catch:{ all -> 0x018d }
            r1.zzm(r10)     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzw r10 = r6.zzdo()     // Catch:{ all -> 0x018d }
            r10.zza(r1)     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzby r10 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzau r10 = r10.zzad()     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzaw r10 = r10.zzdi()     // Catch:{ all -> 0x018d }
            java.lang.String r11 = "Fetching config failed. code, error"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x018d }
            r10.zza(r11, r1, r9)     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzbs r9 = r6.zzdp()     // Catch:{ all -> 0x018d }
            r9.zzba(r7)     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzby r7 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzbf r7 = r7.zzae()     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzbi r7 = r7.zzlc     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzby r9 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.common.util.Clock r9 = r9.zzz()     // Catch:{ all -> 0x018d }
            long r9 = r9.currentTimeMillis()     // Catch:{ all -> 0x018d }
            r7.set(r9)     // Catch:{ all -> 0x018d }
            r7 = 503(0x1f7, float:7.05E-43)
            if (r8 == r7) goto L_0x00ae
            r7 = 429(0x1ad, float:6.01E-43)
            if (r8 != r7) goto L_0x00ad
            goto L_0x00ae
        L_0x00ad:
            r4 = 0
        L_0x00ae:
            if (r4 == 0) goto L_0x00c5
            com.google.android.gms.measurement.internal.zzby r7 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzbf r7 = r7.zzae()     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzbi r7 = r7.zzld     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzby r8 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.common.util.Clock r8 = r8.zzz()     // Catch:{ all -> 0x018d }
            long r8 = r8.currentTimeMillis()     // Catch:{ all -> 0x018d }
            r7.set(r8)     // Catch:{ all -> 0x018d }
        L_0x00c5:
            r6.zzgc()     // Catch:{ all -> 0x018d }
            goto L_0x0179
        L_0x00ca:
            r9 = 0
            if (r11 == 0) goto L_0x00d6
            java.lang.String r2 = "Last-Modified"
            java.lang.Object r11 = r11.get(r2)     // Catch:{ all -> 0x018d }
            java.util.List r11 = (java.util.List) r11     // Catch:{ all -> 0x018d }
            goto L_0x00d7
        L_0x00d6:
            r11 = r9
        L_0x00d7:
            if (r11 == 0) goto L_0x00e6
            int r2 = r11.size()     // Catch:{ all -> 0x018d }
            if (r2 <= 0) goto L_0x00e6
            java.lang.Object r11 = r11.get(r0)     // Catch:{ all -> 0x018d }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ all -> 0x018d }
            goto L_0x00e7
        L_0x00e6:
            r11 = r9
        L_0x00e7:
            if (r8 == r5) goto L_0x0103
            if (r8 != r3) goto L_0x00ec
            goto L_0x0103
        L_0x00ec:
            com.google.android.gms.measurement.internal.zzbs r9 = r6.zzdp()     // Catch:{ all -> 0x018d }
            boolean r9 = r9.zza(r7, r10, r11)     // Catch:{ all -> 0x018d }
            if (r9 != 0) goto L_0x0124
            com.google.android.gms.measurement.internal.zzw r7 = r6.zzdo()     // Catch:{ all -> 0x0196 }
            r7.endTransaction()     // Catch:{ all -> 0x0196 }
            r6.zzsi = r0
            r6.zzgd()
            return
        L_0x0103:
            com.google.android.gms.measurement.internal.zzbs r11 = r6.zzdp()     // Catch:{ all -> 0x018d }
            com.google.android.gms.internal.measurement.zzce r11 = r11.zzay(r7)     // Catch:{ all -> 0x018d }
            if (r11 != 0) goto L_0x0124
            com.google.android.gms.measurement.internal.zzbs r11 = r6.zzdp()     // Catch:{ all -> 0x018d }
            boolean r9 = r11.zza(r7, r9, r9)     // Catch:{ all -> 0x018d }
            if (r9 != 0) goto L_0x0124
            com.google.android.gms.measurement.internal.zzw r7 = r6.zzdo()     // Catch:{ all -> 0x0196 }
            r7.endTransaction()     // Catch:{ all -> 0x0196 }
            r6.zzsi = r0
            r6.zzgd()
            return
        L_0x0124:
            com.google.android.gms.measurement.internal.zzby r9 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.common.util.Clock r9 = r9.zzz()     // Catch:{ all -> 0x018d }
            long r2 = r9.currentTimeMillis()     // Catch:{ all -> 0x018d }
            r1.zzl(r2)     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzw r9 = r6.zzdo()     // Catch:{ all -> 0x018d }
            r9.zza(r1)     // Catch:{ all -> 0x018d }
            if (r8 != r5) goto L_0x014a
            com.google.android.gms.measurement.internal.zzby r8 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzau r8 = r8.zzad()     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzdf()     // Catch:{ all -> 0x018d }
            java.lang.String r9 = "Config not found. Using empty config. appId"
            r8.zza(r9, r7)     // Catch:{ all -> 0x018d }
            goto L_0x0162
        L_0x014a:
            com.google.android.gms.measurement.internal.zzby r7 = r6.zzl     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzau r7 = r7.zzad()     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzaw r7 = r7.zzdi()     // Catch:{ all -> 0x018d }
            java.lang.String r9 = "Successfully fetched config. Got network response. code, size"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x018d }
            int r10 = r10.length     // Catch:{ all -> 0x018d }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ all -> 0x018d }
            r7.zza(r9, r8, r10)     // Catch:{ all -> 0x018d }
        L_0x0162:
            com.google.android.gms.measurement.internal.zzay r7 = r6.zzfu()     // Catch:{ all -> 0x018d }
            boolean r7 = r7.zzdl()     // Catch:{ all -> 0x018d }
            if (r7 == 0) goto L_0x0176
            boolean r7 = r6.zzgb()     // Catch:{ all -> 0x018d }
            if (r7 == 0) goto L_0x0176
            r6.zzga()     // Catch:{ all -> 0x018d }
            goto L_0x0179
        L_0x0176:
            r6.zzgc()     // Catch:{ all -> 0x018d }
        L_0x0179:
            com.google.android.gms.measurement.internal.zzw r7 = r6.zzdo()     // Catch:{ all -> 0x018d }
            r7.setTransactionSuccessful()     // Catch:{ all -> 0x018d }
            com.google.android.gms.measurement.internal.zzw r7 = r6.zzdo()     // Catch:{ all -> 0x0196 }
            r7.endTransaction()     // Catch:{ all -> 0x0196 }
            r6.zzsi = r0
            r6.zzgd()
            return
        L_0x018d:
            r7 = move-exception
            com.google.android.gms.measurement.internal.zzw r8 = r6.zzdo()     // Catch:{ all -> 0x0196 }
            r8.endTransaction()     // Catch:{ all -> 0x0196 }
            throw r7     // Catch:{ all -> 0x0196 }
        L_0x0196:
            r7 = move-exception
            r6.zzsi = r0
            r6.zzgd()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzft.zzb(java.lang.String, int, java.lang.Throwable, byte[], java.util.Map):void");
    }

    @WorkerThread
    private final void zzgc() {
        long j;
        long j2;
        zzq();
        zzfy();
        if (zzgg() || this.zzl.zzaf().zza(zzal.zzip)) {
            if (this.zzse > 0) {
                long abs = 3600000 - Math.abs(this.zzl.zzz().elapsedRealtime() - this.zzse);
                if (abs > 0) {
                    this.zzl.zzad().zzdi().zza("Upload has been suspended. Will update scheduling later in approximately ms", Long.valueOf(abs));
                    zzfv().unregister();
                    zzfw().cancel();
                    return;
                }
                this.zzse = 0;
            }
            if (!this.zzl.zzet() || !zzgb()) {
                this.zzl.zzad().zzdi().zzaq("Nothing to upload or uploading impossible");
                zzfv().unregister();
                zzfw().cancel();
                return;
            }
            long currentTimeMillis = this.zzl.zzz().currentTimeMillis();
            long max = Math.max(0, ((Long) zzal.zzhd.get(null)).longValue());
            boolean z = zzdo().zzce() || zzdo().zzbz();
            if (z) {
                String zzbu = this.zzl.zzaf().zzbu();
                if (TextUtils.isEmpty(zzbu) || ".none.".equals(zzbu)) {
                    j = Math.max(0, ((Long) zzal.zzgx.get(null)).longValue());
                } else {
                    j = Math.max(0, ((Long) zzal.zzgy.get(null)).longValue());
                }
            } else {
                j = Math.max(0, ((Long) zzal.zzgw.get(null)).longValue());
            }
            long j3 = this.zzl.zzae().zzlb.get();
            long j4 = this.zzl.zzae().zzlc.get();
            long j5 = j;
            long j6 = max;
            long max2 = Math.max(zzdo().zzcb(), zzdo().zzcc());
            if (max2 == 0) {
                j2 = 0;
            } else {
                long abs2 = currentTimeMillis - Math.abs(max2 - currentTimeMillis);
                long abs3 = currentTimeMillis - Math.abs(j3 - currentTimeMillis);
                long abs4 = currentTimeMillis - Math.abs(j4 - currentTimeMillis);
                long max3 = Math.max(abs3, abs4);
                long j7 = abs2 + j6;
                if (z && max3 > 0) {
                    j7 = Math.min(abs2, max3) + j5;
                }
                long j8 = j5;
                j2 = !zzdm().zzb(max3, j8) ? max3 + j8 : j7;
                if (abs4 != 0 && abs4 >= abs2) {
                    int i = 0;
                    while (true) {
                        if (i >= Math.min(20, Math.max(0, ((Integer) zzal.zzhf.get(null)).intValue()))) {
                            j2 = 0;
                            break;
                        }
                        j2 += Math.max(0, ((Long) zzal.zzhe.get(null)).longValue()) * (1 << i);
                        if (j2 > abs4) {
                            break;
                        }
                        i++;
                    }
                }
            }
            if (j2 == 0) {
                this.zzl.zzad().zzdi().zzaq("Next upload time is 0");
                zzfv().unregister();
                zzfw().cancel();
            } else if (!zzfu().zzdl()) {
                this.zzl.zzad().zzdi().zzaq("No network");
                zzfv().zzdq();
                zzfw().cancel();
            } else {
                long j9 = this.zzl.zzae().zzld.get();
                long max4 = Math.max(0, ((Long) zzal.zzgu.get(null)).longValue());
                if (!zzdm().zzb(j9, max4)) {
                    j2 = Math.max(j2, j9 + max4);
                }
                zzfv().unregister();
                long currentTimeMillis2 = j2 - this.zzl.zzz().currentTimeMillis();
                if (currentTimeMillis2 <= 0) {
                    currentTimeMillis2 = Math.max(0, ((Long) zzal.zzgz.get(null)).longValue());
                    this.zzl.zzae().zzlb.set(this.zzl.zzz().currentTimeMillis());
                }
                this.zzl.zzad().zzdi().zza("Upload scheduled in approximately ms", Long.valueOf(currentTimeMillis2));
                zzfw().zzv(currentTimeMillis2);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzf(Runnable runnable) {
        zzq();
        if (this.zzsf == null) {
            this.zzsf = new ArrayList();
        }
        this.zzsf.add(runnable);
    }

    @WorkerThread
    private final void zzgd() {
        zzq();
        if (this.zzsi || this.zzsj || this.zzsk) {
            this.zzl.zzad().zzdi().zza("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzsi), Boolean.valueOf(this.zzsj), Boolean.valueOf(this.zzsk));
            return;
        }
        this.zzl.zzad().zzdi().zzaq("Stopping uploading service(s)");
        List<Runnable> list = this.zzsf;
        if (list != null) {
            for (Runnable run : list) {
                run.run();
            }
            this.zzsf.clear();
        }
    }

    @WorkerThread
    private final Boolean zzc(zzg zzg) {
        try {
            if (zzg.zzat() != -2147483648L) {
                if (zzg.zzat() == ((long) Wrappers.packageManager(this.zzl.getContext()).getPackageInfo(zzg.zzan(), 0).versionCode)) {
                    return Boolean.valueOf(true);
                }
            } else {
                String str = Wrappers.packageManager(this.zzl.getContext()).getPackageInfo(zzg.zzan(), 0).versionName;
                if (zzg.zzas() != null && zzg.zzas().equals(str)) {
                    return Boolean.valueOf(true);
                }
            }
            return Boolean.valueOf(false);
        } catch (NameNotFoundException unused) {
            return null;
        }
    }

    @WorkerThread
    @VisibleForTesting
    private final boolean zzge() {
        zzq();
        try {
            this.zzsm = new RandomAccessFile(new File(this.zzl.getContext().getFilesDir(), "google_app_measurement.db"), "rw").getChannel();
            this.zzsl = this.zzsm.tryLock();
            if (this.zzsl != null) {
                this.zzl.zzad().zzdi().zzaq("Storage concurrent access okay");
                return true;
            }
            this.zzl.zzad().zzda().zzaq("Storage concurrent data access panic");
            return false;
        } catch (FileNotFoundException e) {
            this.zzl.zzad().zzda().zza("Failed to acquire storage lock", e);
        } catch (IOException e2) {
            this.zzl.zzad().zzda().zza("Failed to access storage lock file", e2);
        }
    }

    @WorkerThread
    @VisibleForTesting
    private final int zza(FileChannel fileChannel) {
        zzq();
        int i = 0;
        if (fileChannel == null || !fileChannel.isOpen()) {
            this.zzl.zzad().zzda().zzaq("Bad channel to read from");
            return 0;
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        try {
            fileChannel.position(0);
            int read = fileChannel.read(allocate);
            if (read != 4) {
                if (read != -1) {
                    this.zzl.zzad().zzdd().zza("Unexpected data length. Bytes read", Integer.valueOf(read));
                }
                return 0;
            }
            allocate.flip();
            i = allocate.getInt();
            return i;
        } catch (IOException e) {
            this.zzl.zzad().zzda().zza("Failed to read from channel", e);
        }
    }

    @WorkerThread
    @VisibleForTesting
    private final boolean zza(int i, FileChannel fileChannel) {
        zzq();
        if (fileChannel == null || !fileChannel.isOpen()) {
            this.zzl.zzad().zzda().zzaq("Bad channel to read from");
            return false;
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.putInt(i);
        allocate.flip();
        try {
            fileChannel.truncate(0);
            fileChannel.write(allocate);
            fileChannel.force(true);
            if (fileChannel.size() != 4) {
                this.zzl.zzad().zzda().zza("Error writing to channel. Bytes written", Long.valueOf(fileChannel.size()));
            }
            return true;
        } catch (IOException e) {
            this.zzl.zzad().zzda().zza("Failed to write to channel", e);
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzgf() {
        zzq();
        zzfy();
        if (!this.zzsd) {
            this.zzsd = true;
            zzq();
            zzfy();
            if ((this.zzl.zzaf().zza(zzal.zzip) || zzgg()) && zzge()) {
                int zza2 = zza(this.zzsm);
                int zzcx = this.zzl.zzt().zzcx();
                zzq();
                if (zza2 > zzcx) {
                    this.zzl.zzad().zzda().zza("Panic: can't downgrade version. Previous, current version", Integer.valueOf(zza2), Integer.valueOf(zzcx));
                } else if (zza2 < zzcx) {
                    if (zza(zzcx, this.zzsm)) {
                        this.zzl.zzad().zzdi().zza("Storage version upgraded. Previous, current version", Integer.valueOf(zza2), Integer.valueOf(zzcx));
                    } else {
                        this.zzl.zzad().zzda().zza("Storage version upgrade failed. Previous, current version", Integer.valueOf(zza2), Integer.valueOf(zzcx));
                    }
                }
            }
        }
        if (!this.zzsc && !this.zzl.zzaf().zza(zzal.zzip)) {
            this.zzl.zzad().zzdg().zzaq("This instance being marked as an uploader");
            this.zzsc = true;
            zzgc();
        }
    }

    @WorkerThread
    private final boolean zzgg() {
        zzq();
        zzfy();
        return this.zzsc;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    @VisibleForTesting
    public final void zzd(zzm zzm) {
        if (this.zzsn != null) {
            this.zzso = new ArrayList();
            this.zzso.addAll(this.zzsn);
        }
        zzw zzdo = zzdo();
        String str = zzm.packageName;
        Preconditions.checkNotEmpty(str);
        zzdo.zzq();
        zzdo.zzah();
        try {
            SQLiteDatabase writableDatabase = zzdo.getWritableDatabase();
            String[] strArr = {str};
            int delete = writableDatabase.delete("apps", "app_id=?", strArr) + 0 + writableDatabase.delete("events", "app_id=?", strArr) + writableDatabase.delete("user_attributes", "app_id=?", strArr) + writableDatabase.delete("conditional_properties", "app_id=?", strArr) + writableDatabase.delete("raw_events", "app_id=?", strArr) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr) + writableDatabase.delete("queue", "app_id=?", strArr) + writableDatabase.delete("audience_filter_values", "app_id=?", strArr) + writableDatabase.delete("main_event_params", "app_id=?", strArr);
            if (delete > 0) {
                zzdo.zzad().zzdi().zza("Reset analytics data. app, records", str, Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzdo.zzad().zzda().zza("Error resetting analytics data. appId, error", zzau.zzao(str), e);
        }
        zzm zza2 = zza(this.zzl.getContext(), zzm.packageName, zzm.zzch, zzm.zzcr, zzm.zzct, zzm.zzcu, zzm.zzdp, zzm.zzcv);
        if (!this.zzl.zzaf().zzs(zzm.packageName) || zzm.zzcr) {
            zzf(zza2);
        }
    }

    private final zzm zza(Context context, String str, String str2, boolean z, boolean z2, boolean z3, long j, String str3) {
        String str4;
        int i;
        String str5 = str;
        String str6 = "Unknown";
        String str7 = "Unknown";
        String str8 = "Unknown";
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            this.zzl.zzad().zzda().zzaq("PackageManager is null, can not log app install information");
            return null;
        }
        try {
            str6 = packageManager.getInstallerPackageName(str5);
        } catch (IllegalArgumentException unused) {
            this.zzl.zzad().zzda().zza("Error retrieving installer package name. appId", zzau.zzao(str));
        }
        String str9 = str6 == null ? "manual_install" : "com.android.vending".equals(str6) ? "" : str6;
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str5, 0);
            if (packageInfo != null) {
                CharSequence applicationLabel = Wrappers.packageManager(context).getApplicationLabel(str5);
                if (!TextUtils.isEmpty(applicationLabel)) {
                    String charSequence = applicationLabel.toString();
                }
                str4 = packageInfo.versionName;
                i = packageInfo.versionCode;
            } else {
                str4 = str7;
                i = Integer.MIN_VALUE;
            }
            this.zzl.zzag();
            zzm zzm = new zzm(str, str2, str4, (long) i, str9, this.zzl.zzaf().zzav(), this.zzl.zzab().zzc(context, str5), (String) null, z, false, "", 0, this.zzl.zzaf().zzu(str5) ? j : 0, 0, z2, z3, false, str3, (Boolean) null, 0);
            return zzm;
        } catch (NameNotFoundException unused2) {
            this.zzl.zzad().zzda().zza("Error retrieving newly installed package info. appId, appName", zzau.zzao(str), str8);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzb(zzga zzga, zzm zzm) {
        zzq();
        zzfy();
        if (TextUtils.isEmpty(zzm.zzch) && TextUtils.isEmpty(zzm.zzcv)) {
            return;
        }
        if (!zzm.zzcr) {
            zzg(zzm);
            return;
        }
        int zzbo = this.zzl.zzab().zzbo(zzga.name);
        if (zzbo != 0) {
            this.zzl.zzab();
            this.zzl.zzab().zza(zzm.packageName, zzbo, "_ev", zzgd.zza(zzga.name, 24, true), zzga.name != null ? zzga.name.length() : 0);
            return;
        }
        int zzc = this.zzl.zzab().zzc(zzga.name, zzga.getValue());
        if (zzc != 0) {
            this.zzl.zzab();
            String zza2 = zzgd.zza(zzga.name, 24, true);
            Object value = zzga.getValue();
            this.zzl.zzab().zza(zzm.packageName, zzc, "_ev", zza2, (value == null || (!(value instanceof String) && !(value instanceof CharSequence))) ? 0 : String.valueOf(value).length());
            return;
        }
        Object zzd = this.zzl.zzab().zzd(zzga.name, zzga.getValue());
        if (zzd != null) {
            if ("_sid".equals(zzga.name) && this.zzl.zzaf().zzz(zzm.packageName)) {
                long j = zzga.zzsx;
                String str = zzga.origin;
                long j2 = 0;
                zzgc zze = zzdo().zze(zzm.packageName, "_sno");
                if (zze == null || !(zze.value instanceof Long)) {
                    if (zze != null) {
                        this.zzl.zzad().zzdd().zza("Retrieved last session number from database does not contain a valid (long) value", zze.value);
                    }
                    if (this.zzl.zzaf().zze(zzm.packageName, zzal.zzii)) {
                        zzaf zzc2 = zzdo().zzc(zzm.packageName, "_s");
                        if (zzc2 != null) {
                            j2 = zzc2.zzfe;
                            this.zzl.zzad().zzdi().zza("Backfill the session number. Last used session number", Long.valueOf(j2));
                        }
                    }
                } else {
                    j2 = ((Long) zze.value).longValue();
                }
                zzga zzga2 = new zzga("_sno", j, Long.valueOf(j2 + 1), str);
                zzb(zzga2, zzm);
            }
            zzgc zzgc = new zzgc(zzm.packageName, zzga.origin, zzga.name, zzga.zzsx, zzd);
            this.zzl.zzad().zzdh().zza("Setting user property", this.zzl.zzaa().zzan(zzgc.name), zzd);
            zzdo().beginTransaction();
            try {
                zzg(zzm);
                boolean zza3 = zzdo().zza(zzgc);
                zzdo().setTransactionSuccessful();
                if (zza3) {
                    this.zzl.zzad().zzdh().zza("User property set", this.zzl.zzaa().zzan(zzgc.name), zzgc.value);
                } else {
                    this.zzl.zzad().zzda().zza("Too many unique user properties are set. Ignoring user property", this.zzl.zzaa().zzan(zzgc.name), zzgc.value);
                    this.zzl.zzab().zza(zzm.packageName, 9, (String) null, (String) null, 0);
                }
            } finally {
                zzdo().endTransaction();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzc(zzga zzga, zzm zzm) {
        zzq();
        zzfy();
        if (TextUtils.isEmpty(zzm.zzch) && TextUtils.isEmpty(zzm.zzcv)) {
            return;
        }
        if (!zzm.zzcr) {
            zzg(zzm);
        } else if (!this.zzl.zzaf().zze(zzm.packageName, zzal.zzin)) {
            this.zzl.zzad().zzdh().zza("Removing user property", this.zzl.zzaa().zzan(zzga.name));
            zzdo().beginTransaction();
            try {
                zzg(zzm);
                zzdo().zzd(zzm.packageName, zzga.name);
                zzdo().setTransactionSuccessful();
                this.zzl.zzad().zzdh().zza("User property removed", this.zzl.zzaa().zzan(zzga.name));
            } finally {
                zzdo().endTransaction();
            }
        } else if (!"_npa".equals(zzga.name) || zzm.zzcw == null) {
            this.zzl.zzad().zzdh().zza("Removing user property", this.zzl.zzaa().zzan(zzga.name));
            zzdo().beginTransaction();
            try {
                zzg(zzm);
                zzdo().zzd(zzm.packageName, zzga.name);
                zzdo().setTransactionSuccessful();
                this.zzl.zzad().zzdh().zza("User property removed", this.zzl.zzaa().zzan(zzga.name));
            } finally {
                zzdo().endTransaction();
            }
        } else {
            this.zzl.zzad().zzdh().zzaq("Falling back to manifest metadata value for ad personalization");
            zzga zzga2 = new zzga("_npa", this.zzl.zzz().currentTimeMillis(), Long.valueOf(zzm.zzcw.booleanValue() ? 1 : 0), "auto");
            zzb(zzga2, zzm);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(zzfs zzfs) {
        this.zzsg++;
    }

    /* access modifiers changed from: 0000 */
    public final void zzgh() {
        this.zzsh++;
    }

    /* access modifiers changed from: 0000 */
    public final zzby zzgi() {
        return this.zzl;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzf(zzm zzm) {
        int i;
        zzg zzae;
        ApplicationInfo applicationInfo;
        long j;
        PackageInfo packageInfo;
        boolean z;
        zzw zzdo;
        String zzan;
        zzm zzm2 = zzm;
        zzq();
        zzfy();
        Preconditions.checkNotNull(zzm);
        Preconditions.checkNotEmpty(zzm2.packageName);
        if (!TextUtils.isEmpty(zzm2.zzch) || !TextUtils.isEmpty(zzm2.zzcv)) {
            zzg zzae2 = zzdo().zzae(zzm2.packageName);
            if (zzae2 != null && TextUtils.isEmpty(zzae2.getGmpAppId()) && !TextUtils.isEmpty(zzm2.zzch)) {
                zzae2.zzl(0);
                zzdo().zza(zzae2);
                zzdp().zzbb(zzm2.packageName);
            }
            if (!zzm2.zzcr) {
                zzg(zzm);
                return;
            }
            long j2 = zzm2.zzdp;
            if (j2 == 0) {
                j2 = this.zzl.zzz().currentTimeMillis();
            }
            if (this.zzl.zzaf().zze(zzm2.packageName, zzal.zzin)) {
                this.zzl.zzy().zzcr();
            }
            int i2 = zzm2.zzdq;
            if (i2 == 0 || i2 == 1) {
                i = i2;
            } else {
                this.zzl.zzad().zzdd().zza("Incorrect app type, assuming installed app. appId, appType", zzau.zzao(zzm2.packageName), Integer.valueOf(i2));
                i = 0;
            }
            zzdo().beginTransaction();
            try {
                if (this.zzl.zzaf().zze(zzm2.packageName, zzal.zzin)) {
                    zzgc zze = zzdo().zze(zzm2.packageName, "_npa");
                    if (zze == null || "auto".equals(zze.origin)) {
                        if (zzm2.zzcw != null) {
                            zzga zzga = r7;
                            zzga zzga2 = new zzga("_npa", j2, Long.valueOf(zzm2.zzcw.booleanValue() ? 1 : 0), "auto");
                            if (zze == null || !zze.value.equals(zzga.zzsy)) {
                                zzb(zzga, zzm2);
                            }
                        } else if (zze != null) {
                            zzga zzga3 = new zzga("_npa", j2, null, "auto");
                            zzc(zzga3, zzm2);
                        }
                    }
                }
                zzae = zzdo().zzae(zzm2.packageName);
                applicationInfo = null;
                if (zzae != null) {
                    this.zzl.zzab();
                    if (zzgd.zza(zzm2.zzch, zzae.getGmpAppId(), zzm2.zzcv, zzae.zzao())) {
                        this.zzl.zzad().zzdd().zza("New GMP App Id passed in. Removing cached database data. appId", zzau.zzao(zzae.zzan()));
                        zzdo = zzdo();
                        zzan = zzae.zzan();
                        zzdo.zzah();
                        zzdo.zzq();
                        Preconditions.checkNotEmpty(zzan);
                        SQLiteDatabase writableDatabase = zzdo.getWritableDatabase();
                        String[] strArr = {zzan};
                        int delete = writableDatabase.delete("events", "app_id=?", strArr) + 0 + writableDatabase.delete("user_attributes", "app_id=?", strArr) + writableDatabase.delete("conditional_properties", "app_id=?", strArr) + writableDatabase.delete("apps", "app_id=?", strArr) + writableDatabase.delete("raw_events", "app_id=?", strArr) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr) + writableDatabase.delete("event_filters", "app_id=?", strArr) + writableDatabase.delete("property_filters", "app_id=?", strArr) + writableDatabase.delete("audience_filter_values", "app_id=?", strArr);
                        if (delete > 0) {
                            zzdo.zzad().zzdi().zza("Deleted application data. app, records", zzan, Integer.valueOf(delete));
                        }
                        zzae = null;
                    }
                }
            } catch (SQLiteException e) {
                zzdo.zzad().zzda().zza("Error deleting application data. appId, error", zzau.zzao(zzan), e);
            } catch (Throwable th) {
                zzdo().endTransaction();
                throw th;
            }
            if (zzae != null) {
                if (zzae.zzat() != -2147483648L) {
                    if (zzae.zzat() != zzm2.zzco) {
                        Bundle bundle = new Bundle();
                        bundle.putString("_pv", zzae.zzas());
                        zzaj zzaj = new zzaj("_au", new zzag(bundle), "auto", j2);
                        zzc(zzaj, zzm2);
                    }
                } else if (zzae.zzas() != null && !zzae.zzas().equals(zzm2.zzcn)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("_pv", zzae.zzas());
                    zzaj zzaj2 = new zzaj("_au", new zzag(bundle2), "auto", j2);
                    zzc(zzaj2, zzm2);
                }
            }
            zzg(zzm);
            zzaf zzaf = i == 0 ? zzdo().zzc(zzm2.packageName, "_f") : i == 1 ? zzdo().zzc(zzm2.packageName, "_v") : null;
            if (zzaf == null) {
                long j3 = ((j2 / 3600000) + 1) * 3600000;
                if (i == 0) {
                    j = 1;
                    zzga zzga4 = new zzga("_fot", j2, Long.valueOf(j3), "auto");
                    zzb(zzga4, zzm2);
                    if (this.zzl.zzaf().zzw(zzm2.zzch)) {
                        zzq();
                        this.zzl.zzej().zzaw(zzm2.packageName);
                    }
                    zzq();
                    zzfy();
                    Bundle bundle3 = new Bundle();
                    bundle3.putLong("_c", 1);
                    bundle3.putLong("_r", 1);
                    bundle3.putLong("_uwa", 0);
                    bundle3.putLong("_pfo", 0);
                    bundle3.putLong("_sys", 0);
                    bundle3.putLong("_sysu", 0);
                    if (this.zzl.zzaf().zzac(zzm2.packageName)) {
                        bundle3.putLong("_et", 1);
                    }
                    if (this.zzl.zzaf().zzs(zzm2.packageName) && zzm2.zzdr) {
                        bundle3.putLong("_dac", 1);
                    }
                    if (this.zzl.getContext().getPackageManager() == null) {
                        this.zzl.zzad().zzda().zza("PackageManager is null, first open report might be inaccurate. appId", zzau.zzao(zzm2.packageName));
                    } else {
                        try {
                            packageInfo = Wrappers.packageManager(this.zzl.getContext()).getPackageInfo(zzm2.packageName, 0);
                        } catch (NameNotFoundException e2) {
                            this.zzl.zzad().zzda().zza("Package info is null, first open report might be inaccurate. appId", zzau.zzao(zzm2.packageName), e2);
                            packageInfo = null;
                        }
                        if (!(packageInfo == null || packageInfo.firstInstallTime == 0)) {
                            if (packageInfo.firstInstallTime != packageInfo.lastUpdateTime) {
                                bundle3.putLong("_uwa", 1);
                                z = false;
                            } else {
                                z = true;
                            }
                            zzga zzga5 = new zzga("_fi", j2, Long.valueOf(z ? 1 : 0), "auto");
                            zzb(zzga5, zzm2);
                        }
                        try {
                            applicationInfo = Wrappers.packageManager(this.zzl.getContext()).getApplicationInfo(zzm2.packageName, 0);
                        } catch (NameNotFoundException e3) {
                            this.zzl.zzad().zzda().zza("Application info is null, first open report might be inaccurate. appId", zzau.zzao(zzm2.packageName), e3);
                        }
                        if (applicationInfo != null) {
                            if ((applicationInfo.flags & 1) != 0) {
                                bundle3.putLong("_sys", 1);
                            }
                            if ((applicationInfo.flags & 128) != 0) {
                                bundle3.putLong("_sysu", 1);
                            }
                        }
                    }
                    zzw zzdo2 = zzdo();
                    String str = zzm2.packageName;
                    Preconditions.checkNotEmpty(str);
                    zzdo2.zzq();
                    zzdo2.zzah();
                    long zzj = zzdo2.zzj(str, "first_open_count");
                    if (zzj >= 0) {
                        bundle3.putLong("_pfo", zzj);
                    }
                    zzaj zzaj3 = new zzaj("_f", new zzag(bundle3), "auto", j2);
                    zzc(zzaj3, zzm2);
                } else {
                    j = 1;
                    if (i == 1) {
                        zzga zzga6 = new zzga("_fvt", j2, Long.valueOf(j3), "auto");
                        zzb(zzga6, zzm2);
                        zzq();
                        zzfy();
                        Bundle bundle4 = new Bundle();
                        bundle4.putLong("_c", 1);
                        bundle4.putLong("_r", 1);
                        if (this.zzl.zzaf().zzac(zzm2.packageName)) {
                            bundle4.putLong("_et", 1);
                        }
                        if (this.zzl.zzaf().zzs(zzm2.packageName) && zzm2.zzdr) {
                            bundle4.putLong("_dac", 1);
                        }
                        zzaj zzaj4 = new zzaj("_v", new zzag(bundle4), "auto", j2);
                        zzc(zzaj4, zzm2);
                    }
                }
                if (!this.zzl.zzaf().zze(zzm2.packageName, zzal.zzim)) {
                    Bundle bundle5 = new Bundle();
                    bundle5.putLong("_et", j);
                    if (this.zzl.zzaf().zzac(zzm2.packageName)) {
                        bundle5.putLong("_fr", j);
                    }
                    zzaj zzaj5 = new zzaj("_e", new zzag(bundle5), "auto", j2);
                    zzc(zzaj5, zzm2);
                }
            } else if (zzm2.zzdo) {
                zzaj zzaj6 = new zzaj("_cd", new zzag(new Bundle()), "auto", j2);
                zzc(zzaj6, zzm2);
            }
            zzdo().setTransactionSuccessful();
            zzdo().endTransaction();
        }
    }

    @WorkerThread
    private final zzm zzbk(String str) {
        String str2 = str;
        zzg zzae = zzdo().zzae(str2);
        if (zzae == null || TextUtils.isEmpty(zzae.zzas())) {
            this.zzl.zzad().zzdh().zza("No app data available; dropping", str2);
            return null;
        }
        Boolean zzc = zzc(zzae);
        if (zzc == null || zzc.booleanValue()) {
            zzg zzg = zzae;
            zzm zzm = new zzm(str, zzae.getGmpAppId(), zzae.zzas(), zzae.zzat(), zzae.zzau(), zzae.zzav(), zzae.zzaw(), (String) null, zzae.isMeasurementEnabled(), false, zzae.getFirebaseInstanceId(), zzg.zzbk(), 0, 0, zzg.zzbl(), zzg.zzbm(), false, zzg.zzao(), zzg.zzbn(), zzg.zzax());
            return zzm;
        }
        this.zzl.zzad().zzda().zza("App version does not match; dropping. appId", zzau.zzao(str));
        return null;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zze(zzr zzr) {
        zzm zzbk = zzbk(zzr.packageName);
        if (zzbk != null) {
            zzb(zzr, zzbk);
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzb(zzr zzr, zzm zzm) {
        Preconditions.checkNotNull(zzr);
        Preconditions.checkNotEmpty(zzr.packageName);
        Preconditions.checkNotNull(zzr.origin);
        Preconditions.checkNotNull(zzr.zzdv);
        Preconditions.checkNotEmpty(zzr.zzdv.name);
        zzq();
        zzfy();
        if (TextUtils.isEmpty(zzm.zzch) && TextUtils.isEmpty(zzm.zzcv)) {
            return;
        }
        if (!zzm.zzcr) {
            zzg(zzm);
            return;
        }
        zzr zzr2 = new zzr(zzr);
        boolean z = false;
        zzr2.active = false;
        zzdo().beginTransaction();
        try {
            zzr zzf = zzdo().zzf(zzr2.packageName, zzr2.zzdv.name);
            if (zzf != null && !zzf.origin.equals(zzr2.origin)) {
                this.zzl.zzad().zzdd().zza("Updating a conditional user property with different origin. name, origin, origin (from DB)", this.zzl.zzaa().zzan(zzr2.zzdv.name), zzr2.origin, zzf.origin);
            }
            if (zzf != null && zzf.active) {
                zzr2.origin = zzf.origin;
                zzr2.creationTimestamp = zzf.creationTimestamp;
                zzr2.triggerTimeout = zzf.triggerTimeout;
                zzr2.triggerEventName = zzf.triggerEventName;
                zzr2.zzdx = zzf.zzdx;
                zzr2.active = zzf.active;
                zzga zzga = new zzga(zzr2.zzdv.name, zzf.zzdv.zzsx, zzr2.zzdv.getValue(), zzf.zzdv.origin);
                zzr2.zzdv = zzga;
            } else if (TextUtils.isEmpty(zzr2.triggerEventName)) {
                zzga zzga2 = new zzga(zzr2.zzdv.name, zzr2.creationTimestamp, zzr2.zzdv.getValue(), zzr2.zzdv.origin);
                zzr2.zzdv = zzga2;
                zzr2.active = true;
                z = true;
            }
            if (zzr2.active) {
                zzga zzga3 = zzr2.zzdv;
                zzgc zzgc = new zzgc(zzr2.packageName, zzr2.origin, zzga3.name, zzga3.zzsx, zzga3.getValue());
                if (zzdo().zza(zzgc)) {
                    this.zzl.zzad().zzdh().zza("User property updated immediately", zzr2.packageName, this.zzl.zzaa().zzan(zzgc.name), zzgc.value);
                } else {
                    this.zzl.zzad().zzda().zza("(2)Too many active user properties, ignoring", zzau.zzao(zzr2.packageName), this.zzl.zzaa().zzan(zzgc.name), zzgc.value);
                }
                if (z && zzr2.zzdx != null) {
                    zzd(new zzaj(zzr2.zzdx, zzr2.creationTimestamp), zzm);
                }
            }
            if (zzdo().zza(zzr2)) {
                this.zzl.zzad().zzdh().zza("Conditional property added", zzr2.packageName, this.zzl.zzaa().zzan(zzr2.zzdv.name), zzr2.zzdv.getValue());
            } else {
                this.zzl.zzad().zzda().zza("Too many conditional properties, ignoring", zzau.zzao(zzr2.packageName), this.zzl.zzaa().zzan(zzr2.zzdv.name), zzr2.zzdv.getValue());
            }
            zzdo().setTransactionSuccessful();
        } finally {
            zzdo().endTransaction();
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzf(zzr zzr) {
        zzm zzbk = zzbk(zzr.packageName);
        if (zzbk != null) {
            zzc(zzr, zzbk);
        }
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzc(zzr zzr, zzm zzm) {
        Preconditions.checkNotNull(zzr);
        Preconditions.checkNotEmpty(zzr.packageName);
        Preconditions.checkNotNull(zzr.zzdv);
        Preconditions.checkNotEmpty(zzr.zzdv.name);
        zzq();
        zzfy();
        if (TextUtils.isEmpty(zzm.zzch) && TextUtils.isEmpty(zzm.zzcv)) {
            return;
        }
        if (!zzm.zzcr) {
            zzg(zzm);
            return;
        }
        zzdo().beginTransaction();
        try {
            zzg(zzm);
            zzr zzf = zzdo().zzf(zzr.packageName, zzr.zzdv.name);
            if (zzf != null) {
                this.zzl.zzad().zzdh().zza("Removing conditional user property", zzr.packageName, this.zzl.zzaa().zzan(zzr.zzdv.name));
                zzdo().zzg(zzr.packageName, zzr.zzdv.name);
                if (zzf.active) {
                    zzdo().zzd(zzr.packageName, zzr.zzdv.name);
                }
                if (zzr.zzdy != null) {
                    zzd(this.zzl.zzab().zza(zzr.packageName, zzr.zzdy.name, zzr.zzdy.zzfd != null ? zzr.zzdy.zzfd.zzct() : null, zzf.origin, zzr.zzdy.zzfp, true, false), zzm);
                }
            } else {
                this.zzl.zzad().zzdd().zza("Conditional user property doesn't exist", zzau.zzao(zzr.packageName), this.zzl.zzaa().zzan(zzr.zzdv.name));
            }
            zzdo().setTransactionSuccessful();
        } finally {
            zzdo().endTransaction();
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final zzg zzg(zzm zzm) {
        boolean z;
        zzq();
        zzfy();
        Preconditions.checkNotNull(zzm);
        Preconditions.checkNotEmpty(zzm.packageName);
        zzg zzae = zzdo().zzae(zzm.packageName);
        String zzas = this.zzl.zzae().zzas(zzm.packageName);
        if (zzae == null) {
            zzae = new zzg(this.zzl, zzm.packageName);
            zzae.zza(this.zzl.zzab().zzgn());
            zzae.zzd(zzas);
            z = true;
        } else if (!zzas.equals(zzae.zzap())) {
            zzae.zzd(zzas);
            zzae.zza(this.zzl.zzab().zzgn());
            z = true;
        } else {
            z = false;
        }
        if (!TextUtils.equals(zzm.zzch, zzae.getGmpAppId())) {
            zzae.zzb(zzm.zzch);
            z = true;
        }
        if (!TextUtils.equals(zzm.zzcv, zzae.zzao())) {
            zzae.zzc(zzm.zzcv);
            z = true;
        }
        if (!TextUtils.isEmpty(zzm.zzcj) && !zzm.zzcj.equals(zzae.getFirebaseInstanceId())) {
            zzae.zze(zzm.zzcj);
            z = true;
        }
        if (!(zzm.zzt == 0 || zzm.zzt == zzae.zzav())) {
            zzae.zzh(zzm.zzt);
            z = true;
        }
        if (!TextUtils.isEmpty(zzm.zzcn) && !zzm.zzcn.equals(zzae.zzas())) {
            zzae.zzf(zzm.zzcn);
            z = true;
        }
        if (zzm.zzco != zzae.zzat()) {
            zzae.zzg(zzm.zzco);
            z = true;
        }
        if (zzm.zzcp != null && !zzm.zzcp.equals(zzae.zzau())) {
            zzae.zzg(zzm.zzcp);
            z = true;
        }
        if (zzm.zzcq != zzae.zzaw()) {
            zzae.zzi(zzm.zzcq);
            z = true;
        }
        if (zzm.zzcr != zzae.isMeasurementEnabled()) {
            zzae.setMeasurementEnabled(zzm.zzcr);
            z = true;
        }
        if (!TextUtils.isEmpty(zzm.zzdn) && !zzm.zzdn.equals(zzae.zzbi())) {
            zzae.zzh(zzm.zzdn);
            z = true;
        }
        if (zzm.zzcs != zzae.zzbk()) {
            zzae.zzt(zzm.zzcs);
            z = true;
        }
        if (zzm.zzct != zzae.zzbl()) {
            zzae.zzb(zzm.zzct);
            z = true;
        }
        if (zzm.zzcu != zzae.zzbm()) {
            zzae.zzc(zzm.zzcu);
            z = true;
        }
        if (this.zzl.zzaf().zze(zzm.packageName, zzal.zzin) && zzm.zzcw != zzae.zzbn()) {
            zzae.zza(zzm.zzcw);
            z = true;
        }
        if (!(zzm.zzu == 0 || zzm.zzu == zzae.zzax())) {
            zzae.zzj(zzm.zzu);
            z = true;
        }
        if (z) {
            zzdo().zza(zzae);
        }
        return zzae;
    }

    /* access modifiers changed from: 0000 */
    public final String zzh(zzm zzm) {
        try {
            return (String) this.zzl.zzac().zza((Callable<V>) new zzfx<V>(this, zzm)).get(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            this.zzl.zzad().zzda().zza("Failed to get app instance id. appId", zzau.zzao(zzm.packageName), e);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzj(boolean z) {
        zzgc();
    }
}
