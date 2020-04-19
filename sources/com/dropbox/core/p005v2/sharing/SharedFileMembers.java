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

/* renamed from: com.dropbox.core.v2.sharing.SharedFileMembers */
public class SharedFileMembers {
    protected final String cursor;
    protected final List<GroupMembershipInfo> groups;
    protected final List<InviteeMembershipInfo> invitees;
    protected final List<UserFileMembershipInfo> users;

    /* renamed from: com.dropbox.core.v2.sharing.SharedFileMembers$Serializer */
    static class Serializer extends StructSerializer<SharedFileMembers> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedFileMembers sharedFileMembers, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("users");
            StoneSerializers.list(Serializer.INSTANCE).serialize(sharedFileMembers.users, jsonGenerator);
            jsonGenerator.writeFieldName("groups");
            StoneSerializers.list(Serializer.INSTANCE).serialize(sharedFileMembers.groups, jsonGenerator);
            jsonGenerator.writeFieldName("invitees");
            StoneSerializers.list(Serializer.INSTANCE).serialize(sharedFileMembers.invitees, jsonGenerator);
            if (sharedFileMembers.cursor != null) {
                jsonGenerator.writeFieldName("cursor");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedFileMembers.cursor, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedFileMembers deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list2 = null;
                List list3 = null;
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("users".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("groups".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("invitees".equals(currentName)) {
                        list3 = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("cursor".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"users\" missing.");
                } else if (list2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"groups\" missing.");
                } else if (list3 != null) {
                    SharedFileMembers sharedFileMembers = new SharedFileMembers(list, list2, list3, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedFileMembers, sharedFileMembers.toStringMultiline());
                    return sharedFileMembers;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"invitees\" missing.");
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

    public SharedFileMembers(List<UserFileMembershipInfo> list, List<GroupMembershipInfo> list2, List<InviteeMembershipInfo> list3, String str) {
        if (list != null) {
            for (UserFileMembershipInfo userFileMembershipInfo : list) {
                if (userFileMembershipInfo == null) {
                    throw new IllegalArgumentException("An item in list 'users' is null");
                }
            }
            this.users = list;
            if (list2 != null) {
                for (GroupMembershipInfo groupMembershipInfo : list2) {
                    if (groupMembershipInfo == null) {
                        throw new IllegalArgumentException("An item in list 'groups' is null");
                    }
                }
                this.groups = list2;
                if (list3 != null) {
                    for (InviteeMembershipInfo inviteeMembershipInfo : list3) {
                        if (inviteeMembershipInfo == null) {
                            throw new IllegalArgumentException("An item in list 'invitees' is null");
                        }
                    }
                    this.invitees = list3;
                    this.cursor = str;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'invitees' is null");
            }
            throw new IllegalArgumentException("Required value for 'groups' is null");
        }
        throw new IllegalArgumentException("Required value for 'users' is null");
    }

    public SharedFileMembers(List<UserFileMembershipInfo> list, List<GroupMembershipInfo> list2, List<InviteeMembershipInfo> list3) {
        this(list, list2, list3, null);
    }

    public List<UserFileMembershipInfo> getUsers() {
        return this.users;
    }

    public List<GroupMembershipInfo> getGroups() {
        return this.groups;
    }

    public List<InviteeMembershipInfo> getInvitees() {
        return this.invitees;
    }

    public String getCursor() {
        return this.cursor;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.users, this.groups, this.invitees, this.cursor});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0048, code lost:
        if (r2.equals(r5) == false) goto L_0x004b;
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
            if (r2 == 0) goto L_0x004d
            com.dropbox.core.v2.sharing.SharedFileMembers r5 = (com.dropbox.core.p005v2.sharing.SharedFileMembers) r5
            java.util.List<com.dropbox.core.v2.sharing.UserFileMembershipInfo> r2 = r4.users
            java.util.List<com.dropbox.core.v2.sharing.UserFileMembershipInfo> r3 = r5.users
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x0024:
            java.util.List<com.dropbox.core.v2.sharing.GroupMembershipInfo> r2 = r4.groups
            java.util.List<com.dropbox.core.v2.sharing.GroupMembershipInfo> r3 = r5.groups
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x0030:
            java.util.List<com.dropbox.core.v2.sharing.InviteeMembershipInfo> r2 = r4.invitees
            java.util.List<com.dropbox.core.v2.sharing.InviteeMembershipInfo> r3 = r5.invitees
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x003c:
            java.lang.String r2 = r4.cursor
            java.lang.String r5 = r5.cursor
            if (r2 == r5) goto L_0x004c
            if (r2 == 0) goto L_0x004b
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x004b
            goto L_0x004c
        L_0x004b:
            r0 = 0
        L_0x004c:
            return r0
        L_0x004d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SharedFileMembers.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
