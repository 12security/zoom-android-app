package com.dropbox.core;

public class BadRequestException extends ProtocolException {
    private static final long serialVersionUID = 0;

    public BadRequestException(String str, String str2) {
        super(str, str2);
    }
}
