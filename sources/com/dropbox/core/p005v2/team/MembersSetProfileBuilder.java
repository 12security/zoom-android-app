package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.MembersSetProfileArg.Builder;

/* renamed from: com.dropbox.core.v2.team.MembersSetProfileBuilder */
public class MembersSetProfileBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    MembersSetProfileBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public MembersSetProfileBuilder withNewEmail(String str) {
        this._builder.withNewEmail(str);
        return this;
    }

    public MembersSetProfileBuilder withNewExternalId(String str) {
        this._builder.withNewExternalId(str);
        return this;
    }

    public MembersSetProfileBuilder withNewGivenName(String str) {
        this._builder.withNewGivenName(str);
        return this;
    }

    public MembersSetProfileBuilder withNewSurname(String str) {
        this._builder.withNewSurname(str);
        return this;
    }

    public MembersSetProfileBuilder withNewPersistentId(String str) {
        this._builder.withNewPersistentId(str);
        return this;
    }

    public MembersSetProfileBuilder withNewIsDirectoryRestricted(Boolean bool) {
        this._builder.withNewIsDirectoryRestricted(bool);
        return this;
    }

    public TeamMemberInfo start() throws MembersSetProfileErrorException, DbxException {
        return this._client.membersSetProfile(this._builder.build());
    }
}
