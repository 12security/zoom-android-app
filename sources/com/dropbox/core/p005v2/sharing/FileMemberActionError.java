package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.FileMemberActionError */
public final class FileMemberActionError {
    public static final FileMemberActionError INVALID_MEMBER = new FileMemberActionError().withTag(Tag.INVALID_MEMBER);
    public static final FileMemberActionError NO_PERMISSION = new FileMemberActionError().withTag(Tag.NO_PERMISSION);
    public static final FileMemberActionError OTHER = new FileMemberActionError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public MemberAccessLevelResult noExplicitAccessValue;

    /* renamed from: com.dropbox.core.v2.sharing.FileMemberActionError$Serializer */
    static class Serializer extends UnionSerializer<FileMemberActionError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FileMemberActionError fileMemberActionError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (fileMemberActionError.tag()) {
                case INVALID_MEMBER:
                    jsonGenerator.writeString("invalid_member");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(fileMemberActionError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NO_EXPLICIT_ACCESS:
                    jsonGenerator.writeStartObject();
                    writeTag("no_explicit_access", jsonGenerator);
                    Serializer.INSTANCE.serialize(fileMemberActionError.noExplicitAccessValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public FileMemberActionError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            FileMemberActionError fileMemberActionError;
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
                if ("invalid_member".equals(str)) {
                    fileMemberActionError = FileMemberActionError.INVALID_MEMBER;
                } else if ("no_permission".equals(str)) {
                    fileMemberActionError = FileMemberActionError.NO_PERMISSION;
                } else if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    fileMemberActionError = FileMemberActionError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("no_explicit_access".equals(str)) {
                    fileMemberActionError = FileMemberActionError.noExplicitAccess(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    fileMemberActionError = FileMemberActionError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return fileMemberActionError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.FileMemberActionError$Tag */
    public enum Tag {
        INVALID_MEMBER,
        NO_PERMISSION,
        ACCESS_ERROR,
        NO_EXPLICIT_ACCESS,
        OTHER
    }

    private FileMemberActionError() {
    }

    private FileMemberActionError withTag(Tag tag) {
        FileMemberActionError fileMemberActionError = new FileMemberActionError();
        fileMemberActionError._tag = tag;
        return fileMemberActionError;
    }

    private FileMemberActionError withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        FileMemberActionError fileMemberActionError = new FileMemberActionError();
        fileMemberActionError._tag = tag;
        fileMemberActionError.accessErrorValue = sharingFileAccessError;
        return fileMemberActionError;
    }

    private FileMemberActionError withTagAndNoExplicitAccess(Tag tag, MemberAccessLevelResult memberAccessLevelResult) {
        FileMemberActionError fileMemberActionError = new FileMemberActionError();
        fileMemberActionError._tag = tag;
        fileMemberActionError.noExplicitAccessValue = memberAccessLevelResult;
        return fileMemberActionError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isInvalidMember() {
        return this._tag == Tag.INVALID_MEMBER;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static FileMemberActionError accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new FileMemberActionError().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
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

    public static FileMemberActionError noExplicitAccess(MemberAccessLevelResult memberAccessLevelResult) {
        if (memberAccessLevelResult != null) {
            return new FileMemberActionError().withTagAndNoExplicitAccess(Tag.NO_EXPLICIT_ACCESS, memberAccessLevelResult);
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
        return Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue, this.noExplicitAccessValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof FileMemberActionError)) {
            return false;
        }
        FileMemberActionError fileMemberActionError = (FileMemberActionError) obj;
        if (this._tag != fileMemberActionError._tag) {
            return false;
        }
        switch (this._tag) {
            case INVALID_MEMBER:
                return true;
            case NO_PERMISSION:
                return true;
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = fileMemberActionError.accessErrorValue;
                if (sharingFileAccessError != sharingFileAccessError2 && !sharingFileAccessError.equals(sharingFileAccessError2)) {
                    z = false;
                }
                return z;
            case NO_EXPLICIT_ACCESS:
                MemberAccessLevelResult memberAccessLevelResult = this.noExplicitAccessValue;
                MemberAccessLevelResult memberAccessLevelResult2 = fileMemberActionError.noExplicitAccessValue;
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
