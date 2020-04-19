package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchError */
public final class PropertiesSearchError {
    public static final PropertiesSearchError OTHER = new PropertiesSearchError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookUpPropertiesError propertyGroupLookupValue;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchError$Serializer */
    static class Serializer extends UnionSerializer<PropertiesSearchError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertiesSearchError propertiesSearchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06411.f75xf3c146fc[propertiesSearchError.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("property_group_lookup", jsonGenerator);
            jsonGenerator.writeFieldName("property_group_lookup");
            com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError.Serializer.INSTANCE.serialize(propertiesSearchError.propertyGroupLookupValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public PropertiesSearchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PropertiesSearchError propertiesSearchError;
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
                if ("property_group_lookup".equals(str)) {
                    expectField("property_group_lookup", jsonParser);
                    propertiesSearchError = PropertiesSearchError.propertyGroupLookup(com.dropbox.core.p005v2.fileproperties.LookUpPropertiesError.Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    propertiesSearchError = PropertiesSearchError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return propertiesSearchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchError$Tag */
    public enum Tag {
        PROPERTY_GROUP_LOOKUP,
        OTHER
    }

    private PropertiesSearchError() {
    }

    private PropertiesSearchError withTag(Tag tag) {
        PropertiesSearchError propertiesSearchError = new PropertiesSearchError();
        propertiesSearchError._tag = tag;
        return propertiesSearchError;
    }

    private PropertiesSearchError withTagAndPropertyGroupLookup(Tag tag, LookUpPropertiesError lookUpPropertiesError) {
        PropertiesSearchError propertiesSearchError = new PropertiesSearchError();
        propertiesSearchError._tag = tag;
        propertiesSearchError.propertyGroupLookupValue = lookUpPropertiesError;
        return propertiesSearchError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPropertyGroupLookup() {
        return this._tag == Tag.PROPERTY_GROUP_LOOKUP;
    }

    public static PropertiesSearchError propertyGroupLookup(LookUpPropertiesError lookUpPropertiesError) {
        if (lookUpPropertiesError != null) {
            return new PropertiesSearchError().withTagAndPropertyGroupLookup(Tag.PROPERTY_GROUP_LOOKUP, lookUpPropertiesError);
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

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.propertyGroupLookupValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof PropertiesSearchError)) {
            return false;
        }
        PropertiesSearchError propertiesSearchError = (PropertiesSearchError) obj;
        if (this._tag != propertiesSearchError._tag) {
            return false;
        }
        switch (this._tag) {
            case PROPERTY_GROUP_LOOKUP:
                LookUpPropertiesError lookUpPropertiesError = this.propertyGroupLookupValue;
                LookUpPropertiesError lookUpPropertiesError2 = propertiesSearchError.propertyGroupLookupValue;
                if (lookUpPropertiesError != lookUpPropertiesError2 && !lookUpPropertiesError.equals(lookUpPropertiesError2)) {
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
