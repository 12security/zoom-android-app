package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.TeamProfileChangeNameDetails */
public class TeamProfileChangeNameDetails {
    protected final TeamName newValue;
    protected final TeamName previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.TeamProfileChangeNameDetails$Serializer */
    static class Serializer extends StructSerializer<TeamProfileChangeNameDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamProfileChangeNameDetails teamProfileChangeNameDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(teamProfileChangeNameDetails.newValue, jsonGenerator);
            if (teamProfileChangeNameDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(teamProfileChangeNameDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamProfileChangeNameDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TeamName teamName = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                TeamName teamName2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        teamName = (TeamName) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        teamName2 = (TeamName) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (teamName != null) {
                    TeamProfileChangeNameDetails teamProfileChangeNameDetails = new TeamProfileChangeNameDetails(teamName, teamName2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamProfileChangeNameDetails, teamProfileChangeNameDetails.toStringMultiline());
                    return teamProfileChangeNameDetails;
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

    public TeamProfileChangeNameDetails(TeamName teamName, TeamName teamName2) {
        this.previousValue = teamName2;
        if (teamName != null) {
            this.newValue = teamName;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public TeamProfileChangeNameDetails(TeamName teamName) {
        this(teamName, null);
    }

    public TeamName getNewValue() {
        return this.newValue;
    }

    public TeamName getPreviousValue() {
        return this.previousValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousValue, this.newValue});
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
            com.dropbox.core.v2.teamlog.TeamProfileChangeNameDetails r5 = (com.dropbox.core.p005v2.teamlog.TeamProfileChangeNameDetails) r5
            com.dropbox.core.v2.teamlog.TeamName r2 = r4.newValue
            com.dropbox.core.v2.teamlog.TeamName r3 = r5.newValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.teamlog.TeamName r2 = r4.previousValue
            com.dropbox.core.v2.teamlog.TeamName r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.TeamProfileChangeNameDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
