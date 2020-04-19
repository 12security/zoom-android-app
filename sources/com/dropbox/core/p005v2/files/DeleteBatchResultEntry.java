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

/* renamed from: com.dropbox.core.v2.files.DeleteBatchResultEntry */
public final class DeleteBatchResultEntry {
    private Tag _tag;
    /* access modifiers changed from: private */
    public DeleteError failureValue;
    /* access modifiers changed from: private */
    public DeleteBatchResultData successValue;

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchResultEntry$Serializer */
    static class Serializer extends UnionSerializer<DeleteBatchResultEntry> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DeleteBatchResultEntry deleteBatchResultEntry, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (deleteBatchResultEntry.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    Serializer.INSTANCE.serialize(deleteBatchResultEntry.successValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILURE:
                    jsonGenerator.writeStartObject();
                    writeTag("failure", jsonGenerator);
                    jsonGenerator.writeFieldName("failure");
                    Serializer.INSTANCE.serialize(deleteBatchResultEntry.failureValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(deleteBatchResultEntry.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public DeleteBatchResultEntry deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            DeleteBatchResultEntry deleteBatchResultEntry;
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
                    deleteBatchResultEntry = DeleteBatchResultEntry.success(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failure".equals(str)) {
                    expectField("failure", jsonParser);
                    deleteBatchResultEntry = DeleteBatchResultEntry.failure(Serializer.INSTANCE.deserialize(jsonParser));
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
                return deleteBatchResultEntry;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.DeleteBatchResultEntry$Tag */
    public enum Tag {
        SUCCESS,
        FAILURE
    }

    private DeleteBatchResultEntry() {
    }

    private DeleteBatchResultEntry withTag(Tag tag) {
        DeleteBatchResultEntry deleteBatchResultEntry = new DeleteBatchResultEntry();
        deleteBatchResultEntry._tag = tag;
        return deleteBatchResultEntry;
    }

    private DeleteBatchResultEntry withTagAndSuccess(Tag tag, DeleteBatchResultData deleteBatchResultData) {
        DeleteBatchResultEntry deleteBatchResultEntry = new DeleteBatchResultEntry();
        deleteBatchResultEntry._tag = tag;
        deleteBatchResultEntry.successValue = deleteBatchResultData;
        return deleteBatchResultEntry;
    }

    private DeleteBatchResultEntry withTagAndFailure(Tag tag, DeleteError deleteError) {
        DeleteBatchResultEntry deleteBatchResultEntry = new DeleteBatchResultEntry();
        deleteBatchResultEntry._tag = tag;
        deleteBatchResultEntry.failureValue = deleteError;
        return deleteBatchResultEntry;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static DeleteBatchResultEntry success(DeleteBatchResultData deleteBatchResultData) {
        if (deleteBatchResultData != null) {
            return new DeleteBatchResultEntry().withTagAndSuccess(Tag.SUCCESS, deleteBatchResultData);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DeleteBatchResultData getSuccessValue() {
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

    public static DeleteBatchResultEntry failure(DeleteError deleteError) {
        if (deleteError != null) {
            return new DeleteBatchResultEntry().withTagAndFailure(Tag.FAILURE, deleteError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public DeleteError getFailureValue() {
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
        if (obj == null || !(obj instanceof DeleteBatchResultEntry)) {
            return false;
        }
        DeleteBatchResultEntry deleteBatchResultEntry = (DeleteBatchResultEntry) obj;
        if (this._tag != deleteBatchResultEntry._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                DeleteBatchResultData deleteBatchResultData = this.successValue;
                DeleteBatchResultData deleteBatchResultData2 = deleteBatchResultEntry.successValue;
                if (deleteBatchResultData != deleteBatchResultData2 && !deleteBatchResultData.equals(deleteBatchResultData2)) {
                    z = false;
                }
                return z;
            case FAILURE:
                DeleteError deleteError = this.failureValue;
                DeleteError deleteError2 = deleteBatchResultEntry.failureValue;
                if (deleteError != deleteError2 && !deleteError.equals(deleteError2)) {
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
