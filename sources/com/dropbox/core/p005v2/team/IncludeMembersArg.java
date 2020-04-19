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

/* renamed from: com.dropbox.core.v2.team.IncludeMembersArg */
class IncludeMembersArg {
    protected final boolean returnMembers;

    /* renamed from: com.dropbox.core.v2.team.IncludeMembersArg$Serializer */
    private static class Serializer extends StructSerializer<IncludeMembersArg> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(IncludeMembersArg includeMembersArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("return_members");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(includeMembersArg.returnMembers), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public IncludeMembersArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
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
                    if ("return_members".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                IncludeMembersArg includeMembersArg = new IncludeMembersArg(valueOf.booleanValue());
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(includeMembersArg, includeMembersArg.toStringMultiline());
                return includeMembersArg;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public IncludeMembersArg(boolean z) {
        this.returnMembers = z;
    }

    public IncludeMembersArg() {
        this(true);
    }

    public boolean getReturnMembers() {
        return this.returnMembers;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Boolean.valueOf(this.returnMembers)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        if (this.returnMembers != ((IncludeMembersArg) obj).returnMembers) {
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
