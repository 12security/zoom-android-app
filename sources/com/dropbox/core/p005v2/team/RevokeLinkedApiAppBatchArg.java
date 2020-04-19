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

/* renamed from: com.dropbox.core.v2.team.RevokeLinkedApiAppBatchArg */
class RevokeLinkedApiAppBatchArg {
    protected final List<RevokeLinkedApiAppArg> revokeLinkedApp;

    /* renamed from: com.dropbox.core.v2.team.RevokeLinkedApiAppBatchArg$Serializer */
    static class Serializer extends StructSerializer<RevokeLinkedApiAppBatchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RevokeLinkedApiAppBatchArg revokeLinkedApiAppBatchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("revoke_linked_app");
            StoneSerializers.list(Serializer.INSTANCE).serialize(revokeLinkedApiAppBatchArg.revokeLinkedApp, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RevokeLinkedApiAppBatchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("revoke_linked_app".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    RevokeLinkedApiAppBatchArg revokeLinkedApiAppBatchArg = new RevokeLinkedApiAppBatchArg(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(revokeLinkedApiAppBatchArg, revokeLinkedApiAppBatchArg.toStringMultiline());
                    return revokeLinkedApiAppBatchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"revoke_linked_app\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RevokeLinkedApiAppBatchArg(List<RevokeLinkedApiAppArg> list) {
        if (list != null) {
            for (RevokeLinkedApiAppArg revokeLinkedApiAppArg : list) {
                if (revokeLinkedApiAppArg == null) {
                    throw new IllegalArgumentException("An item in list 'revokeLinkedApp' is null");
                }
            }
            this.revokeLinkedApp = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'revokeLinkedApp' is null");
    }

    public List<RevokeLinkedApiAppArg> getRevokeLinkedApp() {
        return this.revokeLinkedApp;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.revokeLinkedApp});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RevokeLinkedApiAppBatchArg revokeLinkedApiAppBatchArg = (RevokeLinkedApiAppBatchArg) obj;
        List<RevokeLinkedApiAppArg> list = this.revokeLinkedApp;
        List<RevokeLinkedApiAppArg> list2 = revokeLinkedApiAppBatchArg.revokeLinkedApp;
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
