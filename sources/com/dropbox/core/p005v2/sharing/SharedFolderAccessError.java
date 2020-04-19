package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.SharedFolderAccessError */
public enum SharedFolderAccessError {
    INVALID_ID,
    NOT_A_MEMBER,
    EMAIL_UNVERIFIED,
    UNMOUNTED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.SharedFolderAccessError$Serializer */
    static class Serializer extends UnionSerializer<SharedFolderAccessError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedFolderAccessError sharedFolderAccessError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedFolderAccessError) {
                case INVALID_ID:
                    jsonGenerator.writeString("invalid_id");
                    return;
                case NOT_A_MEMBER:
                    jsonGenerator.writeString("not_a_member");
                    return;
                case EMAIL_UNVERIFIED:
                    jsonGenerator.writeString("email_unverified");
                    return;
                case UNMOUNTED:
                    jsonGenerator.writeString("unmounted");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedFolderAccessError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedFolderAccessError sharedFolderAccessError;
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
                if ("invalid_id".equals(str)) {
                    sharedFolderAccessError = SharedFolderAccessError.INVALID_ID;
                } else if ("not_a_member".equals(str)) {
                    sharedFolderAccessError = SharedFolderAccessError.NOT_A_MEMBER;
                } else if ("email_unverified".equals(str)) {
                    sharedFolderAccessError = SharedFolderAccessError.EMAIL_UNVERIFIED;
                } else if ("unmounted".equals(str)) {
                    sharedFolderAccessError = SharedFolderAccessError.UNMOUNTED;
                } else {
                    sharedFolderAccessError = SharedFolderAccessError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedFolderAccessError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
