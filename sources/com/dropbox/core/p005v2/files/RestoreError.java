package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.RestoreError */
public final class RestoreError {
    public static final RestoreError INVALID_REVISION = new RestoreError().withTag(Tag.INVALID_REVISION);
    public static final RestoreError OTHER = new RestoreError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathLookupValue;
    /* access modifiers changed from: private */
    public WriteError pathWriteValue;

    /* renamed from: com.dropbox.core.v2.files.RestoreError$Serializer */
    static class Serializer extends UnionSerializer<RestoreError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RestoreError restoreError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (restoreError.tag()) {
                case PATH_LOOKUP:
                    jsonGenerator.writeStartObject();
                    writeTag("path_lookup", jsonGenerator);
                    jsonGenerator.writeFieldName("path_lookup");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(restoreError.pathLookupValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case PATH_WRITE:
                    jsonGenerator.writeStartObject();
                    writeTag("path_write", jsonGenerator);
                    jsonGenerator.writeFieldName("path_write");
                    Serializer.INSTANCE.serialize(restoreError.pathWriteValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case INVALID_REVISION:
                    jsonGenerator.writeString("invalid_revision");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public RestoreError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            RestoreError restoreError;
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
                if ("path_lookup".equals(str)) {
                    expectField("path_lookup", jsonParser);
                    restoreError = RestoreError.pathLookup(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("path_write".equals(str)) {
                    expectField("path_write", jsonParser);
                    restoreError = RestoreError.pathWrite(Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("invalid_revision".equals(str)) {
                    restoreError = RestoreError.INVALID_REVISION;
                } else {
                    restoreError = RestoreError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return restoreError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.RestoreError$Tag */
    public enum Tag {
        PATH_LOOKUP,
        PATH_WRITE,
        INVALID_REVISION,
        OTHER
    }

    private RestoreError() {
    }

    private RestoreError withTag(Tag tag) {
        RestoreError restoreError = new RestoreError();
        restoreError._tag = tag;
        return restoreError;
    }

    private RestoreError withTagAndPathLookup(Tag tag, LookupError lookupError) {
        RestoreError restoreError = new RestoreError();
        restoreError._tag = tag;
        restoreError.pathLookupValue = lookupError;
        return restoreError;
    }

    private RestoreError withTagAndPathWrite(Tag tag, WriteError writeError) {
        RestoreError restoreError = new RestoreError();
        restoreError._tag = tag;
        restoreError.pathWriteValue = writeError;
        return restoreError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPathLookup() {
        return this._tag == Tag.PATH_LOOKUP;
    }

    public static RestoreError pathLookup(LookupError lookupError) {
        if (lookupError != null) {
            return new RestoreError().withTagAndPathLookup(Tag.PATH_LOOKUP, lookupError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public LookupError getPathLookupValue() {
        if (this._tag == Tag.PATH_LOOKUP) {
            return this.pathLookupValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH_LOOKUP, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isPathWrite() {
        return this._tag == Tag.PATH_WRITE;
    }

    public static RestoreError pathWrite(WriteError writeError) {
        if (writeError != null) {
            return new RestoreError().withTagAndPathWrite(Tag.PATH_WRITE, writeError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WriteError getPathWriteValue() {
        if (this._tag == Tag.PATH_WRITE) {
            return this.pathWriteValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PATH_WRITE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isInvalidRevision() {
        return this._tag == Tag.INVALID_REVISION;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.pathLookupValue, this.pathWriteValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof RestoreError)) {
            return false;
        }
        RestoreError restoreError = (RestoreError) obj;
        if (this._tag != restoreError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH_LOOKUP:
                LookupError lookupError = this.pathLookupValue;
                LookupError lookupError2 = restoreError.pathLookupValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case PATH_WRITE:
                WriteError writeError = this.pathWriteValue;
                WriteError writeError2 = restoreError.pathWriteValue;
                if (writeError != writeError2 && !writeError.equals(writeError2)) {
                    z = false;
                }
                return z;
            case INVALID_REVISION:
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
