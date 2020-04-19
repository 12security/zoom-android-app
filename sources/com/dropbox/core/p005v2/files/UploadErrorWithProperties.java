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

/* renamed from: com.dropbox.core.v2.files.UploadErrorWithProperties */
public final class UploadErrorWithProperties {
    public static final UploadErrorWithProperties OTHER = new UploadErrorWithProperties().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public UploadWriteFailed pathValue;
    /* access modifiers changed from: private */
    public InvalidPropertyGroupError propertiesErrorValue;

    /* renamed from: com.dropbox.core.v2.files.UploadErrorWithProperties$Serializer */
    static class Serializer extends UnionSerializer<UploadErrorWithProperties> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadErrorWithProperties uploadErrorWithProperties, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (uploadErrorWithProperties.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    Serializer.INSTANCE.serialize(uploadErrorWithProperties.pathValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case PROPERTIES_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("properties_error", jsonGenerator);
                    jsonGenerator.writeFieldName("properties_error");
                    com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupError.Serializer.INSTANCE.serialize(uploadErrorWithProperties.propertiesErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(uploadErrorWithProperties.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public UploadErrorWithProperties deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            UploadErrorWithProperties uploadErrorWithProperties;
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
                    uploadErrorWithProperties = UploadErrorWithProperties.path(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("properties_error".equals(str)) {
                    expectField("properties_error", jsonParser);
                    uploadErrorWithProperties = UploadErrorWithProperties.propertiesError(com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("other".equals(str)) {
                    uploadErrorWithProperties = UploadErrorWithProperties.OTHER;
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
                return uploadErrorWithProperties;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.UploadErrorWithProperties$Tag */
    public enum Tag {
        PATH,
        PROPERTIES_ERROR,
        OTHER
    }

    private UploadErrorWithProperties() {
    }

    private UploadErrorWithProperties withTag(Tag tag) {
        UploadErrorWithProperties uploadErrorWithProperties = new UploadErrorWithProperties();
        uploadErrorWithProperties._tag = tag;
        return uploadErrorWithProperties;
    }

    private UploadErrorWithProperties withTagAndPath(Tag tag, UploadWriteFailed uploadWriteFailed) {
        UploadErrorWithProperties uploadErrorWithProperties = new UploadErrorWithProperties();
        uploadErrorWithProperties._tag = tag;
        uploadErrorWithProperties.pathValue = uploadWriteFailed;
        return uploadErrorWithProperties;
    }

    private UploadErrorWithProperties withTagAndPropertiesError(Tag tag, InvalidPropertyGroupError invalidPropertyGroupError) {
        UploadErrorWithProperties uploadErrorWithProperties = new UploadErrorWithProperties();
        uploadErrorWithProperties._tag = tag;
        uploadErrorWithProperties.propertiesErrorValue = invalidPropertyGroupError;
        return uploadErrorWithProperties;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static UploadErrorWithProperties path(UploadWriteFailed uploadWriteFailed) {
        if (uploadWriteFailed != null) {
            return new UploadErrorWithProperties().withTagAndPath(Tag.PATH, uploadWriteFailed);
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

    public static UploadErrorWithProperties propertiesError(InvalidPropertyGroupError invalidPropertyGroupError) {
        if (invalidPropertyGroupError != null) {
            return new UploadErrorWithProperties().withTagAndPropertiesError(Tag.PROPERTIES_ERROR, invalidPropertyGroupError);
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
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.pathValue, this.propertiesErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UploadErrorWithProperties)) {
            return false;
        }
        UploadErrorWithProperties uploadErrorWithProperties = (UploadErrorWithProperties) obj;
        if (this._tag != uploadErrorWithProperties._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                UploadWriteFailed uploadWriteFailed = this.pathValue;
                UploadWriteFailed uploadWriteFailed2 = uploadErrorWithProperties.pathValue;
                if (uploadWriteFailed != uploadWriteFailed2 && !uploadWriteFailed.equals(uploadWriteFailed2)) {
                    z = false;
                }
                return z;
            case PROPERTIES_ERROR:
                InvalidPropertyGroupError invalidPropertyGroupError = this.propertiesErrorValue;
                InvalidPropertyGroupError invalidPropertyGroupError2 = uploadErrorWithProperties.propertiesErrorValue;
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
