package com.dropbox.core;

public class BadResponseException extends ProtocolException {
    private static final long serialVersionUID = 0;

    public BadResponseException(String str, String str2) {
        super(str, str2);
    }

    public BadResponseException(String str, String str2, Throwable th) {
        super(str, str2, th);
    }
}
