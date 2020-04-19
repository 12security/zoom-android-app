package com.dropbox.core.p005v2.paper;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.paper.AddPaperDocUser.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.paper.DocsUsersAddBuilder */
public class DocsUsersAddBuilder {
    private final Builder _builder;
    private final DbxUserPaperRequests _client;

    DocsUsersAddBuilder(DbxUserPaperRequests dbxUserPaperRequests, Builder builder) {
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

    public DocsUsersAddBuilder withCustomMessage(String str) {
        this._builder.withCustomMessage(str);
        return this;
    }

    public DocsUsersAddBuilder withQuiet(Boolean bool) {
        this._builder.withQuiet(bool);
        return this;
    }

    public List<AddPaperDocUserMemberResult> start() throws DocLookupErrorException, DbxException {
        return this._client.docsUsersAdd(this._builder.build());
    }
}
