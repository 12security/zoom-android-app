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

/* renamed from: com.dropbox.core.v2.teamlog.ShowcaseFileAddedDetails */
public class ShowcaseFileAddedDetails {
    protected final String eventUuid;

    /* renamed from: com.dropbox.core.v2.teamlog.ShowcaseFileAddedDetails$Serializer */
    static class Serializer extends StructSerializer<ShowcaseFileAddedDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShowcaseFileAddedDetails showcaseFileAddedDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(showcaseFileAddedDetails.eventUuid, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ShowcaseFileAddedDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("event_uuid".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ShowcaseFileAddedDetails showcaseFileAddedDetails = new ShowcaseFileAddedDetails(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(showcaseFileAddedDetails, showcaseFileAddedDetails.toStringMultiline());
                    return showcaseFileAddedDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"event_uuid\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ShowcaseFileAddedDetails(String str) {
        if (str != null) {
            this.eventUuid = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'eventUuid' is null");
    }

    public String getEventUuid() {
        return this.eventUuid;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.eventUuid});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ShowcaseFileAddedDetails showcaseFileAddedDetails = (ShowcaseFileAddedDetails) obj;
        String str = this.eventUuid;
        String str2 = showcaseFileAddedDetails.eventUuid;
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
