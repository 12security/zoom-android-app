package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxFile;
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

/* renamed from: com.dropbox.core.v2.sharing.RelinquishFileMembershipArg */
class RelinquishFileMembershipArg {
    protected final String file;

    /* renamed from: com.dropbox.core.v2.sharing.RelinquishFileMembershipArg$Serializer */
    static class Serializer extends StructSerializer<RelinquishFileMembershipArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelinquishFileMembershipArg relinquishFileMembershipArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxFile.TYPE);
            StoneSerializers.string().serialize(relinquishFileMembershipArg.file, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RelinquishFileMembershipArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if (BoxFile.TYPE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    RelinquishFileMembershipArg relinquishFileMembershipArg = new RelinquishFileMembershipArg(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(relinquishFileMembershipArg, relinquishFileMembershipArg.toStringMultiline());
                    return relinquishFileMembershipArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"file\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RelinquishFileMembershipArg(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'file' is null");
        } else if (str.length() < 1) {
            throw new IllegalArgumentException("String 'file' is shorter than 1");
        } else if (Pattern.matches("((/|id:).*|nspath:[0-9]+:.*)|ns:[0-9]+(/.*)?", str)) {
            this.file = str;
        } else {
            throw new IllegalArgumentException("String 'file' does not match pattern");
        }
    }

    public String getFile() {
        return this.file;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.file});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RelinquishFileMembershipArg relinquishFileMembershipArg = (RelinquishFileMembershipArg) obj;
        String str = this.file;
        String str2 = relinquishFileMembershipArg.file;
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
