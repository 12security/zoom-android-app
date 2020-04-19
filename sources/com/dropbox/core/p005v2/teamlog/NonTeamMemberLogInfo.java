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

/* renamed from: com.dropbox.core.v2.teamlog.NonTeamMemberLogInfo */
public class NonTeamMemberLogInfo extends UserLogInfo {

    /* renamed from: com.dropbox.core.v2.teamlog.NonTeamMemberLogInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamlog.UserLogInfo.Builder {
        protected Builder() {
        }

        public Builder withAccountId(String str) {
            super.withAccountId(str);
            return this;
        }

        public Builder withDisplayName(String str) {
            super.withDisplayName(str);
            return this;
        }

        public Builder withEmail(String str) {
            super.withEmail(str);
            return this;
        }

        public NonTeamMemberLogInfo build() {
            return new NonTeamMemberLogInfo(this.accountId, this.displayName, this.email);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.NonTeamMemberLogInfo$Serializer */
    static class Serializer extends StructSerializer<NonTeamMemberLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(NonTeamMemberLogInfo nonTeamMemberLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("non_team_member", jsonGenerator);
            if (nonTeamMemberLogInfo.accountId != null) {
                jsonGenerator.writeFieldName("account_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(nonTeamMemberLogInfo.accountId, jsonGenerator);
            }
            if (nonTeamMemberLogInfo.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(nonTeamMemberLogInfo.displayName, jsonGenerator);
            }
            if (nonTeamMemberLogInfo.email != null) {
                jsonGenerator.writeFieldName("email");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(nonTeamMemberLogInfo.email, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public NonTeamMemberLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("non_team_member".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("account_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("email".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                NonTeamMemberLogInfo nonTeamMemberLogInfo = new NonTeamMemberLogInfo(str2, str3, str4);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(nonTeamMemberLogInfo, nonTeamMemberLogInfo.toStringMultiline());
                return nonTeamMemberLogInfo;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public NonTeamMemberLogInfo(String str, String str2, String str3) {
        super(str, str2, str3);
    }

    public NonTeamMemberLogInfo() {
        this(null, null, null);
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getEmail() {
        return this.email;
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
        NonTeamMemberLogInfo nonTeamMemberLogInfo = (NonTeamMemberLogInfo) obj;
        if ((this.accountId != nonTeamMemberLogInfo.accountId && (this.accountId == null || !this.accountId.equals(nonTeamMemberLogInfo.accountId))) || ((this.displayName != nonTeamMemberLogInfo.displayName && (this.displayName == null || !this.displayName.equals(nonTeamMemberLogInfo.displayName))) || (this.email != nonTeamMemberLogInfo.email && (this.email == null || !this.email.equals(nonTeamMemberLogInfo.email))))) {
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
