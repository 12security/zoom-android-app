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

/* renamed from: com.dropbox.core.v2.users.GetAccountArg */
public class GetAccountArg {
    protected final String accountId;

    /* renamed from: com.dropbox.core.v2.users.GetAccountArg$Serializer */
    public static class Serializer extends StructSerializer<GetAccountArg> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(GetAccountArg getAccountArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("account_id");
            StoneSerializers.string().serialize(getAccountArg.accountId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetAccountArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("account_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    GetAccountArg getAccountArg = new GetAccountArg(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getAccountArg, getAccountArg.toStringMultiline());
                    return getAccountArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"account_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetAccountArg(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'accountId' is null");
        } else if (str.length() < 40) {
            throw new IllegalArgumentException("String 'accountId' is shorter than 40");
        } else if (str.length() <= 40) {
            this.accountId = str;
        } else {
            throw new IllegalArgumentException("String 'accountId' is longer than 40");
        }
    }

    public String getAccountId() {
        return this.accountId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accountId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        GetAccountArg getAccountArg = (GetAccountArg) obj;
        String str = this.accountId;
        String str2 = getAccountArg.accountId;
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
