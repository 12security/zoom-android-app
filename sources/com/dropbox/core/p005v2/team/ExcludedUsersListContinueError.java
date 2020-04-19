package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersListContinueError */
public enum ExcludedUsersListContinueError {
    INVALID_CURSOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersListContinueError$1 */
    static /* synthetic */ class C08021 {

        /* renamed from: $SwitchMap$com$dropbox$core$v2$team$ExcludedUsersListContinueError */
        static final /* synthetic */ int[] f135x8c7f05fe = null;

        static {
            f135x8c7f05fe = new int[ExcludedUsersListContinueError.values().length];
            try {
                f135x8c7f05fe[ExcludedUsersListContinueError.INVALID_CURSOR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersListContinueError$Serializer */
    static class Serializer extends UnionSerializer<ExcludedUsersListContinueError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ExcludedUsersListContinueError excludedUsersListContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08021.f135x8c7f05fe[excludedUsersListContinueError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("invalid_cursor");
            }
        }

        public ExcludedUsersListContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ExcludedUsersListContinueError excludedUsersListContinueError;
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
                    excludedUsersListContinueError = ExcludedUsersListContinueError.INVALID_CURSOR;
                } else {
                    excludedUsersListContinueError = ExcludedUsersListContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return excludedUsersListContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
