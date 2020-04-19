package com.dropbox.core.p005v2;

import com.dropbox.core.p005v2.fileproperties.DbxTeamFilePropertiesRequests;
import com.dropbox.core.p005v2.team.DbxTeamTeamRequests;
import com.dropbox.core.p005v2.teamlog.DbxTeamTeamLogRequests;

/* renamed from: com.dropbox.core.v2.DbxTeamClientV2Base */
public class DbxTeamClientV2Base {
    protected final DbxRawClientV2 _client;
    private final DbxTeamFilePropertiesRequests fileProperties;
    private final DbxTeamTeamRequests team;
    private final DbxTeamTeamLogRequests teamLog;

    protected DbxTeamClientV2Base(DbxRawClientV2 dbxRawClientV2) {
        this._client = dbxRawClientV2;
        this.fileProperties = new DbxTeamFilePropertiesRequests(dbxRawClientV2);
        this.team = new DbxTeamTeamRequests(dbxRawClientV2);
        this.teamLog = new DbxTeamTeamLogRequests(dbxRawClientV2);
    }

    public DbxTeamFilePropertiesRequests fileProperties() {
        return this.fileProperties;
    }

    public DbxTeamTeamRequests team() {
        return this.team;
    }

    public DbxTeamTeamLogRequests teamLog() {
        return this.teamLog;
    }
}
