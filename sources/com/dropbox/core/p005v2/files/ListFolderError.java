package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.ListFolderError */
public final class ListFolderError {
    public static final ListFolderError OTHER = new ListFolderError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.files.ListFolderError$Serializer */
    static class Serializer extends UnionSerializer<ListFolderError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFolderError listFolderError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06751.$SwitchMap$com$dropbox$core$v2$files$ListFolderError$Tag[listFolderError.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("path", jsonGenerator);
            jsonGenerator.writeFieldName("path");
            com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(listFolderError.pathValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public ListFolderError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFolderError listFolderError;
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
                    listFolderError = ListFolderError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    listFolderError = ListFolderError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFolderError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.ListFolderError$Tag */
    public enum Tag {
        PATH,
        OTHER
    }

    private ListFolderError() {
    }

    private ListFolderError withTag(Tag tag) {
        ListFolderError listFolderError = new ListFolderError();
        listFolderError._tag = tag;
        return listFolderError;
    }

    private ListFolderError withTagAndPath(Tag tag, LookupError lookupError) {
        ListFolderError listFolderError = new ListFolderError();
        listFolderError._tag = tag;
        listFolderError.pathValue = lookupError;
        return listFolderError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static ListFolderError path(LookupError lookupError) {
        if (lookupError != null) {
            return new ListFolderError().withTagAndPath(Tag.PATH, lookupError);
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
        if (obj == null || !(obj instanceof ListFolderError)) {
            return false;
        }
        ListFolderError listFolderError = (ListFolderError) obj;
        if (this._tag != listFolderError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = listFolderError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
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
