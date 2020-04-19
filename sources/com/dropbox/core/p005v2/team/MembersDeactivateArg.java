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

/* renamed from: com.dropbox.core.v2.team.MembersDeactivateArg */
class MembersDeactivateArg extends MembersDeactivateBaseArg {
    protected final boolean wipeData;

    /* renamed from: com.dropbox.core.v2.team.MembersDeactivateArg$Serializer */
    static class Serializer extends StructSerializer<MembersDeactivateArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersDeactivateArg membersDeactivateArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(membersDeactivateArg.user, jsonGenerator);
            jsonGenerator.writeFieldName("wipe_data");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(membersDeactivateArg.wipeData), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersDeactivateArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserSelectorArg userSelectorArg = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(true);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("wipe_data".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userSelectorArg != null) {
                    MembersDeactivateArg membersDeactivateArg = new MembersDeactivateArg(userSelectorArg, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersDeactivateArg, membersDeactivateArg.toStringMultiline());
                    return membersDeactivateArg;
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

    public MembersDeactivateArg(UserSelectorArg userSelectorArg, boolean z) {
        super(userSelectorArg);
        this.wipeData = z;
    }

    public MembersDeactivateArg(UserSelectorArg userSelectorArg) {
        this(userSelectorArg, true);
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public boolean getWipeData() {
        return this.wipeData;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{Boolean.valueOf(this.wipeData)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MembersDeactivateArg membersDeactivateArg = (MembersDeactivateArg) obj;
        if ((this.user != membersDeactivateArg.user && !this.user.equals(membersDeactivateArg.user)) || this.wipeData != membersDeactivateArg.wipeData) {
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
