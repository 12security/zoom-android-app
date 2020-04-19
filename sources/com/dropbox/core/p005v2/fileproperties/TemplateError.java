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

/* renamed from: com.dropbox.core.v2.fileproperties.TemplateError */
public final class TemplateError {
    public static final TemplateError OTHER = new TemplateError().withTag(Tag.OTHER);
    public static final TemplateError RESTRICTED_CONTENT = new TemplateError().withTag(Tag.RESTRICTED_CONTENT);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String templateNotFoundValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.TemplateError$Serializer */
    public static class Serializer extends UnionSerializer<TemplateError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(TemplateError templateError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (templateError.tag()) {
                case TEMPLATE_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("template_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("template_not_found");
                    StoneSerializers.string().serialize(templateError.templateNotFoundValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case RESTRICTED_CONTENT:
                    jsonGenerator.writeString("restricted_content");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TemplateError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TemplateError templateError;
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
                    templateError = TemplateError.templateNotFound((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("restricted_content".equals(str)) {
                    templateError = TemplateError.RESTRICTED_CONTENT;
                } else {
                    templateError = TemplateError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return templateError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.TemplateError$Tag */
    public enum Tag {
        TEMPLATE_NOT_FOUND,
        RESTRICTED_CONTENT,
        OTHER
    }

    private TemplateError() {
    }

    private TemplateError withTag(Tag tag) {
        TemplateError templateError = new TemplateError();
        templateError._tag = tag;
        return templateError;
    }

    private TemplateError withTagAndTemplateNotFound(Tag tag, String str) {
        TemplateError templateError = new TemplateError();
        templateError._tag = tag;
        templateError.templateNotFoundValue = str;
        return templateError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isTemplateNotFound() {
        return this._tag == Tag.TEMPLATE_NOT_FOUND;
    }

    public static TemplateError templateNotFound(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String is shorter than 1");
        } else if (Pattern.matches("(/|ptid:).*", str)) {
            return new TemplateError().withTagAndTemplateNotFound(Tag.TEMPLATE_NOT_FOUND, str);
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

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.templateNotFoundValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof TemplateError)) {
            return false;
        }
        TemplateError templateError = (TemplateError) obj;
        if (this._tag != templateError._tag) {
            return false;
        }
        switch (this._tag) {
            case TEMPLATE_NOT_FOUND:
                String str = this.templateNotFoundValue;
                String str2 = templateError.templateNotFoundValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case RESTRICTED_CONTENT:
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
