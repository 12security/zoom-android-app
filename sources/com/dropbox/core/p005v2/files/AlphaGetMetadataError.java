package com.dropbox.core.p005v2.files;

import com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.AlphaGetMetadataError */
public final class AlphaGetMetadataError {
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;
    /* access modifiers changed from: private */
    public LookUpPropertiesError propertiesErrorValue;

    /* renamed from: com.dropbox.core.v2.files.AlphaGetMetadataError$Serializer */
    static class Serializer extends UnionSerializer<AlphaGetMetadataError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AlphaGetMetadataError alphaGetMetadataError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (alphaGetMetadataError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(alphaGetMetadataError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case PROPERTIES_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("properties_error", jsonGenerator);
                    jsonGenerator.writeFieldName("properties_error");
                    com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError.Serializer.INSTANCE.serialize(alphaGetMetadataError.propertiesErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(alphaGetMetadataError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public AlphaGetMetadataError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AlphaGetMetadataError alphaGetMetadataError;
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
                    alphaGetMetadataError = AlphaGetMetadataError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("properties_error".equals(str)) {
                    expectField("properties_error", jsonParser);
                    alphaGetMetadataError = AlphaGetMetadataError.propertiesError(com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError.Serializer.INSTANCE.deserialize(jsonParser));
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
                return alphaGetMetadataError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.AlphaGetMetadataError$Tag */
    public enum Tag {
        PATH,
        PROPERTIES_ERROR
    }

    private AlphaGetMetadataError() {
    }

    private AlphaGetMetadataError withTag(Tag tag) {
        AlphaGetMetadataError alphaGetMetadataError = new AlphaGetMetadataError();
        alphaGetMetadataError._tag = tag;
        return alphaGetMetadataError;
    }

    private AlphaGetMetadataError withTagAndPath(Tag tag, LookupError lookupError) {
        AlphaGetMetadataError alphaGetMetadataError = new AlphaGetMetadataError();
        alphaGetMetadataError._tag = tag;
        alphaGetMetadataError.pathValue = lookupError;
        return alphaGetMetadataError;
    }

    private AlphaGetMetadataError withTagAndPropertiesError(Tag tag, LookUpPropertiesError lookUpPropertiesError) {
        AlphaGetMetadataError alphaGetMetadataError = new AlphaGetMetadataError();
        alphaGetMetadataError._tag = tag;
        alphaGetMetadataError.propertiesErrorValue = lookUpPropertiesError;
        return alphaGetMetadataError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static AlphaGetMetadataError path(LookupError lookupError) {
        if (lookupError != null) {
            return new AlphaGetMetadataError().withTagAndPath(Tag.PATH, lookupError);
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

    public boolean isPropertiesError() {
        return this._tag == Tag.PROPERTIES_ERROR;
    }

    public static AlphaGetMetadataError propertiesError(LookUpPropertiesError lookUpPropertiesError) {
        if (lookUpPropertiesError != null) {
            return new AlphaGetMetadataError().withTagAndPropertiesError(Tag.PROPERTIES_ERROR, lookUpPropertiesError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookUpPropertiesError getPropertiesErrorValue() {
        if (this._tag == Tag.PROPERTIES_ERROR) {
            return this.propertiesErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PROPERTIES_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.pathValue, this.propertiesErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AlphaGetMetadataError)) {
            return false;
        }
        AlphaGetMetadataError alphaGetMetadataError = (AlphaGetMetadataError) obj;
        if (this._tag != alphaGetMetadataError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = alphaGetMetadataError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case PROPERTIES_ERROR:
                LookUpPropertiesError lookUpPropertiesError = this.propertiesErrorValue;
                LookUpPropertiesError lookUpPropertiesError2 = alphaGetMetadataError.propertiesErrorValue;
                if (lookUpPropertiesError != lookUpPropertiesError2 && !lookUpPropertiesError.equals(lookUpPropertiesError2)) {
                    z = false;
                }
                return z;
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
