package com.dropbox.core.p005v2.team;

import com.dropbox.core.p005v2.files.SyncSettingsError;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsError */
public final class TeamFolderUpdateSyncSettingsError {
    public static final TeamFolderUpdateSyncSettingsError OTHER = new TeamFolderUpdateSyncSettingsError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public TeamFolderAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public TeamFolderInvalidStatusError statusErrorValue;
    /* access modifiers changed from: private */
    public SyncSettingsError syncSettingsErrorValue;
    /* access modifiers changed from: private */
    public TeamFolderTeamSharedDropboxError teamSharedDropboxErrorValue;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsError$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderUpdateSyncSettingsError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderUpdateSyncSettingsError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(teamFolderUpdateSyncSettingsError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case STATUS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("status_error", jsonGenerator);
                    jsonGenerator.writeFieldName("status_error");
                    Serializer.INSTANCE.serialize(teamFolderUpdateSyncSettingsError.statusErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TEAM_SHARED_DROPBOX_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("team_shared_dropbox_error", jsonGenerator);
                    jsonGenerator.writeFieldName("team_shared_dropbox_error");
                    Serializer.INSTANCE.serialize(teamFolderUpdateSyncSettingsError.teamSharedDropboxErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SYNC_SETTINGS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("sync_settings_error", jsonGenerator);
                    jsonGenerator.writeFieldName("sync_settings_error");
                    com.dropbox.core.p005v2.files.SyncSettingsError.Serializer.INSTANCE.serialize(teamFolderUpdateSyncSettingsError.syncSettingsErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamFolderUpdateSyncSettingsError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamFolderUpdateSyncSettingsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    teamFolderUpdateSyncSettingsError = TeamFolderUpdateSyncSettingsError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("status_error".equals(str)) {
                    expectField("status_error", jsonParser);
                    teamFolderUpdateSyncSettingsError = TeamFolderUpdateSyncSettingsError.statusError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("team_shared_dropbox_error".equals(str)) {
                    expectField("team_shared_dropbox_error", jsonParser);
                    teamFolderUpdateSyncSettingsError = TeamFolderUpdateSyncSettingsError.teamSharedDropboxError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("other".equals(str)) {
                    teamFolderUpdateSyncSettingsError = TeamFolderUpdateSyncSettingsError.OTHER;
                } else if ("sync_settings_error".equals(str)) {
                    expectField("sync_settings_error", jsonParser);
                    teamFolderUpdateSyncSettingsError = TeamFolderUpdateSyncSettingsError.syncSettingsError(com.dropbox.core.p005v2.files.SyncSettingsError.Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return teamFolderUpdateSyncSettingsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderUpdateSyncSettingsError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        STATUS_ERROR,
        TEAM_SHARED_DROPBOX_ERROR,
        OTHER,
        SYNC_SETTINGS_ERROR
    }

    private TeamFolderUpdateSyncSettingsError() {
    }

    private TeamFolderUpdateSyncSettingsError withTag(Tag tag) {
        TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError = new TeamFolderUpdateSyncSettingsError();
        teamFolderUpdateSyncSettingsError._tag = tag;
        return teamFolderUpdateSyncSettingsError;
    }

    private TeamFolderUpdateSyncSettingsError withTagAndAccessError(Tag tag, TeamFolderAccessError teamFolderAccessError) {
        TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError = new TeamFolderUpdateSyncSettingsError();
        teamFolderUpdateSyncSettingsError._tag = tag;
        teamFolderUpdateSyncSettingsError.accessErrorValue = teamFolderAccessError;
        return teamFolderUpdateSyncSettingsError;
    }

    private TeamFolderUpdateSyncSettingsError withTagAndStatusError(Tag tag, TeamFolderInvalidStatusError teamFolderInvalidStatusError) {
        TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError = new TeamFolderUpdateSyncSettingsError();
        teamFolderUpdateSyncSettingsError._tag = tag;
        teamFolderUpdateSyncSettingsError.statusErrorValue = teamFolderInvalidStatusError;
        return teamFolderUpdateSyncSettingsError;
    }

    private TeamFolderUpdateSyncSettingsError withTagAndTeamSharedDropboxError(Tag tag, TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError) {
        TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError = new TeamFolderUpdateSyncSettingsError();
        teamFolderUpdateSyncSettingsError._tag = tag;
        teamFolderUpdateSyncSettingsError.teamSharedDropboxErrorValue = teamFolderTeamSharedDropboxError;
        return teamFolderUpdateSyncSettingsError;
    }

    private TeamFolderUpdateSyncSettingsError withTagAndSyncSettingsError(Tag tag, SyncSettingsError syncSettingsError) {
        TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError = new TeamFolderUpdateSyncSettingsError();
        teamFolderUpdateSyncSettingsError._tag = tag;
        teamFolderUpdateSyncSettingsError.syncSettingsErrorValue = syncSettingsError;
        return teamFolderUpdateSyncSettingsError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static TeamFolderUpdateSyncSettingsError accessError(TeamFolderAccessError teamFolderAccessError) {
        if (teamFolderAccessError != null) {
            return new TeamFolderUpdateSyncSettingsError().withTagAndAccessError(Tag.ACCESS_ERROR, teamFolderAccessError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamFolderAccessError getAccessErrorValue() {
        if (this._tag == Tag.ACCESS_ERROR) {
            return this.accessErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ACCESS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isStatusError() {
        return this._tag == Tag.STATUS_ERROR;
    }

    public static TeamFolderUpdateSyncSettingsError statusError(TeamFolderInvalidStatusError teamFolderInvalidStatusError) {
        if (teamFolderInvalidStatusError != null) {
            return new TeamFolderUpdateSyncSettingsError().withTagAndStatusError(Tag.STATUS_ERROR, teamFolderInvalidStatusError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamFolderInvalidStatusError getStatusErrorValue() {
        if (this._tag == Tag.STATUS_ERROR) {
            return this.statusErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.STATUS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTeamSharedDropboxError() {
        return this._tag == Tag.TEAM_SHARED_DROPBOX_ERROR;
    }

    public static TeamFolderUpdateSyncSettingsError teamSharedDropboxError(TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError) {
        if (teamFolderTeamSharedDropboxError != null) {
            return new TeamFolderUpdateSyncSettingsError().withTagAndTeamSharedDropboxError(Tag.TEAM_SHARED_DROPBOX_ERROR, teamFolderTeamSharedDropboxError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamFolderTeamSharedDropboxError getTeamSharedDropboxErrorValue() {
        if (this._tag == Tag.TEAM_SHARED_DROPBOX_ERROR) {
            return this.teamSharedDropboxErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TEAM_SHARED_DROPBOX_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public boolean isSyncSettingsError() {
        return this._tag == Tag.SYNC_SETTINGS_ERROR;
    }

    public static TeamFolderUpdateSyncSettingsError syncSettingsError(SyncSettingsError syncSettingsError) {
        if (syncSettingsError != null) {
            return new TeamFolderUpdateSyncSettingsError().withTagAndSyncSettingsError(Tag.SYNC_SETTINGS_ERROR, syncSettingsError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SyncSettingsError getSyncSettingsErrorValue() {
        if (this._tag == Tag.SYNC_SETTINGS_ERROR) {
            return this.syncSettingsErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SYNC_SETTINGS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue, this.statusErrorValue, this.teamSharedDropboxErrorValue, this.syncSettingsErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TeamFolderUpdateSyncSettingsError)) {
            return false;
        }
        TeamFolderUpdateSyncSettingsError teamFolderUpdateSyncSettingsError = (TeamFolderUpdateSyncSettingsError) obj;
        if (this._tag != teamFolderUpdateSyncSettingsError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                TeamFolderAccessError teamFolderAccessError = this.accessErrorValue;
                TeamFolderAccessError teamFolderAccessError2 = teamFolderUpdateSyncSettingsError.accessErrorValue;
                if (teamFolderAccessError != teamFolderAccessError2 && !teamFolderAccessError.equals(teamFolderAccessError2)) {
                    z = false;
                }
                return z;
            case STATUS_ERROR:
                TeamFolderInvalidStatusError teamFolderInvalidStatusError = this.statusErrorValue;
                TeamFolderInvalidStatusError teamFolderInvalidStatusError2 = teamFolderUpdateSyncSettingsError.statusErrorValue;
                if (teamFolderInvalidStatusError != teamFolderInvalidStatusError2 && !teamFolderInvalidStatusError.equals(teamFolderInvalidStatusError2)) {
                    z = false;
                }
                return z;
            case TEAM_SHARED_DROPBOX_ERROR:
                TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError = this.teamSharedDropboxErrorValue;
                TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError2 = teamFolderUpdateSyncSettingsError.teamSharedDropboxErrorValue;
                if (teamFolderTeamSharedDropboxError != teamFolderTeamSharedDropboxError2 && !teamFolderTeamSharedDropboxError.equals(teamFolderTeamSharedDropboxError2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
            case SYNC_SETTINGS_ERROR:
                SyncSettingsError syncSettingsError = this.syncSettingsErrorValue;
                SyncSettingsError syncSettingsError2 = teamFolderUpdateSyncSettingsError.syncSettingsErrorValue;
                if (syncSettingsError != syncSettingsError2 && !syncSettingsError.equals(syncSettingsError2)) {
                    z = false;
                }
                return z;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
