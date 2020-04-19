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

/* renamed from: com.dropbox.core.v2.teamlog.LoginSuccessDetails */
public class LoginSuccessDetails {
    protected final Boolean isEmmManaged;
    protected final LoginMethod loginMethod;

    /* renamed from: com.dropbox.core.v2.teamlog.LoginSuccessDetails$Serializer */
    static class Serializer extends StructSerializer<LoginSuccessDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(LoginSuccessDetails loginSuccessDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("login_method");
            Serializer.INSTANCE.serialize(loginSuccessDetails.loginMethod, jsonGenerator);
            if (loginSuccessDetails.isEmmManaged != null) {
                jsonGenerator.writeFieldName("is_emm_managed");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(loginSuccessDetails.isEmmManaged, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public LoginSuccessDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            LoginMethod loginMethod = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean bool = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("login_method".equals(currentName)) {
                        loginMethod = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("is_emm_managed".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (loginMethod != null) {
                    LoginSuccessDetails loginSuccessDetails = new LoginSuccessDetails(loginMethod, bool);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(loginSuccessDetails, loginSuccessDetails.toStringMultiline());
                    return loginSuccessDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"login_method\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public LoginSuccessDetails(LoginMethod loginMethod2, Boolean bool) {
        this.isEmmManaged = bool;
        if (loginMethod2 != null) {
            this.loginMethod = loginMethod2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'loginMethod' is null");
    }

    public LoginSuccessDetails(LoginMethod loginMethod2) {
        this(loginMethod2, null);
    }

    public LoginMethod getLoginMethod() {
        return this.loginMethod;
    }

    public Boolean getIsEmmManaged() {
        return this.isEmmManaged;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.isEmmManaged, this.loginMethod});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.teamlog.LoginSuccessDetails r5 = (com.dropbox.core.p005v2.teamlog.LoginSuccessDetails) r5
            com.dropbox.core.v2.teamlog.LoginMethod r2 = r4.loginMethod
            com.dropbox.core.v2.teamlog.LoginMethod r3 = r5.loginMethod
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.Boolean r2 = r4.isEmmManaged
            java.lang.Boolean r5 = r5.isEmmManaged
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.LoginSuccessDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
