package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxError;
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

/* renamed from: com.dropbox.core.v2.teamlog.ApiSessionLogInfo */
public class ApiSessionLogInfo {
    protected final String requestId;

    /* renamed from: com.dropbox.core.v2.teamlog.ApiSessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<ApiSessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ApiSessionLogInfo apiSessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxError.FIELD_REQUEST_ID);
            StoneSerializers.string().serialize(apiSessionLogInfo.requestId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ApiSessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if (BoxError.FIELD_REQUEST_ID.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ApiSessionLogInfo apiSessionLogInfo = new ApiSessionLogInfo(str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(apiSessionLogInfo, apiSessionLogInfo.toStringMultiline());
                    return apiSessionLogInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"request_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ApiSessionLogInfo(String str) {
        if (str != null) {
            this.requestId = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'requestId' is null");
    }

    public String getRequestId() {
        return this.requestId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.requestId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ApiSessionLogInfo apiSessionLogInfo = (ApiSessionLogInfo) obj;
        String str = this.requestId;
        String str2 = apiSessionLogInfo.requestId;
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
