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

/* renamed from: com.dropbox.core.v2.teamlog.UserLinkedAppLogInfo */
public class UserLinkedAppLogInfo extends AppLogInfo {

    /* renamed from: com.dropbox.core.v2.teamlog.UserLinkedAppLogInfo$Builder */
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

        public UserLinkedAppLogInfo build() {
            return new UserLinkedAppLogInfo(this.appId, this.displayName);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.UserLinkedAppLogInfo$Serializer */
    static class Serializer extends StructSerializer<UserLinkedAppLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserLinkedAppLogInfo userLinkedAppLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("user_linked_app", jsonGenerator);
            if (userLinkedAppLogInfo.appId != null) {
                jsonGenerator.writeFieldName("app_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(userLinkedAppLogInfo.appId, jsonGenerator);
            }
            if (userLinkedAppLogInfo.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(userLinkedAppLogInfo.displayName, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserLinkedAppLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("user_linked_app".equals(str)) {
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
                UserLinkedAppLogInfo userLinkedAppLogInfo = new UserLinkedAppLogInfo(str2, str3);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(userLinkedAppLogInfo, userLinkedAppLogInfo.toStringMultiline());
                return userLinkedAppLogInfo;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UserLinkedAppLogInfo(String str, String str2) {
        super(str, str2);
    }

    public UserLinkedAppLogInfo() {
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
        UserLinkedAppLogInfo userLinkedAppLogInfo = (UserLinkedAppLogInfo) obj;
        if ((this.appId != userLinkedAppLogInfo.appId && (this.appId == null || !this.appId.equals(userLinkedAppLogInfo.appId))) || (this.displayName != userLinkedAppLogInfo.displayName && (this.displayName == null || !this.displayName.equals(userLinkedAppLogInfo.displayName)))) {
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
