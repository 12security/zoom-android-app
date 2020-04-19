package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.TeamFolderGetInfoItem */
public final class TeamFolderGetInfoItem {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String idNotFoundValue;
    /* access modifiers changed from: private */
    public TeamFolderMetadata teamFolderMetadataValue;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderGetInfoItem$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderGetInfoItem> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderGetInfoItem teamFolderGetInfoItem, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderGetInfoItem.tag()) {
                case ID_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("id_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("id_not_found");
                    StoneSerializers.string().serialize(teamFolderGetInfoItem.idNotFoundValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TEAM_FOLDER_METADATA:
                    jsonGenerator.writeStartObject();
                    writeTag("team_folder_metadata", jsonGenerator);
                    Serializer.INSTANCE.serialize(teamFolderGetInfoItem.teamFolderMetadataValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(teamFolderGetInfoItem.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public TeamFolderGetInfoItem deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            TeamFolderGetInfoItem teamFolderGetInfoItem;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if ("id_not_found".equals(str)) {
                    expectField("id_not_found", jsonParser);
                    teamFolderGetInfoItem = TeamFolderGetInfoItem.idNotFound((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("team_folder_metadata".equals(str)) {
                    teamFolderGetInfoItem = TeamFolderGetInfoItem.teamFolderMetadata(Serializer.INSTANCE.deserialize(jsonParser, true));
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
                return teamFolderGetInfoItem;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderGetInfoItem$Tag */
    public enum Tag {
        ID_NOT_FOUND,
        TEAM_FOLDER_METADATA
    }

    private TeamFolderGetInfoItem() {
    }

    private TeamFolderGetInfoItem withTag(Tag tag) {
        TeamFolderGetInfoItem teamFolderGetInfoItem = new TeamFolderGetInfoItem();
        teamFolderGetInfoItem._tag = tag;
        return teamFolderGetInfoItem;
    }

    private TeamFolderGetInfoItem withTagAndIdNotFound(Tag tag, String str) {
        TeamFolderGetInfoItem teamFolderGetInfoItem = new TeamFolderGetInfoItem();
        teamFolderGetInfoItem._tag = tag;
        teamFolderGetInfoItem.idNotFoundValue = str;
        return teamFolderGetInfoItem;
    }

    private TeamFolderGetInfoItem withTagAndTeamFolderMetadata(Tag tag, TeamFolderMetadata teamFolderMetadata) {
        TeamFolderGetInfoItem teamFolderGetInfoItem = new TeamFolderGetInfoItem();
        teamFolderGetInfoItem._tag = tag;
        teamFolderGetInfoItem.teamFolderMetadataValue = teamFolderMetadata;
        return teamFolderGetInfoItem;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isIdNotFound() {
        return this._tag == Tag.ID_NOT_FOUND;
    }

    public static TeamFolderGetInfoItem idNotFound(String str) {
        if (str != null) {
            return new TeamFolderGetInfoItem().withTagAndIdNotFound(Tag.ID_NOT_FOUND, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getIdNotFoundValue() {
        if (this._tag == Tag.ID_NOT_FOUND) {
            return this.idNotFoundValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ID_NOT_FOUND, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTeamFolderMetadata() {
        return this._tag == Tag.TEAM_FOLDER_METADATA;
    }

    public static TeamFolderGetInfoItem teamFolderMetadata(TeamFolderMetadata teamFolderMetadata) {
        if (teamFolderMetadata != null) {
            return new TeamFolderGetInfoItem().withTagAndTeamFolderMetadata(Tag.TEAM_FOLDER_METADATA, teamFolderMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public TeamFolderMetadata getTeamFolderMetadataValue() {
        if (this._tag == Tag.TEAM_FOLDER_METADATA) {
            return this.teamFolderMetadataValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TEAM_FOLDER_METADATA, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.idNotFoundValue, this.teamFolderMetadataValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TeamFolderGetInfoItem)) {
            return false;
        }
        TeamFolderGetInfoItem teamFolderGetInfoItem = (TeamFolderGetInfoItem) obj;
        if (this._tag != teamFolderGetInfoItem._tag) {
            return false;
        }
        switch (this._tag) {
            case ID_NOT_FOUND:
                String str = this.idNotFoundValue;
                String str2 = teamFolderGetInfoItem.idNotFoundValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case TEAM_FOLDER_METADATA:
                TeamFolderMetadata teamFolderMetadata = this.teamFolderMetadataValue;
                TeamFolderMetadata teamFolderMetadata2 = teamFolderGetInfoItem.teamFolderMetadataValue;
                if (teamFolderMetadata != teamFolderMetadata2 && !teamFolderMetadata.equals(teamFolderMetadata2)) {
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
