package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.p005v2.users.Team;
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

/* renamed from: com.dropbox.core.v2.sharing.TeamMemberInfo */
public class TeamMemberInfo {
    protected final String displayName;
    protected final String memberId;
    protected final Team teamInfo;

    /* renamed from: com.dropbox.core.v2.sharing.TeamMemberInfo$Serializer */
    static class Serializer extends StructSerializer<TeamMemberInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamMemberInfo teamMemberInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_info");
            com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE.serialize(teamMemberInfo.teamInfo, jsonGenerator);
            jsonGenerator.writeFieldName("display_name");
            StoneSerializers.string().serialize(teamMemberInfo.displayName, jsonGenerator);
            if (teamMemberInfo.memberId != null) {
                jsonGenerator.writeFieldName("member_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(teamMemberInfo.memberId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamMemberInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Team team = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_info".equals(currentName)) {
                        team = (Team) com.dropbox.core.p005v2.users.Team.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("member_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (team == null) {
                    throw new JsonParseException(jsonParser, "Required field \"team_info\" missing.");
                } else if (str2 != null) {
                    TeamMemberInfo teamMemberInfo = new TeamMemberInfo(team, str2, str3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamMemberInfo, teamMemberInfo.toStringMultiline());
                    return teamMemberInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"display_name\" missing.");
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

    public TeamMemberInfo(Team team, String str, String str2) {
        if (team != null) {
            this.teamInfo = team;
            if (str != null) {
                this.displayName = str;
                this.memberId = str2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'displayName' is null");
        }
        throw new IllegalArgumentException("Required value for 'teamInfo' is null");
    }

    public TeamMemberInfo(Team team, String str) {
        this(team, str, null);
    }

    public Team getTeamInfo() {
        return this.teamInfo;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.teamInfo, this.displayName, this.memberId});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (r2.equals(r5) == false) goto L_0x003f;
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
            if (r2 == 0) goto L_0x0041
            com.dropbox.core.v2.sharing.TeamMemberInfo r5 = (com.dropbox.core.p005v2.sharing.TeamMemberInfo) r5
            com.dropbox.core.v2.users.Team r2 = r4.teamInfo
            com.dropbox.core.v2.users.Team r3 = r5.teamInfo
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0024:
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0030:
            java.lang.String r2 = r4.memberId
            java.lang.String r5 = r5.memberId
            if (r2 == r5) goto L_0x0040
            if (r2 == 0) goto L_0x003f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r0 = 0
        L_0x0040:
            return r0
        L_0x0041:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.TeamMemberInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
