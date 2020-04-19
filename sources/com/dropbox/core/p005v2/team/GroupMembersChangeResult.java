package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.GroupMembersChangeResult */
public class GroupMembersChangeResult {
    protected final String asyncJobId;
    protected final GroupFullInfo groupInfo;

    /* renamed from: com.dropbox.core.v2.team.GroupMembersChangeResult$Serializer */
    static class Serializer extends StructSerializer<GroupMembersChangeResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupMembersChangeResult groupMembersChangeResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("group_info");
            Serializer.INSTANCE.serialize(groupMembersChangeResult.groupInfo, jsonGenerator);
            jsonGenerator.writeFieldName("async_job_id");
            StoneSerializers.string().serialize(groupMembersChangeResult.asyncJobId, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupMembersChangeResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            GroupFullInfo groupFullInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("group_info".equals(currentName)) {
                        groupFullInfo = (GroupFullInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("async_job_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (groupFullInfo == null) {
                    throw new JsonParseException(jsonParser, "Required field \"group_info\" missing.");
                } else if (str2 != null) {
                    GroupMembersChangeResult groupMembersChangeResult = new GroupMembersChangeResult(groupFullInfo, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupMembersChangeResult, groupMembersChangeResult.toStringMultiline());
                    return groupMembersChangeResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"async_job_id\" missing.");
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

    public GroupMembersChangeResult(GroupFullInfo groupFullInfo, String str) {
        if (groupFullInfo != null) {
            this.groupInfo = groupFullInfo;
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'asyncJobId' is null");
            } else if (str.length() >= 1) {
                this.asyncJobId = str;
            } else {
                throw new IllegalArgumentException("String 'asyncJobId' is shorter than 1");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'groupInfo' is null");
        }
    }

    public GroupFullInfo getGroupInfo() {
        return this.groupInfo;
    }

    public String getAsyncJobId() {
        return this.asyncJobId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.groupInfo, this.asyncJobId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.team.GroupMembersChangeResult r5 = (com.dropbox.core.p005v2.team.GroupMembersChangeResult) r5
            com.dropbox.core.v2.team.GroupFullInfo r2 = r4.groupInfo
            com.dropbox.core.v2.team.GroupFullInfo r3 = r5.groupInfo
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            java.lang.String r2 = r4.asyncJobId
            java.lang.String r5 = r5.asyncJobId
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupMembersChangeResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
