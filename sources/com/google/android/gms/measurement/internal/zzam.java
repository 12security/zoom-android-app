package com.google.android.gms.measurement.internal;

import android.os.IInterface;
import android.os.RemoteException;
import java.util.List;

public interface zzam extends IInterface {
    List<zzga> zza(zzm zzm, boolean z) throws RemoteException;

    List<zzr> zza(String str, String str2, zzm zzm) throws RemoteException;

    List<zzga> zza(String str, String str2, String str3, boolean z) throws RemoteException;

    List<zzga> zza(String str, String str2, boolean z, zzm zzm) throws RemoteException;

    void zza(long j, String str, String str2, String str3) throws RemoteException;

    void zza(zzaj zzaj, zzm zzm) throws RemoteException;

    void zza(zzaj zzaj, String str, String str2) throws RemoteException;

    void zza(zzga zzga, zzm zzm) throws RemoteException;

    void zza(zzm zzm) throws RemoteException;

    void zza(zzr zzr, zzm zzm) throws RemoteException;

    byte[] zza(zzaj zzaj, String str) throws RemoteException;

    void zzb(zzm zzm) throws RemoteException;

    void zzb(zzr zzr) throws RemoteException;

    String zzc(zzm zzm) throws RemoteException;

    List<zzr> zzd(String str, String str2, String str3) throws RemoteException;

    void zzd(zzm zzm) throws RemoteException;
}
