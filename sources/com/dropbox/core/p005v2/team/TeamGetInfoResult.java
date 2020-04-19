package com.dropbox.core.p005v2.team;

import com.dropbox.core.p005v2.teampolicies.TeamMemberPolicies;
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

/* renamed from: com.dropbox.core.v2.team.TeamGetInfoResult */
public class TeamGetInfoResult {
    protected final String name;
    protected final long numLicensedUsers;
    protected final long numProvisionedUsers;
    protected final TeamMemberPolicies policies;
    protected final String teamId;

    /* renamed from: com.dropbox.core.v2.team.TeamGetInfoResult$Serializer */
    static class Serializer extends StructSerializer<TeamGetInfoResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamGetInfoResult teamGetInfoResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("name");
            StoneSerializers.string().serialize(teamGetInfoResult.name, jsonGenerator);
            jsonGenerator.writeFieldName("team_id");
            StoneSerializers.string().serialize(teamGetInfoResult.teamId, jsonGenerator);
            jsonGenerator.writeFieldName("num_licensed_users");
            StoneSerializers.uInt32().serialize(Long.valueOf(teamGetInfoResult.numLicensedUsers), jsonGenerator);
            jsonGenerator.writeFieldName("num_provisioned_users");
            StoneSerializers.uInt32().serialize(Long.valueOf(teamGetInfoResult.numProvisionedUsers), jsonGenerator);
            jsonGenerator.writeFieldName("policies");
            com.dropbox.core.p005v2.teampolicies.TeamMemberPolicies.Serializer.INSTANCE.serialize(teamGetInfoResult.policies, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamGetInfoResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str2 = null;
                String str3 = null;
                TeamMemberPolicies teamMemberPolicies = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("team_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("num_licensed_users".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else if ("num_provisioned_users".equals(currentName)) {
                        l2 = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else if ("policies".equals(currentName)) {
                        teamMemberPolicies = (TeamMemberPolicies) com.dropbox.core.p005v2.teampolicies.TeamMemberPolicies.Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"name\" missing.");
                } else if (str3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"team_id\" missing.");
                } else if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"num_licensed_users\" missing.");
                } else if (l2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"num_provisioned_users\" missing.");
                } else if (teamMemberPolicies != null) {
                    TeamGetInfoResult teamGetInfoResult = new TeamGetInfoResult(str2, str3, l.longValue(), l2.longValue(), teamMemberPolicies);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamGetInfoResult, teamGetInfoResult.toStringMultiline());
                    return teamGetInfoResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"policies\" missing.");
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

    public TeamGetInfoResult(String str, String str2, long j, long j2, TeamMemberPolicies teamMemberPolicies) {
        if (str != null) {
            this.name = str;
            if (str2 != null) {
                this.teamId = str2;
                this.numLicensedUsers = j;
                this.numProvisionedUsers = j2;
                if (teamMemberPolicies != null) {
                    this.policies = teamMemberPolicies;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'policies' is null");
            }
            throw new IllegalArgumentException("Required value for 'teamId' is null");
        }
        throw new IllegalArgumentException("Required value for 'name' is null");
    }

    public String getName() {
        return this.name;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public long getNumLicensedUsers() {
        return this.numLicensedUsers;
    }

    public long getNumProvisionedUsers() {
        return this.numProvisionedUsers;
    }

    public TeamMemberPolicies getPolicies() {
        return this.policies;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.name, this.teamId, Long.valueOf(this.numLicensedUsers), Long.valueOf(this.numProvisionedUsers), this.policies});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
        if (r2.equals(r7) == false) goto L_0x004d;
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
            if (r2 == 0) goto L_0x004f
            com.dropbox.core.v2.team.TeamGetInfoResult r7 = (com.dropbox.core.p005v2.team.TeamGetInfoResult) r7
            java.lang.String r2 = r6.name
            java.lang.String r3 = r7.name
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004d
        L_0x0024:
            java.lang.String r2 = r6.teamId
            java.lang.String r3 = r7.teamId
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004d
        L_0x0030:
            long r2 = r6.numLicensedUsers
            long r4 = r7.numLicensedUsers
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x004d
            long r2 = r6.numProvisionedUsers
            long r4 = r7.numProvisionedUsers
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x004d
            com.dropbox.core.v2.teampolicies.TeamMemberPolicies r2 = r6.policies
            com.dropbox.core.v2.teampolicies.TeamMemberPolicies r7 = r7.policies
            if (r2 == r7) goto L_0x004e
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x004d
            goto L_0x004e
        L_0x004d:
            r0 = 0
        L_0x004e:
            return r0
        L_0x004f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.TeamGetInfoResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
