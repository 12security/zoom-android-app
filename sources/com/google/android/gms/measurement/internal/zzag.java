package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.Iterator;

@Class(creator = "EventParamsCreator")
@Reserved({1})
public final class zzag extends AbstractSafeParcelable implements Iterable<String> {
    public static final Creator<zzag> CREATOR = new zzai();
    /* access modifiers changed from: private */
    @Field(getter = "z", mo23591id = 2)
    public final Bundle zzfm;

    @Constructor
    zzag(@Param(mo23594id = 2) Bundle bundle) {
        this.zzfm = bundle;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, zzct(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    /* access modifiers changed from: 0000 */
    public final Object get(String str) {
        return this.zzfm.get(str);
    }

    /* access modifiers changed from: 0000 */
    public final Long getLong(String str) {
        return Long.valueOf(this.zzfm.getLong(str));
    }

    /* access modifiers changed from: 0000 */
    public final Double zzaj(String str) {
        return Double.valueOf(this.zzfm.getDouble(str));
    }

    /* access modifiers changed from: 0000 */
    public final String getString(String str) {
        return this.zzfm.getString(str);
    }

    public final int size() {
        return this.zzfm.size();
    }

    public final String toString() {
        return this.zzfm.toString();
    }

    public final Bundle zzct() {
        return new Bundle(this.zzfm);
    }

    public final Iterator<String> iterator() {
        return new zzah(this);
    }
}
