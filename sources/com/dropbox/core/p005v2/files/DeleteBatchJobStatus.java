package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.DeleteBatchJobStatus */
public final class DeleteBatchJobStatus {
    public static final DeleteBatchJobStatus IN_PROGRESS = new DeleteBatchJobStatus().withTag(Tag.IN_PROGRESS);
    public static final DeleteBatchJobStatus OTHER = new DeleteBatchJobStatus().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public DeleteBatchResult completeValue;
    /* access modifiers changed from: private */
    public DeleteBatchError failedValue;

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchJobStatus$Serializer */
    static class Serializer extends UnionSerializer<DeleteBatchJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeleteBatchJobStatus deleteBatchJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (deleteBatchJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(deleteBatchJobStatus.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    Serializer.INSTANCE.serialize(deleteBatchJobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public DeleteBatchJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            DeleteBatchJobStatus deleteBatchJobStatus;
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
                    deleteBatchJobStatus = DeleteBatchJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    deleteBatchJobStatus = DeleteBatchJobStatus.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    deleteBatchJobStatus = DeleteBatchJobStatus.failed(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    deleteBatchJobStatus = DeleteBatchJobStatus.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return deleteBatchJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED,
        OTHER
    }

    private DeleteBatchJobStatus() {
    }

    private DeleteBatchJobStatus withTag(Tag tag) {
        DeleteBatchJobStatus deleteBatchJobStatus = new DeleteBatchJobStatus();
        deleteBatchJobStatus._tag = tag;
        return deleteBatchJobStatus;
    }

    private DeleteBatchJobStatus withTagAndComplete(Tag tag, DeleteBatchResult deleteBatchResult) {
        DeleteBatchJobStatus deleteBatchJobStatus = new DeleteBatchJobStatus();
        deleteBatchJobStatus._tag = tag;
        deleteBatchJobStatus.completeValue = deleteBatchResult;
        return deleteBatchJobStatus;
    }

    private DeleteBatchJobStatus withTagAndFailed(Tag tag, DeleteBatchError deleteBatchError) {
        DeleteBatchJobStatus deleteBatchJobStatus = new DeleteBatchJobStatus();
        deleteBatchJobStatus._tag = tag;
        deleteBatchJobStatus.failedValue = deleteBatchError;
        return deleteBatchJobStatus;
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

    public static DeleteBatchJobStatus complete(DeleteBatchResult deleteBatchResult) {
        if (deleteBatchResult != null) {
            return new DeleteBatchJobStatus().withTagAndComplete(Tag.COMPLETE, deleteBatchResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DeleteBatchResult getCompleteValue() {
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

    public static DeleteBatchJobStatus failed(DeleteBatchError deleteBatchError) {
        if (deleteBatchError != null) {
            return new DeleteBatchJobStatus().withTagAndFailed(Tag.FAILED, deleteBatchError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DeleteBatchError getFailedValue() {
        if (this._tag == Tag.FAILED) {
            return this.failedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FAILED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.completeValue, this.failedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof DeleteBatchJobStatus)) {
            return false;
        }
        DeleteBatchJobStatus deleteBatchJobStatus = (DeleteBatchJobStatus) obj;
        if (this._tag != deleteBatchJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                DeleteBatchResult deleteBatchResult = this.completeValue;
                DeleteBatchResult deleteBatchResult2 = deleteBatchJobStatus.completeValue;
                if (deleteBatchResult != deleteBatchResult2 && !deleteBatchResult.equals(deleteBatchResult2)) {
                    z = false;
                }
                return z;
            case FAILED:
                DeleteBatchError deleteBatchError = this.failedValue;
                DeleteBatchError deleteBatchError2 = deleteBatchJobStatus.failedValue;
                if (deleteBatchError != deleteBatchError2 && !deleteBatchError.equals(deleteBatchError2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
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
