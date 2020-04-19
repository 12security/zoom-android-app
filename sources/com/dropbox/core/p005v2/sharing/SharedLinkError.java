package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.SharedLinkError */
public enum SharedLinkError {
    SHARED_LINK_NOT_FOUND,
    SHARED_LINK_ACCESS_DENIED,
    UNSUPPORTED_LINK_TYPE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.SharedLinkError$Serializer */
    static class Serializer extends UnionSerializer<SharedLinkError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedLinkError sharedLinkError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedLinkError) {
                case SHARED_LINK_NOT_FOUND:
                    jsonGenerator.writeString("shared_link_not_found");
                    return;
                case SHARED_LINK_ACCESS_DENIED:
                    jsonGenerator.writeString("shared_link_access_denied");
                    return;
                case UNSUPPORTED_LINK_TYPE:
                    jsonGenerator.writeString("unsupported_link_type");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedLinkError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedLinkError sharedLinkError;
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
                if ("shared_link_not_found".equals(str)) {
                    sharedLinkError = SharedLinkError.SHARED_LINK_NOT_FOUND;
                } else if ("shared_link_access_denied".equals(str)) {
                    sharedLinkError = SharedLinkError.SHARED_LINK_ACCESS_DENIED;
                } else if ("unsupported_link_type".equals(str)) {
                    sharedLinkError = SharedLinkError.UNSUPPORTED_LINK_TYPE;
                } else {
                    sharedLinkError = SharedLinkError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedLinkError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
