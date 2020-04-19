package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.p005v2.files.LookupError;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkError */
public final class CreateSharedLinkError {
    public static final CreateSharedLinkError OTHER = new CreateSharedLinkError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkError$Serializer */
    static class Serializer extends UnionSerializer<CreateSharedLinkError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateSharedLinkError createSharedLinkError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C07361.$SwitchMap$com$dropbox$core$v2$sharing$CreateSharedLinkError$Tag[createSharedLinkError.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("path", jsonGenerator);
            jsonGenerator.writeFieldName("path");
            com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(createSharedLinkError.pathValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public CreateSharedLinkError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            CreateSharedLinkError createSharedLinkError;
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
                    expectField("path", jsonParser);
                    createSharedLinkError = CreateSharedLinkError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    createSharedLinkError = CreateSharedLinkError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return createSharedLinkError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.CreateSharedLinkError$Tag */
    public enum Tag {
        PATH,
        OTHER
    }

    private CreateSharedLinkError() {
    }

    private CreateSharedLinkError withTag(Tag tag) {
        CreateSharedLinkError createSharedLinkError = new CreateSharedLinkError();
        createSharedLinkError._tag = tag;
        return createSharedLinkError;
    }

    private CreateSharedLinkError withTagAndPath(Tag tag, LookupError lookupError) {
        CreateSharedLinkError createSharedLinkError = new CreateSharedLinkError();
        createSharedLinkError._tag = tag;
        createSharedLinkError.pathValue = lookupError;
        return createSharedLinkError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static CreateSharedLinkError path(LookupError lookupError) {
        if (lookupError != null) {
            return new CreateSharedLinkError().withTagAndPath(Tag.PATH, lookupError);
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
        if (obj == null || !(obj instanceof CreateSharedLinkError)) {
            return false;
        }
        CreateSharedLinkError createSharedLinkError = (CreateSharedLinkError) obj;
        if (this._tag != createSharedLinkError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = createSharedLinkError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
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
