package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.p005v2.files.LookupError;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ListSharedLinksError */
public final class ListSharedLinksError {
    public static final ListSharedLinksError OTHER = new ListSharedLinksError().withTag(Tag.OTHER);
    public static final ListSharedLinksError RESET = new ListSharedLinksError().withTag(Tag.RESET);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.sharing.ListSharedLinksError$Serializer */
    static class Serializer extends UnionSerializer<ListSharedLinksError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListSharedLinksError listSharedLinksError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listSharedLinksError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(listSharedLinksError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case RESET:
                    jsonGenerator.writeString("reset");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ListSharedLinksError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListSharedLinksError listSharedLinksError;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("path".equals(str)) {
                    expectField("path", jsonParser);
                    listSharedLinksError = ListSharedLinksError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("reset".equals(str)) {
                    listSharedLinksError = ListSharedLinksError.RESET;
                } else {
                    listSharedLinksError = ListSharedLinksError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listSharedLinksError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListSharedLinksError$Tag */
    public enum Tag {
        PATH,
        RESET,
        OTHER
    }

    private ListSharedLinksError() {
    }

    private ListSharedLinksError withTag(Tag tag) {
        ListSharedLinksError listSharedLinksError = new ListSharedLinksError();
        listSharedLinksError._tag = tag;
        return listSharedLinksError;
    }

    private ListSharedLinksError withTagAndPath(Tag tag, LookupError lookupError) {
        ListSharedLinksError listSharedLinksError = new ListSharedLinksError();
        listSharedLinksError._tag = tag;
        listSharedLinksError.pathValue = lookupError;
        return listSharedLinksError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static ListSharedLinksError path(LookupError lookupError) {
        if (lookupError != null) {
            return new ListSharedLinksError().withTagAndPath(Tag.PATH, lookupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookupError getPathValue() {
        if (this._tag == Tag.PATH) {
            return this.pathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isReset() {
        return this._tag == Tag.RESET;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ListSharedLinksError)) {
            return false;
        }
        ListSharedLinksError listSharedLinksError = (ListSharedLinksError) obj;
        if (this._tag != listSharedLinksError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = listSharedLinksError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case RESET:
                return true;
            case OTHER:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
