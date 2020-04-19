package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.MemberAddNameDetails */
public class MemberAddNameDetails {
    protected final UserNameLogInfo newValue;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberAddNameDetails$Serializer */
    static class Serializer extends StructSerializer<MemberAddNameDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberAddNameDetails memberAddNameDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(memberAddNameDetails.newValue, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberAddNameDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserNameLogInfo userNameLogInfo = null;
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
                    if ("new_value".equals(currentName)) {
                        userNameLogInfo = (UserNameLogInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userNameLogInfo != null) {
                    MemberAddNameDetails memberAddNameDetails = new MemberAddNameDetails(userNameLogInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberAddNameDetails, memberAddNameDetails.toStringMultiline());
                    return memberAddNameDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MemberAddNameDetails(UserNameLogInfo userNameLogInfo) {
        if (userNameLogInfo != null) {
            this.newValue = userNameLogInfo;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public UserNameLogInfo getNewValue() {
        return this.newValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        MemberAddNameDetails memberAddNameDetails = (MemberAddNameDetails) obj;
        UserNameLogInfo userNameLogInfo = this.newValue;
        UserNameLogInfo userNameLogInfo2 = memberAddNameDetails.newValue;
        if (userNameLogInfo != userNameLogInfo2 && !userNameLogInfo.equals(userNameLogInfo2)) {
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
