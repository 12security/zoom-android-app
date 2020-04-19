package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.CreateFolderBatchJobStatus */
public final class CreateFolderBatchJobStatus {
    public static final CreateFolderBatchJobStatus IN_PROGRESS = new CreateFolderBatchJobStatus().withTag(Tag.IN_PROGRESS);
    public static final CreateFolderBatchJobStatus OTHER = new CreateFolderBatchJobStatus().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public CreateFolderBatchResult completeValue;
    /* access modifiers changed from: private */
    public CreateFolderBatchError failedValue;

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchJobStatus$Serializer */
    static class Serializer extends UnionSerializer<CreateFolderBatchJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateFolderBatchJobStatus createFolderBatchJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (createFolderBatchJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(createFolderBatchJobStatus.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    Serializer.INSTANCE.serialize(createFolderBatchJobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public CreateFolderBatchJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            CreateFolderBatchJobStatus createFolderBatchJobStatus;
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
                    createFolderBatchJobStatus = CreateFolderBatchJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    createFolderBatchJobStatus = CreateFolderBatchJobStatus.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    createFolderBatchJobStatus = CreateFolderBatchJobStatus.failed(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    createFolderBatchJobStatus = CreateFolderBatchJobStatus.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return createFolderBatchJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED,
        OTHER
    }

    private CreateFolderBatchJobStatus() {
    }

    private CreateFolderBatchJobStatus withTag(Tag tag) {
        CreateFolderBatchJobStatus createFolderBatchJobStatus = new CreateFolderBatchJobStatus();
        createFolderBatchJobStatus._tag = tag;
        return createFolderBatchJobStatus;
    }

    private CreateFolderBatchJobStatus withTagAndComplete(Tag tag, CreateFolderBatchResult createFolderBatchResult) {
        CreateFolderBatchJobStatus createFolderBatchJobStatus = new CreateFolderBatchJobStatus();
        createFolderBatchJobStatus._tag = tag;
        createFolderBatchJobStatus.completeValue = createFolderBatchResult;
        return createFolderBatchJobStatus;
    }

    private CreateFolderBatchJobStatus withTagAndFailed(Tag tag, CreateFolderBatchError createFolderBatchError) {
        CreateFolderBatchJobStatus createFolderBatchJobStatus = new CreateFolderBatchJobStatus();
        createFolderBatchJobStatus._tag = tag;
        createFolderBatchJobStatus.failedValue = createFolderBatchError;
        return createFolderBatchJobStatus;
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

    public static CreateFolderBatchJobStatus complete(CreateFolderBatchResult createFolderBatchResult) {
        if (createFolderBatchResult != null) {
            return new CreateFolderBatchJobStatus().withTagAndComplete(Tag.COMPLETE, createFolderBatchResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public CreateFolderBatchResult getCompleteValue() {
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

    public static CreateFolderBatchJobStatus failed(CreateFolderBatchError createFolderBatchError) {
        if (createFolderBatchError != null) {
            return new CreateFolderBatchJobStatus().withTagAndFailed(Tag.FAILED, createFolderBatchError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public CreateFolderBatchError getFailedValue() {
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
        if (obj == null || !(obj instanceof CreateFolderBatchJobStatus)) {
            return false;
        }
        CreateFolderBatchJobStatus createFolderBatchJobStatus = (CreateFolderBatchJobStatus) obj;
        if (this._tag != createFolderBatchJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                CreateFolderBatchResult createFolderBatchResult = this.completeValue;
                CreateFolderBatchResult createFolderBatchResult2 = createFolderBatchJobStatus.completeValue;
                if (createFolderBatchResult != createFolderBatchResult2 && !createFolderBatchResult.equals(createFolderBatchResult2)) {
                    z = false;
                }
                return z;
            case FAILED:
                CreateFolderBatchError createFolderBatchError = this.failedValue;
                CreateFolderBatchError createFolderBatchError2 = createFolderBatchJobStatus.failedValue;
                if (createFolderBatchError != createFolderBatchError2 && !createFolderBatchError.equals(createFolderBatchError2)) {
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
