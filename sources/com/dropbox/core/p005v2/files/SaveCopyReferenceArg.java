package com.dropbox.core.p005v2.files;

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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.SaveCopyReferenceArg */
class SaveCopyReferenceArg {
    protected final String copyReference;
    protected final String path;

    /* renamed from: com.dropbox.core.v2.files.SaveCopyReferenceArg$Serializer */
    static class Serializer extends StructSerializer<SaveCopyReferenceArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SaveCopyReferenceArg saveCopyReferenceArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("copy_reference");
            StoneSerializers.string().serialize(saveCopyReferenceArg.copyReference, jsonGenerator);
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(saveCopyReferenceArg.path, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SaveCopyReferenceArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("copy_reference".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("path".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"copy_reference\" missing.");
                } else if (str3 != null) {
                    SaveCopyReferenceArg saveCopyReferenceArg = new SaveCopyReferenceArg(str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(saveCopyReferenceArg, saveCopyReferenceArg.toStringMultiline());
                    return saveCopyReferenceArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
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

    public SaveCopyReferenceArg(String str, String str2) {
        if (str != null) {
            this.copyReference = str;
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'path' is null");
            } else if (Pattern.matches("/(.|[\\r\\n])*", str2)) {
                this.path = str2;
            } else {
                throw new IllegalArgumentException("String 'path' does not match pattern");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'copyReference' is null");
        }
    }

    public String getCopyReference() {
        return this.copyReference;
    }

    public String getPath() {
        return this.path;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.copyReference, this.path});
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
            com.dropbox.core.v2.files.SaveCopyReferenceArg r5 = (com.dropbox.core.p005v2.files.SaveCopyReferenceArg) r5
            java.lang.String r2 = r4.copyReference
            java.lang.String r3 = r5.copyReference
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.path
            java.lang.String r5 = r5.path
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.SaveCopyReferenceArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
