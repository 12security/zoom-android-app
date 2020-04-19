package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.team.AdminTier */
public enum AdminTier {
    TEAM_ADMIN,
    USER_MANAGEMENT_ADMIN,
    SUPPORT_ADMIN,
    MEMBER_ONLY;

    /* renamed from: com.dropbox.core.v2.team.AdminTier$Serializer */
    static class Serializer extends UnionSerializer<AdminTier> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(AdminTier adminTier, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (adminTier) {
                case TEAM_ADMIN:
                    jsonGenerator.writeString("team_admin");
                    return;
                case USER_MANAGEMENT_ADMIN:
                    jsonGenerator.writeString("user_management_admin");
                    return;
                case SUPPORT_ADMIN:
                    jsonGenerator.writeString("support_admin");
                    return;
                case MEMBER_ONLY:
                    jsonGenerator.writeString("member_only");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(adminTier);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public AdminTier deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AdminTier adminTier;
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
                    adminTier = AdminTier.TEAM_ADMIN;
                } else if ("user_management_admin".equals(str)) {
                    adminTier = AdminTier.USER_MANAGEMENT_ADMIN;
                } else if ("support_admin".equals(str)) {
                    adminTier = AdminTier.SUPPORT_ADMIN;
                } else if ("member_only".equals(str)) {
                    adminTier = AdminTier.MEMBER_ONLY;
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
                return adminTier;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
