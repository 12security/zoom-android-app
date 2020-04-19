package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxFolder;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.PendingUploadMode */
public enum PendingUploadMode {
    FILE,
    FOLDER;

    /* renamed from: com.dropbox.core.v2.sharing.PendingUploadMode$Serializer */
    static class Serializer extends UnionSerializer<PendingUploadMode> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PendingUploadMode pendingUploadMode, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (pendingUploadMode) {
                case FILE:
                    jsonGenerator.writeString(BoxFile.TYPE);
                    return;
                case FOLDER:
                    jsonGenerator.writeString(BoxFolder.TYPE);
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(pendingUploadMode);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public PendingUploadMode deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PendingUploadMode pendingUploadMode;
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
                    pendingUploadMode = PendingUploadMode.FILE;
                } else if (BoxFolder.TYPE.equals(str)) {
                    pendingUploadMode = PendingUploadMode.FOLDER;
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
                return pendingUploadMode;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
