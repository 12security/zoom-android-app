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

/* renamed from: com.dropbox.core.v2.teamlog.DesktopSessionLogInfo */
public class DesktopSessionLogInfo extends SessionLogInfo {

    /* renamed from: com.dropbox.core.v2.teamlog.DesktopSessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<DesktopSessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DesktopSessionLogInfo desktopSessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("desktop", jsonGenerator);
            if (desktopSessionLogInfo.sessionId != null) {
                jsonGenerator.writeFieldName("session_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(desktopSessionLogInfo.sessionId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DesktopSessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("desktop".equals(str)) {
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
                DesktopSessionLogInfo desktopSessionLogInfo = new DesktopSessionLogInfo(str2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(desktopSessionLogInfo, desktopSessionLogInfo.toStringMultiline());
                return desktopSessionLogInfo;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DesktopSessionLogInfo(String str) {
        super(str);
    }

    public DesktopSessionLogInfo() {
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
        DesktopSessionLogInfo desktopSessionLogInfo = (DesktopSessionLogInfo) obj;
        if (this.sessionId != desktopSessionLogInfo.sessionId && (this.sessionId == null || !this.sessionId.equals(desktopSessionLogInfo.sessionId))) {
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
