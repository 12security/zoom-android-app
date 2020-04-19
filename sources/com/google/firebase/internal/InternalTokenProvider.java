package com.google.firebase.internal;

import androidx.annotation.Nullable;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;

@KeepForSdk
@Deprecated
/* compiled from: com.google.firebase:firebase-common@@16.1.0 */
public interface InternalTokenProvider {
    @KeepForSdk
    Task<GetTokenResult> getAccessToken(boolean z);

    @KeepForSdk
    @Nullable
    String getUid();
}
