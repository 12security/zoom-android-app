package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.paper.ListUsersCursorError */
public final class ListUsersCursorError {
    public static final ListUsersCursorError DOC_NOT_FOUND = new ListUsersCursorError().withTag(Tag.DOC_NOT_FOUND);
    public static final ListUsersCursorError INSUFFICIENT_PERMISSIONS = new ListUsersCursorError().withTag(Tag.INSUFFICIENT_PERMISSIONS);
    public static final ListUsersCursorError OTHER = new ListUsersCursorError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public PaperApiCursorError cursorErrorValue;

    /* renamed from: com.dropbox.core.v2.paper.ListUsersCursorError$Serializer */
    static class Serializer extends UnionSerializer<ListUsersCursorError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListUsersCursorError listUsersCursorError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listUsersCursorError.tag()) {
                case INSUFFICIENT_PERMISSIONS:
                    jsonGenerator.writeString("insufficient_permissions");
                    return;
                case OTHER:
                    jsonGenerator.writeString("other");
                    return;
                case DOC_NOT_FOUND:
                    jsonGenerator.writeString("doc_not_found");
                    return;
                case CURSOR_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("cursor_error", jsonGenerator);
                    jsonGenerator.writeFieldName("cursor_error");
                    Serializer.INSTANCE.serialize(listUsersCursorError.cursorErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(listUsersCursorError.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public ListUsersCursorError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListUsersCursorError listUsersCursorError;
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
                if ("insufficient_permissions".equals(str)) {
                    listUsersCursorError = ListUsersCursorError.INSUFFICIENT_PERMISSIONS;
                } else if ("other".equals(str)) {
                    listUsersCursorError = ListUsersCursorError.OTHER;
                } else if ("doc_not_found".equals(str)) {
                    listUsersCursorError = ListUsersCursorError.DOC_NOT_FOUND;
                } else if ("cursor_error".equals(str)) {
                    expectField("cursor_error", jsonParser);
                    listUsersCursorError = ListUsersCursorError.cursorError(Serializer.INSTANCE.deserialize(jsonParser));
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
                return listUsersCursorError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.paper.ListUsersCursorError$Tag */
    public enum Tag {
        INSUFFICIENT_PERMISSIONS,
        OTHER,
        DOC_NOT_FOUND,
        CURSOR_ERROR
    }

    private ListUsersCursorError() {
    }

    private ListUsersCursorError withTag(Tag tag) {
        ListUsersCursorError listUsersCursorError = new ListUsersCursorError();
        listUsersCursorError._tag = tag;
        return listUsersCursorError;
    }

    private ListUsersCursorError withTagAndCursorError(Tag tag, PaperApiCursorError paperApiCursorError) {
        ListUsersCursorError listUsersCursorError = new ListUsersCursorError();
        listUsersCursorError._tag = tag;
        listUsersCursorError.cursorErrorValue = paperApiCursorError;
        return listUsersCursorError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isInsufficientPermissions() {
        return this._tag == Tag.INSUFFICIENT_PERMISSIONS;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public boolean isDocNotFound() {
        return this._tag == Tag.DOC_NOT_FOUND;
    }

    public boolean isCursorError() {
        return this._tag == Tag.CURSOR_ERROR;
    }

    public static ListUsersCursorError cursorError(PaperApiCursorError paperApiCursorError) {
        if (paperApiCursorError != null) {
            return new ListUsersCursorError().withTagAndCursorError(Tag.CURSOR_ERROR, paperApiCursorError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public PaperApiCursorError getCursorErrorValue() {
        if (this._tag == Tag.CURSOR_ERROR) {
            return this.cursorErrorValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.CURSOR_ERROR, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this._tag, this.cursorErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ListUsersCursorError)) {
            return false;
        }
        ListUsersCursorError listUsersCursorError = (ListUsersCursorError) obj;
        if (this._tag != listUsersCursorError._tag) {
            return false;
        }
        switch (this._tag) {
            case INSUFFICIENT_PERMISSIONS:
                return true;
            case OTHER:
                return true;
            case DOC_NOT_FOUND:
                return true;
            case CURSOR_ERROR:
                PaperApiCursorError paperApiCursorError = this.cursorErrorValue;
                PaperApiCursorError paperApiCursorError2 = listUsersCursorError.cursorErrorValue;
                if (paperApiCursorError != paperApiCursorError2 && !paperApiCursorError.equals(paperApiCursorError2)) {
                    z = false;
                }
                return z;
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
