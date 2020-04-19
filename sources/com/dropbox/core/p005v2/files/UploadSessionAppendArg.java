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

/* renamed from: com.dropbox.core.v2.files.UploadSessionAppendArg */
class UploadSessionAppendArg {
    protected final boolean close;
    protected final UploadSessionCursor cursor;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionAppendArg$Serializer */
    static class Serializer extends StructSerializer<UploadSessionAppendArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionAppendArg uploadSessionAppendArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("cursor");
            Serializer.INSTANCE.serialize(uploadSessionAppendArg.cursor, jsonGenerator);
            jsonGenerator.writeFieldName("close");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(uploadSessionAppendArg.close), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UploadSessionAppendArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UploadSessionCursor uploadSessionCursor = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("cursor".equals(currentName)) {
                        uploadSessionCursor = (UploadSessionCursor) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("close".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (uploadSessionCursor != null) {
                    UploadSessionAppendArg uploadSessionAppendArg = new UploadSessionAppendArg(uploadSessionCursor, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(uploadSessionAppendArg, uploadSessionAppendArg.toStringMultiline());
                    return uploadSessionAppendArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"cursor\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UploadSessionAppendArg(UploadSessionCursor uploadSessionCursor, boolean z) {
        if (uploadSessionCursor != null) {
            this.cursor = uploadSessionCursor;
            this.close = z;
            return;
        }
        throw new IllegalArgumentException("Required value for 'cursor' is null");
    }

    public UploadSessionAppendArg(UploadSessionCursor uploadSessionCursor) {
        this(uploadSessionCursor, false);
    }

    public UploadSessionCursor getCursor() {
        return this.cursor;
    }

    public boolean getClose() {
        return this.close;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.cursor, Boolean.valueOf(this.close)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        UploadSessionAppendArg uploadSessionAppendArg = (UploadSessionAppendArg) obj;
        UploadSessionCursor uploadSessionCursor = this.cursor;
        UploadSessionCursor uploadSessionCursor2 = uploadSessionAppendArg.cursor;
        if ((uploadSessionCursor != uploadSessionCursor2 && !uploadSessionCursor.equals(uploadSessionCursor2)) || this.close != uploadSessionAppendArg.close) {
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
