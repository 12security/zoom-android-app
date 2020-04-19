package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.ListFolderMembersArgs.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersBuilder */
public class ListFolderMembersBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    ListFolderMembersBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public ListFolderMembersBuilder withActions(List<MemberAction> list) {
        this._builder.withActions(list);
        return this;
    }

    public ListFolderMembersBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public SharedFolderMembers start() throws SharedFolderAccessErrorException, DbxException {
        return this._client.listFolderMembers(this._builder.build());
    }
}
