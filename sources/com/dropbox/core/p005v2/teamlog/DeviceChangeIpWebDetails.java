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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceChangeIpWebDetails */
public class DeviceChangeIpWebDetails {
    protected final String userAgent;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceChangeIpWebDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceChangeIpWebDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceChangeIpWebDetails deviceChangeIpWebDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user_agent");
            StoneSerializers.string().serialize(deviceChangeIpWebDetails.userAgent, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceChangeIpWebDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("user_agent".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    DeviceChangeIpWebDetails deviceChangeIpWebDetails = new DeviceChangeIpWebDetails(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(deviceChangeIpWebDetails, deviceChangeIpWebDetails.toStringMultiline());
                    return deviceChangeIpWebDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"user_agent\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceChangeIpWebDetails(String str) {
        if (str != null) {
            this.userAgent = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'userAgent' is null");
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.userAgent});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DeviceChangeIpWebDetails deviceChangeIpWebDetails = (DeviceChangeIpWebDetails) obj;
        String str = this.userAgent;
        String str2 = deviceChangeIpWebDetails.userAgent;
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
