package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.SharedLinkAccessFailureReason */
public enum SharedLinkAccessFailureReason {
    LOGIN_REQUIRED,
    EMAIL_VERIFY_REQUIRED,
    PASSWORD_REQUIRED,
    TEAM_ONLY,
    OWNER_ONLY,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.SharedLinkAccessFailureReason$Serializer */
    static class Serializer extends UnionSerializer<SharedLinkAccessFailureReason> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedLinkAccessFailureReason sharedLinkAccessFailureReason, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedLinkAccessFailureReason) {
                case LOGIN_REQUIRED:
                    jsonGenerator.writeString("login_required");
                    return;
                case EMAIL_VERIFY_REQUIRED:
                    jsonGenerator.writeString("email_verify_required");
                    return;
                case PASSWORD_REQUIRED:
                    jsonGenerator.writeString("password_required");
                    return;
                case TEAM_ONLY:
                    jsonGenerator.writeString("team_only");
                    return;
                case OWNER_ONLY:
                    jsonGenerator.writeString("owner_only");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedLinkAccessFailureReason deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedLinkAccessFailureReason sharedLinkAccessFailureReason;
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
                if ("login_required".equals(str)) {
                    sharedLinkAccessFailureReason = SharedLinkAccessFailureReason.LOGIN_REQUIRED;
                } else if ("email_verify_required".equals(str)) {
                    sharedLinkAccessFailureReason = SharedLinkAccessFailureReason.EMAIL_VERIFY_REQUIRED;
                } else if ("password_required".equals(str)) {
                    sharedLinkAccessFailureReason = SharedLinkAccessFailureReason.PASSWORD_REQUIRED;
                } else if ("team_only".equals(str)) {
                    sharedLinkAccessFailureReason = SharedLinkAccessFailureReason.TEAM_ONLY;
                } else if ("owner_only".equals(str)) {
                    sharedLinkAccessFailureReason = SharedLinkAccessFailureReason.OWNER_ONLY;
                } else {
                    sharedLinkAccessFailureReason = SharedLinkAccessFailureReason.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedLinkAccessFailureReason;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
