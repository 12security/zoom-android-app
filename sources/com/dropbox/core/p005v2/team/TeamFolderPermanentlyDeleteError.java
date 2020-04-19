package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TeamFolderPermanentlyDeleteError */
public final class TeamFolderPermanentlyDeleteError {
    public static final TeamFolderPermanentlyDeleteError OTHER = new TeamFolderPermanentlyDeleteError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public TeamFolderAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public TeamFolderInvalidStatusError statusErrorValue;
    /* access modifiers changed from: private */
    public TeamFolderTeamSharedDropboxError teamSharedDropboxErrorValue;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderPermanentlyDeleteError$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderPermanentlyDeleteError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderPermanentlyDeleteError teamFolderPermanentlyDeleteError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderPermanentlyDeleteError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(teamFolderPermanentlyDeleteError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case STATUS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("status_error", jsonGenerator);
                    jsonGenerator.writeFieldName("status_error");
                    Serializer.INSTANCE.serialize(teamFolderPermanentlyDeleteError.statusErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TEAM_SHARED_DROPBOX_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("team_shared_dropbox_error", jsonGenerator);
                    jsonGenerator.writeFieldName("team_shared_dropbox_error");
                    Serializer.INSTANCE.serialize(teamFolderPermanentlyDeleteError.teamSharedDropboxErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamFolderPermanentlyDeleteError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamFolderPermanentlyDeleteError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderPermanentlyDeleteError teamFolderPermanentlyDeleteError;
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
                    teamFolderPermanentlyDeleteError = TeamFolderPermanentlyDeleteError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("status_error".equals(str)) {
                    expectField("status_error", jsonParser);
                    teamFolderPermanentlyDeleteError = TeamFolderPermanentlyDeleteError.statusError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("team_shared_dropbox_error".equals(str)) {
                    expectField("team_shared_dropbox_error", jsonParser);
                    teamFolderPermanentlyDeleteError = TeamFolderPermanentlyDeleteError.teamSharedDropboxError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("other".equals(str)) {
                    teamFolderPermanentlyDeleteError = TeamFolderPermanentlyDeleteError.OTHER;
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
                return teamFolderPermanentlyDeleteError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderPermanentlyDeleteError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        STATUS_ERROR,
        TEAM_SHARED_DROPBOX_ERROR,
        OTHER
    }

    private TeamFolderPermanentlyDeleteError() {
    }

    private TeamFolderPermanentlyDeleteError withTag(Tag tag) {
        TeamFolderPermanentlyDeleteError teamFolderPermanentlyDeleteError = new TeamFolderPermanentlyDeleteError();
        teamFolderPermanentlyDeleteError._tag = tag;
        return teamFolderPermanentlyDeleteError;
    }

    private TeamFolderPermanentlyDeleteError withTagAndAccessError(Tag tag, TeamFolderAccessError teamFolderAccessError) {
        TeamFolderPermanentlyDeleteError teamFolderPermanentlyDeleteError = new TeamFolderPermanentlyDeleteError();
        teamFolderPermanentlyDeleteError._tag = tag;
        teamFolderPermanentlyDeleteError.accessErrorValue = teamFolderAccessError;
        return teamFolderPermanentlyDeleteError;
    }

    private TeamFolderPermanentlyDeleteError withTagAndStatusError(Tag tag, TeamFolderInvalidStatusError teamFolderInvalidStatusError) {
        TeamFolderPermanentlyDeleteError teamFolderPermanentlyDeleteError = new TeamFolderPermanentlyDeleteError();
        teamFolderPermanentlyDeleteError._tag = tag;
        teamFolderPermanentlyDeleteError.statusErrorValue = teamFolderInvalidStatusError;
        return teamFolderPermanentlyDeleteError;
    }

    private TeamFolderPermanentlyDeleteError withTagAndTeamSharedDropboxError(Tag tag, TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError) {
        TeamFolderPermanentlyDeleteError teamFolderPermanentlyDeleteError = new TeamFolderPermanentlyDeleteError();
        teamFolderPermanentlyDeleteError._tag = tag;
        teamFolderPermanentlyDeleteError.teamSharedDropboxErrorValue = teamFolderTeamSharedDropboxError;
        return teamFolderPermanentlyDeleteError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static TeamFolderPermanentlyDeleteError accessError(TeamFolderAccessError teamFolderAccessError) {
        if (teamFolderAccessError != null) {
            return new TeamFolderPermanentlyDeleteError().withTagAndAccessError(Tag.ACCESS_ERROR, teamFolderAccessError);
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

    public static TeamFolderPermanentlyDeleteError statusError(TeamFolderInvalidStatusError teamFolderInvalidStatusError) {
        if (teamFolderInvalidStatusError != null) {
            return new TeamFolderPermanentlyDeleteError().withTagAndStatusError(Tag.STATUS_ERROR, teamFolderInvalidStatusError);
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

    public static TeamFolderPermanentlyDeleteError teamSharedDropboxError(TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError) {
        if (teamFolderTeamSharedDropboxError != null) {
            return new TeamFolderPermanentlyDeleteError().withTagAndTeamSharedDropboxError(Tag.TEAM_SHARED_DROPBOX_ERROR, teamFolderTeamSharedDropboxError);
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
        if (obj == null || !(obj instanceof TeamFolderPermanentlyDeleteError)) {
            return false;
        }
        TeamFolderPermanentlyDeleteError teamFolderPermanentlyDeleteError = (TeamFolderPermanentlyDeleteError) obj;
        if (this._tag != teamFolderPermanentlyDeleteError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                TeamFolderAccessError teamFolderAccessError = this.accessErrorValue;
                TeamFolderAccessError teamFolderAccessError2 = teamFolderPermanentlyDeleteError.accessErrorValue;
                if (teamFolderAccessError != teamFolderAccessError2 && !teamFolderAccessError.equals(teamFolderAccessError2)) {
                    z = false;
                }
                return z;
            case STATUS_ERROR:
                TeamFolderInvalidStatusError teamFolderInvalidStatusError = this.statusErrorValue;
                TeamFolderInvalidStatusError teamFolderInvalidStatusError2 = teamFolderPermanentlyDeleteError.statusErrorValue;
                if (teamFolderInvalidStatusError != teamFolderInvalidStatusError2 && !teamFolderInvalidStatusError.equals(teamFolderInvalidStatusError2)) {
                    z = false;
                }
                return z;
            case TEAM_SHARED_DROPBOX_ERROR:
                TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError = this.teamSharedDropboxErrorValue;
                TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError2 = teamFolderPermanentlyDeleteError.teamSharedDropboxErrorValue;
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
