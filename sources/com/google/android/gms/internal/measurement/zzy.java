package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;

@Class(creator = "InitializationParamsCreator")
public final class zzy extends AbstractSafeParcelable {
    public static final Creator<zzy> CREATOR = new zzz();
    @Field(mo23591id = 5)
    public final String origin;
    @Field(mo23591id = 1)
    public final long zzt;
    @Field(mo23591id = 2)
    public final long zzu;
    @Field(mo23591id = 3)
    public final boolean zzv;
    @Field(mo23591id = 4)
    public final String zzw;
    @Field(mo23591id = 6)
    public final String zzx;
    @Field(mo23591id = 7)
    public final Bundle zzy;

    @Constructor
    public zzy(@Param(mo23594id = 1) long j, @Param(mo23594id = 2) long j2, @Param(mo23594id = 3) boolean z, @Param(mo23594id = 4) String str, @Param(mo23594id = 5) String str2, @Param(mo23594id = 6) String str3, @Param(mo23594id = 7) Bundle bundle) {
        this.zzt = j;
        this.zzu = j2;
        this.zzv = z;
        this.zzw = str;
        this.origin = str2;
        this.zzx = str3;
        this.zzy = bundle;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeLong(parcel, 1, this.zzt);
        SafeParcelWriter.writeLong(parcel, 2, this.zzu);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzv);
        SafeParcelWriter.writeString(parcel, 4, this.zzw, false);
        SafeParcelWriter.writeString(parcel, 5, this.origin, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzx, false);
        SafeParcelWriter.writeBundle(parcel, 7, this.zzy, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
