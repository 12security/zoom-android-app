package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;

@Class(creator = "AppMetadataCreator")
@Reserved({1, 20})
public final class zzm extends AbstractSafeParcelable {
    public static final Creator<zzm> CREATOR = new zzn();
    @Field(mo23591id = 2)
    public final String packageName;
    @Field(mo23591id = 3)
    public final String zzch;
    @Field(mo23591id = 12)
    public final String zzcj;
    @Field(mo23591id = 4)
    public final String zzcn;
    @Field(defaultValueUnchecked = "Integer.MIN_VALUE", mo23591id = 11)
    public final long zzco;
    @Field(mo23591id = 5)
    public final String zzcp;
    @Field(mo23591id = 7)
    public final long zzcq;
    @Field(defaultValue = "true", mo23591id = 9)
    public final boolean zzcr;
    @Field(mo23591id = 13)
    public final long zzcs;
    @Field(defaultValue = "true", mo23591id = 16)
    public final boolean zzct;
    @Field(defaultValue = "true", mo23591id = 17)
    public final boolean zzcu;
    @Field(mo23591id = 19)
    public final String zzcv;
    @Field(mo23591id = 21)
    public final Boolean zzcw;
    @Field(mo23591id = 8)
    public final String zzdn;
    @Field(mo23591id = 10)
    public final boolean zzdo;
    @Field(mo23591id = 14)
    public final long zzdp;
    @Field(mo23591id = 15)
    public final int zzdq;
    @Field(mo23591id = 18)
    public final boolean zzdr;
    @Field(mo23591id = 6)
    public final long zzt;
    @Field(mo23591id = 22)
    public final long zzu;

    zzm(String str, String str2, String str3, long j, String str4, long j2, long j3, String str5, boolean z, boolean z2, String str6, long j4, long j5, int i, boolean z3, boolean z4, boolean z5, String str7, Boolean bool, long j6) {
        Preconditions.checkNotEmpty(str);
        this.packageName = str;
        this.zzch = TextUtils.isEmpty(str2) ? null : str2;
        this.zzcn = str3;
        this.zzco = j;
        this.zzcp = str4;
        this.zzt = j2;
        this.zzcq = j3;
        this.zzdn = str5;
        this.zzcr = z;
        this.zzdo = z2;
        this.zzcj = str6;
        this.zzcs = j4;
        this.zzdp = j5;
        this.zzdq = i;
        this.zzct = z3;
        this.zzcu = z4;
        this.zzdr = z5;
        this.zzcv = str7;
        this.zzcw = bool;
        this.zzu = j6;
    }

    @Constructor
    zzm(@Param(mo23594id = 2) String str, @Param(mo23594id = 3) String str2, @Param(mo23594id = 4) String str3, @Param(mo23594id = 5) String str4, @Param(mo23594id = 6) long j, @Param(mo23594id = 7) long j2, @Param(mo23594id = 8) String str5, @Param(mo23594id = 9) boolean z, @Param(mo23594id = 10) boolean z2, @Param(mo23594id = 11) long j3, @Param(mo23594id = 12) String str6, @Param(mo23594id = 13) long j4, @Param(mo23594id = 14) long j5, @Param(mo23594id = 15) int i, @Param(mo23594id = 16) boolean z3, @Param(mo23594id = 17) boolean z4, @Param(mo23594id = 18) boolean z5, @Param(mo23594id = 19) String str7, @Param(mo23594id = 21) Boolean bool, @Param(mo23594id = 22) long j6) {
        this.packageName = str;
        this.zzch = str2;
        this.zzcn = str3;
        this.zzco = j3;
        this.zzcp = str4;
        this.zzt = j;
        this.zzcq = j2;
        this.zzdn = str5;
        this.zzcr = z;
        this.zzdo = z2;
        this.zzcj = str6;
        this.zzcs = j4;
        this.zzdp = j5;
        this.zzdq = i;
        this.zzct = z3;
        this.zzcu = z4;
        this.zzdr = z5;
        this.zzcv = str7;
        this.zzcw = bool;
        this.zzu = j6;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzch, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzcn, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzcp, false);
        SafeParcelWriter.writeLong(parcel, 6, this.zzt);
        SafeParcelWriter.writeLong(parcel, 7, this.zzcq);
        SafeParcelWriter.writeString(parcel, 8, this.zzdn, false);
        SafeParcelWriter.writeBoolean(parcel, 9, this.zzcr);
        SafeParcelWriter.writeBoolean(parcel, 10, this.zzdo);
        SafeParcelWriter.writeLong(parcel, 11, this.zzco);
        SafeParcelWriter.writeString(parcel, 12, this.zzcj, false);
        SafeParcelWriter.writeLong(parcel, 13, this.zzcs);
        SafeParcelWriter.writeLong(parcel, 14, this.zzdp);
        SafeParcelWriter.writeInt(parcel, 15, this.zzdq);
        SafeParcelWriter.writeBoolean(parcel, 16, this.zzct);
        SafeParcelWriter.writeBoolean(parcel, 17, this.zzcu);
        SafeParcelWriter.writeBoolean(parcel, 18, this.zzdr);
        SafeParcelWriter.writeString(parcel, 19, this.zzcv, false);
        SafeParcelWriter.writeBooleanObject(parcel, 21, this.zzcw, false);
        SafeParcelWriter.writeLong(parcel, 22, this.zzu);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
