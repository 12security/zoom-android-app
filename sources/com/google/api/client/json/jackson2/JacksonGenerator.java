package com.google.api.client.json.jackson2;

import com.google.api.client.json.JsonGenerator;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

final class JacksonGenerator extends JsonGenerator {
    private final JacksonFactory factory;
    private final com.fasterxml.jackson.core.JsonGenerator generator;

    public JacksonFactory getFactory() {
        return this.factory;
    }

    JacksonGenerator(JacksonFactory jacksonFactory, com.fasterxml.jackson.core.JsonGenerator jsonGenerator) {
        this.factory = jacksonFactory;
        this.generator = jsonGenerator;
    }

    public void flush() throws IOException {
        this.generator.flush();
    }

    public void close() throws IOException {
        this.generator.close();
    }

    public void writeBoolean(boolean z) throws IOException {
        this.generator.writeBoolean(z);
    }

    public void writeEndArray() throws IOException {
        this.generator.writeEndArray();
    }

    public void writeEndObject() throws IOException {
        this.generator.writeEndObject();
    }

    public void writeFieldName(String str) throws IOException {
        this.generator.writeFieldName(str);
    }

    public void writeNull() throws IOException {
        this.generator.writeNull();
    }

    public void writeNumber(int i) throws IOException {
        this.generator.writeNumber(i);
    }

    public void writeNumber(long j) throws IOException {
        this.generator.writeNumber(j);
    }

    public void writeNumber(BigInteger bigInteger) throws IOException {
        this.generator.writeNumber(bigInteger);
    }

    public void writeNumber(double d) throws IOException {
        this.generator.writeNumber(d);
    }

    public void writeNumber(float f) throws IOException {
        this.generator.writeNumber(f);
    }

    public void writeNumber(BigDecimal bigDecimal) throws IOException {
        this.generator.writeNumber(bigDecimal);
    }

    public void writeNumber(String str) throws IOException {
        this.generator.writeNumber(str);
    }

    public void writeStartArray() throws IOException {
        this.generator.writeStartArray();
    }

    public void writeStartObject() throws IOException {
        this.generator.writeStartObject();
    }

    public void writeString(String str) throws IOException {
        this.generator.writeString(str);
    }

    public void enablePrettyPrint() throws IOException {
        this.generator.useDefaultPrettyPrinter();
    }
}
