package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.p005v2.sharing.AccessLevel;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentAddMemberDetails */
public class SharedContentAddMemberDetails {
    protected final AccessLevel sharedContentAccessLevel;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentAddMemberDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentAddMemberDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentAddMemberDetails sharedContentAddMemberDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_content_access_level");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(sharedContentAddMemberDetails.sharedContentAccessLevel, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentAddMemberDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AccessLevel accessLevel = null;
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
                    if ("shared_content_access_level".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessLevel != null) {
                    SharedContentAddMemberDetails sharedContentAddMemberDetails = new SharedContentAddMemberDetails(accessLevel);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedContentAddMemberDetails, sharedContentAddMemberDetails.toStringMultiline());
                    return sharedContentAddMemberDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"shared_content_access_level\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedContentAddMemberDetails(AccessLevel accessLevel) {
        if (accessLevel != null) {
            this.sharedContentAccessLevel = accessLevel;
            return;
        }
        throw new IllegalArgumentException("Required value for 'sharedContentAccessLevel' is null");
    }

    public AccessLevel getSharedContentAccessLevel() {
        return this.sharedContentAccessLevel;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedContentAccessLevel});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SharedContentAddMemberDetails sharedContentAddMemberDetails = (SharedContentAddMemberDetails) obj;
        AccessLevel accessLevel = this.sharedContentAccessLevel;
        AccessLevel accessLevel2 = sharedContentAddMemberDetails.sharedContentAccessLevel;
        if (accessLevel != accessLevel2 && !accessLevel.equals(accessLevel2)) {
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
