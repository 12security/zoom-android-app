package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersError */
public final class ListFileMembersError {
    public static final ListFileMembersError OTHER = new ListFileMembersError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public SharingUserError userErrorValue;

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersError$Serializer */
    static class Serializer extends UnionSerializer<ListFileMembersError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFileMembersError listFileMembersError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listFileMembersError.tag()) {
                case USER_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("user_error", jsonGenerator);
                    jsonGenerator.writeFieldName("user_error");
                    Serializer.INSTANCE.serialize(listFileMembersError.userErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(listFileMembersError.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ListFileMembersError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            ListFileMembersError listFileMembersError;
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
                    listFileMembersError = ListFileMembersError.userError(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    listFileMembersError = ListFileMembersError.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    listFileMembersError = ListFileMembersError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFileMembersError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersError$Tag */
    public enum Tag {
        USER_ERROR,
        ACCESS_ERROR,
        OTHER
    }

    private ListFileMembersError() {
    }

    private ListFileMembersError withTag(Tag tag) {
        ListFileMembersError listFileMembersError = new ListFileMembersError();
        listFileMembersError._tag = tag;
        return listFileMembersError;
    }

    private ListFileMembersError withTagAndUserError(Tag tag, SharingUserError sharingUserError) {
        ListFileMembersError listFileMembersError = new ListFileMembersError();
        listFileMembersError._tag = tag;
        listFileMembersError.userErrorValue = sharingUserError;
        return listFileMembersError;
    }

    private ListFileMembersError withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        ListFileMembersError listFileMembersError = new ListFileMembersError();
        listFileMembersError._tag = tag;
        listFileMembersError.accessErrorValue = sharingFileAccessError;
        return listFileMembersError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isUserError() {
        return this._tag == Tag.USER_ERROR;
    }

    public static ListFileMembersError userError(SharingUserError sharingUserError) {
        if (sharingUserError != null) {
            return new ListFileMembersError().withTagAndUserError(Tag.USER_ERROR, sharingUserError);
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

    public static ListFileMembersError accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new ListFileMembersError().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
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
        if (obj == null || !(obj instanceof ListFileMembersError)) {
            return false;
        }
        ListFileMembersError listFileMembersError = (ListFileMembersError) obj;
        if (this._tag != listFileMembersError._tag) {
            return false;
        }
        switch (this._tag) {
            case USER_ERROR:
                SharingUserError sharingUserError = this.userErrorValue;
                SharingUserError sharingUserError2 = listFileMembersError.userErrorValue;
                if (sharingUserError != sharingUserError2 && !sharingUserError.equals(sharingUserError2)) {
                    z = false;
                }
                return z;
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = listFileMembersError.accessErrorValue;
                if (sharingFileAccessError != sharingFileAccessError2 && !sharingFileAccessError.equals(sharingFileAccessError2)) {
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
