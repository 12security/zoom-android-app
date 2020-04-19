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

/* renamed from: com.dropbox.core.v2.team.TeamFolderCreateError */
public final class TeamFolderCreateError {
    public static final TeamFolderCreateError FOLDER_NAME_ALREADY_USED = new TeamFolderCreateError().withTag(Tag.FOLDER_NAME_ALREADY_USED);
    public static final TeamFolderCreateError FOLDER_NAME_RESERVED = new TeamFolderCreateError().withTag(Tag.FOLDER_NAME_RESERVED);
    public static final TeamFolderCreateError INVALID_FOLDER_NAME = new TeamFolderCreateError().withTag(Tag.INVALID_FOLDER_NAME);
    public static final TeamFolderCreateError OTHER = new TeamFolderCreateError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SyncSettingsError syncSettingsErrorValue;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderCreateError$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderCreateError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderCreateError teamFolderCreateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderCreateError.tag()) {
                case INVALID_FOLDER_NAME:
                    jsonGenerator.writeString("invalid_folder_name");
                    return;
                case FOLDER_NAME_ALREADY_USED:
                    jsonGenerator.writeString("folder_name_already_used");
                    return;
                case FOLDER_NAME_RESERVED:
                    jsonGenerator.writeString("folder_name_reserved");
                    return;
                case SYNC_SETTINGS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("sync_settings_error", jsonGenerator);
                    jsonGenerator.writeFieldName("sync_settings_error");
                    com.dropbox.core.p005v2.files.SyncSettingsError.Serializer.INSTANCE.serialize(teamFolderCreateError.syncSettingsErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TeamFolderCreateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderCreateError teamFolderCreateError;
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
                if ("invalid_folder_name".equals(str)) {
                    teamFolderCreateError = TeamFolderCreateError.INVALID_FOLDER_NAME;
                } else if ("folder_name_already_used".equals(str)) {
                    teamFolderCreateError = TeamFolderCreateError.FOLDER_NAME_ALREADY_USED;
                } else if ("folder_name_reserved".equals(str)) {
                    teamFolderCreateError = TeamFolderCreateError.FOLDER_NAME_RESERVED;
                } else if ("sync_settings_error".equals(str)) {
                    expectField("sync_settings_error", jsonParser);
                    teamFolderCreateError = TeamFolderCreateError.syncSettingsError(com.dropbox.core.p005v2.files.SyncSettingsError.Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    teamFolderCreateError = TeamFolderCreateError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return teamFolderCreateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderCreateError$Tag */
    public enum Tag {
        INVALID_FOLDER_NAME,
        FOLDER_NAME_ALREADY_USED,
        FOLDER_NAME_RESERVED,
        SYNC_SETTINGS_ERROR,
        OTHER
    }

    private TeamFolderCreateError() {
    }

    private TeamFolderCreateError withTag(Tag tag) {
        TeamFolderCreateError teamFolderCreateError = new TeamFolderCreateError();
        teamFolderCreateError._tag = tag;
        return teamFolderCreateError;
    }

    private TeamFolderCreateError withTagAndSyncSettingsError(Tag tag, SyncSettingsError syncSettingsError) {
        TeamFolderCreateError teamFolderCreateError = new TeamFolderCreateError();
        teamFolderCreateError._tag = tag;
        teamFolderCreateError.syncSettingsErrorValue = syncSettingsError;
        return teamFolderCreateError;
    }

    public Tag tag() {
        return this._tag;
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

    public boolean isSyncSettingsError() {
        return this._tag == Tag.SYNC_SETTINGS_ERROR;
    }

    public static TeamFolderCreateError syncSettingsError(SyncSettingsError syncSettingsError) {
        if (syncSettingsError != null) {
            return new TeamFolderCreateError().withTagAndSyncSettingsError(Tag.SYNC_SETTINGS_ERROR, syncSettingsError);
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

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.syncSettingsErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TeamFolderCreateError)) {
            return false;
        }
        TeamFolderCreateError teamFolderCreateError = (TeamFolderCreateError) obj;
        if (this._tag != teamFolderCreateError._tag) {
            return false;
        }
        switch (this._tag) {
            case INVALID_FOLDER_NAME:
                return true;
            case FOLDER_NAME_ALREADY_USED:
                return true;
            case FOLDER_NAME_RESERVED:
                return true;
            case SYNC_SETTINGS_ERROR:
                SyncSettingsError syncSettingsError = this.syncSettingsErrorValue;
                SyncSettingsError syncSettingsError2 = teamFolderCreateError.syncSettingsErrorValue;
                if (syncSettingsError != syncSettingsError2 && !syncSettingsError.equals(syncSettingsError2)) {
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
