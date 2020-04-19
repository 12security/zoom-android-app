package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.files.RelocationArg.Builder;

/* renamed from: com.dropbox.core.v2.files.MoveV2Builder */
public class MoveV2Builder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    MoveV2Builder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public MoveV2Builder withAllowSharedFolder(Boolean bool) {
        this._builder.withAllowSharedFolder(bool);
        return this;
    }

    public MoveV2Builder withAutorename(Boolean bool) {
        this._builder.withAutorename(bool);
        return this;
    }

    public MoveV2Builder withAllowOwnershipTransfer(Boolean bool) {
        this._builder.withAllowOwnershipTransfer(bool);
        return this;
    }

    public RelocationResult start() throws RelocationErrorException, DbxException {
        return this._client.moveV2(this._builder.build());
    }
}
