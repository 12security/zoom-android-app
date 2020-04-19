package com.google.firebase.auth;

import androidx.annotation.NonNull;
import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.FirebaseException;
import com.google.firebase.annotations.PublicApi;

@PublicApi
/* compiled from: com.google.firebase:firebase-common@@16.1.0 */
public class FirebaseAuthException extends FirebaseException {
    private final String errorCode;

    @PublicApi
    public FirebaseAuthException(@NonNull String str, @NonNull String str2) {
        super(str2);
        this.errorCode = Preconditions.checkNotEmpty(str);
    }

    @PublicApi
    @NonNull
    public String getErrorCode() {
        return this.errorCode;
    }
}
