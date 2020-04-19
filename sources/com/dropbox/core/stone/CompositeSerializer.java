package com.dropbox.core.stone;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

public abstract class CompositeSerializer<T> extends StoneSerializer<T> {
    protected static final String TAG_FIELD = ".tag";

    protected static boolean hasTag(JsonParser jsonParser) throws IOException, JsonParseException {
        return jsonParser.getCurrentToken() == JsonToken.FIELD_NAME && TAG_FIELD.equals(jsonParser.getCurrentName());
    }

    protected static String readTag(JsonParser jsonParser) throws IOException, JsonParseException {
        if (!hasTag(jsonParser)) {
            return null;
        }
        jsonParser.nextToken();
        String stringValue = getStringValue(jsonParser);
        jsonParser.nextToken();
        return stringValue;
    }

    /* access modifiers changed from: protected */
    public void writeTag(String str, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
        if (str != null) {
            jsonGenerator.writeStringField(TAG_FIELD, str);
        }
    }
}
