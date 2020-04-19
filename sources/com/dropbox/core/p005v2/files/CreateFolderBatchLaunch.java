package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.CreateFolderBatchLaunch */
public final class CreateFolderBatchLaunch {
    public static final CreateFolderBatchLaunch OTHER = new CreateFolderBatchLaunch().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;
    /* access modifiers changed from: private */
    public CreateFolderBatchResult completeValue;

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchLaunch$Serializer */
    static class Serializer extends UnionSerializer<CreateFolderBatchLaunch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateFolderBatchLaunch createFolderBatchLaunch, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (createFolderBatchLaunch.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(createFolderBatchLaunch.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(createFolderBatchLaunch.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public CreateFolderBatchLaunch deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            CreateFolderBatchLaunch createFolderBatchLaunch;
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
                if ("async_job_id".equals(str)) {
                    expectField("async_job_id", jsonParser);
                    createFolderBatchLaunch = CreateFolderBatchLaunch.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    createFolderBatchLaunch = CreateFolderBatchLaunch.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    createFolderBatchLaunch = CreateFolderBatchLaunch.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return createFolderBatchLaunch;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchLaunch$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE,
        OTHER
    }

    private CreateFolderBatchLaunch() {
    }

    private CreateFolderBatchLaunch withTag(Tag tag) {
        CreateFolderBatchLaunch createFolderBatchLaunch = new CreateFolderBatchLaunch();
        createFolderBatchLaunch._tag = tag;
        return createFolderBatchLaunch;
    }

    private CreateFolderBatchLaunch withTagAndAsyncJobId(Tag tag, String str) {
        CreateFolderBatchLaunch createFolderBatchLaunch = new CreateFolderBatchLaunch();
        createFolderBatchLaunch._tag = tag;
        createFolderBatchLaunch.asyncJobIdValue = str;
        return createFolderBatchLaunch;
    }

    private CreateFolderBatchLaunch withTagAndComplete(Tag tag, CreateFolderBatchResult createFolderBatchResult) {
        CreateFolderBatchLaunch createFolderBatchLaunch = new CreateFolderBatchLaunch();
        createFolderBatchLaunch._tag = tag;
        createFolderBatchLaunch.completeValue = createFolderBatchResult;
        return createFolderBatchLaunch;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static CreateFolderBatchLaunch asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new CreateFolderBatchLaunch().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
        } else {
            throw new IllegalArgumentException("String is shorter than 1");
        }
    }

    public String getAsyncJobIdValue() {
        if (this._tag == Tag.ASYNC_JOB_ID) {
            return this.asyncJobIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ASYNC_JOB_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isComplete() {
        return this._tag == Tag.COMPLETE;
    }

    public static CreateFolderBatchLaunch complete(CreateFolderBatchResult createFolderBatchResult) {
        if (createFolderBatchResult != null) {
            return new CreateFolderBatchLaunch().withTagAndComplete(Tag.COMPLETE, createFolderBatchResult);
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

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.asyncJobIdValue, this.completeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof CreateFolderBatchLaunch)) {
            return false;
        }
        CreateFolderBatchLaunch createFolderBatchLaunch = (CreateFolderBatchLaunch) obj;
        if (this._tag != createFolderBatchLaunch._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = createFolderBatchLaunch.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
                CreateFolderBatchResult createFolderBatchResult = this.completeValue;
                CreateFolderBatchResult createFolderBatchResult2 = createFolderBatchLaunch.completeValue;
                if (createFolderBatchResult != createFolderBatchResult2 && !createFolderBatchResult.equals(createFolderBatchResult2)) {
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
