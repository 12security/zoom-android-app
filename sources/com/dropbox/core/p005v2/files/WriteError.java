package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.WriteError */
public final class WriteError {
    public static final WriteError DISALLOWED_NAME = new WriteError().withTag(Tag.DISALLOWED_NAME);
    public static final WriteError INSUFFICIENT_SPACE = new WriteError().withTag(Tag.INSUFFICIENT_SPACE);
    public static final WriteError NO_WRITE_PERMISSION = new WriteError().withTag(Tag.NO_WRITE_PERMISSION);
    public static final WriteError OTHER = new WriteError().withTag(Tag.OTHER);
    public static final WriteError TEAM_FOLDER = new WriteError().withTag(Tag.TEAM_FOLDER);
    public static final WriteError TOO_MANY_WRITE_OPERATIONS = new WriteError().withTag(Tag.TOO_MANY_WRITE_OPERATIONS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public WriteConflictError conflictValue;
    /* access modifiers changed from: private */
    public String malformedPathValue;

    /* renamed from: com.dropbox.core.v2.files.WriteError$Serializer */
    static class Serializer extends UnionSerializer<WriteError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(WriteError writeError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (writeError.tag()) {
                case MALFORMED_PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("malformed_path", jsonGenerator);
                    jsonGenerator.writeFieldName("malformed_path");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(writeError.malformedPathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case CONFLICT:
                    jsonGenerator.writeStartObject();
                    writeTag("conflict", jsonGenerator);
                    jsonGenerator.writeFieldName("conflict");
                    Serializer.INSTANCE.serialize(writeError.conflictValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NO_WRITE_PERMISSION:
                    jsonGenerator.writeString("no_write_permission");
                    return;
                case INSUFFICIENT_SPACE:
                    jsonGenerator.writeString("insufficient_space");
                    return;
                case DISALLOWED_NAME:
                    jsonGenerator.writeString("disallowed_name");
                    return;
                case TEAM_FOLDER:
                    jsonGenerator.writeString("team_folder");
                    return;
                case TOO_MANY_WRITE_OPERATIONS:
                    jsonGenerator.writeString("too_many_write_operations");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public WriteError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            WriteError writeError;
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
                if ("malformed_path".equals(str)) {
                    String str2 = null;
                    if (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
                        expectField("malformed_path", jsonParser);
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    }
                    if (str2 == null) {
                        writeError = WriteError.malformedPath();
                    } else {
                        writeError = WriteError.malformedPath(str2);
                    }
                } else if ("conflict".equals(str)) {
                    expectField("conflict", jsonParser);
                    writeError = WriteError.conflict(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("no_write_permission".equals(str)) {
                    writeError = WriteError.NO_WRITE_PERMISSION;
                } else if ("insufficient_space".equals(str)) {
                    writeError = WriteError.INSUFFICIENT_SPACE;
                } else if ("disallowed_name".equals(str)) {
                    writeError = WriteError.DISALLOWED_NAME;
                } else if ("team_folder".equals(str)) {
                    writeError = WriteError.TEAM_FOLDER;
                } else if ("too_many_write_operations".equals(str)) {
                    writeError = WriteError.TOO_MANY_WRITE_OPERATIONS;
                } else {
                    writeError = WriteError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return writeError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.WriteError$Tag */
    public enum Tag {
        MALFORMED_PATH,
        CONFLICT,
        NO_WRITE_PERMISSION,
        INSUFFICIENT_SPACE,
        DISALLOWED_NAME,
        TEAM_FOLDER,
        TOO_MANY_WRITE_OPERATIONS,
        OTHER
    }

    private WriteError() {
    }

    private WriteError withTag(Tag tag) {
        WriteError writeError = new WriteError();
        writeError._tag = tag;
        return writeError;
    }

    private WriteError withTagAndMalformedPath(Tag tag, String str) {
        WriteError writeError = new WriteError();
        writeError._tag = tag;
        writeError.malformedPathValue = str;
        return writeError;
    }

    private WriteError withTagAndConflict(Tag tag, WriteConflictError writeConflictError) {
        WriteError writeError = new WriteError();
        writeError._tag = tag;
        writeError.conflictValue = writeConflictError;
        return writeError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isMalformedPath() {
        return this._tag == Tag.MALFORMED_PATH;
    }

    public static WriteError malformedPath(String str) {
        return new WriteError().withTagAndMalformedPath(Tag.MALFORMED_PATH, str);
    }

    public static WriteError malformedPath() {
        return malformedPath(null);
    }

    public String getMalformedPathValue() {
        if (this._tag == Tag.MALFORMED_PATH) {
            return this.malformedPathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.MALFORMED_PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isConflict() {
        return this._tag == Tag.CONFLICT;
    }

    public static WriteError conflict(WriteConflictError writeConflictError) {
        if (writeConflictError != null) {
            return new WriteError().withTagAndConflict(Tag.CONFLICT, writeConflictError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WriteConflictError getConflictValue() {
        if (this._tag == Tag.CONFLICT) {
            return this.conflictValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.CONFLICT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isNoWritePermission() {
        return this._tag == Tag.NO_WRITE_PERMISSION;
    }

    public boolean isInsufficientSpace() {
        return this._tag == Tag.INSUFFICIENT_SPACE;
    }

    public boolean isDisallowedName() {
        return this._tag == Tag.DISALLOWED_NAME;
    }

    public boolean isTeamFolder() {
        return this._tag == Tag.TEAM_FOLDER;
    }

    public boolean isTooManyWriteOperations() {
        return this._tag == Tag.TOO_MANY_WRITE_OPERATIONS;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.malformedPathValue, this.conflictValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof WriteError)) {
            return false;
        }
        WriteError writeError = (WriteError) obj;
        if (this._tag != writeError._tag) {
            return false;
        }
        switch (this._tag) {
            case MALFORMED_PATH:
                String str = this.malformedPathValue;
                String str2 = writeError.malformedPathValue;
                if (str != str2 && (str == null || !str.equals(str2))) {
                    z = false;
                }
                return z;
            case CONFLICT:
                WriteConflictError writeConflictError = this.conflictValue;
                WriteConflictError writeConflictError2 = writeError.conflictValue;
                if (writeConflictError != writeConflictError2 && !writeConflictError.equals(writeConflictError2)) {
                    z = false;
                }
                return z;
            case NO_WRITE_PERMISSION:
                return true;
            case INSUFFICIENT_SPACE:
                return true;
            case DISALLOWED_NAME:
                return true;
            case TEAM_FOLDER:
                return true;
            case TOO_MANY_WRITE_OPERATIONS:
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
