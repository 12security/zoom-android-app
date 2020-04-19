package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.measurement.zzb;
import com.google.android.gms.internal.measurement.zzc;
import java.util.List;

public abstract class zzan extends zzb implements zzam {
    public zzan() {
        super("com.google.android.gms.measurement.internal.IMeasurementService");
    }

    /* access modifiers changed from: protected */
    public final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        switch (i) {
            case 1:
                zza((zzaj) zzc.zza(parcel, zzaj.CREATOR), (zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                break;
            case 2:
                zza((zzga) zzc.zza(parcel, zzga.CREATOR), (zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                break;
            case 4:
                zza((zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                break;
            case 5:
                zza((zzaj) zzc.zza(parcel, zzaj.CREATOR), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                break;
            case 6:
                zzb((zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                break;
            case 7:
                List zza = zza((zzm) zzc.zza(parcel, zzm.CREATOR), zzc.zza(parcel));
                parcel2.writeNoException();
                parcel2.writeTypedList(zza);
                break;
            case 9:
                byte[] zza2 = zza((zzaj) zzc.zza(parcel, zzaj.CREATOR), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeByteArray(zza2);
                break;
            case 10:
                zza(parcel.readLong(), parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                break;
            case 11:
                String zzc = zzc((zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                parcel2.writeString(zzc);
                break;
            case 12:
                zza((zzr) zzc.zza(parcel, zzr.CREATOR), (zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                break;
            case 13:
                zzb((zzr) zzc.zza(parcel, zzr.CREATOR));
                parcel2.writeNoException();
                break;
            case 14:
                List zza3 = zza(parcel.readString(), parcel.readString(), zzc.zza(parcel), (zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(zza3);
                break;
            case 15:
                List zza4 = zza(parcel.readString(), parcel.readString(), parcel.readString(), zzc.zza(parcel));
                parcel2.writeNoException();
                parcel2.writeTypedList(zza4);
                break;
            case 16:
                List zza5 = zza(parcel.readString(), parcel.readString(), (zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(zza5);
                break;
            case 17:
                List zzd = zzd(parcel.readString(), parcel.readString(), parcel.readString());
                parcel2.writeNoException();
                parcel2.writeTypedList(zzd);
                break;
            case 18:
                zzd((zzm) zzc.zza(parcel, zzm.CREATOR));
                parcel2.writeNoException();
                break;
            default:
                return false;
        }
        return true;
    }
}
