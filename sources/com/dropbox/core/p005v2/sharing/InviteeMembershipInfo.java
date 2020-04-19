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

/* renamed from: com.dropbox.core.v2.sharing.InviteeMembershipInfo */
public class InviteeMembershipInfo extends MembershipInfo {
    protected final InviteeInfo invitee;
    protected final UserInfo user;

    /* renamed from: com.dropbox.core.v2.sharing.InviteeMembershipInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.MembershipInfo.Builder {
        protected final InviteeInfo invitee;
        protected UserInfo user;

        protected Builder(AccessLevel accessLevel, InviteeInfo inviteeInfo) {
            super(accessLevel);
            if (inviteeInfo != null) {
                this.invitee = inviteeInfo;
                this.user = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'invitee' is null");
        }

        public Builder withUser(UserInfo userInfo) {
            this.user = userInfo;
            return this;
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

        public InviteeMembershipInfo build() {
            InviteeMembershipInfo inviteeMembershipInfo = new InviteeMembershipInfo(this.accessType, this.invitee, this.permissions, this.initials, this.isInherited, this.user);
            return inviteeMembershipInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.InviteeMembershipInfo$Serializer */
    static class Serializer extends StructSerializer<InviteeMembershipInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(InviteeMembershipInfo inviteeMembershipInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("access_type");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(inviteeMembershipInfo.accessType, jsonGenerator);
            jsonGenerator.writeFieldName("invitee");
            com.dropbox.core.p005v2.sharing.InviteeInfo.Serializer.INSTANCE.serialize(inviteeMembershipInfo.invitee, jsonGenerator);
            if (inviteeMembershipInfo.permissions != null) {
                jsonGenerator.writeFieldName("permissions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(inviteeMembershipInfo.permissions, jsonGenerator);
            }
            if (inviteeMembershipInfo.initials != null) {
                jsonGenerator.writeFieldName("initials");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(inviteeMembershipInfo.initials, jsonGenerator);
            }
            jsonGenerator.writeFieldName("is_inherited");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(inviteeMembershipInfo.isInherited), jsonGenerator);
            if (inviteeMembershipInfo.user != null) {
                jsonGenerator.writeFieldName("user");
                StoneSerializers.nullableStruct(com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE).serialize(inviteeMembershipInfo.user, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public InviteeMembershipInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                InviteeInfo inviteeInfo = null;
                List list = null;
                String str2 = null;
                UserInfo userInfo = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("access_type".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("invitee".equals(currentName)) {
                        inviteeInfo = com.dropbox.core.p005v2.sharing.InviteeInfo.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("permissions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("initials".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("is_inherited".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("user".equals(currentName)) {
                        userInfo = (UserInfo) StoneSerializers.nullableStruct(com.dropbox.core.p005v2.sharing.UserInfo.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel == null) {
                    throw new JsonParseException(jsonParser, "Required field \"access_type\" missing.");
                } else if (inviteeInfo != null) {
                    InviteeMembershipInfo inviteeMembershipInfo = new InviteeMembershipInfo(accessLevel, inviteeInfo, list, str2, valueOf.booleanValue(), userInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(inviteeMembershipInfo, inviteeMembershipInfo.toStringMultiline());
                    return inviteeMembershipInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"invitee\" missing.");
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

    public InviteeMembershipInfo(AccessLevel accessLevel, InviteeInfo inviteeInfo, List<MemberPermission> list, String str, boolean z, UserInfo userInfo) {
        super(accessLevel, list, str, z);
        if (inviteeInfo != null) {
            this.invitee = inviteeInfo;
            this.user = userInfo;
            return;
        }
        throw new IllegalArgumentException("Required value for 'invitee' is null");
    }

    public InviteeMembershipInfo(AccessLevel accessLevel, InviteeInfo inviteeInfo) {
        this(accessLevel, inviteeInfo, null, null, false, null);
    }

    public AccessLevel getAccessType() {
        return this.accessType;
    }

    public InviteeInfo getInvitee() {
        return this.invitee;
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

    public UserInfo getUser() {
        return this.user;
    }

    public static Builder newBuilder(AccessLevel accessLevel, InviteeInfo inviteeInfo) {
        return new Builder(accessLevel, inviteeInfo);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.invitee, this.user});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x006e, code lost:
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
            com.dropbox.core.v2.sharing.InviteeMembershipInfo r5 = (com.dropbox.core.p005v2.sharing.InviteeMembershipInfo) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            if (r2 == r3) goto L_0x0028
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessType
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessType
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0071
        L_0x0028:
            com.dropbox.core.v2.sharing.InviteeInfo r2 = r4.invitee
            com.dropbox.core.v2.sharing.InviteeInfo r3 = r5.invitee
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0071
        L_0x0034:
            java.util.List r2 = r4.permissions
            java.util.List r3 = r5.permissions
            if (r2 == r3) goto L_0x0048
            java.util.List r2 = r4.permissions
            if (r2 == 0) goto L_0x0071
            java.util.List r2 = r4.permissions
            java.util.List r3 = r5.permissions
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0071
        L_0x0048:
            java.lang.String r2 = r4.initials
            java.lang.String r3 = r5.initials
            if (r2 == r3) goto L_0x005c
            java.lang.String r2 = r4.initials
            if (r2 == 0) goto L_0x0071
            java.lang.String r2 = r4.initials
            java.lang.String r3 = r5.initials
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0071
        L_0x005c:
            boolean r2 = r4.isInherited
            boolean r3 = r5.isInherited
            if (r2 != r3) goto L_0x0071
            com.dropbox.core.v2.sharing.UserInfo r2 = r4.user
            com.dropbox.core.v2.sharing.UserInfo r5 = r5.user
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.InviteeMembershipInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
