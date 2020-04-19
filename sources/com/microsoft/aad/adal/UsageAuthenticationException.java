package com.microsoft.aad.adal;

public class UsageAuthenticationException extends AuthenticationException {
    static final long serialVersionUID = 1;

    public UsageAuthenticationException(ADALError aDALError, String str) {
        super(aDALError, str);
    }

    public UsageAuthenticationException(ADALError aDALError, String str, Throwable th) {
        super(aDALError, str, th);
    }
}
