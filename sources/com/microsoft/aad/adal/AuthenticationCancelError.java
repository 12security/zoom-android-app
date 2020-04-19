package com.microsoft.aad.adal;

public class AuthenticationCancelError extends AuthenticationException {
    static final long serialVersionUID = 1;

    public AuthenticationCancelError() {
    }

    public AuthenticationCancelError(String str) {
        super(ADALError.AUTH_FAILED_CANCELLED, str);
    }
}
