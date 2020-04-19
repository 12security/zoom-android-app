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

/* renamed from: com.dropbox.core.v2.teamlog.SsoChangeLogoutUrlDetails */
public class SsoChangeLogoutUrlDetails {
    protected final String newValue;
    protected final String previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SsoChangeLogoutUrlDetails$Builder */
    public static class Builder {
        protected String newValue = null;
        protected String previousValue = null;

        protected Builder() {
        }

        public Builder withPreviousValue(String str) {
            this.previousValue = str;
            return this;
        }

        public Builder withNewValue(String str) {
            this.newValue = str;
            return this;
        }

        public SsoChangeLogoutUrlDetails build() {
            return new SsoChangeLogoutUrlDetails(this.previousValue, this.newValue);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.SsoChangeLogoutUrlDetails$Serializer */
    static class Serializer extends StructSerializer<SsoChangeLogoutUrlDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SsoChangeLogoutUrlDetails ssoChangeLogoutUrlDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (ssoChangeLogoutUrlDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(ssoChangeLogoutUrlDetails.previousValue, jsonGenerator);
            }
            if (ssoChangeLogoutUrlDetails.newValue != null) {
                jsonGenerator.writeFieldName("new_value");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(ssoChangeLogoutUrlDetails.newValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SsoChangeLogoutUrlDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("previous_value".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_value".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SsoChangeLogoutUrlDetails ssoChangeLogoutUrlDetails = new SsoChangeLogoutUrlDetails(str2, str3);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(ssoChangeLogoutUrlDetails, ssoChangeLogoutUrlDetails.toStringMultiline());
                return ssoChangeLogoutUrlDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SsoChangeLogoutUrlDetails(String str, String str2) {
        this.previousValue = str;
        this.newValue = str2;
    }

    public SsoChangeLogoutUrlDetails() {
        this(null, null);
    }

    public String getPreviousValue() {
        return this.previousValue;
    }

    public String getNewValue() {
        return this.newValue;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousValue, this.newValue});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (r2.equals(r5) == false) goto L_0x0035;
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
            if (r2 == 0) goto L_0x0037
            com.dropbox.core.v2.teamlog.SsoChangeLogoutUrlDetails r5 = (com.dropbox.core.p005v2.teamlog.SsoChangeLogoutUrlDetails) r5
            java.lang.String r2 = r4.previousValue
            java.lang.String r3 = r5.previousValue
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.lang.String r2 = r4.newValue
            java.lang.String r5 = r5.newValue
            if (r2 == r5) goto L_0x0036
            if (r2 == 0) goto L_0x0035
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r0 = 0
        L_0x0036:
            return r0
        L_0x0037:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SsoChangeLogoutUrlDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
