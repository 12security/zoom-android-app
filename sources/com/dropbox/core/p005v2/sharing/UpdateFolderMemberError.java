package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.UpdateFolderMemberError */
public final class UpdateFolderMemberError {
    public static final UpdateFolderMemberError INSUFFICIENT_PLAN = new UpdateFolderMemberError().withTag(Tag.INSUFFICIENT_PLAN);
    public static final UpdateFolderMemberError NO_PERMISSION = new UpdateFolderMemberError().withTag(Tag.NO_PERMISSION);
    public static final UpdateFolderMemberError OTHER = new UpdateFolderMemberError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public SharedFolderMemberError memberErrorValue;
    /* access modifiers changed from: private */
    public AddFolderMemberError noExplicitAccessValue;

    /* renamed from: com.dropbox.core.v2.sharing.UpdateFolderMemberError$Serializer */
    static class Serializer extends UnionSerializer<UpdateFolderMemberError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UpdateFolderMemberError updateFolderMemberError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (updateFolderMemberError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(updateFolderMemberError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case MEMBER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("member_error", jsonGenerator);
                    jsonGenerator.writeFieldName("member_error");
                    Serializer.INSTANCE.serialize(updateFolderMemberError.memberErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NO_EXPLICIT_ACCESS:
                    jsonGenerator.writeStartObject();
                    writeTag("no_explicit_access", jsonGenerator);
                    jsonGenerator.writeFieldName("no_explicit_access");
                    Serializer.INSTANCE.serialize(updateFolderMemberError.noExplicitAccessValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case INSUFFICIENT_PLAN:
                    jsonGenerator.writeString("insufficient_plan");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UpdateFolderMemberError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UpdateFolderMemberError updateFolderMemberError;
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
                if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    updateFolderMemberError = UpdateFolderMemberError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("member_error".equals(str)) {
                    expectField("member_error", jsonParser);
                    updateFolderMemberError = UpdateFolderMemberError.memberError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("no_explicit_access".equals(str)) {
                    expectField("no_explicit_access", jsonParser);
                    updateFolderMemberError = UpdateFolderMemberError.noExplicitAccess(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("insufficient_plan".equals(str)) {
                    updateFolderMemberError = UpdateFolderMemberError.INSUFFICIENT_PLAN;
                } else if ("no_permission".equals(str)) {
                    updateFolderMemberError = UpdateFolderMemberError.NO_PERMISSION;
                } else {
                    updateFolderMemberError = UpdateFolderMemberError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return updateFolderMemberError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.UpdateFolderMemberError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        MEMBER_ERROR,
        NO_EXPLICIT_ACCESS,
        INSUFFICIENT_PLAN,
        NO_PERMISSION,
        OTHER
    }

    private UpdateFolderMemberError() {
    }

    private UpdateFolderMemberError withTag(Tag tag) {
        UpdateFolderMemberError updateFolderMemberError = new UpdateFolderMemberError();
        updateFolderMemberError._tag = tag;
        return updateFolderMemberError;
    }

    private UpdateFolderMemberError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        UpdateFolderMemberError updateFolderMemberError = new UpdateFolderMemberError();
        updateFolderMemberError._tag = tag;
        updateFolderMemberError.accessErrorValue = sharedFolderAccessError;
        return updateFolderMemberError;
    }

    private UpdateFolderMemberError withTagAndMemberError(Tag tag, SharedFolderMemberError sharedFolderMemberError) {
        UpdateFolderMemberError updateFolderMemberError = new UpdateFolderMemberError();
        updateFolderMemberError._tag = tag;
        updateFolderMemberError.memberErrorValue = sharedFolderMemberError;
        return updateFolderMemberError;
    }

    private UpdateFolderMemberError withTagAndNoExplicitAccess(Tag tag, AddFolderMemberError addFolderMemberError) {
        UpdateFolderMemberError updateFolderMemberError = new UpdateFolderMemberError();
        updateFolderMemberError._tag = tag;
        updateFolderMemberError.noExplicitAccessValue = addFolderMemberError;
        return updateFolderMemberError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static UpdateFolderMemberError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new UpdateFolderMemberError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharedFolderAccessError getAccessErrorValue() {
        if (this._tag == Tag.ACCESS_ERROR) {
            return this.accessErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ACCESS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isMemberError() {
        return this._tag == Tag.MEMBER_ERROR;
    }

    public static UpdateFolderMemberError memberError(SharedFolderMemberError sharedFolderMemberError) {
        if (sharedFolderMemberError != null) {
            return new UpdateFolderMemberError().withTagAndMemberError(Tag.MEMBER_ERROR, sharedFolderMemberError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharedFolderMemberError getMemberErrorValue() {
        if (this._tag == Tag.MEMBER_ERROR) {
            return this.memberErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.MEMBER_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isNoExplicitAccess() {
        return this._tag == Tag.NO_EXPLICIT_ACCESS;
    }

    public static UpdateFolderMemberError noExplicitAccess(AddFolderMemberError addFolderMemberError) {
        if (addFolderMemberError != null) {
            return new UpdateFolderMemberError().withTagAndNoExplicitAccess(Tag.NO_EXPLICIT_ACCESS, addFolderMemberError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public AddFolderMemberError getNoExplicitAccessValue() {
        if (this._tag == Tag.NO_EXPLICIT_ACCESS) {
            return this.noExplicitAccessValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.NO_EXPLICIT_ACCESS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isInsufficientPlan() {
        return this._tag == Tag.INSUFFICIENT_PLAN;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue, this.memberErrorValue, this.noExplicitAccessValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UpdateFolderMemberError)) {
            return false;
        }
        UpdateFolderMemberError updateFolderMemberError = (UpdateFolderMemberError) obj;
        if (this._tag != updateFolderMemberError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = updateFolderMemberError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
            case MEMBER_ERROR:
                SharedFolderMemberError sharedFolderMemberError = this.memberErrorValue;
                SharedFolderMemberError sharedFolderMemberError2 = updateFolderMemberError.memberErrorValue;
                if (sharedFolderMemberError != sharedFolderMemberError2 && !sharedFolderMemberError.equals(sharedFolderMemberError2)) {
                    z = false;
                }
                return z;
            case NO_EXPLICIT_ACCESS:
                AddFolderMemberError addFolderMemberError = this.noExplicitAccessValue;
                AddFolderMemberError addFolderMemberError2 = updateFolderMemberError.noExplicitAccessValue;
                if (addFolderMemberError != addFolderMemberError2 && !addFolderMemberError.equals(addFolderMemberError2)) {
                    z = false;
                }
                return z;
            case INSUFFICIENT_PLAN:
                return true;
            case NO_PERMISSION:
                return true;
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
