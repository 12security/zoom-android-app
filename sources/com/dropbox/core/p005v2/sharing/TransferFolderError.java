package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.TransferFolderError */
public final class TransferFolderError {
    public static final TransferFolderError INVALID_DROPBOX_ID = new TransferFolderError().withTag(Tag.INVALID_DROPBOX_ID);
    public static final TransferFolderError NEW_OWNER_EMAIL_UNVERIFIED = new TransferFolderError().withTag(Tag.NEW_OWNER_EMAIL_UNVERIFIED);
    public static final TransferFolderError NEW_OWNER_NOT_A_MEMBER = new TransferFolderError().withTag(Tag.NEW_OWNER_NOT_A_MEMBER);
    public static final TransferFolderError NEW_OWNER_UNMOUNTED = new TransferFolderError().withTag(Tag.NEW_OWNER_UNMOUNTED);
    public static final TransferFolderError NO_PERMISSION = new TransferFolderError().withTag(Tag.NO_PERMISSION);
    public static final TransferFolderError OTHER = new TransferFolderError().withTag(Tag.OTHER);
    public static final TransferFolderError TEAM_FOLDER = new TransferFolderError().withTag(Tag.TEAM_FOLDER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.TransferFolderError$Serializer */
    static class Serializer extends UnionSerializer<TransferFolderError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TransferFolderError transferFolderError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (transferFolderError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(transferFolderError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case INVALID_DROPBOX_ID:
                    jsonGenerator.writeString("invalid_dropbox_id");
                    return;
                case NEW_OWNER_NOT_A_MEMBER:
                    jsonGenerator.writeString("new_owner_not_a_member");
                    return;
                case NEW_OWNER_UNMOUNTED:
                    jsonGenerator.writeString("new_owner_unmounted");
                    return;
                case NEW_OWNER_EMAIL_UNVERIFIED:
                    jsonGenerator.writeString("new_owner_email_unverified");
                    return;
                case TEAM_FOLDER:
                    jsonGenerator.writeString("team_folder");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public TransferFolderError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            TransferFolderError transferFolderError;
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
                    transferFolderError = TransferFolderError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("invalid_dropbox_id".equals(str)) {
                    transferFolderError = TransferFolderError.INVALID_DROPBOX_ID;
                } else if ("new_owner_not_a_member".equals(str)) {
                    transferFolderError = TransferFolderError.NEW_OWNER_NOT_A_MEMBER;
                } else if ("new_owner_unmounted".equals(str)) {
                    transferFolderError = TransferFolderError.NEW_OWNER_UNMOUNTED;
                } else if ("new_owner_email_unverified".equals(str)) {
                    transferFolderError = TransferFolderError.NEW_OWNER_EMAIL_UNVERIFIED;
                } else if ("team_folder".equals(str)) {
                    transferFolderError = TransferFolderError.TEAM_FOLDER;
                } else if ("no_permission".equals(str)) {
                    transferFolderError = TransferFolderError.NO_PERMISSION;
                } else {
                    transferFolderError = TransferFolderError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return transferFolderError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.TransferFolderError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        INVALID_DROPBOX_ID,
        NEW_OWNER_NOT_A_MEMBER,
        NEW_OWNER_UNMOUNTED,
        NEW_OWNER_EMAIL_UNVERIFIED,
        TEAM_FOLDER,
        NO_PERMISSION,
        OTHER
    }

    private TransferFolderError() {
    }

    private TransferFolderError withTag(Tag tag) {
        TransferFolderError transferFolderError = new TransferFolderError();
        transferFolderError._tag = tag;
        return transferFolderError;
    }

    private TransferFolderError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        TransferFolderError transferFolderError = new TransferFolderError();
        transferFolderError._tag = tag;
        transferFolderError.accessErrorValue = sharedFolderAccessError;
        return transferFolderError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static TransferFolderError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new TransferFolderError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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

    public boolean isInvalidDropboxId() {
        return this._tag == Tag.INVALID_DROPBOX_ID;
    }

    public boolean isNewOwnerNotAMember() {
        return this._tag == Tag.NEW_OWNER_NOT_A_MEMBER;
    }

    public boolean isNewOwnerUnmounted() {
        return this._tag == Tag.NEW_OWNER_UNMOUNTED;
    }

    public boolean isNewOwnerEmailUnverified() {
        return this._tag == Tag.NEW_OWNER_EMAIL_UNVERIFIED;
    }

    public boolean isTeamFolder() {
        return this._tag == Tag.TEAM_FOLDER;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
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
        if (obj == null || !(obj instanceof TransferFolderError)) {
            return false;
        }
        TransferFolderError transferFolderError = (TransferFolderError) obj;
        if (this._tag != transferFolderError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = transferFolderError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
            case INVALID_DROPBOX_ID:
                return true;
            case NEW_OWNER_NOT_A_MEMBER:
                return true;
            case NEW_OWNER_UNMOUNTED:
                return true;
            case NEW_OWNER_EMAIL_UNVERIFIED:
                return true;
            case TEAM_FOLDER:
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
