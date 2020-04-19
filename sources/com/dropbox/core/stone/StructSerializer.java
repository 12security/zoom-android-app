package com.dropbox.core.stone;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

public abstract class StructSerializer<T> extends CompositeSerializer<T> {
    public abstract T deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException;

    public abstract void serialize(T t, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException;

    public void serialize(T t, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        serialize(t, jsonGenerator, false);
    }

    public T deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
        return deserialize(jsonParser, false);
    }
}
