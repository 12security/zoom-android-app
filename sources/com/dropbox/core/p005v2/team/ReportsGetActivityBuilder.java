package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.DateRange.Builder;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.team.ReportsGetActivityBuilder */
public class ReportsGetActivityBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    ReportsGetActivityBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public ReportsGetActivityBuilder withStartDate(Date date) {
        this._builder.withStartDate(date);
        return this;
    }

    public ReportsGetActivityBuilder withEndDate(Date date) {
        this._builder.withEndDate(date);
        return this;
    }

    public GetActivityReport start() throws DateRangeErrorException, DbxException {
        return this._client.reportsGetActivity(this._builder.build());
    }
}
