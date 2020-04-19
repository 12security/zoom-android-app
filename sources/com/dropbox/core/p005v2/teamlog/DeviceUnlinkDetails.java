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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceUnlinkDetails */
public class DeviceUnlinkDetails {
    protected final boolean deleteData;
    protected final String displayName;
    protected final SessionLogInfo sessionInfo;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceUnlinkDetails$Builder */
    public static class Builder {
        protected final boolean deleteData;
        protected String displayName = null;
        protected SessionLogInfo sessionInfo = null;

        protected Builder(boolean z) {
            this.deleteData = z;
        }

        public Builder withSessionInfo(SessionLogInfo sessionLogInfo) {
            this.sessionInfo = sessionLogInfo;
            return this;
        }

        public Builder withDisplayName(String str) {
            this.displayName = str;
            return this;
        }

        public DeviceUnlinkDetails build() {
            return new DeviceUnlinkDetails(this.deleteData, this.sessionInfo, this.displayName);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceUnlinkDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceUnlinkDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceUnlinkDetails deviceUnlinkDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("delete_data");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(deviceUnlinkDetails.deleteData), jsonGenerator);
            if (deviceUnlinkDetails.sessionInfo != null) {
                jsonGenerator.writeFieldName("session_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(deviceUnlinkDetails.sessionInfo, jsonGenerator);
            }
            if (deviceUnlinkDetails.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deviceUnlinkDetails.displayName, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceUnlinkDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SessionLogInfo sessionLogInfo = null;
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("delete_data".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("session_info".equals(currentName)) {
                        sessionLogInfo = (SessionLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (bool != null) {
                    DeviceUnlinkDetails deviceUnlinkDetails = new DeviceUnlinkDetails(bool.booleanValue(), sessionLogInfo, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(deviceUnlinkDetails, deviceUnlinkDetails.toStringMultiline());
                    return deviceUnlinkDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"delete_data\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceUnlinkDetails(boolean z, SessionLogInfo sessionLogInfo, String str) {
        this.sessionInfo = sessionLogInfo;
        this.displayName = str;
        this.deleteData = z;
    }

    public DeviceUnlinkDetails(boolean z) {
        this(z, null, null);
    }

    public boolean getDeleteData() {
        return this.deleteData;
    }

    public SessionLogInfo getSessionInfo() {
        return this.sessionInfo;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Builder newBuilder(boolean z) {
        return new Builder(z);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sessionInfo, this.displayName, Boolean.valueOf(this.deleteData)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0038, code lost:
        if (r2.equals(r5) == false) goto L_0x003b;
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
            if (r2 == 0) goto L_0x003d
            com.dropbox.core.v2.teamlog.DeviceUnlinkDetails r5 = (com.dropbox.core.p005v2.teamlog.DeviceUnlinkDetails) r5
            boolean r2 = r4.deleteData
            boolean r3 = r5.deleteData
            if (r2 != r3) goto L_0x003b
            com.dropbox.core.v2.teamlog.SessionLogInfo r2 = r4.sessionInfo
            com.dropbox.core.v2.teamlog.SessionLogInfo r3 = r5.sessionInfo
            if (r2 == r3) goto L_0x002c
            if (r2 == 0) goto L_0x003b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003b
        L_0x002c:
            java.lang.String r2 = r4.displayName
            java.lang.String r5 = r5.displayName
            if (r2 == r5) goto L_0x003c
            if (r2 == 0) goto L_0x003b
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003b
            goto L_0x003c
        L_0x003b:
            r0 = 0
        L_0x003c:
            return r0
        L_0x003d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DeviceUnlinkDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
