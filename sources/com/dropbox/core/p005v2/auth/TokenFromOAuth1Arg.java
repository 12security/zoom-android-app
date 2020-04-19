package com.dropbox.core.p005v2.auth;

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

/* renamed from: com.dropbox.core.v2.auth.TokenFromOAuth1Arg */
class TokenFromOAuth1Arg {
    protected final String oauth1Token;
    protected final String oauth1TokenSecret;

    /* renamed from: com.dropbox.core.v2.auth.TokenFromOAuth1Arg$Serializer */
    static class Serializer extends StructSerializer<TokenFromOAuth1Arg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TokenFromOAuth1Arg tokenFromOAuth1Arg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("oauth1_token");
            StoneSerializers.string().serialize(tokenFromOAuth1Arg.oauth1Token, jsonGenerator);
            jsonGenerator.writeFieldName("oauth1_token_secret");
            StoneSerializers.string().serialize(tokenFromOAuth1Arg.oauth1TokenSecret, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TokenFromOAuth1Arg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("oauth1_token".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("oauth1_token_secret".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"oauth1_token\" missing.");
                } else if (str3 != null) {
                    TokenFromOAuth1Arg tokenFromOAuth1Arg = new TokenFromOAuth1Arg(str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(tokenFromOAuth1Arg, tokenFromOAuth1Arg.toStringMultiline());
                    return tokenFromOAuth1Arg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"oauth1_token_secret\" missing.");
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

    public TokenFromOAuth1Arg(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'oauth1Token' is null");
        } else if (str.length() >= 1) {
            this.oauth1Token = str;
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'oauth1TokenSecret' is null");
            } else if (str2.length() >= 1) {
                this.oauth1TokenSecret = str2;
            } else {
                throw new IllegalArgumentException("String 'oauth1TokenSecret' is shorter than 1");
            }
        } else {
            throw new IllegalArgumentException("String 'oauth1Token' is shorter than 1");
        }
    }

    public String getOauth1Token() {
        return this.oauth1Token;
    }

    public String getOauth1TokenSecret() {
        return this.oauth1TokenSecret;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.oauth1Token, this.oauth1TokenSecret});
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
            com.dropbox.core.v2.auth.TokenFromOAuth1Arg r5 = (com.dropbox.core.p005v2.auth.TokenFromOAuth1Arg) r5
            java.lang.String r2 = r4.oauth1Token
            java.lang.String r3 = r5.oauth1Token
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.oauth1TokenSecret
            java.lang.String r5 = r5.oauth1TokenSecret
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.auth.TokenFromOAuth1Arg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
