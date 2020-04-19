package com.dropbox.core.p005v2.auth;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.p005v2.DbxRawClientV2;

/* renamed from: com.dropbox.core.v2.auth.DbxAppAuthRequests */
public class DbxAppAuthRequests {
    private final DbxRawClientV2 client;

    public DbxAppAuthRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public TokenFromOAuth1Result tokenFromOauth1(TokenFromOAuth1Arg tokenFromOAuth1Arg) throws TokenFromOAuth1ErrorException, DbxException {
        try {
            return (TokenFromOAuth1Result) this.client.rpcStyle(this.client.getHost().getApi(), "2/auth/token/from_oauth1", tokenFromOAuth1Arg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TokenFromOAuth1ErrorException("2/auth/token/from_oauth1", e.getRequestId(), e.getUserMessage(), (TokenFromOAuth1Error) e.getErrorValue());
        }
    }

    public TokenFromOAuth1Result tokenFromOauth1(String str, String str2) throws TokenFromOAuth1ErrorException, DbxException {
        return tokenFromOauth1(new TokenFromOAuth1Arg(str, str2));
    }
}
