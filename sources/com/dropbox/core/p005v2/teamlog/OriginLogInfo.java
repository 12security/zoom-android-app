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

/* renamed from: com.dropbox.core.v2.teamlog.OriginLogInfo */
public class OriginLogInfo {
    protected final AccessMethodLogInfo accessMethod;
    protected final GeoLocationLogInfo geoLocation;

    /* renamed from: com.dropbox.core.v2.teamlog.OriginLogInfo$Serializer */
    static class Serializer extends StructSerializer<OriginLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(OriginLogInfo originLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("access_method");
            Serializer.INSTANCE.serialize(originLogInfo.accessMethod, jsonGenerator);
            if (originLogInfo.geoLocation != null) {
                jsonGenerator.writeFieldName("geo_location");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(originLogInfo.geoLocation, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public OriginLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AccessMethodLogInfo accessMethodLogInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                GeoLocationLogInfo geoLocationLogInfo = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("access_method".equals(currentName)) {
                        accessMethodLogInfo = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("geo_location".equals(currentName)) {
                        geoLocationLogInfo = (GeoLocationLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (accessMethodLogInfo != null) {
                    OriginLogInfo originLogInfo = new OriginLogInfo(accessMethodLogInfo, geoLocationLogInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(originLogInfo, originLogInfo.toStringMultiline());
                    return originLogInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"access_method\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public OriginLogInfo(AccessMethodLogInfo accessMethodLogInfo, GeoLocationLogInfo geoLocationLogInfo) {
        this.geoLocation = geoLocationLogInfo;
        if (accessMethodLogInfo != null) {
            this.accessMethod = accessMethodLogInfo;
            return;
        }
        throw new IllegalArgumentException("Required value for 'accessMethod' is null");
    }

    public OriginLogInfo(AccessMethodLogInfo accessMethodLogInfo) {
        this(accessMethodLogInfo, null);
    }

    public AccessMethodLogInfo getAccessMethod() {
        return this.accessMethod;
    }

    public GeoLocationLogInfo getGeoLocation() {
        return this.geoLocation;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.geoLocation, this.accessMethod});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.teamlog.OriginLogInfo r5 = (com.dropbox.core.p005v2.teamlog.OriginLogInfo) r5
            com.dropbox.core.v2.teamlog.AccessMethodLogInfo r2 = r4.accessMethod
            com.dropbox.core.v2.teamlog.AccessMethodLogInfo r3 = r5.accessMethod
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.teamlog.GeoLocationLogInfo r2 = r4.geoLocation
            com.dropbox.core.v2.teamlog.GeoLocationLogInfo r5 = r5.geoLocation
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.OriginLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
