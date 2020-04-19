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

/* renamed from: com.dropbox.core.v2.files.UploadError */
public final class UploadError {
    public static final UploadError OTHER = new UploadError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public UploadWriteFailed pathValue;
    /* access modifiers changed from: private */
    public InvalidPropertyGroupError propertiesErrorValue;

    /* renamed from: com.dropbox.core.v2.files.UploadError$Serializer */
    static class Serializer extends UnionSerializer<UploadError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadError uploadError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (uploadError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    Serializer.INSTANCE.serialize(uploadError.pathValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case PROPERTIES_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("properties_error", jsonGenerator);
                    jsonGenerator.writeFieldName("properties_error");
                    com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupError.Serializer.INSTANCE.serialize(uploadError.propertiesErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UploadError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            UploadError uploadError;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if ("path".equals(str)) {
                    uploadError = UploadError.path(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("properties_error".equals(str)) {
                    expectField("properties_error", jsonParser);
                    uploadError = UploadError.propertiesError(com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    uploadError = UploadError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return uploadError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.UploadError$Tag */
    public enum Tag {
        PATH,
        PROPERTIES_ERROR,
        OTHER
    }

    private UploadError() {
    }

    private UploadError withTag(Tag tag) {
        UploadError uploadError = new UploadError();
        uploadError._tag = tag;
        return uploadError;
    }

    private UploadError withTagAndPath(Tag tag, UploadWriteFailed uploadWriteFailed) {
        UploadError uploadError = new UploadError();
        uploadError._tag = tag;
        uploadError.pathValue = uploadWriteFailed;
        return uploadError;
    }

    private UploadError withTagAndPropertiesError(Tag tag, InvalidPropertyGroupError invalidPropertyGroupError) {
        UploadError uploadError = new UploadError();
        uploadError._tag = tag;
        uploadError.propertiesErrorValue = invalidPropertyGroupError;
        return uploadError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static UploadError path(UploadWriteFailed uploadWriteFailed) {
        if (uploadWriteFailed != null) {
            return new UploadError().withTagAndPath(Tag.PATH, uploadWriteFailed);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UploadWriteFailed getPathValue() {
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

    public static UploadError propertiesError(InvalidPropertyGroupError invalidPropertyGroupError) {
        if (invalidPropertyGroupError != null) {
            return new UploadError().withTagAndPropertiesError(Tag.PROPERTIES_ERROR, invalidPropertyGroupError);
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

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathValue, this.propertiesErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UploadError)) {
            return false;
        }
        UploadError uploadError = (UploadError) obj;
        if (this._tag != uploadError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                UploadWriteFailed uploadWriteFailed = this.pathValue;
                UploadWriteFailed uploadWriteFailed2 = uploadError.pathValue;
                if (uploadWriteFailed != uploadWriteFailed2 && !uploadWriteFailed.equals(uploadWriteFailed2)) {
                    z = false;
                }
                return z;
            case PROPERTIES_ERROR:
                InvalidPropertyGroupError invalidPropertyGroupError = this.propertiesErrorValue;
                InvalidPropertyGroupError invalidPropertyGroupError2 = uploadError.propertiesErrorValue;
                if (invalidPropertyGroupError != invalidPropertyGroupError2 && !invalidPropertyGroupError.equals(invalidPropertyGroupError2)) {
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
