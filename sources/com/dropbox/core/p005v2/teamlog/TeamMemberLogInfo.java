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

/* renamed from: com.dropbox.core.v2.teamlog.TeamMemberLogInfo */
public class TeamMemberLogInfo extends UserLogInfo {
    protected final String memberExternalId;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.teamlog.TeamMemberLogInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamlog.UserLogInfo.Builder {
        protected String memberExternalId = null;
        protected String teamMemberId = null;

        protected Builder() {
        }

        public Builder withTeamMemberId(String str) {
            this.teamMemberId = str;
            return this;
        }

        public Builder withMemberExternalId(String str) {
            if (str == null || str.length() <= 64) {
                this.memberExternalId = str;
                return this;
            }
            throw new IllegalArgumentException("String 'memberExternalId' is longer than 64");
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

        public TeamMemberLogInfo build() {
            TeamMemberLogInfo teamMemberLogInfo = new TeamMemberLogInfo(this.accountId, this.displayName, this.email, this.teamMemberId, this.memberExternalId);
            return teamMemberLogInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.TeamMemberLogInfo$Serializer */
    static class Serializer extends StructSerializer<TeamMemberLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamMemberLogInfo teamMemberLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("team_member", jsonGenerator);
            if (teamMemberLogInfo.accountId != null) {
                jsonGenerator.writeFieldName("account_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberLogInfo.accountId, jsonGenerator);
            }
            if (teamMemberLogInfo.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberLogInfo.displayName, jsonGenerator);
            }
            if (teamMemberLogInfo.email != null) {
                jsonGenerator.writeFieldName("email");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberLogInfo.email, jsonGenerator);
            }
            if (teamMemberLogInfo.teamMemberId != null) {
                jsonGenerator.writeFieldName("team_member_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberLogInfo.teamMemberId, jsonGenerator);
            }
            if (teamMemberLogInfo.memberExternalId != null) {
                jsonGenerator.writeFieldName("member_external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberLogInfo.memberExternalId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamMemberLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("team_member".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                String str6 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("account_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("email".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("team_member_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("member_external_id".equals(currentName)) {
                        str6 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                TeamMemberLogInfo teamMemberLogInfo = new TeamMemberLogInfo(str2, str3, str4, str5, str6);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(teamMemberLogInfo, teamMemberLogInfo.toStringMultiline());
                return teamMemberLogInfo;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamMemberLogInfo(String str, String str2, String str3, String str4, String str5) {
        super(str, str2, str3);
        this.teamMemberId = str4;
        if (str5 == null || str5.length() <= 64) {
            this.memberExternalId = str5;
            return;
        }
        throw new IllegalArgumentException("String 'memberExternalId' is longer than 64");
    }

    public TeamMemberLogInfo() {
        this(null, null, null, null, null);
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

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public String getMemberExternalId() {
        return this.memberExternalId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.teamMemberId, this.memberExternalId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x006e, code lost:
        if (r2.equals(r5) == false) goto L_0x0071;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0073
            com.dropbox.core.v2.teamlog.TeamMemberLogInfo r5 = (com.dropbox.core.p005v2.teamlog.TeamMemberLogInfo) r5
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            if (r2 == r3) goto L_0x002c
            java.lang.String r2 = r4.accountId
            if (r2 == 0) goto L_0x0071
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0071
        L_0x002c:
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            if (r2 == r3) goto L_0x0040
            java.lang.String r2 = r4.displayName
            if (r2 == 0) goto L_0x0071
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0071
        L_0x0040:
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            if (r2 == r3) goto L_0x0054
            java.lang.String r2 = r4.email
            if (r2 == 0) goto L_0x0071
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0071
        L_0x0054:
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r3 = r5.teamMemberId
            if (r2 == r3) goto L_0x0062
            if (r2 == 0) goto L_0x0071
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0071
        L_0x0062:
            java.lang.String r2 = r4.memberExternalId
            java.lang.String r5 = r5.memberExternalId
            if (r2 == r5) goto L_0x0072
            if (r2 == 0) goto L_0x0071
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0071
            goto L_0x0072
        L_0x0071:
            r0 = 0
        L_0x0072:
            return r0
        L_0x0073:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.TeamMemberLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
