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

/* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionBatchArg */
class RevokeDeviceSessionBatchArg {
    protected final List<RevokeDeviceSessionArg> revokeDevices;

    /* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionBatchArg$Serializer */
    static class Serializer extends StructSerializer<RevokeDeviceSessionBatchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RevokeDeviceSessionBatchArg revokeDeviceSessionBatchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("revoke_devices");
            StoneSerializers.list(Serializer.INSTANCE).serialize(revokeDeviceSessionBatchArg.revokeDevices, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RevokeDeviceSessionBatchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("revoke_devices".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    RevokeDeviceSessionBatchArg revokeDeviceSessionBatchArg = new RevokeDeviceSessionBatchArg(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(revokeDeviceSessionBatchArg, revokeDeviceSessionBatchArg.toStringMultiline());
                    return revokeDeviceSessionBatchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"revoke_devices\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RevokeDeviceSessionBatchArg(List<RevokeDeviceSessionArg> list) {
        if (list != null) {
            for (RevokeDeviceSessionArg revokeDeviceSessionArg : list) {
                if (revokeDeviceSessionArg == null) {
                    throw new IllegalArgumentException("An item in list 'revokeDevices' is null");
                }
            }
            this.revokeDevices = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'revokeDevices' is null");
    }

    public List<RevokeDeviceSessionArg> getRevokeDevices() {
        return this.revokeDevices;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.revokeDevices});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RevokeDeviceSessionBatchArg revokeDeviceSessionBatchArg = (RevokeDeviceSessionBatchArg) obj;
        List<RevokeDeviceSessionArg> list = this.revokeDevices;
        List<RevokeDeviceSessionArg> list2 = revokeDeviceSessionBatchArg.revokeDevices;
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
