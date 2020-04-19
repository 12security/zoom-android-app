package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.SharedLinkSettingsError */
public enum SharedLinkSettingsError {
    INVALID_SETTINGS,
    NOT_AUTHORIZED;

    /* renamed from: com.dropbox.core.v2.sharing.SharedLinkSettingsError$Serializer */
    static class Serializer extends UnionSerializer<SharedLinkSettingsError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedLinkSettingsError sharedLinkSettingsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedLinkSettingsError) {
                case INVALID_SETTINGS:
                    jsonGenerator.writeString("invalid_settings");
                    return;
                case NOT_AUTHORIZED:
                    jsonGenerator.writeString("not_authorized");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(sharedLinkSettingsError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public SharedLinkSettingsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedLinkSettingsError sharedLinkSettingsError;
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
                if ("invalid_settings".equals(str)) {
                    sharedLinkSettingsError = SharedLinkSettingsError.INVALID_SETTINGS;
                } else if ("not_authorized".equals(str)) {
                    sharedLinkSettingsError = SharedLinkSettingsError.NOT_AUTHORIZED;
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
                return sharedLinkSettingsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
