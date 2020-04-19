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

/* renamed from: com.dropbox.core.v2.team.GroupMembersAddArg */
class GroupMembersAddArg extends IncludeMembersArg {
    protected final GroupSelector group;
    protected final List<MemberAccess> members;

    /* renamed from: com.dropbox.core.v2.team.GroupMembersAddArg$Serializer */
    static class Serializer extends StructSerializer<GroupMembersAddArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupMembersAddArg groupMembersAddArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxGroup.TYPE);
            Serializer.INSTANCE.serialize(groupMembersAddArg.group, jsonGenerator);
            jsonGenerator.writeFieldName("members");
            StoneSerializers.list(Serializer.INSTANCE).serialize(groupMembersAddArg.members, jsonGenerator);
            jsonGenerator.writeFieldName("return_members");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(groupMembersAddArg.returnMembers), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupMembersAddArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    } else if ("members".equals(currentName)) {
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
                    GroupMembersAddArg groupMembersAddArg = new GroupMembersAddArg(groupSelector, list, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupMembersAddArg, groupMembersAddArg.toStringMultiline());
                    return groupMembersAddArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"members\" missing.");
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

    public GroupMembersAddArg(GroupSelector groupSelector, List<MemberAccess> list, boolean z) {
        super(z);
        if (groupSelector != null) {
            this.group = groupSelector;
            if (list != null) {
                for (MemberAccess memberAccess : list) {
                    if (memberAccess == null) {
                        throw new IllegalArgumentException("An item in list 'members' is null");
                    }
                }
                this.members = list;
                return;
            }
            throw new IllegalArgumentException("Required value for 'members' is null");
        }
        throw new IllegalArgumentException("Required value for 'group' is null");
    }

    public GroupMembersAddArg(GroupSelector groupSelector, List<MemberAccess> list) {
        this(groupSelector, list, true);
    }

    public GroupSelector getGroup() {
        return this.group;
    }

    public List<MemberAccess> getMembers() {
        return this.members;
    }

    public boolean getReturnMembers() {
        return this.returnMembers;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.group, this.members});
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
            com.dropbox.core.v2.team.GroupMembersAddArg r5 = (com.dropbox.core.p005v2.team.GroupMembersAddArg) r5
            com.dropbox.core.v2.team.GroupSelector r2 = r4.group
            com.dropbox.core.v2.team.GroupSelector r3 = r5.group
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
        L_0x0024:
            java.util.List<com.dropbox.core.v2.team.MemberAccess> r2 = r4.members
            java.util.List<com.dropbox.core.v2.team.MemberAccess> r3 = r5.members
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupMembersAddArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
