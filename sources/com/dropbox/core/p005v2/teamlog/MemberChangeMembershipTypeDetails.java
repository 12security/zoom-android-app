package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.MemberChangeMembershipTypeDetails */
public class MemberChangeMembershipTypeDetails {
    protected final TeamMembershipType newValue;
    protected final TeamMembershipType prevValue;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberChangeMembershipTypeDetails$Serializer */
    static class Serializer extends StructSerializer<MemberChangeMembershipTypeDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberChangeMembershipTypeDetails memberChangeMembershipTypeDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("prev_value");
            Serializer.INSTANCE.serialize(memberChangeMembershipTypeDetails.prevValue, jsonGenerator);
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(memberChangeMembershipTypeDetails.newValue, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberChangeMembershipTypeDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            TeamMembershipType teamMembershipType = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                TeamMembershipType teamMembershipType2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("prev_value".equals(currentName)) {
                        teamMembershipType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("new_value".equals(currentName)) {
                        teamMembershipType2 = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (teamMembershipType == null) {
                    throw new JsonParseException(jsonParser, "Required field \"prev_value\" missing.");
                } else if (teamMembershipType2 != null) {
                    MemberChangeMembershipTypeDetails memberChangeMembershipTypeDetails = new MemberChangeMembershipTypeDetails(teamMembershipType, teamMembershipType2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberChangeMembershipTypeDetails, memberChangeMembershipTypeDetails.toStringMultiline());
                    return memberChangeMembershipTypeDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
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

    public MemberChangeMembershipTypeDetails(TeamMembershipType teamMembershipType, TeamMembershipType teamMembershipType2) {
        if (teamMembershipType != null) {
            this.prevValue = teamMembershipType;
            if (teamMembershipType2 != null) {
                this.newValue = teamMembershipType2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'newValue' is null");
        }
        throw new IllegalArgumentException("Required value for 'prevValue' is null");
    }

    public TeamMembershipType getPrevValue() {
        return this.prevValue;
    }

    public TeamMembershipType getNewValue() {
        return this.newValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.prevValue, this.newValue});
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
            com.dropbox.core.v2.teamlog.MemberChangeMembershipTypeDetails r5 = (com.dropbox.core.p005v2.teamlog.MemberChangeMembershipTypeDetails) r5
            com.dropbox.core.v2.teamlog.TeamMembershipType r2 = r4.prevValue
            com.dropbox.core.v2.teamlog.TeamMembershipType r3 = r5.prevValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.teamlog.TeamMembershipType r2 = r4.newValue
            com.dropbox.core.v2.teamlog.TeamMembershipType r5 = r5.newValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.MemberChangeMembershipTypeDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
