package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.SetAccessInheritanceError */
public final class SetAccessInheritanceError {
    public static final SetAccessInheritanceError NO_PERMISSION = new SetAccessInheritanceError().withTag(Tag.NO_PERMISSION);
    public static final SetAccessInheritanceError OTHER = new SetAccessInheritanceError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.SetAccessInheritanceError$Serializer */
    static class Serializer extends UnionSerializer<SetAccessInheritanceError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SetAccessInheritanceError setAccessInheritanceError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (setAccessInheritanceError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(setAccessInheritanceError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SetAccessInheritanceError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SetAccessInheritanceError setAccessInheritanceError;
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
                    setAccessInheritanceError = SetAccessInheritanceError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("no_permission".equals(str)) {
                    setAccessInheritanceError = SetAccessInheritanceError.NO_PERMISSION;
                } else {
                    setAccessInheritanceError = SetAccessInheritanceError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return setAccessInheritanceError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SetAccessInheritanceError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        NO_PERMISSION,
        OTHER
    }

    private SetAccessInheritanceError() {
    }

    private SetAccessInheritanceError withTag(Tag tag) {
        SetAccessInheritanceError setAccessInheritanceError = new SetAccessInheritanceError();
        setAccessInheritanceError._tag = tag;
        return setAccessInheritanceError;
    }

    private SetAccessInheritanceError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        SetAccessInheritanceError setAccessInheritanceError = new SetAccessInheritanceError();
        setAccessInheritanceError._tag = tag;
        setAccessInheritanceError.accessErrorValue = sharedFolderAccessError;
        return setAccessInheritanceError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static SetAccessInheritanceError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new SetAccessInheritanceError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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
        if (obj == null || !(obj instanceof SetAccessInheritanceError)) {
            return false;
        }
        SetAccessInheritanceError setAccessInheritanceError = (SetAccessInheritanceError) obj;
        if (this._tag != setAccessInheritanceError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = setAccessInheritanceError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
                    z = false;
                }
                return z;
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
