package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchResultEntry */
public final class UploadSessionFinishBatchResultEntry {
    private Tag _tag;
    /* access modifiers changed from: private */
    public UploadSessionFinishError failureValue;
    /* access modifiers changed from: private */
    public FileMetadata successValue;

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchResultEntry$Serializer */
    static class Serializer extends UnionSerializer<UploadSessionFinishBatchResultEntry> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UploadSessionFinishBatchResultEntry uploadSessionFinishBatchResultEntry, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (uploadSessionFinishBatchResultEntry.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    Serializer.INSTANCE.serialize(uploadSessionFinishBatchResultEntry.successValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILURE:
                    jsonGenerator.writeStartObject();
                    writeTag("failure", jsonGenerator);
                    jsonGenerator.writeFieldName("failure");
                    Serializer.INSTANCE.serialize(uploadSessionFinishBatchResultEntry.failureValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(uploadSessionFinishBatchResultEntry.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public UploadSessionFinishBatchResultEntry deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            UploadSessionFinishBatchResultEntry uploadSessionFinishBatchResultEntry;
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
                if (Param.SUCCESS.equals(str)) {
                    uploadSessionFinishBatchResultEntry = UploadSessionFinishBatchResultEntry.success(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failure".equals(str)) {
                    expectField("failure", jsonParser);
                    uploadSessionFinishBatchResultEntry = UploadSessionFinishBatchResultEntry.failure(Serializer.INSTANCE.deserialize(jsonParser));
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
                return uploadSessionFinishBatchResultEntry;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.UploadSessionFinishBatchResultEntry$Tag */
    public enum Tag {
        SUCCESS,
        FAILURE
    }

    private UploadSessionFinishBatchResultEntry() {
    }

    private UploadSessionFinishBatchResultEntry withTag(Tag tag) {
        UploadSessionFinishBatchResultEntry uploadSessionFinishBatchResultEntry = new UploadSessionFinishBatchResultEntry();
        uploadSessionFinishBatchResultEntry._tag = tag;
        return uploadSessionFinishBatchResultEntry;
    }

    private UploadSessionFinishBatchResultEntry withTagAndSuccess(Tag tag, FileMetadata fileMetadata) {
        UploadSessionFinishBatchResultEntry uploadSessionFinishBatchResultEntry = new UploadSessionFinishBatchResultEntry();
        uploadSessionFinishBatchResultEntry._tag = tag;
        uploadSessionFinishBatchResultEntry.successValue = fileMetadata;
        return uploadSessionFinishBatchResultEntry;
    }

    private UploadSessionFinishBatchResultEntry withTagAndFailure(Tag tag, UploadSessionFinishError uploadSessionFinishError) {
        UploadSessionFinishBatchResultEntry uploadSessionFinishBatchResultEntry = new UploadSessionFinishBatchResultEntry();
        uploadSessionFinishBatchResultEntry._tag = tag;
        uploadSessionFinishBatchResultEntry.failureValue = uploadSessionFinishError;
        return uploadSessionFinishBatchResultEntry;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static UploadSessionFinishBatchResultEntry success(FileMetadata fileMetadata) {
        if (fileMetadata != null) {
            return new UploadSessionFinishBatchResultEntry().withTagAndSuccess(Tag.SUCCESS, fileMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public FileMetadata getSuccessValue() {
        if (this._tag == Tag.SUCCESS) {
            return this.successValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SUCCESS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isFailure() {
        return this._tag == Tag.FAILURE;
    }

    public static UploadSessionFinishBatchResultEntry failure(UploadSessionFinishError uploadSessionFinishError) {
        if (uploadSessionFinishError != null) {
            return new UploadSessionFinishBatchResultEntry().withTagAndFailure(Tag.FAILURE, uploadSessionFinishError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public UploadSessionFinishError getFailureValue() {
        if (this._tag == Tag.FAILURE) {
            return this.failureValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FAILURE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.successValue, this.failureValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UploadSessionFinishBatchResultEntry)) {
            return false;
        }
        UploadSessionFinishBatchResultEntry uploadSessionFinishBatchResultEntry = (UploadSessionFinishBatchResultEntry) obj;
        if (this._tag != uploadSessionFinishBatchResultEntry._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                FileMetadata fileMetadata = this.successValue;
                FileMetadata fileMetadata2 = uploadSessionFinishBatchResultEntry.successValue;
                if (fileMetadata != fileMetadata2 && !fileMetadata.equals(fileMetadata2)) {
                    z = false;
                }
                return z;
            case FAILURE:
                UploadSessionFinishError uploadSessionFinishError = this.failureValue;
                UploadSessionFinishError uploadSessionFinishError2 = uploadSessionFinishBatchResultEntry.failureValue;
                if (uploadSessionFinishError != uploadSessionFinishError2 && !uploadSessionFinishError.equals(uploadSessionFinishError2)) {
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
