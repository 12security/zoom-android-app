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

/* renamed from: com.dropbox.core.v2.team.SetCustomQuotaArg */
class SetCustomQuotaArg {
    protected final List<UserCustomQuotaArg> usersAndQuotas;

    /* renamed from: com.dropbox.core.v2.team.SetCustomQuotaArg$Serializer */
    static class Serializer extends StructSerializer<SetCustomQuotaArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SetCustomQuotaArg setCustomQuotaArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("users_and_quotas");
            StoneSerializers.list(Serializer.INSTANCE).serialize(setCustomQuotaArg.usersAndQuotas, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SetCustomQuotaArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("users_and_quotas".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    SetCustomQuotaArg setCustomQuotaArg = new SetCustomQuotaArg(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(setCustomQuotaArg, setCustomQuotaArg.toStringMultiline());
                    return setCustomQuotaArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"users_and_quotas\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SetCustomQuotaArg(List<UserCustomQuotaArg> list) {
        if (list != null) {
            for (UserCustomQuotaArg userCustomQuotaArg : list) {
                if (userCustomQuotaArg == null) {
                    throw new IllegalArgumentException("An item in list 'usersAndQuotas' is null");
                }
            }
            this.usersAndQuotas = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'usersAndQuotas' is null");
    }

    public List<UserCustomQuotaArg> getUsersAndQuotas() {
        return this.usersAndQuotas;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.usersAndQuotas});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SetCustomQuotaArg setCustomQuotaArg = (SetCustomQuotaArg) obj;
        List<UserCustomQuotaArg> list = this.usersAndQuotas;
        List<UserCustomQuotaArg> list2 = setCustomQuotaArg.usersAndQuotas;
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
