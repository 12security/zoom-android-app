package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.filerequests.UpdateFileRequestArgs.Builder;

/* renamed from: com.dropbox.core.v2.filerequests.UpdateBuilder */
public class UpdateBuilder {
    private final Builder _builder;
    private final DbxUserFileRequestsRequests _client;

    UpdateBuilder(DbxUserFileRequestsRequests dbxUserFileRequestsRequests, Builder builder) {
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

    public UpdateBuilder withTitle(String str) {
        this._builder.withTitle(str);
        return this;
    }

    public UpdateBuilder withDestination(String str) {
        this._builder.withDestination(str);
        return this;
    }

    public UpdateBuilder withDeadline(UpdateFileRequestDeadline updateFileRequestDeadline) {
        this._builder.withDeadline(updateFileRequestDeadline);
        return this;
    }

    public UpdateBuilder withOpen(Boolean bool) {
        this._builder.withOpen(bool);
        return this;
    }

    public FileRequest start() throws UpdateFileRequestErrorException, DbxException {
        return this._client.update(this._builder.build());
    }
}
