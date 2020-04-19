package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.GroupUpdateArgs.Builder;
import com.dropbox.core.p005v2.teamcommon.GroupManagementType;

/* renamed from: com.dropbox.core.v2.team.GroupsUpdateBuilder */
public class GroupsUpdateBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    GroupsUpdateBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public GroupsUpdateBuilder withReturnMembers(Boolean bool) {
        this._builder.withReturnMembers(bool);
        return this;
    }

    public GroupsUpdateBuilder withNewGroupName(String str) {
        this._builder.withNewGroupName(str);
        return this;
    }

    public GroupsUpdateBuilder withNewGroupExternalId(String str) {
        this._builder.withNewGroupExternalId(str);
        return this;
    }

    public GroupsUpdateBuilder withNewGroupManagementType(GroupManagementType groupManagementType) {
        this._builder.withNewGroupManagementType(groupManagementType);
        return this;
    }

    public GroupFullInfo start() throws GroupUpdateErrorException, DbxException {
        return this._client.groupsUpdate(this._builder.build());
    }
}
