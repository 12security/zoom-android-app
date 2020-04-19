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
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;

@Class(creator = "EventParcelCreator")
@Reserved({1})
public final class zzaj extends AbstractSafeParcelable {
    public static final Creator<zzaj> CREATOR = new zzak();
    @Field(mo23591id = 2)
    public final String name;
    @Field(mo23591id = 4)
    public final String origin;
    @Field(mo23591id = 3)
    public final zzag zzfd;
    @Field(mo23591id = 5)
    public final long zzfp;

    @Constructor
    public zzaj(@Param(mo23594id = 2) String str, @Param(mo23594id = 3) zzag zzag, @Param(mo23594id = 4) String str2, @Param(mo23594id = 5) long j) {
        this.name = str;
        this.zzfd = zzag;
        this.origin = str2;
        this.zzfp = j;
    }

    zzaj(zzaj zzaj, long j) {
        Preconditions.checkNotNull(zzaj);
        this.name = zzaj.name;
        this.zzfd = zzaj.zzfd;
        this.origin = zzaj.origin;
        this.zzfp = j;
    }

    public final String toString() {
        String str = this.origin;
        String str2 = this.name;
        String valueOf = String.valueOf(this.zzfd);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 21 + String.valueOf(str2).length() + String.valueOf(valueOf).length());
        sb.append("origin=");
        sb.append(str);
        sb.append(",name=");
        sb.append(str2);
        sb.append(",params=");
        sb.append(valueOf);
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzfd, i, false);
        SafeParcelWriter.writeString(parcel, 4, this.origin, false);
        SafeParcelWriter.writeLong(parcel, 5, this.zzfp);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
