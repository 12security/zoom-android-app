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

/* renamed from: com.dropbox.core.v2.teamlog.SharedLinkCopyDetails */
public class SharedLinkCopyDetails {
    protected final UserLogInfo sharedLinkOwner;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedLinkCopyDetails$Serializer */
    static class Serializer extends StructSerializer<SharedLinkCopyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedLinkCopyDetails sharedLinkCopyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedLinkCopyDetails.sharedLinkOwner != null) {
                jsonGenerator.writeFieldName("shared_link_owner");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedLinkCopyDetails.sharedLinkOwner, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedLinkCopyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserLogInfo userLogInfo = null;
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
                    if ("shared_link_owner".equals(currentName)) {
                        userLogInfo = (UserLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedLinkCopyDetails sharedLinkCopyDetails = new SharedLinkCopyDetails(userLogInfo);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedLinkCopyDetails, sharedLinkCopyDetails.toStringMultiline());
                return sharedLinkCopyDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedLinkCopyDetails(UserLogInfo userLogInfo) {
        this.sharedLinkOwner = userLogInfo;
    }

    public SharedLinkCopyDetails() {
        this(null);
    }

    public UserLogInfo getSharedLinkOwner() {
        return this.sharedLinkOwner;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedLinkOwner});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        SharedLinkCopyDetails sharedLinkCopyDetails = (SharedLinkCopyDetails) obj;
        UserLogInfo userLogInfo = this.sharedLinkOwner;
        UserLogInfo userLogInfo2 = sharedLinkCopyDetails.sharedLinkOwner;
        if (userLogInfo != userLogInfo2 && (userLogInfo == null || !userLogInfo.equals(userLogInfo2))) {
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
