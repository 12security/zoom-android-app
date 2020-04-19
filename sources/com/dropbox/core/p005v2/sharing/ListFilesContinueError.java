package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ListFilesContinueError */
public final class ListFilesContinueError {
    public static final ListFilesContinueError INVALID_CURSOR = new ListFilesContinueError().withTag(Tag.INVALID_CURSOR);
    public static final ListFilesContinueError OTHER = new ListFilesContinueError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingUserError userErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.ListFilesContinueError$Serializer */
    static class Serializer extends UnionSerializer<ListFilesContinueError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFilesContinueError listFilesContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listFilesContinueError.tag()) {
                case USER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("user_error", jsonGenerator);
                    jsonGenerator.writeFieldName("user_error");
                    Serializer.INSTANCE.serialize(listFilesContinueError.userErrorValue, jsonGenerator);
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

        public ListFilesContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFilesContinueError listFilesContinueError;
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
                    listFilesContinueError = ListFilesContinueError.userError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("invalid_cursor".equals(str)) {
                    listFilesContinueError = ListFilesContinueError.INVALID_CURSOR;
                } else {
                    listFilesContinueError = ListFilesContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFilesContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFilesContinueError$Tag */
    public enum Tag {
        USER_ERROR,
        INVALID_CURSOR,
        OTHER
    }

    private ListFilesContinueError() {
    }

    private ListFilesContinueError withTag(Tag tag) {
        ListFilesContinueError listFilesContinueError = new ListFilesContinueError();
        listFilesContinueError._tag = tag;
        return listFilesContinueError;
    }

    private ListFilesContinueError withTagAndUserError(Tag tag, SharingUserError sharingUserError) {
        ListFilesContinueError listFilesContinueError = new ListFilesContinueError();
        listFilesContinueError._tag = tag;
        listFilesContinueError.userErrorValue = sharingUserError;
        return listFilesContinueError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUserError() {
        return this._tag == Tag.USER_ERROR;
    }

    public static ListFilesContinueError userError(SharingUserError sharingUserError) {
        if (sharingUserError != null) {
            return new ListFilesContinueError().withTagAndUserError(Tag.USER_ERROR, sharingUserError);
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

    public boolean isInvalidCursor() {
        return this._tag == Tag.INVALID_CURSOR;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.userErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ListFilesContinueError)) {
            return false;
        }
        ListFilesContinueError listFilesContinueError = (ListFilesContinueError) obj;
        if (this._tag != listFilesContinueError._tag) {
            return false;
        }
        switch (this._tag) {
            case USER_ERROR:
                SharingUserError sharingUserError = this.userErrorValue;
                SharingUserError sharingUserError2 = listFilesContinueError.userErrorValue;
                if (sharingUserError != sharingUserError2 && !sharingUserError.equals(sharingUserError2)) {
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
