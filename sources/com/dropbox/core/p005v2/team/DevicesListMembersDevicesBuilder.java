package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.ListMembersDevicesArg.Builder;

/* renamed from: com.dropbox.core.v2.team.DevicesListMembersDevicesBuilder */
public class DevicesListMembersDevicesBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    DevicesListMembersDevicesBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public DevicesListMembersDevicesBuilder withCursor(String str) {
        this._builder.withCursor(str);
        return this;
    }

    public DevicesListMembersDevicesBuilder withIncludeWebSessions(Boolean bool) {
        this._builder.withIncludeWebSessions(bool);
        return this;
    }

    public DevicesListMembersDevicesBuilder withIncludeDesktopClients(Boolean bool) {
        this._builder.withIncludeDesktopClients(bool);
        return this;
    }

    public DevicesListMembersDevicesBuilder withIncludeMobileClients(Boolean bool) {
        this._builder.withIncludeMobileClients(bool);
        return this;
    }

    public ListMembersDevicesResult start() throws ListMembersDevicesErrorException, DbxException {
        return this._client.devicesListMembersDevices(this._builder.build());
    }
}
