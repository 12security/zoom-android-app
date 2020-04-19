package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.ListFileMembersIndividualResult */
public final class ListFileMembersIndividualResult {
    public static final ListFileMembersIndividualResult OTHER = new ListFileMembersIndividualResult().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public SharingFileAccessError accessErrorValue;
    /* access modifiers changed from: private */
    public ListFileMembersCountResult resultValue;

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersIndividualResult$Serializer */
    static class Serializer extends UnionSerializer<ListFileMembersIndividualResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFileMembersIndividualResult listFileMembersIndividualResult, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (listFileMembersIndividualResult.tag()) {
                case RESULT:
                    jsonGenerator.writeStartObject();
                    writeTag("result", jsonGenerator);
                    Serializer.INSTANCE.serialize(listFileMembersIndividualResult.resultValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case ACCESS_ERROR:
                    jsonGenerator.writeStartObject();
                    writeTag("access_error", jsonGenerator);
                    jsonGenerator.writeFieldName("access_error");
                    Serializer.INSTANCE.serialize(listFileMembersIndividualResult.accessErrorValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public ListFileMembersIndividualResult deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            ListFileMembersIndividualResult listFileMembersIndividualResult;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if ("result".equals(str)) {
                    listFileMembersIndividualResult = ListFileMembersIndividualResult.result(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("access_error".equals(str)) {
                    expectField("access_error", jsonParser);
                    listFileMembersIndividualResult = ListFileMembersIndividualResult.accessError(Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    listFileMembersIndividualResult = ListFileMembersIndividualResult.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return listFileMembersIndividualResult;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ListFileMembersIndividualResult$Tag */
    public enum Tag {
        RESULT,
        ACCESS_ERROR,
        OTHER
    }

    private ListFileMembersIndividualResult() {
    }

    private ListFileMembersIndividualResult withTag(Tag tag) {
        ListFileMembersIndividualResult listFileMembersIndividualResult = new ListFileMembersIndividualResult();
        listFileMembersIndividualResult._tag = tag;
        return listFileMembersIndividualResult;
    }

    private ListFileMembersIndividualResult withTagAndResult(Tag tag, ListFileMembersCountResult listFileMembersCountResult) {
        ListFileMembersIndividualResult listFileMembersIndividualResult = new ListFileMembersIndividualResult();
        listFileMembersIndividualResult._tag = tag;
        listFileMembersIndividualResult.resultValue = listFileMembersCountResult;
        return listFileMembersIndividualResult;
    }

    private ListFileMembersIndividualResult withTagAndAccessError(Tag tag, SharingFileAccessError sharingFileAccessError) {
        ListFileMembersIndividualResult listFileMembersIndividualResult = new ListFileMembersIndividualResult();
        listFileMembersIndividualResult._tag = tag;
        listFileMembersIndividualResult.accessErrorValue = sharingFileAccessError;
        return listFileMembersIndividualResult;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isResult() {
        return this._tag == Tag.RESULT;
    }

    public static ListFileMembersIndividualResult result(ListFileMembersCountResult listFileMembersCountResult) {
        if (listFileMembersCountResult != null) {
            return new ListFileMembersIndividualResult().withTagAndResult(Tag.RESULT, listFileMembersCountResult);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public ListFileMembersCountResult getResultValue() {
        if (this._tag == Tag.RESULT) {
            return this.resultValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.RESULT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isAccessError() {
        return this._tag == Tag.ACCESS_ERROR;
    }

    public static ListFileMembersIndividualResult accessError(SharingFileAccessError sharingFileAccessError) {
        if (sharingFileAccessError != null) {
            return new ListFileMembersIndividualResult().withTagAndAccessError(Tag.ACCESS_ERROR, sharingFileAccessError);
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
        return Arrays.hashCode(new Object[]{this._tag, this.resultValue, this.accessErrorValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ListFileMembersIndividualResult)) {
            return false;
        }
        ListFileMembersIndividualResult listFileMembersIndividualResult = (ListFileMembersIndividualResult) obj;
        if (this._tag != listFileMembersIndividualResult._tag) {
            return false;
        }
        switch (this._tag) {
            case RESULT:
                ListFileMembersCountResult listFileMembersCountResult = this.resultValue;
                ListFileMembersCountResult listFileMembersCountResult2 = listFileMembersIndividualResult.resultValue;
                if (listFileMembersCountResult != listFileMembersCountResult2 && !listFileMembersCountResult.equals(listFileMembersCountResult2)) {
                    z = false;
                }
                return z;
            case ACCESS_ERROR:
                SharingFileAccessError sharingFileAccessError = this.accessErrorValue;
                SharingFileAccessError sharingFileAccessError2 = listFileMembersIndividualResult.accessErrorValue;
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
