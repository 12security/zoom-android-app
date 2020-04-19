package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.paper.ListUsersOnPaperDocArgs.Builder;

/* renamed from: com.dropbox.core.v2.paper.DocsUsersListBuilder */
public class DocsUsersListBuilder {
    private final Builder _builder;
    private final DbxUserPaperRequests _client;

    DocsUsersListBuilder(DbxUserPaperRequests dbxUserPaperRequests, Builder builder) {
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

    public DocsUsersListBuilder withLimit(Integer num) {
        this._builder.withLimit(num);
        return this;
    }

    public DocsUsersListBuilder withFilterBy(UserOnPaperDocFilter userOnPaperDocFilter) {
        this._builder.withFilterBy(userOnPaperDocFilter);
        return this;
    }

    public ListUsersOnPaperDocResponse start() throws DocLookupErrorException, DbxException {
        return this._client.docsUsersList(this._builder.build());
    }
}
