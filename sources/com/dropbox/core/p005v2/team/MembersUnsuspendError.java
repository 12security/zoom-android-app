package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersUnsuspendError */
public enum MembersUnsuspendError {
    USER_NOT_FOUND,
    USER_NOT_IN_TEAM,
    OTHER,
    UNSUSPEND_NON_SUSPENDED_MEMBER,
    TEAM_LICENSE_LIMIT;

    /* renamed from: com.dropbox.core.v2.team.MembersUnsuspendError$Serializer */
    static class Serializer extends UnionSerializer<MembersUnsuspendError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersUnsuspendError membersUnsuspendError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersUnsuspendError) {
                case USER_NOT_FOUND:
                    jsonGenerator.writeString("user_not_found");
                    return;
                case USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("user_not_in_team");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case UNSUSPEND_NON_SUSPENDED_MEMBER:
                    jsonGenerator.writeString("unsuspend_non_suspended_member");
                    return;
                case TEAM_LICENSE_LIMIT:
                    jsonGenerator.writeString("team_license_limit");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(membersUnsuspendError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MembersUnsuspendError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersUnsuspendError membersUnsuspendError;
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
                    membersUnsuspendError = MembersUnsuspendError.USER_NOT_FOUND;
                } else if ("user_not_in_team".equals(str)) {
                    membersUnsuspendError = MembersUnsuspendError.USER_NOT_IN_TEAM;
                } else if ("other".equals(str)) {
                    membersUnsuspendError = MembersUnsuspendError.OTHER;
                } else if ("unsuspend_non_suspended_member".equals(str)) {
                    membersUnsuspendError = MembersUnsuspendError.UNSUSPEND_NON_SUSPENDED_MEMBER;
                } else if ("team_license_limit".equals(str)) {
                    membersUnsuspendError = MembersUnsuspendError.TEAM_LICENSE_LIMIT;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return membersUnsuspendError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
