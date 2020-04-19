package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.MembersUnsuspendArg */
class MembersUnsuspendArg {
    protected final UserSelectorArg user;

    /* renamed from: com.dropbox.core.v2.team.MembersUnsuspendArg$Serializer */
    static class Serializer extends StructSerializer<MembersUnsuspendArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersUnsuspendArg membersUnsuspendArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(membersUnsuspendArg.user, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersUnsuspendArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserSelectorArg userSelectorArg = null;
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
                    if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userSelectorArg != null) {
                    MembersUnsuspendArg membersUnsuspendArg = new MembersUnsuspendArg(userSelectorArg);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersUnsuspendArg, membersUnsuspendArg.toStringMultiline());
                    return membersUnsuspendArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MembersUnsuspendArg(UserSelectorArg userSelectorArg) {
        if (userSelectorArg != null) {
            this.user = userSelectorArg;
            return;
        }
        throw new IllegalArgumentException("Required value for 'user' is null");
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.user});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MembersUnsuspendArg membersUnsuspendArg = (MembersUnsuspendArg) obj;
        UserSelectorArg userSelectorArg = this.user;
        UserSelectorArg userSelectorArg2 = membersUnsuspendArg.user;
        if (userSelectorArg != userSelectorArg2 && !userSelectorArg.equals(userSelectorArg2)) {
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
