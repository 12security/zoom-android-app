package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.TeamNamespacesListContinueError */
public enum TeamNamespacesListContinueError {
    INVALID_CURSOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.TeamNamespacesListContinueError$1 */
    static /* synthetic */ class C08731 {

        /* renamed from: $SwitchMap$com$dropbox$core$v2$team$TeamNamespacesListContinueError */
        static final /* synthetic */ int[] f146xf6f1bd75 = null;

        static {
            f146xf6f1bd75 = new int[TeamNamespacesListContinueError.values().length];
            try {
                f146xf6f1bd75[TeamNamespacesListContinueError.INVALID_CURSOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.TeamNamespacesListContinueError$Serializer */
    static class Serializer extends UnionSerializer<TeamNamespacesListContinueError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(TeamNamespacesListContinueError teamNamespacesListContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08731.f146xf6f1bd75[teamNamespacesListContinueError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("invalid_cursor");
            }
        }

        public TeamNamespacesListContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TeamNamespacesListContinueError teamNamespacesListContinueError;
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
                    teamNamespacesListContinueError = TeamNamespacesListContinueError.INVALID_CURSOR;
                } else {
                    teamNamespacesListContinueError = TeamNamespacesListContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return teamNamespacesListContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
