package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.DownloadError */
public final class DownloadError {
    public static final DownloadError OTHER = new DownloadError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.files.DownloadError$Serializer */
    static class Serializer extends UnionSerializer<DownloadError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(DownloadError downloadError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06671.$SwitchMap$com$dropbox$core$v2$files$DownloadError$Tag[downloadError.tag().ordinal()] != 1) {
                jsonGenerator.writeString("other");
                return;
            }
            jsonGenerator.writeStartObject();
            writeTag("path", jsonGenerator);
            jsonGenerator.writeFieldName("path");
            com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(downloadError.pathValue, jsonGenerator);
            jsonGenerator.writeEndObject();
        }

        public DownloadError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            DownloadError downloadError;
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
                    downloadError = DownloadError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else {
                    downloadError = DownloadError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return downloadError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.DownloadError$Tag */
    public enum Tag {
        PATH,
        OTHER
    }

    private DownloadError() {
    }

    private DownloadError withTag(Tag tag) {
        DownloadError downloadError = new DownloadError();
        downloadError._tag = tag;
        return downloadError;
    }

    private DownloadError withTagAndPath(Tag tag, LookupError lookupError) {
        DownloadError downloadError = new DownloadError();
        downloadError._tag = tag;
        downloadError.pathValue = lookupError;
        return downloadError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static DownloadError path(LookupError lookupError) {
        if (lookupError != null) {
            return new DownloadError().withTagAndPath(Tag.PATH, lookupError);
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
        if (obj == null || !(obj instanceof DownloadError)) {
            return false;
        }
        DownloadError downloadError = (DownloadError) obj;
        if (this._tag != downloadError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = downloadError.pathValue;
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
