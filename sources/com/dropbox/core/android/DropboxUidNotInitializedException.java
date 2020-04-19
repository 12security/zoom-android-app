package com.dropbox.core.android;

import com.dropbox.core.DbxException;

public class DropboxUidNotInitializedException extends DbxException {
    private static final long serialVersionUID = 1;

    public DropboxUidNotInitializedException(String str) {
        super(str);
    }
}
