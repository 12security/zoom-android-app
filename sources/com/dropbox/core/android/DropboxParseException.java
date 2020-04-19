package com.dropbox.core.android;

import com.dropbox.core.DbxException;

public class DropboxParseException extends DbxException {
    private static final long serialVersionUID = 1;

    public DropboxParseException(String str) {
        super(str);
    }
}
