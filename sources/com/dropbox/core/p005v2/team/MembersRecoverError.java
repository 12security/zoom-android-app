package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersRecoverError */
public enum MembersRecoverError {
    USER_NOT_FOUND,
    USER_UNRECOVERABLE,
    USER_NOT_IN_TEAM,
    TEAM_LICENSE_LIMIT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.MembersRecoverError$Serializer */
    static class Serializer extends UnionSerializer<MembersRecoverError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersRecoverError membersRecoverError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersRecoverError) {
                case USER_NOT_FOUND:
                    jsonGenerator.writeString("user_not_found");
                    return;
                case USER_UNRECOVERABLE:
                    jsonGenerator.writeString("user_unrecoverable");
                    return;
                case USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("user_not_in_team");
                    return;
                case TEAM_LICENSE_LIMIT:
                    jsonGenerator.writeString("team_license_limit");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MembersRecoverError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersRecoverError membersRecoverError;
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
                if ("user_not_found".equals(str)) {
                    membersRecoverError = MembersRecoverError.USER_NOT_FOUND;
                } else if ("user_unrecoverable".equals(str)) {
                    membersRecoverError = MembersRecoverError.USER_UNRECOVERABLE;
                } else if ("user_not_in_team".equals(str)) {
                    membersRecoverError = MembersRecoverError.USER_NOT_IN_TEAM;
                } else if ("team_license_limit".equals(str)) {
                    membersRecoverError = MembersRecoverError.TEAM_LICENSE_LIMIT;
                } else {
                    membersRecoverError = MembersRecoverError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return membersRecoverError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
