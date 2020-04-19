package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.MembersListArg.Builder;

/* renamed from: com.dropbox.core.v2.team.MembersListBuilder */
public class MembersListBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    MembersListBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
        if (dbxTeamTeamRequests != null) {
            this._client = dbxTeamTeamRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public MembersListBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public MembersListBuilder withIncludeRemoved(Boolean bool) {
        this._builder.withIncludeRemoved(bool);
        return this;
    }

    public MembersListResult start() throws MembersListErrorException, DbxException {
        return this._client.membersList(this._builder.build());
    }
}
