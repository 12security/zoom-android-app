package com.dropbox.core.stone;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public abstract class StoneSerializer<T> {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public abstract T deserialize(JsonParser jsonParser) throws IOException, JsonParseException;

    public abstract void serialize(T t, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException;

    public String serialize(T t) {
        return serialize(t, false);
    }

    public String serialize(T t, boolean z) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            serialize(t, byteArrayOutputStream, z);
            return new String(byteArrayOutputStream.toByteArray(), UTF8);
        } catch (JsonGenerationException e) {
            throw new IllegalStateException("Impossible JSON exception", e);
        } catch (IOException e2) {
            throw new IllegalStateException("Impossible I/O exception", e2);
        }
    }

    public void serialize(T t, OutputStream outputStream) throws IOException {
        serialize(t, outputStream, false);
    }

    public void serialize(T t, OutputStream outputStream, boolean z) throws IOException {
        JsonGenerator createGenerator = Util.JSON.createGenerator(outputStream);
        if (z) {
            createGenerator.useDefaultPrettyPrinter();
        }
        try {
            serialize(t, createGenerator);
            createGenerator.flush();
        } catch (JsonGenerationException e) {
            throw new IllegalStateException("Impossible JSON generation exception", e);
        }
    }

    public T deserialize(String str) throws JsonParseException {
        try {
            JsonParser createParser = Util.JSON.createParser(str);
            createParser.nextToken();
            return deserialize(createParser);
        } catch (JsonParseException e) {
            throw e;
        } catch (IOException e2) {
            throw new IllegalStateException("Impossible I/O exception", e2);
        }
    }

    public T deserialize(InputStream inputStream) throws IOException, JsonParseException {
        JsonParser createParser = Util.JSON.createParser(inputStream);
        createParser.nextToken();
        return deserialize(createParser);
    }

    protected static String getStringValue(JsonParser jsonParser) throws IOException, JsonParseException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            return jsonParser.getText();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("expected string value, but was ");
        sb.append(jsonParser.getCurrentToken());
        throw new JsonParseException(jsonParser, sb.toString());
    }

    protected static void expectField(String str, JsonParser jsonParser) throws IOException, JsonParseException {
        if (jsonParser.getCurrentToken() != JsonToken.FIELD_NAME) {
            StringBuilder sb = new StringBuilder();
            sb.append("expected field name, but was: ");
            sb.append(jsonParser.getCurrentToken());
            throw new JsonParseException(jsonParser, sb.toString());
        } else if (str.equals(jsonParser.getCurrentName())) {
            jsonParser.nextToken();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("expected field '");
            sb2.append(str);
            sb2.append("', but was: '");
            sb2.append(jsonParser.getCurrentName());
            sb2.append("'");
            throw new JsonParseException(jsonParser, sb2.toString());
        }
    }

    protected static void expectStartObject(JsonParser jsonParser) throws IOException, JsonParseException {
        if (jsonParser.getCurrentToken() == JsonToken.START_OBJECT) {
            jsonParser.nextToken();
            return;
        }
        throw new JsonParseException(jsonParser, "expected object value.");
    }

    protected static void expectEndObject(JsonParser jsonParser) throws IOException, JsonParseException {
        if (jsonParser.getCurrentToken() == JsonToken.END_OBJECT) {
            jsonParser.nextToken();
            return;
        }
        throw new JsonParseException(jsonParser, "expected end of object value.");
    }

    protected static void expectStartArray(JsonParser jsonParser) throws IOException, JsonParseException {
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            jsonParser.nextToken();
            return;
        }
        throw new JsonParseException(jsonParser, "expected array value.");
    }

    protected static void expectEndArray(JsonParser jsonParser) throws IOException, JsonParseException {
        if (jsonParser.getCurrentToken() == JsonToken.END_ARRAY) {
            jsonParser.nextToken();
            return;
        }
        throw new JsonParseException(jsonParser, "expected end of array value.");
    }

    protected static void skipValue(JsonParser jsonParser) throws IOException, JsonParseException {
        if (jsonParser.getCurrentToken().isStructStart()) {
            jsonParser.skipChildren();
            jsonParser.nextToken();
        } else if (jsonParser.getCurrentToken().isScalarValue()) {
            jsonParser.nextToken();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't skip JSON value token: ");
            sb.append(jsonParser.getCurrentToken());
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    protected static void skipFields(JsonParser jsonParser) throws IOException, JsonParseException {
        while (jsonParser.getCurrentToken() != null && !jsonParser.getCurrentToken().isStructEnd()) {
            if (jsonParser.getCurrentToken().isStructStart()) {
                jsonParser.skipChildren();
            } else if (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                jsonParser.nextToken();
            } else if (jsonParser.getCurrentToken().isScalarValue()) {
                jsonParser.nextToken();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Can't skip token: ");
                sb.append(jsonParser.getCurrentToken());
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }
}
