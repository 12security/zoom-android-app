package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.ListSharedLinksArg.Builder;

/* renamed from: com.dropbox.core.v2.sharing.ListSharedLinksBuilder */
public class ListSharedLinksBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    ListSharedLinksBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
        if (dbxUserSharingRequests != null) {
            this._client = dbxUserSharingRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public ListSharedLinksBuilder withPath(String str) {
        this._builder.withPath(str);
        return this;
    }

    public ListSharedLinksBuilder withCursor(String str) {
        this._builder.withCursor(str);
        return this;
    }

    public ListSharedLinksBuilder withDirectOnly(Boolean bool) {
        this._builder.withDirectOnly(bool);
        return this;
    }

    public ListSharedLinksResult start() throws ListSharedLinksErrorException, DbxException {
        return this._client.listSharedLinks(this._builder.build());
    }
}
