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

/* renamed from: com.dropbox.core.v2.fileproperties.OverwritePropertyGroupArg */
public class OverwritePropertyGroupArg {
    protected final String path;
    protected final List<PropertyGroup> propertyGroups;

    /* renamed from: com.dropbox.core.v2.fileproperties.OverwritePropertyGroupArg$Serializer */
    public static class Serializer extends StructSerializer<OverwritePropertyGroupArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(OverwritePropertyGroupArg overwritePropertyGroupArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(overwritePropertyGroupArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("property_groups");
            StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE).serialize(overwritePropertyGroupArg.propertyGroups, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public OverwritePropertyGroupArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("property_groups".equals(currentName)) {
                        list = (List) StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
                } else if (list != null) {
                    OverwritePropertyGroupArg overwritePropertyGroupArg = new OverwritePropertyGroupArg(str2, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(overwritePropertyGroupArg, overwritePropertyGroupArg.toStringMultiline());
                    return overwritePropertyGroupArg;
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

    public OverwritePropertyGroupArg(String str, List<PropertyGroup> list) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("/(.|[\\r\\n])*|id:.*|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            if (list == null) {
                throw new IllegalArgumentException("Required value for 'propertyGroups' is null");
            } else if (list.size() >= 1) {
                for (PropertyGroup propertyGroup : list) {
                    if (propertyGroup == null) {
                        throw new IllegalArgumentException("An item in list 'propertyGroups' is null");
                    }
                }
                this.propertyGroups = list;
            } else {
                throw new IllegalArgumentException("List 'propertyGroups' has fewer than 1 items");
            }
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public String getPath() {
        return this.path;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return this.propertyGroups;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, this.propertyGroups});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.fileproperties.OverwritePropertyGroupArg r5 = (com.dropbox.core.p005v2.fileproperties.OverwritePropertyGroupArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r2 = r4.propertyGroups
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r5 = r5.propertyGroups
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.OverwritePropertyGroupArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
