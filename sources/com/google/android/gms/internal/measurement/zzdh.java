package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzdg;
import com.google.android.gms.internal.measurement.zzdh;

public abstract class zzdh<MessageType extends zzdg<MessageType, BuilderType>, BuilderType extends zzdh<MessageType, BuilderType>> implements zzgi {
    /* access modifiers changed from: protected */
    public abstract BuilderType zza(MessageType messagetype);

    /* renamed from: zzjx */
    public abstract BuilderType clone();

    public final /* synthetic */ zzgi zza(zzgh zzgh) {
        if (zzmm().getClass().isInstance(zzgh)) {
            return zza((MessageType) (zzdg) zzgh);
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }
}
