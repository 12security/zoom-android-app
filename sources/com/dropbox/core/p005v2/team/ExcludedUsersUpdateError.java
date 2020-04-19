package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateError */
public enum ExcludedUsersUpdateError {
    USERS_NOT_IN_TEAM,
    TOO_MANY_USERS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateError$Serializer */
    static class Serializer extends UnionSerializer<ExcludedUsersUpdateError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ExcludedUsersUpdateError excludedUsersUpdateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (excludedUsersUpdateError) {
                case USERS_NOT_IN_TEAM:
                    jsonGenerator.writeString("users_not_in_team");
                    return;
                case TOO_MANY_USERS:
                    jsonGenerator.writeString("too_many_users");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ExcludedUsersUpdateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ExcludedUsersUpdateError excludedUsersUpdateError;
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
                if ("users_not_in_team".equals(str)) {
                    excludedUsersUpdateError = ExcludedUsersUpdateError.USERS_NOT_IN_TEAM;
                } else if ("too_many_users".equals(str)) {
                    excludedUsersUpdateError = ExcludedUsersUpdateError.TOO_MANY_USERS;
                } else {
                    excludedUsersUpdateError = ExcludedUsersUpdateError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return excludedUsersUpdateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
