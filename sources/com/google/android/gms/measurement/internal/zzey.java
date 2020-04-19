package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient.BaseConnectionCallbacks;
import com.google.android.gms.common.internal.BaseGmsClient.BaseOnConnectionFailedListener;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.VisibleForTesting;

@VisibleForTesting
public final class zzey implements ServiceConnection, BaseConnectionCallbacks, BaseOnConnectionFailedListener {
    final /* synthetic */ zzeg zzqq;
    /* access modifiers changed from: private */
    public volatile boolean zzqw;
    private volatile zzat zzqx;

    protected zzey(zzeg zzeg) {
        this.zzqq = zzeg;
    }

    @WorkerThread
    public final void zzb(Intent intent) {
        this.zzqq.zzq();
        Context context = this.zzqq.getContext();
        ConnectionTracker instance = ConnectionTracker.getInstance();
        synchronized (this) {
            if (this.zzqw) {
                this.zzqq.zzad().zzdi().zzaq("Connection attempt already in progress");
                return;
            }
            this.zzqq.zzad().zzdi().zzaq("Using local app measurement service");
            this.zzqw = true;
            instance.bindService(context, intent, this.zzqq.zzqj, 129);
        }
    }

    @WorkerThread
    public final void zzfl() {
        if (this.zzqx != null && (this.zzqx.isConnected() || this.zzqx.isConnecting())) {
            this.zzqx.disconnect();
        }
        this.zzqx = null;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r3.zzqq.zzad().zzda().zzaq("Service connect failed to get IMeasurementService");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0063 */
    @androidx.annotation.MainThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onServiceConnected(android.content.ComponentName r4, android.os.IBinder r5) {
        /*
            r3 = this;
            java.lang.String r4 = "MeasurementServiceConnection.onServiceConnected"
            com.google.android.gms.common.internal.Preconditions.checkMainThread(r4)
            monitor-enter(r3)
            r4 = 0
            if (r5 != 0) goto L_0x001f
            r3.zzqw = r4     // Catch:{ all -> 0x001c }
            com.google.android.gms.measurement.internal.zzeg r4 = r3.zzqq     // Catch:{ all -> 0x001c }
            com.google.android.gms.measurement.internal.zzau r4 = r4.zzad()     // Catch:{ all -> 0x001c }
            com.google.android.gms.measurement.internal.zzaw r4 = r4.zzda()     // Catch:{ all -> 0x001c }
            java.lang.String r5 = "Service connected with null binder"
            r4.zzaq(r5)     // Catch:{ all -> 0x001c }
            monitor-exit(r3)     // Catch:{ all -> 0x001c }
            return
        L_0x001c:
            r4 = move-exception
            goto L_0x009a
        L_0x001f:
            r0 = 0
            java.lang.String r1 = r5.getInterfaceDescriptor()     // Catch:{ RemoteException -> 0x0063 }
            java.lang.String r2 = "com.google.android.gms.measurement.internal.IMeasurementService"
            boolean r2 = r2.equals(r1)     // Catch:{ RemoteException -> 0x0063 }
            if (r2 == 0) goto L_0x0053
            if (r5 != 0) goto L_0x002f
            goto L_0x0043
        L_0x002f:
            java.lang.String r1 = "com.google.android.gms.measurement.internal.IMeasurementService"
            android.os.IInterface r1 = r5.queryLocalInterface(r1)     // Catch:{ RemoteException -> 0x0063 }
            boolean r2 = r1 instanceof com.google.android.gms.measurement.internal.zzam     // Catch:{ RemoteException -> 0x0063 }
            if (r2 == 0) goto L_0x003d
            com.google.android.gms.measurement.internal.zzam r1 = (com.google.android.gms.measurement.internal.zzam) r1     // Catch:{ RemoteException -> 0x0063 }
            r0 = r1
            goto L_0x0043
        L_0x003d:
            com.google.android.gms.measurement.internal.zzao r1 = new com.google.android.gms.measurement.internal.zzao     // Catch:{ RemoteException -> 0x0063 }
            r1.<init>(r5)     // Catch:{ RemoteException -> 0x0063 }
            r0 = r1
        L_0x0043:
            com.google.android.gms.measurement.internal.zzeg r5 = r3.zzqq     // Catch:{ RemoteException -> 0x0063 }
            com.google.android.gms.measurement.internal.zzau r5 = r5.zzad()     // Catch:{ RemoteException -> 0x0063 }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzdi()     // Catch:{ RemoteException -> 0x0063 }
            java.lang.String r1 = "Bound to IMeasurementService interface"
            r5.zzaq(r1)     // Catch:{ RemoteException -> 0x0063 }
            goto L_0x0072
        L_0x0053:
            com.google.android.gms.measurement.internal.zzeg r5 = r3.zzqq     // Catch:{ RemoteException -> 0x0063 }
            com.google.android.gms.measurement.internal.zzau r5 = r5.zzad()     // Catch:{ RemoteException -> 0x0063 }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzda()     // Catch:{ RemoteException -> 0x0063 }
            java.lang.String r2 = "Got binder with a wrong descriptor"
            r5.zza(r2, r1)     // Catch:{ RemoteException -> 0x0063 }
            goto L_0x0072
        L_0x0063:
            com.google.android.gms.measurement.internal.zzeg r5 = r3.zzqq     // Catch:{ all -> 0x001c }
            com.google.android.gms.measurement.internal.zzau r5 = r5.zzad()     // Catch:{ all -> 0x001c }
            com.google.android.gms.measurement.internal.zzaw r5 = r5.zzda()     // Catch:{ all -> 0x001c }
            java.lang.String r1 = "Service connect failed to get IMeasurementService"
            r5.zzaq(r1)     // Catch:{ all -> 0x001c }
        L_0x0072:
            if (r0 != 0) goto L_0x008a
            r3.zzqw = r4     // Catch:{ all -> 0x001c }
            com.google.android.gms.common.stats.ConnectionTracker r4 = com.google.android.gms.common.stats.ConnectionTracker.getInstance()     // Catch:{ IllegalArgumentException -> 0x0098 }
            com.google.android.gms.measurement.internal.zzeg r5 = r3.zzqq     // Catch:{ IllegalArgumentException -> 0x0098 }
            android.content.Context r5 = r5.getContext()     // Catch:{ IllegalArgumentException -> 0x0098 }
            com.google.android.gms.measurement.internal.zzeg r0 = r3.zzqq     // Catch:{ IllegalArgumentException -> 0x0098 }
            com.google.android.gms.measurement.internal.zzey r0 = r0.zzqj     // Catch:{ IllegalArgumentException -> 0x0098 }
            r4.unbindService(r5, r0)     // Catch:{ IllegalArgumentException -> 0x0098 }
            goto L_0x0098
        L_0x008a:
            com.google.android.gms.measurement.internal.zzeg r4 = r3.zzqq     // Catch:{ all -> 0x001c }
            com.google.android.gms.measurement.internal.zzbt r4 = r4.zzac()     // Catch:{ all -> 0x001c }
            com.google.android.gms.measurement.internal.zzez r5 = new com.google.android.gms.measurement.internal.zzez     // Catch:{ all -> 0x001c }
            r5.<init>(r3, r0)     // Catch:{ all -> 0x001c }
            r4.zza(r5)     // Catch:{ all -> 0x001c }
        L_0x0098:
            monitor-exit(r3)     // Catch:{ all -> 0x001c }
            return
        L_0x009a:
            monitor-exit(r3)     // Catch:{ all -> 0x001c }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzey.onServiceConnected(android.content.ComponentName, android.os.IBinder):void");
    }

    @MainThread
    public final void onServiceDisconnected(ComponentName componentName) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onServiceDisconnected");
        this.zzqq.zzad().zzdh().zzaq("Service disconnected");
        this.zzqq.zzac().zza((Runnable) new zzfa(this, componentName));
    }

    @WorkerThread
    public final void zzfm() {
        this.zzqq.zzq();
        Context context = this.zzqq.getContext();
        synchronized (this) {
            if (this.zzqw) {
                this.zzqq.zzad().zzdi().zzaq("Connection attempt already in progress");
            } else if (this.zzqx == null || (!this.zzqx.isConnecting() && !this.zzqx.isConnected())) {
                this.zzqx = new zzat(context, Looper.getMainLooper(), this, this);
                this.zzqq.zzad().zzdi().zzaq("Connecting to remote service");
                this.zzqw = true;
                this.zzqx.checkAvailabilityAndConnect();
            } else {
                this.zzqq.zzad().zzdi().zzaq("Already awaiting connection attempt");
            }
        }
    }

    @MainThread
    public final void onConnected(@Nullable Bundle bundle) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnected");
        synchronized (this) {
            try {
                this.zzqq.zzac().zza((Runnable) new zzfb(this, (zzam) this.zzqx.getService()));
            } catch (DeadObjectException | IllegalStateException unused) {
                this.zzqx = null;
                this.zzqw = false;
            }
        }
    }

    @MainThread
    public final void onConnectionSuspended(int i) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionSuspended");
        this.zzqq.zzad().zzdh().zzaq("Service connection suspended");
        this.zzqq.zzac().zza((Runnable) new zzfc(this));
    }

    @MainThread
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Preconditions.checkMainThread("MeasurementServiceConnection.onConnectionFailed");
        zzau zzei = this.zzqq.zzl.zzei();
        if (zzei != null) {
            zzei.zzdd().zza("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.zzqw = false;
            this.zzqx = null;
        }
        this.zzqq.zzac().zza((Runnable) new zzfd(this));
    }
}
