package com.eclipsesource.json;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

public abstract class JsonValue implements Serializable {
    public static final JsonValue FALSE = new JsonLiteral("false");
    public static final JsonValue NULL = new JsonLiteral("null");
    public static final JsonValue TRUE = new JsonLiteral("true");

    public boolean isArray() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isFalse() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isTrue() {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract void write(JsonWriter jsonWriter) throws IOException;

    JsonValue() {
    }

    public static JsonValue readFrom(Reader reader) throws IOException {
        return new JsonParser(reader).parse();
    }

    public static JsonValue readFrom(String str) {
        try {
            return new JsonParser(str).parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonValue valueOf(int i) {
        return new JsonNumber(Integer.toString(i, 10));
    }

    public static JsonValue valueOf(long j) {
        return new JsonNumber(Long.toString(j, 10));
    }

    public static JsonValue valueOf(float f) {
        if (!Float.isInfinite(f) && !Float.isNaN(f)) {
            return new JsonNumber(cutOffPointZero(Float.toString(f)));
        }
        throw new IllegalArgumentException("Infinite and NaN values not permitted in JSON");
    }

    public static JsonValue valueOf(double d) {
        if (!Double.isInfinite(d) && !Double.isNaN(d)) {
            return new JsonNumber(cutOffPointZero(Double.toString(d)));
        }
        throw new IllegalArgumentException("Infinite and NaN values not permitted in JSON");
    }

    public static JsonValue valueOf(String str) {
        return str == null ? NULL : new JsonString(str);
    }

    public static JsonValue valueOf(boolean z) {
        return z ? TRUE : FALSE;
    }

    public JsonObject asObject() {
        StringBuilder sb = new StringBuilder();
        sb.append("Not an object: ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public JsonArray asArray() {
        StringBuilder sb = new StringBuilder();
        sb.append("Not an array: ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public int asInt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Not a number: ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public long asLong() {
        StringBuilder sb = new StringBuilder();
        sb.append("Not a number: ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public float asFloat() {
        StringBuilder sb = new StringBuilder();
        sb.append("Not a number: ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public double asDouble() {
        StringBuilder sb = new StringBuilder();
        sb.append("Not a number: ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public String asString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Not a string: ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public boolean asBoolean() {
        StringBuilder sb = new StringBuilder();
        sb.append("Not a boolean: ");
        sb.append(toString());
        throw new UnsupportedOperationException(sb.toString());
    }

    public void writeTo(Writer writer) throws IOException {
        write(new JsonWriter(writer));
    }

    public String toString() {
        StringWriter stringWriter = new StringWriter();
        try {
            write(new JsonWriter(stringWriter));
            return stringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    private static String cutOffPointZero(String str) {
        return str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
    }
}
