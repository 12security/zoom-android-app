package com.dropbox.core.p005v2.teampolicies;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teampolicies.TeamMemberPolicies */
public class TeamMemberPolicies {
    protected final EmmState emmState;
    protected final OfficeAddInPolicy officeAddin;
    protected final TeamSharingPolicies sharing;

    /* renamed from: com.dropbox.core.v2.teampolicies.TeamMemberPolicies$Serializer */
    public static class Serializer extends StructSerializer<TeamMemberPolicies> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(TeamMemberPolicies teamMemberPolicies, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("sharing");
            com.dropbox.core.p005v2.teampolicies.TeamSharingPolicies.Serializer.INSTANCE.serialize(teamMemberPolicies.sharing, jsonGenerator);
            jsonGenerator.writeFieldName("emm_state");
            com.dropbox.core.p005v2.teampolicies.EmmState.Serializer.INSTANCE.serialize(teamMemberPolicies.emmState, jsonGenerator);
            jsonGenerator.writeFieldName("office_addin");
            com.dropbox.core.p005v2.teampolicies.OfficeAddInPolicy.Serializer.INSTANCE.serialize(teamMemberPolicies.officeAddin, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamMemberPolicies deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TeamSharingPolicies teamSharingPolicies = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                EmmState emmState = null;
                OfficeAddInPolicy officeAddInPolicy = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("sharing".equals(currentName)) {
                        teamSharingPolicies = (TeamSharingPolicies) com.dropbox.core.p005v2.teampolicies.TeamSharingPolicies.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("emm_state".equals(currentName)) {
                        emmState = com.dropbox.core.p005v2.teampolicies.EmmState.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("office_addin".equals(currentName)) {
                        officeAddInPolicy = com.dropbox.core.p005v2.teampolicies.OfficeAddInPolicy.Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (teamSharingPolicies == null) {
                    throw new JsonParseException(jsonParser, "Required field \"sharing\" missing.");
                } else if (emmState == null) {
                    throw new JsonParseException(jsonParser, "Required field \"emm_state\" missing.");
                } else if (officeAddInPolicy != null) {
                    TeamMemberPolicies teamMemberPolicies = new TeamMemberPolicies(teamSharingPolicies, emmState, officeAddInPolicy);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamMemberPolicies, teamMemberPolicies.toStringMultiline());
                    return teamMemberPolicies;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"office_addin\" missing.");
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

    public TeamMemberPolicies(TeamSharingPolicies teamSharingPolicies, EmmState emmState2, OfficeAddInPolicy officeAddInPolicy) {
        if (teamSharingPolicies != null) {
            this.sharing = teamSharingPolicies;
            if (emmState2 != null) {
                this.emmState = emmState2;
                if (officeAddInPolicy != null) {
                    this.officeAddin = officeAddInPolicy;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'officeAddin' is null");
            }
            throw new IllegalArgumentException("Required value for 'emmState' is null");
        }
        throw new IllegalArgumentException("Required value for 'sharing' is null");
    }

    public TeamSharingPolicies getSharing() {
        return this.sharing;
    }

    public EmmState getEmmState() {
        return this.emmState;
    }

    public OfficeAddInPolicy getOfficeAddin() {
        return this.officeAddin;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharing, this.emmState, this.officeAddin});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r5) == false) goto L_0x003d;
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
            if (r2 == 0) goto L_0x003f
            com.dropbox.core.v2.teampolicies.TeamMemberPolicies r5 = (com.dropbox.core.p005v2.teampolicies.TeamMemberPolicies) r5
            com.dropbox.core.v2.teampolicies.TeamSharingPolicies r2 = r4.sharing
            com.dropbox.core.v2.teampolicies.TeamSharingPolicies r3 = r5.sharing
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0024:
            com.dropbox.core.v2.teampolicies.EmmState r2 = r4.emmState
            com.dropbox.core.v2.teampolicies.EmmState r3 = r5.emmState
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x0030:
            com.dropbox.core.v2.teampolicies.OfficeAddInPolicy r2 = r4.officeAddin
            com.dropbox.core.v2.teampolicies.OfficeAddInPolicy r5 = r5.officeAddin
            if (r2 == r5) goto L_0x003e
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = 0
        L_0x003e:
            return r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teampolicies.TeamMemberPolicies.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
