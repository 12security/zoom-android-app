package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersListContinueError */
public enum MembersListContinueError {
    INVALID_CURSOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.MembersListContinueError$1 */
    static /* synthetic */ class C08381 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$MembersListContinueError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$MembersListContinueError = new int[MembersListContinueError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$MembersListContinueError[MembersListContinueError.INVALID_CURSOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MembersListContinueError$Serializer */
    static class Serializer extends UnionSerializer<MembersListContinueError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersListContinueError membersListContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08381.$SwitchMap$com$dropbox$core$v2$team$MembersListContinueError[membersListContinueError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("invalid_cursor");
            }
        }

        public MembersListContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersListContinueError membersListContinueError;
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
                    membersListContinueError = MembersListContinueError.INVALID_CURSOR;
                } else {
                    membersListContinueError = MembersListContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return membersListContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
