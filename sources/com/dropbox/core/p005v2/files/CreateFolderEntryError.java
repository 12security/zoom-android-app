package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.CreateFolderEntryError */
public final class CreateFolderEntryError {
    public static final CreateFolderEntryError OTHER = new CreateFolderEntryError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public WriteError pathValue;

    /* renamed from: com.dropbox.core.v2.files.CreateFolderEntryError$Serializer */
    static class Serializer extends UnionSerializer<CreateFolderEntryError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateFolderEntryError createFolderEntryError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06601.$SwitchMap$com$dropbox$core$v2$files$CreateFolderEntryError$Tag[createFolderEntryError.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("path", jsonGenerator);
            jsonGenerator.writeFieldName("path");
            Serializer.INSTANCE.serialize(createFolderEntryError.pathValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public CreateFolderEntryError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            CreateFolderEntryError createFolderEntryError;
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
                    createFolderEntryError = CreateFolderEntryError.path(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    createFolderEntryError = CreateFolderEntryError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return createFolderEntryError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CreateFolderEntryError$Tag */
    public enum Tag {
        PATH,
        OTHER
    }

    private CreateFolderEntryError() {
    }

    private CreateFolderEntryError withTag(Tag tag) {
        CreateFolderEntryError createFolderEntryError = new CreateFolderEntryError();
        createFolderEntryError._tag = tag;
        return createFolderEntryError;
    }

    private CreateFolderEntryError withTagAndPath(Tag tag, WriteError writeError) {
        CreateFolderEntryError createFolderEntryError = new CreateFolderEntryError();
        createFolderEntryError._tag = tag;
        createFolderEntryError.pathValue = writeError;
        return createFolderEntryError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static CreateFolderEntryError path(WriteError writeError) {
        if (writeError != null) {
            return new CreateFolderEntryError().withTagAndPath(Tag.PATH, writeError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WriteError getPathValue() {
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
        if (obj == null || !(obj instanceof CreateFolderEntryError)) {
            return false;
        }
        CreateFolderEntryError createFolderEntryError = (CreateFolderEntryError) obj;
        if (this._tag != createFolderEntryError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                WriteError writeError = this.pathValue;
                WriteError writeError2 = createFolderEntryError.pathValue;
                if (writeError != writeError2 && !writeError.equals(writeError2)) {
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
