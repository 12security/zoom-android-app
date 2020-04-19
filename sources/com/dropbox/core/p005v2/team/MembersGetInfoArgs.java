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

/* renamed from: com.dropbox.core.v2.team.MembersGetInfoArgs */
class MembersGetInfoArgs {
    protected final List<UserSelectorArg> members;

    /* renamed from: com.dropbox.core.v2.team.MembersGetInfoArgs$Serializer */
    static class Serializer extends StructSerializer<MembersGetInfoArgs> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersGetInfoArgs membersGetInfoArgs, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("members");
            StoneSerializers.list(Serializer.INSTANCE).serialize(membersGetInfoArgs.members, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersGetInfoArgs deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("members".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    MembersGetInfoArgs membersGetInfoArgs = new MembersGetInfoArgs(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersGetInfoArgs, membersGetInfoArgs.toStringMultiline());
                    return membersGetInfoArgs;
                }
                throw new JsonParseException(jsonParser, "Required field \"members\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MembersGetInfoArgs(List<UserSelectorArg> list) {
        if (list != null) {
            for (UserSelectorArg userSelectorArg : list) {
                if (userSelectorArg == null) {
                    throw new IllegalArgumentException("An item in list 'members' is null");
                }
            }
            this.members = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'members' is null");
    }

    public List<UserSelectorArg> getMembers() {
        return this.members;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.members});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MembersGetInfoArgs membersGetInfoArgs = (MembersGetInfoArgs) obj;
        List<UserSelectorArg> list = this.members;
        List<UserSelectorArg> list2 = membersGetInfoArgs.members;
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
