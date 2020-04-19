package com.google.api.client.extensions.android.json;

import android.annotation.TargetApi;
import android.util.JsonWriter;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.util.Beta;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@Beta
@TargetApi(11)
class AndroidJsonGenerator extends JsonGenerator {
    private final AndroidJsonFactory factory;
    private final JsonWriter writer;

    static final class StringNumber extends Number {
        private static final long serialVersionUID = 1;
        private final String encodedValue;

        public double doubleValue() {
            return 0.0d;
        }

        public float floatValue() {
            return 0.0f;
        }

        public int intValue() {
            return 0;
        }

        public long longValue() {
            return 0;
        }

        StringNumber(String str) {
            this.encodedValue = str;
        }

        public String toString() {
            return this.encodedValue;
        }
    }

    AndroidJsonGenerator(AndroidJsonFactory androidJsonFactory, JsonWriter jsonWriter) {
        this.factory = androidJsonFactory;
        this.writer = jsonWriter;
        jsonWriter.setLenient(true);
    }

    public void flush() throws IOException {
        this.writer.flush();
    }

    public void close() throws IOException {
        this.writer.close();
    }

    public JsonFactory getFactory() {
        return this.factory;
    }

    public void writeBoolean(boolean z) throws IOException {
        this.writer.value(z);
    }

    public void writeEndArray() throws IOException {
        this.writer.endArray();
    }

    public void writeEndObject() throws IOException {
        this.writer.endObject();
    }

    public void writeFieldName(String str) throws IOException {
        this.writer.name(str);
    }

    public void writeNull() throws IOException {
        this.writer.nullValue();
    }

    public void writeNumber(int i) throws IOException {
        this.writer.value((long) i);
    }

    public void writeNumber(long j) throws IOException {
        this.writer.value(j);
    }

    public void writeNumber(BigInteger bigInteger) throws IOException {
        this.writer.value(bigInteger);
    }

    public void writeNumber(double d) throws IOException {
        this.writer.value(d);
    }

    public void writeNumber(float f) throws IOException {
        this.writer.value((double) f);
    }

    public void writeNumber(BigDecimal bigDecimal) throws IOException {
        this.writer.value(bigDecimal);
    }

    public void writeNumber(String str) throws IOException {
        this.writer.value(new StringNumber(str));
    }

    public void writeStartArray() throws IOException {
        this.writer.beginArray();
    }

    public void writeStartObject() throws IOException {
        this.writer.beginObject();
    }

    public void writeString(String str) throws IOException {
        this.writer.value(str);
    }

    public void enablePrettyPrint() throws IOException {
        this.writer.setIndent("  ");
    }
}
