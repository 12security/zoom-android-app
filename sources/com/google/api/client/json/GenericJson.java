package com.google.api.client.json;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Throwables;
import java.io.IOException;

public class GenericJson extends GenericData implements Cloneable {
    private JsonFactory jsonFactory;

    public final JsonFactory getFactory() {
        return this.jsonFactory;
    }

    public final void setFactory(JsonFactory jsonFactory2) {
        this.jsonFactory = jsonFactory2;
    }

    public String toString() {
        JsonFactory jsonFactory2 = this.jsonFactory;
        if (jsonFactory2 == null) {
            return super.toString();
        }
        try {
            return jsonFactory2.toString(this);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public String toPrettyString() throws IOException {
        JsonFactory jsonFactory2 = this.jsonFactory;
        if (jsonFactory2 != null) {
            return jsonFactory2.toPrettyString(this);
        }
        return super.toString();
    }

    public GenericJson clone() {
        return (GenericJson) super.clone();
    }

    public GenericJson set(String str, Object obj) {
        return (GenericJson) super.set(str, obj);
    }
}
