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

/* renamed from: com.dropbox.core.v2.files.UploadSessionCursor */
public class UploadSessionCursor {
    protected final long offset;
    protected final String sessionId;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionCursor$Serializer */
    static class Serializer extends StructSerializer<UploadSessionCursor> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionCursor uploadSessionCursor, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("session_id");
            StoneSerializers.string().serialize(uploadSessionCursor.sessionId, jsonGenerator);
            jsonGenerator.writeFieldName("offset");
            StoneSerializers.uInt64().serialize(Long.valueOf(uploadSessionCursor.offset), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UploadSessionCursor deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("offset".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"session_id\" missing.");
                } else if (l != null) {
                    UploadSessionCursor uploadSessionCursor = new UploadSessionCursor(str2, l.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(uploadSessionCursor, uploadSessionCursor.toStringMultiline());
                    return uploadSessionCursor;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"offset\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public UploadSessionCursor(String str, long j) {
        if (str != null) {
            this.sessionId = str;
            this.offset = j;
            return;
        }
        throw new IllegalArgumentException("Required value for 'sessionId' is null");
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public long getOffset() {
        return this.offset;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sessionId, Long.valueOf(this.offset)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        UploadSessionCursor uploadSessionCursor = (UploadSessionCursor) obj;
        String str = this.sessionId;
        String str2 = uploadSessionCursor.sessionId;
        if ((str != str2 && !str.equals(str2)) || this.offset != uploadSessionCursor.offset) {
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
