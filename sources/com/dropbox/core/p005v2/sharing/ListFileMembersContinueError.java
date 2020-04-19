package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersContinueError */
public final class ListFileMembersContinueError {
    public static final ListFileMembersContinueError INVALID_CURSOR = new ListFileMembersContinueError().withTag(Tag.INVALID_CURSOR);
    public static final ListFileMembersContinueError OTHER = new ListFileMembersContinueError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public SharingUserError userErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersContinueError$Serializer */
    static class Serializer extends UnionSerializer<ListFileMembersContinueError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFileMembersContinueError listFileMembersContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listFileMembersContinueError.tag()) {
                case USER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("user_error", jsonGenerator);
                    jsonGenerator.writeFieldName("user_error");
                    Serializer.INSTANCE.serialize(listFileMembersContinueError.userErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(listFileMembersContinueError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case INVALID_CURSOR:
                    jsonGenerator.writeString("invalid_cursor");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ListFileMembersContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFileMembersContinueError listFileMembersContinueError;
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
                if ("user_error".equals(str)) {
                    expectField("user_error", jsonParser);
                    listFileMembersContinueError = ListFileMembersContinueError.userError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    listFileMembersContinueError = ListFileMembersContinueError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("invalid_cursor".equals(str)) {
                    listFileMembersContinueError = ListFileMembersContinueError.INVALID_CURSOR;
                } else {
                    listFileMembersContinueError = ListFileMembersContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFileMembersContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersContinueError$Tag */
    public enum Tag {
        USER_ERROR,
        ACCESS_ERROR,
        INVALID_CURSOR,
        OTHER
    }

    private ListFileMembersContinueError() {
    }

    private ListFileMembersContinueError withTag(Tag tag) {
        ListFileMembersContinueError listFileMembersContinueError = new ListFileMembersContinueError();
        listFileMembersContinueError._tag = tag;
        return listFileMembersContinueError;
    }

    private ListFileMembersContinueError withTagAndUserError(Tag tag, SharingUserError sharingUserError) {
        ListFileMembersContinueError listFileMembersContinueError = new ListFileMembersContinueError();
        listFileMembersContinueError._tag = tag;
        listFileMembersContinueError.userErrorValue = sharingUserError;
        return listFileMembersContinueError;
    }

    private ListFileMembersContinueError withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        ListFileMembersContinueError listFileMembersContinueError = new ListFileMembersContinueError();
        listFileMembersContinueError._tag = tag;
        listFileMembersContinueError.accessErrorValue = sharingFileAccessError;
        return listFileMembersContinueError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUserError() {
        return this._tag == Tag.USER_ERROR;
    }

    public static ListFileMembersContinueError userError(SharingUserError sharingUserError) {
        if (sharingUserError != null) {
            return new ListFileMembersContinueError().withTagAndUserError(Tag.USER_ERROR, sharingUserError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharingUserError getUserErrorValue() {
        if (this._tag == Tag.USER_ERROR) {
            return this.userErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.USER_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static ListFileMembersContinueError accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new ListFileMembersContinueError().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public SharingFileAccessError getAccessErrorValue() {
        if (this._tag == Tag.ACCESS_ERROR) {
            return this.accessErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ACCESS_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isInvalidCursor() {
        return this._tag == Tag.INVALID_CURSOR;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.userErrorValue, this.accessErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ListFileMembersContinueError)) {
            return false;
        }
        ListFileMembersContinueError listFileMembersContinueError = (ListFileMembersContinueError) obj;
        if (this._tag != listFileMembersContinueError._tag) {
            return false;
        }
        switch (this._tag) {
            case USER_ERROR:
                SharingUserError sharingUserError = this.userErrorValue;
                SharingUserError sharingUserError2 = listFileMembersContinueError.userErrorValue;
                if (sharingUserError != sharingUserError2 && !sharingUserError.equals(sharingUserError2)) {
                    z = false;
                }
                return z;
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = listFileMembersContinueError.accessErrorValue;
                if (sharingFileAccessError != sharingFileAccessError2 && !sharingFileAccessError.equals(sharingFileAccessError2)) {
                    z = false;
                }
                return z;
            case INVALID_CURSOR:
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
