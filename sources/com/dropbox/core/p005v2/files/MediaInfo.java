package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.BoxApiMetadata;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.MediaInfo */
public final class MediaInfo {
    public static final MediaInfo PENDING = new MediaInfo().withTag(Tag.PENDING);
    private Tag _tag;
    /* access modifiers changed from: private */
    public MediaMetadata metadataValue;

    /* renamed from: com.dropbox.core.v2.files.MediaInfo$Serializer */
    static class Serializer extends UnionSerializer<MediaInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MediaInfo mediaInfo, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (mediaInfo.tag()) {
                case PENDING:
                    jsonGenerator.writeString("pending");
                    return;
                case METADATA:
                    jsonGenerator.writeStartObject();
                    writeTag(BoxApiMetadata.BOX_API_METADATA, jsonGenerator);
                    jsonGenerator.writeFieldName(BoxApiMetadata.BOX_API_METADATA);
                    Serializer.INSTANCE.serialize(mediaInfo.metadataValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(mediaInfo.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public MediaInfo deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            MediaInfo mediaInfo;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("pending".equals(str)) {
                    mediaInfo = MediaInfo.PENDING;
                } else if (BoxApiMetadata.BOX_API_METADATA.equals(str)) {
                    expectField(BoxApiMetadata.BOX_API_METADATA, jsonParser);
                    mediaInfo = MediaInfo.metadata((MediaMetadata) Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return mediaInfo;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.MediaInfo$Tag */
    public enum Tag {
        PENDING,
        METADATA
    }

    private MediaInfo() {
    }

    private MediaInfo withTag(Tag tag) {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo._tag = tag;
        return mediaInfo;
    }

    private MediaInfo withTagAndMetadata(Tag tag, MediaMetadata mediaMetadata) {
        MediaInfo mediaInfo = new MediaInfo();
        mediaInfo._tag = tag;
        mediaInfo.metadataValue = mediaMetadata;
        return mediaInfo;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPending() {
        return this._tag == Tag.PENDING;
    }

    public boolean isMetadata() {
        return this._tag == Tag.METADATA;
    }

    public static MediaInfo metadata(MediaMetadata mediaMetadata) {
        if (mediaMetadata != null) {
            return new MediaInfo().withTagAndMetadata(Tag.METADATA, mediaMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public MediaMetadata getMetadataValue() {
        if (this._tag == Tag.METADATA) {
            return this.metadataValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.METADATA, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.metadataValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof MediaInfo)) {
            return false;
        }
        MediaInfo mediaInfo = (MediaInfo) obj;
        if (this._tag != mediaInfo._tag) {
            return false;
        }
        switch (this._tag) {
            case PENDING:
                return true;
            case METADATA:
                MediaMetadata mediaMetadata = this.metadataValue;
                MediaMetadata mediaMetadata2 = mediaInfo.metadataValue;
                if (mediaMetadata != mediaMetadata2 && !mediaMetadata.equals(mediaMetadata2)) {
                    z = false;
                }
                return z;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
