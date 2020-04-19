package com.dropbox.core.p005v2.teamcommon;

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

/* renamed from: com.dropbox.core.v2.teamcommon.GroupSummary */
public class GroupSummary {
    /* access modifiers changed from: protected */
    public final String groupExternalId;
    /* access modifiers changed from: protected */
    public final String groupId;
    /* access modifiers changed from: protected */
    public final GroupManagementType groupManagementType;
    /* access modifiers changed from: protected */
    public final String groupName;
    /* access modifiers changed from: protected */
    public final Long memberCount;

    /* renamed from: com.dropbox.core.v2.teamcommon.GroupSummary$Builder */
    public static class Builder {
        protected String groupExternalId;
        protected final String groupId;
        protected final GroupManagementType groupManagementType;
        protected final String groupName;
        protected Long memberCount;

        protected Builder(String str, String str2, GroupManagementType groupManagementType2) {
            if (str != null) {
                this.groupName = str;
                if (str2 != null) {
                    this.groupId = str2;
                    if (groupManagementType2 != null) {
                        this.groupManagementType = groupManagementType2;
                        this.groupExternalId = null;
                        this.memberCount = null;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'groupManagementType' is null");
                }
                throw new IllegalArgumentException("Required value for 'groupId' is null");
            }
            throw new IllegalArgumentException("Required value for 'groupName' is null");
        }

        public Builder withGroupExternalId(String str) {
            this.groupExternalId = str;
            return this;
        }

        public Builder withMemberCount(Long l) {
            this.memberCount = l;
            return this;
        }

        public GroupSummary build() {
            GroupSummary groupSummary = new GroupSummary(this.groupName, this.groupId, this.groupManagementType, this.groupExternalId, this.memberCount);
            return groupSummary;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamcommon.GroupSummary$Serializer */
    public static class Serializer extends StructSerializer<GroupSummary> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(GroupSummary groupSummary, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("group_name");
            StoneSerializers.string().serialize(groupSummary.groupName, jsonGenerator);
            jsonGenerator.writeFieldName(Param.GROUP_ID);
            StoneSerializers.string().serialize(groupSummary.groupId, jsonGenerator);
            jsonGenerator.writeFieldName("group_management_type");
            com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE.serialize(groupSummary.groupManagementType, jsonGenerator);
            if (groupSummary.groupExternalId != null) {
                jsonGenerator.writeFieldName("group_external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(groupSummary.groupExternalId, jsonGenerator);
            }
            if (groupSummary.memberCount != null) {
                jsonGenerator.writeFieldName("member_count");
                StoneSerializers.nullable(StoneSerializers.uInt32()).serialize(groupSummary.memberCount, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupSummary deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
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
                } else if (groupManagementType != null) {
                    GroupSummary groupSummary = new GroupSummary(str2, str3, groupManagementType, str4, l);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupSummary, groupSummary.toStringMultiline());
                    return groupSummary;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"group_management_type\" missing.");
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

    public GroupSummary(String str, String str2, GroupManagementType groupManagementType2, String str3, Long l) {
        if (str != null) {
            this.groupName = str;
            if (str2 != null) {
                this.groupId = str2;
                this.groupExternalId = str3;
                this.memberCount = l;
                if (groupManagementType2 != null) {
                    this.groupManagementType = groupManagementType2;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'groupManagementType' is null");
            }
            throw new IllegalArgumentException("Required value for 'groupId' is null");
        }
        throw new IllegalArgumentException("Required value for 'groupName' is null");
    }

    public GroupSummary(String str, String str2, GroupManagementType groupManagementType2) {
        this(str, str2, groupManagementType2, null, null);
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

    public String getGroupExternalId() {
        return this.groupExternalId;
    }

    public Long getMemberCount() {
        return this.memberCount;
    }

    public static Builder newBuilder(String str, String str2, GroupManagementType groupManagementType2) {
        return new Builder(str, str2, groupManagementType2);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.groupName, this.groupId, this.groupExternalId, this.memberCount, this.groupManagementType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0056, code lost:
        if (r2.equals(r5) == false) goto L_0x0059;
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
            if (r2 == 0) goto L_0x005b
            com.dropbox.core.v2.teamcommon.GroupSummary r5 = (com.dropbox.core.p005v2.teamcommon.GroupSummary) r5
            java.lang.String r2 = r4.groupName
            java.lang.String r3 = r5.groupName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0059
        L_0x0024:
            java.lang.String r2 = r4.groupId
            java.lang.String r3 = r5.groupId
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0059
        L_0x0030:
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r4.groupManagementType
            com.dropbox.core.v2.teamcommon.GroupManagementType r3 = r5.groupManagementType
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0059
        L_0x003c:
            java.lang.String r2 = r4.groupExternalId
            java.lang.String r3 = r5.groupExternalId
            if (r2 == r3) goto L_0x004a
            if (r2 == 0) goto L_0x0059
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0059
        L_0x004a:
            java.lang.Long r2 = r4.memberCount
            java.lang.Long r5 = r5.memberCount
            if (r2 == r5) goto L_0x005a
            if (r2 == 0) goto L_0x0059
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0059
            goto L_0x005a
        L_0x0059:
            r0 = 0
        L_0x005a:
            return r0
        L_0x005b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamcommon.GroupSummary.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
