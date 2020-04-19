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

/* renamed from: com.dropbox.core.v2.teamlog.SharedFolderCreateDetails */
public class SharedFolderCreateDetails {
    protected final String targetNsId;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedFolderCreateDetails$Serializer */
    static class Serializer extends StructSerializer<SharedFolderCreateDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedFolderCreateDetails sharedFolderCreateDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedFolderCreateDetails.targetNsId != null) {
                jsonGenerator.writeFieldName("target_ns_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFolderCreateDetails.targetNsId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedFolderCreateDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("target_ns_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedFolderCreateDetails sharedFolderCreateDetails = new SharedFolderCreateDetails(str2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedFolderCreateDetails, sharedFolderCreateDetails.toStringMultiline());
                return sharedFolderCreateDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedFolderCreateDetails(String str) {
        this.targetNsId = str;
    }

    public SharedFolderCreateDetails() {
        this(null);
    }

    public String getTargetNsId() {
        return this.targetNsId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.targetNsId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SharedFolderCreateDetails sharedFolderCreateDetails = (SharedFolderCreateDetails) obj;
        String str = this.targetNsId;
        String str2 = sharedFolderCreateDetails.targetNsId;
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
