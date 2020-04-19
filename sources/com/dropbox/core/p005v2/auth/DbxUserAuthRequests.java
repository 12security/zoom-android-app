package com.dropbox.core.p005v2.auth;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.LocalizedText;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.stone.StoneSerializers;

/* renamed from: com.dropbox.core.v2.auth.DbxUserAuthRequests */
public class DbxUserAuthRequests {
    private final DbxRawClientV2 client;

    public DbxUserAuthRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    public void tokenRevoke() throws DbxApiException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/auth/token/revoke", null, false, StoneSerializers.void_(), StoneSerializers.void_(), StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"token/revoke\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }
}
