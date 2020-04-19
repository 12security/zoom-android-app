package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.GetMembershipReport */
public class GetMembershipReport extends BaseDfbReport {
    protected final List<Long> licenses;
    protected final List<Long> membersJoined;
    protected final List<Long> pendingInvites;
    protected final List<Long> suspendedMembers;
    protected final List<Long> teamSize;

    /* renamed from: com.dropbox.core.v2.team.GetMembershipReport$Serializer */
    static class Serializer extends StructSerializer<GetMembershipReport> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetMembershipReport getMembershipReport, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Param.START_DATE);
            StoneSerializers.string().serialize(getMembershipReport.startDate, jsonGenerator);
            jsonGenerator.writeFieldName("team_size");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getMembershipReport.teamSize, jsonGenerator);
            jsonGenerator.writeFieldName("pending_invites");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getMembershipReport.pendingInvites, jsonGenerator);
            jsonGenerator.writeFieldName("members_joined");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getMembershipReport.membersJoined, jsonGenerator);
            jsonGenerator.writeFieldName("suspended_members");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getMembershipReport.suspendedMembers, jsonGenerator);
            jsonGenerator.writeFieldName("licenses");
            StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).serialize(getMembershipReport.licenses, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetMembershipReport deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                List list = null;
                List list2 = null;
                List list3 = null;
                List list4 = null;
                List list5 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Param.START_DATE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("team_size".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("pending_invites".equals(currentName)) {
                        list2 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("members_joined".equals(currentName)) {
                        list3 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("suspended_members".equals(currentName)) {
                        list4 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else if ("licenses".equals(currentName)) {
                        list5 = (List) StoneSerializers.list(StoneSerializers.nullable(StoneSerializers.uInt64())).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"start_date\" missing.");
                } else if (list == null) {
                    throw new JsonParseException(jsonParser, "Required field \"team_size\" missing.");
                } else if (list2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"pending_invites\" missing.");
                } else if (list3 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"members_joined\" missing.");
                } else if (list4 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"suspended_members\" missing.");
                } else if (list5 != null) {
                    GetMembershipReport getMembershipReport = new GetMembershipReport(str2, list, list2, list3, list4, list5);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getMembershipReport, getMembershipReport.toStringMultiline());
                    return getMembershipReport;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"licenses\" missing.");
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

    public GetMembershipReport(String str, List<Long> list, List<Long> list2, List<Long> list3, List<Long> list4, List<Long> list5) {
        super(str);
        if (list != null) {
            for (Long l : list) {
                if (l == null) {
                    throw new IllegalArgumentException("An item in list 'teamSize' is null");
                }
            }
            this.teamSize = list;
            if (list2 != null) {
                for (Long l2 : list2) {
                    if (l2 == null) {
                        throw new IllegalArgumentException("An item in list 'pendingInvites' is null");
                    }
                }
                this.pendingInvites = list2;
                if (list3 != null) {
                    for (Long l3 : list3) {
                        if (l3 == null) {
                            throw new IllegalArgumentException("An item in list 'membersJoined' is null");
                        }
                    }
                    this.membersJoined = list3;
                    if (list4 != null) {
                        for (Long l4 : list4) {
                            if (l4 == null) {
                                throw new IllegalArgumentException("An item in list 'suspendedMembers' is null");
                            }
                        }
                        this.suspendedMembers = list4;
                        if (list5 != null) {
                            for (Long l5 : list5) {
                                if (l5 == null) {
                                    throw new IllegalArgumentException("An item in list 'licenses' is null");
                                }
                            }
                            this.licenses = list5;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'licenses' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'suspendedMembers' is null");
                }
                throw new IllegalArgumentException("Required value for 'membersJoined' is null");
            }
            throw new IllegalArgumentException("Required value for 'pendingInvites' is null");
        }
        throw new IllegalArgumentException("Required value for 'teamSize' is null");
    }

    public String getStartDate() {
        return this.startDate;
    }

    public List<Long> getTeamSize() {
        return this.teamSize;
    }

    public List<Long> getPendingInvites() {
        return this.pendingInvites;
    }

    public List<Long> getMembersJoined() {
        return this.membersJoined;
    }

    public List<Long> getSuspendedMembers() {
        return this.suspendedMembers;
    }

    public List<Long> getLicenses() {
        return this.licenses;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.teamSize, this.pendingInvites, this.membersJoined, this.suspendedMembers, this.licenses});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0062, code lost:
        if (r2.equals(r5) == false) goto L_0x0065;
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
            if (r2 == 0) goto L_0x0067
            com.dropbox.core.v2.team.GetMembershipReport r5 = (com.dropbox.core.p005v2.team.GetMembershipReport) r5
            java.lang.String r2 = r4.startDate
            java.lang.String r3 = r5.startDate
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.startDate
            java.lang.String r3 = r5.startDate
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x0028:
            java.util.List<java.lang.Long> r2 = r4.teamSize
            java.util.List<java.lang.Long> r3 = r5.teamSize
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x0034:
            java.util.List<java.lang.Long> r2 = r4.pendingInvites
            java.util.List<java.lang.Long> r3 = r5.pendingInvites
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x0040:
            java.util.List<java.lang.Long> r2 = r4.membersJoined
            java.util.List<java.lang.Long> r3 = r5.membersJoined
            if (r2 == r3) goto L_0x004c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x004c:
            java.util.List<java.lang.Long> r2 = r4.suspendedMembers
            java.util.List<java.lang.Long> r3 = r5.suspendedMembers
            if (r2 == r3) goto L_0x0058
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0065
        L_0x0058:
            java.util.List<java.lang.Long> r2 = r4.licenses
            java.util.List<java.lang.Long> r5 = r5.licenses
            if (r2 == r5) goto L_0x0066
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0065
            goto L_0x0066
        L_0x0065:
            r0 = 0
        L_0x0066:
            return r0
        L_0x0067:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GetMembershipReport.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
