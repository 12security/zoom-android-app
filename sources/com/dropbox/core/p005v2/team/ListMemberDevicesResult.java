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

/* renamed from: com.dropbox.core.v2.team.ListMemberDevicesResult */
public class ListMemberDevicesResult {
    protected final List<ActiveWebSession> activeWebSessions;
    protected final List<DesktopClientSession> desktopClientSessions;
    protected final List<MobileClientSession> mobileClientSessions;

    /* renamed from: com.dropbox.core.v2.team.ListMemberDevicesResult$Builder */
    public static class Builder {
        protected List<ActiveWebSession> activeWebSessions = null;
        protected List<DesktopClientSession> desktopClientSessions = null;
        protected List<MobileClientSession> mobileClientSessions = null;

        protected Builder() {
        }

        public Builder withActiveWebSessions(List<ActiveWebSession> list) {
            if (list != null) {
                for (ActiveWebSession activeWebSession : list) {
                    if (activeWebSession == null) {
                        throw new IllegalArgumentException("An item in list 'activeWebSessions' is null");
                    }
                }
            }
            this.activeWebSessions = list;
            return this;
        }

        public Builder withDesktopClientSessions(List<DesktopClientSession> list) {
            if (list != null) {
                for (DesktopClientSession desktopClientSession : list) {
                    if (desktopClientSession == null) {
                        throw new IllegalArgumentException("An item in list 'desktopClientSessions' is null");
                    }
                }
            }
            this.desktopClientSessions = list;
            return this;
        }

        public Builder withMobileClientSessions(List<MobileClientSession> list) {
            if (list != null) {
                for (MobileClientSession mobileClientSession : list) {
                    if (mobileClientSession == null) {
                        throw new IllegalArgumentException("An item in list 'mobileClientSessions' is null");
                    }
                }
            }
            this.mobileClientSessions = list;
            return this;
        }

        public ListMemberDevicesResult build() {
            return new ListMemberDevicesResult(this.activeWebSessions, this.desktopClientSessions, this.mobileClientSessions);
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListMemberDevicesResult$Serializer */
    static class Serializer extends StructSerializer<ListMemberDevicesResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListMemberDevicesResult listMemberDevicesResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (listMemberDevicesResult.activeWebSessions != null) {
                jsonGenerator.writeFieldName("active_web_sessions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(listMemberDevicesResult.activeWebSessions, jsonGenerator);
            }
            if (listMemberDevicesResult.desktopClientSessions != null) {
                jsonGenerator.writeFieldName("desktop_client_sessions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(listMemberDevicesResult.desktopClientSessions, jsonGenerator);
            }
            if (listMemberDevicesResult.mobileClientSessions != null) {
                jsonGenerator.writeFieldName("mobile_client_sessions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(listMemberDevicesResult.mobileClientSessions, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListMemberDevicesResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list2 = null;
                List list3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("active_web_sessions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("desktop_client_sessions".equals(currentName)) {
                        list2 = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("mobile_client_sessions".equals(currentName)) {
                        list3 = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                ListMemberDevicesResult listMemberDevicesResult = new ListMemberDevicesResult(list, list2, list3);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(listMemberDevicesResult, listMemberDevicesResult.toStringMultiline());
                return listMemberDevicesResult;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListMemberDevicesResult(List<ActiveWebSession> list, List<DesktopClientSession> list2, List<MobileClientSession> list3) {
        if (list != null) {
            for (ActiveWebSession activeWebSession : list) {
                if (activeWebSession == null) {
                    throw new IllegalArgumentException("An item in list 'activeWebSessions' is null");
                }
            }
        }
        this.activeWebSessions = list;
        if (list2 != null) {
            for (DesktopClientSession desktopClientSession : list2) {
                if (desktopClientSession == null) {
                    throw new IllegalArgumentException("An item in list 'desktopClientSessions' is null");
                }
            }
        }
        this.desktopClientSessions = list2;
        if (list3 != null) {
            for (MobileClientSession mobileClientSession : list3) {
                if (mobileClientSession == null) {
                    throw new IllegalArgumentException("An item in list 'mobileClientSessions' is null");
                }
            }
        }
        this.mobileClientSessions = list3;
    }

    public ListMemberDevicesResult() {
        this(null, null, null);
    }

    public List<ActiveWebSession> getActiveWebSessions() {
        return this.activeWebSessions;
    }

    public List<DesktopClientSession> getDesktopClientSessions() {
        return this.desktopClientSessions;
    }

    public List<MobileClientSession> getMobileClientSessions() {
        return this.mobileClientSessions;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.activeWebSessions, this.desktopClientSessions, this.mobileClientSessions});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r2.equals(r5) == false) goto L_0x0043;
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
            if (r2 == 0) goto L_0x0045
            com.dropbox.core.v2.team.ListMemberDevicesResult r5 = (com.dropbox.core.p005v2.team.ListMemberDevicesResult) r5
            java.util.List<com.dropbox.core.v2.team.ActiveWebSession> r2 = r4.activeWebSessions
            java.util.List<com.dropbox.core.v2.team.ActiveWebSession> r3 = r5.activeWebSessions
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0026:
            java.util.List<com.dropbox.core.v2.team.DesktopClientSession> r2 = r4.desktopClientSessions
            java.util.List<com.dropbox.core.v2.team.DesktopClientSession> r3 = r5.desktopClientSessions
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0034:
            java.util.List<com.dropbox.core.v2.team.MobileClientSession> r2 = r4.mobileClientSessions
            java.util.List<com.dropbox.core.v2.team.MobileClientSession> r5 = r5.mobileClientSessions
            if (r2 == r5) goto L_0x0044
            if (r2 == 0) goto L_0x0043
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.ListMemberDevicesResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
