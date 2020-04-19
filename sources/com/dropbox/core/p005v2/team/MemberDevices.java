package com.dropbox.core.p005v2.team;

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
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.MemberDevices */
public class MemberDevices {
    protected final List<DesktopClientSession> desktopClients;
    protected final List<MobileClientSession> mobileClients;
    protected final String teamMemberId;
    protected final List<ActiveWebSession> webSessions;

    /* renamed from: com.dropbox.core.v2.team.MemberDevices$Builder */
    public static class Builder {
        protected List<DesktopClientSession> desktopClients;
        protected List<MobileClientSession> mobileClients;
        protected final String teamMemberId;
        protected List<ActiveWebSession> webSessions;

        protected Builder(String str) {
            if (str != null) {
                this.teamMemberId = str;
                this.webSessions = null;
                this.desktopClients = null;
                this.mobileClients = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'teamMemberId' is null");
        }

        public Builder withWebSessions(List<ActiveWebSession> list) {
            if (list != null) {
                for (ActiveWebSession activeWebSession : list) {
                    if (activeWebSession == null) {
                        throw new IllegalArgumentException("An item in list 'webSessions' is null");
                    }
                }
            }
            this.webSessions = list;
            return this;
        }

        public Builder withDesktopClients(List<DesktopClientSession> list) {
            if (list != null) {
                for (DesktopClientSession desktopClientSession : list) {
                    if (desktopClientSession == null) {
                        throw new IllegalArgumentException("An item in list 'desktopClients' is null");
                    }
                }
            }
            this.desktopClients = list;
            return this;
        }

        public Builder withMobileClients(List<MobileClientSession> list) {
            if (list != null) {
                for (MobileClientSession mobileClientSession : list) {
                    if (mobileClientSession == null) {
                        throw new IllegalArgumentException("An item in list 'mobileClients' is null");
                    }
                }
            }
            this.mobileClients = list;
            return this;
        }

        public MemberDevices build() {
            return new MemberDevices(this.teamMemberId, this.webSessions, this.desktopClients, this.mobileClients);
        }
    }

    /* renamed from: com.dropbox.core.v2.team.MemberDevices$Serializer */
    static class Serializer extends StructSerializer<MemberDevices> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberDevices memberDevices, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_member_id");
            StoneSerializers.string().serialize(memberDevices.teamMemberId, jsonGenerator);
            if (memberDevices.webSessions != null) {
                jsonGenerator.writeFieldName("web_sessions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(memberDevices.webSessions, jsonGenerator);
            }
            if (memberDevices.desktopClients != null) {
                jsonGenerator.writeFieldName("desktop_clients");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(memberDevices.desktopClients, jsonGenerator);
            }
            if (memberDevices.mobileClients != null) {
                jsonGenerator.writeFieldName("mobile_clients");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(memberDevices.mobileClients, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberDevices deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                List list2 = null;
                List list3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_member_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("web_sessions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("desktop_clients".equals(currentName)) {
                        list2 = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("mobile_clients".equals(currentName)) {
                        list3 = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    MemberDevices memberDevices = new MemberDevices(str2, list, list2, list3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberDevices, memberDevices.toStringMultiline());
                    return memberDevices;
                }
                throw new JsonParseException(jsonParser, "Required field \"team_member_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MemberDevices(String str, List<ActiveWebSession> list, List<DesktopClientSession> list2, List<MobileClientSession> list3) {
        if (str != null) {
            this.teamMemberId = str;
            if (list != null) {
                for (ActiveWebSession activeWebSession : list) {
                    if (activeWebSession == null) {
                        throw new IllegalArgumentException("An item in list 'webSessions' is null");
                    }
                }
            }
            this.webSessions = list;
            if (list2 != null) {
                for (DesktopClientSession desktopClientSession : list2) {
                    if (desktopClientSession == null) {
                        throw new IllegalArgumentException("An item in list 'desktopClients' is null");
                    }
                }
            }
            this.desktopClients = list2;
            if (list3 != null) {
                for (MobileClientSession mobileClientSession : list3) {
                    if (mobileClientSession == null) {
                        throw new IllegalArgumentException("An item in list 'mobileClients' is null");
                    }
                }
            }
            this.mobileClients = list3;
            return;
        }
        throw new IllegalArgumentException("Required value for 'teamMemberId' is null");
    }

    public MemberDevices(String str) {
        this(str, null, null, null);
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public List<ActiveWebSession> getWebSessions() {
        return this.webSessions;
    }

    public List<DesktopClientSession> getDesktopClients() {
        return this.desktopClients;
    }

    public List<MobileClientSession> getMobileClients() {
        return this.mobileClients;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.teamMemberId, this.webSessions, this.desktopClients, this.mobileClients});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004c, code lost:
        if (r2.equals(r5) == false) goto L_0x004f;
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
            if (r2 == 0) goto L_0x0051
            com.dropbox.core.v2.team.MemberDevices r5 = (com.dropbox.core.p005v2.team.MemberDevices) r5
            java.lang.String r2 = r4.teamMemberId
            java.lang.String r3 = r5.teamMemberId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0024:
            java.util.List<com.dropbox.core.v2.team.ActiveWebSession> r2 = r4.webSessions
            java.util.List<com.dropbox.core.v2.team.ActiveWebSession> r3 = r5.webSessions
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x004f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0032:
            java.util.List<com.dropbox.core.v2.team.DesktopClientSession> r2 = r4.desktopClients
            java.util.List<com.dropbox.core.v2.team.DesktopClientSession> r3 = r5.desktopClients
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x004f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0040:
            java.util.List<com.dropbox.core.v2.team.MobileClientSession> r2 = r4.mobileClients
            java.util.List<com.dropbox.core.v2.team.MobileClientSession> r5 = r5.mobileClients
            if (r2 == r5) goto L_0x0050
            if (r2 == 0) goto L_0x004f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x004f
            goto L_0x0050
        L_0x004f:
            r0 = 0
        L_0x0050:
            return r0
        L_0x0051:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.MemberDevices.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
