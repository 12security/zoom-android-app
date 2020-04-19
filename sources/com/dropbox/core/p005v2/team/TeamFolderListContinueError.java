package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.TeamFolderListContinueError */
public enum TeamFolderListContinueError {
    INVALID_CURSOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.TeamFolderListContinueError$1 */
    static /* synthetic */ class C08651 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$TeamFolderListContinueError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$TeamFolderListContinueError = new int[TeamFolderListContinueError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$TeamFolderListContinueError[TeamFolderListContinueError.INVALID_CURSOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamFolderListContinueError$Serializer */
    static class Serializer extends UnionSerializer<TeamFolderListContinueError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TeamFolderListContinueError teamFolderListContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08651.$SwitchMap$com$dropbox$core$v2$team$TeamFolderListContinueError[teamFolderListContinueError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("invalid_cursor");
            }
        }

        public TeamFolderListContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamFolderListContinueError teamFolderListContinueError;
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
                if ("invalid_cursor".equals(str)) {
                    teamFolderListContinueError = TeamFolderListContinueError.INVALID_CURSOR;
                } else {
                    teamFolderListContinueError = TeamFolderListContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return teamFolderListContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
