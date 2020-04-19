package com.dropbox.core.p005v2.team;

import com.dropbox.core.DbxException;
import com.dropbox.core.p005v2.team.DateRange.Builder;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.team.ReportsGetStorageBuilder */
public class ReportsGetStorageBuilder {
    private final Builder _builder;
    private final DbxTeamTeamRequests _client;

    ReportsGetStorageBuilder(DbxTeamTeamRequests dbxTeamTeamRequests, Builder builder) {
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

    public ReportsGetStorageBuilder withStartDate(Date date) {
        this._builder.withStartDate(date);
        return this;
    }

    public ReportsGetStorageBuilder withEndDate(Date date) {
        this._builder.withEndDate(date);
        return this;
    }

    public GetStorageReport start() throws DateRangeErrorException, DbxException {
        return this._client.reportsGetStorage(this._builder.build());
    }
}
