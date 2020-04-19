package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.files.ListRevisionsArg.Builder;

/* renamed from: com.dropbox.core.v2.files.ListRevisionsBuilder */
public class ListRevisionsBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    ListRevisionsBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public ListRevisionsBuilder withMode(ListRevisionsMode listRevisionsMode) {
        this._builder.withMode(listRevisionsMode);
        return this;
    }

    public ListRevisionsBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public ListRevisionsResult start() throws ListRevisionsErrorException, DbxException {
        return this._client.listRevisions(this._builder.build());
    }
}
