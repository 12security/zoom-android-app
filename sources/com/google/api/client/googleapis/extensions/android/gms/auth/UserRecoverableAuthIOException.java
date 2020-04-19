package com.google.api.client.googleapis.extensions.android.gms.auth;

import android.content.Intent;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.util.Beta;

@Beta
public class UserRecoverableAuthIOException extends GoogleAuthIOException {
    private static final long serialVersionUID = 1;

    public UserRecoverableAuthIOException(UserRecoverableAuthException userRecoverableAuthException) {
        super(userRecoverableAuthException);
    }

    public UserRecoverableAuthException getCause() {
        return super.getCause();
    }

    public final Intent getIntent() {
        return getCause().getIntent();
    }
}
