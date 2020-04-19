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

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchMatch */
public class PropertiesSearchMatch {

    /* renamed from: id */
    protected final String f76id;
    protected final boolean isDeleted;
    protected final String path;
    protected final List<PropertyGroup> propertyGroups;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchMatch$Serializer */
    static class Serializer extends StructSerializer<PropertiesSearchMatch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertiesSearchMatch propertiesSearchMatch, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(propertiesSearchMatch.f76id, jsonGenerator);
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(propertiesSearchMatch.path, jsonGenerator);
            jsonGenerator.writeFieldName("is_deleted");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(propertiesSearchMatch.isDeleted), jsonGenerator);
            jsonGenerator.writeFieldName("property_groups");
            StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE).serialize(propertiesSearchMatch.propertyGroups, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PropertiesSearchMatch deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                Boolean bool = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("path".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("is_deleted".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("property_groups".equals(currentName)) {
                        list = (List) StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"is_deleted\" missing.");
                } else if (list != null) {
                    PropertiesSearchMatch propertiesSearchMatch = new PropertiesSearchMatch(str2, str3, bool.booleanValue(), list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(propertiesSearchMatch, propertiesSearchMatch.toStringMultiline());
                    return propertiesSearchMatch;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"property_groups\" missing.");
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

    public PropertiesSearchMatch(String str, String str2, boolean z, List<PropertyGroup> list) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'id' is null");
        } else if (str.length() >= 1) {
            this.f76id = str;
            if (str2 != null) {
                this.path = str2;
                this.isDeleted = z;
                if (list != null) {
                    for (PropertyGroup propertyGroup : list) {
                        if (propertyGroup == null) {
                            throw new IllegalArgumentException("An item in list 'propertyGroups' is null");
                        }
                    }
                    this.propertyGroups = list;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'propertyGroups' is null");
            }
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else {
            throw new IllegalArgumentException("String 'id' is shorter than 1");
        }
    }

    public String getId() {
        return this.f76id;
    }

    public String getPath() {
        return this.path;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return this.propertyGroups;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f76id, this.path, Boolean.valueOf(this.isDeleted), this.propertyGroups});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0040, code lost:
        if (r2.equals(r5) == false) goto L_0x0043;
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
            if (r2 == 0) goto L_0x0045
            com.dropbox.core.v2.fileproperties.PropertiesSearchMatch r5 = (com.dropbox.core.p005v2.fileproperties.PropertiesSearchMatch) r5
            java.lang.String r2 = r4.f76id
            java.lang.String r3 = r5.f76id
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0024:
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0030:
            boolean r2 = r4.isDeleted
            boolean r3 = r5.isDeleted
            if (r2 != r3) goto L_0x0043
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r2 = r4.propertyGroups
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r5 = r5.propertyGroups
            if (r2 == r5) goto L_0x0044
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.PropertiesSearchMatch.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
