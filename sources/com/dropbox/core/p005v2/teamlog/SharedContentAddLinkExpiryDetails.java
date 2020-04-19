package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentAddLinkExpiryDetails */
public class SharedContentAddLinkExpiryDetails {
    protected final Date newValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentAddLinkExpiryDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentAddLinkExpiryDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentAddLinkExpiryDetails sharedContentAddLinkExpiryDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedContentAddLinkExpiryDetails.newValue != null) {
                jsonGenerator.writeFieldName("new_value");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedContentAddLinkExpiryDetails.newValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentAddLinkExpiryDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Date date = null;
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
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedContentAddLinkExpiryDetails sharedContentAddLinkExpiryDetails = new SharedContentAddLinkExpiryDetails(date);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedContentAddLinkExpiryDetails, sharedContentAddLinkExpiryDetails.toStringMultiline());
                return sharedContentAddLinkExpiryDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedContentAddLinkExpiryDetails(Date date) {
        this.newValue = LangUtil.truncateMillis(date);
    }

    public SharedContentAddLinkExpiryDetails() {
        this(null);
    }

    public Date getNewValue() {
        return this.newValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SharedContentAddLinkExpiryDetails sharedContentAddLinkExpiryDetails = (SharedContentAddLinkExpiryDetails) obj;
        Date date = this.newValue;
        Date date2 = sharedContentAddLinkExpiryDetails.newValue;
        if (date != date2 && (date == null || !date.equals(date2))) {
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
