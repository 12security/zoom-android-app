package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.filerequests.CreateFileRequestArgs.Builder;

/* renamed from: com.dropbox.core.v2.filerequests.CreateBuilder */
public class CreateBuilder {
    private final Builder _builder;
    private final DbxUserFileRequestsRequests _client;

    CreateBuilder(DbxUserFileRequestsRequests dbxUserFileRequestsRequests, Builder builder) {
        if (dbxUserFileRequestsRequests != null) {
            this._client = dbxUserFileRequestsRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public CreateBuilder withDeadline(FileRequestDeadline fileRequestDeadline) {
        this._builder.withDeadline(fileRequestDeadline);
        return this;
    }

    public CreateBuilder withOpen(Boolean bool) {
        this._builder.withOpen(bool);
        return this;
    }

    public FileRequest start() throws CreateFileRequestErrorException, DbxException {
        return this._client.create(this._builder.build());
    }
}
