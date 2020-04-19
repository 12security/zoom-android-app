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

/* renamed from: com.dropbox.core.v2.teamlog.UserOrTeamLinkedAppLogInfo */
public class UserOrTeamLinkedAppLogInfo extends AppLogInfo {

    /* renamed from: com.dropbox.core.v2.teamlog.UserOrTeamLinkedAppLogInfo$Builder */
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

        public UserOrTeamLinkedAppLogInfo build() {
            return new UserOrTeamLinkedAppLogInfo(this.appId, this.displayName);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.UserOrTeamLinkedAppLogInfo$Serializer */
    static class Serializer extends StructSerializer<UserOrTeamLinkedAppLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserOrTeamLinkedAppLogInfo userOrTeamLinkedAppLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("user_or_team_linked_app", jsonGenerator);
            if (userOrTeamLinkedAppLogInfo.appId != null) {
                jsonGenerator.writeFieldName("app_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(userOrTeamLinkedAppLogInfo.appId, jsonGenerator);
            }
            if (userOrTeamLinkedAppLogInfo.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(userOrTeamLinkedAppLogInfo.displayName, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserOrTeamLinkedAppLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("user_or_team_linked_app".equals(str)) {
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
                UserOrTeamLinkedAppLogInfo userOrTeamLinkedAppLogInfo = new UserOrTeamLinkedAppLogInfo(str2, str3);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(userOrTeamLinkedAppLogInfo, userOrTeamLinkedAppLogInfo.toStringMultiline());
                return userOrTeamLinkedAppLogInfo;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UserOrTeamLinkedAppLogInfo(String str, String str2) {
        super(str, str2);
    }

    public UserOrTeamLinkedAppLogInfo() {
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
        UserOrTeamLinkedAppLogInfo userOrTeamLinkedAppLogInfo = (UserOrTeamLinkedAppLogInfo) obj;
        if ((this.appId != userOrTeamLinkedAppLogInfo.appId && (this.appId == null || !this.appId.equals(userOrTeamLinkedAppLogInfo.appId))) || (this.displayName != userOrTeamLinkedAppLogInfo.displayName && (this.displayName == null || !this.displayName.equals(userOrTeamLinkedAppLogInfo.displayName)))) {
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
