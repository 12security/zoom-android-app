package com.google.firebase.components;

import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
/* compiled from: com.google.firebase:firebase-common@@16.1.0 */
public interface ComponentFactory<T> {
    @KeepForSdk
    T create(ComponentContainer componentContainer);
}
