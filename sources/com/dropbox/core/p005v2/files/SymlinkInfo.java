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

/* renamed from: com.dropbox.core.v2.files.SymlinkInfo */
public class SymlinkInfo {
    protected final String target;

    /* renamed from: com.dropbox.core.v2.files.SymlinkInfo$Serializer */
    static class Serializer extends StructSerializer<SymlinkInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SymlinkInfo symlinkInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("target");
            StoneSerializers.string().serialize(symlinkInfo.target, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SymlinkInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("target".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    SymlinkInfo symlinkInfo = new SymlinkInfo(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(symlinkInfo, symlinkInfo.toStringMultiline());
                    return symlinkInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"target\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SymlinkInfo(String str) {
        if (str != null) {
            this.target = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'target' is null");
    }

    public String getTarget() {
        return this.target;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.target});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SymlinkInfo symlinkInfo = (SymlinkInfo) obj;
        String str = this.target;
        String str2 = symlinkInfo.target;
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
