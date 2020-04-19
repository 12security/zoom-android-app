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

/* renamed from: com.dropbox.core.v2.teamlog.MemberSpaceLimitsAddCustomQuotaDetails */
public class MemberSpaceLimitsAddCustomQuotaDetails {
    protected final long newValue;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberSpaceLimitsAddCustomQuotaDetails$Serializer */
    static class Serializer extends StructSerializer<MemberSpaceLimitsAddCustomQuotaDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberSpaceLimitsAddCustomQuotaDetails memberSpaceLimitsAddCustomQuotaDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            StoneSerializers.uInt64().serialize(Long.valueOf(memberSpaceLimitsAddCustomQuotaDetails.newValue), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberSpaceLimitsAddCustomQuotaDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
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
                    if ("new_value".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l != null) {
                    MemberSpaceLimitsAddCustomQuotaDetails memberSpaceLimitsAddCustomQuotaDetails = new MemberSpaceLimitsAddCustomQuotaDetails(l.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberSpaceLimitsAddCustomQuotaDetails, memberSpaceLimitsAddCustomQuotaDetails.toStringMultiline());
                    return memberSpaceLimitsAddCustomQuotaDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MemberSpaceLimitsAddCustomQuotaDetails(long j) {
        this.newValue = j;
    }

    public long getNewValue() {
        return this.newValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.newValue)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        if (this.newValue != ((MemberSpaceLimitsAddCustomQuotaDetails) obj).newValue) {
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
