package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.IAccountAccessor.Stub;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;

@KeepForSdk
@Class(creator = "GetServiceRequestCreator")
@Reserved({9})
public class GetServiceRequest extends AbstractSafeParcelable {
    public static final Creator<GetServiceRequest> CREATOR = new zzd();
    @VersionField(mo23597id = 1)
    private final int version;
    @Field(mo23591id = 2)
    private final int zzdg;
    @Field(mo23591id = 3)
    private int zzdh;
    @Field(mo23591id = 5)
    IBinder zzdi;
    @Field(mo23591id = 6)
    Scope[] zzdj;
    @Field(mo23591id = 7)
    Bundle zzdk;
    @Field(mo23591id = 8)
    Account zzdl;
    @Field(mo23591id = 10)
    Feature[] zzdm;
    @Field(mo23591id = 11)
    Feature[] zzdn;
    @Field(mo23591id = 12)
    private boolean zzdo;
    @Field(mo23591id = 4)
    String zzy;

    public GetServiceRequest(int i) {
        this.version = 4;
        this.zzdh = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzdg = i;
        this.zzdo = true;
    }

    @Constructor
    GetServiceRequest(@Param(mo23594id = 1) int i, @Param(mo23594id = 2) int i2, @Param(mo23594id = 3) int i3, @Param(mo23594id = 4) String str, @Param(mo23594id = 5) IBinder iBinder, @Param(mo23594id = 6) Scope[] scopeArr, @Param(mo23594id = 7) Bundle bundle, @Param(mo23594id = 8) Account account, @Param(mo23594id = 10) Feature[] featureArr, @Param(mo23594id = 11) Feature[] featureArr2, @Param(mo23594id = 12) boolean z) {
        this.version = i;
        this.zzdg = i2;
        this.zzdh = i3;
        if ("com.google.android.gms".equals(str)) {
            this.zzy = "com.google.android.gms";
        } else {
            this.zzy = str;
        }
        if (i < 2) {
            Account account2 = null;
            if (iBinder != null) {
                account2 = AccountAccessor.getAccountBinderSafe(Stub.asInterface(iBinder));
            }
            this.zzdl = account2;
        } else {
            this.zzdi = iBinder;
            this.zzdl = account;
        }
        this.zzdj = scopeArr;
        this.zzdk = bundle;
        this.zzdm = featureArr;
        this.zzdn = featureArr2;
        this.zzdo = z;
    }

    @KeepForSdk
    public Bundle getExtraArgs() {
        return this.zzdk;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.version);
        SafeParcelWriter.writeInt(parcel, 2, this.zzdg);
        SafeParcelWriter.writeInt(parcel, 3, this.zzdh);
        SafeParcelWriter.writeString(parcel, 4, this.zzy, false);
        SafeParcelWriter.writeIBinder(parcel, 5, this.zzdi, false);
        SafeParcelWriter.writeTypedArray(parcel, 6, this.zzdj, i, false);
        SafeParcelWriter.writeBundle(parcel, 7, this.zzdk, false);
        SafeParcelWriter.writeParcelable(parcel, 8, this.zzdl, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 10, this.zzdm, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 11, this.zzdn, i, false);
        SafeParcelWriter.writeBoolean(parcel, 12, this.zzdo);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
