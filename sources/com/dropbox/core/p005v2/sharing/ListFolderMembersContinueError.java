package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersContinueError */
public final class ListFolderMembersContinueError {
    public static final ListFolderMembersContinueError INVALID_CURSOR = new ListFolderMembersContinueError().withTag(Tag.INVALID_CURSOR);
    public static final ListFolderMembersContinueError OTHER = new ListFolderMembersContinueError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharedFolderAccessError accessErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersContinueError$Serializer */
    static class Serializer extends UnionSerializer<ListFolderMembersContinueError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFolderMembersContinueError listFolderMembersContinueError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listFolderMembersContinueError.tag()) {
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(listFolderMembersContinueError.accessErrorValue, jsonGenerator);
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

        public ListFolderMembersContinueError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFolderMembersContinueError listFolderMembersContinueError;
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
                    listFolderMembersContinueError = ListFolderMembersContinueError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("invalid_cursor".equals(str)) {
                    listFolderMembersContinueError = ListFolderMembersContinueError.INVALID_CURSOR;
                } else {
                    listFolderMembersContinueError = ListFolderMembersContinueError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFolderMembersContinueError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFolderMembersContinueError$Tag */
    public enum Tag {
        ACCESS_ERROR,
        INVALID_CURSOR,
        OTHER
    }

    private ListFolderMembersContinueError() {
    }

    private ListFolderMembersContinueError withTag(Tag tag) {
        ListFolderMembersContinueError listFolderMembersContinueError = new ListFolderMembersContinueError();
        listFolderMembersContinueError._tag = tag;
        return listFolderMembersContinueError;
    }

    private ListFolderMembersContinueError withTagAndAccessError(Tag tag, SharedFolderAccessError sharedFolderAccessError) {
        ListFolderMembersContinueError listFolderMembersContinueError = new ListFolderMembersContinueError();
        listFolderMembersContinueError._tag = tag;
        listFolderMembersContinueError.accessErrorValue = sharedFolderAccessError;
        return listFolderMembersContinueError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static ListFolderMembersContinueError accessError(SharedFolderAccessError sharedFolderAccessError) {
        if (sharedFolderAccessError != null) {
            return new ListFolderMembersContinueError().withTagAndAccessError(Tag.ACCESS_ERROR, sharedFolderAccessError);
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

    public boolean isInvalidCursor() {
        return this._tag == Tag.INVALID_CURSOR;
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
        if (obj == null || !(obj instanceof ListFolderMembersContinueError)) {
            return false;
        }
        ListFolderMembersContinueError listFolderMembersContinueError = (ListFolderMembersContinueError) obj;
        if (this._tag != listFolderMembersContinueError._tag) {
            return false;
        }
        switch (this._tag) {
            case ACCESS_ERROR:
                SharedFolderAccessError sharedFolderAccessError = this.accessErrorValue;
                SharedFolderAccessError sharedFolderAccessError2 = listFolderMembersContinueError.accessErrorValue;
                if (sharedFolderAccessError != sharedFolderAccessError2 && !sharedFolderAccessError.equals(sharedFolderAccessError2)) {
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
