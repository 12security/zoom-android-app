package com.dropbox.core.p005v2.users;

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

/* renamed from: com.dropbox.core.v2.users.Name */
public class Name {
    protected final String abbreviatedName;
    protected final String displayName;
    protected final String familiarName;
    protected final String givenName;
    protected final String surname;

    /* renamed from: com.dropbox.core.v2.users.Name$Serializer */
    public static class Serializer extends StructSerializer<Name> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(Name name, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("given_name");
            StoneSerializers.string().serialize(name.givenName, jsonGenerator);
            jsonGenerator.writeFieldName("surname");
            StoneSerializers.string().serialize(name.surname, jsonGenerator);
            jsonGenerator.writeFieldName("familiar_name");
            StoneSerializers.string().serialize(name.familiarName, jsonGenerator);
            jsonGenerator.writeFieldName("display_name");
            StoneSerializers.string().serialize(name.displayName, jsonGenerator);
            jsonGenerator.writeFieldName("abbreviated_name");
            StoneSerializers.string().serialize(name.abbreviatedName, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public Name deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("given_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("surname".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("familiar_name".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str5 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("abbreviated_name".equals(currentName)) {
                        str6 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"given_name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"surname\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"familiar_name\" missing.");
                } else if (str5 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"display_name\" missing.");
                } else if (str6 != null) {
                    Name name = new Name(str2, str3, str4, str5, str6);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(name, name.toStringMultiline());
                    return name;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"abbreviated_name\" missing.");
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

    public Name(String str, String str2, String str3, String str4, String str5) {
        if (str != null) {
            this.givenName = str;
            if (str2 != null) {
                this.surname = str2;
                if (str3 != null) {
                    this.familiarName = str3;
                    if (str4 != null) {
                        this.displayName = str4;
                        if (str5 != null) {
                            this.abbreviatedName = str5;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'abbreviatedName' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'displayName' is null");
                }
                throw new IllegalArgumentException("Required value for 'familiarName' is null");
            }
            throw new IllegalArgumentException("Required value for 'surname' is null");
        }
        throw new IllegalArgumentException("Required value for 'givenName' is null");
    }

    public String getGivenName() {
        return this.givenName;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getFamiliarName() {
        return this.familiarName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getAbbreviatedName() {
        return this.abbreviatedName;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.givenName, this.surname, this.familiarName, this.displayName, this.abbreviatedName});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0052, code lost:
        if (r2.equals(r5) == false) goto L_0x0055;
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
            if (r2 == 0) goto L_0x0057
            com.dropbox.core.v2.users.Name r5 = (com.dropbox.core.p005v2.users.Name) r5
            java.lang.String r2 = r4.givenName
            java.lang.String r3 = r5.givenName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0055
        L_0x0024:
            java.lang.String r2 = r4.surname
            java.lang.String r3 = r5.surname
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0055
        L_0x0030:
            java.lang.String r2 = r4.familiarName
            java.lang.String r3 = r5.familiarName
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0055
        L_0x003c:
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            if (r2 == r3) goto L_0x0048
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0055
        L_0x0048:
            java.lang.String r2 = r4.abbreviatedName
            java.lang.String r5 = r5.abbreviatedName
            if (r2 == r5) goto L_0x0056
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0055
            goto L_0x0056
        L_0x0055:
            r0 = 0
        L_0x0056:
            return r0
        L_0x0057:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.users.Name.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
