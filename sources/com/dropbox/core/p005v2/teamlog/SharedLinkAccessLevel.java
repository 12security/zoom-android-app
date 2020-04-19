package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import p015io.reactivex.annotations.SchedulerSupport;

/* renamed from: com.dropbox.core.v2.teamlog.SharedLinkAccessLevel */
public enum SharedLinkAccessLevel {
    NONE,
    READER,
    WRITER,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedLinkAccessLevel$Serializer */
    static class Serializer extends UnionSerializer<SharedLinkAccessLevel> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(SharedLinkAccessLevel sharedLinkAccessLevel, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedLinkAccessLevel) {
                case NONE:
                    jsonGenerator.writeString(SchedulerSupport.NONE);
                    return;
                case READER:
                    jsonGenerator.writeString("reader");
                    return;
                case WRITER:
                    jsonGenerator.writeString("writer");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedLinkAccessLevel deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SharedLinkAccessLevel sharedLinkAccessLevel;
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
                if (SchedulerSupport.NONE.equals(str)) {
                    sharedLinkAccessLevel = SharedLinkAccessLevel.NONE;
                } else if ("reader".equals(str)) {
                    sharedLinkAccessLevel = SharedLinkAccessLevel.READER;
                } else if ("writer".equals(str)) {
                    sharedLinkAccessLevel = SharedLinkAccessLevel.WRITER;
                } else {
                    sharedLinkAccessLevel = SharedLinkAccessLevel.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedLinkAccessLevel;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
