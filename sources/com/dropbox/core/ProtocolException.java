package com.dropbox.core;

public abstract class ProtocolException extends DbxException {
    private static final long serialVersionUID = 0;

    public ProtocolException(String str, String str2) {
        super(str, str2);
    }

    public ProtocolException(String str, String str2, Throwable th) {
        super(str, str2, th);
    }
}
