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

/* renamed from: com.dropbox.core.v2.teamlog.ShowcaseUntrashedDeprecatedDetails */
public class ShowcaseUntrashedDeprecatedDetails {
    protected final String eventUuid;

    /* renamed from: com.dropbox.core.v2.teamlog.ShowcaseUntrashedDeprecatedDetails$Serializer */
    static class Serializer extends StructSerializer<ShowcaseUntrashedDeprecatedDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShowcaseUntrashedDeprecatedDetails showcaseUntrashedDeprecatedDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(showcaseUntrashedDeprecatedDetails.eventUuid, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ShowcaseUntrashedDeprecatedDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    ShowcaseUntrashedDeprecatedDetails showcaseUntrashedDeprecatedDetails = new ShowcaseUntrashedDeprecatedDetails(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(showcaseUntrashedDeprecatedDetails, showcaseUntrashedDeprecatedDetails.toStringMultiline());
                    return showcaseUntrashedDeprecatedDetails;
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

    public ShowcaseUntrashedDeprecatedDetails(String str) {
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
        ShowcaseUntrashedDeprecatedDetails showcaseUntrashedDeprecatedDetails = (ShowcaseUntrashedDeprecatedDetails) obj;
        String str = this.eventUuid;
        String str2 = showcaseUntrashedDeprecatedDetails.eventUuid;
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
