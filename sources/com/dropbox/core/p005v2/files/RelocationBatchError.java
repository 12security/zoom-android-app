package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.RelocationBatchError */
public final class RelocationBatchError {
    public static final RelocationBatchError CANT_COPY_SHARED_FOLDER = new RelocationBatchError().withTag(Tag.CANT_COPY_SHARED_FOLDER);
    public static final RelocationBatchError CANT_MOVE_FOLDER_INTO_ITSELF = new RelocationBatchError().withTag(Tag.CANT_MOVE_FOLDER_INTO_ITSELF);
    public static final RelocationBatchError CANT_NEST_SHARED_FOLDER = new RelocationBatchError().withTag(Tag.CANT_NEST_SHARED_FOLDER);
    public static final RelocationBatchError CANT_TRANSFER_OWNERSHIP = new RelocationBatchError().withTag(Tag.CANT_TRANSFER_OWNERSHIP);
    public static final RelocationBatchError DUPLICATED_OR_NESTED_PATHS = new RelocationBatchError().withTag(Tag.DUPLICATED_OR_NESTED_PATHS);
    public static final RelocationBatchError INSUFFICIENT_QUOTA = new RelocationBatchError().withTag(Tag.INSUFFICIENT_QUOTA);
    public static final RelocationBatchError OTHER = new RelocationBatchError().withTag(Tag.OTHER);
    public static final RelocationBatchError TOO_MANY_FILES = new RelocationBatchError().withTag(Tag.TOO_MANY_FILES);
    public static final RelocationBatchError TOO_MANY_WRITE_OPERATIONS = new RelocationBatchError().withTag(Tag.TOO_MANY_WRITE_OPERATIONS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError fromLookupValue;
    /* access modifiers changed from: private */
    public WriteError fromWriteValue;
    /* access modifiers changed from: private */
    public WriteError toValue;

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchError$Serializer */
    static class Serializer extends UnionSerializer<RelocationBatchError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelocationBatchError relocationBatchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (relocationBatchError.tag()) {
                case FROM_LOOKUP:
                    jsonGenerator.writeStartObject();
                    writeTag("from_lookup", jsonGenerator);
                    jsonGenerator.writeFieldName("from_lookup");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(relocationBatchError.fromLookupValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case FROM_WRITE:
                    jsonGenerator.writeStartObject();
                    writeTag("from_write", jsonGenerator);
                    jsonGenerator.writeFieldName("from_write");
                    Serializer.INSTANCE.serialize(relocationBatchError.fromWriteValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TO:
                    jsonGenerator.writeStartObject();
                    writeTag("to", jsonGenerator);
                    jsonGenerator.writeFieldName("to");
                    Serializer.INSTANCE.serialize(relocationBatchError.toValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case CANT_COPY_SHARED_FOLDER:
                    jsonGenerator.writeString("cant_copy_shared_folder");
                    return;
                case CANT_NEST_SHARED_FOLDER:
                    jsonGenerator.writeString("cant_nest_shared_folder");
                    return;
                case CANT_MOVE_FOLDER_INTO_ITSELF:
                    jsonGenerator.writeString("cant_move_folder_into_itself");
                    return;
                case TOO_MANY_FILES:
                    jsonGenerator.writeString("too_many_files");
                    return;
                case DUPLICATED_OR_NESTED_PATHS:
                    jsonGenerator.writeString("duplicated_or_nested_paths");
                    return;
                case CANT_TRANSFER_OWNERSHIP:
                    jsonGenerator.writeString("cant_transfer_ownership");
                    return;
                case INSUFFICIENT_QUOTA:
                    jsonGenerator.writeString("insufficient_quota");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case TOO_MANY_WRITE_OPERATIONS:
                    jsonGenerator.writeString("too_many_write_operations");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(relocationBatchError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public RelocationBatchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RelocationBatchError relocationBatchError;
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
                if ("from_lookup".equals(str)) {
                    expectField("from_lookup", jsonParser);
                    relocationBatchError = RelocationBatchError.fromLookup(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("from_write".equals(str)) {
                    expectField("from_write", jsonParser);
                    relocationBatchError = RelocationBatchError.fromWrite(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("to".equals(str)) {
                    expectField("to", jsonParser);
                    relocationBatchError = RelocationBatchError.m35to(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("cant_copy_shared_folder".equals(str)) {
                    relocationBatchError = RelocationBatchError.CANT_COPY_SHARED_FOLDER;
                } else if ("cant_nest_shared_folder".equals(str)) {
                    relocationBatchError = RelocationBatchError.CANT_NEST_SHARED_FOLDER;
                } else if ("cant_move_folder_into_itself".equals(str)) {
                    relocationBatchError = RelocationBatchError.CANT_MOVE_FOLDER_INTO_ITSELF;
                } else if ("too_many_files".equals(str)) {
                    relocationBatchError = RelocationBatchError.TOO_MANY_FILES;
                } else if ("duplicated_or_nested_paths".equals(str)) {
                    relocationBatchError = RelocationBatchError.DUPLICATED_OR_NESTED_PATHS;
                } else if ("cant_transfer_ownership".equals(str)) {
                    relocationBatchError = RelocationBatchError.CANT_TRANSFER_OWNERSHIP;
                } else if ("insufficient_quota".equals(str)) {
                    relocationBatchError = RelocationBatchError.INSUFFICIENT_QUOTA;
                } else if ("other".equals(str)) {
                    relocationBatchError = RelocationBatchError.OTHER;
                } else if ("too_many_write_operations".equals(str)) {
                    relocationBatchError = RelocationBatchError.TOO_MANY_WRITE_OPERATIONS;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return relocationBatchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchError$Tag */
    public enum Tag {
        FROM_LOOKUP,
        FROM_WRITE,
        TO,
        CANT_COPY_SHARED_FOLDER,
        CANT_NEST_SHARED_FOLDER,
        CANT_MOVE_FOLDER_INTO_ITSELF,
        TOO_MANY_FILES,
        DUPLICATED_OR_NESTED_PATHS,
        CANT_TRANSFER_OWNERSHIP,
        INSUFFICIENT_QUOTA,
        OTHER,
        TOO_MANY_WRITE_OPERATIONS
    }

    private RelocationBatchError() {
    }

    private RelocationBatchError withTag(Tag tag) {
        RelocationBatchError relocationBatchError = new RelocationBatchError();
        relocationBatchError._tag = tag;
        return relocationBatchError;
    }

    private RelocationBatchError withTagAndFromLookup(Tag tag, LookupError lookupError) {
        RelocationBatchError relocationBatchError = new RelocationBatchError();
        relocationBatchError._tag = tag;
        relocationBatchError.fromLookupValue = lookupError;
        return relocationBatchError;
    }

    private RelocationBatchError withTagAndFromWrite(Tag tag, WriteError writeError) {
        RelocationBatchError relocationBatchError = new RelocationBatchError();
        relocationBatchError._tag = tag;
        relocationBatchError.fromWriteValue = writeError;
        return relocationBatchError;
    }

    private RelocationBatchError withTagAndTo(Tag tag, WriteError writeError) {
        RelocationBatchError relocationBatchError = new RelocationBatchError();
        relocationBatchError._tag = tag;
        relocationBatchError.toValue = writeError;
        return relocationBatchError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isFromLookup() {
        return this._tag == Tag.FROM_LOOKUP;
    }

    public static RelocationBatchError fromLookup(LookupError lookupError) {
        if (lookupError != null) {
            return new RelocationBatchError().withTagAndFromLookup(Tag.FROM_LOOKUP, lookupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookupError getFromLookupValue() {
        if (this._tag == Tag.FROM_LOOKUP) {
            return this.fromLookupValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FROM_LOOKUP, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isFromWrite() {
        return this._tag == Tag.FROM_WRITE;
    }

    public static RelocationBatchError fromWrite(WriteError writeError) {
        if (writeError != null) {
            return new RelocationBatchError().withTagAndFromWrite(Tag.FROM_WRITE, writeError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WriteError getFromWriteValue() {
        if (this._tag == Tag.FROM_WRITE) {
            return this.fromWriteValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FROM_WRITE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTo() {
        return this._tag == Tag.TO;
    }

    /* renamed from: to */
    public static RelocationBatchError m35to(WriteError writeError) {
        if (writeError != null) {
            return new RelocationBatchError().withTagAndTo(Tag.TO, writeError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WriteError getToValue() {
        if (this._tag == Tag.TO) {
            return this.toValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TO, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isCantCopySharedFolder() {
        return this._tag == Tag.CANT_COPY_SHARED_FOLDER;
    }

    public boolean isCantNestSharedFolder() {
        return this._tag == Tag.CANT_NEST_SHARED_FOLDER;
    }

    public boolean isCantMoveFolderIntoItself() {
        return this._tag == Tag.CANT_MOVE_FOLDER_INTO_ITSELF;
    }

    public boolean isTooManyFiles() {
        return this._tag == Tag.TOO_MANY_FILES;
    }

    public boolean isDuplicatedOrNestedPaths() {
        return this._tag == Tag.DUPLICATED_OR_NESTED_PATHS;
    }

    public boolean isCantTransferOwnership() {
        return this._tag == Tag.CANT_TRANSFER_OWNERSHIP;
    }

    public boolean isInsufficientQuota() {
        return this._tag == Tag.INSUFFICIENT_QUOTA;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public boolean isTooManyWriteOperations() {
        return this._tag == Tag.TOO_MANY_WRITE_OPERATIONS;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.fromLookupValue, this.fromWriteValue, this.toValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof RelocationBatchError)) {
            return false;
        }
        RelocationBatchError relocationBatchError = (RelocationBatchError) obj;
        if (this._tag != relocationBatchError._tag) {
            return false;
        }
        switch (this._tag) {
            case FROM_LOOKUP:
                LookupError lookupError = this.fromLookupValue;
                LookupError lookupError2 = relocationBatchError.fromLookupValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case FROM_WRITE:
                WriteError writeError = this.fromWriteValue;
                WriteError writeError2 = relocationBatchError.fromWriteValue;
                if (writeError != writeError2 && !writeError.equals(writeError2)) {
                    z = false;
                }
                return z;
            case TO:
                WriteError writeError3 = this.toValue;
                WriteError writeError4 = relocationBatchError.toValue;
                if (writeError3 != writeError4 && !writeError3.equals(writeError4)) {
                    z = false;
                }
                return z;
            case CANT_COPY_SHARED_FOLDER:
                return true;
            case CANT_NEST_SHARED_FOLDER:
                return true;
            case CANT_MOVE_FOLDER_INTO_ITSELF:
                return true;
            case TOO_MANY_FILES:
                return true;
            case DUPLICATED_OR_NESTED_PATHS:
                return true;
            case CANT_TRANSFER_OWNERSHIP:
                return true;
            case INSUFFICIENT_QUOTA:
                return true;
            case OTHER:
                return true;
            case TOO_MANY_WRITE_OPERATIONS:
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
