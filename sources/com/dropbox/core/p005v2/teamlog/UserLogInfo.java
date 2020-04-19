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

/* renamed from: com.dropbox.core.v2.teamlog.UserLogInfo */
public class UserLogInfo {
    protected final String accountId;
    protected final String displayName;
    protected final String email;

    /* renamed from: com.dropbox.core.v2.teamlog.UserLogInfo$Builder */
    public static class Builder {
        protected String accountId = null;
        protected String displayName = null;
        protected String email = null;

        protected Builder() {
        }

        public Builder withAccountId(String str) {
            if (str != null) {
                if (str.length() < 40) {
                    throw new IllegalArgumentException("String 'accountId' is shorter than 40");
                } else if (str.length() > 40) {
                    throw new IllegalArgumentException("String 'accountId' is longer than 40");
                }
            }
            this.accountId = str;
            return this;
        }

        public Builder withDisplayName(String str) {
            if (str == null || str.length() >= 1) {
                this.displayName = str;
                return this;
            }
            throw new IllegalArgumentException("String 'displayName' is shorter than 1");
        }

        public Builder withEmail(String str) {
            if (str == null || str.length() <= 255) {
                this.email = str;
                return this;
            }
            throw new IllegalArgumentException("String 'email' is longer than 255");
        }

        public UserLogInfo build() {
            return new UserLogInfo(this.accountId, this.displayName, this.email);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.UserLogInfo$Serializer */
    static class Serializer extends StructSerializer<UserLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserLogInfo userLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (userLogInfo instanceof TeamMemberLogInfo) {
                Serializer.INSTANCE.serialize((TeamMemberLogInfo) userLogInfo, jsonGenerator, z);
            } else if (userLogInfo instanceof NonTeamMemberLogInfo) {
                Serializer.INSTANCE.serialize((NonTeamMemberLogInfo) userLogInfo, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                if (userLogInfo.accountId != null) {
                    jsonGenerator.writeFieldName("account_id");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(userLogInfo.accountId, jsonGenerator);
                }
                if (userLogInfo.displayName != null) {
                    jsonGenerator.writeFieldName("display_name");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(userLogInfo.displayName, jsonGenerator);
                }
                if (userLogInfo.email != null) {
                    jsonGenerator.writeFieldName("email");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(userLogInfo.email, jsonGenerator);
                }
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public UserLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserLogInfo userLogInfo;
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
                userLogInfo = new UserLogInfo(str2, str3, str4);
            } else if ("".equals(str)) {
                userLogInfo = INSTANCE.deserialize(jsonParser, true);
            } else if ("team_member".equals(str)) {
                userLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("non_team_member".equals(str)) {
                userLogInfo = Serializer.INSTANCE.deserialize(jsonParser, true);
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
            StoneDeserializerLogger.log(userLogInfo, userLogInfo.toStringMultiline());
            return userLogInfo;
        }
    }

    public UserLogInfo(String str, String str2, String str3) {
        if (str != null) {
            if (str.length() < 40) {
                throw new IllegalArgumentException("String 'accountId' is shorter than 40");
            } else if (str.length() > 40) {
                throw new IllegalArgumentException("String 'accountId' is longer than 40");
            }
        }
        this.accountId = str;
        if (str2 == null || str2.length() >= 1) {
            this.displayName = str2;
            if (str3 == null || str3.length() <= 255) {
                this.email = str3;
                return;
            }
            throw new IllegalArgumentException("String 'email' is longer than 255");
        }
        throw new IllegalArgumentException("String 'displayName' is shorter than 1");
    }

    public UserLogInfo() {
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
        return Arrays.hashCode(new Object[]{this.accountId, this.displayName, this.email});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r2.equals(r5) == false) goto L_0x0043;
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
            if (r2 == 0) goto L_0x0045
            com.dropbox.core.v2.teamlog.UserLogInfo r5 = (com.dropbox.core.p005v2.teamlog.UserLogInfo) r5
            java.lang.String r2 = r4.accountId
            java.lang.String r3 = r5.accountId
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0026:
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0034:
            java.lang.String r2 = r4.email
            java.lang.String r5 = r5.email
            if (r2 == r5) goto L_0x0044
            if (r2 == 0) goto L_0x0043
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.UserLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
