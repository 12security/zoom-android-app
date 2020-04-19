package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentRemoveInviteesDetails */
public class SharedContentRemoveInviteesDetails {
    protected final List<String> invitees;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentRemoveInviteesDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentRemoveInviteesDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentRemoveInviteesDetails sharedContentRemoveInviteesDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("invitees");
            StoneSerializers.list(StoneSerializers.string()).serialize(sharedContentRemoveInviteesDetails.invitees, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentRemoveInviteesDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("invitees".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    SharedContentRemoveInviteesDetails sharedContentRemoveInviteesDetails = new SharedContentRemoveInviteesDetails(list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedContentRemoveInviteesDetails, sharedContentRemoveInviteesDetails.toStringMultiline());
                    return sharedContentRemoveInviteesDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"invitees\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedContentRemoveInviteesDetails(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'invitees' is null");
                } else if (str.length() > 255) {
                    throw new IllegalArgumentException("Stringan item in list 'invitees' is longer than 255");
                }
            }
            this.invitees = list;
            return;
        }
        throw new IllegalArgumentException("Required value for 'invitees' is null");
    }

    public List<String> getInvitees() {
        return this.invitees;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.invitees});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SharedContentRemoveInviteesDetails sharedContentRemoveInviteesDetails = (SharedContentRemoveInviteesDetails) obj;
        List<String> list = this.invitees;
        List<String> list2 = sharedContentRemoveInviteesDetails.invitees;
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
