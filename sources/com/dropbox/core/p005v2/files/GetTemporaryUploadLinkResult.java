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

/* renamed from: com.dropbox.core.v2.files.GetTemporaryUploadLinkResult */
public class GetTemporaryUploadLinkResult {
    protected final String link;

    /* renamed from: com.dropbox.core.v2.files.GetTemporaryUploadLinkResult$Serializer */
    static class Serializer extends StructSerializer<GetTemporaryUploadLinkResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetTemporaryUploadLinkResult getTemporaryUploadLinkResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("link");
            StoneSerializers.string().serialize(getTemporaryUploadLinkResult.link, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetTemporaryUploadLinkResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("link".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    GetTemporaryUploadLinkResult getTemporaryUploadLinkResult = new GetTemporaryUploadLinkResult(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getTemporaryUploadLinkResult, getTemporaryUploadLinkResult.toStringMultiline());
                    return getTemporaryUploadLinkResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"link\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetTemporaryUploadLinkResult(String str) {
        if (str != null) {
            this.link = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'link' is null");
    }

    public String getLink() {
        return this.link;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.link});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        GetTemporaryUploadLinkResult getTemporaryUploadLinkResult = (GetTemporaryUploadLinkResult) obj;
        String str = this.link;
        String str2 = getTemporaryUploadLinkResult.link;
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
