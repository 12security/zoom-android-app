package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.CreateFolderBatchError */
public enum CreateFolderBatchError {
    TOO_MANY_FILES,
    OTHER;

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchError$1 */
    static /* synthetic */ class C06561 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$files$CreateFolderBatchError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$files$CreateFolderBatchError = new int[CreateFolderBatchError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$files$CreateFolderBatchError[CreateFolderBatchError.TOO_MANY_FILES.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchError$Serializer */
    static class Serializer extends UnionSerializer<CreateFolderBatchError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(CreateFolderBatchError createFolderBatchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06561.$SwitchMap$com$dropbox$core$v2$files$CreateFolderBatchError[createFolderBatchError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("too_many_files");
            }
        }

        public CreateFolderBatchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            CreateFolderBatchError createFolderBatchError;
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
                if ("too_many_files".equals(str)) {
                    createFolderBatchError = CreateFolderBatchError.TOO_MANY_FILES;
                } else {
                    createFolderBatchError = CreateFolderBatchError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return createFolderBatchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
