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

/* renamed from: com.dropbox.core.v2.fileproperties.UpdatePropertiesArg */
public class UpdatePropertiesArg {
    protected final String path;
    protected final List<PropertyGroupUpdate> updatePropertyGroups;

    /* renamed from: com.dropbox.core.v2.fileproperties.UpdatePropertiesArg$Serializer */
    public static class Serializer extends StructSerializer<UpdatePropertiesArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(UpdatePropertiesArg updatePropertiesArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(updatePropertiesArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("update_property_groups");
            StoneSerializers.list(Serializer.INSTANCE).serialize(updatePropertiesArg.updatePropertyGroups, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UpdatePropertiesArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    } else if ("update_property_groups".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
                } else if (list != null) {
                    UpdatePropertiesArg updatePropertiesArg = new UpdatePropertiesArg(str2, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(updatePropertiesArg, updatePropertiesArg.toStringMultiline());
                    return updatePropertiesArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"update_property_groups\" missing.");
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

    public UpdatePropertiesArg(String str, List<PropertyGroupUpdate> list) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("/(.|[\\r\\n])*|id:.*|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            if (list != null) {
                for (PropertyGroupUpdate propertyGroupUpdate : list) {
                    if (propertyGroupUpdate == null) {
                        throw new IllegalArgumentException("An item in list 'updatePropertyGroups' is null");
                    }
                }
                this.updatePropertyGroups = list;
                return;
            }
            throw new IllegalArgumentException("Required value for 'updatePropertyGroups' is null");
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public String getPath() {
        return this.path;
    }

    public List<PropertyGroupUpdate> getUpdatePropertyGroups() {
        return this.updatePropertyGroups;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, this.updatePropertyGroups});
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
            com.dropbox.core.v2.fileproperties.UpdatePropertiesArg r5 = (com.dropbox.core.p005v2.fileproperties.UpdatePropertiesArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroupUpdate> r2 = r4.updatePropertyGroups
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroupUpdate> r5 = r5.updatePropertyGroups
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.UpdatePropertiesArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
