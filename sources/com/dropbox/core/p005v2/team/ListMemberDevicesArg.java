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

/* renamed from: com.dropbox.core.v2.team.ListMemberDevicesArg */
class ListMemberDevicesArg {
    protected final boolean includeDesktopClients;
    protected final boolean includeMobileClients;
    protected final boolean includeWebSessions;
    protected final String teamMemberId;

    /* renamed from: com.dropbox.core.v2.team.ListMemberDevicesArg$Builder */
    public static class Builder {
        protected boolean includeDesktopClients;
        protected boolean includeMobileClients;
        protected boolean includeWebSessions;
        protected final String teamMemberId;

        protected Builder(String str) {
            if (str != null) {
                this.teamMemberId = str;
                this.includeWebSessions = true;
                this.includeDesktopClients = true;
                this.includeMobileClients = true;
                return;
            }
            throw new IllegalArgumentException("Required value for 'teamMemberId' is null");
        }

        public Builder withIncludeWebSessions(Boolean bool) {
            if (bool != null) {
                this.includeWebSessions = bool.booleanValue();
            } else {
                this.includeWebSessions = true;
            }
            return this;
        }

        public Builder withIncludeDesktopClients(Boolean bool) {
            if (bool != null) {
                this.includeDesktopClients = bool.booleanValue();
            } else {
                this.includeDesktopClients = true;
            }
            return this;
        }

        public Builder withIncludeMobileClients(Boolean bool) {
            if (bool != null) {
                this.includeMobileClients = bool.booleanValue();
            } else {
                this.includeMobileClients = true;
            }
            return this;
        }

        public ListMemberDevicesArg build() {
            return new ListMemberDevicesArg(this.teamMemberId, this.includeWebSessions, this.includeDesktopClients, this.includeMobileClients);
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListMemberDevicesArg$Serializer */
    static class Serializer extends StructSerializer<ListMemberDevicesArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListMemberDevicesArg listMemberDevicesArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("team_member_id");
            StoneSerializers.string().serialize(listMemberDevicesArg.teamMemberId, jsonGenerator);
            jsonGenerator.writeFieldName("include_web_sessions");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listMemberDevicesArg.includeWebSessions), jsonGenerator);
            jsonGenerator.writeFieldName("include_desktop_clients");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listMemberDevicesArg.includeDesktopClients), jsonGenerator);
            jsonGenerator.writeFieldName("include_mobile_clients");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listMemberDevicesArg.includeMobileClients), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListMemberDevicesArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(true);
                Boolean valueOf2 = Boolean.valueOf(true);
                Boolean valueOf3 = Boolean.valueOf(true);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("team_member_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("include_web_sessions".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("include_desktop_clients".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("include_mobile_clients".equals(currentName)) {
                        valueOf3 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ListMemberDevicesArg listMemberDevicesArg = new ListMemberDevicesArg(str2, valueOf.booleanValue(), valueOf2.booleanValue(), valueOf3.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listMemberDevicesArg, listMemberDevicesArg.toStringMultiline());
                    return listMemberDevicesArg;
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

    public ListMemberDevicesArg(String str, boolean z, boolean z2, boolean z3) {
        if (str != null) {
            this.teamMemberId = str;
            this.includeWebSessions = z;
            this.includeDesktopClients = z2;
            this.includeMobileClients = z3;
            return;
        }
        throw new IllegalArgumentException("Required value for 'teamMemberId' is null");
    }

    public ListMemberDevicesArg(String str) {
        this(str, true, true, true);
    }

    public String getTeamMemberId() {
        return this.teamMemberId;
    }

    public boolean getIncludeWebSessions() {
        return this.includeWebSessions;
    }

    public boolean getIncludeDesktopClients() {
        return this.includeDesktopClients;
    }

    public boolean getIncludeMobileClients() {
        return this.includeMobileClients;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.teamMemberId, Boolean.valueOf(this.includeWebSessions), Boolean.valueOf(this.includeDesktopClients), Boolean.valueOf(this.includeMobileClients)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListMemberDevicesArg listMemberDevicesArg = (ListMemberDevicesArg) obj;
        String str = this.teamMemberId;
        String str2 = listMemberDevicesArg.teamMemberId;
        if (!((str == str2 || str.equals(str2)) && this.includeWebSessions == listMemberDevicesArg.includeWebSessions && this.includeDesktopClients == listMemberDevicesArg.includeDesktopClients && this.includeMobileClients == listMemberDevicesArg.includeMobileClients)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
