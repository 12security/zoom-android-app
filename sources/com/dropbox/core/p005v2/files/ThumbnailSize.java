package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.files.ThumbnailSize */
public enum ThumbnailSize {
    W32H32,
    W64H64,
    W128H128,
    W256H256,
    W480H320,
    W640H480,
    W960H640,
    W1024H768,
    W2048H1536;

    /* renamed from: com.dropbox.core.v2.files.ThumbnailSize$Serializer */
    static class Serializer extends UnionSerializer<ThumbnailSize> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(ThumbnailSize thumbnailSize, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (thumbnailSize) {
                case W32H32:
                    jsonGenerator.writeString("w32h32");
                    return;
                case W64H64:
                    jsonGenerator.writeString("w64h64");
                    return;
                case W128H128:
                    jsonGenerator.writeString("w128h128");
                    return;
                case W256H256:
                    jsonGenerator.writeString("w256h256");
                    return;
                case W480H320:
                    jsonGenerator.writeString("w480h320");
                    return;
                case W640H480:
                    jsonGenerator.writeString("w640h480");
                    return;
                case W960H640:
                    jsonGenerator.writeString("w960h640");
                    return;
                case W1024H768:
                    jsonGenerator.writeString("w1024h768");
                    return;
                case W2048H1536:
                    jsonGenerator.writeString("w2048h1536");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(thumbnailSize);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ThumbnailSize deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ThumbnailSize thumbnailSize;
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
                if ("w32h32".equals(str)) {
                    thumbnailSize = ThumbnailSize.W32H32;
                } else if ("w64h64".equals(str)) {
                    thumbnailSize = ThumbnailSize.W64H64;
                } else if ("w128h128".equals(str)) {
                    thumbnailSize = ThumbnailSize.W128H128;
                } else if ("w256h256".equals(str)) {
                    thumbnailSize = ThumbnailSize.W256H256;
                } else if ("w480h320".equals(str)) {
                    thumbnailSize = ThumbnailSize.W480H320;
                } else if ("w640h480".equals(str)) {
                    thumbnailSize = ThumbnailSize.W640H480;
                } else if ("w960h640".equals(str)) {
                    thumbnailSize = ThumbnailSize.W960H640;
                } else if ("w1024h768".equals(str)) {
                    thumbnailSize = ThumbnailSize.W1024H768;
                } else if ("w2048h1536".equals(str)) {
                    thumbnailSize = ThumbnailSize.W2048H1536;
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
                return thumbnailSize;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
