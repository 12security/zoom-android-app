package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.FileMemberRemoveActionResult */
public final class FileMemberRemoveActionResult {
    public static final FileMemberRemoveActionResult OTHER = new FileMemberRemoveActionResult().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public FileMemberActionError memberErrorValue;
    /* access modifiers changed from: private */
    public MemberAccessLevelResult successValue;

    /* renamed from: com.dropbox.core.v2.sharing.FileMemberRemoveActionResult$Serializer */
    static class Serializer extends UnionSerializer<FileMemberRemoveActionResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileMemberRemoveActionResult fileMemberRemoveActionResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (fileMemberRemoveActionResult.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    Serializer.INSTANCE.serialize(fileMemberRemoveActionResult.successValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case MEMBER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("member_error", jsonGenerator);
                    jsonGenerator.writeFieldName("member_error");
                    Serializer.INSTANCE.serialize(fileMemberRemoveActionResult.memberErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public FileMemberRemoveActionResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            FileMemberRemoveActionResult fileMemberRemoveActionResult;
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
                    fileMemberRemoveActionResult = FileMemberRemoveActionResult.success(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("member_error".equals(str)) {
                    expectField("member_error", jsonParser);
                    fileMemberRemoveActionResult = FileMemberRemoveActionResult.memberError(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    fileMemberRemoveActionResult = FileMemberRemoveActionResult.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return fileMemberRemoveActionResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.FileMemberRemoveActionResult$Tag */
    public enum Tag {
        SUCCESS,
        MEMBER_ERROR,
        OTHER
    }

    private FileMemberRemoveActionResult() {
    }

    private FileMemberRemoveActionResult withTag(Tag tag) {
        FileMemberRemoveActionResult fileMemberRemoveActionResult = new FileMemberRemoveActionResult();
        fileMemberRemoveActionResult._tag = tag;
        return fileMemberRemoveActionResult;
    }

    private FileMemberRemoveActionResult withTagAndSuccess(Tag tag, MemberAccessLevelResult memberAccessLevelResult) {
        FileMemberRemoveActionResult fileMemberRemoveActionResult = new FileMemberRemoveActionResult();
        fileMemberRemoveActionResult._tag = tag;
        fileMemberRemoveActionResult.successValue = memberAccessLevelResult;
        return fileMemberRemoveActionResult;
    }

    private FileMemberRemoveActionResult withTagAndMemberError(Tag tag, FileMemberActionError fileMemberActionError) {
        FileMemberRemoveActionResult fileMemberRemoveActionResult = new FileMemberRemoveActionResult();
        fileMemberRemoveActionResult._tag = tag;
        fileMemberRemoveActionResult.memberErrorValue = fileMemberActionError;
        return fileMemberRemoveActionResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static FileMemberRemoveActionResult success(MemberAccessLevelResult memberAccessLevelResult) {
        if (memberAccessLevelResult != null) {
            return new FileMemberRemoveActionResult().withTagAndSuccess(Tag.SUCCESS, memberAccessLevelResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public MemberAccessLevelResult getSuccessValue() {
        if (this._tag == Tag.SUCCESS) {
            return this.successValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SUCCESS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isMemberError() {
        return this._tag == Tag.MEMBER_ERROR;
    }

    public static FileMemberRemoveActionResult memberError(FileMemberActionError fileMemberActionError) {
        if (fileMemberActionError != null) {
            return new FileMemberRemoveActionResult().withTagAndMemberError(Tag.MEMBER_ERROR, fileMemberActionError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public FileMemberActionError getMemberErrorValue() {
        if (this._tag == Tag.MEMBER_ERROR) {
            return this.memberErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.MEMBER_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.successValue, this.memberErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof FileMemberRemoveActionResult)) {
            return false;
        }
        FileMemberRemoveActionResult fileMemberRemoveActionResult = (FileMemberRemoveActionResult) obj;
        if (this._tag != fileMemberRemoveActionResult._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                MemberAccessLevelResult memberAccessLevelResult = this.successValue;
                MemberAccessLevelResult memberAccessLevelResult2 = fileMemberRemoveActionResult.successValue;
                if (memberAccessLevelResult != memberAccessLevelResult2 && !memberAccessLevelResult.equals(memberAccessLevelResult2)) {
                    z = false;
                }
                return z;
            case MEMBER_ERROR:
                FileMemberActionError fileMemberActionError = this.memberErrorValue;
                FileMemberActionError fileMemberActionError2 = fileMemberRemoveActionResult.memberErrorValue;
                if (fileMemberActionError != fileMemberActionError2 && !fileMemberActionError.equals(fileMemberActionError2)) {
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
