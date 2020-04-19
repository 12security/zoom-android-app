package com.dropbox.core.p005v2.async;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.async.PollEmptyResult */
public enum PollEmptyResult {
    IN_PROGRESS,
    COMPLETE;

    /* renamed from: com.dropbox.core.v2.async.PollEmptyResult$Serializer */
    public static class Serializer extends UnionSerializer<PollEmptyResult> {
        public static final Serializer INSTANCE = null;

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(PollEmptyResult pollEmptyResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (pollEmptyResult) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeString("complete");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(pollEmptyResult);
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public PollEmptyResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PollEmptyResult pollEmptyResult;
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
                if ("in_progress".equals(str)) {
                    pollEmptyResult = PollEmptyResult.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    pollEmptyResult = PollEmptyResult.COMPLETE;
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
                return pollEmptyResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
