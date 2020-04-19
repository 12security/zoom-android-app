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
import java.util.Arrays;
import java.util.Date;

/* renamed from: com.dropbox.core.v2.files.VideoMetadata */
public class VideoMetadata extends MediaMetadata {
    protected final Long duration;

    /* renamed from: com.dropbox.core.v2.files.VideoMetadata$Builder */
    public static class Builder extends com.dropbox.core.p005v2.files.MediaMetadata.Builder {
        protected Long duration = null;

        protected Builder() {
        }

        public Builder withDuration(Long l) {
            this.duration = l;
            return this;
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

        public VideoMetadata build() {
            return new VideoMetadata(this.dimensions, this.location, this.timeTaken, this.duration);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.VideoMetadata$Serializer */
    static class Serializer extends StructSerializer<VideoMetadata> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(VideoMetadata videoMetadata, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            writeTag("video", jsonGenerator);
            if (videoMetadata.dimensions != null) {
                jsonGenerator.writeFieldName("dimensions");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(videoMetadata.dimensions, jsonGenerator);
            }
            if (videoMetadata.location != null) {
                jsonGenerator.writeFieldName(Param.LOCATION);
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(videoMetadata.location, jsonGenerator);
            }
            if (videoMetadata.timeTaken != null) {
                jsonGenerator.writeFieldName("time_taken");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(videoMetadata.timeTaken, jsonGenerator);
            }
            if (videoMetadata.duration != null) {
                jsonGenerator.writeFieldName("duration");
                StoneSerializers.nullable(StoneSerializers.uInt64()).serialize(videoMetadata.duration, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public VideoMetadata deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Dimensions dimensions = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                if ("video".equals(str)) {
                    str = null;
                }
            } else {
                str = null;
            }
            if (str == null) {
                GpsCoordinates gpsCoordinates = null;
                Date date = null;
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("dimensions".equals(currentName)) {
                        dimensions = (Dimensions) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if (Param.LOCATION.equals(currentName)) {
                        gpsCoordinates = (GpsCoordinates) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("time_taken".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("duration".equals(currentName)) {
                        l = (Long) StoneSerializers.nullable(StoneSerializers.uInt64()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                VideoMetadata videoMetadata = new VideoMetadata(dimensions, gpsCoordinates, date, l);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(videoMetadata, videoMetadata.toStringMultiline());
                return videoMetadata;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public VideoMetadata(Dimensions dimensions, GpsCoordinates gpsCoordinates, Date date, Long l) {
        super(dimensions, gpsCoordinates, date);
        this.duration = l;
    }

    public VideoMetadata() {
        this(null, null, null, null);
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

    public Long getDuration() {
        return this.duration;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.duration});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0060, code lost:
        if (r2.equals(r5) == false) goto L_0x0063;
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
            if (r2 == 0) goto L_0x0065
            com.dropbox.core.v2.files.VideoMetadata r5 = (com.dropbox.core.p005v2.files.VideoMetadata) r5
            com.dropbox.core.v2.files.Dimensions r2 = r4.dimensions
            com.dropbox.core.v2.files.Dimensions r3 = r5.dimensions
            if (r2 == r3) goto L_0x002c
            com.dropbox.core.v2.files.Dimensions r2 = r4.dimensions
            if (r2 == 0) goto L_0x0063
            com.dropbox.core.v2.files.Dimensions r2 = r4.dimensions
            com.dropbox.core.v2.files.Dimensions r3 = r5.dimensions
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0063
        L_0x002c:
            com.dropbox.core.v2.files.GpsCoordinates r2 = r4.location
            com.dropbox.core.v2.files.GpsCoordinates r3 = r5.location
            if (r2 == r3) goto L_0x0040
            com.dropbox.core.v2.files.GpsCoordinates r2 = r4.location
            if (r2 == 0) goto L_0x0063
            com.dropbox.core.v2.files.GpsCoordinates r2 = r4.location
            com.dropbox.core.v2.files.GpsCoordinates r3 = r5.location
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0063
        L_0x0040:
            java.util.Date r2 = r4.timeTaken
            java.util.Date r3 = r5.timeTaken
            if (r2 == r3) goto L_0x0054
            java.util.Date r2 = r4.timeTaken
            if (r2 == 0) goto L_0x0063
            java.util.Date r2 = r4.timeTaken
            java.util.Date r3 = r5.timeTaken
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0063
        L_0x0054:
            java.lang.Long r2 = r4.duration
            java.lang.Long r5 = r5.duration
            if (r2 == r5) goto L_0x0064
            if (r2 == 0) goto L_0x0063
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0063
            goto L_0x0064
        L_0x0063:
            r0 = 0
        L_0x0064:
            return r0
        L_0x0065:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.VideoMetadata.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
