package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.async.LaunchEmptyResult;
import com.dropbox.core.p005v2.team.MembersRemoveArg.Builder;

/* renamed from: com.dropbox.core.v2.team.MembersRemoveBuilder */
public class MembersRemoveBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    MembersRemoveBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public MembersRemoveBuilder withWipeData(Boolean bool) {
        this._builder.withWipeData(bool);
        return this;
    }

    public MembersRemoveBuilder withTransferDestId(UserSelectorArg userSelectorArg) {
        this._builder.withTransferDestId(userSelectorArg);
        return this;
    }

    public MembersRemoveBuilder withTransferAdminId(UserSelectorArg userSelectorArg) {
        this._builder.withTransferAdminId(userSelectorArg);
        return this;
    }

    public MembersRemoveBuilder withKeepAccount(Boolean bool) {
        this._builder.withKeepAccount(bool);
        return this;
    }

    public LaunchEmptyResult start() throws MembersRemoveErrorException, DbxException {
        return this._client.membersRemove(this._builder.build());
    }
}
