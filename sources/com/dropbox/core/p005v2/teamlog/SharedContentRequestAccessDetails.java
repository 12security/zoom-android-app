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

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentRequestAccessDetails */
public class SharedContentRequestAccessDetails {
    protected final String sharedContentLink;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentRequestAccessDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentRequestAccessDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentRequestAccessDetails sharedContentRequestAccessDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedContentRequestAccessDetails.sharedContentLink != null) {
                jsonGenerator.writeFieldName("shared_content_link");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedContentRequestAccessDetails.sharedContentLink, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentRequestAccessDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("shared_content_link".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedContentRequestAccessDetails sharedContentRequestAccessDetails = new SharedContentRequestAccessDetails(str2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedContentRequestAccessDetails, sharedContentRequestAccessDetails.toStringMultiline());
                return sharedContentRequestAccessDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedContentRequestAccessDetails(String str) {
        this.sharedContentLink = str;
    }

    public SharedContentRequestAccessDetails() {
        this(null);
    }

    public String getSharedContentLink() {
        return this.sharedContentLink;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedContentLink});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SharedContentRequestAccessDetails sharedContentRequestAccessDetails = (SharedContentRequestAccessDetails) obj;
        String str = this.sharedContentLink;
        String str2 = sharedContentRequestAccessDetails.sharedContentLink;
        if (str != str2 && (str == null || !str.equals(str2))) {
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
