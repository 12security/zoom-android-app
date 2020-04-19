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

/* renamed from: com.dropbox.core.v2.team.MembersAddArg */
class MembersAddArg {
    protected final boolean forceAsync;
    protected final List<MemberAddArg> newMembers;

    /* renamed from: com.dropbox.core.v2.team.MembersAddArg$Serializer */
    static class Serializer extends StructSerializer<MembersAddArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MembersAddArg membersAddArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_members");
            StoneSerializers.list(Serializer.INSTANCE).serialize(membersAddArg.newMembers, jsonGenerator);
            jsonGenerator.writeFieldName("force_async");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(membersAddArg.forceAsync), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MembersAddArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_members".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("force_async".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    MembersAddArg membersAddArg = new MembersAddArg(list, valueOf.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(membersAddArg, membersAddArg.toStringMultiline());
                    return membersAddArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_members\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MembersAddArg(List<MemberAddArg> list, boolean z) {
        if (list != null) {
            for (MemberAddArg memberAddArg : list) {
                if (memberAddArg == null) {
                    throw new IllegalArgumentException("An item in list 'newMembers' is null");
                }
            }
            this.newMembers = list;
            this.forceAsync = z;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newMembers' is null");
    }

    public MembersAddArg(List<MemberAddArg> list) {
        this(list, false);
    }

    public List<MemberAddArg> getNewMembers() {
        return this.newMembers;
    }

    public boolean getForceAsync() {
        return this.forceAsync;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newMembers, Boolean.valueOf(this.forceAsync)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MembersAddArg membersAddArg = (MembersAddArg) obj;
        List<MemberAddArg> list = this.newMembers;
        List<MemberAddArg> list2 = membersAddArg.newMembers;
        if ((list != list2 && !list.equals(list2)) || this.forceAsync != membersAddArg.forceAsync) {
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
