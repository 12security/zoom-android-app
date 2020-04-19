package com.dropbox.core.p005v2.files;

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
import java.util.Date;

/* renamed from: com.dropbox.core.v2.files.PhotoMetadata */
public class PhotoMetadata extends MediaMetadata {

    /* renamed from: com.dropbox.core.v2.files.PhotoMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.files.MediaMetadata.Builder {
        protected Builder() {
        }

        public Builder withDimensions(Dimensions dimensions) {
            super.withDimensions(dimensions);
            return this;
        }

        public Builder withLocation(GpsCoordinates gpsCoordinates) {
            super.withLocation(gpsCoordinates);
            return this;
        }

        public Builder withTimeTaken(Date date) {
            super.withTimeTaken(date);
            return this;
        }

        public PhotoMetadata build() {
            return new PhotoMetadata(this.dimensions, this.location, this.timeTaken);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.PhotoMetadata$Serializer */
    static class Serializer extends StructSerializer<PhotoMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PhotoMetadata photoMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("photo", jsonGenerator);
            if (photoMetadata.dimensions != null) {
                jsonGenerator.writeFieldName("dimensions");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(photoMetadata.dimensions, jsonGenerator);
            }
            if (photoMetadata.location != null) {
                jsonGenerator.writeFieldName(Param.LOCATION);
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(photoMetadata.location, jsonGenerator);
            }
            if (photoMetadata.timeTaken != null) {
                jsonGenerator.writeFieldName("time_taken");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(photoMetadata.timeTaken, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PhotoMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Dimensions dimensions = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("photo".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                GpsCoordinates gpsCoordinates = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("dimensions".equals(currentName)) {
                        dimensions = (Dimensions) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if (Param.LOCATION.equals(currentName)) {
                        gpsCoordinates = (GpsCoordinates) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("time_taken".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                PhotoMetadata photoMetadata = new PhotoMetadata(dimensions, gpsCoordinates, date);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(photoMetadata, photoMetadata.toStringMultiline());
                return photoMetadata;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PhotoMetadata(Dimensions dimensions, GpsCoordinates gpsCoordinates, Date date) {
        super(dimensions, gpsCoordinates, date);
    }

    public PhotoMetadata() {
        this(null, null, null);
    }

    public Dimensions getDimensions() {
        return this.dimensions;
    }

    public GpsCoordinates getLocation() {
        return this.location;
    }

    public Date getTimeTaken() {
        return this.timeTaken;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return getClass().toString().hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        PhotoMetadata photoMetadata = (PhotoMetadata) obj;
        if ((this.dimensions != photoMetadata.dimensions && (this.dimensions == null || !this.dimensions.equals(photoMetadata.dimensions))) || ((this.location != photoMetadata.location && (this.location == null || !this.location.equals(photoMetadata.location))) || (this.timeTaken != photoMetadata.timeTaken && (this.timeTaken == null || !this.timeTaken.equals(photoMetadata.timeTaken))))) {
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
