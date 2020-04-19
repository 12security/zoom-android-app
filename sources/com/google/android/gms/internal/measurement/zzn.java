package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.util.Map;

public interface zzn extends IInterface {
    void beginAdUnitExposure(String str, long j) throws RemoteException;

    void clearConditionalUserProperty(String str, String str2, Bundle bundle) throws RemoteException;

    void endAdUnitExposure(String str, long j) throws RemoteException;

    void generateEventId(zzq zzq) throws RemoteException;

    void getAppInstanceId(zzq zzq) throws RemoteException;

    void getCachedAppInstanceId(zzq zzq) throws RemoteException;

    void getConditionalUserProperties(String str, String str2, zzq zzq) throws RemoteException;

    void getCurrentScreenClass(zzq zzq) throws RemoteException;

    void getCurrentScreenName(zzq zzq) throws RemoteException;

    void getGmpAppId(zzq zzq) throws RemoteException;

    void getMaxUserProperties(String str, zzq zzq) throws RemoteException;

    void getTestFlag(zzq zzq, int i) throws RemoteException;

    void getUserProperties(String str, String str2, boolean z, zzq zzq) throws RemoteException;

    void initForTests(Map map) throws RemoteException;

    void initialize(IObjectWrapper iObjectWrapper, zzy zzy, long j) throws RemoteException;

    void isDataCollectionEnabled(zzq zzq) throws RemoteException;

    void logEvent(String str, String str2, Bundle bundle, boolean z, boolean z2, long j) throws RemoteException;

    void logEventAndBundle(String str, String str2, Bundle bundle, zzq zzq, long j) throws RemoteException;

    void logHealthData(int i, String str, IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, IObjectWrapper iObjectWrapper3) throws RemoteException;

    void onActivityCreated(IObjectWrapper iObjectWrapper, Bundle bundle, long j) throws RemoteException;

    void onActivityDestroyed(IObjectWrapper iObjectWrapper, long j) throws RemoteException;

    void onActivityPaused(IObjectWrapper iObjectWrapper, long j) throws RemoteException;

    void onActivityResumed(IObjectWrapper iObjectWrapper, long j) throws RemoteException;

    void onActivitySaveInstanceState(IObjectWrapper iObjectWrapper, zzq zzq, long j) throws RemoteException;

    void onActivityStarted(IObjectWrapper iObjectWrapper, long j) throws RemoteException;

    void onActivityStopped(IObjectWrapper iObjectWrapper, long j) throws RemoteException;

    void performAction(Bundle bundle, zzq zzq, long j) throws RemoteException;

    void registerOnMeasurementEventListener(zzt zzt) throws RemoteException;

    void resetAnalyticsData(long j) throws RemoteException;

    void setConditionalUserProperty(Bundle bundle, long j) throws RemoteException;

    void setCurrentScreen(IObjectWrapper iObjectWrapper, String str, String str2, long j) throws RemoteException;

    void setDataCollectionEnabled(boolean z) throws RemoteException;

    void setEventInterceptor(zzt zzt) throws RemoteException;

    void setInstanceIdProvider(zzw zzw) throws RemoteException;

    void setMeasurementEnabled(boolean z, long j) throws RemoteException;

    void setMinimumSessionDuration(long j) throws RemoteException;

    void setSessionTimeoutDuration(long j) throws RemoteException;

    void setUserId(String str, long j) throws RemoteException;

    void setUserProperty(String str, String str2, IObjectWrapper iObjectWrapper, boolean z, long j) throws RemoteException;

    void unregisterOnMeasurementEventListener(zzt zzt) throws RemoteException;
}
