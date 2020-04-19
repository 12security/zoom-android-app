package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.UserInfo */
public class UserInfo {
    protected final String accountId;
    protected final String displayName;
    protected final String email;
    protected final boolean sameTeam;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.sharing.UserInfo$Serializer */
    public static class Serializer extends StructSerializer<UserInfo> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(UserInfo userInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("account_id");
            StoneSerializers.string().serialize(userInfo.accountId, jsonGenerator);
            jsonGenerator.writeFieldName("email");
            StoneSerializers.string().serialize(userInfo.email, jsonGenerator);
            jsonGenerator.writeFieldName("display_name");
            StoneSerializers.string().serialize(userInfo.displayName, jsonGenerator);
            jsonGenerator.writeFieldName("same_team");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(userInfo.sameTeam), jsonGenerator);
            if (userInfo.teamMemberId != null) {
                jsonGenerator.writeFieldName("team_member_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(userInfo.teamMemberId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                String str4 = null;
                String str5 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("account_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("email".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str4 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("same_team".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("team_member_id".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"account_id\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"email\" missing.");
                } else if (str4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"display_name\" missing.");
                } else if (bool != null) {
                    UserInfo userInfo = new UserInfo(str2, str3, str4, bool.booleanValue(), str5);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(userInfo, userInfo.toStringMultiline());
                    return userInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"same_team\" missing.");
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

    public UserInfo(String str, String str2, String str3, boolean z, String str4) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'accountId' is null");
        } else if (str.length() < 40) {
            throw new IllegalArgumentException("String 'accountId' is shorter than 40");
        } else if (str.length() <= 40) {
            this.accountId = str;
            if (str2 != null) {
                this.email = str2;
                if (str3 != null) {
                    this.displayName = str3;
                    this.sameTeam = z;
                    this.teamMemberId = str4;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'displayName' is null");
            }
            throw new IllegalArgumentException("Required value for 'email' is null");
        } else {
            throw new IllegalArgumentException("String 'accountId' is longer than 40");
        }
    }

    public UserInfo(String str, String str2, String str3, boolean z) {
        this(str, str2, str3, z, null);
    }

    public String getAccountId() {
        return this.accountId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean getSameTeam() {
        return this.sameTeam;
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accountId, this.email, this.displayName, Boolean.valueOf(this.sameTeam), this.teamMemberId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004e, code lost:
        if (r2.equals(r5) == false) goto L_0x0051;
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
            if (r2 == 0) goto L_0x0053
            com.dropbox.core.v2.sharing.UserInfo r5 = (com.dropbox.core.p005v2.sharing.UserInfo) r5
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0024:
            java.lang.String r2 = r4.email
            java.lang.String r3 = r5.email
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0030:
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x003c:
            boolean r2 = r4.sameTeam
            boolean r3 = r5.sameTeam
            if (r2 != r3) goto L_0x0051
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r5 = r5.teamMemberId
            if (r2 == r5) goto L_0x0052
            if (r2 == 0) goto L_0x0051
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0051
            goto L_0x0052
        L_0x0051:
            r0 = 0
        L_0x0052:
            return r0
        L_0x0053:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.UserInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
