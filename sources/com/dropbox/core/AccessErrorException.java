package com.dropbox.core;

import com.dropbox.core.p005v2.auth.AccessError;

public class AccessErrorException extends DbxException {
    private static final long serialVersionUID = 0;
    private final AccessError accessError;

    public AccessError getAccessError() {
        return this.accessError;
    }

    public AccessErrorException(String str, String str2, AccessError accessError2) {
        super(str, str2);
        this.accessError = accessError2;
    }
}
