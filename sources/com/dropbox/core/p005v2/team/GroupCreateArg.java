package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.GroupCreateArg */
class GroupCreateArg {
    protected final String groupExternalId;
    protected final GroupManagementType groupManagementType;
    protected final String groupName;

    /* renamed from: com.dropbox.core.v2.team.GroupCreateArg$Builder */
    public static class Builder {
        protected String groupExternalId;
        protected GroupManagementType groupManagementType;
        protected final String groupName;

        protected Builder(String str) {
            if (str != null) {
                this.groupName = str;
                this.groupExternalId = null;
                this.groupManagementType = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'groupName' is null");
        }

        public Builder withGroupExternalId(String str) {
            this.groupExternalId = str;
            return this;
        }

        public Builder withGroupManagementType(GroupManagementType groupManagementType2) {
            this.groupManagementType = groupManagementType2;
            return this;
        }

        public GroupCreateArg build() {
            return new GroupCreateArg(this.groupName, this.groupExternalId, this.groupManagementType);
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupCreateArg$Serializer */
    static class Serializer extends StructSerializer<GroupCreateArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupCreateArg groupCreateArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("group_name");
            StoneSerializers.string().serialize(groupCreateArg.groupName, jsonGenerator);
            if (groupCreateArg.groupExternalId != null) {
                jsonGenerator.writeFieldName("group_external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(groupCreateArg.groupExternalId, jsonGenerator);
            }
            if (groupCreateArg.groupManagementType != null) {
                jsonGenerator.writeFieldName("group_management_type");
                StoneSerializers.nullable(com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE).serialize(groupCreateArg.groupManagementType, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupCreateArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                GroupManagementType groupManagementType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("group_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("group_external_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("group_management_type".equals(currentName)) {
                        groupManagementType = (GroupManagementType) StoneSerializers.nullable(com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    GroupCreateArg groupCreateArg = new GroupCreateArg(str2, str3, groupManagementType);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupCreateArg, groupCreateArg.toStringMultiline());
                    return groupCreateArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"group_name\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GroupCreateArg(String str, String str2, GroupManagementType groupManagementType2) {
        if (str != null) {
            this.groupName = str;
            this.groupExternalId = str2;
            this.groupManagementType = groupManagementType2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'groupName' is null");
    }

    public GroupCreateArg(String str) {
        this(str, null, null);
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getGroupExternalId() {
        return this.groupExternalId;
    }

    public GroupManagementType getGroupManagementType() {
        return this.groupManagementType;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.groupName, this.groupExternalId, this.groupManagementType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2.equals(r5) == false) goto L_0x0041;
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
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.team.GroupCreateArg r5 = (com.dropbox.core.p005v2.team.GroupCreateArg) r5
            java.lang.String r2 = r4.groupName
            java.lang.String r3 = r5.groupName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            java.lang.String r2 = r4.groupExternalId
            java.lang.String r3 = r5.groupExternalId
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r4.groupManagementType
            com.dropbox.core.v2.teamcommon.GroupManagementType r5 = r5.groupManagementType
            if (r2 == r5) goto L_0x0042
            if (r2 == 0) goto L_0x0041
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupCreateArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
