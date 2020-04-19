package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.TeamFolderAccessError */
public enum TeamFolderAccessError {
    INVALID_TEAM_FOLDER_ID,
    NO_ACCESS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderAccessError$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderAccessError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TeamFolderAccessError teamFolderAccessError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (teamFolderAccessError) {
                case INVALID_TEAM_FOLDER_ID:
                    jsonGenerator.writeString("invalid_team_folder_id");
                    return;
                case NO_ACCESS:
                    jsonGenerator.writeString("no_access");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TeamFolderAccessError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderAccessError teamFolderAccessError;
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
                if ("invalid_team_folder_id".equals(str)) {
                    teamFolderAccessError = TeamFolderAccessError.INVALID_TEAM_FOLDER_ID;
                } else if ("no_access".equals(str)) {
                    teamFolderAccessError = TeamFolderAccessError.NO_ACCESS;
                } else {
                    teamFolderAccessError = TeamFolderAccessError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return teamFolderAccessError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
