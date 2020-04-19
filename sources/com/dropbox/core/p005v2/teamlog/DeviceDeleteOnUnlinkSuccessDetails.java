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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceDeleteOnUnlinkSuccessDetails */
public class DeviceDeleteOnUnlinkSuccessDetails {
    protected final String displayName;
    protected final SessionLogInfo sessionInfo;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceDeleteOnUnlinkSuccessDetails$Builder */
    public static class Builder {
        protected String displayName = null;
        protected SessionLogInfo sessionInfo = null;

        protected Builder() {
        }

        public Builder withSessionInfo(SessionLogInfo sessionLogInfo) {
            this.sessionInfo = sessionLogInfo;
            return this;
        }

        public Builder withDisplayName(String str) {
            this.displayName = str;
            return this;
        }

        public DeviceDeleteOnUnlinkSuccessDetails build() {
            return new DeviceDeleteOnUnlinkSuccessDetails(this.sessionInfo, this.displayName);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceDeleteOnUnlinkSuccessDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceDeleteOnUnlinkSuccessDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceDeleteOnUnlinkSuccessDetails deviceDeleteOnUnlinkSuccessDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (deviceDeleteOnUnlinkSuccessDetails.sessionInfo != null) {
                jsonGenerator.writeFieldName("session_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(deviceDeleteOnUnlinkSuccessDetails.sessionInfo, jsonGenerator);
            }
            if (deviceDeleteOnUnlinkSuccessDetails.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deviceDeleteOnUnlinkSuccessDetails.displayName, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceDeleteOnUnlinkSuccessDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SessionLogInfo sessionLogInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("session_info".equals(currentName)) {
                        sessionLogInfo = (SessionLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                DeviceDeleteOnUnlinkSuccessDetails deviceDeleteOnUnlinkSuccessDetails = new DeviceDeleteOnUnlinkSuccessDetails(sessionLogInfo, str2);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(deviceDeleteOnUnlinkSuccessDetails, deviceDeleteOnUnlinkSuccessDetails.toStringMultiline());
                return deviceDeleteOnUnlinkSuccessDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceDeleteOnUnlinkSuccessDetails(SessionLogInfo sessionLogInfo, String str) {
        this.sessionInfo = sessionLogInfo;
        this.displayName = str;
    }

    public DeviceDeleteOnUnlinkSuccessDetails() {
        this(null, null);
    }

    public SessionLogInfo getSessionInfo() {
        return this.sessionInfo;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sessionInfo, this.displayName});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (r2.equals(r5) == false) goto L_0x0035;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0037
            com.dropbox.core.v2.teamlog.DeviceDeleteOnUnlinkSuccessDetails r5 = (com.dropbox.core.p005v2.teamlog.DeviceDeleteOnUnlinkSuccessDetails) r5
            com.dropbox.core.v2.teamlog.SessionLogInfo r2 = r4.sessionInfo
            com.dropbox.core.v2.teamlog.SessionLogInfo r3 = r5.sessionInfo
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.lang.String r2 = r4.displayName
            java.lang.String r5 = r5.displayName
            if (r2 == r5) goto L_0x0036
            if (r2 == 0) goto L_0x0035
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r0 = 0
        L_0x0036:
            return r0
        L_0x0037:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DeviceDeleteOnUnlinkSuccessDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
