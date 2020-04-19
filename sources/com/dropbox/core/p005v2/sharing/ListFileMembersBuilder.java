package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.sharing.ListFileMembersArg.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersBuilder */
public class ListFileMembersBuilder {
    private final Builder _builder;
    private final DbxUserSharingRequests _client;

    ListFileMembersBuilder(DbxUserSharingRequests dbxUserSharingRequests, Builder builder) {
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

    public ListFileMembersBuilder withActions(List<MemberAction> list) {
        this._builder.withActions(list);
        return this;
    }

    public ListFileMembersBuilder withIncludeInherited(Boolean bool) {
        this._builder.withIncludeInherited(bool);
        return this;
    }

    public ListFileMembersBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public SharedFileMembers start() throws ListFileMembersErrorException, DbxException {
        return this._client.listFileMembers(this._builder.build());
    }
}
