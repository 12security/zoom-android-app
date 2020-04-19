package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.files.SearchArg.Builder;

/* renamed from: com.dropbox.core.v2.files.SearchBuilder */
public class SearchBuilder {
    private final Builder _builder;
    private final DbxUserFilesRequests _client;

    SearchBuilder(DbxUserFilesRequests dbxUserFilesRequests, Builder builder) {
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

    public SearchBuilder withStart(Long l) {
        this._builder.withStart(l);
        return this;
    }

    public SearchBuilder withMaxResults(Long l) {
        this._builder.withMaxResults(l);
        return this;
    }

    public SearchBuilder withMode(SearchMode searchMode) {
        this._builder.withMode(searchMode);
        return this;
    }

    public SearchResult start() throws SearchErrorException, DbxException {
        return this._client.search(this._builder.build());
    }
}
