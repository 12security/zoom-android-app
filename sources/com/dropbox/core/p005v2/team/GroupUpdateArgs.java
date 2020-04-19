package com.dropbox.core.p005v2.team;

import com.box.androidsdk.content.models.BoxGroup;
import com.dropbox.core.p005v2.teamcommon.GroupManagementType;
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

/* renamed from: com.dropbox.core.v2.team.GroupUpdateArgs */
class GroupUpdateArgs extends IncludeMembersArg {
    protected final GroupSelector group;
    protected final String newGroupExternalId;
    protected final GroupManagementType newGroupManagementType;
    protected final String newGroupName;

    /* renamed from: com.dropbox.core.v2.team.GroupUpdateArgs$Builder */
    public static class Builder {
        protected final GroupSelector group;
        protected String newGroupExternalId;
        protected GroupManagementType newGroupManagementType;
        protected String newGroupName;
        protected boolean returnMembers;

        protected Builder(GroupSelector groupSelector) {
            if (groupSelector != null) {
                this.group = groupSelector;
                this.returnMembers = true;
                this.newGroupName = null;
                this.newGroupExternalId = null;
                this.newGroupManagementType = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'group' is null");
        }

        public Builder withReturnMembers(Boolean bool) {
            if (bool != null) {
                this.returnMembers = bool.booleanValue();
            } else {
                this.returnMembers = true;
            }
            return this;
        }

        public Builder withNewGroupName(String str) {
            this.newGroupName = str;
            return this;
        }

        public Builder withNewGroupExternalId(String str) {
            this.newGroupExternalId = str;
            return this;
        }

        public Builder withNewGroupManagementType(GroupManagementType groupManagementType) {
            this.newGroupManagementType = groupManagementType;
            return this;
        }

        public GroupUpdateArgs build() {
            GroupUpdateArgs groupUpdateArgs = new GroupUpdateArgs(this.group, this.returnMembers, this.newGroupName, this.newGroupExternalId, this.newGroupManagementType);
            return groupUpdateArgs;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupUpdateArgs$Serializer */
    static class Serializer extends StructSerializer<GroupUpdateArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupUpdateArgs groupUpdateArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxGroup.TYPE);
            Serializer.INSTANCE.serialize(groupUpdateArgs.group, jsonGenerator);
            jsonGenerator.writeFieldName("return_members");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(groupUpdateArgs.returnMembers), jsonGenerator);
            if (groupUpdateArgs.newGroupName != null) {
                jsonGenerator.writeFieldName("new_group_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(groupUpdateArgs.newGroupName, jsonGenerator);
            }
            if (groupUpdateArgs.newGroupExternalId != null) {
                jsonGenerator.writeFieldName("new_group_external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(groupUpdateArgs.newGroupExternalId, jsonGenerator);
            }
            if (groupUpdateArgs.newGroupManagementType != null) {
                jsonGenerator.writeFieldName("new_group_management_type");
                StoneSerializers.nullable(com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE).serialize(groupUpdateArgs.newGroupManagementType, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupUpdateArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(true);
                GroupSelector groupSelector = null;
                String str2 = null;
                String str3 = null;
                GroupManagementType groupManagementType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxGroup.TYPE.equals(currentName)) {
                        groupSelector = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("return_members".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("new_group_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_group_external_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("new_group_management_type".equals(currentName)) {
                        groupManagementType = (GroupManagementType) StoneSerializers.nullable(com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (groupSelector != null) {
                    GroupUpdateArgs groupUpdateArgs = new GroupUpdateArgs(groupSelector, valueOf.booleanValue(), str2, str3, groupManagementType);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupUpdateArgs, groupUpdateArgs.toStringMultiline());
                    return groupUpdateArgs;
                }
                throw new JsonParseException(jsonParser, "Required field \"group\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GroupUpdateArgs(GroupSelector groupSelector, boolean z, String str, String str2, GroupManagementType groupManagementType) {
        super(z);
        if (groupSelector != null) {
            this.group = groupSelector;
            this.newGroupName = str;
            this.newGroupExternalId = str2;
            this.newGroupManagementType = groupManagementType;
            return;
        }
        throw new IllegalArgumentException("Required value for 'group' is null");
    }

    public GroupUpdateArgs(GroupSelector groupSelector) {
        this(groupSelector, true, null, null, null);
    }

    public GroupSelector getGroup() {
        return this.group;
    }

    public boolean getReturnMembers() {
        return this.returnMembers;
    }

    public String getNewGroupName() {
        return this.newGroupName;
    }

    public String getNewGroupExternalId() {
        return this.newGroupExternalId;
    }

    public GroupManagementType getNewGroupManagementType() {
        return this.newGroupManagementType;
    }

    public static Builder newBuilder(GroupSelector groupSelector) {
        return new Builder(groupSelector);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.group, this.newGroupName, this.newGroupExternalId, this.newGroupManagementType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0052, code lost:
        if (r2.equals(r5) == false) goto L_0x0055;
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
            if (r2 == 0) goto L_0x0057
            com.dropbox.core.v2.team.GroupUpdateArgs r5 = (com.dropbox.core.p005v2.team.GroupUpdateArgs) r5
            com.dropbox.core.v2.team.GroupSelector r2 = r4.group
            com.dropbox.core.v2.team.GroupSelector r3 = r5.group
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0055
        L_0x0024:
            boolean r2 = r4.returnMembers
            boolean r3 = r5.returnMembers
            if (r2 != r3) goto L_0x0055
            java.lang.String r2 = r4.newGroupName
            java.lang.String r3 = r5.newGroupName
            if (r2 == r3) goto L_0x0038
            if (r2 == 0) goto L_0x0055
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0055
        L_0x0038:
            java.lang.String r2 = r4.newGroupExternalId
            java.lang.String r3 = r5.newGroupExternalId
            if (r2 == r3) goto L_0x0046
            if (r2 == 0) goto L_0x0055
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0055
        L_0x0046:
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r4.newGroupManagementType
            com.dropbox.core.v2.teamcommon.GroupManagementType r5 = r5.newGroupManagementType
            if (r2 == r5) goto L_0x0056
            if (r2 == 0) goto L_0x0055
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0055
            goto L_0x0056
        L_0x0055:
            r0 = 0
        L_0x0056:
            return r0
        L_0x0057:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupUpdateArgs.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
