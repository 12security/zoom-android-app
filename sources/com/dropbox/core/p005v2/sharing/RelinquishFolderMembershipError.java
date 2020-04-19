package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.RelinquishFolderMembershipError */
public final class RelinquishFolderMembershipError {
    public static final RelinquishFolderMembershipError FOLDER_OWNER = new RelinquishFolderMembershipError().withTag(Tag.FOLDER_OWNER);
    public static final RelinquishFolderMembershipError GROUP_ACCESS = new RelinquishFolderMembershipError().withTag(Tag.GROUP_ACCESS);
    public static final RelinquishFolderMembershipError MOUNTED = new RelinquishFolderMembershipError().withTag(Tag.MOUNTED);
    public static final RelinquishFolderMembershipError NO_EXPLICIT_ACCESS = new RelinquishFolderMembershipError().withTag(Tag.NO_EXPLICIT_ACCESS);
    public static final RelinquishFolderMembershipError NO_PERMISSION = new RelinquishFolderMembershipError().withTag(Tag.NO_PERMISSION);
    public static final RelinquishFolderMembershipError OTHER = new RelinquishFolderMembershipError().withTag(Tag.OTHER);
    public static final RelinquishFolderMembershipError TEAM_FOLDER = new RelinquishFolderMembershipError().withTag(Tag.TEAM_FOLDER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.RelinquishFolderMembershipError$Serializer */
    static class Serializer extends UnionSerializer<RelinquishFolderMembershipError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelinquishFolderMembershipError relinquishFolderMembershipError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (relinquishFolderMembershipError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(relinquishFolderMembershipError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case FOLDER_OWNER:
                    jsonGenerator.writeString("folder_owner");
                    return;
                case MOUNTED:
                    jsonGenerator.writeString("mounted");
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
                case NO_EXPLICIT_ACCESS:
                    jsonGenerator.writeString("no_explicit_access");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RelinquishFolderMembershipError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RelinquishFolderMembershipError relinquishFolderMembershipError;
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
                    relinquishFolderMembershipError = RelinquishFolderMembershipError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("folder_owner".equals(str)) {
                    relinquishFolderMembershipError = RelinquishFolderMembershipError.FOLDER_OWNER;
                } else if ("mounted".equals(str)) {
                    relinquishFolderMembershipError = RelinquishFolderMembershipError.MOUNTED;
                } else if ("group_access".equals(str)) {
                    relinquishFolderMembershipError = RelinquishFolderMembershipError.GROUP_ACCESS;
                } else if ("team_folder".equals(str)) {
                    relinquishFolderMembershipError = RelinquishFolderMembershipError.TEAM_FOLDER;
                } else if ("no_permission".equals(str)) {
                    relinquishFolderMembershipError = RelinquishFolderMembershipError.NO_PERMISSION;
                } else if ("no_explicit_access".equals(str)) {
                    relinquishFolderMembershipError = RelinquishFolderMembershipError.NO_EXPLICIT_ACCESS;
                } else {
                    relinquishFolderMembershipError = RelinquishFolderMembershipError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return relinquishFolderMembershipError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.RelinquishFolderMembershipError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        FOLDER_OWNER,
        MOUNTED,
        GROUP_ACCESS,
        TEAM_FOLDER,
        NO_PERMISSION,
        NO_EXPLICIT_ACCESS,
        OTHER
    }

    private RelinquishFolderMembershipError() {
    }

    private RelinquishFolderMembershipError withTag(Tag tag) {
        RelinquishFolderMembershipError relinquishFolderMembershipError = new RelinquishFolderMembershipError();
        relinquishFolderMembershipError._tag = tag;
        return relinquishFolderMembershipError;
    }

    private RelinquishFolderMembershipError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        RelinquishFolderMembershipError relinquishFolderMembershipError = new RelinquishFolderMembershipError();
        relinquishFolderMembershipError._tag = tag;
        relinquishFolderMembershipError.accessErrorValue = sharedFolderAccessError;
        return relinquishFolderMembershipError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static RelinquishFolderMembershipError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new RelinquishFolderMembershipError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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

    public boolean isFolderOwner() {
        return this._tag == Tag.FOLDER_OWNER;
    }

    public boolean isMounted() {
        return this._tag == Tag.MOUNTED;
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

    public boolean isNoExplicitAccess() {
        return this._tag == Tag.NO_EXPLICIT_ACCESS;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof RelinquishFolderMembershipError)) {
            return false;
        }
        RelinquishFolderMembershipError relinquishFolderMembershipError = (RelinquishFolderMembershipError) obj;
        if (this._tag != relinquishFolderMembershipError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = relinquishFolderMembershipError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
            case FOLDER_OWNER:
                return true;
            case MOUNTED:
                return true;
            case GROUP_ACCESS:
                return true;
            case TEAM_FOLDER:
                return true;
            case NO_PERMISSION:
                return true;
            case NO_EXPLICIT_ACCESS:
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
