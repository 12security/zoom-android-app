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

/* renamed from: com.dropbox.core.v2.fileproperties.ModifyTemplateError */
public final class ModifyTemplateError {
    public static final ModifyTemplateError CONFLICTING_PROPERTY_NAMES = new ModifyTemplateError().withTag(Tag.CONFLICTING_PROPERTY_NAMES);
    public static final ModifyTemplateError OTHER = new ModifyTemplateError().withTag(Tag.OTHER);
    public static final ModifyTemplateError RESTRICTED_CONTENT = new ModifyTemplateError().withTag(Tag.RESTRICTED_CONTENT);
    public static final ModifyTemplateError TEMPLATE_ATTRIBUTE_TOO_LARGE = new ModifyTemplateError().withTag(Tag.TEMPLATE_ATTRIBUTE_TOO_LARGE);
    public static final ModifyTemplateError TOO_MANY_PROPERTIES = new ModifyTemplateError().withTag(Tag.TOO_MANY_PROPERTIES);
    public static final ModifyTemplateError TOO_MANY_TEMPLATES = new ModifyTemplateError().withTag(Tag.TOO_MANY_TEMPLATES);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String templateNotFoundValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.ModifyTemplateError$Serializer */
    public static class Serializer extends UnionSerializer<ModifyTemplateError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(ModifyTemplateError modifyTemplateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (modifyTemplateError.tag()) {
                case TEMPLATE_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("template_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("template_not_found");
                    StoneSerializers.string().serialize(modifyTemplateError.templateNotFoundValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case RESTRICTED_CONTENT:
                    jsonGenerator.writeString("restricted_content");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case CONFLICTING_PROPERTY_NAMES:
                    jsonGenerator.writeString("conflicting_property_names");
                    return;
                case TOO_MANY_PROPERTIES:
                    jsonGenerator.writeString("too_many_properties");
                    return;
                case TOO_MANY_TEMPLATES:
                    jsonGenerator.writeString("too_many_templates");
                    return;
                case TEMPLATE_ATTRIBUTE_TOO_LARGE:
                    jsonGenerator.writeString("template_attribute_too_large");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(modifyTemplateError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ModifyTemplateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ModifyTemplateError modifyTemplateError;
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
                    modifyTemplateError = ModifyTemplateError.templateNotFound((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("restricted_content".equals(str)) {
                    modifyTemplateError = ModifyTemplateError.RESTRICTED_CONTENT;
                } else if ("other".equals(str)) {
                    modifyTemplateError = ModifyTemplateError.OTHER;
                } else if ("conflicting_property_names".equals(str)) {
                    modifyTemplateError = ModifyTemplateError.CONFLICTING_PROPERTY_NAMES;
                } else if ("too_many_properties".equals(str)) {
                    modifyTemplateError = ModifyTemplateError.TOO_MANY_PROPERTIES;
                } else if ("too_many_templates".equals(str)) {
                    modifyTemplateError = ModifyTemplateError.TOO_MANY_TEMPLATES;
                } else if ("template_attribute_too_large".equals(str)) {
                    modifyTemplateError = ModifyTemplateError.TEMPLATE_ATTRIBUTE_TOO_LARGE;
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
                return modifyTemplateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.ModifyTemplateError$Tag */
    public enum Tag {
        TEMPLATE_NOT_FOUND,
        RESTRICTED_CONTENT,
        OTHER,
        CONFLICTING_PROPERTY_NAMES,
        TOO_MANY_PROPERTIES,
        TOO_MANY_TEMPLATES,
        TEMPLATE_ATTRIBUTE_TOO_LARGE
    }

    private ModifyTemplateError() {
    }

    private ModifyTemplateError withTag(Tag tag) {
        ModifyTemplateError modifyTemplateError = new ModifyTemplateError();
        modifyTemplateError._tag = tag;
        return modifyTemplateError;
    }

    private ModifyTemplateError withTagAndTemplateNotFound(Tag tag, String str) {
        ModifyTemplateError modifyTemplateError = new ModifyTemplateError();
        modifyTemplateError._tag = tag;
        modifyTemplateError.templateNotFoundValue = str;
        return modifyTemplateError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isTemplateNotFound() {
        return this._tag == Tag.TEMPLATE_NOT_FOUND;
    }

    public static ModifyTemplateError templateNotFound(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String is shorter than 1");
        } else if (Pattern.matches("(/|ptid:).*", str)) {
            return new ModifyTemplateError().withTagAndTemplateNotFound(Tag.TEMPLATE_NOT_FOUND, str);
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

    public boolean isConflictingPropertyNames() {
        return this._tag == Tag.CONFLICTING_PROPERTY_NAMES;
    }

    public boolean isTooManyProperties() {
        return this._tag == Tag.TOO_MANY_PROPERTIES;
    }

    public boolean isTooManyTemplates() {
        return this._tag == Tag.TOO_MANY_TEMPLATES;
    }

    public boolean isTemplateAttributeTooLarge() {
        return this._tag == Tag.TEMPLATE_ATTRIBUTE_TOO_LARGE;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.templateNotFoundValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ModifyTemplateError)) {
            return false;
        }
        ModifyTemplateError modifyTemplateError = (ModifyTemplateError) obj;
        if (this._tag != modifyTemplateError._tag) {
            return false;
        }
        switch (this._tag) {
            case TEMPLATE_NOT_FOUND:
                String str = this.templateNotFoundValue;
                String str2 = modifyTemplateError.templateNotFoundValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case RESTRICTED_CONTENT:
                return true;
            case OTHER:
                return true;
            case CONFLICTING_PROPERTY_NAMES:
                return true;
            case TOO_MANY_PROPERTIES:
                return true;
            case TOO_MANY_TEMPLATES:
                return true;
            case TEMPLATE_ATTRIBUTE_TOO_LARGE:
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
