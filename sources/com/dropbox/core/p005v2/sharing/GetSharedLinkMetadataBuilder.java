package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.GetSharedLinkMetadataArg.Builder;

/* renamed from: com.dropbox.core.v2.sharing.GetSharedLinkMetadataBuilder */
public class GetSharedLinkMetadataBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    GetSharedLinkMetadataBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public GetSharedLinkMetadataBuilder withPath(String str) {
        this._builder.withPath(str);
        return this;
    }

    public GetSharedLinkMetadataBuilder withLinkPassword(String str) {
        this._builder.withLinkPassword(str);
        return this;
    }

    public SharedLinkMetadata start() throws SharedLinkErrorException, DbxException {
        return this._client.getSharedLinkMetadata(this._builder.build());
    }
}
