package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.files.RelocationBatchArg.Builder;

/* renamed from: com.dropbox.core.v2.files.MoveBatchBuilder */
public class MoveBatchBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    MoveBatchBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public MoveBatchBuilder withAllowSharedFolder(Boolean bool) {
        this._builder.withAllowSharedFolder(bool);
        return this;
    }

    public MoveBatchBuilder withAutorename(Boolean bool) {
        this._builder.withAutorename(bool);
        return this;
    }

    public MoveBatchBuilder withAllowOwnershipTransfer(Boolean bool) {
        this._builder.withAllowOwnershipTransfer(bool);
        return this;
    }

    public RelocationBatchLaunch start() throws DbxApiException, DbxException {
        return this._client.moveBatch(this._builder.build());
    }
}
