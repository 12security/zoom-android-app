package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ShowFirstParty;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import java.util.ArrayList;
import java.util.Map;

@ShowFirstParty
@Class(creator = "FieldMappingDictionaryEntryCreator")
public final class zal extends AbstractSafeParcelable {
    public static final Creator<zal> CREATOR = new zao();
    @Field(mo23591id = 2)
    final String className;
    @VersionField(mo23597id = 1)
    private final int versionCode;
    @Field(mo23591id = 3)
    final ArrayList<zam> zaqx;

    @Constructor
    zal(@Param(mo23594id = 1) int i, @Param(mo23594id = 2) String str, @Param(mo23594id = 3) ArrayList<zam> arrayList) {
        this.versionCode = i;
        this.className = str;
        this.zaqx = arrayList;
    }

    zal(String str, Map<String, FastJsonResponse.Field<?, ?>> map) {
        ArrayList<zam> arrayList;
        this.versionCode = 1;
        this.className = str;
        if (map == null) {
            arrayList = null;
        } else {
            arrayList = new ArrayList<>();
            for (String str2 : map.keySet()) {
                arrayList.add(new zam(str2, (FastJsonResponse.Field) map.get(str2)));
            }
        }
        this.zaqx = arrayList;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, this.className, false);
        SafeParcelWriter.writeTypedList(parcel, 3, this.zaqx, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
