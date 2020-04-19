package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.measurement.zzq;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@VisibleForTesting
public final class zzeg extends zzf {
    /* access modifiers changed from: private */
    public final zzey zzqj;
    /* access modifiers changed from: private */
    public zzam zzqk;
    private volatile Boolean zzql;
    private final zzab zzqm;
    private final zzfo zzqn;
    private final List<Runnable> zzqo = new ArrayList();
    private final zzab zzqp;

    protected zzeg(zzby zzby) {
        super(zzby);
        this.zzqn = new zzfo(zzby.zzz());
        this.zzqj = new zzey(this);
        this.zzqm = new zzeh(this, zzby);
        this.zzqp = new zzeq(this, zzby);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        return false;
    }

    @WorkerThread
    public final boolean isConnected() {
        zzq();
        zzah();
        return this.zzqk != null;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzfe() {
        zzq();
        zzah();
        zzd((Runnable) new zzer(this, zzi(true)));
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043  */
    @androidx.annotation.WorkerThread
    @com.google.android.gms.common.util.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(com.google.android.gms.measurement.internal.zzam r12, com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable r13, com.google.android.gms.measurement.internal.zzm r14) {
        /*
            r11 = this;
            r11.zzq()
            r11.zzo()
            r11.zzah()
            boolean r0 = r11.zzff()
            r1 = 0
            r2 = 100
            r3 = 0
            r4 = 100
        L_0x0013:
            r5 = 1001(0x3e9, float:1.403E-42)
            if (r3 >= r5) goto L_0x00a9
            if (r4 != r2) goto L_0x00a9
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            if (r0 == 0) goto L_0x0032
            com.google.android.gms.measurement.internal.zzaq r5 = r11.zzw()
            java.util.List r5 = r5.zzc(r2)
            if (r5 == 0) goto L_0x0032
            r4.addAll(r5)
            int r5 = r5.size()
            goto L_0x0033
        L_0x0032:
            r5 = 0
        L_0x0033:
            if (r13 == 0) goto L_0x003a
            if (r5 >= r2) goto L_0x003a
            r4.add(r13)
        L_0x003a:
            java.util.ArrayList r4 = (java.util.ArrayList) r4
            int r6 = r4.size()
            r7 = 0
        L_0x0041:
            if (r7 >= r6) goto L_0x00a4
            java.lang.Object r8 = r4.get(r7)
            int r7 = r7 + 1
            com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable r8 = (com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable) r8
            boolean r9 = r8 instanceof com.google.android.gms.measurement.internal.zzaj
            if (r9 == 0) goto L_0x0064
            com.google.android.gms.measurement.internal.zzaj r8 = (com.google.android.gms.measurement.internal.zzaj) r8     // Catch:{ RemoteException -> 0x0055 }
            r12.zza(r8, r14)     // Catch:{ RemoteException -> 0x0055 }
            goto L_0x0041
        L_0x0055:
            r8 = move-exception
            com.google.android.gms.measurement.internal.zzau r9 = r11.zzad()
            com.google.android.gms.measurement.internal.zzaw r9 = r9.zzda()
            java.lang.String r10 = "Failed to send event to the service"
            r9.zza(r10, r8)
            goto L_0x0041
        L_0x0064:
            boolean r9 = r8 instanceof com.google.android.gms.measurement.internal.zzga
            if (r9 == 0) goto L_0x007d
            com.google.android.gms.measurement.internal.zzga r8 = (com.google.android.gms.measurement.internal.zzga) r8     // Catch:{ RemoteException -> 0x006e }
            r12.zza(r8, r14)     // Catch:{ RemoteException -> 0x006e }
            goto L_0x0041
        L_0x006e:
            r8 = move-exception
            com.google.android.gms.measurement.internal.zzau r9 = r11.zzad()
            com.google.android.gms.measurement.internal.zzaw r9 = r9.zzda()
            java.lang.String r10 = "Failed to send attribute to the service"
            r9.zza(r10, r8)
            goto L_0x0041
        L_0x007d:
            boolean r9 = r8 instanceof com.google.android.gms.measurement.internal.zzr
            if (r9 == 0) goto L_0x0096
            com.google.android.gms.measurement.internal.zzr r8 = (com.google.android.gms.measurement.internal.zzr) r8     // Catch:{ RemoteException -> 0x0087 }
            r12.zza(r8, r14)     // Catch:{ RemoteException -> 0x0087 }
            goto L_0x0041
        L_0x0087:
            r8 = move-exception
            com.google.android.gms.measurement.internal.zzau r9 = r11.zzad()
            com.google.android.gms.measurement.internal.zzaw r9 = r9.zzda()
            java.lang.String r10 = "Failed to send conditional property to the service"
            r9.zza(r10, r8)
            goto L_0x0041
        L_0x0096:
            com.google.android.gms.measurement.internal.zzau r8 = r11.zzad()
            com.google.android.gms.measurement.internal.zzaw r8 = r8.zzda()
            java.lang.String r9 = "Discarding data. Unrecognized parcel type."
            r8.zzaq(r9)
            goto L_0x0041
        L_0x00a4:
            int r3 = r3 + 1
            r4 = r5
            goto L_0x0013
        L_0x00a9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzeg.zza(com.google.android.gms.measurement.internal.zzam, com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable, com.google.android.gms.measurement.internal.zzm):void");
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzc(zzaj zzaj, String str) {
        Preconditions.checkNotNull(zzaj);
        zzq();
        zzah();
        boolean zzff = zzff();
        zzes zzes = new zzes(this, zzff, zzff && zzw().zza(zzaj), zzaj, zzi(true), str);
        zzd((Runnable) zzes);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzd(zzr zzr) {
        Preconditions.checkNotNull(zzr);
        zzq();
        zzah();
        zzag();
        zzet zzet = new zzet(this, true, zzw().zzc(zzr), new zzr(zzr), zzi(true), zzr);
        zzd((Runnable) zzet);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(AtomicReference<List<zzr>> atomicReference, String str, String str2, String str3) {
        zzq();
        zzah();
        zzeu zzeu = new zzeu(this, atomicReference, str, str2, str3, zzi(false));
        zzd((Runnable) zzeu);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(zzq zzq, String str, String str2) {
        zzq();
        zzah();
        zzev zzev = new zzev(this, str, str2, zzi(false), zzq);
        zzd((Runnable) zzev);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(AtomicReference<List<zzga>> atomicReference, String str, String str2, String str3, boolean z) {
        zzq();
        zzah();
        zzew zzew = new zzew(this, atomicReference, str, str2, str3, z, zzi(false));
        zzd((Runnable) zzew);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(zzq zzq, String str, String str2, boolean z) {
        zzq();
        zzah();
        zzex zzex = new zzex(this, str, str2, z, zzi(false), zzq);
        zzd((Runnable) zzex);
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzb(zzga zzga) {
        zzq();
        zzah();
        zzd((Runnable) new zzei(this, zzff() && zzw().zza(zzga), zzga, zzi(true)));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(AtomicReference<List<zzga>> atomicReference, boolean z) {
        zzq();
        zzah();
        zzd((Runnable) new zzej(this, atomicReference, zzi(false), z));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void resetAnalyticsData() {
        zzq();
        zzo();
        zzah();
        zzm zzi = zzi(false);
        if (zzff()) {
            zzw().resetAnalyticsData();
        }
        zzd((Runnable) new zzek(this, zzi));
    }

    private final boolean zzff() {
        zzag();
        return true;
    }

    @WorkerThread
    public final void zza(AtomicReference<String> atomicReference) {
        zzq();
        zzah();
        zzd((Runnable) new zzel(this, atomicReference, zzi(false)));
    }

    @WorkerThread
    public final void getAppInstanceId(zzq zzq) {
        zzq();
        zzah();
        zzd((Runnable) new zzem(this, zzi(false), zzq));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zzfb() {
        zzq();
        zzah();
        zzd((Runnable) new zzen(this, zzi(true)));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public final void zza(zzec zzec) {
        zzq();
        zzah();
        zzd((Runnable) new zzeo(this, zzec));
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzfg() {
        zzq();
        this.zzqn.start();
        this.zzqm.zzv(((Long) zzal.zzhj.get(null)).longValue());
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final void zzfh() {
        boolean z;
        boolean z2;
        zzq();
        zzah();
        if (!isConnected()) {
            boolean z3 = false;
            if (this.zzql == null) {
                zzq();
                zzah();
                Boolean zzdu = zzae().zzdu();
                if (zzdu == null || !zzdu.booleanValue()) {
                    zzag();
                    if (zzt().zzcy() == 1) {
                        z = true;
                        z2 = true;
                    } else {
                        zzad().zzdi().zzaq("Checking service availability");
                        int zzd = zzab().zzd((int) GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE);
                        if (zzd == 9) {
                            zzad().zzdd().zzaq("Service invalid");
                            z = false;
                            z2 = false;
                        } else if (zzd != 18) {
                            switch (zzd) {
                                case 0:
                                    zzad().zzdi().zzaq("Service available");
                                    z = true;
                                    z2 = true;
                                    break;
                                case 1:
                                    zzad().zzdi().zzaq("Service missing");
                                    z = false;
                                    z2 = true;
                                    break;
                                case 2:
                                    zzad().zzdh().zzaq("Service container out of date");
                                    if (zzab().zzgm() >= 15000) {
                                        Boolean zzdu2 = zzae().zzdu();
                                        z = zzdu2 == null || zzdu2.booleanValue();
                                        z2 = false;
                                        break;
                                    } else {
                                        z = false;
                                        z2 = true;
                                        break;
                                    }
                                    break;
                                case 3:
                                    zzad().zzdd().zzaq("Service disabled");
                                    z = false;
                                    z2 = false;
                                    break;
                                default:
                                    zzad().zzdd().zza("Unexpected service status", Integer.valueOf(zzd));
                                    z = false;
                                    z2 = false;
                                    break;
                            }
                        } else {
                            zzad().zzdd().zzaq("Service updating");
                            z = true;
                            z2 = true;
                        }
                    }
                    if (!z && zzaf().zzbw()) {
                        zzad().zzda().zzaq("No way to upload. Consider using the full version of Analytics");
                        z2 = false;
                    }
                    if (z2) {
                        zzae().zzd(z);
                    }
                } else {
                    z = true;
                }
                this.zzql = Boolean.valueOf(z);
            }
            if (this.zzql.booleanValue()) {
                this.zzqj.zzfm();
                return;
            }
            if (!zzaf().zzbw()) {
                zzag();
                List queryIntentServices = getContext().getPackageManager().queryIntentServices(new Intent().setClassName(getContext(), "com.google.android.gms.measurement.AppMeasurementService"), 65536);
                if (queryIntentServices != null && queryIntentServices.size() > 0) {
                    z3 = true;
                }
                if (z3) {
                    Intent intent = new Intent("com.google.android.gms.measurement.START");
                    Context context = getContext();
                    zzag();
                    intent.setComponent(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementService"));
                    this.zzqj.zzb(intent);
                    return;
                }
                zzad().zzda().zzaq("Unable to use remote or local measurement implementation. Please register the AppMeasurementService service in the app manifest");
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final Boolean zzfi() {
        return this.zzql;
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    @VisibleForTesting
    public final void zza(zzam zzam) {
        zzq();
        Preconditions.checkNotNull(zzam);
        this.zzqk = zzam;
        zzfg();
        zzfk();
    }

    @WorkerThread
    public final void disconnect() {
        zzq();
        zzah();
        this.zzqj.zzfl();
        try {
            ConnectionTracker.getInstance().unbindService(getContext(), this.zzqj);
        } catch (IllegalArgumentException | IllegalStateException unused) {
        }
        this.zzqk = null;
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void onServiceDisconnected(ComponentName componentName) {
        zzq();
        if (this.zzqk != null) {
            this.zzqk = null;
            zzad().zzdi().zza("Disconnected from device MeasurementService", componentName);
            zzq();
            zzfh();
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzfj() {
        zzq();
        if (isConnected()) {
            zzad().zzdi().zzaq("Inactivity, disconnecting from the service");
            disconnect();
        }
    }

    @WorkerThread
    private final void zzd(Runnable runnable) throws IllegalStateException {
        zzq();
        if (isConnected()) {
            runnable.run();
        } else if (((long) this.zzqo.size()) >= 1000) {
            zzad().zzda().zzaq("Discarding data. Max runnable queue size reached");
        } else {
            this.zzqo.add(runnable);
            this.zzqp.zzv(60000);
            zzfh();
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzfk() {
        zzq();
        zzad().zzdi().zza("Processing queued up service tasks", Integer.valueOf(this.zzqo.size()));
        for (Runnable run : this.zzqo) {
            try {
                run.run();
            } catch (Exception e) {
                zzad().zzda().zza("Task exception while flushing queue", e);
            }
        }
        this.zzqo.clear();
        this.zzqp.cancel();
    }

    @WorkerThread
    @Nullable
    private final zzm zzi(boolean z) {
        zzag();
        return zzt().zzak(z ? zzad().zzdk() : null);
    }

    @WorkerThread
    public final void zza(zzq zzq, zzaj zzaj, String str) {
        zzq();
        zzah();
        if (zzab().zzd((int) GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE) != 0) {
            zzad().zzdd().zzaq("Not bundling data. Service unavailable or out of date");
            zzab().zza(zzq, new byte[0]);
            return;
        }
        zzd((Runnable) new zzep(this, zzaj, str, zzq));
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

    public final /* bridge */ /* synthetic */ zza zzr() {
        return super.zzr();
    }

    public final /* bridge */ /* synthetic */ zzdd zzs() {
        return super.zzs();
    }

    public final /* bridge */ /* synthetic */ zzap zzt() {
        return super.zzt();
    }

    public final /* bridge */ /* synthetic */ zzeg zzu() {
        return super.zzu();
    }

    public final /* bridge */ /* synthetic */ zzed zzv() {
        return super.zzv();
    }

    public final /* bridge */ /* synthetic */ zzaq zzw() {
        return super.zzw();
    }

    public final /* bridge */ /* synthetic */ zzfj zzx() {
        return super.zzx();
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
