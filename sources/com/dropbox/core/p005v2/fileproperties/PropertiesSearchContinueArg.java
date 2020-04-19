package com.dropbox.core.p005v2.fileproperties;

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

/* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchContinueArg */
class PropertiesSearchContinueArg {
    protected final String cursor;

    /* renamed from: com.dropbox.core.v2.fileproperties.PropertiesSearchContinueArg$Serializer */
    static class Serializer extends StructSerializer<PropertiesSearchContinueArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PropertiesSearchContinueArg propertiesSearchContinueArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("cursor");
            StoneSerializers.string().serialize(propertiesSearchContinueArg.cursor, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PropertiesSearchContinueArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("cursor".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    PropertiesSearchContinueArg propertiesSearchContinueArg = new PropertiesSearchContinueArg(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(propertiesSearchContinueArg, propertiesSearchContinueArg.toStringMultiline());
                    return propertiesSearchContinueArg;
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

    public PropertiesSearchContinueArg(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'cursor' is null");
        } else if (str.length() >= 1) {
            this.cursor = str;
        } else {
            throw new IllegalArgumentException("String 'cursor' is shorter than 1");
        }
    }

    public String getCursor() {
        return this.cursor;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.cursor});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        PropertiesSearchContinueArg propertiesSearchContinueArg = (PropertiesSearchContinueArg) obj;
        String str = this.cursor;
        String str2 = propertiesSearchContinueArg.cursor;
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
