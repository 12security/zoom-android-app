package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.fileproperties.UpdatePropertiesError */
public final class UpdatePropertiesError {
    public static final UpdatePropertiesError DOES_NOT_FIT_TEMPLATE = new UpdatePropertiesError().withTag(Tag.DOES_NOT_FIT_TEMPLATE);
    public static final UpdatePropertiesError OTHER = new UpdatePropertiesError().withTag(Tag.OTHER);
    public static final UpdatePropertiesError PROPERTY_FIELD_TOO_LARGE = new UpdatePropertiesError().withTag(Tag.PROPERTY_FIELD_TOO_LARGE);
    public static final UpdatePropertiesError RESTRICTED_CONTENT = new UpdatePropertiesError().withTag(Tag.RESTRICTED_CONTENT);
    public static final UpdatePropertiesError UNSUPPORTED_FOLDER = new UpdatePropertiesError().withTag(Tag.UNSUPPORTED_FOLDER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;
    /* access modifiers changed from: private */
    public LookUpPropertiesError propertyGroupLookupValue;
    /* access modifiers changed from: private */
    public String templateNotFoundValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.UpdatePropertiesError$Serializer */
    public static class Serializer extends UnionSerializer<UpdatePropertiesError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(UpdatePropertiesError updatePropertiesError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (updatePropertiesError.tag()) {
                case TEMPLATE_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("template_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("template_not_found");
                    StoneSerializers.string().serialize(updatePropertiesError.templateNotFoundValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case RESTRICTED_CONTENT:
                    jsonGenerator.writeString("restricted_content");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    Serializer.INSTANCE.serialize(updatePropertiesError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case UNSUPPORTED_FOLDER:
                    jsonGenerator.writeString("unsupported_folder");
                    return;
                case PROPERTY_FIELD_TOO_LARGE:
                    jsonGenerator.writeString("property_field_too_large");
                    return;
                case DOES_NOT_FIT_TEMPLATE:
                    jsonGenerator.writeString("does_not_fit_template");
                    return;
                case PROPERTY_GROUP_LOOKUP:
                    jsonGenerator.writeStartObject();
                    writeTag("property_group_lookup", jsonGenerator);
                    jsonGenerator.writeFieldName("property_group_lookup");
                    com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError.Serializer.INSTANCE.serialize(updatePropertiesError.propertyGroupLookupValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(updatePropertiesError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public UpdatePropertiesError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UpdatePropertiesError updatePropertiesError;
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
                if ("template_not_found".equals(str)) {
                    expectField("template_not_found", jsonParser);
                    updatePropertiesError = UpdatePropertiesError.templateNotFound((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("restricted_content".equals(str)) {
                    updatePropertiesError = UpdatePropertiesError.RESTRICTED_CONTENT;
                } else if ("other".equals(str)) {
                    updatePropertiesError = UpdatePropertiesError.OTHER;
                } else if ("path".equals(str)) {
                    expectField("path", jsonParser);
                    updatePropertiesError = UpdatePropertiesError.path(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("unsupported_folder".equals(str)) {
                    updatePropertiesError = UpdatePropertiesError.UNSUPPORTED_FOLDER;
                } else if ("property_field_too_large".equals(str)) {
                    updatePropertiesError = UpdatePropertiesError.PROPERTY_FIELD_TOO_LARGE;
                } else if ("does_not_fit_template".equals(str)) {
                    updatePropertiesError = UpdatePropertiesError.DOES_NOT_FIT_TEMPLATE;
                } else if ("property_group_lookup".equals(str)) {
                    expectField("property_group_lookup", jsonParser);
                    updatePropertiesError = UpdatePropertiesError.propertyGroupLookup(com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError.Serializer.INSTANCE.deserialize(jsonParser));
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
                return updatePropertiesError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.UpdatePropertiesError$Tag */
    public enum Tag {
        TEMPLATE_NOT_FOUND,
        RESTRICTED_CONTENT,
        OTHER,
        PATH,
        UNSUPPORTED_FOLDER,
        PROPERTY_FIELD_TOO_LARGE,
        DOES_NOT_FIT_TEMPLATE,
        PROPERTY_GROUP_LOOKUP
    }

    private UpdatePropertiesError() {
    }

    private UpdatePropertiesError withTag(Tag tag) {
        UpdatePropertiesError updatePropertiesError = new UpdatePropertiesError();
        updatePropertiesError._tag = tag;
        return updatePropertiesError;
    }

    private UpdatePropertiesError withTagAndTemplateNotFound(Tag tag, String str) {
        UpdatePropertiesError updatePropertiesError = new UpdatePropertiesError();
        updatePropertiesError._tag = tag;
        updatePropertiesError.templateNotFoundValue = str;
        return updatePropertiesError;
    }

    private UpdatePropertiesError withTagAndPath(Tag tag, LookupError lookupError) {
        UpdatePropertiesError updatePropertiesError = new UpdatePropertiesError();
        updatePropertiesError._tag = tag;
        updatePropertiesError.pathValue = lookupError;
        return updatePropertiesError;
    }

    private UpdatePropertiesError withTagAndPropertyGroupLookup(Tag tag, LookUpPropertiesError lookUpPropertiesError) {
        UpdatePropertiesError updatePropertiesError = new UpdatePropertiesError();
        updatePropertiesError._tag = tag;
        updatePropertiesError.propertyGroupLookupValue = lookUpPropertiesError;
        return updatePropertiesError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isTemplateNotFound() {
        return this._tag == Tag.TEMPLATE_NOT_FOUND;
    }

    public static UpdatePropertiesError templateNotFound(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String is shorter than 1");
        } else if (Pattern.matches("(/|ptid:).*", str)) {
            return new UpdatePropertiesError().withTagAndTemplateNotFound(Tag.TEMPLATE_NOT_FOUND, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getTemplateNotFoundValue() {
        if (this._tag == Tag.TEMPLATE_NOT_FOUND) {
            return this.templateNotFoundValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.TEMPLATE_NOT_FOUND, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isRestrictedContent() {
        return this._tag == Tag.RESTRICTED_CONTENT;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static UpdatePropertiesError path(LookupError lookupError) {
        if (lookupError != null) {
            return new UpdatePropertiesError().withTagAndPath(Tag.PATH, lookupError);
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

    public boolean isUnsupportedFolder() {
        return this._tag == Tag.UNSUPPORTED_FOLDER;
    }

    public boolean isPropertyFieldTooLarge() {
        return this._tag == Tag.PROPERTY_FIELD_TOO_LARGE;
    }

    public boolean isDoesNotFitTemplate() {
        return this._tag == Tag.DOES_NOT_FIT_TEMPLATE;
    }

    public boolean isPropertyGroupLookup() {
        return this._tag == Tag.PROPERTY_GROUP_LOOKUP;
    }

    public static UpdatePropertiesError propertyGroupLookup(LookUpPropertiesError lookUpPropertiesError) {
        if (lookUpPropertiesError != null) {
            return new UpdatePropertiesError().withTagAndPropertyGroupLookup(Tag.PROPERTY_GROUP_LOOKUP, lookUpPropertiesError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookUpPropertiesError getPropertyGroupLookupValue() {
        if (this._tag == Tag.PROPERTY_GROUP_LOOKUP) {
            return this.propertyGroupLookupValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PROPERTY_GROUP_LOOKUP, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.templateNotFoundValue, this.pathValue, this.propertyGroupLookupValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UpdatePropertiesError)) {
            return false;
        }
        UpdatePropertiesError updatePropertiesError = (UpdatePropertiesError) obj;
        if (this._tag != updatePropertiesError._tag) {
            return false;
        }
        switch (this._tag) {
            case TEMPLATE_NOT_FOUND:
                String str = this.templateNotFoundValue;
                String str2 = updatePropertiesError.templateNotFoundValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case RESTRICTED_CONTENT:
                return true;
            case OTHER:
                return true;
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = updatePropertiesError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case UNSUPPORTED_FOLDER:
                return true;
            case PROPERTY_FIELD_TOO_LARGE:
                return true;
            case DOES_NOT_FIT_TEMPLATE:
                return true;
            case PROPERTY_GROUP_LOOKUP:
                LookUpPropertiesError lookUpPropertiesError = this.propertyGroupLookupValue;
                LookUpPropertiesError lookUpPropertiesError2 = updatePropertiesError.propertyGroupLookupValue;
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
