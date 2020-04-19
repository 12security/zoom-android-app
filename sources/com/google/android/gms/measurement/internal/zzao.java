package com.google.android.gms.measurement.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zza;
import com.google.android.gms.internal.measurement.zzc;
import java.util.ArrayList;
import java.util.List;

public final class zzao extends zza implements zzam {
    zzao(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.internal.IMeasurementService");
    }

    public final void zza(zzaj zzaj, zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzaj);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        zza(1, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzga zzga, zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzga);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        zza(2, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        zza(4, obtainAndWriteInterfaceToken);
    }

    public final void zza(zzaj zzaj, String str, String str2) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzaj);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zza(5, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        zza(6, obtainAndWriteInterfaceToken);
    }

    public final List<zzga> zza(zzm zzm, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z);
        Parcel transactAndReadException = transactAndReadException(7, obtainAndWriteInterfaceToken);
        ArrayList createTypedArrayList = transactAndReadException.createTypedArrayList(zzga.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final byte[] zza(zzaj zzaj, String str) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzaj);
        obtainAndWriteInterfaceToken.writeString(str);
        Parcel transactAndReadException = transactAndReadException(9, obtainAndWriteInterfaceToken);
        byte[] createByteArray = transactAndReadException.createByteArray();
        transactAndReadException.recycle();
        return createByteArray;
    }

    public final void zza(long j, String str, String str2, String str3) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeLong(j);
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        zza(10, obtainAndWriteInterfaceToken);
    }

    public final String zzc(zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        Parcel transactAndReadException = transactAndReadException(11, obtainAndWriteInterfaceToken);
        String readString = transactAndReadException.readString();
        transactAndReadException.recycle();
        return readString;
    }

    public final void zza(zzr zzr, zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzr);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        zza(12, obtainAndWriteInterfaceToken);
    }

    public final void zzb(zzr zzr) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzr);
        zza(13, obtainAndWriteInterfaceToken);
    }

    public final List<zzga> zza(String str, String str2, boolean z, zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        Parcel transactAndReadException = transactAndReadException(14, obtainAndWriteInterfaceToken);
        ArrayList createTypedArrayList = transactAndReadException.createTypedArrayList(zzga.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final List<zzga> zza(String str, String str2, String str3, boolean z) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        zzc.writeBoolean(obtainAndWriteInterfaceToken, z);
        Parcel transactAndReadException = transactAndReadException(15, obtainAndWriteInterfaceToken);
        ArrayList createTypedArrayList = transactAndReadException.createTypedArrayList(zzga.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final List<zzr> zza(String str, String str2, zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        Parcel transactAndReadException = transactAndReadException(16, obtainAndWriteInterfaceToken);
        ArrayList createTypedArrayList = transactAndReadException.createTypedArrayList(zzr.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final List<zzr> zzd(String str, String str2, String str3) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        obtainAndWriteInterfaceToken.writeString(str);
        obtainAndWriteInterfaceToken.writeString(str2);
        obtainAndWriteInterfaceToken.writeString(str3);
        Parcel transactAndReadException = transactAndReadException(17, obtainAndWriteInterfaceToken);
        ArrayList createTypedArrayList = transactAndReadException.createTypedArrayList(zzr.CREATOR);
        transactAndReadException.recycle();
        return createTypedArrayList;
    }

    public final void zzd(zzm zzm) throws RemoteException {
        Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
        zzc.zza(obtainAndWriteInterfaceToken, (Parcelable) zzm);
        zza(18, obtainAndWriteInterfaceToken);
    }
}
