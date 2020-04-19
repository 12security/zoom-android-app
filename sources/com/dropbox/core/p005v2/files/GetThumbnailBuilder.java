package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.DbxDownloadStyleBuilder;
import com.dropbox.core.p005v2.files.ThumbnailArg.Builder;

/* renamed from: com.dropbox.core.v2.files.GetThumbnailBuilder */
public class GetThumbnailBuilder extends DbxDownloadStyleBuilder<FileMetadata> {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    GetThumbnailBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
        if (dbxUserFilesRequests != null) {
            this._client = dbxUserFilesRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public GetThumbnailBuilder withFormat(ThumbnailFormat thumbnailFormat) {
        this._builder.withFormat(thumbnailFormat);
        return this;
    }

    public GetThumbnailBuilder withSize(ThumbnailSize thumbnailSize) {
        this._builder.withSize(thumbnailSize);
        return this;
    }

    public GetThumbnailBuilder withMode(ThumbnailMode thumbnailMode) {
        this._builder.withMode(thumbnailMode);
        return this;
    }

    public DbxDownloader<FileMetadata> start() throws ThumbnailErrorException, DbxException {
        return this._client.getThumbnail(this._builder.build(), getHeaders());
    }
}
