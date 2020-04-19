package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.RemoveFileMemberError */
public final class RemoveFileMemberError {
    public static final RemoveFileMemberError OTHER = new RemoveFileMemberError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public MemberAccessLevelResult noExplicitAccessValue;
    /* access modifiers changed from: private */
    public SharingUserError userErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.RemoveFileMemberError$Serializer */
    static class Serializer extends UnionSerializer<RemoveFileMemberError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RemoveFileMemberError removeFileMemberError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (removeFileMemberError.tag()) {
                case USER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("user_error", jsonGenerator);
                    jsonGenerator.writeFieldName("user_error");
                    Serializer.INSTANCE.serialize(removeFileMemberError.userErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(removeFileMemberError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NO_EXPLICIT_ACCESS:
                    jsonGenerator.writeStartObject();
                    writeTag("no_explicit_access", jsonGenerator);
                    Serializer.INSTANCE.serialize(removeFileMemberError.noExplicitAccessValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RemoveFileMemberError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            RemoveFileMemberError removeFileMemberError;
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
                if ("user_error".equals(str)) {
                    expectField("user_error", jsonParser);
                    removeFileMemberError = RemoveFileMemberError.userError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    removeFileMemberError = RemoveFileMemberError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("no_explicit_access".equals(str)) {
                    removeFileMemberError = RemoveFileMemberError.noExplicitAccess(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    removeFileMemberError = RemoveFileMemberError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return removeFileMemberError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.RemoveFileMemberError$Tag */
    public enum Tag {
        USER_ERROR,
        ACCESS_ERROR,
        NO_EXPLICIT_ACCESS,
        OTHER
    }

    private RemoveFileMemberError() {
    }

    private RemoveFileMemberError withTag(Tag tag) {
        RemoveFileMemberError removeFileMemberError = new RemoveFileMemberError();
        removeFileMemberError._tag = tag;
        return removeFileMemberError;
    }

    private RemoveFileMemberError withTagAndUserError(Tag tag, SharingUserError sharingUserError) {
        RemoveFileMemberError removeFileMemberError = new RemoveFileMemberError();
        removeFileMemberError._tag = tag;
        removeFileMemberError.userErrorValue = sharingUserError;
        return removeFileMemberError;
    }

    private RemoveFileMemberError withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        RemoveFileMemberError removeFileMemberError = new RemoveFileMemberError();
        removeFileMemberError._tag = tag;
        removeFileMemberError.accessErrorValue = sharingFileAccessError;
        return removeFileMemberError;
    }

    private RemoveFileMemberError withTagAndNoExplicitAccess(Tag tag, MemberAccessLevelResult memberAccessLevelResult) {
        RemoveFileMemberError removeFileMemberError = new RemoveFileMemberError();
        removeFileMemberError._tag = tag;
        removeFileMemberError.noExplicitAccessValue = memberAccessLevelResult;
        return removeFileMemberError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUserError() {
        return this._tag == Tag.USER_ERROR;
    }

    public static RemoveFileMemberError userError(SharingUserError sharingUserError) {
        if (sharingUserError != null) {
            return new RemoveFileMemberError().withTagAndUserError(Tag.USER_ERROR, sharingUserError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharingUserError getUserErrorValue() {
        if (this._tag == Tag.USER_ERROR) {
            return this.userErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static RemoveFileMemberError accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new RemoveFileMemberError().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
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

    public boolean isNoExplicitAccess() {
        return this._tag == Tag.NO_EXPLICIT_ACCESS;
    }

    public static RemoveFileMemberError noExplicitAccess(MemberAccessLevelResult memberAccessLevelResult) {
        if (memberAccessLevelResult != null) {
            return new RemoveFileMemberError().withTagAndNoExplicitAccess(Tag.NO_EXPLICIT_ACCESS, memberAccessLevelResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public MemberAccessLevelResult getNoExplicitAccessValue() {
        if (this._tag == Tag.NO_EXPLICIT_ACCESS) {
            return this.noExplicitAccessValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.NO_EXPLICIT_ACCESS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.userErrorValue, this.accessErrorValue, this.noExplicitAccessValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof RemoveFileMemberError)) {
            return false;
        }
        RemoveFileMemberError removeFileMemberError = (RemoveFileMemberError) obj;
        if (this._tag != removeFileMemberError._tag) {
            return false;
        }
        switch (this._tag) {
            case USER_ERROR:
                SharingUserError sharingUserError = this.userErrorValue;
                SharingUserError sharingUserError2 = removeFileMemberError.userErrorValue;
                if (sharingUserError != sharingUserError2 && !sharingUserError.equals(sharingUserError2)) {
                    z = false;
                }
                return z;
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = removeFileMemberError.accessErrorValue;
                if (sharingFileAccessError != sharingFileAccessError2 && !sharingFileAccessError.equals(sharingFileAccessError2)) {
                    z = false;
                }
                return z;
            case NO_EXPLICIT_ACCESS:
                MemberAccessLevelResult memberAccessLevelResult = this.noExplicitAccessValue;
                MemberAccessLevelResult memberAccessLevelResult2 = removeFileMemberError.noExplicitAccessValue;
                if (memberAccessLevelResult != memberAccessLevelResult2 && !memberAccessLevelResult.equals(memberAccessLevelResult2)) {
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
