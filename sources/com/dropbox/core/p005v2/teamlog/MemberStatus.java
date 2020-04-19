package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.teamlog.MemberStatus */
public enum MemberStatus {
    NOT_JOINED,
    INVITED,
    ACTIVE,
    SUSPENDED,
    REMOVED,
    OTHER;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberStatus$Serializer */
    static class Serializer extends UnionSerializer<MemberStatus> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(MemberStatus memberStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (memberStatus) {
                case NOT_JOINED:
                    jsonGenerator.writeString("not_joined");
                    return;
                case INVITED:
                    jsonGenerator.writeString("invited");
                    return;
                case ACTIVE:
                    jsonGenerator.writeString(ConditionalUserProperty.ACTIVE);
                    return;
                case SUSPENDED:
                    jsonGenerator.writeString("suspended");
                    return;
                case REMOVED:
                    jsonGenerator.writeString("removed");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MemberStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MemberStatus memberStatus;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("not_joined".equals(str)) {
                    memberStatus = MemberStatus.NOT_JOINED;
                } else if ("invited".equals(str)) {
                    memberStatus = MemberStatus.INVITED;
                } else if (ConditionalUserProperty.ACTIVE.equals(str)) {
                    memberStatus = MemberStatus.ACTIVE;
                } else if ("suspended".equals(str)) {
                    memberStatus = MemberStatus.SUSPENDED;
                } else if ("removed".equals(str)) {
                    memberStatus = MemberStatus.REMOVED;
                } else {
                    memberStatus = MemberStatus.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return memberStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
