package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.p005v2.files.LookupError;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsError */
public final class CreateSharedLinkWithSettingsError {
    public static final CreateSharedLinkWithSettingsError ACCESS_DENIED = new CreateSharedLinkWithSettingsError().withTag(Tag.ACCESS_DENIED);
    public static final CreateSharedLinkWithSettingsError EMAIL_NOT_VERIFIED = new CreateSharedLinkWithSettingsError().withTag(Tag.EMAIL_NOT_VERIFIED);
    public static final CreateSharedLinkWithSettingsError SHARED_LINK_ALREADY_EXISTS = new CreateSharedLinkWithSettingsError().withTag(Tag.SHARED_LINK_ALREADY_EXISTS);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;
    /* access modifiers changed from: private */
    public SharedLinkSettingsError settingsErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsError$Serializer */
    static class Serializer extends UnionSerializer<CreateSharedLinkWithSettingsError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateSharedLinkWithSettingsError createSharedLinkWithSettingsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (createSharedLinkWithSettingsError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(createSharedLinkWithSettingsError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case EMAIL_NOT_VERIFIED:
                    jsonGenerator.writeString("email_not_verified");
                    return;
                case SHARED_LINK_ALREADY_EXISTS:
                    jsonGenerator.writeString("shared_link_already_exists");
                    return;
                case SETTINGS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("settings_error", jsonGenerator);
                    jsonGenerator.writeFieldName("settings_error");
                    Serializer.INSTANCE.serialize(createSharedLinkWithSettingsError.settingsErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case ACCESS_DENIED:
                    jsonGenerator.writeString(AAD.WEB_UI_CANCEL);
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(createSharedLinkWithSettingsError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public CreateSharedLinkWithSettingsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            CreateSharedLinkWithSettingsError createSharedLinkWithSettingsError;
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
                if ("path".equals(str)) {
                    expectField("path", jsonParser);
                    createSharedLinkWithSettingsError = CreateSharedLinkWithSettingsError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("email_not_verified".equals(str)) {
                    createSharedLinkWithSettingsError = CreateSharedLinkWithSettingsError.EMAIL_NOT_VERIFIED;
                } else if ("shared_link_already_exists".equals(str)) {
                    createSharedLinkWithSettingsError = CreateSharedLinkWithSettingsError.SHARED_LINK_ALREADY_EXISTS;
                } else if ("settings_error".equals(str)) {
                    expectField("settings_error", jsonParser);
                    createSharedLinkWithSettingsError = CreateSharedLinkWithSettingsError.settingsError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if (AAD.WEB_UI_CANCEL.equals(str)) {
                    createSharedLinkWithSettingsError = CreateSharedLinkWithSettingsError.ACCESS_DENIED;
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
                return createSharedLinkWithSettingsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsError$Tag */
    public enum Tag {
        PATH,
        EMAIL_NOT_VERIFIED,
        SHARED_LINK_ALREADY_EXISTS,
        SETTINGS_ERROR,
        ACCESS_DENIED
    }

    private CreateSharedLinkWithSettingsError() {
    }

    private CreateSharedLinkWithSettingsError withTag(Tag tag) {
        CreateSharedLinkWithSettingsError createSharedLinkWithSettingsError = new CreateSharedLinkWithSettingsError();
        createSharedLinkWithSettingsError._tag = tag;
        return createSharedLinkWithSettingsError;
    }

    private CreateSharedLinkWithSettingsError withTagAndPath(Tag tag, LookupError lookupError) {
        CreateSharedLinkWithSettingsError createSharedLinkWithSettingsError = new CreateSharedLinkWithSettingsError();
        createSharedLinkWithSettingsError._tag = tag;
        createSharedLinkWithSettingsError.pathValue = lookupError;
        return createSharedLinkWithSettingsError;
    }

    private CreateSharedLinkWithSettingsError withTagAndSettingsError(Tag tag, SharedLinkSettingsError sharedLinkSettingsError) {
        CreateSharedLinkWithSettingsError createSharedLinkWithSettingsError = new CreateSharedLinkWithSettingsError();
        createSharedLinkWithSettingsError._tag = tag;
        createSharedLinkWithSettingsError.settingsErrorValue = sharedLinkSettingsError;
        return createSharedLinkWithSettingsError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static CreateSharedLinkWithSettingsError path(LookupError lookupError) {
        if (lookupError != null) {
            return new CreateSharedLinkWithSettingsError().withTagAndPath(Tag.PATH, lookupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookupError getPathValue() {
        if (this._tag == Tag.PATH) {
            return this.pathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isEmailNotVerified() {
        return this._tag == Tag.EMAIL_NOT_VERIFIED;
    }

    public boolean isSharedLinkAlreadyExists() {
        return this._tag == Tag.SHARED_LINK_ALREADY_EXISTS;
    }

    public boolean isSettingsError() {
        return this._tag == Tag.SETTINGS_ERROR;
    }

    public static CreateSharedLinkWithSettingsError settingsError(SharedLinkSettingsError sharedLinkSettingsError) {
        if (sharedLinkSettingsError != null) {
            return new CreateSharedLinkWithSettingsError().withTagAndSettingsError(Tag.SETTINGS_ERROR, sharedLinkSettingsError);
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

    public boolean isAccessDenied() {
        return this._tag == Tag.ACCESS_DENIED;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathValue, this.settingsErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof CreateSharedLinkWithSettingsError)) {
            return false;
        }
        CreateSharedLinkWithSettingsError createSharedLinkWithSettingsError = (CreateSharedLinkWithSettingsError) obj;
        if (this._tag != createSharedLinkWithSettingsError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = createSharedLinkWithSettingsError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case EMAIL_NOT_VERIFIED:
                return true;
            case SHARED_LINK_ALREADY_EXISTS:
                return true;
            case SETTINGS_ERROR:
                SharedLinkSettingsError sharedLinkSettingsError = this.settingsErrorValue;
                SharedLinkSettingsError sharedLinkSettingsError2 = createSharedLinkWithSettingsError.settingsErrorValue;
                if (sharedLinkSettingsError != sharedLinkSettingsError2 && !sharedLinkSettingsError.equals(sharedLinkSettingsError2)) {
                    z = false;
                }
                return z;
            case ACCESS_DENIED:
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
