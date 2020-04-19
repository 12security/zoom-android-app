package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.PersistableBundle;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.internal.measurement.zzh;
import com.zipow.videobox.view.p014mm.MMContentFileViewerFragment;

public final class zzfp extends zzfs {
    private final AlarmManager zzrp = ((AlarmManager) getContext().getSystemService(NotificationCompat.CATEGORY_ALARM));
    private final zzab zzrq;
    private Integer zzrr;

    protected zzfp(zzft zzft) {
        super(zzft);
        this.zzrq = new zzfq(this, zzft.zzgi(), zzft);
    }

    /* access modifiers changed from: protected */
    public final boolean zzak() {
        this.zzrp.cancel(zzft());
        if (VERSION.SDK_INT >= 24) {
            zzfs();
        }
        return false;
    }

    @TargetApi(24)
    private final void zzfs() {
        JobScheduler jobScheduler = (JobScheduler) getContext().getSystemService("jobscheduler");
        int jobId = getJobId();
        zzad().zzdi().zza("Cancelling job. JobID", Integer.valueOf(jobId));
        jobScheduler.cancel(jobId);
    }

    public final void zzv(long j) {
        zzah();
        zzag();
        Context context = getContext();
        if (!zzbo.zzl(context)) {
            zzad().zzdh().zzaq("Receiver not registered/enabled");
        }
        if (!zzgd.zzb(context, false)) {
            zzad().zzdh().zzaq("Service not registered/enabled");
        }
        cancel();
        long elapsedRealtime = zzz().elapsedRealtime() + j;
        if (j < Math.max(0, ((Long) zzal.zzha.get(null)).longValue()) && !this.zzrq.zzcn()) {
            zzad().zzdi().zzaq("Scheduling upload with DelayedRunnable");
            this.zzrq.zzv(j);
        }
        zzag();
        if (VERSION.SDK_INT >= 24) {
            zzad().zzdi().zzaq("Scheduling upload with JobScheduler");
            Context context2 = getContext();
            ComponentName componentName = new ComponentName(context2, "com.google.android.gms.measurement.AppMeasurementJobService");
            int jobId = getJobId();
            PersistableBundle persistableBundle = new PersistableBundle();
            persistableBundle.putString(MMContentFileViewerFragment.RESULT_ACTION, "com.google.android.gms.measurement.UPLOAD");
            JobInfo build = new Builder(jobId, componentName).setMinimumLatency(j).setOverrideDeadline(j << 1).setExtras(persistableBundle).build();
            zzad().zzdi().zza("Scheduling job. JobID", Integer.valueOf(jobId));
            zzh.zza(context2, build, "com.google.android.gms", "UploadAlarm");
            return;
        }
        zzad().zzdi().zzaq("Scheduling upload with AlarmManager");
        this.zzrp.setInexactRepeating(2, elapsedRealtime, Math.max(((Long) zzal.zzgv.get(null)).longValue(), j), zzft());
    }

    private final int getJobId() {
        if (this.zzrr == null) {
            String str = "measurement";
            String valueOf = String.valueOf(getContext().getPackageName());
            this.zzrr = Integer.valueOf((valueOf.length() != 0 ? str.concat(valueOf) : new String(str)).hashCode());
        }
        return this.zzrr.intValue();
    }

    public final void cancel() {
        zzah();
        this.zzrp.cancel(zzft());
        this.zzrq.cancel();
        if (VERSION.SDK_INT >= 24) {
            zzfs();
        }
    }

    private final PendingIntent zzft() {
        Context context = getContext();
        return PendingIntent.getBroadcast(context, 0, new Intent().setClassName(context, "com.google.android.gms.measurement.AppMeasurementReceiver").setAction("com.google.android.gms.measurement.UPLOAD"), 0);
    }

    public final /* bridge */ /* synthetic */ zzfz zzdm() {
        return super.zzdm();
    }

    public final /* bridge */ /* synthetic */ zzo zzdn() {
        return super.zzdn();
    }

    public final /* bridge */ /* synthetic */ zzw zzdo() {
        return super.zzdo();
    }

    public final /* bridge */ /* synthetic */ zzbs zzdp() {
        return super.zzdp();
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
