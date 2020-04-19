package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.CreateSharedLinkArg.Builder;

/* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkBuilder */
public class CreateSharedLinkBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    CreateSharedLinkBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public CreateSharedLinkBuilder withShortUrl(Boolean bool) {
        this._builder.withShortUrl(bool);
        return this;
    }

    public CreateSharedLinkBuilder withPendingUpload(PendingUploadMode pendingUploadMode) {
        this._builder.withPendingUpload(pendingUploadMode);
        return this;
    }

    public PathLinkMetadata start() throws CreateSharedLinkErrorException, DbxException {
        return this._client.createSharedLink(this._builder.build());
    }
}
