package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.files.RelocationArg.Builder;

/* renamed from: com.dropbox.core.v2.files.CopyBuilder */
public class CopyBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    CopyBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public CopyBuilder withAllowSharedFolder(Boolean bool) {
        this._builder.withAllowSharedFolder(bool);
        return this;
    }

    public CopyBuilder withAutorename(Boolean bool) {
        this._builder.withAutorename(bool);
        return this;
    }

    public CopyBuilder withAllowOwnershipTransfer(Boolean bool) {
        this._builder.withAllowOwnershipTransfer(bool);
        return this;
    }

    public Metadata start() throws RelocationErrorException, DbxException {
        return this._client.copy(this._builder.build());
    }
}
