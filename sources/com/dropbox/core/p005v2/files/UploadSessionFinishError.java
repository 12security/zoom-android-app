package com.dropbox.core.p005v2.files;

import com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupError;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.UploadSessionFinishError */
public final class UploadSessionFinishError {
    public static final UploadSessionFinishError OTHER = new UploadSessionFinishError().withTag(Tag.OTHER);
    public static final UploadSessionFinishError TOO_MANY_SHARED_FOLDER_TARGETS = new UploadSessionFinishError().withTag(Tag.TOO_MANY_SHARED_FOLDER_TARGETS);
    public static final UploadSessionFinishError TOO_MANY_WRITE_OPERATIONS = new UploadSessionFinishError().withTag(Tag.TOO_MANY_WRITE_OPERATIONS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public UploadSessionLookupError lookupFailedValue;
    /* access modifiers changed from: private */
    public WriteError pathValue;
    /* access modifiers changed from: private */
    public InvalidPropertyGroupError propertiesErrorValue;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishError$Serializer */
    static class Serializer extends UnionSerializer<UploadSessionFinishError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionFinishError uploadSessionFinishError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (uploadSessionFinishError.tag()) {
                case LOOKUP_FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("lookup_failed", jsonGenerator);
                    jsonGenerator.writeFieldName("lookup_failed");
                    Serializer.INSTANCE.serialize(uploadSessionFinishError.lookupFailedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    Serializer.INSTANCE.serialize(uploadSessionFinishError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case PROPERTIES_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("properties_error", jsonGenerator);
                    jsonGenerator.writeFieldName("properties_error");
                    com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupError.Serializer.INSTANCE.serialize(uploadSessionFinishError.propertiesErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case TOO_MANY_SHARED_FOLDER_TARGETS:
                    jsonGenerator.writeString("too_many_shared_folder_targets");
                    return;
                case TOO_MANY_WRITE_OPERATIONS:
                    jsonGenerator.writeString("too_many_write_operations");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UploadSessionFinishError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UploadSessionFinishError uploadSessionFinishError;
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
                if ("lookup_failed".equals(str)) {
                    expectField("lookup_failed", jsonParser);
                    uploadSessionFinishError = UploadSessionFinishError.lookupFailed(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("path".equals(str)) {
                    expectField("path", jsonParser);
                    uploadSessionFinishError = UploadSessionFinishError.path(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("properties_error".equals(str)) {
                    expectField("properties_error", jsonParser);
                    uploadSessionFinishError = UploadSessionFinishError.propertiesError(com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("too_many_shared_folder_targets".equals(str)) {
                    uploadSessionFinishError = UploadSessionFinishError.TOO_MANY_SHARED_FOLDER_TARGETS;
                } else if ("too_many_write_operations".equals(str)) {
                    uploadSessionFinishError = UploadSessionFinishError.TOO_MANY_WRITE_OPERATIONS;
                } else {
                    uploadSessionFinishError = UploadSessionFinishError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return uploadSessionFinishError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishError$Tag */
    public enum Tag {
        LOOKUP_FAILED,
        PATH,
        PROPERTIES_ERROR,
        TOO_MANY_SHARED_FOLDER_TARGETS,
        TOO_MANY_WRITE_OPERATIONS,
        OTHER
    }

    private UploadSessionFinishError() {
    }

    private UploadSessionFinishError withTag(Tag tag) {
        UploadSessionFinishError uploadSessionFinishError = new UploadSessionFinishError();
        uploadSessionFinishError._tag = tag;
        return uploadSessionFinishError;
    }

    private UploadSessionFinishError withTagAndLookupFailed(Tag tag, UploadSessionLookupError uploadSessionLookupError) {
        UploadSessionFinishError uploadSessionFinishError = new UploadSessionFinishError();
        uploadSessionFinishError._tag = tag;
        uploadSessionFinishError.lookupFailedValue = uploadSessionLookupError;
        return uploadSessionFinishError;
    }

    private UploadSessionFinishError withTagAndPath(Tag tag, WriteError writeError) {
        UploadSessionFinishError uploadSessionFinishError = new UploadSessionFinishError();
        uploadSessionFinishError._tag = tag;
        uploadSessionFinishError.pathValue = writeError;
        return uploadSessionFinishError;
    }

    private UploadSessionFinishError withTagAndPropertiesError(Tag tag, InvalidPropertyGroupError invalidPropertyGroupError) {
        UploadSessionFinishError uploadSessionFinishError = new UploadSessionFinishError();
        uploadSessionFinishError._tag = tag;
        uploadSessionFinishError.propertiesErrorValue = invalidPropertyGroupError;
        return uploadSessionFinishError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isLookupFailed() {
        return this._tag == Tag.LOOKUP_FAILED;
    }

    public static UploadSessionFinishError lookupFailed(UploadSessionLookupError uploadSessionLookupError) {
        if (uploadSessionLookupError != null) {
            return new UploadSessionFinishError().withTagAndLookupFailed(Tag.LOOKUP_FAILED, uploadSessionLookupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UploadSessionLookupError getLookupFailedValue() {
        if (this._tag == Tag.LOOKUP_FAILED) {
            return this.lookupFailedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.LOOKUP_FAILED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static UploadSessionFinishError path(WriteError writeError) {
        if (writeError != null) {
            return new UploadSessionFinishError().withTagAndPath(Tag.PATH, writeError);
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

    public boolean isPropertiesError() {
        return this._tag == Tag.PROPERTIES_ERROR;
    }

    public static UploadSessionFinishError propertiesError(InvalidPropertyGroupError invalidPropertyGroupError) {
        if (invalidPropertyGroupError != null) {
            return new UploadSessionFinishError().withTagAndPropertiesError(Tag.PROPERTIES_ERROR, invalidPropertyGroupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public InvalidPropertyGroupError getPropertiesErrorValue() {
        if (this._tag == Tag.PROPERTIES_ERROR) {
            return this.propertiesErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PROPERTIES_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isTooManySharedFolderTargets() {
        return this._tag == Tag.TOO_MANY_SHARED_FOLDER_TARGETS;
    }

    public boolean isTooManyWriteOperations() {
        return this._tag == Tag.TOO_MANY_WRITE_OPERATIONS;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.lookupFailedValue, this.pathValue, this.propertiesErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UploadSessionFinishError)) {
            return false;
        }
        UploadSessionFinishError uploadSessionFinishError = (UploadSessionFinishError) obj;
        if (this._tag != uploadSessionFinishError._tag) {
            return false;
        }
        switch (this._tag) {
            case LOOKUP_FAILED:
                UploadSessionLookupError uploadSessionLookupError = this.lookupFailedValue;
                UploadSessionLookupError uploadSessionLookupError2 = uploadSessionFinishError.lookupFailedValue;
                if (uploadSessionLookupError != uploadSessionLookupError2 && !uploadSessionLookupError.equals(uploadSessionLookupError2)) {
                    z = false;
                }
                return z;
            case PATH:
                WriteError writeError = this.pathValue;
                WriteError writeError2 = uploadSessionFinishError.pathValue;
                if (writeError != writeError2 && !writeError.equals(writeError2)) {
                    z = false;
                }
                return z;
            case PROPERTIES_ERROR:
                InvalidPropertyGroupError invalidPropertyGroupError = this.propertiesErrorValue;
                InvalidPropertyGroupError invalidPropertyGroupError2 = uploadSessionFinishError.propertiesErrorValue;
                if (invalidPropertyGroupError != invalidPropertyGroupError2 && !invalidPropertyGroupError.equals(invalidPropertyGroupError2)) {
                    z = false;
                }
                return z;
            case TOO_MANY_SHARED_FOLDER_TARGETS:
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
