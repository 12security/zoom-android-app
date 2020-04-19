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

/* renamed from: com.dropbox.core.v2.fileproperties.PropertyFieldTemplate */
public class PropertyFieldTemplate {
    protected final String description;
    protected final String name;
    protected final PropertyType type;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertyFieldTemplate$Serializer */
    static class Serializer extends StructSerializer<PropertyFieldTemplate> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertyFieldTemplate propertyFieldTemplate, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(propertyFieldTemplate.name, jsonGenerator);
            jsonGenerator.writeFieldName(BoxItem.FIELD_DESCRIPTION);
            StoneSerializers.string().serialize(propertyFieldTemplate.description, jsonGenerator);
            jsonGenerator.writeFieldName("type");
            Serializer.INSTANCE.serialize(propertyFieldTemplate.type, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PropertyFieldTemplate deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                PropertyType propertyType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (BoxItem.FIELD_DESCRIPTION.equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("type".equals(currentName)) {
                        propertyType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"description\" missing.");
                } else if (propertyType != null) {
                    PropertyFieldTemplate propertyFieldTemplate = new PropertyFieldTemplate(str2, str3, propertyType);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(propertyFieldTemplate, propertyFieldTemplate.toStringMultiline());
                    return propertyFieldTemplate;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"type\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public PropertyFieldTemplate(String str, String str2, PropertyType propertyType) {
        if (str != null) {
            this.name = str;
            if (str2 != null) {
                this.description = str2;
                if (propertyType != null) {
                    this.type = propertyType;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'type' is null");
            }
            throw new IllegalArgumentException("Required value for 'description' is null");
        }
        throw new IllegalArgumentException("Required value for 'name' is null");
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public PropertyType getType() {
        return this.type;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.name, this.description, this.type});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r5) == false) goto L_0x003d;
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
            if (r2 == 0) goto L_0x003f
            com.dropbox.core.v2.fileproperties.PropertyFieldTemplate r5 = (com.dropbox.core.p005v2.fileproperties.PropertyFieldTemplate) r5
            java.lang.String r2 = r4.name
            java.lang.String r3 = r5.name
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0024:
            java.lang.String r2 = r4.description
            java.lang.String r3 = r5.description
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0030:
            com.dropbox.core.v2.fileproperties.PropertyType r2 = r4.type
            com.dropbox.core.v2.fileproperties.PropertyType r5 = r5.type
            if (r2 == r5) goto L_0x003e
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = 0
        L_0x003e:
            return r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.PropertyFieldTemplate.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
