package com.dropbox.core.p005v2.filerequests;

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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.filerequests.GetFileRequestArgs */
class GetFileRequestArgs {

    /* renamed from: id */
    protected final String f84id;

    /* renamed from: com.dropbox.core.v2.filerequests.GetFileRequestArgs$Serializer */
    static class Serializer extends StructSerializer<GetFileRequestArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetFileRequestArgs getFileRequestArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("id");
            StoneSerializers.string().serialize(getFileRequestArgs.f84id, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetFileRequestArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    GetFileRequestArgs getFileRequestArgs = new GetFileRequestArgs(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getFileRequestArgs, getFileRequestArgs.toStringMultiline());
                    return getFileRequestArgs;
                }
                throw new JsonParseException(jsonParser, "Required field \"id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetFileRequestArgs(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'id' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'id' is shorter than 1");
        } else if (Pattern.matches("[-_0-9a-zA-Z]+", str)) {
            this.f84id = str;
        } else {
            throw new IllegalArgumentException("String 'id' does not match pattern");
        }
    }

    public String getId() {
        return this.f84id;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f84id});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        GetFileRequestArgs getFileRequestArgs = (GetFileRequestArgs) obj;
        String str = this.f84id;
        String str2 = getFileRequestArgs.f84id;
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
