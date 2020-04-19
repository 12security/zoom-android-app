package com.fasterxml.jackson.core.p006io;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/* renamed from: com.fasterxml.jackson.core.io.JsonEOFException */
public class JsonEOFException extends JsonParseException {
    private static final long serialVersionUID = 1;
    protected final JsonToken _token;

    public JsonEOFException(JsonParser jsonParser, JsonToken jsonToken, String str) {
        super(jsonParser, str);
        this._token = jsonToken;
    }

    public JsonToken getTokenBeingDecoded() {
        return this._token;
    }
}
