package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.RevokeDesktopClientArg */
public class RevokeDesktopClientArg extends DeviceSessionArg {
    protected final boolean deleteOnUnlink;

    /* renamed from: com.dropbox.core.v2.team.RevokeDesktopClientArg$Serializer */
    static class Serializer extends StructSerializer<RevokeDesktopClientArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RevokeDesktopClientArg revokeDesktopClientArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("session_id");
            StoneSerializers.string().serialize(revokeDesktopClientArg.sessionId, jsonGenerator);
            jsonGenerator.writeFieldName("team_member_id");
            StoneSerializers.string().serialize(revokeDesktopClientArg.teamMemberId, jsonGenerator);
            jsonGenerator.writeFieldName("delete_on_unlink");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(revokeDesktopClientArg.deleteOnUnlink), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RevokeDesktopClientArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("session_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("team_member_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("delete_on_unlink".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"session_id\" missing.");
                } else if (str3 != null) {
                    RevokeDesktopClientArg revokeDesktopClientArg = new RevokeDesktopClientArg(str2, str3, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(revokeDesktopClientArg, revokeDesktopClientArg.toStringMultiline());
                    return revokeDesktopClientArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"team_member_id\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public RevokeDesktopClientArg(String str, String str2, boolean z) {
        super(str, str2);
        this.deleteOnUnlink = z;
    }

    public RevokeDesktopClientArg(String str, String str2) {
        this(str, str2, false);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public boolean getDeleteOnUnlink() {
        return this.deleteOnUnlink;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{Boolean.valueOf(this.deleteOnUnlink)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RevokeDesktopClientArg revokeDesktopClientArg = (RevokeDesktopClientArg) obj;
        if ((this.sessionId != revokeDesktopClientArg.sessionId && !this.sessionId.equals(revokeDesktopClientArg.sessionId)) || ((this.teamMemberId != revokeDesktopClientArg.teamMemberId && !this.teamMemberId.equals(revokeDesktopClientArg.teamMemberId)) || this.deleteOnUnlink != revokeDesktopClientArg.deleteOnUnlink)) {
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
