package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;

@Class(creator = "UserAttributeParcelCreator")
public final class zzga extends AbstractSafeParcelable {
    public static final Creator<zzga> CREATOR = new zzgb();
    @Field(mo23591id = 2)
    public final String name;
    @Field(mo23591id = 7)
    public final String origin;
    @Field(mo23591id = 1)
    private final int versionCode;
    @Field(mo23591id = 6)
    public final String zzki;
    @Field(mo23591id = 3)
    public final long zzsx;
    @Field(mo23591id = 4)
    public final Long zzsy;
    @Field(mo23591id = 5)
    private final Float zzsz;
    @Field(mo23591id = 8)
    public final Double zzta;

    zzga(zzgc zzgc) {
        this(zzgc.name, zzgc.zzsx, zzgc.value, zzgc.origin);
    }

    zzga(String str, long j, Object obj, String str2) {
        Preconditions.checkNotEmpty(str);
        this.versionCode = 2;
        this.name = str;
        this.zzsx = j;
        this.origin = str2;
        if (obj == null) {
            this.zzsy = null;
            this.zzsz = null;
            this.zzta = null;
            this.zzki = null;
        } else if (obj instanceof Long) {
            this.zzsy = (Long) obj;
            this.zzsz = null;
            this.zzta = null;
            this.zzki = null;
        } else if (obj instanceof String) {
            this.zzsy = null;
            this.zzsz = null;
            this.zzta = null;
            this.zzki = (String) obj;
        } else if (obj instanceof Double) {
            this.zzsy = null;
            this.zzsz = null;
            this.zzta = (Double) obj;
            this.zzki = null;
        } else {
            throw new IllegalArgumentException("User attribute given of un-supported type");
        }
    }

    zzga(String str, long j, String str2) {
        Preconditions.checkNotEmpty(str);
        this.versionCode = 2;
        this.name = str;
        this.zzsx = 0;
        this.zzsy = null;
        this.zzsz = null;
        this.zzta = null;
        this.zzki = null;
        this.origin = null;
    }

    @Constructor
    zzga(@Param(mo23594id = 1) int i, @Param(mo23594id = 2) String str, @Param(mo23594id = 3) long j, @Param(mo23594id = 4) Long l, @Param(mo23594id = 5) Float f, @Param(mo23594id = 6) String str2, @Param(mo23594id = 7) String str3, @Param(mo23594id = 8) Double d) {
        this.versionCode = i;
        this.name = str;
        this.zzsx = j;
        this.zzsy = l;
        Double d2 = null;
        this.zzsz = null;
        if (i == 1) {
            if (f != null) {
                d2 = Double.valueOf(f.doubleValue());
            }
            this.zzta = d2;
        } else {
            this.zzta = d;
        }
        this.zzki = str2;
        this.origin = str3;
    }

    public final Object getValue() {
        Long l = this.zzsy;
        if (l != null) {
            return l;
        }
        Double d = this.zzta;
        if (d != null) {
            return d;
        }
        String str = this.zzki;
        if (str != null) {
            return str;
        }
        return null;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeLong(parcel, 3, this.zzsx);
        SafeParcelWriter.writeLongObject(parcel, 4, this.zzsy, false);
        SafeParcelWriter.writeFloatObject(parcel, 5, null, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzki, false);
        SafeParcelWriter.writeString(parcel, 7, this.origin, false);
        SafeParcelWriter.writeDoubleObject(parcel, 8, this.zzta, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
