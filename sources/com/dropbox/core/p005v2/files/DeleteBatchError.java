package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.DeleteBatchError */
public enum DeleteBatchError {
    TOO_MANY_WRITE_OPERATIONS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchError$1 */
    static /* synthetic */ class C06621 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$files$DeleteBatchError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$files$DeleteBatchError = new int[DeleteBatchError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$files$DeleteBatchError[DeleteBatchError.TOO_MANY_WRITE_OPERATIONS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchError$Serializer */
    static class Serializer extends UnionSerializer<DeleteBatchError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(DeleteBatchError deleteBatchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06621.$SwitchMap$com$dropbox$core$v2$files$DeleteBatchError[deleteBatchError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("too_many_write_operations");
            }
        }

        public DeleteBatchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DeleteBatchError deleteBatchError;
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
                if ("too_many_write_operations".equals(str)) {
                    deleteBatchError = DeleteBatchError.TOO_MANY_WRITE_OPERATIONS;
                } else {
                    deleteBatchError = DeleteBatchError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return deleteBatchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
