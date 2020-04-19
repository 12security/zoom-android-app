package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ModifySharedLinkSettingsError */
public final class ModifySharedLinkSettingsError {
    public static final ModifySharedLinkSettingsError EMAIL_NOT_VERIFIED = new ModifySharedLinkSettingsError().withTag(Tag.EMAIL_NOT_VERIFIED);
    public static final ModifySharedLinkSettingsError OTHER = new ModifySharedLinkSettingsError().withTag(Tag.OTHER);
    public static final ModifySharedLinkSettingsError SHARED_LINK_ACCESS_DENIED = new ModifySharedLinkSettingsError().withTag(Tag.SHARED_LINK_ACCESS_DENIED);
    public static final ModifySharedLinkSettingsError SHARED_LINK_NOT_FOUND = new ModifySharedLinkSettingsError().withTag(Tag.SHARED_LINK_NOT_FOUND);
    public static final ModifySharedLinkSettingsError UNSUPPORTED_LINK_TYPE = new ModifySharedLinkSettingsError().withTag(Tag.UNSUPPORTED_LINK_TYPE);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedLinkSettingsError settingsErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.ModifySharedLinkSettingsError$Serializer */
    static class Serializer extends UnionSerializer<ModifySharedLinkSettingsError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ModifySharedLinkSettingsError modifySharedLinkSettingsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (modifySharedLinkSettingsError.tag()) {
                case SHARED_LINK_NOT_FOUND:
                    jsonGenerator.writeString("shared_link_not_found");
                    return;
                case SHARED_LINK_ACCESS_DENIED:
                    jsonGenerator.writeString("shared_link_access_denied");
                    return;
                case UNSUPPORTED_LINK_TYPE:
                    jsonGenerator.writeString("unsupported_link_type");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case SETTINGS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("settings_error", jsonGenerator);
                    jsonGenerator.writeFieldName("settings_error");
                    Serializer.INSTANCE.serialize(modifySharedLinkSettingsError.settingsErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case EMAIL_NOT_VERIFIED:
                    jsonGenerator.writeString("email_not_verified");
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(modifySharedLinkSettingsError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ModifySharedLinkSettingsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ModifySharedLinkSettingsError modifySharedLinkSettingsError;
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
                if ("shared_link_not_found".equals(str)) {
                    modifySharedLinkSettingsError = ModifySharedLinkSettingsError.SHARED_LINK_NOT_FOUND;
                } else if ("shared_link_access_denied".equals(str)) {
                    modifySharedLinkSettingsError = ModifySharedLinkSettingsError.SHARED_LINK_ACCESS_DENIED;
                } else if ("unsupported_link_type".equals(str)) {
                    modifySharedLinkSettingsError = ModifySharedLinkSettingsError.UNSUPPORTED_LINK_TYPE;
                } else if ("other".equals(str)) {
                    modifySharedLinkSettingsError = ModifySharedLinkSettingsError.OTHER;
                } else if ("settings_error".equals(str)) {
                    expectField("settings_error", jsonParser);
                    modifySharedLinkSettingsError = ModifySharedLinkSettingsError.settingsError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("email_not_verified".equals(str)) {
                    modifySharedLinkSettingsError = ModifySharedLinkSettingsError.EMAIL_NOT_VERIFIED;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return modifySharedLinkSettingsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ModifySharedLinkSettingsError$Tag */
    public enum Tag {
        SHARED_LINK_NOT_FOUND,
        SHARED_LINK_ACCESS_DENIED,
        UNSUPPORTED_LINK_TYPE,
        OTHER,
        SETTINGS_ERROR,
        EMAIL_NOT_VERIFIED
    }

    private ModifySharedLinkSettingsError() {
    }

    private ModifySharedLinkSettingsError withTag(Tag tag) {
        ModifySharedLinkSettingsError modifySharedLinkSettingsError = new ModifySharedLinkSettingsError();
        modifySharedLinkSettingsError._tag = tag;
        return modifySharedLinkSettingsError;
    }

    private ModifySharedLinkSettingsError withTagAndSettingsError(Tag tag, SharedLinkSettingsError sharedLinkSettingsError) {
        ModifySharedLinkSettingsError modifySharedLinkSettingsError = new ModifySharedLinkSettingsError();
        modifySharedLinkSettingsError._tag = tag;
        modifySharedLinkSettingsError.settingsErrorValue = sharedLinkSettingsError;
        return modifySharedLinkSettingsError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isSharedLinkNotFound() {
        return this._tag == Tag.SHARED_LINK_NOT_FOUND;
    }

    public boolean isSharedLinkAccessDenied() {
        return this._tag == Tag.SHARED_LINK_ACCESS_DENIED;
    }

    public boolean isUnsupportedLinkType() {
        return this._tag == Tag.UNSUPPORTED_LINK_TYPE;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public boolean isSettingsError() {
        return this._tag == Tag.SETTINGS_ERROR;
    }

    public static ModifySharedLinkSettingsError settingsError(SharedLinkSettingsError sharedLinkSettingsError) {
        if (sharedLinkSettingsError != null) {
            return new ModifySharedLinkSettingsError().withTagAndSettingsError(Tag.SETTINGS_ERROR, sharedLinkSettingsError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharedLinkSettingsError getSettingsErrorValue() {
        if (this._tag == Tag.SETTINGS_ERROR) {
            return this.settingsErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SETTINGS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isEmailNotVerified() {
        return this._tag == Tag.EMAIL_NOT_VERIFIED;
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.settingsErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ModifySharedLinkSettingsError)) {
            return false;
        }
        ModifySharedLinkSettingsError modifySharedLinkSettingsError = (ModifySharedLinkSettingsError) obj;
        if (this._tag != modifySharedLinkSettingsError._tag) {
            return false;
        }
        switch (this._tag) {
            case SHARED_LINK_NOT_FOUND:
                return true;
            case SHARED_LINK_ACCESS_DENIED:
                return true;
            case UNSUPPORTED_LINK_TYPE:
                return true;
            case OTHER:
                return true;
            case SETTINGS_ERROR:
                SharedLinkSettingsError sharedLinkSettingsError = this.settingsErrorValue;
                SharedLinkSettingsError sharedLinkSettingsError2 = modifySharedLinkSettingsError.settingsErrorValue;
                if (sharedLinkSettingsError != sharedLinkSettingsError2 && !sharedLinkSettingsError.equals(sharedLinkSettingsError2)) {
                    z = false;
                }
                return z;
            case EMAIL_NOT_VERIFIED:
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
