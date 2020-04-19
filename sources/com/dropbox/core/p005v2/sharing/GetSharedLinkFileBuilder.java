package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.DbxDownloadStyleBuilder;
import com.dropbox.core.p005v2.sharing.GetSharedLinkMetadataArg.Builder;

/* renamed from: com.dropbox.core.v2.sharing.GetSharedLinkFileBuilder */
public class GetSharedLinkFileBuilder extends DbxDownloadStyleBuilder<SharedLinkMetadata> {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    GetSharedLinkFileBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public GetSharedLinkFileBuilder withPath(String str) {
        this._builder.withPath(str);
        return this;
    }

    public GetSharedLinkFileBuilder withLinkPassword(String str) {
        this._builder.withLinkPassword(str);
        return this;
    }

    public DbxDownloader<SharedLinkMetadata> start() throws GetSharedLinkFileErrorException, DbxException {
        return this._client.getSharedLinkFile(this._builder.build(), getHeaders());
    }
}
