package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TeamFolderActivateError */
public final class TeamFolderActivateError {
    public static final TeamFolderActivateError OTHER = new TeamFolderActivateError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public TeamFolderAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public TeamFolderInvalidStatusError statusErrorValue;
    /* access modifiers changed from: private */
    public TeamFolderTeamSharedDropboxError teamSharedDropboxErrorValue;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderActivateError$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderActivateError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderActivateError teamFolderActivateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderActivateError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(teamFolderActivateError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case STATUS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("status_error", jsonGenerator);
                    jsonGenerator.writeFieldName("status_error");
                    Serializer.INSTANCE.serialize(teamFolderActivateError.statusErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TEAM_SHARED_DROPBOX_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("team_shared_dropbox_error", jsonGenerator);
                    jsonGenerator.writeFieldName("team_shared_dropbox_error");
                    Serializer.INSTANCE.serialize(teamFolderActivateError.teamSharedDropboxErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamFolderActivateError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamFolderActivateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderActivateError teamFolderActivateError;
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
                    teamFolderActivateError = TeamFolderActivateError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("status_error".equals(str)) {
                    expectField("status_error", jsonParser);
                    teamFolderActivateError = TeamFolderActivateError.statusError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("team_shared_dropbox_error".equals(str)) {
                    expectField("team_shared_dropbox_error", jsonParser);
                    teamFolderActivateError = TeamFolderActivateError.teamSharedDropboxError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("other".equals(str)) {
                    teamFolderActivateError = TeamFolderActivateError.OTHER;
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
                return teamFolderActivateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderActivateError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        STATUS_ERROR,
        TEAM_SHARED_DROPBOX_ERROR,
        OTHER
    }

    private TeamFolderActivateError() {
    }

    private TeamFolderActivateError withTag(Tag tag) {
        TeamFolderActivateError teamFolderActivateError = new TeamFolderActivateError();
        teamFolderActivateError._tag = tag;
        return teamFolderActivateError;
    }

    private TeamFolderActivateError withTagAndAccessError(Tag tag, TeamFolderAccessError teamFolderAccessError) {
        TeamFolderActivateError teamFolderActivateError = new TeamFolderActivateError();
        teamFolderActivateError._tag = tag;
        teamFolderActivateError.accessErrorValue = teamFolderAccessError;
        return teamFolderActivateError;
    }

    private TeamFolderActivateError withTagAndStatusError(Tag tag, TeamFolderInvalidStatusError teamFolderInvalidStatusError) {
        TeamFolderActivateError teamFolderActivateError = new TeamFolderActivateError();
        teamFolderActivateError._tag = tag;
        teamFolderActivateError.statusErrorValue = teamFolderInvalidStatusError;
        return teamFolderActivateError;
    }

    private TeamFolderActivateError withTagAndTeamSharedDropboxError(Tag tag, TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError) {
        TeamFolderActivateError teamFolderActivateError = new TeamFolderActivateError();
        teamFolderActivateError._tag = tag;
        teamFolderActivateError.teamSharedDropboxErrorValue = teamFolderTeamSharedDropboxError;
        return teamFolderActivateError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static TeamFolderActivateError accessError(TeamFolderAccessError teamFolderAccessError) {
        if (teamFolderAccessError != null) {
            return new TeamFolderActivateError().withTagAndAccessError(Tag.ACCESS_ERROR, teamFolderAccessError);
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

    public static TeamFolderActivateError statusError(TeamFolderInvalidStatusError teamFolderInvalidStatusError) {
        if (teamFolderInvalidStatusError != null) {
            return new TeamFolderActivateError().withTagAndStatusError(Tag.STATUS_ERROR, teamFolderInvalidStatusError);
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

    public static TeamFolderActivateError teamSharedDropboxError(TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError) {
        if (teamFolderTeamSharedDropboxError != null) {
            return new TeamFolderActivateError().withTagAndTeamSharedDropboxError(Tag.TEAM_SHARED_DROPBOX_ERROR, teamFolderTeamSharedDropboxError);
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

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue, this.statusErrorValue, this.teamSharedDropboxErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TeamFolderActivateError)) {
            return false;
        }
        TeamFolderActivateError teamFolderActivateError = (TeamFolderActivateError) obj;
        if (this._tag != teamFolderActivateError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                TeamFolderAccessError teamFolderAccessError = this.accessErrorValue;
                TeamFolderAccessError teamFolderAccessError2 = teamFolderActivateError.accessErrorValue;
                if (teamFolderAccessError != teamFolderAccessError2 && !teamFolderAccessError.equals(teamFolderAccessError2)) {
                    z = false;
                }
                return z;
            case STATUS_ERROR:
                TeamFolderInvalidStatusError teamFolderInvalidStatusError = this.statusErrorValue;
                TeamFolderInvalidStatusError teamFolderInvalidStatusError2 = teamFolderActivateError.statusErrorValue;
                if (teamFolderInvalidStatusError != teamFolderInvalidStatusError2 && !teamFolderInvalidStatusError.equals(teamFolderInvalidStatusError2)) {
                    z = false;
                }
                return z;
            case TEAM_SHARED_DROPBOX_ERROR:
                TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError = this.teamSharedDropboxErrorValue;
                TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError2 = teamFolderActivateError.teamSharedDropboxErrorValue;
                if (teamFolderTeamSharedDropboxError != teamFolderTeamSharedDropboxError2 && !teamFolderTeamSharedDropboxError.equals(teamFolderTeamSharedDropboxError2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
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
