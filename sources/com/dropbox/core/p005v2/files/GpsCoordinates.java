package com.dropbox.core.p005v2.files;

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

/* renamed from: com.dropbox.core.v2.files.GpsCoordinates */
public class GpsCoordinates {
    protected final double latitude;
    protected final double longitude;

    /* renamed from: com.dropbox.core.v2.files.GpsCoordinates$Serializer */
    static class Serializer extends StructSerializer<GpsCoordinates> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GpsCoordinates gpsCoordinates, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("latitude");
            StoneSerializers.float64().serialize(Double.valueOf(gpsCoordinates.latitude), jsonGenerator);
            jsonGenerator.writeFieldName("longitude");
            StoneSerializers.float64().serialize(Double.valueOf(gpsCoordinates.longitude), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GpsCoordinates deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Double d = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Double d2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("latitude".equals(currentName)) {
                        d = (Double) StoneSerializers.float64().deserialize(jsonParser);
                    } else if ("longitude".equals(currentName)) {
                        d2 = (Double) StoneSerializers.float64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (d == null) {
                    throw new JsonParseException(jsonParser, "Required field \"latitude\" missing.");
                } else if (d2 != null) {
                    GpsCoordinates gpsCoordinates = new GpsCoordinates(d.doubleValue(), d2.doubleValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(gpsCoordinates, gpsCoordinates.toStringMultiline());
                    return gpsCoordinates;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"longitude\" missing.");
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

    public GpsCoordinates(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Double.valueOf(this.latitude), Double.valueOf(this.longitude)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        GpsCoordinates gpsCoordinates = (GpsCoordinates) obj;
        if (!(this.latitude == gpsCoordinates.latitude && this.longitude == gpsCoordinates.longitude)) {
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
