package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TeamFolderRenameError */
public final class TeamFolderRenameError {
    public static final TeamFolderRenameError FOLDER_NAME_ALREADY_USED = new TeamFolderRenameError().withTag(Tag.FOLDER_NAME_ALREADY_USED);
    public static final TeamFolderRenameError FOLDER_NAME_RESERVED = new TeamFolderRenameError().withTag(Tag.FOLDER_NAME_RESERVED);
    public static final TeamFolderRenameError INVALID_FOLDER_NAME = new TeamFolderRenameError().withTag(Tag.INVALID_FOLDER_NAME);
    public static final TeamFolderRenameError OTHER = new TeamFolderRenameError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public TeamFolderAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public TeamFolderInvalidStatusError statusErrorValue;
    /* access modifiers changed from: private */
    public TeamFolderTeamSharedDropboxError teamSharedDropboxErrorValue;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderRenameError$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderRenameError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderRenameError teamFolderRenameError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderRenameError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(teamFolderRenameError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case STATUS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("status_error", jsonGenerator);
                    jsonGenerator.writeFieldName("status_error");
                    Serializer.INSTANCE.serialize(teamFolderRenameError.statusErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TEAM_SHARED_DROPBOX_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("team_shared_dropbox_error", jsonGenerator);
                    jsonGenerator.writeFieldName("team_shared_dropbox_error");
                    Serializer.INSTANCE.serialize(teamFolderRenameError.teamSharedDropboxErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case INVALID_FOLDER_NAME:
                    jsonGenerator.writeString("invalid_folder_name");
                    return;
                case FOLDER_NAME_ALREADY_USED:
                    jsonGenerator.writeString("folder_name_already_used");
                    return;
                case FOLDER_NAME_RESERVED:
                    jsonGenerator.writeString("folder_name_reserved");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamFolderRenameError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamFolderRenameError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderRenameError teamFolderRenameError;
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
                    teamFolderRenameError = TeamFolderRenameError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("status_error".equals(str)) {
                    expectField("status_error", jsonParser);
                    teamFolderRenameError = TeamFolderRenameError.statusError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("team_shared_dropbox_error".equals(str)) {
                    expectField("team_shared_dropbox_error", jsonParser);
                    teamFolderRenameError = TeamFolderRenameError.teamSharedDropboxError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("other".equals(str)) {
                    teamFolderRenameError = TeamFolderRenameError.OTHER;
                } else if ("invalid_folder_name".equals(str)) {
                    teamFolderRenameError = TeamFolderRenameError.INVALID_FOLDER_NAME;
                } else if ("folder_name_already_used".equals(str)) {
                    teamFolderRenameError = TeamFolderRenameError.FOLDER_NAME_ALREADY_USED;
                } else if ("folder_name_reserved".equals(str)) {
                    teamFolderRenameError = TeamFolderRenameError.FOLDER_NAME_RESERVED;
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
                return teamFolderRenameError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderRenameError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        STATUS_ERROR,
        TEAM_SHARED_DROPBOX_ERROR,
        OTHER,
        INVALID_FOLDER_NAME,
        FOLDER_NAME_ALREADY_USED,
        FOLDER_NAME_RESERVED
    }

    private TeamFolderRenameError() {
    }

    private TeamFolderRenameError withTag(Tag tag) {
        TeamFolderRenameError teamFolderRenameError = new TeamFolderRenameError();
        teamFolderRenameError._tag = tag;
        return teamFolderRenameError;
    }

    private TeamFolderRenameError withTagAndAccessError(Tag tag, TeamFolderAccessError teamFolderAccessError) {
        TeamFolderRenameError teamFolderRenameError = new TeamFolderRenameError();
        teamFolderRenameError._tag = tag;
        teamFolderRenameError.accessErrorValue = teamFolderAccessError;
        return teamFolderRenameError;
    }

    private TeamFolderRenameError withTagAndStatusError(Tag tag, TeamFolderInvalidStatusError teamFolderInvalidStatusError) {
        TeamFolderRenameError teamFolderRenameError = new TeamFolderRenameError();
        teamFolderRenameError._tag = tag;
        teamFolderRenameError.statusErrorValue = teamFolderInvalidStatusError;
        return teamFolderRenameError;
    }

    private TeamFolderRenameError withTagAndTeamSharedDropboxError(Tag tag, TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError) {
        TeamFolderRenameError teamFolderRenameError = new TeamFolderRenameError();
        teamFolderRenameError._tag = tag;
        teamFolderRenameError.teamSharedDropboxErrorValue = teamFolderTeamSharedDropboxError;
        return teamFolderRenameError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static TeamFolderRenameError accessError(TeamFolderAccessError teamFolderAccessError) {
        if (teamFolderAccessError != null) {
            return new TeamFolderRenameError().withTagAndAccessError(Tag.ACCESS_ERROR, teamFolderAccessError);
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

    public static TeamFolderRenameError statusError(TeamFolderInvalidStatusError teamFolderInvalidStatusError) {
        if (teamFolderInvalidStatusError != null) {
            return new TeamFolderRenameError().withTagAndStatusError(Tag.STATUS_ERROR, teamFolderInvalidStatusError);
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

    public static TeamFolderRenameError teamSharedDropboxError(TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError) {
        if (teamFolderTeamSharedDropboxError != null) {
            return new TeamFolderRenameError().withTagAndTeamSharedDropboxError(Tag.TEAM_SHARED_DROPBOX_ERROR, teamFolderTeamSharedDropboxError);
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

    public boolean isInvalidFolderName() {
        return this._tag == Tag.INVALID_FOLDER_NAME;
    }

    public boolean isFolderNameAlreadyUsed() {
        return this._tag == Tag.FOLDER_NAME_ALREADY_USED;
    }

    public boolean isFolderNameReserved() {
        return this._tag == Tag.FOLDER_NAME_RESERVED;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue, this.statusErrorValue, this.teamSharedDropboxErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TeamFolderRenameError)) {
            return false;
        }
        TeamFolderRenameError teamFolderRenameError = (TeamFolderRenameError) obj;
        if (this._tag != teamFolderRenameError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                TeamFolderAccessError teamFolderAccessError = this.accessErrorValue;
                TeamFolderAccessError teamFolderAccessError2 = teamFolderRenameError.accessErrorValue;
                if (teamFolderAccessError != teamFolderAccessError2 && !teamFolderAccessError.equals(teamFolderAccessError2)) {
                    z = false;
                }
                return z;
            case STATUS_ERROR:
                TeamFolderInvalidStatusError teamFolderInvalidStatusError = this.statusErrorValue;
                TeamFolderInvalidStatusError teamFolderInvalidStatusError2 = teamFolderRenameError.statusErrorValue;
                if (teamFolderInvalidStatusError != teamFolderInvalidStatusError2 && !teamFolderInvalidStatusError.equals(teamFolderInvalidStatusError2)) {
                    z = false;
                }
                return z;
            case TEAM_SHARED_DROPBOX_ERROR:
                TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError = this.teamSharedDropboxErrorValue;
                TeamFolderTeamSharedDropboxError teamFolderTeamSharedDropboxError2 = teamFolderRenameError.teamSharedDropboxErrorValue;
                if (teamFolderTeamSharedDropboxError != teamFolderTeamSharedDropboxError2 && !teamFolderTeamSharedDropboxError.equals(teamFolderTeamSharedDropboxError2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
            case INVALID_FOLDER_NAME:
                return true;
            case FOLDER_NAME_ALREADY_USED:
                return true;
            case FOLDER_NAME_RESERVED:
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
