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

/* renamed from: com.dropbox.core.v2.teamlog.SessionLogInfo */
public class SessionLogInfo {
    protected final String sessionId;

    /* renamed from: com.dropbox.core.v2.teamlog.SessionLogInfo$Serializer */
    static class Serializer extends StructSerializer<SessionLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SessionLogInfo sessionLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (sessionLogInfo instanceof WebSessionLogInfo) {
                Serializer.INSTANCE.serialize((WebSessionLogInfo) sessionLogInfo, jsonGenerator, z);
            } else if (sessionLogInfo instanceof DesktopSessionLogInfo) {
                Serializer.INSTANCE.serialize((DesktopSessionLogInfo) sessionLogInfo, jsonGenerator, z);
            } else if (sessionLogInfo instanceof MobileSessionLogInfo) {
                Serializer.INSTANCE.serialize((MobileSessionLogInfo) sessionLogInfo, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                if (sessionLogInfo.sessionId != null) {
                    jsonGenerator.writeFieldName("session_id");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(sessionLogInfo.sessionId, jsonGenerator);
                }
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public SessionLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SessionLogInfo sessionLogInfo;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("".equals(str)) {
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
                sessionLogInfo = new SessionLogInfo(str2);
            } else if ("".equals(str)) {
                sessionLogInfo = INSTANCE.deserialize(jsonParser, true);
            } else if ("web".equals(str)) {
                sessionLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("desktop".equals(str)) {
                sessionLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("mobile".equals(str)) {
                sessionLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
            if (!z) {
                expectEndObject(jsonParser);
            }
            StoneDeserializerLogger.log(sessionLogInfo, sessionLogInfo.toStringMultiline());
            return sessionLogInfo;
        }
    }

    public SessionLogInfo(String str) {
        this.sessionId = str;
    }

    public SessionLogInfo() {
        this(null);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sessionId});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SessionLogInfo sessionLogInfo = (SessionLogInfo) obj;
        String str = this.sessionId;
        String str2 = sessionLogInfo.sessionId;
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
