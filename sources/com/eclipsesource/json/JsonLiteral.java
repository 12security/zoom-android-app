package com.eclipsesource.json;

import java.io.IOException;

class JsonLiteral extends JsonValue {
    private final String value;

    JsonLiteral(String str) {
        this.value = str;
    }

    /* access modifiers changed from: protected */
    public void write(JsonWriter jsonWriter) throws IOException {
        jsonWriter.write(this.value);
    }

    public String toString() {
        return this.value;
    }

    public boolean asBoolean() {
        return isBoolean() ? isTrue() : super.asBoolean();
    }

    public boolean isNull() {
        return this == NULL;
    }

    public boolean isBoolean() {
        return this == TRUE || this == FALSE;
    }

    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.value.equals(((JsonLiteral) obj).value);
    }
}
