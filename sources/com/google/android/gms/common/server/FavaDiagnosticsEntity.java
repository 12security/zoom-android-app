package com.google.android.gms.common.server;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;

@KeepForSdk
@Class(creator = "FavaDiagnosticsEntityCreator")
public class FavaDiagnosticsEntity extends AbstractSafeParcelable implements ReflectedParcelable {
    @KeepForSdk
    public static final Creator<FavaDiagnosticsEntity> CREATOR = new zaa();
    @VersionField(mo23597id = 1)
    private final int zale;
    @Field(mo23591id = 2)
    private final String zapi;
    @Field(mo23591id = 3)
    private final int zapj;

    @Constructor
    public FavaDiagnosticsEntity(@Param(mo23594id = 1) int i, @Param(mo23594id = 2) String str, @Param(mo23594id = 3) int i2) {
        this.zale = i;
        this.zapi = str;
        this.zapj = i2;
    }

    @KeepForSdk
    public FavaDiagnosticsEntity(String str, int i) {
        this.zale = 1;
        this.zapi = str;
        this.zapj = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zale);
        SafeParcelWriter.writeString(parcel, 2, this.zapi, false);
        SafeParcelWriter.writeInt(parcel, 3, this.zapj);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
