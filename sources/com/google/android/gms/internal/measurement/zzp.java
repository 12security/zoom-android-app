package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.util.Map;

public final class zzp extends zza implements zzn {
    zzp(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.api.internal.IAppMeasurementDynamiteService");
    }

    public final void initialize(IObjectWrapper iObjectWrapper, zzy zzy, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzy);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(1, obtainAndWriteInterfaceToken);
    }

    public final void logEvent(String str, String str2, Bundle bundle, boolean z, boolean z2, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z);
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z2);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(2, obtainAndWriteInterfaceToken);
    }

    public final void logEventAndBundle(String str, String str2, Bundle bundle, zzq zzq, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(3, obtainAndWriteInterfaceToken);
    }

    public final void setUserProperty(String str, String str2, IObjectWrapper iObjectWrapper, boolean z, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(4, obtainAndWriteInterfaceToken);
    }

    public final void getUserProperties(String str, String str2, boolean z, zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(5, obtainAndWriteInterfaceToken);
    }

    public final void getMaxUserProperties(String str, zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(6, obtainAndWriteInterfaceToken);
    }

    public final void setUserId(String str, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(7, obtainAndWriteInterfaceToken);
    }

    public final void setConditionalUserProperty(Bundle bundle, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(8, obtainAndWriteInterfaceToken);
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        zza(9, obtainAndWriteInterfaceToken);
    }

    public final void getConditionalUserProperties(String str, String str2, zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(10, obtainAndWriteInterfaceToken);
    }

    public final void setMeasurementEnabled(boolean z, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(11, obtainAndWriteInterfaceToken);
    }

    public final void resetAnalyticsData(long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(12, obtainAndWriteInterfaceToken);
    }

    public final void setMinimumSessionDuration(long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(13, obtainAndWriteInterfaceToken);
    }

    public final void setSessionTimeoutDuration(long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(14, obtainAndWriteInterfaceToken);
    }

    public final void setCurrentScreen(IObjectWrapper iObjectWrapper, String str, String str2, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(15, obtainAndWriteInterfaceToken);
    }

    public final void getCurrentScreenName(zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(16, obtainAndWriteInterfaceToken);
    }

    public final void getCurrentScreenClass(zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(17, obtainAndWriteInterfaceToken);
    }

    public final void setInstanceIdProvider(zzw zzw) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzw);
        zza(18, obtainAndWriteInterfaceToken);
    }

    public final void getCachedAppInstanceId(zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(19, obtainAndWriteInterfaceToken);
    }

    public final void getAppInstanceId(zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(20, obtainAndWriteInterfaceToken);
    }

    public final void getGmpAppId(zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(21, obtainAndWriteInterfaceToken);
    }

    public final void generateEventId(zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(22, obtainAndWriteInterfaceToken);
    }

    public final void beginAdUnitExposure(String str, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(23, obtainAndWriteInterfaceToken);
    }

    public final void endAdUnitExposure(String str, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(24, obtainAndWriteInterfaceToken);
    }

    public final void onActivityStarted(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(25, obtainAndWriteInterfaceToken);
    }

    public final void onActivityStopped(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(26, obtainAndWriteInterfaceToken);
    }

    public final void onActivityCreated(IObjectWrapper iObjectWrapper, Bundle bundle, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(27, obtainAndWriteInterfaceToken);
    }

    public final void onActivityDestroyed(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(28, obtainAndWriteInterfaceToken);
    }

    public final void onActivityPaused(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(29, obtainAndWriteInterfaceToken);
    }

    public final void onActivityResumed(IObjectWrapper iObjectWrapper, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(30, obtainAndWriteInterfaceToken);
    }

    public final void onActivitySaveInstanceState(IObjectWrapper iObjectWrapper, zzq zzq, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(31, obtainAndWriteInterfaceToken);
    }

    public final void performAction(Bundle bundle, zzq zzq, long j) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) bundle);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        obtainAndWriteInterfaceToken.writeLong(j);
        zza(32, obtainAndWriteInterfaceToken);
    }

    public final void logHealthData(int i, String str, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, IObjectWrapper iObjectWrapper3) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeInt(i);
        obtainAndWriteInterfaceToken.writeString(str);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper2);
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) iObjectWrapper3);
        zza(33, obtainAndWriteInterfaceToken);
    }

    public final void setEventInterceptor(zzt zzt) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzt);
        zza(34, obtainAndWriteInterfaceToken);
    }

    public final void registerOnMeasurementEventListener(zzt zzt) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzt);
        zza(35, obtainAndWriteInterfaceToken);
    }

    public final void unregisterOnMeasurementEventListener(zzt zzt) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzt);
        zza(36, obtainAndWriteInterfaceToken);
    }

    public final void initForTests(Map map) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeMap(map);
        zza(37, obtainAndWriteInterfaceToken);
    }

    public final void getTestFlag(zzq zzq, int i) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        obtainAndWriteInterfaceToken.writeInt(i);
        zza(38, obtainAndWriteInterfaceToken);
    }

    public final void setDataCollectionEnabled(boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z);
        zza(39, obtainAndWriteInterfaceToken);
    }

    public final void isDataCollectionEnabled(zzq zzq) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (IInterface) zzq);
        zza(40, obtainAndWriteInterfaceToken);
    }
}
