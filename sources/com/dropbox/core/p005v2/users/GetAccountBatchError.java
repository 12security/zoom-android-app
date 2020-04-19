package com.dropbox.core.p005v2.users;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.users.GetAccountBatchError */
public final class GetAccountBatchError {
    public static final GetAccountBatchError OTHER = new GetAccountBatchError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String noAccountValue;

    /* renamed from: com.dropbox.core.v2.users.GetAccountBatchError$Serializer */
    public static class Serializer extends UnionSerializer<GetAccountBatchError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(GetAccountBatchError getAccountBatchError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C09511.$SwitchMap$com$dropbox$core$v2$users$GetAccountBatchError$Tag[getAccountBatchError.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("no_account", jsonGenerator);
            jsonGenerator.writeFieldName("no_account");
            StoneSerializers.string().serialize(getAccountBatchError.noAccountValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public GetAccountBatchError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GetAccountBatchError getAccountBatchError;
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
                if ("no_account".equals(str)) {
                    expectField("no_account", jsonParser);
                    getAccountBatchError = GetAccountBatchError.noAccount((String) StoneSerializers.string().deserialize(jsonParser));
                } else {
                    getAccountBatchError = GetAccountBatchError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return getAccountBatchError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.users.GetAccountBatchError$Tag */
    public enum Tag {
        NO_ACCOUNT,
        OTHER
    }

    private GetAccountBatchError() {
    }

    private GetAccountBatchError withTag(Tag tag) {
        GetAccountBatchError getAccountBatchError = new GetAccountBatchError();
        getAccountBatchError._tag = tag;
        return getAccountBatchError;
    }

    private GetAccountBatchError withTagAndNoAccount(Tag tag, String str) {
        GetAccountBatchError getAccountBatchError = new GetAccountBatchError();
        getAccountBatchError._tag = tag;
        getAccountBatchError.noAccountValue = str;
        return getAccountBatchError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isNoAccount() {
        return this._tag == Tag.NO_ACCOUNT;
    }

    public static GetAccountBatchError noAccount(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() < 40) {
            throw new IllegalArgumentException("String is shorter than 40");
        } else if (str.length() <= 40) {
            return new GetAccountBatchError().withTagAndNoAccount(Tag.NO_ACCOUNT, str);
        } else {
            throw new IllegalArgumentException("String is longer than 40");
        }
    }

    public String getNoAccountValue() {
        if (this._tag == Tag.NO_ACCOUNT) {
            return this.noAccountValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.NO_ACCOUNT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.noAccountValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GetAccountBatchError)) {
            return false;
        }
        GetAccountBatchError getAccountBatchError = (GetAccountBatchError) obj;
        if (this._tag != getAccountBatchError._tag) {
            return false;
        }
        switch (this._tag) {
            case NO_ACCOUNT:
                String str = this.noAccountValue;
                String str2 = getAccountBatchError.noAccountValue;
                if (str != str2 && !str.equals(str2)) {
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
