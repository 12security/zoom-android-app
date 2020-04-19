package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.UnshareFolderError */
public final class UnshareFolderError {
    public static final UnshareFolderError NO_PERMISSION = new UnshareFolderError().withTag(Tag.NO_PERMISSION);
    public static final UnshareFolderError OTHER = new UnshareFolderError().withTag(Tag.OTHER);
    public static final UnshareFolderError TEAM_FOLDER = new UnshareFolderError().withTag(Tag.TEAM_FOLDER);
    public static final UnshareFolderError TOO_MANY_FILES = new UnshareFolderError().withTag(Tag.TOO_MANY_FILES);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.UnshareFolderError$Serializer */
    static class Serializer extends UnionSerializer<UnshareFolderError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UnshareFolderError unshareFolderError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (unshareFolderError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(unshareFolderError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
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

        public UnshareFolderError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UnshareFolderError unshareFolderError;
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
                    unshareFolderError = UnshareFolderError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("team_folder".equals(str)) {
                    unshareFolderError = UnshareFolderError.TEAM_FOLDER;
                } else if ("no_permission".equals(str)) {
                    unshareFolderError = UnshareFolderError.NO_PERMISSION;
                } else if ("too_many_files".equals(str)) {
                    unshareFolderError = UnshareFolderError.TOO_MANY_FILES;
                } else {
                    unshareFolderError = UnshareFolderError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return unshareFolderError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.UnshareFolderError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        TEAM_FOLDER,
        NO_PERMISSION,
        TOO_MANY_FILES,
        OTHER
    }

    private UnshareFolderError() {
    }

    private UnshareFolderError withTag(Tag tag) {
        UnshareFolderError unshareFolderError = new UnshareFolderError();
        unshareFolderError._tag = tag;
        return unshareFolderError;
    }

    private UnshareFolderError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        UnshareFolderError unshareFolderError = new UnshareFolderError();
        unshareFolderError._tag = tag;
        unshareFolderError.accessErrorValue = sharedFolderAccessError;
        return unshareFolderError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static UnshareFolderError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new UnshareFolderError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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
        return Arrays.hashCode(new Object[]{this._tag, this.accessErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UnshareFolderError)) {
            return false;
        }
        UnshareFolderError unshareFolderError = (UnshareFolderError) obj;
        if (this._tag != unshareFolderError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = unshareFolderError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
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
