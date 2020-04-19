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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.CreateFolderArg */
class CreateFolderArg {
    protected final boolean autorename;
    protected final String path;

    /* renamed from: com.dropbox.core.v2.files.CreateFolderArg$Serializer */
    static class Serializer extends StructSerializer<CreateFolderArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateFolderArg createFolderArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(createFolderArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("autorename");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(createFolderArg.autorename), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CreateFolderArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
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
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("autorename".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    CreateFolderArg createFolderArg = new CreateFolderArg(str2, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(createFolderArg, createFolderArg.toStringMultiline());
                    return createFolderArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public CreateFolderArg(String str, boolean z) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            this.autorename = z;
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public CreateFolderArg(String str) {
        this(str, false);
    }

    public String getPath() {
        return this.path;
    }

    public boolean getAutorename() {
        return this.autorename;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, Boolean.valueOf(this.autorename)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        CreateFolderArg createFolderArg = (CreateFolderArg) obj;
        String str = this.path;
        String str2 = createFolderArg.path;
        if ((str != str2 && !str.equals(str2)) || this.autorename != createFolderArg.autorename) {
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
