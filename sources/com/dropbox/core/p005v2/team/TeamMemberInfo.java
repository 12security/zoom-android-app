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

/* renamed from: com.dropbox.core.v2.team.TeamMemberInfo */
public class TeamMemberInfo {
    protected final TeamMemberProfile profile;
    protected final AdminTier role;

    /* renamed from: com.dropbox.core.v2.team.TeamMemberInfo$Serializer */
    static class Serializer extends StructSerializer<TeamMemberInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamMemberInfo teamMemberInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Scopes.PROFILE);
            Serializer.INSTANCE.serialize(teamMemberInfo.profile, jsonGenerator);
            jsonGenerator.writeFieldName("role");
            Serializer.INSTANCE.serialize(teamMemberInfo.role, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamMemberInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TeamMemberProfile teamMemberProfile = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AdminTier adminTier = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Scopes.PROFILE.equals(currentName)) {
                        teamMemberProfile = (TeamMemberProfile) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("role".equals(currentName)) {
                        adminTier = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (teamMemberProfile == null) {
                    throw new JsonParseException(jsonParser, "Required field \"profile\" missing.");
                } else if (adminTier != null) {
                    TeamMemberInfo teamMemberInfo = new TeamMemberInfo(teamMemberProfile, adminTier);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamMemberInfo, teamMemberInfo.toStringMultiline());
                    return teamMemberInfo;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"role\" missing.");
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

    public TeamMemberInfo(TeamMemberProfile teamMemberProfile, AdminTier adminTier) {
        if (teamMemberProfile != null) {
            this.profile = teamMemberProfile;
            if (adminTier != null) {
                this.role = adminTier;
                return;
            }
            throw new IllegalArgumentException("Required value for 'role' is null");
        }
        throw new IllegalArgumentException("Required value for 'profile' is null");
    }

    public TeamMemberProfile getProfile() {
        return this.profile;
    }

    public AdminTier getRole() {
        return this.role;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.profile, this.role});
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
            com.dropbox.core.v2.team.TeamMemberInfo r5 = (com.dropbox.core.p005v2.team.TeamMemberInfo) r5
            com.dropbox.core.v2.team.TeamMemberProfile r2 = r4.profile
            com.dropbox.core.v2.team.TeamMemberProfile r3 = r5.profile
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.team.AdminTier r2 = r4.role
            com.dropbox.core.v2.team.AdminTier r5 = r5.role
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.TeamMemberInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
