package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.SaveCopyReferenceError */
public final class SaveCopyReferenceError {
    public static final SaveCopyReferenceError INVALID_COPY_REFERENCE = new SaveCopyReferenceError().withTag(Tag.INVALID_COPY_REFERENCE);
    public static final SaveCopyReferenceError NOT_FOUND = new SaveCopyReferenceError().withTag(Tag.NOT_FOUND);
    public static final SaveCopyReferenceError NO_PERMISSION = new SaveCopyReferenceError().withTag(Tag.NO_PERMISSION);
    public static final SaveCopyReferenceError OTHER = new SaveCopyReferenceError().withTag(Tag.OTHER);
    public static final SaveCopyReferenceError TOO_MANY_FILES = new SaveCopyReferenceError().withTag(Tag.TOO_MANY_FILES);
    private Tag _tag;
    /* access modifiers changed from: private */
    public WriteError pathValue;

    /* renamed from: com.dropbox.core.v2.files.SaveCopyReferenceError$Serializer */
    static class Serializer extends UnionSerializer<SaveCopyReferenceError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SaveCopyReferenceError saveCopyReferenceError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (saveCopyReferenceError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    Serializer.INSTANCE.serialize(saveCopyReferenceError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case INVALID_COPY_REFERENCE:
                    jsonGenerator.writeString("invalid_copy_reference");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                case NOT_FOUND:
                    jsonGenerator.writeString("not_found");
                    return;
                case TOO_MANY_FILES:
                    jsonGenerator.writeString("too_many_files");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SaveCopyReferenceError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SaveCopyReferenceError saveCopyReferenceError;
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
                    saveCopyReferenceError = SaveCopyReferenceError.path(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("invalid_copy_reference".equals(str)) {
                    saveCopyReferenceError = SaveCopyReferenceError.INVALID_COPY_REFERENCE;
                } else if ("no_permission".equals(str)) {
                    saveCopyReferenceError = SaveCopyReferenceError.NO_PERMISSION;
                } else if ("not_found".equals(str)) {
                    saveCopyReferenceError = SaveCopyReferenceError.NOT_FOUND;
                } else if ("too_many_files".equals(str)) {
                    saveCopyReferenceError = SaveCopyReferenceError.TOO_MANY_FILES;
                } else {
                    saveCopyReferenceError = SaveCopyReferenceError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return saveCopyReferenceError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.SaveCopyReferenceError$Tag */
    public enum Tag {
        PATH,
        INVALID_COPY_REFERENCE,
        NO_PERMISSION,
        NOT_FOUND,
        TOO_MANY_FILES,
        OTHER
    }

    private SaveCopyReferenceError() {
    }

    private SaveCopyReferenceError withTag(Tag tag) {
        SaveCopyReferenceError saveCopyReferenceError = new SaveCopyReferenceError();
        saveCopyReferenceError._tag = tag;
        return saveCopyReferenceError;
    }

    private SaveCopyReferenceError withTagAndPath(Tag tag, WriteError writeError) {
        SaveCopyReferenceError saveCopyReferenceError = new SaveCopyReferenceError();
        saveCopyReferenceError._tag = tag;
        saveCopyReferenceError.pathValue = writeError;
        return saveCopyReferenceError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static SaveCopyReferenceError path(WriteError writeError) {
        if (writeError != null) {
            return new SaveCopyReferenceError().withTagAndPath(Tag.PATH, writeError);
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

    public boolean isInvalidCopyReference() {
        return this._tag == Tag.INVALID_COPY_REFERENCE;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isNotFound() {
        return this._tag == Tag.NOT_FOUND;
    }

    public boolean isTooManyFiles() {
        return this._tag == Tag.TOO_MANY_FILES;
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
        if (obj == null || !(obj instanceof SaveCopyReferenceError)) {
            return false;
        }
        SaveCopyReferenceError saveCopyReferenceError = (SaveCopyReferenceError) obj;
        if (this._tag != saveCopyReferenceError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                WriteError writeError = this.pathValue;
                WriteError writeError2 = saveCopyReferenceError.pathValue;
                if (writeError != writeError2 && !writeError.equals(writeError2)) {
                    z = false;
                }
                return z;
            case INVALID_COPY_REFERENCE:
                return true;
            case NO_PERMISSION:
                return true;
            case NOT_FOUND:
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
