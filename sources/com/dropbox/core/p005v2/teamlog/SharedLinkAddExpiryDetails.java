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

/* renamed from: com.dropbox.core.v2.teamlog.SharedLinkAddExpiryDetails */
public class SharedLinkAddExpiryDetails {
    protected final Date newValue;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedLinkAddExpiryDetails$Serializer */
    static class Serializer extends StructSerializer<SharedLinkAddExpiryDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedLinkAddExpiryDetails sharedLinkAddExpiryDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            StoneSerializers.timestamp().serialize(sharedLinkAddExpiryDetails.newValue, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedLinkAddExpiryDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (date != null) {
                    SharedLinkAddExpiryDetails sharedLinkAddExpiryDetails = new SharedLinkAddExpiryDetails(date);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedLinkAddExpiryDetails, sharedLinkAddExpiryDetails.toStringMultiline());
                    return sharedLinkAddExpiryDetails;
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

    public SharedLinkAddExpiryDetails(Date date) {
        if (date != null) {
            this.newValue = LangUtil.truncateMillis(date);
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
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
        SharedLinkAddExpiryDetails sharedLinkAddExpiryDetails = (SharedLinkAddExpiryDetails) obj;
        Date date = this.newValue;
        Date date2 = sharedLinkAddExpiryDetails.newValue;
        if (date != date2 && !date.equals(date2)) {
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
