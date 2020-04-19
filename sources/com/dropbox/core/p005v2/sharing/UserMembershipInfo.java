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
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.UserMembershipInfo */
public class UserMembershipInfo extends MembershipInfo {
    protected final UserInfo user;

    /* renamed from: com.dropbox.core.v2.sharing.UserMembershipInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.MembershipInfo.Builder {
        protected final UserInfo user;

        protected Builder(AccessLevel accessLevel, UserInfo userInfo) {
            super(accessLevel);
            if (userInfo != null) {
                this.user = userInfo;
                return;
            }
            throw new IllegalArgumentException("Required value for 'user' is null");
        }

        public Builder withPermissions(List<MemberPermission> list) {
            super.withPermissions(list);
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

        public UserMembershipInfo build() {
            UserMembershipInfo userMembershipInfo = new UserMembershipInfo(this.accessType, this.user, this.permissions, this.initials, this.isInherited);
            return userMembershipInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.UserMembershipInfo$Serializer */
    static class Serializer extends StructSerializer<UserMembershipInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserMembershipInfo userMembershipInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("access_type");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(userMembershipInfo.accessType, jsonGenerator);
            jsonGenerator.writeFieldName("user");
            com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE.serialize(userMembershipInfo.user, jsonGenerator);
            if (userMembershipInfo.permissions != null) {
                jsonGenerator.writeFieldName("permissions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(userMembershipInfo.permissions, jsonGenerator);
            }
            if (userMembershipInfo.initials != null) {
                jsonGenerator.writeFieldName("initials");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(userMembershipInfo.initials, jsonGenerator);
            }
            jsonGenerator.writeFieldName("is_inherited");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(userMembershipInfo.isInherited), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserMembershipInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel == null) {
                    throw new JsonParseException(jsonParser, "Required field \"access_type\" missing.");
                } else if (userInfo != null) {
                    UserMembershipInfo userMembershipInfo = new UserMembershipInfo(accessLevel, userInfo, list, str2, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(userMembershipInfo, userMembershipInfo.toStringMultiline());
                    return userMembershipInfo;
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

    public UserMembershipInfo(AccessLevel accessLevel, UserInfo userInfo, List<MemberPermission> list, String str, boolean z) {
        super(accessLevel, list, str, z);
        if (userInfo != null) {
            this.user = userInfo;
            return;
        }
        throw new IllegalArgumentException("Required value for 'user' is null");
    }

    public UserMembershipInfo(AccessLevel accessLevel, UserInfo userInfo) {
        this(accessLevel, userInfo, null, null, false);
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

    public static Builder newBuilder(AccessLevel accessLevel, UserInfo userInfo) {
        return new Builder(accessLevel, userInfo);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.user});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
        if (r2.equals(r3) == false) goto L_0x0063;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0046, code lost:
        if (r4.permissions.equals(r5.permissions) == false) goto L_0x0063;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005a, code lost:
        if (r4.initials.equals(r5.initials) == false) goto L_0x0063;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0060, code lost:
        if (r4.isInherited != r5.isInherited) goto L_0x0063;
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
            if (r2 == 0) goto L_0x0065
            com.dropbox.core.v2.sharing.UserMembershipInfo r5 = (com.dropbox.core.p005v2.sharing.UserMembershipInfo) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            if (r2 == r3) goto L_0x0028
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0063
        L_0x0028:
            com.dropbox.core.v2.sharing.UserInfo r2 = r4.user
            com.dropbox.core.v2.sharing.UserInfo r3 = r5.user
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0063
        L_0x0034:
            java.util.List r2 = r4.permissions
            java.util.List r3 = r5.permissions
            if (r2 == r3) goto L_0x0048
            java.util.List r2 = r4.permissions
            if (r2 == 0) goto L_0x0063
            java.util.List r2 = r4.permissions
            java.util.List r3 = r5.permissions
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0063
        L_0x0048:
            java.lang.String r2 = r4.initials
            java.lang.String r3 = r5.initials
            if (r2 == r3) goto L_0x005c
            java.lang.String r2 = r4.initials
            if (r2 == 0) goto L_0x0063
            java.lang.String r2 = r4.initials
            java.lang.String r3 = r5.initials
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0063
        L_0x005c:
            boolean r2 = r4.isInherited
            boolean r5 = r5.isInherited
            if (r2 != r5) goto L_0x0063
            goto L_0x0064
        L_0x0063:
            r0 = 0
        L_0x0064:
            return r0
        L_0x0065:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.UserMembershipInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
