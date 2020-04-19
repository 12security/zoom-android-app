package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.GetDevicesReport */
public class GetDevicesReport extends BaseDfbReport {
    protected final DevicesActive active1Day;
    protected final DevicesActive active28Day;
    protected final DevicesActive active7Day;

    /* renamed from: com.dropbox.core.v2.team.GetDevicesReport$Serializer */
    static class Serializer extends StructSerializer<GetDevicesReport> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetDevicesReport getDevicesReport, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(Param.START_DATE);
            StoneSerializers.string().serialize(getDevicesReport.startDate, jsonGenerator);
            jsonGenerator.writeFieldName("active_1_day");
            Serializer.INSTANCE.serialize(getDevicesReport.active1Day, jsonGenerator);
            jsonGenerator.writeFieldName("active_7_day");
            Serializer.INSTANCE.serialize(getDevicesReport.active7Day, jsonGenerator);
            jsonGenerator.writeFieldName("active_28_day");
            Serializer.INSTANCE.serialize(getDevicesReport.active28Day, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetDevicesReport deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                DevicesActive devicesActive = null;
                DevicesActive devicesActive2 = null;
                DevicesActive devicesActive3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (Param.START_DATE.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("active_1_day".equals(currentName)) {
                        devicesActive = (DevicesActive) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("active_7_day".equals(currentName)) {
                        devicesActive2 = (DevicesActive) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("active_28_day".equals(currentName)) {
                        devicesActive3 = (DevicesActive) Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"start_date\" missing.");
                } else if (devicesActive == null) {
                    throw new JsonParseException(jsonParser, "Required field \"active_1_day\" missing.");
                } else if (devicesActive2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"active_7_day\" missing.");
                } else if (devicesActive3 != null) {
                    GetDevicesReport getDevicesReport = new GetDevicesReport(str2, devicesActive, devicesActive2, devicesActive3);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getDevicesReport, getDevicesReport.toStringMultiline());
                    return getDevicesReport;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"active_28_day\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public GetDevicesReport(String str, DevicesActive devicesActive, DevicesActive devicesActive2, DevicesActive devicesActive3) {
        super(str);
        if (devicesActive != null) {
            this.active1Day = devicesActive;
            if (devicesActive2 != null) {
                this.active7Day = devicesActive2;
                if (devicesActive3 != null) {
                    this.active28Day = devicesActive3;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'active28Day' is null");
            }
            throw new IllegalArgumentException("Required value for 'active7Day' is null");
        }
        throw new IllegalArgumentException("Required value for 'active1Day' is null");
    }

    public String getStartDate() {
        return this.startDate;
    }

    public DevicesActive getActive1Day() {
        return this.active1Day;
    }

    public DevicesActive getActive7Day() {
        return this.active7Day;
    }

    public DevicesActive getActive28Day() {
        return this.active28Day;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.active1Day, this.active7Day, this.active28Day});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
        if (r2.equals(r5) == false) goto L_0x004d;
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
            if (r2 == 0) goto L_0x004f
            com.dropbox.core.v2.team.GetDevicesReport r5 = (com.dropbox.core.p005v2.team.GetDevicesReport) r5
            java.lang.String r2 = r4.startDate
            java.lang.String r3 = r5.startDate
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.startDate
            java.lang.String r3 = r5.startDate
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004d
        L_0x0028:
            com.dropbox.core.v2.team.DevicesActive r2 = r4.active1Day
            com.dropbox.core.v2.team.DevicesActive r3 = r5.active1Day
            if (r2 == r3) goto L_0x0034
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004d
        L_0x0034:
            com.dropbox.core.v2.team.DevicesActive r2 = r4.active7Day
            com.dropbox.core.v2.team.DevicesActive r3 = r5.active7Day
            if (r2 == r3) goto L_0x0040
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004d
        L_0x0040:
            com.dropbox.core.v2.team.DevicesActive r2 = r4.active28Day
            com.dropbox.core.v2.team.DevicesActive r5 = r5.active28Day
            if (r2 == r5) goto L_0x004e
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x004d
            goto L_0x004e
        L_0x004d:
            r0 = 0
        L_0x004e:
            return r0
        L_0x004f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.GetDevicesReport.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
