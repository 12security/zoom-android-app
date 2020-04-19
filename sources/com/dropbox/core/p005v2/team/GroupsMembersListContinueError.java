package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupsMembersListContinueError */
public enum GroupsMembersListContinueError {
    INVALID_CURSOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.GroupsMembersListContinueError$1 */
    static /* synthetic */ class C08211 {

        /* renamed from: $SwitchMap$com$dropbox$core$v2$team$GroupsMembersListContinueError */
        static final /* synthetic */ int[] f137x9a23a97 = null;

        static {
            f137x9a23a97 = new int[GroupsMembersListContinueError.values().length];
            try {
                f137x9a23a97[GroupsMembersListContinueError.INVALID_CURSOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupsMembersListContinueError$Serializer */
    static class Serializer extends UnionSerializer<GroupsMembersListContinueError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupsMembersListContinueError groupsMembersListContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08211.f137x9a23a97[groupsMembersListContinueError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("invalid_cursor");
            }
        }

        public GroupsMembersListContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupsMembersListContinueError groupsMembersListContinueError;
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
                    groupsMembersListContinueError = GroupsMembersListContinueError.INVALID_CURSOR;
                } else {
                    groupsMembersListContinueError = GroupsMembersListContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupsMembersListContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
