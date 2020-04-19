package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.ListTeamDevicesArg.Builder;

/* renamed from: com.dropbox.core.v2.team.DevicesListTeamDevicesBuilder */
public class DevicesListTeamDevicesBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    DevicesListTeamDevicesBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public DevicesListTeamDevicesBuilder withCursor(String str) {
        this._builder.withCursor(str);
        return this;
    }

    public DevicesListTeamDevicesBuilder withIncludeWebSessions(Boolean bool) {
        this._builder.withIncludeWebSessions(bool);
        return this;
    }

    public DevicesListTeamDevicesBuilder withIncludeDesktopClients(Boolean bool) {
        this._builder.withIncludeDesktopClients(bool);
        return this;
    }

    public DevicesListTeamDevicesBuilder withIncludeMobileClients(Boolean bool) {
        this._builder.withIncludeMobileClients(bool);
        return this;
    }

    public ListTeamDevicesResult start() throws ListTeamDevicesErrorException, DbxException {
        return this._client.devicesListTeamDevices(this._builder.build());
    }
}
