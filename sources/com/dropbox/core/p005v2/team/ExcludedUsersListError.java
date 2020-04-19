package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersListError */
public enum ExcludedUsersListError {
    LIST_ERROR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersListError$1 */
    static /* synthetic */ class C08031 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$team$ExcludedUsersListError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$team$ExcludedUsersListError = new int[ExcludedUsersListError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$team$ExcludedUsersListError[ExcludedUsersListError.LIST_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersListError$Serializer */
    static class Serializer extends UnionSerializer<ExcludedUsersListError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ExcludedUsersListError excludedUsersListError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C08031.$SwitchMap$com$dropbox$core$v2$team$ExcludedUsersListError[excludedUsersListError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("list_error");
            }
        }

        public ExcludedUsersListError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ExcludedUsersListError excludedUsersListError;
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
                if ("list_error".equals(str)) {
                    excludedUsersListError = ExcludedUsersListError.LIST_ERROR;
                } else {
                    excludedUsersListError = ExcludedUsersListError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return excludedUsersListError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
