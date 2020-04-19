package com.dropbox.core;

import com.dropbox.core.stone.StoneSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

final class ApiErrorResponse<T> {
    private final T error;
    private LocalizedText userMessage;

    static final class Serializer<T> extends StoneSerializer<ApiErrorResponse<T>> {
        private StoneSerializer<T> errSerializer;

        public Serializer(StoneSerializer<T> stoneSerializer) {
            this.errSerializer = stoneSerializer;
        }

        public void serialize(ApiErrorResponse<T> apiErrorResponse, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            throw new UnsupportedOperationException("Error wrapper serialization not supported.");
        }

        public ApiErrorResponse<T> deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            expectStartObject(jsonParser);
            Object obj = null;
            LocalizedText localizedText = null;
            while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                String currentName = jsonParser.getCurrentName();
                jsonParser.nextToken();
                if ("error".equals(currentName)) {
                    obj = this.errSerializer.deserialize(jsonParser);
                } else if ("user_message".equals(currentName)) {
                    localizedText = (LocalizedText) LocalizedText.STONE_SERIALIZER.deserialize(jsonParser);
                } else {
                    skipValue(jsonParser);
                }
            }
            if (obj != null) {
                ApiErrorResponse<T> apiErrorResponse = new ApiErrorResponse<>(obj, localizedText);
                expectEndObject(jsonParser);
                return apiErrorResponse;
            }
            throw new JsonParseException(jsonParser, "Required field \"error\" missing.");
        }
    }

    public ApiErrorResponse(T t, LocalizedText localizedText) {
        if (t != null) {
            this.error = t;
            this.userMessage = localizedText;
            return;
        }
        throw new NullPointerException("error");
    }

    public T getError() {
        return this.error;
    }

    public LocalizedText getUserMessage() {
        return this.userMessage;
    }
}
