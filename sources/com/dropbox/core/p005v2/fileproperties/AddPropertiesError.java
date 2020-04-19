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

/* renamed from: com.dropbox.core.v2.fileproperties.AddPropertiesError */
public final class AddPropertiesError {
    public static final AddPropertiesError DOES_NOT_FIT_TEMPLATE = new AddPropertiesError().withTag(Tag.DOES_NOT_FIT_TEMPLATE);
    public static final AddPropertiesError OTHER = new AddPropertiesError().withTag(Tag.OTHER);
    public static final AddPropertiesError PROPERTY_FIELD_TOO_LARGE = new AddPropertiesError().withTag(Tag.PROPERTY_FIELD_TOO_LARGE);
    public static final AddPropertiesError PROPERTY_GROUP_ALREADY_EXISTS = new AddPropertiesError().withTag(Tag.PROPERTY_GROUP_ALREADY_EXISTS);
    public static final AddPropertiesError RESTRICTED_CONTENT = new AddPropertiesError().withTag(Tag.RESTRICTED_CONTENT);
    public static final AddPropertiesError UNSUPPORTED_FOLDER = new AddPropertiesError().withTag(Tag.UNSUPPORTED_FOLDER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;
    /* access modifiers changed from: private */
    public String templateNotFoundValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.AddPropertiesError$Serializer */
    public static class Serializer extends UnionSerializer<AddPropertiesError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(AddPropertiesError addPropertiesError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (addPropertiesError.tag()) {
                case TEMPLATE_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("template_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("template_not_found");
                    StoneSerializers.string().serialize(addPropertiesError.templateNotFoundValue, jsonGenerator);
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
                    Serializer.INSTANCE.serialize(addPropertiesError.pathValue, jsonGenerator);
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
                case PROPERTY_GROUP_ALREADY_EXISTS:
                    jsonGenerator.writeString("property_group_already_exists");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(addPropertiesError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public AddPropertiesError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            AddPropertiesError addPropertiesError;
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
                    addPropertiesError = AddPropertiesError.templateNotFound((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("restricted_content".equals(str)) {
                    addPropertiesError = AddPropertiesError.RESTRICTED_CONTENT;
                } else if ("other".equals(str)) {
                    addPropertiesError = AddPropertiesError.OTHER;
                } else if ("path".equals(str)) {
                    expectField("path", jsonParser);
                    addPropertiesError = AddPropertiesError.path(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("unsupported_folder".equals(str)) {
                    addPropertiesError = AddPropertiesError.UNSUPPORTED_FOLDER;
                } else if ("property_field_too_large".equals(str)) {
                    addPropertiesError = AddPropertiesError.PROPERTY_FIELD_TOO_LARGE;
                } else if ("does_not_fit_template".equals(str)) {
                    addPropertiesError = AddPropertiesError.DOES_NOT_FIT_TEMPLATE;
                } else if ("property_group_already_exists".equals(str)) {
                    addPropertiesError = AddPropertiesError.PROPERTY_GROUP_ALREADY_EXISTS;
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
                return addPropertiesError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.AddPropertiesError$Tag */
    public enum Tag {
        TEMPLATE_NOT_FOUND,
        RESTRICTED_CONTENT,
        OTHER,
        PATH,
        UNSUPPORTED_FOLDER,
        PROPERTY_FIELD_TOO_LARGE,
        DOES_NOT_FIT_TEMPLATE,
        PROPERTY_GROUP_ALREADY_EXISTS
    }

    private AddPropertiesError() {
    }

    private AddPropertiesError withTag(Tag tag) {
        AddPropertiesError addPropertiesError = new AddPropertiesError();
        addPropertiesError._tag = tag;
        return addPropertiesError;
    }

    private AddPropertiesError withTagAndTemplateNotFound(Tag tag, String str) {
        AddPropertiesError addPropertiesError = new AddPropertiesError();
        addPropertiesError._tag = tag;
        addPropertiesError.templateNotFoundValue = str;
        return addPropertiesError;
    }

    private AddPropertiesError withTagAndPath(Tag tag, LookupError lookupError) {
        AddPropertiesError addPropertiesError = new AddPropertiesError();
        addPropertiesError._tag = tag;
        addPropertiesError.pathValue = lookupError;
        return addPropertiesError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isTemplateNotFound() {
        return this._tag == Tag.TEMPLATE_NOT_FOUND;
    }

    public static AddPropertiesError templateNotFound(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String is shorter than 1");
        } else if (Pattern.matches("(/|ptid:).*", str)) {
            return new AddPropertiesError().withTagAndTemplateNotFound(Tag.TEMPLATE_NOT_FOUND, str);
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

    public static AddPropertiesError path(LookupError lookupError) {
        if (lookupError != null) {
            return new AddPropertiesError().withTagAndPath(Tag.PATH, lookupError);
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

    public boolean isPropertyGroupAlreadyExists() {
        return this._tag == Tag.PROPERTY_GROUP_ALREADY_EXISTS;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.templateNotFoundValue, this.pathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AddPropertiesError)) {
            return false;
        }
        AddPropertiesError addPropertiesError = (AddPropertiesError) obj;
        if (this._tag != addPropertiesError._tag) {
            return false;
        }
        switch (this._tag) {
            case TEMPLATE_NOT_FOUND:
                String str = this.templateNotFoundValue;
                String str2 = addPropertiesError.templateNotFoundValue;
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
                LookupError lookupError2 = addPropertiesError.pathValue;
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
            case PROPERTY_GROUP_ALREADY_EXISTS:
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
