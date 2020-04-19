package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.ThumbnailFormat */
public enum ThumbnailFormat {
    JPEG,
    PNG;

    /* renamed from: com.dropbox.core.v2.files.ThumbnailFormat$Serializer */
    static class Serializer extends UnionSerializer<ThumbnailFormat> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ThumbnailFormat thumbnailFormat, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (thumbnailFormat) {
                case JPEG:
                    jsonGenerator.writeString("jpeg");
                    return;
                case PNG:
                    jsonGenerator.writeString("png");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(thumbnailFormat);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ThumbnailFormat deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ThumbnailFormat thumbnailFormat;
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
                if ("jpeg".equals(str)) {
                    thumbnailFormat = ThumbnailFormat.JPEG;
                } else if ("png".equals(str)) {
                    thumbnailFormat = ThumbnailFormat.PNG;
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
                return thumbnailFormat;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
