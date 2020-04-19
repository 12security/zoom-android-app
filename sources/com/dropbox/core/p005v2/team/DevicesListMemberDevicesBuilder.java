package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.ListMemberDevicesArg.Builder;

/* renamed from: com.dropbox.core.v2.team.DevicesListMemberDevicesBuilder */
public class DevicesListMemberDevicesBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    DevicesListMemberDevicesBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public DevicesListMemberDevicesBuilder withIncludeWebSessions(Boolean bool) {
        this._builder.withIncludeWebSessions(bool);
        return this;
    }

    public DevicesListMemberDevicesBuilder withIncludeDesktopClients(Boolean bool) {
        this._builder.withIncludeDesktopClients(bool);
        return this;
    }

    public DevicesListMemberDevicesBuilder withIncludeMobileClients(Boolean bool) {
        this._builder.withIncludeMobileClients(bool);
        return this;
    }

    public ListMemberDevicesResult start() throws ListMemberDevicesErrorException, DbxException {
        return this._client.devicesListMemberDevices(this._builder.build());
    }
}
