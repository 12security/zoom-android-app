package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.GetThumbnailBatchError */
public enum GetThumbnailBatchError {
    TOO_MANY_FILES,
    OTHER;

    /* renamed from: com.dropbox.core.v2.files.GetThumbnailBatchError$1 */
    static /* synthetic */ class C06721 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$files$GetThumbnailBatchError = null;

        static {
            $SwitchMap$com$dropbox$core$v2$files$GetThumbnailBatchError = new int[GetThumbnailBatchError.values().length];
            try {
                $SwitchMap$com$dropbox$core$v2$files$GetThumbnailBatchError[GetThumbnailBatchError.TOO_MANY_FILES.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.files.GetThumbnailBatchError$Serializer */
    static class Serializer extends UnionSerializer<GetThumbnailBatchError> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(GetThumbnailBatchError getThumbnailBatchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06721.$SwitchMap$com$dropbox$core$v2$files$GetThumbnailBatchError[getThumbnailBatchError.ordinal()] != 1) {
                jsonGenerator.writeString("other");
            } else {
                jsonGenerator.writeString("too_many_files");
            }
        }

        public GetThumbnailBatchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GetThumbnailBatchError getThumbnailBatchError;
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
                    getThumbnailBatchError = GetThumbnailBatchError.TOO_MANY_FILES;
                } else {
                    getThumbnailBatchError = GetThumbnailBatchError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return getThumbnailBatchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
