package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.teamcommon.TimeRange;
import com.dropbox.core.p005v2.teamlog.GetTeamEventsArg.Builder;

/* renamed from: com.dropbox.core.v2.teamlog.GetEventsBuilder */
public class GetEventsBuilder {
    private final Builder _builder;
    private final DbxTeamTeamLogRequests _client;

    GetEventsBuilder(DbxTeamTeamLogRequests dbxTeamTeamLogRequests, Builder builder) {
        if (dbxTeamTeamLogRequests != null) {
            this._client = dbxTeamTeamLogRequests;
            if (builder != null) {
                this._builder = builder;
                return;
            }
            throw new NullPointerException("_builder");
        }
        throw new NullPointerException("_client");
    }

    public GetEventsBuilder withLimit(Long l) {
        this._builder.withLimit(l);
        return this;
    }

    public GetEventsBuilder withAccountId(String str) {
        this._builder.withAccountId(str);
        return this;
    }

    public GetEventsBuilder withTime(TimeRange timeRange) {
        this._builder.withTime(timeRange);
        return this;
    }

    public GetEventsBuilder withCategory(EventCategory eventCategory) {
        this._builder.withCategory(eventCategory);
        return this;
    }

    public GetTeamEventsResult start() throws GetTeamEventsErrorException, DbxException {
        return this._client.getEvents(this._builder.build());
    }
}
