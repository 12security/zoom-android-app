package com.google.android.gms.common.api.internal;

import androidx.annotation.NonNull;
import com.google.android.gms.common.api.Api.AnyClient;

public final class zabw {
    public final RegisterListenerMethod<AnyClient, ?> zajw;
    public final UnregisterListenerMethod<AnyClient, ?> zajx;

    public zabw(@NonNull RegisterListenerMethod<AnyClient, ?> registerListenerMethod, @NonNull UnregisterListenerMethod<AnyClient, ?> unregisterListenerMethod) {
        this.zajw = registerListenerMethod;
        this.zajx = unregisterListenerMethod;
    }
}
