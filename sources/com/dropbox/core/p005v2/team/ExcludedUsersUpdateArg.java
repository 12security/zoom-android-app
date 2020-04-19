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

/* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateArg */
class ExcludedUsersUpdateArg {
    protected final List<UserSelectorArg> users;

    /* renamed from: com.dropbox.core.v2.team.ExcludedUsersUpdateArg$Serializer */
    static class Serializer extends StructSerializer<ExcludedUsersUpdateArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ExcludedUsersUpdateArg excludedUsersUpdateArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (excludedUsersUpdateArg.users != null) {
                jsonGenerator.writeFieldName("users");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(excludedUsersUpdateArg.users, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ExcludedUsersUpdateArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("users".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                ExcludedUsersUpdateArg excludedUsersUpdateArg = new ExcludedUsersUpdateArg(list);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(excludedUsersUpdateArg, excludedUsersUpdateArg.toStringMultiline());
                return excludedUsersUpdateArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public ExcludedUsersUpdateArg(List<UserSelectorArg> list) {
        if (list != null) {
            for (UserSelectorArg userSelectorArg : list) {
                if (userSelectorArg == null) {
                    throw new IllegalArgumentException("An item in list 'users' is null");
                }
            }
        }
        this.users = list;
    }

    public ExcludedUsersUpdateArg() {
        this(null);
    }

    public List<UserSelectorArg> getUsers() {
        return this.users;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.users});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        ExcludedUsersUpdateArg excludedUsersUpdateArg = (ExcludedUsersUpdateArg) obj;
        List<UserSelectorArg> list = this.users;
        List<UserSelectorArg> list2 = excludedUsersUpdateArg.users;
        if (list != list2 && (list == null || !list.equals(list2))) {
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
