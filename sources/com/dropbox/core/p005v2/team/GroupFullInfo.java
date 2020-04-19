package com.dropbox.core.p005v2.team;

import com.dropbox.core.p005v2.teamcommon.GroupManagementType;
import com.dropbox.core.p005v2.teamcommon.GroupSummary;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.GroupFullInfo */
public class GroupFullInfo extends GroupSummary {
    protected final long created;
    protected final List<GroupMemberInfo> members;

    /* renamed from: com.dropbox.core.v2.team.GroupFullInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamcommon.GroupSummary.Builder {
        protected final long created;
        protected List<GroupMemberInfo> members = null;

        protected Builder(String str, String str2, GroupManagementType groupManagementType, long j) {
            super(str, str2, groupManagementType);
            this.created = j;
        }

        public Builder withMembers(List<GroupMemberInfo> list) {
            if (list != null) {
                for (GroupMemberInfo groupMemberInfo : list) {
                    if (groupMemberInfo == null) {
                        throw new IllegalArgumentException("An item in list 'members' is null");
                    }
                }
            }
            this.members = list;
            return this;
        }

        public Builder withGroupExternalId(String str) {
            super.withGroupExternalId(str);
            return this;
        }

        public Builder withMemberCount(Long l) {
            super.withMemberCount(l);
            return this;
        }

        public GroupFullInfo build() {
            GroupFullInfo groupFullInfo = new GroupFullInfo(this.groupName, this.groupId, this.groupManagementType, this.created, this.groupExternalId, this.memberCount, this.members);
            return groupFullInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupFullInfo$Serializer */
    static class Serializer extends StructSerializer<GroupFullInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupFullInfo groupFullInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("group_name");
            StoneSerializers.string().serialize(groupFullInfo.groupName, jsonGenerator);
            jsonGenerator.writeFieldName(Param.GROUP_ID);
            StoneSerializers.string().serialize(groupFullInfo.groupId, jsonGenerator);
            jsonGenerator.writeFieldName("group_management_type");
            com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE.serialize(groupFullInfo.groupManagementType, jsonGenerator);
            jsonGenerator.writeFieldName("created");
            StoneSerializers.uInt64().serialize(Long.valueOf(groupFullInfo.created), jsonGenerator);
            if (groupFullInfo.groupExternalId != null) {
                jsonGenerator.writeFieldName("group_external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(groupFullInfo.groupExternalId, jsonGenerator);
            }
            if (groupFullInfo.memberCount != null) {
                jsonGenerator.writeFieldName("member_count");
                StoneSerializers.nullable(StoneSerializers.uInt32()).serialize(groupFullInfo.memberCount, jsonGenerator);
            }
            if (groupFullInfo.members != null) {
                jsonGenerator.writeFieldName("members");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(groupFullInfo.members, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupFullInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                GroupManagementType groupManagementType = null;
                String str4 = null;
                Long l2 = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("group_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (Param.GROUP_ID.equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("group_management_type".equals(currentName)) {
                        groupManagementType = com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("created".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("group_external_id".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("member_count".equals(currentName)) {
                        l2 = (Long) StoneSerializers.nullable(StoneSerializers.uInt32()).deserialize(jsonParser);
                    } else if ("members".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"group_name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"group_id\" missing.");
                } else if (groupManagementType == null) {
                    throw new JsonParseException(jsonParser, "Required field \"group_management_type\" missing.");
                } else if (l != null) {
                    GroupFullInfo groupFullInfo = new GroupFullInfo(str2, str3, groupManagementType, l.longValue(), str4, l2, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupFullInfo, groupFullInfo.toStringMultiline());
                    return groupFullInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"created\" missing.");
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

    public GroupFullInfo(String str, String str2, GroupManagementType groupManagementType, long j, String str3, Long l, List<GroupMemberInfo> list) {
        super(str, str2, groupManagementType, str3, l);
        if (list != null) {
            for (GroupMemberInfo groupMemberInfo : list) {
                if (groupMemberInfo == null) {
                    throw new IllegalArgumentException("An item in list 'members' is null");
                }
            }
        }
        this.members = list;
        this.created = j;
    }

    public GroupFullInfo(String str, String str2, GroupManagementType groupManagementType, long j) {
        this(str, str2, groupManagementType, j, null, null, null);
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public GroupManagementType getGroupManagementType() {
        return this.groupManagementType;
    }

    public long getCreated() {
        return this.created;
    }

    public String getGroupExternalId() {
        return this.groupExternalId;
    }

    public Long getMemberCount() {
        return this.memberCount;
    }

    public List<GroupMemberInfo> getMembers() {
        return this.members;
    }

    public static Builder newBuilder(String str, String str2, GroupManagementType groupManagementType, long j) {
        Builder builder = new Builder(str, str2, groupManagementType, j);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.members, Long.valueOf(this.created)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0084, code lost:
        if (r2.equals(r7) == false) goto L_0x0087;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0089
            com.dropbox.core.v2.team.GroupFullInfo r7 = (com.dropbox.core.p005v2.team.GroupFullInfo) r7
            java.lang.String r2 = r6.groupName
            java.lang.String r3 = r7.groupName
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r6.groupName
            java.lang.String r3 = r7.groupName
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0028:
            java.lang.String r2 = r6.groupId
            java.lang.String r3 = r7.groupId
            if (r2 == r3) goto L_0x0038
            java.lang.String r2 = r6.groupId
            java.lang.String r3 = r7.groupId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0038:
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r6.groupManagementType
            com.dropbox.core.v2.teamcommon.GroupManagementType r3 = r7.groupManagementType
            if (r2 == r3) goto L_0x0048
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r6.groupManagementType
            com.dropbox.core.v2.teamcommon.GroupManagementType r3 = r7.groupManagementType
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0048:
            long r2 = r6.created
            long r4 = r7.created
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x0087
            java.lang.String r2 = r6.groupExternalId
            java.lang.String r3 = r7.groupExternalId
            if (r2 == r3) goto L_0x0064
            java.lang.String r2 = r6.groupExternalId
            if (r2 == 0) goto L_0x0087
            java.lang.String r2 = r6.groupExternalId
            java.lang.String r3 = r7.groupExternalId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0064:
            java.lang.Long r2 = r6.memberCount
            java.lang.Long r3 = r7.memberCount
            if (r2 == r3) goto L_0x0078
            java.lang.Long r2 = r6.memberCount
            if (r2 == 0) goto L_0x0087
            java.lang.Long r2 = r6.memberCount
            java.lang.Long r3 = r7.memberCount
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0087
        L_0x0078:
            java.util.List<com.dropbox.core.v2.team.GroupMemberInfo> r2 = r6.members
            java.util.List<com.dropbox.core.v2.team.GroupMemberInfo> r7 = r7.members
            if (r2 == r7) goto L_0x0088
            if (r2 == 0) goto L_0x0087
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x0087
            goto L_0x0088
        L_0x0087:
            r0 = 0
        L_0x0088:
            return r0
        L_0x0089:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupFullInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
