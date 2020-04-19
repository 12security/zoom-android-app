package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.FileMemberActionIndividualResult */
public final class FileMemberActionIndividualResult {
    private Tag _tag;
    /* access modifiers changed from: private */
    public FileMemberActionError memberErrorValue;
    /* access modifiers changed from: private */
    public AccessLevel successValue;

    /* renamed from: com.dropbox.core.v2.sharing.FileMemberActionIndividualResult$Serializer */
    static class Serializer extends UnionSerializer<FileMemberActionIndividualResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileMemberActionIndividualResult fileMemberActionIndividualResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (fileMemberActionIndividualResult.tag()) {
                case SUCCESS:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.SUCCESS, jsonGenerator);
                    jsonGenerator.writeFieldName(Param.SUCCESS);
                    StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(fileMemberActionIndividualResult.successValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case MEMBER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("member_error", jsonGenerator);
                    jsonGenerator.writeFieldName("member_error");
                    Serializer.INSTANCE.serialize(fileMemberActionIndividualResult.memberErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(fileMemberActionIndividualResult.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public FileMemberActionIndividualResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            FileMemberActionIndividualResult fileMemberActionIndividualResult;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if (Param.SUCCESS.equals(str)) {
                    AccessLevel accessLevel = null;
                    if (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
                        expectField(Param.SUCCESS, jsonParser);
                        accessLevel = (AccessLevel) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).deserialize(jsonParser);
                    }
                    if (accessLevel == null) {
                        fileMemberActionIndividualResult = FileMemberActionIndividualResult.success();
                    } else {
                        fileMemberActionIndividualResult = FileMemberActionIndividualResult.success(accessLevel);
                    }
                } else if ("member_error".equals(str)) {
                    expectField("member_error", jsonParser);
                    fileMemberActionIndividualResult = FileMemberActionIndividualResult.memberError(Serializer.INSTANCE.deserialize(jsonParser));
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
                return fileMemberActionIndividualResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.FileMemberActionIndividualResult$Tag */
    public enum Tag {
        SUCCESS,
        MEMBER_ERROR
    }

    private FileMemberActionIndividualResult() {
    }

    private FileMemberActionIndividualResult withTag(Tag tag) {
        FileMemberActionIndividualResult fileMemberActionIndividualResult = new FileMemberActionIndividualResult();
        fileMemberActionIndividualResult._tag = tag;
        return fileMemberActionIndividualResult;
    }

    private FileMemberActionIndividualResult withTagAndSuccess(Tag tag, AccessLevel accessLevel) {
        FileMemberActionIndividualResult fileMemberActionIndividualResult = new FileMemberActionIndividualResult();
        fileMemberActionIndividualResult._tag = tag;
        fileMemberActionIndividualResult.successValue = accessLevel;
        return fileMemberActionIndividualResult;
    }

    private FileMemberActionIndividualResult withTagAndMemberError(Tag tag, FileMemberActionError fileMemberActionError) {
        FileMemberActionIndividualResult fileMemberActionIndividualResult = new FileMemberActionIndividualResult();
        fileMemberActionIndividualResult._tag = tag;
        fileMemberActionIndividualResult.memberErrorValue = fileMemberActionError;
        return fileMemberActionIndividualResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSuccess() {
        return this._tag == Tag.SUCCESS;
    }

    public static FileMemberActionIndividualResult success(AccessLevel accessLevel) {
        return new FileMemberActionIndividualResult().withTagAndSuccess(Tag.SUCCESS, accessLevel);
    }

    public static FileMemberActionIndividualResult success() {
        return success(null);
    }

    public AccessLevel getSuccessValue() {
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

    public static FileMemberActionIndividualResult memberError(FileMemberActionError fileMemberActionError) {
        if (fileMemberActionError != null) {
            return new FileMemberActionIndividualResult().withTagAndMemberError(Tag.MEMBER_ERROR, fileMemberActionError);
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

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.successValue, this.memberErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof FileMemberActionIndividualResult)) {
            return false;
        }
        FileMemberActionIndividualResult fileMemberActionIndividualResult = (FileMemberActionIndividualResult) obj;
        if (this._tag != fileMemberActionIndividualResult._tag) {
            return false;
        }
        switch (this._tag) {
            case SUCCESS:
                AccessLevel accessLevel = this.successValue;
                AccessLevel accessLevel2 = fileMemberActionIndividualResult.successValue;
                if (accessLevel != accessLevel2 && (accessLevel == null || !accessLevel.equals(accessLevel2))) {
                    z = false;
                }
                return z;
            case MEMBER_ERROR:
                FileMemberActionError fileMemberActionError = this.memberErrorValue;
                FileMemberActionError fileMemberActionError2 = fileMemberActionIndividualResult.memberErrorValue;
                if (fileMemberActionError != fileMemberActionError2 && !fileMemberActionError.equals(fileMemberActionError2)) {
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
