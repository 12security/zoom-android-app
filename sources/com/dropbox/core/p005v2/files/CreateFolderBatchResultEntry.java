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

/* renamed from: com.dropbox.core.v2.files.CreateFolderBatchResultEntry */
public final class CreateFolderBatchResultEntry {
    private Tag _tag;
    /* access modifiers changed from: private */
    public CreateFolderEntryError failureValue;
    /* access modifiers changed from: private */
    public CreateFolderEntryResult successValue;

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchResultEntry$Serializer */
    static class Serializer extends UnionSerializer<CreateFolderBatchResultEntry> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateFolderBatchResultEntry createFolderBatchResultEntry, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (createFolderBatchResultEntry.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    Serializer.INSTANCE.serialize(createFolderBatchResultEntry.successValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILURE:
                    jsonGenerator.writeStartObject();
                    writeTag("failure", jsonGenerator);
                    jsonGenerator.writeFieldName("failure");
                    Serializer.INSTANCE.serialize(createFolderBatchResultEntry.failureValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(createFolderBatchResultEntry.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public CreateFolderBatchResultEntry deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            CreateFolderBatchResultEntry createFolderBatchResultEntry;
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
                    createFolderBatchResultEntry = CreateFolderBatchResultEntry.success(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failure".equals(str)) {
                    expectField("failure", jsonParser);
                    createFolderBatchResultEntry = CreateFolderBatchResultEntry.failure(Serializer.INSTANCE.deserialize(jsonParser));
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
                return createFolderBatchResultEntry;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchResultEntry$Tag */
    public enum Tag {
        SUCCESS,
        FAILURE
    }

    private CreateFolderBatchResultEntry() {
    }

    private CreateFolderBatchResultEntry withTag(Tag tag) {
        CreateFolderBatchResultEntry createFolderBatchResultEntry = new CreateFolderBatchResultEntry();
        createFolderBatchResultEntry._tag = tag;
        return createFolderBatchResultEntry;
    }

    private CreateFolderBatchResultEntry withTagAndSuccess(Tag tag, CreateFolderEntryResult createFolderEntryResult) {
        CreateFolderBatchResultEntry createFolderBatchResultEntry = new CreateFolderBatchResultEntry();
        createFolderBatchResultEntry._tag = tag;
        createFolderBatchResultEntry.successValue = createFolderEntryResult;
        return createFolderBatchResultEntry;
    }

    private CreateFolderBatchResultEntry withTagAndFailure(Tag tag, CreateFolderEntryError createFolderEntryError) {
        CreateFolderBatchResultEntry createFolderBatchResultEntry = new CreateFolderBatchResultEntry();
        createFolderBatchResultEntry._tag = tag;
        createFolderBatchResultEntry.failureValue = createFolderEntryError;
        return createFolderBatchResultEntry;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static CreateFolderBatchResultEntry success(CreateFolderEntryResult createFolderEntryResult) {
        if (createFolderEntryResult != null) {
            return new CreateFolderBatchResultEntry().withTagAndSuccess(Tag.SUCCESS, createFolderEntryResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public CreateFolderEntryResult getSuccessValue() {
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

    public static CreateFolderBatchResultEntry failure(CreateFolderEntryError createFolderEntryError) {
        if (createFolderEntryError != null) {
            return new CreateFolderBatchResultEntry().withTagAndFailure(Tag.FAILURE, createFolderEntryError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public CreateFolderEntryError getFailureValue() {
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
        if (obj == null || !(obj instanceof CreateFolderBatchResultEntry)) {
            return false;
        }
        CreateFolderBatchResultEntry createFolderBatchResultEntry = (CreateFolderBatchResultEntry) obj;
        if (this._tag != createFolderBatchResultEntry._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                CreateFolderEntryResult createFolderEntryResult = this.successValue;
                CreateFolderEntryResult createFolderEntryResult2 = createFolderBatchResultEntry.successValue;
                if (createFolderEntryResult != createFolderEntryResult2 && !createFolderEntryResult.equals(createFolderEntryResult2)) {
                    z = false;
                }
                return z;
            case FAILURE:
                CreateFolderEntryError createFolderEntryError = this.failureValue;
                CreateFolderEntryError createFolderEntryError2 = createFolderBatchResultEntry.failureValue;
                if (createFolderEntryError != createFolderEntryError2 && !createFolderEntryError.equals(createFolderEntryError2)) {
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
