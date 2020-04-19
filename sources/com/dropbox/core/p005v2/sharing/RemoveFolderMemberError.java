package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.RemoveFolderMemberError */
public final class RemoveFolderMemberError {
    public static final RemoveFolderMemberError FOLDER_OWNER = new RemoveFolderMemberError().withTag(Tag.FOLDER_OWNER);
    public static final RemoveFolderMemberError GROUP_ACCESS = new RemoveFolderMemberError().withTag(Tag.GROUP_ACCESS);
    public static final RemoveFolderMemberError NO_PERMISSION = new RemoveFolderMemberError().withTag(Tag.NO_PERMISSION);
    public static final RemoveFolderMemberError OTHER = new RemoveFolderMemberError().withTag(Tag.OTHER);
    public static final RemoveFolderMemberError TEAM_FOLDER = new RemoveFolderMemberError().withTag(Tag.TEAM_FOLDER);
    public static final RemoveFolderMemberError TOO_MANY_FILES = new RemoveFolderMemberError().withTag(Tag.TOO_MANY_FILES);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public SharedFolderMemberError memberErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.RemoveFolderMemberError$Serializer */
    static class Serializer extends UnionSerializer<RemoveFolderMemberError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RemoveFolderMemberError removeFolderMemberError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (removeFolderMemberError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(removeFolderMemberError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case MEMBER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("member_error", jsonGenerator);
                    jsonGenerator.writeFieldName("member_error");
                    Serializer.INSTANCE.serialize(removeFolderMemberError.memberErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case FOLDER_OWNER:
                    jsonGenerator.writeString("folder_owner");
                    return;
                case GROUP_ACCESS:
                    jsonGenerator.writeString("group_access");
                    return;
                case TEAM_FOLDER:
                    jsonGenerator.writeString("team_folder");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                case TOO_MANY_FILES:
                    jsonGenerator.writeString("too_many_files");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RemoveFolderMemberError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RemoveFolderMemberError removeFolderMemberError;
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
                    removeFolderMemberError = RemoveFolderMemberError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("member_error".equals(str)) {
                    expectField("member_error", jsonParser);
                    removeFolderMemberError = RemoveFolderMemberError.memberError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("folder_owner".equals(str)) {
                    removeFolderMemberError = RemoveFolderMemberError.FOLDER_OWNER;
                } else if ("group_access".equals(str)) {
                    removeFolderMemberError = RemoveFolderMemberError.GROUP_ACCESS;
                } else if ("team_folder".equals(str)) {
                    removeFolderMemberError = RemoveFolderMemberError.TEAM_FOLDER;
                } else if ("no_permission".equals(str)) {
                    removeFolderMemberError = RemoveFolderMemberError.NO_PERMISSION;
                } else if ("too_many_files".equals(str)) {
                    removeFolderMemberError = RemoveFolderMemberError.TOO_MANY_FILES;
                } else {
                    removeFolderMemberError = RemoveFolderMemberError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return removeFolderMemberError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.RemoveFolderMemberError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        MEMBER_ERROR,
        FOLDER_OWNER,
        GROUP_ACCESS,
        TEAM_FOLDER,
        NO_PERMISSION,
        TOO_MANY_FILES,
        OTHER
    }

    private RemoveFolderMemberError() {
    }

    private RemoveFolderMemberError withTag(Tag tag) {
        RemoveFolderMemberError removeFolderMemberError = new RemoveFolderMemberError();
        removeFolderMemberError._tag = tag;
        return removeFolderMemberError;
    }

    private RemoveFolderMemberError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        RemoveFolderMemberError removeFolderMemberError = new RemoveFolderMemberError();
        removeFolderMemberError._tag = tag;
        removeFolderMemberError.accessErrorValue = sharedFolderAccessError;
        return removeFolderMemberError;
    }

    private RemoveFolderMemberError withTagAndMemberError(Tag tag, SharedFolderMemberError sharedFolderMemberError) {
        RemoveFolderMemberError removeFolderMemberError = new RemoveFolderMemberError();
        removeFolderMemberError._tag = tag;
        removeFolderMemberError.memberErrorValue = sharedFolderMemberError;
        return removeFolderMemberError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static RemoveFolderMemberError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new RemoveFolderMemberError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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

    public static RemoveFolderMemberError memberError(SharedFolderMemberError sharedFolderMemberError) {
        if (sharedFolderMemberError != null) {
            return new RemoveFolderMemberError().withTagAndMemberError(Tag.MEMBER_ERROR, sharedFolderMemberError);
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

    public boolean isFolderOwner() {
        return this._tag == Tag.FOLDER_OWNER;
    }

    public boolean isGroupAccess() {
        return this._tag == Tag.GROUP_ACCESS;
    }

    public boolean isTeamFolder() {
        return this._tag == Tag.TEAM_FOLDER;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isTooManyFiles() {
        return this._tag == Tag.TOO_MANY_FILES;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue, this.memberErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof RemoveFolderMemberError)) {
            return false;
        }
        RemoveFolderMemberError removeFolderMemberError = (RemoveFolderMemberError) obj;
        if (this._tag != removeFolderMemberError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = removeFolderMemberError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
            case MEMBER_ERROR:
                SharedFolderMemberError sharedFolderMemberError = this.memberErrorValue;
                SharedFolderMemberError sharedFolderMemberError2 = removeFolderMemberError.memberErrorValue;
                if (sharedFolderMemberError != sharedFolderMemberError2 && !sharedFolderMemberError.equals(sharedFolderMemberError2)) {
                    z = false;
                }
                return z;
            case FOLDER_OWNER:
                return true;
            case GROUP_ACCESS:
                return true;
            case TEAM_FOLDER:
                return true;
            case NO_PERMISSION:
                return true;
            case TOO_MANY_FILES:
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
