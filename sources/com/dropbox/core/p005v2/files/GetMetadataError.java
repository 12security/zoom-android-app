package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.GetMetadataError */
public final class GetMetadataError {
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.files.GetMetadataError$1 */
    static /* synthetic */ class C06701 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$files$GetMetadataError$Tag = new int[Tag.values().length];

        static {
            try {
                $SwitchMap$com$dropbox$core$v2$files$GetMetadataError$Tag[Tag.PATH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.files.GetMetadataError$Serializer */
    static class Serializer extends UnionSerializer<GetMetadataError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetMetadataError getMetadataError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06701.$SwitchMap$com$dropbox$core$v2$files$GetMetadataError$Tag[getMetadataError.tag().ordinal()] == 1) {
                jsonGenerator.writeStartObject();
                writeTag("path", jsonGenerator);
                jsonGenerator.writeFieldName("path");
                com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(getMetadataError.pathValue, jsonGenerator);
                jsonGenerator.writeEndObject();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized tag: ");
            sb.append(getMetadataError.tag());
            throw new IllegalArgumentException(sb.toString());
        }

        public GetMetadataError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str == null) {
                throw new JsonParseException(jsonParser, "Required field missing: .tag");
            } else if ("path".equals(str)) {
                expectField("path", jsonParser);
                GetMetadataError path = GetMetadataError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return path;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown tag: ");
                sb.append(str);
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.files.GetMetadataError$Tag */
    public enum Tag {
        PATH
    }

    private GetMetadataError() {
    }

    private GetMetadataError withTag(Tag tag) {
        GetMetadataError getMetadataError = new GetMetadataError();
        getMetadataError._tag = tag;
        return getMetadataError;
    }

    private GetMetadataError withTagAndPath(Tag tag, LookupError lookupError) {
        GetMetadataError getMetadataError = new GetMetadataError();
        getMetadataError._tag = tag;
        getMetadataError.pathValue = lookupError;
        return getMetadataError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static GetMetadataError path(LookupError lookupError) {
        if (lookupError != null) {
            return new GetMetadataError().withTagAndPath(Tag.PATH, lookupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookupError getPathValue() {
        if (this._tag == Tag.PATH) {
            return this.pathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GetMetadataError)) {
            return false;
        }
        GetMetadataError getMetadataError = (GetMetadataError) obj;
        if (this._tag != getMetadataError._tag || C06701.$SwitchMap$com$dropbox$core$v2$files$GetMetadataError$Tag[this._tag.ordinal()] != 1) {
            return false;
        }
        LookupError lookupError = this.pathValue;
        LookupError lookupError2 = getMetadataError.pathValue;
        if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
