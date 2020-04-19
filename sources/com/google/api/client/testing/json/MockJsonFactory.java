package com.google.api.client.testing.json;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.util.Beta;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

@Beta
public class MockJsonFactory extends JsonFactory {
    public JsonParser createJsonParser(InputStream inputStream) throws IOException {
        return new MockJsonParser(this);
    }

    public JsonParser createJsonParser(InputStream inputStream, Charset charset) throws IOException {
        return new MockJsonParser(this);
    }

    public JsonParser createJsonParser(String str) throws IOException {
        return new MockJsonParser(this);
    }

    public JsonParser createJsonParser(Reader reader) throws IOException {
        return new MockJsonParser(this);
    }

    public JsonGenerator createJsonGenerator(OutputStream outputStream, Charset charset) throws IOException {
        return new MockJsonGenerator(this);
    }

    public JsonGenerator createJsonGenerator(Writer writer) throws IOException {
        return new MockJsonGenerator(this);
    }
}
