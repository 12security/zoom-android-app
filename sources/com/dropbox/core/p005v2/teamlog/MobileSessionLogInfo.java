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

/* renamed from: com.dropbox.core.v2.teamlog.MobileSessionLogInfo */
public class MobileSessionLogInfo extends SessionLogInfo {

    /* renamed from: com.dropbox.core.v2.teamlog.MobileSessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<MobileSessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MobileSessionLogInfo mobileSessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("mobile", jsonGenerator);
            if (mobileSessionLogInfo.sessionId != null) {
                jsonGenerator.writeFieldName("session_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(mobileSessionLogInfo.sessionId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MobileSessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("mobile".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                MobileSessionLogInfo mobileSessionLogInfo = new MobileSessionLogInfo(str2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(mobileSessionLogInfo, mobileSessionLogInfo.toStringMultiline());
                return mobileSessionLogInfo;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MobileSessionLogInfo(String str) {
        super(str);
    }

    public MobileSessionLogInfo() {
        this(null);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public int hashCode() {
        return getClass().toString().hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MobileSessionLogInfo mobileSessionLogInfo = (MobileSessionLogInfo) obj;
        if (this.sessionId != mobileSessionLogInfo.sessionId && (this.sessionId == null || !this.sessionId.equals(mobileSessionLogInfo.sessionId))) {
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
