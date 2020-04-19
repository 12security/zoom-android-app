package com.microsoft.aad.adal;

class ServerRespondingWithRetryableException extends AuthenticationException {
    static final long serialVersionUID = 1;

    ServerRespondingWithRetryableException(String str) {
        super(null, str);
    }

    ServerRespondingWithRetryableException(String str, Throwable th) {
        super((ADALError) null, str, th);
    }

    ServerRespondingWithRetryableException(String str, HttpWebResponse httpWebResponse) {
        super((ADALError) null, str, httpWebResponse);
    }
}
