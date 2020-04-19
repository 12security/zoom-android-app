package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.UserNameLogInfo */
public class UserNameLogInfo {
    protected final String givenName;
    protected final String locale;
    protected final String surname;

    /* renamed from: com.dropbox.core.v2.teamlog.UserNameLogInfo$Serializer */
    static class Serializer extends StructSerializer<UserNameLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserNameLogInfo userNameLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("given_name");
            StoneSerializers.string().serialize(userNameLogInfo.givenName, jsonGenerator);
            jsonGenerator.writeFieldName("surname");
            StoneSerializers.string().serialize(userNameLogInfo.surname, jsonGenerator);
            if (userNameLogInfo.locale != null) {
                jsonGenerator.writeFieldName(OAuth.LOCALE);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(userNameLogInfo.locale, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserNameLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("given_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("surname".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (OAuth.LOCALE.equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"given_name\" missing.");
                } else if (str3 != null) {
                    UserNameLogInfo userNameLogInfo = new UserNameLogInfo(str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(userNameLogInfo, userNameLogInfo.toStringMultiline());
                    return userNameLogInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"surname\" missing.");
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

    public UserNameLogInfo(String str, String str2, String str3) {
        if (str != null) {
            this.givenName = str;
            if (str2 != null) {
                this.surname = str2;
                this.locale = str3;
                return;
            }
            throw new IllegalArgumentException("Required value for 'surname' is null");
        }
        throw new IllegalArgumentException("Required value for 'givenName' is null");
    }

    public UserNameLogInfo(String str, String str2) {
        this(str, str2, null);
    }

    public String getGivenName() {
        return this.givenName;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getLocale() {
        return this.locale;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.givenName, this.surname, this.locale});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (r2.equals(r5) == false) goto L_0x003f;
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
            if (r2 == 0) goto L_0x0041
            com.dropbox.core.v2.teamlog.UserNameLogInfo r5 = (com.dropbox.core.p005v2.teamlog.UserNameLogInfo) r5
            java.lang.String r2 = r4.givenName
            java.lang.String r3 = r5.givenName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0024:
            java.lang.String r2 = r4.surname
            java.lang.String r3 = r5.surname
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0030:
            java.lang.String r2 = r4.locale
            java.lang.String r5 = r5.locale
            if (r2 == r5) goto L_0x0040
            if (r2 == 0) goto L_0x003f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r0 = 0
        L_0x0040:
            return r0
        L_0x0041:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.UserNameLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
