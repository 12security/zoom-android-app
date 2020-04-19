package com.dropbox.core.p005v2.users;

import com.dropbox.core.p005v2.teamcommon.MemberSpaceLimitType;
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

/* renamed from: com.dropbox.core.v2.users.TeamSpaceAllocation */
public class TeamSpaceAllocation {
    protected final long allocated;
    protected final long used;
    protected final long userWithinTeamSpaceAllocated;
    protected final MemberSpaceLimitType userWithinTeamSpaceLimitType;

    /* renamed from: com.dropbox.core.v2.users.TeamSpaceAllocation$Serializer */
    static class Serializer extends StructSerializer<TeamSpaceAllocation> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamSpaceAllocation teamSpaceAllocation, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("used");
            StoneSerializers.uInt64().serialize(Long.valueOf(teamSpaceAllocation.used), jsonGenerator);
            jsonGenerator.writeFieldName("allocated");
            StoneSerializers.uInt64().serialize(Long.valueOf(teamSpaceAllocation.allocated), jsonGenerator);
            jsonGenerator.writeFieldName("user_within_team_space_allocated");
            StoneSerializers.uInt64().serialize(Long.valueOf(teamSpaceAllocation.userWithinTeamSpaceAllocated), jsonGenerator);
            jsonGenerator.writeFieldName("user_within_team_space_limit_type");
            com.dropbox.core.p005v2.teamcommon.MemberSpaceLimitType.Serializer.INSTANCE.serialize(teamSpaceAllocation.userWithinTeamSpaceLimitType, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamSpaceAllocation deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l2 = null;
                Long l3 = null;
                MemberSpaceLimitType memberSpaceLimitType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("used".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("allocated".equals(currentName)) {
                        l2 = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("user_within_team_space_allocated".equals(currentName)) {
                        l3 = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("user_within_team_space_limit_type".equals(currentName)) {
                        memberSpaceLimitType = com.dropbox.core.p005v2.teamcommon.MemberSpaceLimitType.Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"used\" missing.");
                } else if (l2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"allocated\" missing.");
                } else if (l3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"user_within_team_space_allocated\" missing.");
                } else if (memberSpaceLimitType != null) {
                    TeamSpaceAllocation teamSpaceAllocation = new TeamSpaceAllocation(l.longValue(), l2.longValue(), l3.longValue(), memberSpaceLimitType);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamSpaceAllocation, teamSpaceAllocation.toStringMultiline());
                    return teamSpaceAllocation;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"user_within_team_space_limit_type\" missing.");
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

    public TeamSpaceAllocation(long j, long j2, long j3, MemberSpaceLimitType memberSpaceLimitType) {
        this.used = j;
        this.allocated = j2;
        this.userWithinTeamSpaceAllocated = j3;
        if (memberSpaceLimitType != null) {
            this.userWithinTeamSpaceLimitType = memberSpaceLimitType;
            return;
        }
        throw new IllegalArgumentException("Required value for 'userWithinTeamSpaceLimitType' is null");
    }

    public long getUsed() {
        return this.used;
    }

    public long getAllocated() {
        return this.allocated;
    }

    public long getUserWithinTeamSpaceAllocated() {
        return this.userWithinTeamSpaceAllocated;
    }

    public MemberSpaceLimitType getUserWithinTeamSpaceLimitType() {
        return this.userWithinTeamSpaceLimitType;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.used), Long.valueOf(this.allocated), Long.valueOf(this.userWithinTeamSpaceAllocated), this.userWithinTeamSpaceLimitType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003a, code lost:
        if (r2.equals(r7) == false) goto L_0x003d;
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
            if (r2 == 0) goto L_0x003f
            com.dropbox.core.v2.users.TeamSpaceAllocation r7 = (com.dropbox.core.p005v2.users.TeamSpaceAllocation) r7
            long r2 = r6.used
            long r4 = r7.used
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x003d
            long r2 = r6.allocated
            long r4 = r7.allocated
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x003d
            long r2 = r6.userWithinTeamSpaceAllocated
            long r4 = r7.userWithinTeamSpaceAllocated
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x003d
            com.dropbox.core.v2.teamcommon.MemberSpaceLimitType r2 = r6.userWithinTeamSpaceLimitType
            com.dropbox.core.v2.teamcommon.MemberSpaceLimitType r7 = r7.userWithinTeamSpaceLimitType
            if (r2 == r7) goto L_0x003e
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = 0
        L_0x003e:
            return r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.users.TeamSpaceAllocation.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
