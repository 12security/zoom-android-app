package com.dropbox.core.p005v2.callbacks;

import com.dropbox.core.DbxException;

/* renamed from: com.dropbox.core.v2.callbacks.DbxNetworkErrorCallback */
public abstract class DbxNetworkErrorCallback {
    public abstract void onNetworkError(DbxException dbxException);
}
