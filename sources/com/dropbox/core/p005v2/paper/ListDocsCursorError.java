package com.dropbox.core.p005v2.paper;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.paper.ListDocsCursorError */
public final class ListDocsCursorError {
    public static final ListDocsCursorError OTHER = new ListDocsCursorError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public PaperApiCursorError cursorErrorValue;

    /* renamed from: com.dropbox.core.v2.paper.ListDocsCursorError$Serializer */
    static class Serializer extends UnionSerializer<ListDocsCursorError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListDocsCursorError listDocsCursorError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C07161.$SwitchMap$com$dropbox$core$v2$paper$ListDocsCursorError$Tag[listDocsCursorError.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("cursor_error", jsonGenerator);
            jsonGenerator.writeFieldName("cursor_error");
            Serializer.INSTANCE.serialize(listDocsCursorError.cursorErrorValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public ListDocsCursorError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListDocsCursorError listDocsCursorError;
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
                if ("cursor_error".equals(str)) {
                    expectField("cursor_error", jsonParser);
                    listDocsCursorError = ListDocsCursorError.cursorError(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    listDocsCursorError = ListDocsCursorError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listDocsCursorError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.paper.ListDocsCursorError$Tag */
    public enum Tag {
        CURSOR_ERROR,
        OTHER
    }

    private ListDocsCursorError() {
    }

    private ListDocsCursorError withTag(Tag tag) {
        ListDocsCursorError listDocsCursorError = new ListDocsCursorError();
        listDocsCursorError._tag = tag;
        return listDocsCursorError;
    }

    private ListDocsCursorError withTagAndCursorError(Tag tag, PaperApiCursorError paperApiCursorError) {
        ListDocsCursorError listDocsCursorError = new ListDocsCursorError();
        listDocsCursorError._tag = tag;
        listDocsCursorError.cursorErrorValue = paperApiCursorError;
        return listDocsCursorError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isCursorError() {
        return this._tag == Tag.CURSOR_ERROR;
    }

    public static ListDocsCursorError cursorError(PaperApiCursorError paperApiCursorError) {
        if (paperApiCursorError != null) {
            return new ListDocsCursorError().withTagAndCursorError(Tag.CURSOR_ERROR, paperApiCursorError);
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

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.cursorErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ListDocsCursorError)) {
            return false;
        }
        ListDocsCursorError listDocsCursorError = (ListDocsCursorError) obj;
        if (this._tag != listDocsCursorError._tag) {
            return false;
        }
        switch (this._tag) {
            case CURSOR_ERROR:
                PaperApiCursorError paperApiCursorError = this.cursorErrorValue;
                PaperApiCursorError paperApiCursorError2 = listDocsCursorError.cursorErrorValue;
                if (paperApiCursorError != paperApiCursorError2 && !paperApiCursorError.equals(paperApiCursorError2)) {
                    z = false;
                }
                return z;
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
