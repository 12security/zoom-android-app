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

/* renamed from: com.dropbox.core.v2.teamlog.DeviceDeleteOnUnlinkFailDetails */
public class DeviceDeleteOnUnlinkFailDetails {
    protected final String displayName;
    protected final long numFailures;
    protected final SessionLogInfo sessionInfo;

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceDeleteOnUnlinkFailDetails$Builder */
    public static class Builder {
        protected String displayName = null;
        protected final long numFailures;
        protected SessionLogInfo sessionInfo = null;

        protected Builder(long j) {
            this.numFailures = j;
        }

        public Builder withSessionInfo(SessionLogInfo sessionLogInfo) {
            this.sessionInfo = sessionLogInfo;
            return this;
        }

        public Builder withDisplayName(String str) {
            this.displayName = str;
            return this;
        }

        public DeviceDeleteOnUnlinkFailDetails build() {
            return new DeviceDeleteOnUnlinkFailDetails(this.numFailures, this.sessionInfo, this.displayName);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.DeviceDeleteOnUnlinkFailDetails$Serializer */
    static class Serializer extends StructSerializer<DeviceDeleteOnUnlinkFailDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeviceDeleteOnUnlinkFailDetails deviceDeleteOnUnlinkFailDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("num_failures");
            StoneSerializers.int64().serialize(Long.valueOf(deviceDeleteOnUnlinkFailDetails.numFailures), jsonGenerator);
            if (deviceDeleteOnUnlinkFailDetails.sessionInfo != null) {
                jsonGenerator.writeFieldName("session_info");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(deviceDeleteOnUnlinkFailDetails.sessionInfo, jsonGenerator);
            }
            if (deviceDeleteOnUnlinkFailDetails.displayName != null) {
                jsonGenerator.writeFieldName("display_name");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(deviceDeleteOnUnlinkFailDetails.displayName, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public DeviceDeleteOnUnlinkFailDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
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
                    if ("num_failures".equals(currentName)) {
                        l = (Long) StoneSerializers.int64().deserialize(jsonParser);
                    } else if ("session_info".equals(currentName)) {
                        sessionLogInfo = (SessionLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("display_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l != null) {
                    DeviceDeleteOnUnlinkFailDetails deviceDeleteOnUnlinkFailDetails = new DeviceDeleteOnUnlinkFailDetails(l.longValue(), sessionLogInfo, str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(deviceDeleteOnUnlinkFailDetails, deviceDeleteOnUnlinkFailDetails.toStringMultiline());
                    return deviceDeleteOnUnlinkFailDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"num_failures\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public DeviceDeleteOnUnlinkFailDetails(long j, SessionLogInfo sessionLogInfo, String str) {
        this.sessionInfo = sessionLogInfo;
        this.displayName = str;
        this.numFailures = j;
    }

    public DeviceDeleteOnUnlinkFailDetails(long j) {
        this(j, null, null);
    }

    public long getNumFailures() {
        return this.numFailures;
    }

    public SessionLogInfo getSessionInfo() {
        return this.sessionInfo;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static Builder newBuilder(long j) {
        return new Builder(j);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sessionInfo, this.displayName, Long.valueOf(this.numFailures)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        if (r2.equals(r7) == false) goto L_0x003d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
            com.dropbox.core.v2.teamlog.DeviceDeleteOnUnlinkFailDetails r7 = (com.dropbox.core.p005v2.teamlog.DeviceDeleteOnUnlinkFailDetails) r7
            long r2 = r6.numFailures
            long r4 = r7.numFailures
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x003d
            com.dropbox.core.v2.teamlog.SessionLogInfo r2 = r6.sessionInfo
            com.dropbox.core.v2.teamlog.SessionLogInfo r3 = r7.sessionInfo
            if (r2 == r3) goto L_0x002e
            if (r2 == 0) goto L_0x003d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003d
        L_0x002e:
            java.lang.String r2 = r6.displayName
            java.lang.String r7 = r7.displayName
            if (r2 == r7) goto L_0x003e
            if (r2 == 0) goto L_0x003d
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = 0
        L_0x003e:
            return r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.DeviceDeleteOnUnlinkFailDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
