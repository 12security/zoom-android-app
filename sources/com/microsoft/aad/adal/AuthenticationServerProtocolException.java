package com.microsoft.aad.adal;

class AuthenticationServerProtocolException extends AuthenticationException {
    static final long serialVersionUID = 1;

    AuthenticationServerProtocolException(String str) {
        super(ADALError.DEVICE_CHALLENGE_FAILURE, str);
    }
}
