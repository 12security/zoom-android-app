package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.common.Scopes;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.GroupMemberInfo */
public class GroupMemberInfo {
    protected final GroupAccessType accessType;
    protected final MemberProfile profile;

    /* renamed from: com.dropbox.core.v2.team.GroupMemberInfo$Serializer */
    static class Serializer extends StructSerializer<GroupMemberInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupMemberInfo groupMemberInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Scopes.PROFILE);
            Serializer.INSTANCE.serialize(groupMemberInfo.profile, jsonGenerator);
            jsonGenerator.writeFieldName("access_type");
            Serializer.INSTANCE.serialize(groupMemberInfo.accessType, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupMemberInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            MemberProfile memberProfile = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                GroupAccessType groupAccessType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Scopes.PROFILE.equals(currentName)) {
                        memberProfile = (MemberProfile) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("access_type".equals(currentName)) {
                        groupAccessType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (memberProfile == null) {
                    throw new JsonParseException(jsonParser, "Required field \"profile\" missing.");
                } else if (groupAccessType != null) {
                    GroupMemberInfo groupMemberInfo = new GroupMemberInfo(memberProfile, groupAccessType);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupMemberInfo, groupMemberInfo.toStringMultiline());
                    return groupMemberInfo;
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

    public GroupMemberInfo(MemberProfile memberProfile, GroupAccessType groupAccessType) {
        if (memberProfile != null) {
            this.profile = memberProfile;
            if (groupAccessType != null) {
                this.accessType = groupAccessType;
                return;
            }
            throw new IllegalArgumentException("Required value for 'accessType' is null");
        }
        throw new IllegalArgumentException("Required value for 'profile' is null");
    }

    public MemberProfile getProfile() {
        return this.profile;
    }

    public GroupAccessType getAccessType() {
        return this.accessType;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.profile, this.accessType});
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
            com.dropbox.core.v2.team.GroupMemberInfo r5 = (com.dropbox.core.p005v2.team.GroupMemberInfo) r5
            com.dropbox.core.v2.team.MemberProfile r2 = r4.profile
            com.dropbox.core.v2.team.MemberProfile r3 = r5.profile
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.team.GroupAccessType r2 = r4.accessType
            com.dropbox.core.v2.team.GroupAccessType r5 = r5.accessType
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GroupMemberInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
