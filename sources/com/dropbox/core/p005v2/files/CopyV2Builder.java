package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.files.RelocationArg.Builder;

/* renamed from: com.dropbox.core.v2.files.CopyV2Builder */
public class CopyV2Builder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    CopyV2Builder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public CopyV2Builder withAllowSharedFolder(Boolean bool) {
        this._builder.withAllowSharedFolder(bool);
        return this;
    }

    public CopyV2Builder withAutorename(Boolean bool) {
        this._builder.withAutorename(bool);
        return this;
    }

    public CopyV2Builder withAllowOwnershipTransfer(Boolean bool) {
        this._builder.withAllowOwnershipTransfer(bool);
        return this;
    }

    public RelocationResult start() throws RelocationErrorException, DbxException {
        return this._client.copyV2(this._builder.build());
    }
}
