package com.dropbox.core.p005v2.auth;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.auth.RateLimitReason */
public enum RateLimitReason {
    TOO_MANY_REQUESTS,
    TOO_MANY_WRITE_OPERATIONS,
    OTHER;

    /* renamed from: com.dropbox.core.v2.auth.RateLimitReason$Serializer */
    public static class Serializer extends UnionSerializer<RateLimitReason> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(RateLimitReason rateLimitReason, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (rateLimitReason) {
                case TOO_MANY_REQUESTS:
                    jsonGenerator.writeString("too_many_requests");
                    return;
                case TOO_MANY_WRITE_OPERATIONS:
                    jsonGenerator.writeString("too_many_write_operations");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RateLimitReason deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RateLimitReason rateLimitReason;
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
                if ("too_many_requests".equals(str)) {
                    rateLimitReason = RateLimitReason.TOO_MANY_REQUESTS;
                } else if ("too_many_write_operations".equals(str)) {
                    rateLimitReason = RateLimitReason.TOO_MANY_WRITE_OPERATIONS;
                } else {
                    rateLimitReason = RateLimitReason.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return rateLimitReason;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
