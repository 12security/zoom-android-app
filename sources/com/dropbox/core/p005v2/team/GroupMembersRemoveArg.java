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
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.GroupMembersRemoveArg */
class GroupMembersRemoveArg extends IncludeMembersArg {
    protected final GroupSelector group;
    protected final List<UserSelectorArg> users;

    /* renamed from: com.dropbox.core.v2.team.GroupMembersRemoveArg$Serializer */
    static class Serializer extends StructSerializer<GroupMembersRemoveArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupMembersRemoveArg groupMembersRemoveArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxGroup.TYPE);
            Serializer.INSTANCE.serialize(groupMembersRemoveArg.group, jsonGenerator);
            jsonGenerator.writeFieldName("users");
            StoneSerializers.list(Serializer.INSTANCE).serialize(groupMembersRemoveArg.users, jsonGenerator);
            jsonGenerator.writeFieldName("return_members");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(groupMembersRemoveArg.returnMembers), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupMembersRemoveArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            GroupSelector groupSelector = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(true);
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxGroup.TYPE.equals(currentName)) {
                        groupSelector = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("users".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("return_members".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (groupSelector == null) {
                    throw new JsonParseException(jsonParser, "Required field \"group\" missing.");
                } else if (list != null) {
                    GroupMembersRemoveArg groupMembersRemoveArg = new GroupMembersRemoveArg(groupSelector, list, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupMembersRemoveArg, groupMembersRemoveArg.toStringMultiline());
                    return groupMembersRemoveArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"users\" missing.");
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

    public GroupMembersRemoveArg(GroupSelector groupSelector, List<UserSelectorArg> list, boolean z) {
        super(z);
        if (groupSelector != null) {
            this.group = groupSelector;
            if (list != null) {
                for (UserSelectorArg userSelectorArg : list) {
                    if (userSelectorArg == null) {
                        throw new IllegalArgumentException("An item in list 'users' is null");
                    }
                }
                this.users = list;
                return;
            }
            throw new IllegalArgumentException("Required value for 'users' is null");
        }
        throw new IllegalArgumentException("Required value for 'group' is null");
    }

    public GroupMembersRemoveArg(GroupSelector groupSelector, List<UserSelectorArg> list) {
        this(groupSelector, list, true);
    }

    public GroupSelector getGroup() {
        return this.group;
    }

    public List<UserSelectorArg> getUsers() {
        return this.users;
    }

    public boolean getReturnMembers() {
        return this.returnMembers;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.group, this.users});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r3) == false) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        if (r4.returnMembers != r5.returnMembers) goto L_0x0037;
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
            if (r2 == 0) goto L_0x0039
            com.dropbox.core.v2.team.GroupMembersRemoveArg r5 = (com.dropbox.core.p005v2.team.GroupMembersRemoveArg) r5
            com.dropbox.core.v2.team.GroupSelector r2 = r4.group
            com.dropbox.core.v2.team.GroupSelector r3 = r5.group
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0024:
            java.util.List<com.dropbox.core.v2.team.UserSelectorArg> r2 = r4.users
            java.util.List<com.dropbox.core.v2.team.UserSelectorArg> r3 = r5.users
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0030:
            boolean r2 = r4.returnMembers
            boolean r5 = r5.returnMembers
            if (r2 != r5) goto L_0x0037
            goto L_0x0038
        L_0x0037:
            r0 = 0
        L_0x0038:
            return r0
        L_0x0039:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupMembersRemoveArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
