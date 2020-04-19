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

/* renamed from: com.dropbox.core.v2.teamlog.SharedLinkRemoveExpiryDetails */
public class SharedLinkRemoveExpiryDetails {
    protected final Date previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedLinkRemoveExpiryDetails$Serializer */
    static class Serializer extends StructSerializer<SharedLinkRemoveExpiryDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedLinkRemoveExpiryDetails sharedLinkRemoveExpiryDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedLinkRemoveExpiryDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedLinkRemoveExpiryDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedLinkRemoveExpiryDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("previous_value".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedLinkRemoveExpiryDetails sharedLinkRemoveExpiryDetails = new SharedLinkRemoveExpiryDetails(date);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedLinkRemoveExpiryDetails, sharedLinkRemoveExpiryDetails.toStringMultiline());
                return sharedLinkRemoveExpiryDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedLinkRemoveExpiryDetails(Date date) {
        this.previousValue = LangUtil.truncateMillis(date);
    }

    public SharedLinkRemoveExpiryDetails() {
        this(null);
    }

    public Date getPreviousValue() {
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
        SharedLinkRemoveExpiryDetails sharedLinkRemoveExpiryDetails = (SharedLinkRemoveExpiryDetails) obj;
        Date date = this.previousValue;
        Date date2 = sharedLinkRemoveExpiryDetails.previousValue;
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
