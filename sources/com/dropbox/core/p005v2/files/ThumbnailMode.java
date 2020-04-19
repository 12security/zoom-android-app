package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.ThumbnailMode */
public enum ThumbnailMode {
    STRICT,
    BESTFIT,
    FITONE_BESTFIT;

    /* renamed from: com.dropbox.core.v2.files.ThumbnailMode$Serializer */
    static class Serializer extends UnionSerializer<ThumbnailMode> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ThumbnailMode thumbnailMode, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (thumbnailMode) {
                case STRICT:
                    jsonGenerator.writeString("strict");
                    return;
                case BESTFIT:
                    jsonGenerator.writeString("bestfit");
                    return;
                case FITONE_BESTFIT:
                    jsonGenerator.writeString("fitone_bestfit");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(thumbnailMode);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ThumbnailMode deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ThumbnailMode thumbnailMode;
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
                if ("strict".equals(str)) {
                    thumbnailMode = ThumbnailMode.STRICT;
                } else if ("bestfit".equals(str)) {
                    thumbnailMode = ThumbnailMode.BESTFIT;
                } else if ("fitone_bestfit".equals(str)) {
                    thumbnailMode = ThumbnailMode.FITONE_BESTFIT;
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
                return thumbnailMode;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
