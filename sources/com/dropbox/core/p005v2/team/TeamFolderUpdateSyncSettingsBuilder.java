package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.files.ContentSyncSettingArg;
import com.dropbox.core.p005v2.files.SyncSettingArg;
import com.dropbox.core.p005v2.team.TeamFolderUpdateSyncSettingsArg.Builder;
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsBuilder */
public class TeamFolderUpdateSyncSettingsBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    TeamFolderUpdateSyncSettingsBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public TeamFolderUpdateSyncSettingsBuilder withSyncSetting(SyncSettingArg syncSettingArg) {
        this._builder.withSyncSetting(syncSettingArg);
        return this;
    }

    public TeamFolderUpdateSyncSettingsBuilder withContentSyncSettings(List<ContentSyncSettingArg> list) {
        this._builder.withContentSyncSettings(list);
        return this;
    }

    public TeamFolderMetadata start() throws TeamFolderUpdateSyncSettingsErrorException, DbxException {
        return this._client.teamFolderUpdateSyncSettings(this._builder.build());
    }
}
