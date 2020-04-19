package com.dropbox.core;

@Deprecated
public class DbxWebAuthNoRedirect {
    private final DbxWebAuth auth;

    public DbxWebAuthNoRedirect(DbxRequestConfig dbxRequestConfig, DbxAppInfo dbxAppInfo) {
        this.auth = new DbxWebAuth(dbxRequestConfig, dbxAppInfo);
    }

    public String start() {
        return this.auth.authorize(DbxWebAuth.newRequestBuilder().withNoRedirect().build());
    }

    public DbxAuthFinish finish(String str) throws DbxException {
        return this.auth.finishFromCode(str);
    }
}
