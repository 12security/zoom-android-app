package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.RelocationBatchJobStatus */
public final class RelocationBatchJobStatus {
    public static final RelocationBatchJobStatus IN_PROGRESS = new RelocationBatchJobStatus().withTag(Tag.IN_PROGRESS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public RelocationBatchResult completeValue;
    /* access modifiers changed from: private */
    public RelocationBatchError failedValue;

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchJobStatus$Serializer */
    static class Serializer extends UnionSerializer<RelocationBatchJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelocationBatchJobStatus relocationBatchJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (relocationBatchJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(relocationBatchJobStatus.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    Serializer.INSTANCE.serialize(relocationBatchJobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(relocationBatchJobStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public RelocationBatchJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            RelocationBatchJobStatus relocationBatchJobStatus;
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
                    relocationBatchJobStatus = RelocationBatchJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    relocationBatchJobStatus = RelocationBatchJobStatus.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    relocationBatchJobStatus = RelocationBatchJobStatus.failed(Serializer.INSTANCE.deserialize(jsonParser));
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
                return relocationBatchJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED
    }

    private RelocationBatchJobStatus() {
    }

    private RelocationBatchJobStatus withTag(Tag tag) {
        RelocationBatchJobStatus relocationBatchJobStatus = new RelocationBatchJobStatus();
        relocationBatchJobStatus._tag = tag;
        return relocationBatchJobStatus;
    }

    private RelocationBatchJobStatus withTagAndComplete(Tag tag, RelocationBatchResult relocationBatchResult) {
        RelocationBatchJobStatus relocationBatchJobStatus = new RelocationBatchJobStatus();
        relocationBatchJobStatus._tag = tag;
        relocationBatchJobStatus.completeValue = relocationBatchResult;
        return relocationBatchJobStatus;
    }

    private RelocationBatchJobStatus withTagAndFailed(Tag tag, RelocationBatchError relocationBatchError) {
        RelocationBatchJobStatus relocationBatchJobStatus = new RelocationBatchJobStatus();
        relocationBatchJobStatus._tag = tag;
        relocationBatchJobStatus.failedValue = relocationBatchError;
        return relocationBatchJobStatus;
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

    public static RelocationBatchJobStatus complete(RelocationBatchResult relocationBatchResult) {
        if (relocationBatchResult != null) {
            return new RelocationBatchJobStatus().withTagAndComplete(Tag.COMPLETE, relocationBatchResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RelocationBatchResult getCompleteValue() {
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

    public static RelocationBatchJobStatus failed(RelocationBatchError relocationBatchError) {
        if (relocationBatchError != null) {
            return new RelocationBatchJobStatus().withTagAndFailed(Tag.FAILED, relocationBatchError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RelocationBatchError getFailedValue() {
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
        if (obj == null || !(obj instanceof RelocationBatchJobStatus)) {
            return false;
        }
        RelocationBatchJobStatus relocationBatchJobStatus = (RelocationBatchJobStatus) obj;
        if (this._tag != relocationBatchJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                RelocationBatchResult relocationBatchResult = this.completeValue;
                RelocationBatchResult relocationBatchResult2 = relocationBatchJobStatus.completeValue;
                if (relocationBatchResult != relocationBatchResult2 && !relocationBatchResult.equals(relocationBatchResult2)) {
                    z = false;
                }
                return z;
            case FAILED:
                RelocationBatchError relocationBatchError = this.failedValue;
                RelocationBatchError relocationBatchError2 = relocationBatchJobStatus.failedValue;
                if (relocationBatchError != relocationBatchError2 && !relocationBatchError.equals(relocationBatchError2)) {
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
