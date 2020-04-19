package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.MountFolderError */
public final class MountFolderError {
    public static final MountFolderError ALREADY_MOUNTED = new MountFolderError().withTag(Tag.ALREADY_MOUNTED);
    public static final MountFolderError INSIDE_SHARED_FOLDER = new MountFolderError().withTag(Tag.INSIDE_SHARED_FOLDER);
    public static final MountFolderError NOT_MOUNTABLE = new MountFolderError().withTag(Tag.NOT_MOUNTABLE);
    public static final MountFolderError NO_PERMISSION = new MountFolderError().withTag(Tag.NO_PERMISSION);
    public static final MountFolderError OTHER = new MountFolderError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public InsufficientQuotaAmounts insufficientQuotaValue;

    /* renamed from: com.dropbox.core.v2.sharing.MountFolderError$Serializer */
    static class Serializer extends UnionSerializer<MountFolderError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MountFolderError mountFolderError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (mountFolderError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(mountFolderError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case INSIDE_SHARED_FOLDER:
                    jsonGenerator.writeString("inside_shared_folder");
                    return;
                case INSUFFICIENT_QUOTA:
                    jsonGenerator.writeStartObject();
                    writeTag("insufficient_quota", jsonGenerator);
                    Serializer.INSTANCE.serialize(mountFolderError.insufficientQuotaValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case ALREADY_MOUNTED:
                    jsonGenerator.writeString("already_mounted");
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                case NOT_MOUNTABLE:
                    jsonGenerator.writeString("not_mountable");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public MountFolderError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            MountFolderError mountFolderError;
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
                if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    mountFolderError = MountFolderError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("inside_shared_folder".equals(str)) {
                    mountFolderError = MountFolderError.INSIDE_SHARED_FOLDER;
                } else if ("insufficient_quota".equals(str)) {
                    mountFolderError = MountFolderError.insufficientQuota(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("already_mounted".equals(str)) {
                    mountFolderError = MountFolderError.ALREADY_MOUNTED;
                } else if ("no_permission".equals(str)) {
                    mountFolderError = MountFolderError.NO_PERMISSION;
                } else if ("not_mountable".equals(str)) {
                    mountFolderError = MountFolderError.NOT_MOUNTABLE;
                } else {
                    mountFolderError = MountFolderError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return mountFolderError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.MountFolderError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        INSIDE_SHARED_FOLDER,
        INSUFFICIENT_QUOTA,
        ALREADY_MOUNTED,
        NO_PERMISSION,
        NOT_MOUNTABLE,
        OTHER
    }

    private MountFolderError() {
    }

    private MountFolderError withTag(Tag tag) {
        MountFolderError mountFolderError = new MountFolderError();
        mountFolderError._tag = tag;
        return mountFolderError;
    }

    private MountFolderError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        MountFolderError mountFolderError = new MountFolderError();
        mountFolderError._tag = tag;
        mountFolderError.accessErrorValue = sharedFolderAccessError;
        return mountFolderError;
    }

    private MountFolderError withTagAndInsufficientQuota(Tag tag, InsufficientQuotaAmounts insufficientQuotaAmounts) {
        MountFolderError mountFolderError = new MountFolderError();
        mountFolderError._tag = tag;
        mountFolderError.insufficientQuotaValue = insufficientQuotaAmounts;
        return mountFolderError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static MountFolderError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new MountFolderError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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

    public boolean isInsideSharedFolder() {
        return this._tag == Tag.INSIDE_SHARED_FOLDER;
    }

    public boolean isInsufficientQuota() {
        return this._tag == Tag.INSUFFICIENT_QUOTA;
    }

    public static MountFolderError insufficientQuota(InsufficientQuotaAmounts insufficientQuotaAmounts) {
        if (insufficientQuotaAmounts != null) {
            return new MountFolderError().withTagAndInsufficientQuota(Tag.INSUFFICIENT_QUOTA, insufficientQuotaAmounts);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public InsufficientQuotaAmounts getInsufficientQuotaValue() {
        if (this._tag == Tag.INSUFFICIENT_QUOTA) {
            return this.insufficientQuotaValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INSUFFICIENT_QUOTA, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAlreadyMounted() {
        return this._tag == Tag.ALREADY_MOUNTED;
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isNotMountable() {
        return this._tag == Tag.NOT_MOUNTABLE;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue, this.insufficientQuotaValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof MountFolderError)) {
            return false;
        }
        MountFolderError mountFolderError = (MountFolderError) obj;
        if (this._tag != mountFolderError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = mountFolderError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
            case INSIDE_SHARED_FOLDER:
                return true;
            case INSUFFICIENT_QUOTA:
                InsufficientQuotaAmounts insufficientQuotaAmounts = this.insufficientQuotaValue;
                InsufficientQuotaAmounts insufficientQuotaAmounts2 = mountFolderError.insufficientQuotaValue;
                if (insufficientQuotaAmounts != insufficientQuotaAmounts2 && !insufficientQuotaAmounts.equals(insufficientQuotaAmounts2)) {
                    z = false;
                }
                return z;
            case ALREADY_MOUNTED:
                return true;
            case NO_PERMISSION:
                return true;
            case NOT_MOUNTABLE:
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
