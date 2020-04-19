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

/* renamed from: com.dropbox.core.v2.teamlog.SsoRemoveLoginUrlDetails */
public class SsoRemoveLoginUrlDetails {
    protected final String previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SsoRemoveLoginUrlDetails$Serializer */
    static class Serializer extends StructSerializer<SsoRemoveLoginUrlDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SsoRemoveLoginUrlDetails ssoRemoveLoginUrlDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("previous_value");
            StoneSerializers.string().serialize(ssoRemoveLoginUrlDetails.previousValue, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SsoRemoveLoginUrlDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("previous_value".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    SsoRemoveLoginUrlDetails ssoRemoveLoginUrlDetails = new SsoRemoveLoginUrlDetails(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(ssoRemoveLoginUrlDetails, ssoRemoveLoginUrlDetails.toStringMultiline());
                    return ssoRemoveLoginUrlDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"previous_value\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SsoRemoveLoginUrlDetails(String str) {
        if (str != null) {
            this.previousValue = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'previousValue' is null");
    }

    public String getPreviousValue() {
        return this.previousValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SsoRemoveLoginUrlDetails ssoRemoveLoginUrlDetails = (SsoRemoveLoginUrlDetails) obj;
        String str = this.previousValue;
        String str2 = ssoRemoveLoginUrlDetails.previousValue;
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
