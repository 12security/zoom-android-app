package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.p005v2.team.TeamFolderStatus;
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

/* renamed from: com.dropbox.core.v2.teamlog.TeamFolderChangeStatusDetails */
public class TeamFolderChangeStatusDetails {
    protected final TeamFolderStatus newValue;
    protected final TeamFolderStatus previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.TeamFolderChangeStatusDetails$Serializer */
    static class Serializer extends StructSerializer<TeamFolderChangeStatusDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamFolderChangeStatusDetails teamFolderChangeStatusDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            com.dropbox.core.p005v2.team.TeamFolderStatus.Serializer.INSTANCE.serialize(teamFolderChangeStatusDetails.newValue, jsonGenerator);
            if (teamFolderChangeStatusDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(com.dropbox.core.p005v2.team.TeamFolderStatus.Serializer.INSTANCE).serialize(teamFolderChangeStatusDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamFolderChangeStatusDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TeamFolderStatus teamFolderStatus = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                TeamFolderStatus teamFolderStatus2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        teamFolderStatus = com.dropbox.core.p005v2.team.TeamFolderStatus.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        teamFolderStatus2 = (TeamFolderStatus) StoneSerializers.nullable(com.dropbox.core.p005v2.team.TeamFolderStatus.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (teamFolderStatus != null) {
                    TeamFolderChangeStatusDetails teamFolderChangeStatusDetails = new TeamFolderChangeStatusDetails(teamFolderStatus, teamFolderStatus2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamFolderChangeStatusDetails, teamFolderChangeStatusDetails.toStringMultiline());
                    return teamFolderChangeStatusDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public TeamFolderChangeStatusDetails(TeamFolderStatus teamFolderStatus, TeamFolderStatus teamFolderStatus2) {
        if (teamFolderStatus != null) {
            this.newValue = teamFolderStatus;
            this.previousValue = teamFolderStatus2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public TeamFolderChangeStatusDetails(TeamFolderStatus teamFolderStatus) {
        this(teamFolderStatus, null);
    }

    public TeamFolderStatus getNewValue() {
        return this.newValue;
    }

    public TeamFolderStatus getPreviousValue() {
        return this.previousValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newValue, this.previousValue});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.teamlog.TeamFolderChangeStatusDetails r5 = (com.dropbox.core.p005v2.teamlog.TeamFolderChangeStatusDetails) r5
            com.dropbox.core.v2.team.TeamFolderStatus r2 = r4.newValue
            com.dropbox.core.v2.team.TeamFolderStatus r3 = r5.newValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.team.TeamFolderStatus r2 = r4.previousValue
            com.dropbox.core.v2.team.TeamFolderStatus r5 = r5.previousValue
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.TeamFolderChangeStatusDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
