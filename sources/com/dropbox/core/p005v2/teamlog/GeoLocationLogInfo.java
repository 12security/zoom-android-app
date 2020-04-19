package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxEnterpriseEvent;
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

/* renamed from: com.dropbox.core.v2.teamlog.GeoLocationLogInfo */
public class GeoLocationLogInfo {
    protected final String city;
    protected final String country;
    protected final String ipAddress;
    protected final String region;

    /* renamed from: com.dropbox.core.v2.teamlog.GeoLocationLogInfo$Builder */
    public static class Builder {
        protected String city;
        protected String country;
        protected final String ipAddress;
        protected String region;

        protected Builder(String str) {
            if (str != null) {
                this.ipAddress = str;
                this.city = null;
                this.region = null;
                this.country = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'ipAddress' is null");
        }

        public Builder withCity(String str) {
            this.city = str;
            return this;
        }

        public Builder withRegion(String str) {
            this.region = str;
            return this;
        }

        public Builder withCountry(String str) {
            this.country = str;
            return this;
        }

        public GeoLocationLogInfo build() {
            return new GeoLocationLogInfo(this.ipAddress, this.city, this.region, this.country);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.GeoLocationLogInfo$Serializer */
    static class Serializer extends StructSerializer<GeoLocationLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GeoLocationLogInfo geoLocationLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxEnterpriseEvent.FIELD_IP_ADDRESS);
            StoneSerializers.string().serialize(geoLocationLogInfo.ipAddress, jsonGenerator);
            if (geoLocationLogInfo.city != null) {
                jsonGenerator.writeFieldName("city");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(geoLocationLogInfo.city, jsonGenerator);
            }
            if (geoLocationLogInfo.region != null) {
                jsonGenerator.writeFieldName("region");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(geoLocationLogInfo.region, jsonGenerator);
            }
            if (geoLocationLogInfo.country != null) {
                jsonGenerator.writeFieldName("country");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(geoLocationLogInfo.country, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GeoLocationLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                String str4 = null;
                String str5 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxEnterpriseEvent.FIELD_IP_ADDRESS.equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("city".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("region".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("country".equals(currentName)) {
                        str5 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    GeoLocationLogInfo geoLocationLogInfo = new GeoLocationLogInfo(str2, str3, str4, str5);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(geoLocationLogInfo, geoLocationLogInfo.toStringMultiline());
                    return geoLocationLogInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"ip_address\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GeoLocationLogInfo(String str, String str2, String str3, String str4) {
        this.city = str2;
        this.region = str3;
        this.country = str4;
        if (str != null) {
            this.ipAddress = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'ipAddress' is null");
    }

    public GeoLocationLogInfo(String str) {
        this(str, null, null, null);
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getCity() {
        return this.city;
    }

    public String getRegion() {
        return this.region;
    }

    public String getCountry() {
        return this.country;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.city, this.region, this.country, this.ipAddress});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004c, code lost:
        if (r2.equals(r5) == false) goto L_0x004f;
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
            if (r2 == 0) goto L_0x0051
            com.dropbox.core.v2.teamlog.GeoLocationLogInfo r5 = (com.dropbox.core.p005v2.teamlog.GeoLocationLogInfo) r5
            java.lang.String r2 = r4.ipAddress
            java.lang.String r3 = r5.ipAddress
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0024:
            java.lang.String r2 = r4.city
            java.lang.String r3 = r5.city
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x004f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0032:
            java.lang.String r2 = r4.region
            java.lang.String r3 = r5.region
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x004f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x004f
        L_0x0040:
            java.lang.String r2 = r4.country
            java.lang.String r5 = r5.country
            if (r2 == r5) goto L_0x0050
            if (r2 == 0) goto L_0x004f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x004f
            goto L_0x0050
        L_0x004f:
            r0 = 0
        L_0x0050:
            return r0
        L_0x0051:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.GeoLocationLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
