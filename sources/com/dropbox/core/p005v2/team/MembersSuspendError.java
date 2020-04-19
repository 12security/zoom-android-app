package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.MembersSuspendError */
public enum MembersSuspendError {
    USER_NOT_FOUND,
    USER_NOT_IN_TEAM,
    OTHER,
    SUSPEND_INACTIVE_USER,
    SUSPEND_LAST_ADMIN,
    TEAM_LICENSE_LIMIT;

    /* renamed from: com.dropbox.core.v2.team.MembersSuspendError$Serializer */
    static class Serializer extends UnionSerializer<MembersSuspendError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MembersSuspendError membersSuspendError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (membersSuspendError) {
                case USER_NOT_FOUND:
                    jsonGenerator.writeString("user_not_found");
                    return;
                case USER_NOT_IN_TEAM:
                    jsonGenerator.writeString("user_not_in_team");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SUSPEND_INACTIVE_USER:
                    jsonGenerator.writeString("suspend_inactive_user");
                    return;
                case SUSPEND_LAST_ADMIN:
                    jsonGenerator.writeString("suspend_last_admin");
                    return;
                case TEAM_LICENSE_LIMIT:
                    jsonGenerator.writeString("team_license_limit");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(membersSuspendError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MembersSuspendError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MembersSuspendError membersSuspendError;
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
                    membersSuspendError = MembersSuspendError.USER_NOT_FOUND;
                } else if ("user_not_in_team".equals(str)) {
                    membersSuspendError = MembersSuspendError.USER_NOT_IN_TEAM;
                } else if ("other".equals(str)) {
                    membersSuspendError = MembersSuspendError.OTHER;
                } else if ("suspend_inactive_user".equals(str)) {
                    membersSuspendError = MembersSuspendError.SUSPEND_INACTIVE_USER;
                } else if ("suspend_last_admin".equals(str)) {
                    membersSuspendError = MembersSuspendError.SUSPEND_LAST_ADMIN;
                } else if ("team_license_limit".equals(str)) {
                    membersSuspendError = MembersSuspendError.TEAM_LICENSE_LIMIT;
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
                return membersSuspendError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
