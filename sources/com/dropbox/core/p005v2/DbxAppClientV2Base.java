package com.dropbox.core.p005v2;

import com.dropbox.core.p005v2.auth.DbxAppAuthRequests;

/* renamed from: com.dropbox.core.v2.DbxAppClientV2Base */
public class DbxAppClientV2Base {
    protected final DbxRawClientV2 _client;
    private final DbxAppAuthRequests auth;

    protected DbxAppClientV2Base(DbxRawClientV2 dbxRawClientV2) {
        this._client = dbxRawClientV2;
        this.auth = new DbxAppAuthRequests(dbxRawClientV2);
    }

    public DbxAppAuthRequests auth() {
        return this.auth;
    }
}
