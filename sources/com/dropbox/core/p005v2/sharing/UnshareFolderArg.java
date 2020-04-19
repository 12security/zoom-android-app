package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.UnshareFolderArg */
class UnshareFolderArg {
    protected final boolean leaveACopy;
    protected final String sharedFolderId;

    /* renamed from: com.dropbox.core.v2.sharing.UnshareFolderArg$Serializer */
    static class Serializer extends StructSerializer<UnshareFolderArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UnshareFolderArg unshareFolderArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(unshareFolderArg.sharedFolderId, jsonGenerator);
            jsonGenerator.writeFieldName("leave_a_copy");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(unshareFolderArg.leaveACopy), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UnshareFolderArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("leave_a_copy".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    UnshareFolderArg unshareFolderArg = new UnshareFolderArg(str2, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(unshareFolderArg, unshareFolderArg.toStringMultiline());
                    return unshareFolderArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"shared_folder_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UnshareFolderArg(String str, boolean z) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.sharedFolderId = str;
            this.leaveACopy = z;
        } else {
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        }
    }

    public UnshareFolderArg(String str) {
        this(str, false);
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public boolean getLeaveACopy() {
        return this.leaveACopy;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedFolderId, Boolean.valueOf(this.leaveACopy)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        UnshareFolderArg unshareFolderArg = (UnshareFolderArg) obj;
        String str = this.sharedFolderId;
        String str2 = unshareFolderArg.sharedFolderId;
        if ((str != str2 && !str.equals(str2)) || this.leaveACopy != unshareFolderArg.leaveACopy) {
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
