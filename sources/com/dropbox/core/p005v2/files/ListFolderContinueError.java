package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.ListFolderContinueError */
public final class ListFolderContinueError {
    public static final ListFolderContinueError OTHER = new ListFolderContinueError().withTag(Tag.OTHER);
    public static final ListFolderContinueError RESET = new ListFolderContinueError().withTag(Tag.RESET);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.files.ListFolderContinueError$Serializer */
    static class Serializer extends UnionSerializer<ListFolderContinueError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFolderContinueError listFolderContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listFolderContinueError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(listFolderContinueError.pathValue, jsonGenerator);
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

        public ListFolderContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFolderContinueError listFolderContinueError;
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
                    listFolderContinueError = ListFolderContinueError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("reset".equals(str)) {
                    listFolderContinueError = ListFolderContinueError.RESET;
                } else {
                    listFolderContinueError = ListFolderContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFolderContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.ListFolderContinueError$Tag */
    public enum Tag {
        PATH,
        RESET,
        OTHER
    }

    private ListFolderContinueError() {
    }

    private ListFolderContinueError withTag(Tag tag) {
        ListFolderContinueError listFolderContinueError = new ListFolderContinueError();
        listFolderContinueError._tag = tag;
        return listFolderContinueError;
    }

    private ListFolderContinueError withTagAndPath(Tag tag, LookupError lookupError) {
        ListFolderContinueError listFolderContinueError = new ListFolderContinueError();
        listFolderContinueError._tag = tag;
        listFolderContinueError.pathValue = lookupError;
        return listFolderContinueError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static ListFolderContinueError path(LookupError lookupError) {
        if (lookupError != null) {
            return new ListFolderContinueError().withTagAndPath(Tag.PATH, lookupError);
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
        if (obj == null || !(obj instanceof ListFolderContinueError)) {
            return false;
        }
        ListFolderContinueError listFolderContinueError = (ListFolderContinueError) obj;
        if (this._tag != listFolderContinueError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = listFolderContinueError.pathValue;
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
