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

/* renamed from: com.dropbox.core.v2.files.GetThumbnailBatchResultEntry */
public final class GetThumbnailBatchResultEntry {
    public static final GetThumbnailBatchResultEntry OTHER = new GetThumbnailBatchResultEntry().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public ThumbnailError failureValue;
    /* access modifiers changed from: private */
    public GetThumbnailBatchResultData successValue;

    /* renamed from: com.dropbox.core.v2.files.GetThumbnailBatchResultEntry$Serializer */
    static class Serializer extends UnionSerializer<GetThumbnailBatchResultEntry> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetThumbnailBatchResultEntry getThumbnailBatchResultEntry, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (getThumbnailBatchResultEntry.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    Serializer.INSTANCE.serialize(getThumbnailBatchResultEntry.successValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILURE:
                    jsonGenerator.writeStartObject();
                    writeTag("failure", jsonGenerator);
                    jsonGenerator.writeFieldName("failure");
                    Serializer.INSTANCE.serialize(getThumbnailBatchResultEntry.failureValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GetThumbnailBatchResultEntry deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            GetThumbnailBatchResultEntry getThumbnailBatchResultEntry;
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
                    getThumbnailBatchResultEntry = GetThumbnailBatchResultEntry.success(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failure".equals(str)) {
                    expectField("failure", jsonParser);
                    getThumbnailBatchResultEntry = GetThumbnailBatchResultEntry.failure(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    getThumbnailBatchResultEntry = GetThumbnailBatchResultEntry.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return getThumbnailBatchResultEntry;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.GetThumbnailBatchResultEntry$Tag */
    public enum Tag {
        SUCCESS,
        FAILURE,
        OTHER
    }

    private GetThumbnailBatchResultEntry() {
    }

    private GetThumbnailBatchResultEntry withTag(Tag tag) {
        GetThumbnailBatchResultEntry getThumbnailBatchResultEntry = new GetThumbnailBatchResultEntry();
        getThumbnailBatchResultEntry._tag = tag;
        return getThumbnailBatchResultEntry;
    }

    private GetThumbnailBatchResultEntry withTagAndSuccess(Tag tag, GetThumbnailBatchResultData getThumbnailBatchResultData) {
        GetThumbnailBatchResultEntry getThumbnailBatchResultEntry = new GetThumbnailBatchResultEntry();
        getThumbnailBatchResultEntry._tag = tag;
        getThumbnailBatchResultEntry.successValue = getThumbnailBatchResultData;
        return getThumbnailBatchResultEntry;
    }

    private GetThumbnailBatchResultEntry withTagAndFailure(Tag tag, ThumbnailError thumbnailError) {
        GetThumbnailBatchResultEntry getThumbnailBatchResultEntry = new GetThumbnailBatchResultEntry();
        getThumbnailBatchResultEntry._tag = tag;
        getThumbnailBatchResultEntry.failureValue = thumbnailError;
        return getThumbnailBatchResultEntry;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static GetThumbnailBatchResultEntry success(GetThumbnailBatchResultData getThumbnailBatchResultData) {
        if (getThumbnailBatchResultData != null) {
            return new GetThumbnailBatchResultEntry().withTagAndSuccess(Tag.SUCCESS, getThumbnailBatchResultData);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public GetThumbnailBatchResultData getSuccessValue() {
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

    public static GetThumbnailBatchResultEntry failure(ThumbnailError thumbnailError) {
        if (thumbnailError != null) {
            return new GetThumbnailBatchResultEntry().withTagAndFailure(Tag.FAILURE, thumbnailError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public ThumbnailError getFailureValue() {
        if (this._tag == Tag.FAILURE) {
            return this.failureValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FAILURE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.successValue, this.failureValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GetThumbnailBatchResultEntry)) {
            return false;
        }
        GetThumbnailBatchResultEntry getThumbnailBatchResultEntry = (GetThumbnailBatchResultEntry) obj;
        if (this._tag != getThumbnailBatchResultEntry._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                GetThumbnailBatchResultData getThumbnailBatchResultData = this.successValue;
                GetThumbnailBatchResultData getThumbnailBatchResultData2 = getThumbnailBatchResultEntry.successValue;
                if (getThumbnailBatchResultData != getThumbnailBatchResultData2 && !getThumbnailBatchResultData.equals(getThumbnailBatchResultData2)) {
                    z = false;
                }
                return z;
            case FAILURE:
                ThumbnailError thumbnailError = this.failureValue;
                ThumbnailError thumbnailError2 = getThumbnailBatchResultEntry.failureValue;
                if (thumbnailError != thumbnailError2 && !thumbnailError.equals(thumbnailError2)) {
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
