package com.dropbox.core;

public class BadResponseCodeException extends BadResponseException {
    private static final long serialVersionUID = 0;
    private final int statusCode;

    public BadResponseCodeException(String str, String str2, int i) {
        super(str, str2);
        this.statusCode = i;
    }

    public BadResponseCodeException(String str, String str2, int i, Throwable th) {
        super(str, str2, th);
        this.statusCode = i;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
