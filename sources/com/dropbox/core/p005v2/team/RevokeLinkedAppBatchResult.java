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

/* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppBatchResult */
public class RevokeLinkedAppBatchResult {
    protected final List<RevokeLinkedAppStatus> revokeLinkedAppStatus;

    /* renamed from: com.dropbox.core.v2.team.RevokeLinkedAppBatchResult$Serializer */
    static class Serializer extends StructSerializer<RevokeLinkedAppBatchResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RevokeLinkedAppBatchResult revokeLinkedAppBatchResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("revoke_linked_app_status");
            StoneSerializers.list(Serializer.INSTANCE).serialize(revokeLinkedAppBatchResult.revokeLinkedAppStatus, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RevokeLinkedAppBatchResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("revoke_linked_app_status".equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    RevokeLinkedAppBatchResult revokeLinkedAppBatchResult = new RevokeLinkedAppBatchResult(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(revokeLinkedAppBatchResult, revokeLinkedAppBatchResult.toStringMultiline());
                    return revokeLinkedAppBatchResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"revoke_linked_app_status\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RevokeLinkedAppBatchResult(List<RevokeLinkedAppStatus> list) {
        if (list != null) {
            for (RevokeLinkedAppStatus revokeLinkedAppStatus2 : list) {
                if (revokeLinkedAppStatus2 == null) {
                    throw new IllegalArgumentException("An item in list 'revokeLinkedAppStatus' is null");
                }
            }
            this.revokeLinkedAppStatus = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'revokeLinkedAppStatus' is null");
    }

    public List<RevokeLinkedAppStatus> getRevokeLinkedAppStatus() {
        return this.revokeLinkedAppStatus;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.revokeLinkedAppStatus});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RevokeLinkedAppBatchResult revokeLinkedAppBatchResult = (RevokeLinkedAppBatchResult) obj;
        List<RevokeLinkedAppStatus> list = this.revokeLinkedAppStatus;
        List<RevokeLinkedAppStatus> list2 = revokeLinkedAppBatchResult.revokeLinkedAppStatus;
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
