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

/* renamed from: com.dropbox.core.v2.fileproperties.RemovePropertiesError */
public final class RemovePropertiesError {
    public static final RemovePropertiesError OTHER = new RemovePropertiesError().withTag(Tag.OTHER);
    public static final RemovePropertiesError RESTRICTED_CONTENT = new RemovePropertiesError().withTag(Tag.RESTRICTED_CONTENT);
    public static final RemovePropertiesError UNSUPPORTED_FOLDER = new RemovePropertiesError().withTag(Tag.UNSUPPORTED_FOLDER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;
    /* access modifiers changed from: private */
    public LookUpPropertiesError propertyGroupLookupValue;
    /* access modifiers changed from: private */
    public String templateNotFoundValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.RemovePropertiesError$Serializer */
    public static class Serializer extends UnionSerializer<RemovePropertiesError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(RemovePropertiesError removePropertiesError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (removePropertiesError.tag()) {
                case TEMPLATE_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("template_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("template_not_found");
                    StoneSerializers.string().serialize(removePropertiesError.templateNotFoundValue, jsonGenerator);
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
                    Serializer.INSTANCE.serialize(removePropertiesError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case UNSUPPORTED_FOLDER:
                    jsonGenerator.writeString("unsupported_folder");
                    return;
                case PROPERTY_GROUP_LOOKUP:
                    jsonGenerator.writeStartObject();
                    writeTag("property_group_lookup", jsonGenerator);
                    jsonGenerator.writeFieldName("property_group_lookup");
                    com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError.Serializer.INSTANCE.serialize(removePropertiesError.propertyGroupLookupValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(removePropertiesError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public RemovePropertiesError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RemovePropertiesError removePropertiesError;
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
                    removePropertiesError = RemovePropertiesError.templateNotFound((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("restricted_content".equals(str)) {
                    removePropertiesError = RemovePropertiesError.RESTRICTED_CONTENT;
                } else if ("other".equals(str)) {
                    removePropertiesError = RemovePropertiesError.OTHER;
                } else if ("path".equals(str)) {
                    expectField("path", jsonParser);
                    removePropertiesError = RemovePropertiesError.path(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("unsupported_folder".equals(str)) {
                    removePropertiesError = RemovePropertiesError.UNSUPPORTED_FOLDER;
                } else if ("property_group_lookup".equals(str)) {
                    expectField("property_group_lookup", jsonParser);
                    removePropertiesError = RemovePropertiesError.propertyGroupLookup(com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError.Serializer.INSTANCE.deserialize(jsonParser));
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
                return removePropertiesError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.RemovePropertiesError$Tag */
    public enum Tag {
        TEMPLATE_NOT_FOUND,
        RESTRICTED_CONTENT,
        OTHER,
        PATH,
        UNSUPPORTED_FOLDER,
        PROPERTY_GROUP_LOOKUP
    }

    private RemovePropertiesError() {
    }

    private RemovePropertiesError withTag(Tag tag) {
        RemovePropertiesError removePropertiesError = new RemovePropertiesError();
        removePropertiesError._tag = tag;
        return removePropertiesError;
    }

    private RemovePropertiesError withTagAndTemplateNotFound(Tag tag, String str) {
        RemovePropertiesError removePropertiesError = new RemovePropertiesError();
        removePropertiesError._tag = tag;
        removePropertiesError.templateNotFoundValue = str;
        return removePropertiesError;
    }

    private RemovePropertiesError withTagAndPath(Tag tag, LookupError lookupError) {
        RemovePropertiesError removePropertiesError = new RemovePropertiesError();
        removePropertiesError._tag = tag;
        removePropertiesError.pathValue = lookupError;
        return removePropertiesError;
    }

    private RemovePropertiesError withTagAndPropertyGroupLookup(Tag tag, LookUpPropertiesError lookUpPropertiesError) {
        RemovePropertiesError removePropertiesError = new RemovePropertiesError();
        removePropertiesError._tag = tag;
        removePropertiesError.propertyGroupLookupValue = lookUpPropertiesError;
        return removePropertiesError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isTemplateNotFound() {
        return this._tag == Tag.TEMPLATE_NOT_FOUND;
    }

    public static RemovePropertiesError templateNotFound(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String is shorter than 1");
        } else if (Pattern.matches("(/|ptid:).*", str)) {
            return new RemovePropertiesError().withTagAndTemplateNotFound(Tag.TEMPLATE_NOT_FOUND, str);
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

    public static RemovePropertiesError path(LookupError lookupError) {
        if (lookupError != null) {
            return new RemovePropertiesError().withTagAndPath(Tag.PATH, lookupError);
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

    public boolean isPropertyGroupLookup() {
        return this._tag == Tag.PROPERTY_GROUP_LOOKUP;
    }

    public static RemovePropertiesError propertyGroupLookup(LookUpPropertiesError lookUpPropertiesError) {
        if (lookUpPropertiesError != null) {
            return new RemovePropertiesError().withTagAndPropertyGroupLookup(Tag.PROPERTY_GROUP_LOOKUP, lookUpPropertiesError);
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
        if (obj == null || !(obj instanceof RemovePropertiesError)) {
            return false;
        }
        RemovePropertiesError removePropertiesError = (RemovePropertiesError) obj;
        if (this._tag != removePropertiesError._tag) {
            return false;
        }
        switch (this._tag) {
            case TEMPLATE_NOT_FOUND:
                String str = this.templateNotFoundValue;
                String str2 = removePropertiesError.templateNotFoundValue;
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
                LookupError lookupError2 = removePropertiesError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case UNSUPPORTED_FOLDER:
                return true;
            case PROPERTY_GROUP_LOOKUP:
                LookUpPropertiesError lookUpPropertiesError = this.propertyGroupLookupValue;
                LookUpPropertiesError lookUpPropertiesError2 = removePropertiesError.propertyGroupLookupValue;
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
