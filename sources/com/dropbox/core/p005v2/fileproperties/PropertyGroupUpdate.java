package com.dropbox.core.p005v2.fileproperties;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.fileproperties.PropertyGroupUpdate */
public class PropertyGroupUpdate {
    protected final List<PropertyField> addOrUpdateFields;
    protected final List<String> removeFields;
    protected final String templateId;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertyGroupUpdate$Builder */
    public static class Builder {
        protected List<PropertyField> addOrUpdateFields;
        protected List<String> removeFields;
        protected final String templateId;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'templateId' is null");
            } else if (str.length() < 1) {
                throw new IllegalArgumentException("String 'templateId' is shorter than 1");
            } else if (Pattern.matches("(/|ptid:).*", str)) {
                this.templateId = str;
                this.addOrUpdateFields = null;
                this.removeFields = null;
            } else {
                throw new IllegalArgumentException("String 'templateId' does not match pattern");
            }
        }

        public Builder withAddOrUpdateFields(List<PropertyField> list) {
            if (list != null) {
                for (PropertyField propertyField : list) {
                    if (propertyField == null) {
                        throw new IllegalArgumentException("An item in list 'addOrUpdateFields' is null");
                    }
                }
            }
            this.addOrUpdateFields = list;
            return this;
        }

        public Builder withRemoveFields(List<String> list) {
            if (list != null) {
                for (String str : list) {
                    if (str == null) {
                        throw new IllegalArgumentException("An item in list 'removeFields' is null");
                    }
                }
            }
            this.removeFields = list;
            return this;
        }

        public PropertyGroupUpdate build() {
            return new PropertyGroupUpdate(this.templateId, this.addOrUpdateFields, this.removeFields);
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertyGroupUpdate$Serializer */
    static class Serializer extends StructSerializer<PropertyGroupUpdate> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertyGroupUpdate propertyGroupUpdate, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("template_id");
            StoneSerializers.string().serialize(propertyGroupUpdate.templateId, jsonGenerator);
            if (propertyGroupUpdate.addOrUpdateFields != null) {
                jsonGenerator.writeFieldName("add_or_update_fields");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(propertyGroupUpdate.addOrUpdateFields, jsonGenerator);
            }
            if (propertyGroupUpdate.removeFields != null) {
                jsonGenerator.writeFieldName("remove_fields");
                StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).serialize(propertyGroupUpdate.removeFields, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PropertyGroupUpdate deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                List list2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("template_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("add_or_update_fields".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("remove_fields".equals(currentName)) {
                        list2 = (List) StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    PropertyGroupUpdate propertyGroupUpdate = new PropertyGroupUpdate(str2, list, list2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(propertyGroupUpdate, propertyGroupUpdate.toStringMultiline());
                    return propertyGroupUpdate;
                }
                throw new JsonParseException(jsonParser, "Required field \"template_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PropertyGroupUpdate(String str, List<PropertyField> list, List<String> list2) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'templateId' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'templateId' is shorter than 1");
        } else if (Pattern.matches("(/|ptid:).*", str)) {
            this.templateId = str;
            if (list != null) {
                for (PropertyField propertyField : list) {
                    if (propertyField == null) {
                        throw new IllegalArgumentException("An item in list 'addOrUpdateFields' is null");
                    }
                }
            }
            this.addOrUpdateFields = list;
            if (list2 != null) {
                for (String str2 : list2) {
                    if (str2 == null) {
                        throw new IllegalArgumentException("An item in list 'removeFields' is null");
                    }
                }
            }
            this.removeFields = list2;
        } else {
            throw new IllegalArgumentException("String 'templateId' does not match pattern");
        }
    }

    public PropertyGroupUpdate(String str) {
        this(str, null, null);
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public List<PropertyField> getAddOrUpdateFields() {
        return this.addOrUpdateFields;
    }

    public List<String> getRemoveFields() {
        return this.removeFields;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.templateId, this.addOrUpdateFields, this.removeFields});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2.equals(r5) == false) goto L_0x0041;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.fileproperties.PropertyGroupUpdate r5 = (com.dropbox.core.p005v2.fileproperties.PropertyGroupUpdate) r5
            java.lang.String r2 = r4.templateId
            java.lang.String r3 = r5.templateId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyField> r2 = r4.addOrUpdateFields
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyField> r3 = r5.addOrUpdateFields
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            java.util.List<java.lang.String> r2 = r4.removeFields
            java.util.List<java.lang.String> r5 = r5.removeFields
            if (r2 == r5) goto L_0x0042
            if (r2 == 0) goto L_0x0041
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.PropertyGroupUpdate.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
