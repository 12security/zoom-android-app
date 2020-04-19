package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.p005v2.DbxRawClientV2;

/* renamed from: com.dropbox.core.v2.teamlog.DbxTeamTeamLogRequests */
public class DbxTeamTeamLogRequests {
    private final DbxRawClientV2 client;

    public DbxTeamTeamLogRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public GetTeamEventsResult getEvents(GetTeamEventsArg getTeamEventsArg) throws GetTeamEventsErrorException, DbxException {
        try {
            return (GetTeamEventsResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team_log/get_events", getTeamEventsArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetTeamEventsErrorException("2/team_log/get_events", e.getRequestId(), e.getUserMessage(), (GetTeamEventsError) e.getErrorValue());
        }
    }

    public GetTeamEventsResult getEvents() throws GetTeamEventsErrorException, DbxException {
        return getEvents(new GetTeamEventsArg());
    }

    public GetEventsBuilder getEventsBuilder() {
        return new GetEventsBuilder(this, GetTeamEventsArg.newBuilder());
    }

    /* access modifiers changed from: 0000 */
    public GetTeamEventsResult getEventsContinue(GetTeamEventsContinueArg getTeamEventsContinueArg) throws GetTeamEventsContinueErrorException, DbxException {
        try {
            return (GetTeamEventsResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/team_log/get_events/continue", getTeamEventsContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetTeamEventsContinueErrorException("2/team_log/get_events/continue", e.getRequestId(), e.getUserMessage(), (GetTeamEventsContinueError) e.getErrorValue());
        }
    }

    public GetTeamEventsResult getEventsContinue(String str) throws GetTeamEventsContinueErrorException, DbxException {
        return getEventsContinue(new GetTeamEventsContinueArg(str));
    }
}
