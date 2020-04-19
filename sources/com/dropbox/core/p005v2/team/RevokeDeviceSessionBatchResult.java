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

/* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionBatchResult */
public class RevokeDeviceSessionBatchResult {
    protected final List<RevokeDeviceSessionStatus> revokeDevicesStatus;

    /* renamed from: com.dropbox.core.v2.team.RevokeDeviceSessionBatchResult$Serializer */
    static class Serializer extends StructSerializer<RevokeDeviceSessionBatchResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RevokeDeviceSessionBatchResult revokeDeviceSessionBatchResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("revoke_devices_status");
            StoneSerializers.list(Serializer.INSTANCE).serialize(revokeDeviceSessionBatchResult.revokeDevicesStatus, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RevokeDeviceSessionBatchResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("revoke_devices_status".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    RevokeDeviceSessionBatchResult revokeDeviceSessionBatchResult = new RevokeDeviceSessionBatchResult(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(revokeDeviceSessionBatchResult, revokeDeviceSessionBatchResult.toStringMultiline());
                    return revokeDeviceSessionBatchResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"revoke_devices_status\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RevokeDeviceSessionBatchResult(List<RevokeDeviceSessionStatus> list) {
        if (list != null) {
            for (RevokeDeviceSessionStatus revokeDeviceSessionStatus : list) {
                if (revokeDeviceSessionStatus == null) {
                    throw new IllegalArgumentException("An item in list 'revokeDevicesStatus' is null");
                }
            }
            this.revokeDevicesStatus = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'revokeDevicesStatus' is null");
    }

    public List<RevokeDeviceSessionStatus> getRevokeDevicesStatus() {
        return this.revokeDevicesStatus;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.revokeDevicesStatus});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RevokeDeviceSessionBatchResult revokeDeviceSessionBatchResult = (RevokeDeviceSessionBatchResult) obj;
        List<RevokeDeviceSessionStatus> list = this.revokeDevicesStatus;
        List<RevokeDeviceSessionStatus> list2 = revokeDeviceSessionBatchResult.revokeDevicesStatus;
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
