package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.AddFolderMemberArg.Builder;

/* renamed from: com.dropbox.core.v2.sharing.AddFolderMemberBuilder */
public class AddFolderMemberBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    AddFolderMemberBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
        if (dbxUserSharingRequests != null) {
            this._client = dbxUserSharingRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public AddFolderMemberBuilder withQuiet(Boolean bool) {
        this._builder.withQuiet(bool);
        return this;
    }

    public AddFolderMemberBuilder withCustomMessage(String str) {
        this._builder.withCustomMessage(str);
        return this;
    }

    public void start() throws AddFolderMemberErrorException, DbxException {
        this._client.addFolderMember(this._builder.build());
    }
}
