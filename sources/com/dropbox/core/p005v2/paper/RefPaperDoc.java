package com.dropbox.core.p005v2.paper;

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

/* renamed from: com.dropbox.core.v2.paper.RefPaperDoc */
class RefPaperDoc {
    protected final String docId;

    /* renamed from: com.dropbox.core.v2.paper.RefPaperDoc$Serializer */
    static class Serializer extends StructSerializer<RefPaperDoc> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RefPaperDoc refPaperDoc, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("doc_id");
            StoneSerializers.string().serialize(refPaperDoc.docId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RefPaperDoc deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("doc_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    RefPaperDoc refPaperDoc = new RefPaperDoc(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(refPaperDoc, refPaperDoc.toStringMultiline());
                    return refPaperDoc;
                }
                throw new JsonParseException(jsonParser, "Required field \"doc_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RefPaperDoc(String str) {
        if (str != null) {
            this.docId = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'docId' is null");
    }

    public String getDocId() {
        return this.docId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.docId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RefPaperDoc refPaperDoc = (RefPaperDoc) obj;
        String str = this.docId;
        String str2 = refPaperDoc.docId;
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
