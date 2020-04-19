package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxSharedLink;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.LoginMethod */
public enum LoginMethod {
    PASSWORD,
    TWO_FACTOR_AUTHENTICATION,
    SAML,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.LoginMethod$Serializer */
    static class Serializer extends UnionSerializer<LoginMethod> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(LoginMethod loginMethod, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (loginMethod) {
                case PASSWORD:
                    jsonGenerator.writeString(BoxSharedLink.FIELD_PASSWORD);
                    return;
                case TWO_FACTOR_AUTHENTICATION:
                    jsonGenerator.writeString("two_factor_authentication");
                    return;
                case SAML:
                    jsonGenerator.writeString("saml");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public LoginMethod deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LoginMethod loginMethod;
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
                if (BoxSharedLink.FIELD_PASSWORD.equals(str)) {
                    loginMethod = LoginMethod.PASSWORD;
                } else if ("two_factor_authentication".equals(str)) {
                    loginMethod = LoginMethod.TWO_FACTOR_AUTHENTICATION;
                } else if ("saml".equals(str)) {
                    loginMethod = LoginMethod.SAML;
                } else {
                    loginMethod = LoginMethod.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return loginMethod;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
