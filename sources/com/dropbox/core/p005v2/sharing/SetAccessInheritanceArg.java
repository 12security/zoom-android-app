package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.SetAccessInheritanceArg */
class SetAccessInheritanceArg {
    protected final AccessInheritance accessInheritance;
    protected final String sharedFolderId;

    /* renamed from: com.dropbox.core.v2.sharing.SetAccessInheritanceArg$Serializer */
    static class Serializer extends StructSerializer<SetAccessInheritanceArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SetAccessInheritanceArg setAccessInheritanceArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(setAccessInheritanceArg.sharedFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("access_inheritance");
            Serializer.INSTANCE.serialize(setAccessInheritanceArg.accessInheritance, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SetAccessInheritanceArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AccessInheritance accessInheritance = AccessInheritance.INHERIT;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("access_inheritance".equals(currentName)) {
                        accessInheritance = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    SetAccessInheritanceArg setAccessInheritanceArg = new SetAccessInheritanceArg(str2, accessInheritance);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(setAccessInheritanceArg, setAccessInheritanceArg.toStringMultiline());
                    return setAccessInheritanceArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"shared_folder_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SetAccessInheritanceArg(String str, AccessInheritance accessInheritance2) {
        if (accessInheritance2 != null) {
            this.accessInheritance = accessInheritance2;
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.sharedFolderId = str;
            } else {
                throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'accessInheritance' is null");
        }
    }

    public SetAccessInheritanceArg(String str) {
        this(str, AccessInheritance.INHERIT);
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public AccessInheritance getAccessInheritance() {
        return this.accessInheritance;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accessInheritance, this.sharedFolderId});
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
            com.dropbox.core.v2.sharing.SetAccessInheritanceArg r5 = (com.dropbox.core.p005v2.sharing.SetAccessInheritanceArg) r5
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.sharing.AccessInheritance r2 = r4.accessInheritance
            com.dropbox.core.v2.sharing.AccessInheritance r5 = r5.accessInheritance
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SetAccessInheritanceArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
