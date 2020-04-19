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

/* renamed from: com.dropbox.core.v2.teamlog.SharedLinkCreateDetails */
public class SharedLinkCreateDetails {
    protected final SharedLinkAccessLevel sharedLinkAccessLevel;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedLinkCreateDetails$Serializer */
    static class Serializer extends StructSerializer<SharedLinkCreateDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedLinkCreateDetails sharedLinkCreateDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedLinkCreateDetails.sharedLinkAccessLevel != null) {
                jsonGenerator.writeFieldName("shared_link_access_level");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(sharedLinkCreateDetails.sharedLinkAccessLevel, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedLinkCreateDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SharedLinkAccessLevel sharedLinkAccessLevel = null;
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
                    if ("shared_link_access_level".equals(currentName)) {
                        sharedLinkAccessLevel = (SharedLinkAccessLevel) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedLinkCreateDetails sharedLinkCreateDetails = new SharedLinkCreateDetails(sharedLinkAccessLevel);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedLinkCreateDetails, sharedLinkCreateDetails.toStringMultiline());
                return sharedLinkCreateDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedLinkCreateDetails(SharedLinkAccessLevel sharedLinkAccessLevel2) {
        this.sharedLinkAccessLevel = sharedLinkAccessLevel2;
    }

    public SharedLinkCreateDetails() {
        this(null);
    }

    public SharedLinkAccessLevel getSharedLinkAccessLevel() {
        return this.sharedLinkAccessLevel;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedLinkAccessLevel});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SharedLinkCreateDetails sharedLinkCreateDetails = (SharedLinkCreateDetails) obj;
        SharedLinkAccessLevel sharedLinkAccessLevel2 = this.sharedLinkAccessLevel;
        SharedLinkAccessLevel sharedLinkAccessLevel3 = sharedLinkCreateDetails.sharedLinkAccessLevel;
        if (sharedLinkAccessLevel2 != sharedLinkAccessLevel3 && (sharedLinkAccessLevel2 == null || !sharedLinkAccessLevel2.equals(sharedLinkAccessLevel3))) {
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
