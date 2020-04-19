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

/* renamed from: com.dropbox.core.v2.teamlog.TeamLinkedAppLogInfo */
public class TeamLinkedAppLogInfo extends AppLogInfo {

    /* renamed from: com.dropbox.core.v2.teamlog.TeamLinkedAppLogInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamlog.AppLogInfo.Builder {
        protected Builder() {
        }

        public Builder withAppId(String str) {
            super.withAppId(str);
            return this;
        }

        public Builder withDisplayName(String str) {
            super.withDisplayName(str);
            return this;
        }

        public TeamLinkedAppLogInfo build() {
            return new TeamLinkedAppLogInfo(this.appId, this.displayName);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.TeamLinkedAppLogInfo$Serializer */
    static class Serializer extends StructSerializer<TeamLinkedAppLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamLinkedAppLogInfo teamLinkedAppLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("team_linked_app", jsonGenerator);
            if (teamLinkedAppLogInfo.appId != null) {
                jsonGenerator.writeFieldName("app_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamLinkedAppLogInfo.appId, jsonGenerator);
            }
            if (teamLinkedAppLogInfo.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamLinkedAppLogInfo.displayName, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamLinkedAppLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("team_linked_app".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("app_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                TeamLinkedAppLogInfo teamLinkedAppLogInfo = new TeamLinkedAppLogInfo(str2, str3);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(teamLinkedAppLogInfo, teamLinkedAppLogInfo.toStringMultiline());
                return teamLinkedAppLogInfo;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamLinkedAppLogInfo(String str, String str2) {
        super(str, str2);
    }

    public TeamLinkedAppLogInfo() {
        this(null, null);
    }

    public String getAppId() {
        return this.appId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Builder newBuilder() {
        return new Builder();
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
        TeamLinkedAppLogInfo teamLinkedAppLogInfo = (TeamLinkedAppLogInfo) obj;
        if ((this.appId != teamLinkedAppLogInfo.appId && (this.appId == null || !this.appId.equals(teamLinkedAppLogInfo.appId))) || (this.displayName != teamLinkedAppLogInfo.displayName && (this.displayName == null || !this.displayName.equals(teamLinkedAppLogInfo.displayName)))) {
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
