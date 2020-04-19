package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.FileGetCopyReferenceDetails */
public class FileGetCopyReferenceDetails {

    /* renamed from: com.dropbox.core.v2.teamlog.FileGetCopyReferenceDetails$Serializer */
    static class Serializer extends StructSerializer<FileGetCopyReferenceDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileGetCopyReferenceDetails fileGetCopyReferenceDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileGetCopyReferenceDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                FileGetCopyReferenceDetails fileGetCopyReferenceDetails = new FileGetCopyReferenceDetails();
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(fileGetCopyReferenceDetails, fileGetCopyReferenceDetails.toStringMultiline());
                return fileGetCopyReferenceDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public int hashCode() {
        return getClass().toString().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        FileGetCopyReferenceDetails fileGetCopyReferenceDetails = (FileGetCopyReferenceDetails) obj;
        return true;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
