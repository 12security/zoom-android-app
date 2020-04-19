package com.dropbox.core.p005v2.files;

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

/* renamed from: com.dropbox.core.v2.files.UploadSessionStartResult */
public class UploadSessionStartResult {
    protected final String sessionId;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionStartResult$Serializer */
    static class Serializer extends StructSerializer<UploadSessionStartResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionStartResult uploadSessionStartResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("session_id");
            StoneSerializers.string().serialize(uploadSessionStartResult.sessionId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UploadSessionStartResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    UploadSessionStartResult uploadSessionStartResult = new UploadSessionStartResult(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(uploadSessionStartResult, uploadSessionStartResult.toStringMultiline());
                    return uploadSessionStartResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"session_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UploadSessionStartResult(String str) {
        if (str != null) {
            this.sessionId = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'sessionId' is null");
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sessionId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        UploadSessionStartResult uploadSessionStartResult = (UploadSessionStartResult) obj;
        String str = this.sessionId;
        String str2 = uploadSessionStartResult.sessionId;
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
