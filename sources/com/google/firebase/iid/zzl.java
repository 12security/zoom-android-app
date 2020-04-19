package com.google.firebase.iid;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;

final class zzl implements Creator<zzm> {
    zzl() {
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzm[i];
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        IBinder readStrongBinder = parcel.readStrongBinder();
        if (readStrongBinder != null) {
            return new zzm(readStrongBinder);
        }
        return null;
    }
}
