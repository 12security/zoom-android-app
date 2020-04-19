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

/* renamed from: com.dropbox.core.v2.team.ListMembersDevicesArg */
class ListMembersDevicesArg {
    protected final String cursor;
    protected final boolean includeDesktopClients;
    protected final boolean includeMobileClients;
    protected final boolean includeWebSessions;

    /* renamed from: com.dropbox.core.v2.team.ListMembersDevicesArg$Builder */
    public static class Builder {
        protected String cursor = null;
        protected boolean includeDesktopClients = true;
        protected boolean includeMobileClients = true;
        protected boolean includeWebSessions = true;

        protected Builder() {
        }

        public Builder withCursor(String str) {
            this.cursor = str;
            return this;
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

        public ListMembersDevicesArg build() {
            return new ListMembersDevicesArg(this.cursor, this.includeWebSessions, this.includeDesktopClients, this.includeMobileClients);
        }
    }

    /* renamed from: com.dropbox.core.v2.team.ListMembersDevicesArg$Serializer */
    static class Serializer extends StructSerializer<ListMembersDevicesArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListMembersDevicesArg listMembersDevicesArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (listMembersDevicesArg.cursor != null) {
                jsonGenerator.writeFieldName("cursor");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(listMembersDevicesArg.cursor, jsonGenerator);
            }
            jsonGenerator.writeFieldName("include_web_sessions");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listMembersDevicesArg.includeWebSessions), jsonGenerator);
            jsonGenerator.writeFieldName("include_desktop_clients");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listMembersDevicesArg.includeDesktopClients), jsonGenerator);
            jsonGenerator.writeFieldName("include_mobile_clients");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listMembersDevicesArg.includeMobileClients), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListMembersDevicesArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("cursor".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
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
                ListMembersDevicesArg listMembersDevicesArg = new ListMembersDevicesArg(str2, valueOf.booleanValue(), valueOf2.booleanValue(), valueOf3.booleanValue());
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(listMembersDevicesArg, listMembersDevicesArg.toStringMultiline());
                return listMembersDevicesArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ListMembersDevicesArg(String str, boolean z, boolean z2, boolean z3) {
        this.cursor = str;
        this.includeWebSessions = z;
        this.includeDesktopClients = z2;
        this.includeMobileClients = z3;
    }

    public ListMembersDevicesArg() {
        this(null, true, true, true);
    }

    public String getCursor() {
        return this.cursor;
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

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.cursor, Boolean.valueOf(this.includeWebSessions), Boolean.valueOf(this.includeDesktopClients), Boolean.valueOf(this.includeMobileClients)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ListMembersDevicesArg listMembersDevicesArg = (ListMembersDevicesArg) obj;
        String str = this.cursor;
        String str2 = listMembersDevicesArg.cursor;
        if (!((str == str2 || (str != null && str.equals(str2))) && this.includeWebSessions == listMembersDevicesArg.includeWebSessions && this.includeDesktopClients == listMembersDevicesArg.includeDesktopClients && this.includeMobileClients == listMembersDevicesArg.includeMobileClients)) {
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
