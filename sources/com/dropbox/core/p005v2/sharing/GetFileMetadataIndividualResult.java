package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.BoxApiMetadata;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.GetFileMetadataIndividualResult */
public final class GetFileMetadataIndividualResult {
    public static final GetFileMetadataIndividualResult OTHER = new GetFileMetadataIndividualResult().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public SharedFileMetadata metadataValue;

    /* renamed from: com.dropbox.core.v2.sharing.GetFileMetadataIndividualResult$Serializer */
    static class Serializer extends UnionSerializer<GetFileMetadataIndividualResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetFileMetadataIndividualResult getFileMetadataIndividualResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (getFileMetadataIndividualResult.tag()) {
                case METADATA:
                    jsonGenerator.writeStartObject();
                    writeTag(BoxApiMetadata.BOX_API_METADATA, jsonGenerator);
                    Serializer.INSTANCE.serialize(getFileMetadataIndividualResult.metadataValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(getFileMetadataIndividualResult.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public GetFileMetadataIndividualResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            GetFileMetadataIndividualResult getFileMetadataIndividualResult;
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
                if (BoxApiMetadata.BOX_API_METADATA.equals(str)) {
                    getFileMetadataIndividualResult = GetFileMetadataIndividualResult.metadata(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    getFileMetadataIndividualResult = GetFileMetadataIndividualResult.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    getFileMetadataIndividualResult = GetFileMetadataIndividualResult.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return getFileMetadataIndividualResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.GetFileMetadataIndividualResult$Tag */
    public enum Tag {
        METADATA,
        ACCESS_ERROR,
        OTHER
    }

    private GetFileMetadataIndividualResult() {
    }

    private GetFileMetadataIndividualResult withTag(Tag tag) {
        GetFileMetadataIndividualResult getFileMetadataIndividualResult = new GetFileMetadataIndividualResult();
        getFileMetadataIndividualResult._tag = tag;
        return getFileMetadataIndividualResult;
    }

    private GetFileMetadataIndividualResult withTagAndMetadata(Tag tag, SharedFileMetadata sharedFileMetadata) {
        GetFileMetadataIndividualResult getFileMetadataIndividualResult = new GetFileMetadataIndividualResult();
        getFileMetadataIndividualResult._tag = tag;
        getFileMetadataIndividualResult.metadataValue = sharedFileMetadata;
        return getFileMetadataIndividualResult;
    }

    private GetFileMetadataIndividualResult withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        GetFileMetadataIndividualResult getFileMetadataIndividualResult = new GetFileMetadataIndividualResult();
        getFileMetadataIndividualResult._tag = tag;
        getFileMetadataIndividualResult.accessErrorValue = sharingFileAccessError;
        return getFileMetadataIndividualResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isMetadata() {
        return this._tag == Tag.METADATA;
    }

    public static GetFileMetadataIndividualResult metadata(SharedFileMetadata sharedFileMetadata) {
        if (sharedFileMetadata != null) {
            return new GetFileMetadataIndividualResult().withTagAndMetadata(Tag.METADATA, sharedFileMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharedFileMetadata getMetadataValue() {
        if (this._tag == Tag.METADATA) {
            return this.metadataValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.METADATA, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static GetFileMetadataIndividualResult accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new GetFileMetadataIndividualResult().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharingFileAccessError getAccessErrorValue() {
        if (this._tag == Tag.ACCESS_ERROR) {
            return this.accessErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ACCESS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.metadataValue, this.accessErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GetFileMetadataIndividualResult)) {
            return false;
        }
        GetFileMetadataIndividualResult getFileMetadataIndividualResult = (GetFileMetadataIndividualResult) obj;
        if (this._tag != getFileMetadataIndividualResult._tag) {
            return false;
        }
        switch (this._tag) {
            case METADATA:
                SharedFileMetadata sharedFileMetadata = this.metadataValue;
                SharedFileMetadata sharedFileMetadata2 = getFileMetadataIndividualResult.metadataValue;
                if (sharedFileMetadata != sharedFileMetadata2 && !sharedFileMetadata.equals(sharedFileMetadata2)) {
                    z = false;
                }
                return z;
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = getFileMetadataIndividualResult.accessErrorValue;
                if (sharingFileAccessError != sharingFileAccessError2 && !sharingFileAccessError.equals(sharingFileAccessError2)) {
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
