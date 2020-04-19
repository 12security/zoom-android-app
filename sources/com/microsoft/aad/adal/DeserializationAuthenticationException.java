package com.microsoft.aad.adal;

class DeserializationAuthenticationException extends AuthenticationException {
    static final long serialVersionUID = 1;

    DeserializationAuthenticationException(String str) {
        super(ADALError.INCOMPATIBLE_BLOB_VERSION, str);
    }
}
