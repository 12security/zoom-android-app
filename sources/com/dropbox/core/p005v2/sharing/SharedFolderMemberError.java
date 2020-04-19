package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.SharedFolderMemberError */
public final class SharedFolderMemberError {
    public static final SharedFolderMemberError INVALID_DROPBOX_ID = new SharedFolderMemberError().withTag(Tag.INVALID_DROPBOX_ID);
    public static final SharedFolderMemberError NOT_A_MEMBER = new SharedFolderMemberError().withTag(Tag.NOT_A_MEMBER);
    public static final SharedFolderMemberError OTHER = new SharedFolderMemberError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public MemberAccessLevelResult noExplicitAccessValue;

    /* renamed from: com.dropbox.core.v2.sharing.SharedFolderMemberError$Serializer */
    static class Serializer extends UnionSerializer<SharedFolderMemberError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedFolderMemberError sharedFolderMemberError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharedFolderMemberError.tag()) {
                case INVALID_DROPBOX_ID:
                    jsonGenerator.writeString("invalid_dropbox_id");
                    return;
                case NOT_A_MEMBER:
                    jsonGenerator.writeString("not_a_member");
                    return;
                case NO_EXPLICIT_ACCESS:
                    jsonGenerator.writeStartObject();
                    writeTag("no_explicit_access", jsonGenerator);
                    Serializer.INSTANCE.serialize(sharedFolderMemberError.noExplicitAccessValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharedFolderMemberError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            SharedFolderMemberError sharedFolderMemberError;
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
                if ("invalid_dropbox_id".equals(str)) {
                    sharedFolderMemberError = SharedFolderMemberError.INVALID_DROPBOX_ID;
                } else if ("not_a_member".equals(str)) {
                    sharedFolderMemberError = SharedFolderMemberError.NOT_A_MEMBER;
                } else if ("no_explicit_access".equals(str)) {
                    sharedFolderMemberError = SharedFolderMemberError.noExplicitAccess(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    sharedFolderMemberError = SharedFolderMemberError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharedFolderMemberError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharedFolderMemberError$Tag */
    public enum Tag {
        INVALID_DROPBOX_ID,
        NOT_A_MEMBER,
        NO_EXPLICIT_ACCESS,
        OTHER
    }

    private SharedFolderMemberError() {
    }

    private SharedFolderMemberError withTag(Tag tag) {
        SharedFolderMemberError sharedFolderMemberError = new SharedFolderMemberError();
        sharedFolderMemberError._tag = tag;
        return sharedFolderMemberError;
    }

    private SharedFolderMemberError withTagAndNoExplicitAccess(Tag tag, MemberAccessLevelResult memberAccessLevelResult) {
        SharedFolderMemberError sharedFolderMemberError = new SharedFolderMemberError();
        sharedFolderMemberError._tag = tag;
        sharedFolderMemberError.noExplicitAccessValue = memberAccessLevelResult;
        return sharedFolderMemberError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isInvalidDropboxId() {
        return this._tag == Tag.INVALID_DROPBOX_ID;
    }

    public boolean isNotAMember() {
        return this._tag == Tag.NOT_A_MEMBER;
    }

    public boolean isNoExplicitAccess() {
        return this._tag == Tag.NO_EXPLICIT_ACCESS;
    }

    public static SharedFolderMemberError noExplicitAccess(MemberAccessLevelResult memberAccessLevelResult) {
        if (memberAccessLevelResult != null) {
            return new SharedFolderMemberError().withTagAndNoExplicitAccess(Tag.NO_EXPLICIT_ACCESS, memberAccessLevelResult);
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
        return Arrays.hashCode(new Object[]{this._tag, this.noExplicitAccessValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof SharedFolderMemberError)) {
            return false;
        }
        SharedFolderMemberError sharedFolderMemberError = (SharedFolderMemberError) obj;
        if (this._tag != sharedFolderMemberError._tag) {
            return false;
        }
        switch (this._tag) {
            case INVALID_DROPBOX_ID:
                return true;
            case NOT_A_MEMBER:
                return true;
            case NO_EXPLICIT_ACCESS:
                MemberAccessLevelResult memberAccessLevelResult = this.noExplicitAccessValue;
                MemberAccessLevelResult memberAccessLevelResult2 = sharedFolderMemberError.noExplicitAccessValue;
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
