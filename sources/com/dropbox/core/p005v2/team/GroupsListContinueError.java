package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.GroupsListContinueError */
public enum GroupsListContinueError {
    INVALID_CURSOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.GroupsListContinueError$1 */
    static /* synthetic */ class C08201 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$GroupsListContinueError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$GroupsListContinueError = new int[GroupsListContinueError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$GroupsListContinueError[GroupsListContinueError.INVALID_CURSOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupsListContinueError$Serializer */
    static class Serializer extends UnionSerializer<GroupsListContinueError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GroupsListContinueError groupsListContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08201.$SwitchMap$com$dropbox$core$v2$team$GroupsListContinueError[groupsListContinueError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("invalid_cursor");
            }
        }

        public GroupsListContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupsListContinueError groupsListContinueError;
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
                    groupsListContinueError = GroupsListContinueError.INVALID_CURSOR;
                } else {
                    groupsListContinueError = GroupsListContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupsListContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
