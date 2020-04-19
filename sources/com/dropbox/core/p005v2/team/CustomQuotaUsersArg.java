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

/* renamed from: com.dropbox.core.v2.team.CustomQuotaUsersArg */
class CustomQuotaUsersArg {
    protected final List<UserSelectorArg> users;

    /* renamed from: com.dropbox.core.v2.team.CustomQuotaUsersArg$Serializer */
    static class Serializer extends StructSerializer<CustomQuotaUsersArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CustomQuotaUsersArg customQuotaUsersArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("users");
            StoneSerializers.list(Serializer.INSTANCE).serialize(customQuotaUsersArg.users, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CustomQuotaUsersArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    CustomQuotaUsersArg customQuotaUsersArg = new CustomQuotaUsersArg(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(customQuotaUsersArg, customQuotaUsersArg.toStringMultiline());
                    return customQuotaUsersArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"users\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public CustomQuotaUsersArg(List<UserSelectorArg> list) {
        if (list != null) {
            for (UserSelectorArg userSelectorArg : list) {
                if (userSelectorArg == null) {
                    throw new IllegalArgumentException("An item in list 'users' is null");
                }
            }
            this.users = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'users' is null");
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
        CustomQuotaUsersArg customQuotaUsersArg = (CustomQuotaUsersArg) obj;
        List<UserSelectorArg> list = this.users;
        List<UserSelectorArg> list2 = customQuotaUsersArg.users;
        if (list != list2 && !list.equals(list2)) {
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
