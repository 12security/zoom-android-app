package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ShareFolderLaunch */
public final class ShareFolderLaunch {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String asyncJobIdValue;
    /* access modifiers changed from: private */
    public SharedFolderMetadata completeValue;

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderLaunch$Serializer */
    static class Serializer extends UnionSerializer<ShareFolderLaunch> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShareFolderLaunch shareFolderLaunch, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (shareFolderLaunch.tag()) {
                case ASYNC_JOB_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("async_job_id", jsonGenerator);
                    jsonGenerator.writeFieldName("async_job_id");
                    StoneSerializers.string().serialize(shareFolderLaunch.asyncJobIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(shareFolderLaunch.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(shareFolderLaunch.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ShareFolderLaunch deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            ShareFolderLaunch shareFolderLaunch;
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
                    shareFolderLaunch = ShareFolderLaunch.asyncJobId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("complete".equals(str)) {
                    shareFolderLaunch = ShareFolderLaunch.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
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
                return shareFolderLaunch;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderLaunch$Tag */
    public enum Tag {
        ASYNC_JOB_ID,
        COMPLETE
    }

    private ShareFolderLaunch() {
    }

    private ShareFolderLaunch withTag(Tag tag) {
        ShareFolderLaunch shareFolderLaunch = new ShareFolderLaunch();
        shareFolderLaunch._tag = tag;
        return shareFolderLaunch;
    }

    private ShareFolderLaunch withTagAndAsyncJobId(Tag tag, String str) {
        ShareFolderLaunch shareFolderLaunch = new ShareFolderLaunch();
        shareFolderLaunch._tag = tag;
        shareFolderLaunch.asyncJobIdValue = str;
        return shareFolderLaunch;
    }

    private ShareFolderLaunch withTagAndComplete(Tag tag, SharedFolderMetadata sharedFolderMetadata) {
        ShareFolderLaunch shareFolderLaunch = new ShareFolderLaunch();
        shareFolderLaunch._tag = tag;
        shareFolderLaunch.completeValue = sharedFolderMetadata;
        return shareFolderLaunch;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAsyncJobId() {
        return this._tag == Tag.ASYNC_JOB_ID;
    }

    public static ShareFolderLaunch asyncJobId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() >= 1) {
            return new ShareFolderLaunch().withTagAndAsyncJobId(Tag.ASYNC_JOB_ID, str);
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

    public static ShareFolderLaunch complete(SharedFolderMetadata sharedFolderMetadata) {
        if (sharedFolderMetadata != null) {
            return new ShareFolderLaunch().withTagAndComplete(Tag.COMPLETE, sharedFolderMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharedFolderMetadata getCompleteValue() {
        if (this._tag == Tag.COMPLETE) {
            return this.completeValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.COMPLETE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.asyncJobIdValue, this.completeValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ShareFolderLaunch)) {
            return false;
        }
        ShareFolderLaunch shareFolderLaunch = (ShareFolderLaunch) obj;
        if (this._tag != shareFolderLaunch._tag) {
            return false;
        }
        switch (this._tag) {
            case ASYNC_JOB_ID:
                String str = this.asyncJobIdValue;
                String str2 = shareFolderLaunch.asyncJobIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case COMPLETE:
                SharedFolderMetadata sharedFolderMetadata = this.completeValue;
                SharedFolderMetadata sharedFolderMetadata2 = shareFolderLaunch.completeValue;
                if (sharedFolderMetadata != sharedFolderMetadata2 && !sharedFolderMetadata.equals(sharedFolderMetadata2)) {
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
