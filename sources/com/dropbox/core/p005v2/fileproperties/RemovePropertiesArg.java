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

/* renamed from: com.dropbox.core.v2.fileproperties.RemovePropertiesArg */
public class RemovePropertiesArg {
    protected final String path;
    protected final List<String> propertyTemplateIds;

    /* renamed from: com.dropbox.core.v2.fileproperties.RemovePropertiesArg$Serializer */
    public static class Serializer extends StructSerializer<RemovePropertiesArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(RemovePropertiesArg removePropertiesArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(removePropertiesArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("property_template_ids");
            StoneSerializers.list(StoneSerializers.string()).serialize(removePropertiesArg.propertyTemplateIds, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RemovePropertiesArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    } else if ("property_template_ids".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
                } else if (list != null) {
                    RemovePropertiesArg removePropertiesArg = new RemovePropertiesArg(str2, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(removePropertiesArg, removePropertiesArg.toStringMultiline());
                    return removePropertiesArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"property_template_ids\" missing.");
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

    public RemovePropertiesArg(String str, List<String> list) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("/(.|[\\r\\n])*|id:.*|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            if (list != null) {
                for (String str2 : list) {
                    if (str2 == null) {
                        throw new IllegalArgumentException("An item in list 'propertyTemplateIds' is null");
                    } else if (str2.length() < 1) {
                        throw new IllegalArgumentException("Stringan item in list 'propertyTemplateIds' is shorter than 1");
                    } else if (!Pattern.matches("(/|ptid:).*", str2)) {
                        throw new IllegalArgumentException("Stringan item in list 'propertyTemplateIds' does not match pattern");
                    }
                }
                this.propertyTemplateIds = list;
                return;
            }
            throw new IllegalArgumentException("Required value for 'propertyTemplateIds' is null");
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public String getPath() {
        return this.path;
    }

    public List<String> getPropertyTemplateIds() {
        return this.propertyTemplateIds;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, this.propertyTemplateIds});
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
            com.dropbox.core.v2.fileproperties.RemovePropertiesArg r5 = (com.dropbox.core.p005v2.fileproperties.RemovePropertiesArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.util.List<java.lang.String> r2 = r4.propertyTemplateIds
            java.util.List<java.lang.String> r5 = r5.propertyTemplateIds
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.fileproperties.RemovePropertiesArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
