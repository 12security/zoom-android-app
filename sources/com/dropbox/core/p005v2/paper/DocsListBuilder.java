package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.paper.ListPaperDocsArgs.Builder;

/* renamed from: com.dropbox.core.v2.paper.DocsListBuilder */
public class DocsListBuilder {
    private final Builder _builder;
    private final DbxUserPaperRequests _client;

    DocsListBuilder(DbxUserPaperRequests dbxUserPaperRequests, Builder builder) {
        if (dbxUserPaperRequests != null) {
            this._client = dbxUserPaperRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public DocsListBuilder withFilterBy(ListPaperDocsFilterBy listPaperDocsFilterBy) {
        this._builder.withFilterBy(listPaperDocsFilterBy);
        return this;
    }

    public DocsListBuilder withSortBy(ListPaperDocsSortBy listPaperDocsSortBy) {
        this._builder.withSortBy(listPaperDocsSortBy);
        return this;
    }

    public DocsListBuilder withSortOrder(ListPaperDocsSortOrder listPaperDocsSortOrder) {
        this._builder.withSortOrder(listPaperDocsSortOrder);
        return this;
    }

    public DocsListBuilder withLimit(Integer num) {
        this._builder.withLimit(num);
        return this;
    }

    public ListPaperDocsResponse start() throws DbxApiException, DbxException {
        return this._client.docsList(this._builder.build());
    }
}
