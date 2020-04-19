package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersSetPermissionsError */
public enum MembersSetPermissionsError {
    USER_NOT_FOUND,
    LAST_ADMIN,
    USER_NOT_IN_TEAM,
    CANNOT_SET_PERMISSIONS,
    TEAM_LICENSE_LIMIT,
    OTHER;

    /* renamed from: com.dropbox.core.v2.team.MembersSetPermissionsError$Serializer */
    static class Serializer extends UnionSerializer<MembersSetPermissionsError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersSetPermissionsError membersSetPermissionsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersSetPermissionsError) {
                case USER_NOT_FOUND:
                    jsonGenerator.writeString("user_not_found");
                    return;
                case LAST_ADMIN:
                    jsonGenerator.writeString("last_admin");
                    return;
                case USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("user_not_in_team");
                    return;
                case CANNOT_SET_PERMISSIONS:
                    jsonGenerator.writeString("cannot_set_permissions");
                    return;
                case TEAM_LICENSE_LIMIT:
                    jsonGenerator.writeString("team_license_limit");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MembersSetPermissionsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersSetPermissionsError membersSetPermissionsError;
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
                    membersSetPermissionsError = MembersSetPermissionsError.USER_NOT_FOUND;
                } else if ("last_admin".equals(str)) {
                    membersSetPermissionsError = MembersSetPermissionsError.LAST_ADMIN;
                } else if ("user_not_in_team".equals(str)) {
                    membersSetPermissionsError = MembersSetPermissionsError.USER_NOT_IN_TEAM;
                } else if ("cannot_set_permissions".equals(str)) {
                    membersSetPermissionsError = MembersSetPermissionsError.CANNOT_SET_PERMISSIONS;
                } else if ("team_license_limit".equals(str)) {
                    membersSetPermissionsError = MembersSetPermissionsError.TEAM_LICENSE_LIMIT;
                } else {
                    membersSetPermissionsError = MembersSetPermissionsError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return membersSetPermissionsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
