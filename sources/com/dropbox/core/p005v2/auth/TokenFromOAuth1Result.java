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

/* renamed from: com.dropbox.core.v2.auth.TokenFromOAuth1Result */
public class TokenFromOAuth1Result {
    protected final String oauth2Token;

    /* renamed from: com.dropbox.core.v2.auth.TokenFromOAuth1Result$Serializer */
    static class Serializer extends StructSerializer<TokenFromOAuth1Result> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TokenFromOAuth1Result tokenFromOAuth1Result, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("oauth2_token");
            StoneSerializers.string().serialize(tokenFromOAuth1Result.oauth2Token, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TokenFromOAuth1Result deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("oauth2_token".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    TokenFromOAuth1Result tokenFromOAuth1Result = new TokenFromOAuth1Result(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(tokenFromOAuth1Result, tokenFromOAuth1Result.toStringMultiline());
                    return tokenFromOAuth1Result;
                }
                throw new JsonParseException(jsonParser, "Required field \"oauth2_token\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TokenFromOAuth1Result(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'oauth2Token' is null");
        } else if (str.length() >= 1) {
            this.oauth2Token = str;
        } else {
            throw new IllegalArgumentException("String 'oauth2Token' is shorter than 1");
        }
    }

    public String getOauth2Token() {
        return this.oauth2Token;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.oauth2Token});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        TokenFromOAuth1Result tokenFromOAuth1Result = (TokenFromOAuth1Result) obj;
        String str = this.oauth2Token;
        String str2 = tokenFromOAuth1Result.oauth2Token;
        if (str != str2 && !str.equals(str2)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
