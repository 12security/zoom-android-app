package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.AdminRole */
public enum AdminRole {
    TEAM_ADMIN,
    USER_MANAGEMENT_ADMIN,
    SUPPORT_ADMIN,
    LIMITED_ADMIN,
    MEMBER_ONLY,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.AdminRole$Serializer */
    static class Serializer extends UnionSerializer<AdminRole> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AdminRole adminRole, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (adminRole) {
                case TEAM_ADMIN:
                    jsonGenerator.writeString("team_admin");
                    return;
                case USER_MANAGEMENT_ADMIN:
                    jsonGenerator.writeString("user_management_admin");
                    return;
                case SUPPORT_ADMIN:
                    jsonGenerator.writeString("support_admin");
                    return;
                case LIMITED_ADMIN:
                    jsonGenerator.writeString("limited_admin");
                    return;
                case MEMBER_ONLY:
                    jsonGenerator.writeString("member_only");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AdminRole deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AdminRole adminRole;
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
                if ("team_admin".equals(str)) {
                    adminRole = AdminRole.TEAM_ADMIN;
                } else if ("user_management_admin".equals(str)) {
                    adminRole = AdminRole.USER_MANAGEMENT_ADMIN;
                } else if ("support_admin".equals(str)) {
                    adminRole = AdminRole.SUPPORT_ADMIN;
                } else if ("limited_admin".equals(str)) {
                    adminRole = AdminRole.LIMITED_ADMIN;
                } else if ("member_only".equals(str)) {
                    adminRole = AdminRole.MEMBER_ONLY;
                } else {
                    adminRole = AdminRole.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return adminRole;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
