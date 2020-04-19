package com.dropbox.core;

public class InvalidAccessTokenException extends DbxException {
    private static final long serialVersionUID = 0;

    public InvalidAccessTokenException(String str, String str2) {
        super(str, str2);
    }
}
