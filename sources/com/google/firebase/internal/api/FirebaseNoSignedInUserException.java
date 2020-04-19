package com.google.firebase.internal.api;

import androidx.annotation.NonNull;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.firebase.FirebaseException;

@KeepForSdk
/* compiled from: com.google.firebase:firebase-common@@16.1.0 */
public class FirebaseNoSignedInUserException extends FirebaseException {
    @KeepForSdk
    public FirebaseNoSignedInUserException(@NonNull String str) {
        super(str);
    }
}
