package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.p005v2.teamcommon.GroupManagementType;
import com.dropbox.core.p005v2.teamcommon.GroupSummary;
import com.dropbox.core.p005v2.teamcommon.GroupType;
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

/* renamed from: com.dropbox.core.v2.sharing.GroupInfo */
public class GroupInfo extends GroupSummary {
    protected final GroupType groupType;
    protected final boolean isMember;
    protected final boolean isOwner;
    protected final boolean sameTeam;

    /* renamed from: com.dropbox.core.v2.sharing.GroupInfo$Builder */
    public static class Builder extends com.dropbox.core.p005v2.teamcommon.GroupSummary.Builder {
        protected final GroupType groupType;
        protected final boolean isMember;
        protected final boolean isOwner;
        protected final boolean sameTeam;

        protected Builder(String str, String str2, GroupManagementType groupManagementType, GroupType groupType2, boolean z, boolean z2, boolean z3) {
            super(str, str2, groupManagementType);
            if (groupType2 != null) {
                this.groupType = groupType2;
                this.isMember = z;
                this.isOwner = z2;
                this.sameTeam = z3;
                return;
            }
            throw new IllegalArgumentException("Required value for 'groupType' is null");
        }

        public Builder withGroupExternalId(String str) {
            super.withGroupExternalId(str);
            return this;
        }

        public Builder withMemberCount(Long l) {
            super.withMemberCount(l);
            return this;
        }

        public GroupInfo build() {
            GroupInfo groupInfo = new GroupInfo(this.groupName, this.groupId, this.groupManagementType, this.groupType, this.isMember, this.isOwner, this.sameTeam, this.groupExternalId, this.memberCount);
            return groupInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.GroupInfo$Serializer */
    static class Serializer extends StructSerializer<GroupInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupInfo groupInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("group_name");
            StoneSerializers.string().serialize(groupInfo.groupName, jsonGenerator);
            jsonGenerator.writeFieldName(Param.GROUP_ID);
            StoneSerializers.string().serialize(groupInfo.groupId, jsonGenerator);
            jsonGenerator.writeFieldName("group_management_type");
            com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE.serialize(groupInfo.groupManagementType, jsonGenerator);
            jsonGenerator.writeFieldName("group_type");
            com.dropbox.core.p005v2.teamcommon.GroupType.Serializer.INSTANCE.serialize(groupInfo.groupType, jsonGenerator);
            jsonGenerator.writeFieldName("is_member");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(groupInfo.isMember), jsonGenerator);
            jsonGenerator.writeFieldName("is_owner");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(groupInfo.isOwner), jsonGenerator);
            jsonGenerator.writeFieldName("same_team");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(groupInfo.sameTeam), jsonGenerator);
            if (groupInfo.groupExternalId != null) {
                jsonGenerator.writeFieldName("group_external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(groupInfo.groupExternalId, jsonGenerator);
            }
            if (groupInfo.memberCount != null) {
                jsonGenerator.writeFieldName("member_count");
                StoneSerializers.nullable(StoneSerializers.uInt32()).serialize(groupInfo.memberCount, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean bool2 = null;
                Boolean bool3 = null;
                String str2 = null;
                String str3 = null;
                GroupManagementType groupManagementType = null;
                GroupType groupType = null;
                String str4 = null;
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("group_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (Param.GROUP_ID.equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("group_management_type".equals(currentName)) {
                        groupManagementType = com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("group_type".equals(currentName)) {
                        groupType = com.dropbox.core.p005v2.teamcommon.GroupType.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("is_member".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("is_owner".equals(currentName)) {
                        bool2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("same_team".equals(currentName)) {
                        bool3 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("group_external_id".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("member_count".equals(currentName)) {
                        l = (Long) StoneSerializers.nullable(StoneSerializers.uInt32()).deserialize(jsonParser);
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
                } else if (groupType == null) {
                    throw new JsonParseException(jsonParser, "Required field \"group_type\" missing.");
                } else if (bool == null) {
                    throw new JsonParseException(jsonParser, "Required field \"is_member\" missing.");
                } else if (bool2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"is_owner\" missing.");
                } else if (bool3 != null) {
                    GroupInfo groupInfo = new GroupInfo(str2, str3, groupManagementType, groupType, bool.booleanValue(), bool2.booleanValue(), bool3.booleanValue(), str4, l);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupInfo, groupInfo.toStringMultiline());
                    return groupInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"same_team\" missing.");
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

    public GroupInfo(String str, String str2, GroupManagementType groupManagementType, GroupType groupType2, boolean z, boolean z2, boolean z3, String str3, Long l) {
        super(str, str2, groupManagementType, str3, l);
        if (groupType2 != null) {
            this.groupType = groupType2;
            this.isMember = z;
            this.isOwner = z2;
            this.sameTeam = z3;
            return;
        }
        throw new IllegalArgumentException("Required value for 'groupType' is null");
    }

    public GroupInfo(String str, String str2, GroupManagementType groupManagementType, GroupType groupType2, boolean z, boolean z2, boolean z3) {
        this(str, str2, groupManagementType, groupType2, z, z2, z3, null, null);
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

    public GroupType getGroupType() {
        return this.groupType;
    }

    public boolean getIsMember() {
        return this.isMember;
    }

    public boolean getIsOwner() {
        return this.isOwner;
    }

    public boolean getSameTeam() {
        return this.sameTeam;
    }

    public String getGroupExternalId() {
        return this.groupExternalId;
    }

    public Long getMemberCount() {
        return this.memberCount;
    }

    public static Builder newBuilder(String str, String str2, GroupManagementType groupManagementType, GroupType groupType2, boolean z, boolean z2, boolean z3) {
        Builder builder = new Builder(str, str2, groupManagementType, groupType2, z, z2, z3);
        return builder;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.groupType, Boolean.valueOf(this.isMember), Boolean.valueOf(this.isOwner), Boolean.valueOf(this.sameTeam)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0052, code lost:
        if (r2.equals(r3) == false) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0078, code lost:
        if (r4.groupExternalId.equals(r5.groupExternalId) == false) goto L_0x008f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x008c, code lost:
        if (r4.memberCount.equals(r5.memberCount) == false) goto L_0x008f;
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
            if (r2 == 0) goto L_0x0091
            com.dropbox.core.v2.sharing.GroupInfo r5 = (com.dropbox.core.p005v2.sharing.GroupInfo) r5
            java.lang.String r2 = r4.groupName
            java.lang.String r3 = r5.groupName
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.groupName
            java.lang.String r3 = r5.groupName
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x008f
        L_0x0028:
            java.lang.String r2 = r4.groupId
            java.lang.String r3 = r5.groupId
            if (r2 == r3) goto L_0x0038
            java.lang.String r2 = r4.groupId
            java.lang.String r3 = r5.groupId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x008f
        L_0x0038:
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r4.groupManagementType
            com.dropbox.core.v2.teamcommon.GroupManagementType r3 = r5.groupManagementType
            if (r2 == r3) goto L_0x0048
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r4.groupManagementType
            com.dropbox.core.v2.teamcommon.GroupManagementType r3 = r5.groupManagementType
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x008f
        L_0x0048:
            com.dropbox.core.v2.teamcommon.GroupType r2 = r4.groupType
            com.dropbox.core.v2.teamcommon.GroupType r3 = r5.groupType
            if (r2 == r3) goto L_0x0054
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x008f
        L_0x0054:
            boolean r2 = r4.isMember
            boolean r3 = r5.isMember
            if (r2 != r3) goto L_0x008f
            boolean r2 = r4.isOwner
            boolean r3 = r5.isOwner
            if (r2 != r3) goto L_0x008f
            boolean r2 = r4.sameTeam
            boolean r3 = r5.sameTeam
            if (r2 != r3) goto L_0x008f
            java.lang.String r2 = r4.groupExternalId
            java.lang.String r3 = r5.groupExternalId
            if (r2 == r3) goto L_0x007a
            java.lang.String r2 = r4.groupExternalId
            if (r2 == 0) goto L_0x008f
            java.lang.String r2 = r4.groupExternalId
            java.lang.String r3 = r5.groupExternalId
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x008f
        L_0x007a:
            java.lang.Long r2 = r4.memberCount
            java.lang.Long r3 = r5.memberCount
            if (r2 == r3) goto L_0x0090
            java.lang.Long r2 = r4.memberCount
            if (r2 == 0) goto L_0x008f
            java.lang.Long r2 = r4.memberCount
            java.lang.Long r5 = r5.memberCount
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x008f
            goto L_0x0090
        L_0x008f:
            r0 = 0
        L_0x0090:
            return r0
        L_0x0091:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.GroupInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
