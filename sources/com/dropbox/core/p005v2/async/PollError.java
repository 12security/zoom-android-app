package com.dropbox.core.p005v2.async;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.async.PollError */
public enum PollError {
    INVALID_ASYNC_JOB_ID,
    INTERNAL_ERROR,
    OTHER;

    /* renamed from: com.dropbox.core.v2.async.PollError$Serializer */
    public static class Serializer extends UnionSerializer<PollError> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PollError pollError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (pollError) {
                case INVALID_ASYNC_JOB_ID:
                    jsonGenerator.writeString("invalid_async_job_id");
                    return;
                case INTERNAL_ERROR:
                    jsonGenerator.writeString("internal_error");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PollError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PollError pollError;
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
                if ("invalid_async_job_id".equals(str)) {
                    pollError = PollError.INVALID_ASYNC_JOB_ID;
                } else if ("internal_error".equals(str)) {
                    pollError = PollError.INTERNAL_ERROR;
                } else {
                    pollError = PollError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return pollError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
