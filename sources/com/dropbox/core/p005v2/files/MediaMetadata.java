package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.files.MediaMetadata */
public class MediaMetadata {
    protected final Dimensions dimensions;
    protected final GpsCoordinates location;
    protected final Date timeTaken;

    /* renamed from: com.dropbox.core.v2.files.MediaMetadata$Builder */
    public static class Builder {
        protected Dimensions dimensions = null;
        protected GpsCoordinates location = null;
        protected Date timeTaken = null;

        protected Builder() {
        }

        public Builder withDimensions(Dimensions dimensions2) {
            this.dimensions = dimensions2;
            return this;
        }

        public Builder withLocation(GpsCoordinates gpsCoordinates) {
            this.location = gpsCoordinates;
            return this;
        }

        public Builder withTimeTaken(Date date) {
            this.timeTaken = LangUtil.truncateMillis(date);
            return this;
        }

        public MediaMetadata build() {
            return new MediaMetadata(this.dimensions, this.location, this.timeTaken);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.MediaMetadata$Serializer */
    static class Serializer extends StructSerializer<MediaMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MediaMetadata mediaMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (mediaMetadata instanceof PhotoMetadata) {
                Serializer.INSTANCE.serialize((PhotoMetadata) mediaMetadata, jsonGenerator, z);
            } else if (mediaMetadata instanceof VideoMetadata) {
                Serializer.INSTANCE.serialize((VideoMetadata) mediaMetadata, jsonGenerator, z);
            } else {
                if (!z) {
                    jsonGenerator.writeStartObject();
                }
                if (mediaMetadata.dimensions != null) {
                    jsonGenerator.writeFieldName("dimensions");
                    StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(mediaMetadata.dimensions, jsonGenerator);
                }
                if (mediaMetadata.location != null) {
                    jsonGenerator.writeFieldName(Param.LOCATION);
                    StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(mediaMetadata.location, jsonGenerator);
                }
                if (mediaMetadata.timeTaken != null) {
                    jsonGenerator.writeFieldName("time_taken");
                    StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(mediaMetadata.timeTaken, jsonGenerator);
                }
                if (!z) {
                    jsonGenerator.writeEndObject();
                }
            }
        }

        public MediaMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            MediaMetadata mediaMetadata;
            Dimensions dimensions = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("".equals(str)) {
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
                mediaMetadata = new MediaMetadata(dimensions, gpsCoordinates, date);
            } else if ("".equals(str)) {
                mediaMetadata = INSTANCE.deserialize(jsonParser, true);
            } else if ("photo".equals(str)) {
                mediaMetadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else if ("video".equals(str)) {
                mediaMetadata = Serializer.INSTANCE.deserialize(jsonParser, true);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
            if (!z) {
                expectEndObject(jsonParser);
            }
            StoneDeserializerLogger.log(mediaMetadata, mediaMetadata.toStringMultiline());
            return mediaMetadata;
        }
    }

    public MediaMetadata(Dimensions dimensions2, GpsCoordinates gpsCoordinates, Date date) {
        this.dimensions = dimensions2;
        this.location = gpsCoordinates;
        this.timeTaken = LangUtil.truncateMillis(date);
    }

    public MediaMetadata() {
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
        return Arrays.hashCode(new Object[]{this.dimensions, this.location, this.timeTaken});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r2.equals(r5) == false) goto L_0x0043;
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
            if (r2 == 0) goto L_0x0045
            com.dropbox.core.v2.files.MediaMetadata r5 = (com.dropbox.core.p005v2.files.MediaMetadata) r5
            com.dropbox.core.v2.files.Dimensions r2 = r4.dimensions
            com.dropbox.core.v2.files.Dimensions r3 = r5.dimensions
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0026:
            com.dropbox.core.v2.files.GpsCoordinates r2 = r4.location
            com.dropbox.core.v2.files.GpsCoordinates r3 = r5.location
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0034:
            java.util.Date r2 = r4.timeTaken
            java.util.Date r5 = r5.timeTaken
            if (r2 == r5) goto L_0x0044
            if (r2 == 0) goto L_0x0043
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.MediaMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
