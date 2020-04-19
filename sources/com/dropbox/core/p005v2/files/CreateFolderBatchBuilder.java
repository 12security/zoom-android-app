package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.files.CreateFolderBatchArg.Builder;

/* renamed from: com.dropbox.core.v2.files.CreateFolderBatchBuilder */
public class CreateFolderBatchBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    CreateFolderBatchBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public CreateFolderBatchBuilder withAutorename(Boolean bool) {
        this._builder.withAutorename(bool);
        return this;
    }

    public CreateFolderBatchBuilder withForceAsync(Boolean bool) {
        this._builder.withForceAsync(bool);
        return this;
    }

    public CreateFolderBatchLaunch start() throws DbxApiException, DbxException {
        return this._client.createFolderBatch(this._builder.build());
    }
}
