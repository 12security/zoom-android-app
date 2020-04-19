package com.dropbox.core.p005v2.team;

import com.box.androidsdk.content.models.BoxGroup;
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

/* renamed from: com.dropbox.core.v2.team.GroupMembersSetAccessTypeArg */
class GroupMembersSetAccessTypeArg extends GroupMemberSelector {
    protected final GroupAccessType accessType;
    protected final boolean returnMembers;

    /* renamed from: com.dropbox.core.v2.team.GroupMembersSetAccessTypeArg$Serializer */
    static class Serializer extends StructSerializer<GroupMembersSetAccessTypeArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupMembersSetAccessTypeArg groupMembersSetAccessTypeArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxGroup.TYPE);
            Serializer.INSTANCE.serialize(groupMembersSetAccessTypeArg.group, jsonGenerator);
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(groupMembersSetAccessTypeArg.user, jsonGenerator);
            jsonGenerator.writeFieldName("access_type");
            Serializer.INSTANCE.serialize(groupMembersSetAccessTypeArg.accessType, jsonGenerator);
            jsonGenerator.writeFieldName("return_members");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(groupMembersSetAccessTypeArg.returnMembers), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupMembersSetAccessTypeArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            GroupSelector groupSelector = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                GroupAccessType groupAccessType = null;
                Boolean valueOf = Boolean.valueOf(true);
                UserSelectorArg userSelectorArg = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxGroup.TYPE.equals(currentName)) {
                        groupSelector = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("access_type".equals(currentName)) {
                        groupAccessType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("return_members".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (groupSelector == null) {
                    throw new JsonParseException(jsonParser, "Required field \"group\" missing.");
                } else if (userSelectorArg == null) {
                    throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
                } else if (groupAccessType != null) {
                    GroupMembersSetAccessTypeArg groupMembersSetAccessTypeArg = new GroupMembersSetAccessTypeArg(groupSelector, userSelectorArg, groupAccessType, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupMembersSetAccessTypeArg, groupMembersSetAccessTypeArg.toStringMultiline());
                    return groupMembersSetAccessTypeArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"access_type\" missing.");
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

    public GroupMembersSetAccessTypeArg(GroupSelector groupSelector, UserSelectorArg userSelectorArg, GroupAccessType groupAccessType, boolean z) {
        super(groupSelector, userSelectorArg);
        if (groupAccessType != null) {
            this.accessType = groupAccessType;
            this.returnMembers = z;
            return;
        }
        throw new IllegalArgumentException("Required value for 'accessType' is null");
    }

    public GroupMembersSetAccessTypeArg(GroupSelector groupSelector, UserSelectorArg userSelectorArg, GroupAccessType groupAccessType) {
        this(groupSelector, userSelectorArg, groupAccessType, true);
    }

    public GroupSelector getGroup() {
        return this.group;
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public GroupAccessType getAccessType() {
        return this.accessType;
    }

    public boolean getReturnMembers() {
        return this.returnMembers;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.accessType, Boolean.valueOf(this.returnMembers)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0042, code lost:
        if (r2.equals(r3) == false) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0048, code lost:
        if (r4.returnMembers != r5.returnMembers) goto L_0x004b;
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
            com.dropbox.core.v2.team.GroupMembersSetAccessTypeArg r5 = (com.dropbox.core.p005v2.team.GroupMembersSetAccessTypeArg) r5
            com.dropbox.core.v2.team.GroupSelector r2 = r4.group
            com.dropbox.core.v2.team.GroupSelector r3 = r5.group
            if (r2 == r3) goto L_0x0028
            com.dropbox.core.v2.team.GroupSelector r2 = r4.group
            com.dropbox.core.v2.team.GroupSelector r3 = r5.group
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x0028:
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            if (r2 == r3) goto L_0x0038
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x0038:
            com.dropbox.core.v2.team.GroupAccessType r2 = r4.accessType
            com.dropbox.core.v2.team.GroupAccessType r3 = r5.accessType
            if (r2 == r3) goto L_0x0044
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004b
        L_0x0044:
            boolean r2 = r4.returnMembers
            boolean r5 = r5.returnMembers
            if (r2 != r5) goto L_0x004b
            goto L_0x004c
        L_0x004b:
            r0 = 0
        L_0x004c:
            return r0
        L_0x004d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupMembersSetAccessTypeArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
