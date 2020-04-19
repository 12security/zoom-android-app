package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.RemoveMemberJobStatus */
public final class RemoveMemberJobStatus {
    public static final RemoveMemberJobStatus IN_PROGRESS = new RemoveMemberJobStatus().withTag(Tag.IN_PROGRESS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public MemberAccessLevelResult completeValue;
    /* access modifiers changed from: private */
    public RemoveFolderMemberError failedValue;

    /* renamed from: com.dropbox.core.v2.sharing.RemoveMemberJobStatus$Serializer */
    static class Serializer extends UnionSerializer<RemoveMemberJobStatus> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RemoveMemberJobStatus removeMemberJobStatus, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (removeMemberJobStatus.tag()) {
                case IN_PROGRESS:
                    jsonGenerator.writeString("in_progress");
                    return;
                case COMPLETE:
                    jsonGenerator.writeStartObject();
                    writeTag("complete", jsonGenerator);
                    Serializer.INSTANCE.serialize(removeMemberJobStatus.completeValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FAILED:
                    jsonGenerator.writeStartObject();
                    writeTag("failed", jsonGenerator);
                    jsonGenerator.writeFieldName("failed");
                    Serializer.INSTANCE.serialize(removeMemberJobStatus.failedValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(removeMemberJobStatus.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public RemoveMemberJobStatus deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            RemoveMemberJobStatus removeMemberJobStatus;
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
                    removeMemberJobStatus = RemoveMemberJobStatus.IN_PROGRESS;
                } else if ("complete".equals(str)) {
                    removeMemberJobStatus = RemoveMemberJobStatus.complete(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("failed".equals(str)) {
                    expectField("failed", jsonParser);
                    removeMemberJobStatus = RemoveMemberJobStatus.failed(Serializer.INSTANCE.deserialize(jsonParser));
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
                return removeMemberJobStatus;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.RemoveMemberJobStatus$Tag */
    public enum Tag {
        IN_PROGRESS,
        COMPLETE,
        FAILED
    }

    private RemoveMemberJobStatus() {
    }

    private RemoveMemberJobStatus withTag(Tag tag) {
        RemoveMemberJobStatus removeMemberJobStatus = new RemoveMemberJobStatus();
        removeMemberJobStatus._tag = tag;
        return removeMemberJobStatus;
    }

    private RemoveMemberJobStatus withTagAndComplete(Tag tag, MemberAccessLevelResult memberAccessLevelResult) {
        RemoveMemberJobStatus removeMemberJobStatus = new RemoveMemberJobStatus();
        removeMemberJobStatus._tag = tag;
        removeMemberJobStatus.completeValue = memberAccessLevelResult;
        return removeMemberJobStatus;
    }

    private RemoveMemberJobStatus withTagAndFailed(Tag tag, RemoveFolderMemberError removeFolderMemberError) {
        RemoveMemberJobStatus removeMemberJobStatus = new RemoveMemberJobStatus();
        removeMemberJobStatus._tag = tag;
        removeMemberJobStatus.failedValue = removeFolderMemberError;
        return removeMemberJobStatus;
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

    public static RemoveMemberJobStatus complete(MemberAccessLevelResult memberAccessLevelResult) {
        if (memberAccessLevelResult != null) {
            return new RemoveMemberJobStatus().withTagAndComplete(Tag.COMPLETE, memberAccessLevelResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public MemberAccessLevelResult getCompleteValue() {
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

    public static RemoveMemberJobStatus failed(RemoveFolderMemberError removeFolderMemberError) {
        if (removeFolderMemberError != null) {
            return new RemoveMemberJobStatus().withTagAndFailed(Tag.FAILED, removeFolderMemberError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RemoveFolderMemberError getFailedValue() {
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
        if (obj == null || !(obj instanceof RemoveMemberJobStatus)) {
            return false;
        }
        RemoveMemberJobStatus removeMemberJobStatus = (RemoveMemberJobStatus) obj;
        if (this._tag != removeMemberJobStatus._tag) {
            return false;
        }
        switch (this._tag) {
            case IN_PROGRESS:
                return true;
            case COMPLETE:
                MemberAccessLevelResult memberAccessLevelResult = this.completeValue;
                MemberAccessLevelResult memberAccessLevelResult2 = removeMemberJobStatus.completeValue;
                if (memberAccessLevelResult != memberAccessLevelResult2 && !memberAccessLevelResult.equals(memberAccessLevelResult2)) {
                    z = false;
                }
                return z;
            case FAILED:
                RemoveFolderMemberError removeFolderMemberError = this.failedValue;
                RemoveFolderMemberError removeFolderMemberError2 = removeMemberJobStatus.failedValue;
                if (removeFolderMemberError != removeFolderMemberError2 && !removeFolderMemberError.equals(removeFolderMemberError2)) {
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
