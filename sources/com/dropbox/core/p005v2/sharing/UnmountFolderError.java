package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.UnmountFolderError */
public final class UnmountFolderError {
    public static final UnmountFolderError NOT_UNMOUNTABLE = new UnmountFolderError().withTag(Tag.NOT_UNMOUNTABLE);
    public static final UnmountFolderError NO_PERMISSION = new UnmountFolderError().withTag(Tag.NO_PERMISSION);
    public static final UnmountFolderError OTHER = new UnmountFolderError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.UnmountFolderError$Serializer */
    static class Serializer extends UnionSerializer<UnmountFolderError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UnmountFolderError unmountFolderError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (unmountFolderError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(unmountFolderError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                case NOT_UNMOUNTABLE:
                    jsonGenerator.writeString("not_unmountable");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UnmountFolderError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            UnmountFolderError unmountFolderError;
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
                    unmountFolderError = UnmountFolderError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("no_permission".equals(str)) {
                    unmountFolderError = UnmountFolderError.NO_PERMISSION;
                } else if ("not_unmountable".equals(str)) {
                    unmountFolderError = UnmountFolderError.NOT_UNMOUNTABLE;
                } else {
                    unmountFolderError = UnmountFolderError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return unmountFolderError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.UnmountFolderError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        NO_PERMISSION,
        NOT_UNMOUNTABLE,
        OTHER
    }

    private UnmountFolderError() {
    }

    private UnmountFolderError withTag(Tag tag) {
        UnmountFolderError unmountFolderError = new UnmountFolderError();
        unmountFolderError._tag = tag;
        return unmountFolderError;
    }

    private UnmountFolderError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        UnmountFolderError unmountFolderError = new UnmountFolderError();
        unmountFolderError._tag = tag;
        unmountFolderError.accessErrorValue = sharedFolderAccessError;
        return unmountFolderError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static UnmountFolderError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new UnmountFolderError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isNotUnmountable() {
        return this._tag == Tag.NOT_UNMOUNTABLE;
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
        if (obj == null || !(obj instanceof UnmountFolderError)) {
            return false;
        }
        UnmountFolderError unmountFolderError = (UnmountFolderError) obj;
        if (this._tag != unmountFolderError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = unmountFolderError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
            case NO_PERMISSION:
                return true;
            case NOT_UNMOUNTABLE:
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
