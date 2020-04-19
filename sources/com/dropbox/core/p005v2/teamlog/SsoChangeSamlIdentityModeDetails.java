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

/* renamed from: com.dropbox.core.v2.teamlog.SsoChangeSamlIdentityModeDetails */
public class SsoChangeSamlIdentityModeDetails {
    protected final long newValue;
    protected final long previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SsoChangeSamlIdentityModeDetails$Serializer */
    static class Serializer extends StructSerializer<SsoChangeSamlIdentityModeDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SsoChangeSamlIdentityModeDetails ssoChangeSamlIdentityModeDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("previous_value");
            StoneSerializers.int64().serialize(Long.valueOf(ssoChangeSamlIdentityModeDetails.previousValue), jsonGenerator);
            jsonGenerator.writeFieldName("new_value");
            StoneSerializers.int64().serialize(Long.valueOf(ssoChangeSamlIdentityModeDetails.newValue), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SsoChangeSamlIdentityModeDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("previous_value".equals(currentName)) {
                        l = (Long) StoneSerializers.int64().deserialize(jsonParser);
                    } else if ("new_value".equals(currentName)) {
                        l2 = (Long) StoneSerializers.int64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"previous_value\" missing.");
                } else if (l2 != null) {
                    SsoChangeSamlIdentityModeDetails ssoChangeSamlIdentityModeDetails = new SsoChangeSamlIdentityModeDetails(l.longValue(), l2.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(ssoChangeSamlIdentityModeDetails, ssoChangeSamlIdentityModeDetails.toStringMultiline());
                    return ssoChangeSamlIdentityModeDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
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

    public SsoChangeSamlIdentityModeDetails(long j, long j2) {
        this.previousValue = j;
        this.newValue = j2;
    }

    public long getPreviousValue() {
        return this.previousValue;
    }

    public long getNewValue() {
        return this.newValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.previousValue), Long.valueOf(this.newValue)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SsoChangeSamlIdentityModeDetails ssoChangeSamlIdentityModeDetails = (SsoChangeSamlIdentityModeDetails) obj;
        if (!(this.previousValue == ssoChangeSamlIdentityModeDetails.previousValue && this.newValue == ssoChangeSamlIdentityModeDetails.newValue)) {
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
