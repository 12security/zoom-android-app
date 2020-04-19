package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.p005v2.seenstate.PlatformType;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.UserFileMembershipInfo */
public class UserFileMembershipInfo extends UserMembershipInfo {
    protected final PlatformType platformType;
    protected final Date timeLastSeen;

    /* renamed from: com.dropbox.core.v2.sharing.UserFileMembershipInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.UserMembershipInfo.Builder {
        protected PlatformType platformType = null;
        protected Date timeLastSeen = null;

        protected Builder(AccessLevel accessLevel, UserInfo userInfo) {
            super(accessLevel, userInfo);
        }

        public Builder withTimeLastSeen(Date date) {
            this.timeLastSeen = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withPlatformType(PlatformType platformType2) {
            this.platformType = platformType2;
            return this;
        }

        public Builder withPermissions(List<MemberPermission> list) {
            super.withPermissions((List) list);
            return this;
        }

        public Builder withInitials(String str) {
            super.withInitials(str);
            return this;
        }

        public Builder withIsInherited(Boolean bool) {
            super.withIsInherited(bool);
            return this;
        }

        public UserFileMembershipInfo build() {
            UserFileMembershipInfo userFileMembershipInfo = new UserFileMembershipInfo(this.accessType, this.user, this.permissions, this.initials, this.isInherited, this.timeLastSeen, this.platformType);
            return userFileMembershipInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.UserFileMembershipInfo$Serializer */
    static class Serializer extends StructSerializer<UserFileMembershipInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserFileMembershipInfo userFileMembershipInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("access_type");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(userFileMembershipInfo.accessType, jsonGenerator);
            jsonGenerator.writeFieldName("user");
            com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE.serialize(userFileMembershipInfo.user, jsonGenerator);
            if (userFileMembershipInfo.permissions != null) {
                jsonGenerator.writeFieldName("permissions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(userFileMembershipInfo.permissions, jsonGenerator);
            }
            if (userFileMembershipInfo.initials != null) {
                jsonGenerator.writeFieldName("initials");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(userFileMembershipInfo.initials, jsonGenerator);
            }
            jsonGenerator.writeFieldName("is_inherited");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(userFileMembershipInfo.isInherited), jsonGenerator);
            if (userFileMembershipInfo.timeLastSeen != null) {
                jsonGenerator.writeFieldName("time_last_seen");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(userFileMembershipInfo.timeLastSeen, jsonGenerator);
            }
            if (userFileMembershipInfo.platformType != null) {
                jsonGenerator.writeFieldName("platform_type");
                StoneSerializers.nullable(com.dropbox.core.p005v2.seenstate.PlatformType.Serializer.INSTANCE).serialize(userFileMembershipInfo.platformType, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserFileMembershipInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                AccessLevel accessLevel = null;
                UserInfo userInfo = null;
                List list = null;
                String str2 = null;
                Date date = null;
                PlatformType platformType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("access_type".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("user".equals(currentName)) {
                        userInfo = (UserInfo) com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("permissions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("initials".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("is_inherited".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("time_last_seen".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("platform_type".equals(currentName)) {
                        platformType = (PlatformType) StoneSerializers.nullable(com.dropbox.core.p005v2.seenstate.PlatformType.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel == null) {
                    throw new JsonParseException(jsonParser, "Required field \"access_type\" missing.");
                } else if (userInfo != null) {
                    UserFileMembershipInfo userFileMembershipInfo = new UserFileMembershipInfo(accessLevel, userInfo, list, str2, valueOf.booleanValue(), date, platformType);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(userFileMembershipInfo, userFileMembershipInfo.toStringMultiline());
                    return userFileMembershipInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
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

    public UserFileMembershipInfo(AccessLevel accessLevel, UserInfo userInfo, List<MemberPermission> list, String str, boolean z, Date date, PlatformType platformType2) {
        super(accessLevel, userInfo, list, str, z);
        this.timeLastSeen = LangUtil.truncateMillis(date);
        this.platformType = platformType2;
    }

    public UserFileMembershipInfo(AccessLevel accessLevel, UserInfo userInfo) {
        this(accessLevel, userInfo, null, null, false, null, null);
    }

    public AccessLevel getAccessType() {
        return this.accessType;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public List<MemberPermission> getPermissions() {
        return this.permissions;
    }

    public String getInitials() {
        return this.initials;
    }

    public boolean getIsInherited() {
        return this.isInherited;
    }

    public Date getTimeLastSeen() {
        return this.timeLastSeen;
    }

    public PlatformType getPlatformType() {
        return this.platformType;
    }

    public static Builder newBuilder(AccessLevel accessLevel, UserInfo userInfo) {
        return new Builder(accessLevel, userInfo);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.timeLastSeen, this.platformType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0080, code lost:
        if (r2.equals(r5) == false) goto L_0x0083;
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
            if (r2 == 0) goto L_0x0085
            com.dropbox.core.v2.sharing.UserFileMembershipInfo r5 = (com.dropbox.core.p005v2.sharing.UserFileMembershipInfo) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            if (r2 == r3) goto L_0x0028
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0028:
            com.dropbox.core.v2.sharing.UserInfo r2 = r4.user
            com.dropbox.core.v2.sharing.UserInfo r3 = r5.user
            if (r2 == r3) goto L_0x0038
            com.dropbox.core.v2.sharing.UserInfo r2 = r4.user
            com.dropbox.core.v2.sharing.UserInfo r3 = r5.user
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0038:
            java.util.List r2 = r4.permissions
            java.util.List r3 = r5.permissions
            if (r2 == r3) goto L_0x004c
            java.util.List r2 = r4.permissions
            if (r2 == 0) goto L_0x0083
            java.util.List r2 = r4.permissions
            java.util.List r3 = r5.permissions
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x004c:
            java.lang.String r2 = r4.initials
            java.lang.String r3 = r5.initials
            if (r2 == r3) goto L_0x0060
            java.lang.String r2 = r4.initials
            if (r2 == 0) goto L_0x0083
            java.lang.String r2 = r4.initials
            java.lang.String r3 = r5.initials
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0060:
            boolean r2 = r4.isInherited
            boolean r3 = r5.isInherited
            if (r2 != r3) goto L_0x0083
            java.util.Date r2 = r4.timeLastSeen
            java.util.Date r3 = r5.timeLastSeen
            if (r2 == r3) goto L_0x0074
            if (r2 == 0) goto L_0x0083
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0083
        L_0x0074:
            com.dropbox.core.v2.seenstate.PlatformType r2 = r4.platformType
            com.dropbox.core.v2.seenstate.PlatformType r5 = r5.platformType
            if (r2 == r5) goto L_0x0084
            if (r2 == 0) goto L_0x0083
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0083
            goto L_0x0084
        L_0x0083:
            r0 = 0
        L_0x0084:
            return r0
        L_0x0085:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.UserFileMembershipInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
