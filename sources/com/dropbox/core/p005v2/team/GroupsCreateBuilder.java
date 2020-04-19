package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.GroupCreateArg.Builder;
import com.dropbox.core.p005v2.teamcommon.GroupManagementType;

/* renamed from: com.dropbox.core.v2.team.GroupsCreateBuilder */
public class GroupsCreateBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    GroupsCreateBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public GroupsCreateBuilder withGroupExternalId(String str) {
        this._builder.withGroupExternalId(str);
        return this;
    }

    public GroupsCreateBuilder withGroupManagementType(GroupManagementType groupManagementType) {
        this._builder.withGroupManagementType(groupManagementType);
        return this;
    }

    public GroupFullInfo start() throws GroupCreateErrorException, DbxException {
        return this._client.groupsCreate(this._builder.build());
    }
}
