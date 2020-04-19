package com.dropbox.core.p005v2.fileproperties;

import com.box.androidsdk.content.models.BoxItem;
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

/* renamed from: com.dropbox.core.v2.fileproperties.UpdateTemplateArg */
public class UpdateTemplateArg {
    protected final List<PropertyFieldTemplate> addFields;
    protected final String description;
    protected final String name;
    protected final String templateId;

    /* renamed from: com.dropbox.core.v2.fileproperties.UpdateTemplateArg$Builder */
    public static class Builder {
        protected List<PropertyFieldTemplate> addFields;
        protected String description;
        protected String name;
        protected final String templateId;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'templateId' is null");
            } else if (str.length() < 1) {
                throw new IllegalArgumentException("String 'templateId' is shorter than 1");
            } else if (Pattern.matches("(/|ptid:).*", str)) {
                this.templateId = str;
                this.name = null;
                this.description = null;
                this.addFields = null;
            } else {
                throw new IllegalArgumentException("String 'templateId' does not match pattern");
            }
        }

        public Builder withName(String str) {
            this.name = str;
            return this;
        }

        public Builder withDescription(String str) {
            this.description = str;
            return this;
        }

        public Builder withAddFields(List<PropertyFieldTemplate> list) {
            if (list != null) {
                for (PropertyFieldTemplate propertyFieldTemplate : list) {
                    if (propertyFieldTemplate == null) {
                        throw new IllegalArgumentException("An item in list 'addFields' is null");
                    }
                }
            }
            this.addFields = list;
            return this;
        }

        public UpdateTemplateArg build() {
            return new UpdateTemplateArg(this.templateId, this.name, this.description, this.addFields);
        }
    }

    /* renamed from: com.dropbox.core.v2.fileproperties.UpdateTemplateArg$Serializer */
    public static class Serializer extends StructSerializer<UpdateTemplateArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(UpdateTemplateArg updateTemplateArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("template_id");
            StoneSerializers.string().serialize(updateTemplateArg.templateId, jsonGenerator);
            if (updateTemplateArg.name != null) {
                jsonGenerator.writeFieldName("name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(updateTemplateArg.name, jsonGenerator);
            }
            if (updateTemplateArg.description != null) {
                jsonGenerator.writeFieldName(BoxItem.FIELD_DESCRIPTION);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(updateTemplateArg.description, jsonGenerator);
            }
            if (updateTemplateArg.addFields != null) {
                jsonGenerator.writeFieldName("add_fields");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(updateTemplateArg.addFields, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UpdateTemplateArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                String str4 = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("template_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("name".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if (BoxItem.FIELD_DESCRIPTION.equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("add_fields".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    UpdateTemplateArg updateTemplateArg = new UpdateTemplateArg(str2, str3, str4, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(updateTemplateArg, updateTemplateArg.toStringMultiline());
                    return updateTemplateArg;
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

    public UpdateTemplateArg(String str, String str2, String str3, List<PropertyFieldTemplate> list) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'templateId' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'templateId' is shorter than 1");
        } else if (Pattern.matches("(/|ptid:).*", str)) {
            this.templateId = str;
            this.name = str2;
            this.description = str3;
            if (list != null) {
                for (PropertyFieldTemplate propertyFieldTemplate : list) {
                    if (propertyFieldTemplate == null) {
                        throw new IllegalArgumentException("An item in list 'addFields' is null");
                    }
                }
            }
            this.addFields = list;
        } else {
            throw new IllegalArgumentException("String 'templateId' does not match pattern");
        }
    }

    public UpdateTemplateArg(String str) {
        this(str, null, null, null);
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<PropertyFieldTemplate> getAddFields() {
        return this.addFields;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.templateId, this.name, this.description, this.addFields});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004c, code lost:
        if (r2.equals(r5) == false) goto L_0x004f;
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
            if (r2 == 0) goto L_0x0051
            com.dropbox.core.v2.fileproperties.UpdateTemplateArg r5 = (com.dropbox.core.p005v2.fileproperties.UpdateTemplateArg) r5
            java.lang.String r2 = r4.templateId
            java.lang.String r3 = r5.templateId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0024:
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x004f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0032:
            java.lang.String r2 = r4.description
            java.lang.String r3 = r5.description
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x004f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0040:
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyFieldTemplate> r2 = r4.addFields
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyFieldTemplate> r5 = r5.addFields
            if (r2 == r5) goto L_0x0050
            if (r2 == 0) goto L_0x004f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x004f
            goto L_0x0050
        L_0x004f:
            r0 = 0
        L_0x0050:
            return r0
        L_0x0051:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.UpdateTemplateArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
