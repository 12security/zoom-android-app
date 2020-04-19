package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.DeleteError */
public final class DeleteError {
    public static final DeleteError OTHER = new DeleteError().withTag(Tag.OTHER);
    public static final DeleteError TOO_MANY_FILES = new DeleteError().withTag(Tag.TOO_MANY_FILES);
    public static final DeleteError TOO_MANY_WRITE_OPERATIONS = new DeleteError().withTag(Tag.TOO_MANY_WRITE_OPERATIONS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathLookupValue;
    /* access modifiers changed from: private */
    public WriteError pathWriteValue;

    /* renamed from: com.dropbox.core.v2.files.DeleteError$Serializer */
    static class Serializer extends UnionSerializer<DeleteError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeleteError deleteError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (deleteError.tag()) {
                case PATH_LOOKUP:
                    jsonGenerator.writeStartObject();
                    writeTag("path_lookup", jsonGenerator);
                    jsonGenerator.writeFieldName("path_lookup");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(deleteError.pathLookupValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case PATH_WRITE:
                    jsonGenerator.writeStartObject();
                    writeTag("path_write", jsonGenerator);
                    jsonGenerator.writeFieldName("path_write");
                    Serializer.INSTANCE.serialize(deleteError.pathWriteValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TOO_MANY_WRITE_OPERATIONS:
                    jsonGenerator.writeString("too_many_write_operations");
                    return;
                case TOO_MANY_FILES:
                    jsonGenerator.writeString("too_many_files");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DeleteError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DeleteError deleteError;
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
                if ("path_lookup".equals(str)) {
                    expectField("path_lookup", jsonParser);
                    deleteError = DeleteError.pathLookup(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("path_write".equals(str)) {
                    expectField("path_write", jsonParser);
                    deleteError = DeleteError.pathWrite(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("too_many_write_operations".equals(str)) {
                    deleteError = DeleteError.TOO_MANY_WRITE_OPERATIONS;
                } else if ("too_many_files".equals(str)) {
                    deleteError = DeleteError.TOO_MANY_FILES;
                } else {
                    deleteError = DeleteError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return deleteError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.DeleteError$Tag */
    public enum Tag {
        PATH_LOOKUP,
        PATH_WRITE,
        TOO_MANY_WRITE_OPERATIONS,
        TOO_MANY_FILES,
        OTHER
    }

    private DeleteError() {
    }

    private DeleteError withTag(Tag tag) {
        DeleteError deleteError = new DeleteError();
        deleteError._tag = tag;
        return deleteError;
    }

    private DeleteError withTagAndPathLookup(Tag tag, LookupError lookupError) {
        DeleteError deleteError = new DeleteError();
        deleteError._tag = tag;
        deleteError.pathLookupValue = lookupError;
        return deleteError;
    }

    private DeleteError withTagAndPathWrite(Tag tag, WriteError writeError) {
        DeleteError deleteError = new DeleteError();
        deleteError._tag = tag;
        deleteError.pathWriteValue = writeError;
        return deleteError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPathLookup() {
        return this._tag == Tag.PATH_LOOKUP;
    }

    public static DeleteError pathLookup(LookupError lookupError) {
        if (lookupError != null) {
            return new DeleteError().withTagAndPathLookup(Tag.PATH_LOOKUP, lookupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookupError getPathLookupValue() {
        if (this._tag == Tag.PATH_LOOKUP) {
            return this.pathLookupValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH_LOOKUP, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isPathWrite() {
        return this._tag == Tag.PATH_WRITE;
    }

    public static DeleteError pathWrite(WriteError writeError) {
        if (writeError != null) {
            return new DeleteError().withTagAndPathWrite(Tag.PATH_WRITE, writeError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WriteError getPathWriteValue() {
        if (this._tag == Tag.PATH_WRITE) {
            return this.pathWriteValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH_WRITE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTooManyWriteOperations() {
        return this._tag == Tag.TOO_MANY_WRITE_OPERATIONS;
    }

    public boolean isTooManyFiles() {
        return this._tag == Tag.TOO_MANY_FILES;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathLookupValue, this.pathWriteValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof DeleteError)) {
            return false;
        }
        DeleteError deleteError = (DeleteError) obj;
        if (this._tag != deleteError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH_LOOKUP:
                LookupError lookupError = this.pathLookupValue;
                LookupError lookupError2 = deleteError.pathLookupValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case PATH_WRITE:
                WriteError writeError = this.pathWriteValue;
                WriteError writeError2 = deleteError.pathWriteValue;
                if (writeError != writeError2 && !writeError.equals(writeError2)) {
                    z = false;
                }
                return z;
            case TOO_MANY_WRITE_OPERATIONS:
                return true;
            case TOO_MANY_FILES:
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
