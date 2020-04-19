package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.FileAddCommentDetails */
public class FileAddCommentDetails {
    protected final String commentText;

    /* renamed from: com.dropbox.core.v2.teamlog.FileAddCommentDetails$Serializer */
    static class Serializer extends StructSerializer<FileAddCommentDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileAddCommentDetails fileAddCommentDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (fileAddCommentDetails.commentText != null) {
                jsonGenerator.writeFieldName("comment_text");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(fileAddCommentDetails.commentText, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FileAddCommentDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("comment_text".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                FileAddCommentDetails fileAddCommentDetails = new FileAddCommentDetails(str2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(fileAddCommentDetails, fileAddCommentDetails.toStringMultiline());
                return fileAddCommentDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FileAddCommentDetails(String str) {
        this.commentText = str;
    }

    public FileAddCommentDetails() {
        this(null);
    }

    public String getCommentText() {
        return this.commentText;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.commentText});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        FileAddCommentDetails fileAddCommentDetails = (FileAddCommentDetails) obj;
        String str = this.commentText;
        String str2 = fileAddCommentDetails.commentText;
        if (str != str2 && (str == null || !str.equals(str2))) {
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
