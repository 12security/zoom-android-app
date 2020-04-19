package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxFolder;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.WriteConflictError */
public enum WriteConflictError {
    FILE,
    FOLDER,
    FILE_ANCESTOR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.files.WriteConflictError$Serializer */
    static class Serializer extends UnionSerializer<WriteConflictError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(WriteConflictError writeConflictError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (writeConflictError) {
                case FILE:
                    jsonGenerator.writeString(BoxFile.TYPE);
                    return;
                case FOLDER:
                    jsonGenerator.writeString(BoxFolder.TYPE);
                    return;
                case FILE_ANCESTOR:
                    jsonGenerator.writeString("file_ancestor");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public WriteConflictError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            WriteConflictError writeConflictError;
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
                if (BoxFile.TYPE.equals(str)) {
                    writeConflictError = WriteConflictError.FILE;
                } else if (BoxFolder.TYPE.equals(str)) {
                    writeConflictError = WriteConflictError.FOLDER;
                } else if ("file_ancestor".equals(str)) {
                    writeConflictError = WriteConflictError.FILE_ANCESTOR;
                } else {
                    writeConflictError = WriteConflictError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return writeConflictError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
