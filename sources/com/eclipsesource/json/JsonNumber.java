package com.eclipsesource.json;

import java.io.IOException;

class JsonNumber extends JsonValue {
    private final String string;

    public boolean isNumber() {
        return true;
    }

    JsonNumber(String str) {
        if (str != null) {
            this.string = str;
            return;
        }
        throw new NullPointerException("string is null");
    }

    public String toString() {
        return this.string;
    }

    /* access modifiers changed from: protected */
    public void write(JsonWriter jsonWriter) throws IOException {
        jsonWriter.write(this.string);
    }

    public int asInt() {
        return Integer.parseInt(this.string, 10);
    }

    public long asLong() {
        return Long.parseLong(this.string, 10);
    }

    public float asFloat() {
        return Float.parseFloat(this.string);
    }

    public double asDouble() {
        return Double.parseDouble(this.string);
    }

    public int hashCode() {
        return this.string.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.string.equals(((JsonNumber) obj).string);
    }
}
