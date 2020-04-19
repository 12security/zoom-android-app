package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.GetSharedLinkFileError */
public enum GetSharedLinkFileError {
    SHARED_LINK_NOT_FOUND,
    SHARED_LINK_ACCESS_DENIED,
    UNSUPPORTED_LINK_TYPE,
    OTHER,
    SHARED_LINK_IS_DIRECTORY;

    /* renamed from: com.dropbox.core.v2.sharing.GetSharedLinkFileError$Serializer */
    static class Serializer extends UnionSerializer<GetSharedLinkFileError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GetSharedLinkFileError getSharedLinkFileError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (getSharedLinkFileError) {
                case SHARED_LINK_NOT_FOUND:
                    jsonGenerator.writeString("shared_link_not_found");
                    return;
                case SHARED_LINK_ACCESS_DENIED:
                    jsonGenerator.writeString("shared_link_access_denied");
                    return;
                case UNSUPPORTED_LINK_TYPE:
                    jsonGenerator.writeString("unsupported_link_type");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SHARED_LINK_IS_DIRECTORY:
                    jsonGenerator.writeString("shared_link_is_directory");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(getSharedLinkFileError);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GetSharedLinkFileError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GetSharedLinkFileError getSharedLinkFileError;
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
                    getSharedLinkFileError = GetSharedLinkFileError.SHARED_LINK_NOT_FOUND;
                } else if ("shared_link_access_denied".equals(str)) {
                    getSharedLinkFileError = GetSharedLinkFileError.SHARED_LINK_ACCESS_DENIED;
                } else if ("unsupported_link_type".equals(str)) {
                    getSharedLinkFileError = GetSharedLinkFileError.UNSUPPORTED_LINK_TYPE;
                } else if ("other".equals(str)) {
                    getSharedLinkFileError = GetSharedLinkFileError.OTHER;
                } else if ("shared_link_is_directory".equals(str)) {
                    getSharedLinkFileError = GetSharedLinkFileError.SHARED_LINK_IS_DIRECTORY;
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
                return getSharedLinkFileError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
