package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.SharingFileAccessError */
public enum SharingFileAccessError {
    NO_PERMISSION,
    INVALID_FILE,
    IS_FOLDER,
    INSIDE_PUBLIC_FOLDER,
    INSIDE_OSX_PACKAGE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.SharingFileAccessError$Serializer */
    static class Serializer extends UnionSerializer<SharingFileAccessError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharingFileAccessError sharingFileAccessError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharingFileAccessError) {
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                case INVALID_FILE:
                    jsonGenerator.writeString("invalid_file");
                    return;
                case IS_FOLDER:
                    jsonGenerator.writeString("is_folder");
                    return;
                case INSIDE_PUBLIC_FOLDER:
                    jsonGenerator.writeString("inside_public_folder");
                    return;
                case INSIDE_OSX_PACKAGE:
                    jsonGenerator.writeString("inside_osx_package");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharingFileAccessError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharingFileAccessError sharingFileAccessError;
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
                if ("no_permission".equals(str)) {
                    sharingFileAccessError = SharingFileAccessError.NO_PERMISSION;
                } else if ("invalid_file".equals(str)) {
                    sharingFileAccessError = SharingFileAccessError.INVALID_FILE;
                } else if ("is_folder".equals(str)) {
                    sharingFileAccessError = SharingFileAccessError.IS_FOLDER;
                } else if ("inside_public_folder".equals(str)) {
                    sharingFileAccessError = SharingFileAccessError.INSIDE_PUBLIC_FOLDER;
                } else if ("inside_osx_package".equals(str)) {
                    sharingFileAccessError = SharingFileAccessError.INSIDE_OSX_PACKAGE;
                } else {
                    sharingFileAccessError = SharingFileAccessError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharingFileAccessError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
