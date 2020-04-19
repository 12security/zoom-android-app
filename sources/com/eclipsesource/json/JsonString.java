package com.eclipsesource.json;

import java.io.IOException;

class JsonString extends JsonValue {
    private final String string;

    public boolean isString() {
        return true;
    }

    JsonString(String str) {
        if (str != null) {
            this.string = str;
            return;
        }
        throw new NullPointerException("string is null");
    }

    /* access modifiers changed from: protected */
    public void write(JsonWriter jsonWriter) throws IOException {
        jsonWriter.writeString(this.string);
    }

    public String asString() {
        return this.string;
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
        return this.string.equals(((JsonString) obj).string);
    }
}
