package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.GetSharedLinksError */
public final class GetSharedLinksError {
    public static final GetSharedLinksError OTHER = new GetSharedLinksError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String pathValue;

    /* renamed from: com.dropbox.core.v2.sharing.GetSharedLinksError$Serializer */
    static class Serializer extends UnionSerializer<GetSharedLinksError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetSharedLinksError getSharedLinksError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C07461.$SwitchMap$com$dropbox$core$v2$sharing$GetSharedLinksError$Tag[getSharedLinksError.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("path", jsonGenerator);
            jsonGenerator.writeFieldName("path");
            StoneSerializers.nullable(StoneSerializers.string()).serialize(getSharedLinksError.pathValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public GetSharedLinksError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GetSharedLinksError getSharedLinksError;
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
                if ("path".equals(str)) {
                    String str2 = null;
                    if (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
                        expectField("path", jsonParser);
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    }
                    if (str2 == null) {
                        getSharedLinksError = GetSharedLinksError.path();
                    } else {
                        getSharedLinksError = GetSharedLinksError.path(str2);
                    }
                } else {
                    getSharedLinksError = GetSharedLinksError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return getSharedLinksError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.GetSharedLinksError$Tag */
    public enum Tag {
        PATH,
        OTHER
    }

    private GetSharedLinksError() {
    }

    private GetSharedLinksError withTag(Tag tag) {
        GetSharedLinksError getSharedLinksError = new GetSharedLinksError();
        getSharedLinksError._tag = tag;
        return getSharedLinksError;
    }

    private GetSharedLinksError withTagAndPath(Tag tag, String str) {
        GetSharedLinksError getSharedLinksError = new GetSharedLinksError();
        getSharedLinksError._tag = tag;
        getSharedLinksError.pathValue = str;
        return getSharedLinksError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static GetSharedLinksError path(String str) {
        return new GetSharedLinksError().withTagAndPath(Tag.PATH, str);
    }

    public static GetSharedLinksError path() {
        return path(null);
    }

    public String getPathValue() {
        if (this._tag == Tag.PATH) {
            return this.pathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GetSharedLinksError)) {
            return false;
        }
        GetSharedLinksError getSharedLinksError = (GetSharedLinksError) obj;
        if (this._tag != getSharedLinksError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                String str = this.pathValue;
                String str2 = getSharedLinksError.pathValue;
                if (str != str2 && (str == null || !str.equals(str2))) {
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
