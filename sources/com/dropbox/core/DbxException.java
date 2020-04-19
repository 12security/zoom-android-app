package com.dropbox.core;

public class DbxException extends Exception {
    private static final long serialVersionUID = 0;
    private final String requestId;

    public DbxException(String str) {
        this((String) null, str);
    }

    public DbxException(String str, String str2) {
        super(str2);
        this.requestId = str;
    }

    public DbxException(String str, Throwable th) {
        this(null, str, th);
    }

    public DbxException(String str, String str2, Throwable th) {
        super(str2, th);
        this.requestId = str;
    }

    public String getRequestId() {
        return this.requestId;
    }
}
