package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchJobStatus */
public final class UploadSessionFinishBatchJobStatus {
    public static final UploadSessionFinishBatchJobStatus IN_PROGRESS = new UploadSessionFinishBatchJobStatus().withTag(Tag.IN_PROGRESS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public UploadSessionFinishBatchResult completeValue;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchJobStatus$Serializer */
    static class Serializer extends UnionSerializer<UploadSessionFinishBatchJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionFinishBatchJobStatus uploadSessionFinishBatchJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (uploadSessionFinishBatchJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(uploadSessionFinishBatchJobStatus.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(uploadSessionFinishBatchJobStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public UploadSessionFinishBatchJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            UploadSessionFinishBatchJobStatus uploadSessionFinishBatchJobStatus;
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
                    uploadSessionFinishBatchJobStatus = UploadSessionFinishBatchJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    uploadSessionFinishBatchJobStatus = UploadSessionFinishBatchJobStatus.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
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
                return uploadSessionFinishBatchJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE
    }

    private UploadSessionFinishBatchJobStatus() {
    }

    private UploadSessionFinishBatchJobStatus withTag(Tag tag) {
        UploadSessionFinishBatchJobStatus uploadSessionFinishBatchJobStatus = new UploadSessionFinishBatchJobStatus();
        uploadSessionFinishBatchJobStatus._tag = tag;
        return uploadSessionFinishBatchJobStatus;
    }

    private UploadSessionFinishBatchJobStatus withTagAndComplete(Tag tag, UploadSessionFinishBatchResult uploadSessionFinishBatchResult) {
        UploadSessionFinishBatchJobStatus uploadSessionFinishBatchJobStatus = new UploadSessionFinishBatchJobStatus();
        uploadSessionFinishBatchJobStatus._tag = tag;
        uploadSessionFinishBatchJobStatus.completeValue = uploadSessionFinishBatchResult;
        return uploadSessionFinishBatchJobStatus;
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

    public static UploadSessionFinishBatchJobStatus complete(UploadSessionFinishBatchResult uploadSessionFinishBatchResult) {
        if (uploadSessionFinishBatchResult != null) {
            return new UploadSessionFinishBatchJobStatus().withTagAndComplete(Tag.COMPLETE, uploadSessionFinishBatchResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UploadSessionFinishBatchResult getCompleteValue() {
        if (this._tag == Tag.COMPLETE) {
            return this.completeValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.COMPLETE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.completeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UploadSessionFinishBatchJobStatus)) {
            return false;
        }
        UploadSessionFinishBatchJobStatus uploadSessionFinishBatchJobStatus = (UploadSessionFinishBatchJobStatus) obj;
        if (this._tag != uploadSessionFinishBatchJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                UploadSessionFinishBatchResult uploadSessionFinishBatchResult = this.completeValue;
                UploadSessionFinishBatchResult uploadSessionFinishBatchResult2 = uploadSessionFinishBatchJobStatus.completeValue;
                if (uploadSessionFinishBatchResult != uploadSessionFinishBatchResult2 && !uploadSessionFinishBatchResult.equals(uploadSessionFinishBatchResult2)) {
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
