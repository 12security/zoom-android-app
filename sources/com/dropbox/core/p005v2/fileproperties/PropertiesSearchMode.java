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

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchMode */
public final class PropertiesSearchMode {
    public static final PropertiesSearchMode OTHER = new PropertiesSearchMode().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String fieldNameValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchMode$Serializer */
    static class Serializer extends UnionSerializer<PropertiesSearchMode> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertiesSearchMode propertiesSearchMode, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06421.f77xa024451b[propertiesSearchMode.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("field_name", jsonGenerator);
            jsonGenerator.writeFieldName("field_name");
            StoneSerializers.string().serialize(propertiesSearchMode.fieldNameValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public PropertiesSearchMode deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PropertiesSearchMode propertiesSearchMode;
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
                if ("field_name".equals(str)) {
                    expectField("field_name", jsonParser);
                    propertiesSearchMode = PropertiesSearchMode.fieldName((String) StoneSerializers.string().deserialize(jsonParser));
                } else {
                    propertiesSearchMode = PropertiesSearchMode.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return propertiesSearchMode;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchMode$Tag */
    public enum Tag {
        FIELD_NAME,
        OTHER
    }

    private PropertiesSearchMode() {
    }

    private PropertiesSearchMode withTag(Tag tag) {
        PropertiesSearchMode propertiesSearchMode = new PropertiesSearchMode();
        propertiesSearchMode._tag = tag;
        return propertiesSearchMode;
    }

    private PropertiesSearchMode withTagAndFieldName(Tag tag, String str) {
        PropertiesSearchMode propertiesSearchMode = new PropertiesSearchMode();
        propertiesSearchMode._tag = tag;
        propertiesSearchMode.fieldNameValue = str;
        return propertiesSearchMode;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isFieldName() {
        return this._tag == Tag.FIELD_NAME;
    }

    public static PropertiesSearchMode fieldName(String str) {
        if (str != null) {
            return new PropertiesSearchMode().withTagAndFieldName(Tag.FIELD_NAME, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getFieldNameValue() {
        if (this._tag == Tag.FIELD_NAME) {
            return this.fieldNameValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FIELD_NAME, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.fieldNameValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof PropertiesSearchMode)) {
            return false;
        }
        PropertiesSearchMode propertiesSearchMode = (PropertiesSearchMode) obj;
        if (this._tag != propertiesSearchMode._tag) {
            return false;
        }
        switch (this._tag) {
            case FIELD_NAME:
                String str = this.fieldNameValue;
                String str2 = propertiesSearchMode.fieldNameValue;
                if (str != str2 && !str.equals(str2)) {
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
