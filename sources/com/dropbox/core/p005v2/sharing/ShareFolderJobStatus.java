package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ShareFolderJobStatus */
public final class ShareFolderJobStatus {
    public static final ShareFolderJobStatus IN_PROGRESS = new ShareFolderJobStatus().withTag(Tag.IN_PROGRESS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderMetadata completeValue;
    /* access modifiers changed from: private */
    public ShareFolderError failedValue;

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderJobStatus$Serializer */
    static class Serializer extends UnionSerializer<ShareFolderJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShareFolderJobStatus shareFolderJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (shareFolderJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(shareFolderJobStatus.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    Serializer.INSTANCE.serialize(shareFolderJobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(shareFolderJobStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ShareFolderJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            ShareFolderJobStatus shareFolderJobStatus;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if ("in_progress".equals(str)) {
                    shareFolderJobStatus = ShareFolderJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    shareFolderJobStatus = ShareFolderJobStatus.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    shareFolderJobStatus = ShareFolderJobStatus.failed(Serializer.INSTANCE.deserialize(jsonParser));
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
                return shareFolderJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED
    }

    private ShareFolderJobStatus() {
    }

    private ShareFolderJobStatus withTag(Tag tag) {
        ShareFolderJobStatus shareFolderJobStatus = new ShareFolderJobStatus();
        shareFolderJobStatus._tag = tag;
        return shareFolderJobStatus;
    }

    private ShareFolderJobStatus withTagAndComplete(Tag tag, SharedFolderMetadata sharedFolderMetadata) {
        ShareFolderJobStatus shareFolderJobStatus = new ShareFolderJobStatus();
        shareFolderJobStatus._tag = tag;
        shareFolderJobStatus.completeValue = sharedFolderMetadata;
        return shareFolderJobStatus;
    }

    private ShareFolderJobStatus withTagAndFailed(Tag tag, ShareFolderError shareFolderError) {
        ShareFolderJobStatus shareFolderJobStatus = new ShareFolderJobStatus();
        shareFolderJobStatus._tag = tag;
        shareFolderJobStatus.failedValue = shareFolderError;
        return shareFolderJobStatus;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isInProgress() {
        return this._tag == Tag.IN_PROGRESS;
    }

    public boolean isComplete() {
        return this._tag == Tag.COMPLETE;
    }

    public static ShareFolderJobStatus complete(SharedFolderMetadata sharedFolderMetadata) {
        if (sharedFolderMetadata != null) {
            return new ShareFolderJobStatus().withTagAndComplete(Tag.COMPLETE, sharedFolderMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharedFolderMetadata getCompleteValue() {
        if (this._tag == Tag.COMPLETE) {
            return this.completeValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.COMPLETE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isFailed() {
        return this._tag == Tag.FAILED;
    }

    public static ShareFolderJobStatus failed(ShareFolderError shareFolderError) {
        if (shareFolderError != null) {
            return new ShareFolderJobStatus().withTagAndFailed(Tag.FAILED, shareFolderError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public ShareFolderError getFailedValue() {
        if (this._tag == Tag.FAILED) {
            return this.failedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FAILED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.completeValue, this.failedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ShareFolderJobStatus)) {
            return false;
        }
        ShareFolderJobStatus shareFolderJobStatus = (ShareFolderJobStatus) obj;
        if (this._tag != shareFolderJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                SharedFolderMetadata sharedFolderMetadata = this.completeValue;
                SharedFolderMetadata sharedFolderMetadata2 = shareFolderJobStatus.completeValue;
                if (sharedFolderMetadata != sharedFolderMetadata2 && !sharedFolderMetadata.equals(sharedFolderMetadata2)) {
                    z = false;
                }
                return z;
            case FAILED:
                ShareFolderError shareFolderError = this.failedValue;
                ShareFolderError shareFolderError2 = shareFolderJobStatus.failedValue;
                if (shareFolderError != shareFolderError2 && !shareFolderError.equals(shareFolderError2)) {
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
