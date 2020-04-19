package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.LookupError */
public final class LookupError {
    public static final LookupError NOT_FILE = new LookupError().withTag(Tag.NOT_FILE);
    public static final LookupError NOT_FOLDER = new LookupError().withTag(Tag.NOT_FOLDER);
    public static final LookupError NOT_FOUND = new LookupError().withTag(Tag.NOT_FOUND);
    public static final LookupError OTHER = new LookupError().withTag(Tag.OTHER);
    public static final LookupError RESTRICTED_CONTENT = new LookupError().withTag(Tag.RESTRICTED_CONTENT);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String malformedPathValue;

    /* renamed from: com.dropbox.core.v2.files.LookupError$Serializer */
    public static class Serializer extends UnionSerializer<LookupError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(LookupError lookupError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (lookupError.tag()) {
                case MALFORMED_PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("malformed_path", jsonGenerator);
                    jsonGenerator.writeFieldName("malformed_path");
                    StoneSerializers.nullable(StoneSerializers.string()).serialize(lookupError.malformedPathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NOT_FOUND:
                    jsonGenerator.writeString("not_found");
                    return;
                case NOT_FILE:
                    jsonGenerator.writeString("not_file");
                    return;
                case NOT_FOLDER:
                    jsonGenerator.writeString("not_folder");
                    return;
                case RESTRICTED_CONTENT:
                    jsonGenerator.writeString("restricted_content");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public LookupError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            LookupError lookupError;
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
                if ("malformed_path".equals(str)) {
                    String str2 = null;
                    if (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
                        expectField("malformed_path", jsonParser);
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    }
                    if (str2 == null) {
                        lookupError = LookupError.malformedPath();
                    } else {
                        lookupError = LookupError.malformedPath(str2);
                    }
                } else if ("not_found".equals(str)) {
                    lookupError = LookupError.NOT_FOUND;
                } else if ("not_file".equals(str)) {
                    lookupError = LookupError.NOT_FILE;
                } else if ("not_folder".equals(str)) {
                    lookupError = LookupError.NOT_FOLDER;
                } else if ("restricted_content".equals(str)) {
                    lookupError = LookupError.RESTRICTED_CONTENT;
                } else {
                    lookupError = LookupError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return lookupError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.LookupError$Tag */
    public enum Tag {
        MALFORMED_PATH,
        NOT_FOUND,
        NOT_FILE,
        NOT_FOLDER,
        RESTRICTED_CONTENT,
        OTHER
    }

    private LookupError() {
    }

    private LookupError withTag(Tag tag) {
        LookupError lookupError = new LookupError();
        lookupError._tag = tag;
        return lookupError;
    }

    private LookupError withTagAndMalformedPath(Tag tag, String str) {
        LookupError lookupError = new LookupError();
        lookupError._tag = tag;
        lookupError.malformedPathValue = str;
        return lookupError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isMalformedPath() {
        return this._tag == Tag.MALFORMED_PATH;
    }

    public static LookupError malformedPath(String str) {
        return new LookupError().withTagAndMalformedPath(Tag.MALFORMED_PATH, str);
    }

    public static LookupError malformedPath() {
        return malformedPath(null);
    }

    public String getMalformedPathValue() {
        if (this._tag == Tag.MALFORMED_PATH) {
            return this.malformedPathValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.MALFORMED_PATH, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isNotFound() {
        return this._tag == Tag.NOT_FOUND;
    }

    public boolean isNotFile() {
        return this._tag == Tag.NOT_FILE;
    }

    public boolean isNotFolder() {
        return this._tag == Tag.NOT_FOLDER;
    }

    public boolean isRestrictedContent() {
        return this._tag == Tag.RESTRICTED_CONTENT;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.malformedPathValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof LookupError)) {
            return false;
        }
        LookupError lookupError = (LookupError) obj;
        if (this._tag != lookupError._tag) {
            return false;
        }
        switch (this._tag) {
            case MALFORMED_PATH:
                String str = this.malformedPathValue;
                String str2 = lookupError.malformedPathValue;
                if (str != str2 && (str == null || !str.equals(str2))) {
                    z = false;
                }
                return z;
            case NOT_FOUND:
                return true;
            case NOT_FILE:
                return true;
            case NOT_FOLDER:
                return true;
            case RESTRICTED_CONTENT:
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
