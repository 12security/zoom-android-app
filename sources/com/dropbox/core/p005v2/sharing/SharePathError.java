package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.SharePathError */
public final class SharePathError {
    public static final SharePathError CONTAINS_APP_FOLDER = new SharePathError().withTag(Tag.CONTAINS_APP_FOLDER);
    public static final SharePathError CONTAINS_SHARED_FOLDER = new SharePathError().withTag(Tag.CONTAINS_SHARED_FOLDER);
    public static final SharePathError CONTAINS_TEAM_FOLDER = new SharePathError().withTag(Tag.CONTAINS_TEAM_FOLDER);
    public static final SharePathError INSIDE_APP_FOLDER = new SharePathError().withTag(Tag.INSIDE_APP_FOLDER);
    public static final SharePathError INSIDE_OSX_PACKAGE = new SharePathError().withTag(Tag.INSIDE_OSX_PACKAGE);
    public static final SharePathError INSIDE_PUBLIC_FOLDER = new SharePathError().withTag(Tag.INSIDE_PUBLIC_FOLDER);
    public static final SharePathError INSIDE_SHARED_FOLDER = new SharePathError().withTag(Tag.INSIDE_SHARED_FOLDER);
    public static final SharePathError INVALID_PATH = new SharePathError().withTag(Tag.INVALID_PATH);
    public static final SharePathError IS_APP_FOLDER = new SharePathError().withTag(Tag.IS_APP_FOLDER);
    public static final SharePathError IS_FILE = new SharePathError().withTag(Tag.IS_FILE);
    public static final SharePathError IS_OSX_PACKAGE = new SharePathError().withTag(Tag.IS_OSX_PACKAGE);
    public static final SharePathError IS_PUBLIC_FOLDER = new SharePathError().withTag(Tag.IS_PUBLIC_FOLDER);
    public static final SharePathError OTHER = new SharePathError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderMetadata alreadySharedValue;

    /* renamed from: com.dropbox.core.v2.sharing.SharePathError$Serializer */
    static class Serializer extends UnionSerializer<SharePathError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharePathError sharePathError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (sharePathError.tag()) {
                case IS_FILE:
                    jsonGenerator.writeString("is_file");
                    return;
                case INSIDE_SHARED_FOLDER:
                    jsonGenerator.writeString("inside_shared_folder");
                    return;
                case CONTAINS_SHARED_FOLDER:
                    jsonGenerator.writeString("contains_shared_folder");
                    return;
                case CONTAINS_APP_FOLDER:
                    jsonGenerator.writeString("contains_app_folder");
                    return;
                case CONTAINS_TEAM_FOLDER:
                    jsonGenerator.writeString("contains_team_folder");
                    return;
                case IS_APP_FOLDER:
                    jsonGenerator.writeString("is_app_folder");
                    return;
                case INSIDE_APP_FOLDER:
                    jsonGenerator.writeString("inside_app_folder");
                    return;
                case IS_PUBLIC_FOLDER:
                    jsonGenerator.writeString("is_public_folder");
                    return;
                case INSIDE_PUBLIC_FOLDER:
                    jsonGenerator.writeString("inside_public_folder");
                    return;
                case ALREADY_SHARED:
                    jsonGenerator.writeStartObject();
                    writeTag("already_shared", jsonGenerator);
                    Serializer.INSTANCE.serialize(sharePathError.alreadySharedValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case INVALID_PATH:
                    jsonGenerator.writeString("invalid_path");
                    return;
                case IS_OSX_PACKAGE:
                    jsonGenerator.writeString("is_osx_package");
                    return;
                case INSIDE_OSX_PACKAGE:
                    jsonGenerator.writeString("inside_osx_package");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SharePathError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            SharePathError sharePathError;
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
                if ("is_file".equals(str)) {
                    sharePathError = SharePathError.IS_FILE;
                } else if ("inside_shared_folder".equals(str)) {
                    sharePathError = SharePathError.INSIDE_SHARED_FOLDER;
                } else if ("contains_shared_folder".equals(str)) {
                    sharePathError = SharePathError.CONTAINS_SHARED_FOLDER;
                } else if ("contains_app_folder".equals(str)) {
                    sharePathError = SharePathError.CONTAINS_APP_FOLDER;
                } else if ("contains_team_folder".equals(str)) {
                    sharePathError = SharePathError.CONTAINS_TEAM_FOLDER;
                } else if ("is_app_folder".equals(str)) {
                    sharePathError = SharePathError.IS_APP_FOLDER;
                } else if ("inside_app_folder".equals(str)) {
                    sharePathError = SharePathError.INSIDE_APP_FOLDER;
                } else if ("is_public_folder".equals(str)) {
                    sharePathError = SharePathError.IS_PUBLIC_FOLDER;
                } else if ("inside_public_folder".equals(str)) {
                    sharePathError = SharePathError.INSIDE_PUBLIC_FOLDER;
                } else if ("already_shared".equals(str)) {
                    sharePathError = SharePathError.alreadyShared(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("invalid_path".equals(str)) {
                    sharePathError = SharePathError.INVALID_PATH;
                } else if ("is_osx_package".equals(str)) {
                    sharePathError = SharePathError.IS_OSX_PACKAGE;
                } else if ("inside_osx_package".equals(str)) {
                    sharePathError = SharePathError.INSIDE_OSX_PACKAGE;
                } else {
                    sharePathError = SharePathError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return sharePathError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharePathError$Tag */
    public enum Tag {
        IS_FILE,
        INSIDE_SHARED_FOLDER,
        CONTAINS_SHARED_FOLDER,
        CONTAINS_APP_FOLDER,
        CONTAINS_TEAM_FOLDER,
        IS_APP_FOLDER,
        INSIDE_APP_FOLDER,
        IS_PUBLIC_FOLDER,
        INSIDE_PUBLIC_FOLDER,
        ALREADY_SHARED,
        INVALID_PATH,
        IS_OSX_PACKAGE,
        INSIDE_OSX_PACKAGE,
        OTHER
    }

    private SharePathError() {
    }

    private SharePathError withTag(Tag tag) {
        SharePathError sharePathError = new SharePathError();
        sharePathError._tag = tag;
        return sharePathError;
    }

    private SharePathError withTagAndAlreadyShared(Tag tag, SharedFolderMetadata sharedFolderMetadata) {
        SharePathError sharePathError = new SharePathError();
        sharePathError._tag = tag;
        sharePathError.alreadySharedValue = sharedFolderMetadata;
        return sharePathError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isIsFile() {
        return this._tag == Tag.IS_FILE;
    }

    public boolean isInsideSharedFolder() {
        return this._tag == Tag.INSIDE_SHARED_FOLDER;
    }

    public boolean isContainsSharedFolder() {
        return this._tag == Tag.CONTAINS_SHARED_FOLDER;
    }

    public boolean isContainsAppFolder() {
        return this._tag == Tag.CONTAINS_APP_FOLDER;
    }

    public boolean isContainsTeamFolder() {
        return this._tag == Tag.CONTAINS_TEAM_FOLDER;
    }

    public boolean isIsAppFolder() {
        return this._tag == Tag.IS_APP_FOLDER;
    }

    public boolean isInsideAppFolder() {
        return this._tag == Tag.INSIDE_APP_FOLDER;
    }

    public boolean isIsPublicFolder() {
        return this._tag == Tag.IS_PUBLIC_FOLDER;
    }

    public boolean isInsidePublicFolder() {
        return this._tag == Tag.INSIDE_PUBLIC_FOLDER;
    }

    public boolean isAlreadyShared() {
        return this._tag == Tag.ALREADY_SHARED;
    }

    public static SharePathError alreadyShared(SharedFolderMetadata sharedFolderMetadata) {
        if (sharedFolderMetadata != null) {
            return new SharePathError().withTagAndAlreadyShared(Tag.ALREADY_SHARED, sharedFolderMetadata);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharedFolderMetadata getAlreadySharedValue() {
        if (this._tag == Tag.ALREADY_SHARED) {
            return this.alreadySharedValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ALREADY_SHARED, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isInvalidPath() {
        return this._tag == Tag.INVALID_PATH;
    }

    public boolean isIsOsxPackage() {
        return this._tag == Tag.IS_OSX_PACKAGE;
    }

    public boolean isInsideOsxPackage() {
        return this._tag == Tag.INSIDE_OSX_PACKAGE;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.alreadySharedValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof SharePathError)) {
            return false;
        }
        SharePathError sharePathError = (SharePathError) obj;
        if (this._tag != sharePathError._tag) {
            return false;
        }
        switch (this._tag) {
            case IS_FILE:
                return true;
            case INSIDE_SHARED_FOLDER:
                return true;
            case CONTAINS_SHARED_FOLDER:
                return true;
            case CONTAINS_APP_FOLDER:
                return true;
            case CONTAINS_TEAM_FOLDER:
                return true;
            case IS_APP_FOLDER:
                return true;
            case INSIDE_APP_FOLDER:
                return true;
            case IS_PUBLIC_FOLDER:
                return true;
            case INSIDE_PUBLIC_FOLDER:
                return true;
            case ALREADY_SHARED:
                SharedFolderMetadata sharedFolderMetadata = this.alreadySharedValue;
                SharedFolderMetadata sharedFolderMetadata2 = sharePathError.alreadySharedValue;
                if (sharedFolderMetadata != sharedFolderMetadata2 && !sharedFolderMetadata.equals(sharedFolderMetadata2)) {
                    z = false;
                }
                return z;
            case INVALID_PATH:
                return true;
            case IS_OSX_PACKAGE:
                return true;
            case INSIDE_OSX_PACKAGE:
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
