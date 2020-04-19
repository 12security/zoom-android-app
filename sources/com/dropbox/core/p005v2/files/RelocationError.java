package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.RelocationError */
public final class RelocationError {
    public static final RelocationError CANT_COPY_SHARED_FOLDER = new RelocationError().withTag(Tag.CANT_COPY_SHARED_FOLDER);
    public static final RelocationError CANT_MOVE_FOLDER_INTO_ITSELF = new RelocationError().withTag(Tag.CANT_MOVE_FOLDER_INTO_ITSELF);
    public static final RelocationError CANT_NEST_SHARED_FOLDER = new RelocationError().withTag(Tag.CANT_NEST_SHARED_FOLDER);
    public static final RelocationError CANT_TRANSFER_OWNERSHIP = new RelocationError().withTag(Tag.CANT_TRANSFER_OWNERSHIP);
    public static final RelocationError DUPLICATED_OR_NESTED_PATHS = new RelocationError().withTag(Tag.DUPLICATED_OR_NESTED_PATHS);
    public static final RelocationError INSUFFICIENT_QUOTA = new RelocationError().withTag(Tag.INSUFFICIENT_QUOTA);
    public static final RelocationError OTHER = new RelocationError().withTag(Tag.OTHER);
    public static final RelocationError TOO_MANY_FILES = new RelocationError().withTag(Tag.TOO_MANY_FILES);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError fromLookupValue;
    /* access modifiers changed from: private */
    public WriteError fromWriteValue;
    /* access modifiers changed from: private */
    public WriteError toValue;

    /* renamed from: com.dropbox.core.v2.files.RelocationError$Serializer */
    static class Serializer extends UnionSerializer<RelocationError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelocationError relocationError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (relocationError.tag()) {
                case FROM_LOOKUP:
                    jsonGenerator.writeStartObject();
                    writeTag("from_lookup", jsonGenerator);
                    jsonGenerator.writeFieldName("from_lookup");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(relocationError.fromLookupValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case FROM_WRITE:
                    jsonGenerator.writeStartObject();
                    writeTag("from_write", jsonGenerator);
                    jsonGenerator.writeFieldName("from_write");
                    Serializer.INSTANCE.serialize(relocationError.fromWriteValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TO:
                    jsonGenerator.writeStartObject();
                    writeTag("to", jsonGenerator);
                    jsonGenerator.writeFieldName("to");
                    Serializer.INSTANCE.serialize(relocationError.toValue, jsonGenerator);
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
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RelocationError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RelocationError relocationError;
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
                    relocationError = RelocationError.fromLookup(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("from_write".equals(str)) {
                    expectField("from_write", jsonParser);
                    relocationError = RelocationError.fromWrite(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("to".equals(str)) {
                    expectField("to", jsonParser);
                    relocationError = RelocationError.m36to(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("cant_copy_shared_folder".equals(str)) {
                    relocationError = RelocationError.CANT_COPY_SHARED_FOLDER;
                } else if ("cant_nest_shared_folder".equals(str)) {
                    relocationError = RelocationError.CANT_NEST_SHARED_FOLDER;
                } else if ("cant_move_folder_into_itself".equals(str)) {
                    relocationError = RelocationError.CANT_MOVE_FOLDER_INTO_ITSELF;
                } else if ("too_many_files".equals(str)) {
                    relocationError = RelocationError.TOO_MANY_FILES;
                } else if ("duplicated_or_nested_paths".equals(str)) {
                    relocationError = RelocationError.DUPLICATED_OR_NESTED_PATHS;
                } else if ("cant_transfer_ownership".equals(str)) {
                    relocationError = RelocationError.CANT_TRANSFER_OWNERSHIP;
                } else if ("insufficient_quota".equals(str)) {
                    relocationError = RelocationError.INSUFFICIENT_QUOTA;
                } else {
                    relocationError = RelocationError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return relocationError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.RelocationError$Tag */
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
        OTHER
    }

    private RelocationError() {
    }

    private RelocationError withTag(Tag tag) {
        RelocationError relocationError = new RelocationError();
        relocationError._tag = tag;
        return relocationError;
    }

    private RelocationError withTagAndFromLookup(Tag tag, LookupError lookupError) {
        RelocationError relocationError = new RelocationError();
        relocationError._tag = tag;
        relocationError.fromLookupValue = lookupError;
        return relocationError;
    }

    private RelocationError withTagAndFromWrite(Tag tag, WriteError writeError) {
        RelocationError relocationError = new RelocationError();
        relocationError._tag = tag;
        relocationError.fromWriteValue = writeError;
        return relocationError;
    }

    private RelocationError withTagAndTo(Tag tag, WriteError writeError) {
        RelocationError relocationError = new RelocationError();
        relocationError._tag = tag;
        relocationError.toValue = writeError;
        return relocationError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isFromLookup() {
        return this._tag == Tag.FROM_LOOKUP;
    }

    public static RelocationError fromLookup(LookupError lookupError) {
        if (lookupError != null) {
            return new RelocationError().withTagAndFromLookup(Tag.FROM_LOOKUP, lookupError);
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

    public static RelocationError fromWrite(WriteError writeError) {
        if (writeError != null) {
            return new RelocationError().withTagAndFromWrite(Tag.FROM_WRITE, writeError);
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
    public static RelocationError m36to(WriteError writeError) {
        if (writeError != null) {
            return new RelocationError().withTagAndTo(Tag.TO, writeError);
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

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.fromLookupValue, this.fromWriteValue, this.toValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof RelocationError)) {
            return false;
        }
        RelocationError relocationError = (RelocationError) obj;
        if (this._tag != relocationError._tag) {
            return false;
        }
        switch (this._tag) {
            case FROM_LOOKUP:
                LookupError lookupError = this.fromLookupValue;
                LookupError lookupError2 = relocationError.fromLookupValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case FROM_WRITE:
                WriteError writeError = this.fromWriteValue;
                WriteError writeError2 = relocationError.fromWriteValue;
                if (writeError != writeError2 && !writeError.equals(writeError2)) {
                    z = false;
                }
                return z;
            case TO:
                WriteError writeError3 = this.toValue;
                WriteError writeError4 = relocationError.toValue;
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
