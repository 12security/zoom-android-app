package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.SyncSettingsError */
public final class SyncSettingsError {
    public static final SyncSettingsError OTHER = new SyncSettingsError().withTag(Tag.OTHER);
    public static final SyncSettingsError UNSUPPORTED_COMBINATION = new SyncSettingsError().withTag(Tag.UNSUPPORTED_COMBINATION);
    public static final SyncSettingsError UNSUPPORTED_CONFIGURATION = new SyncSettingsError().withTag(Tag.UNSUPPORTED_CONFIGURATION);
    private Tag _tag;
    /* access modifiers changed from: private */
    public LookupError pathValue;

    /* renamed from: com.dropbox.core.v2.files.SyncSettingsError$Serializer */
    public static class Serializer extends UnionSerializer<SyncSettingsError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(SyncSettingsError syncSettingsError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (syncSettingsError.tag()) {
                case PATH:
                    jsonGenerator.writeStartObject();
                    writeTag("path", jsonGenerator);
                    jsonGenerator.writeFieldName("path");
                    com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.serialize(syncSettingsError.pathValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case UNSUPPORTED_COMBINATION:
                    jsonGenerator.writeString("unsupported_combination");
                    return;
                case UNSUPPORTED_CONFIGURATION:
                    jsonGenerator.writeString("unsupported_configuration");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public SyncSettingsError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            SyncSettingsError syncSettingsError;
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
                    syncSettingsError = SyncSettingsError.path(com.dropbox.core.p005v2.files.LookupError.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("unsupported_combination".equals(str)) {
                    syncSettingsError = SyncSettingsError.UNSUPPORTED_COMBINATION;
                } else if ("unsupported_configuration".equals(str)) {
                    syncSettingsError = SyncSettingsError.UNSUPPORTED_CONFIGURATION;
                } else {
                    syncSettingsError = SyncSettingsError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return syncSettingsError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.SyncSettingsError$Tag */
    public enum Tag {
        PATH,
        UNSUPPORTED_COMBINATION,
        UNSUPPORTED_CONFIGURATION,
        OTHER
    }

    private SyncSettingsError() {
    }

    private SyncSettingsError withTag(Tag tag) {
        SyncSettingsError syncSettingsError = new SyncSettingsError();
        syncSettingsError._tag = tag;
        return syncSettingsError;
    }

    private SyncSettingsError withTagAndPath(Tag tag, LookupError lookupError) {
        SyncSettingsError syncSettingsError = new SyncSettingsError();
        syncSettingsError._tag = tag;
        syncSettingsError.pathValue = lookupError;
        return syncSettingsError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static SyncSettingsError path(LookupError lookupError) {
        if (lookupError != null) {
            return new SyncSettingsError().withTagAndPath(Tag.PATH, lookupError);
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

    public boolean isUnsupportedCombination() {
        return this._tag == Tag.UNSUPPORTED_COMBINATION;
    }

    public boolean isUnsupportedConfiguration() {
        return this._tag == Tag.UNSUPPORTED_CONFIGURATION;
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
        if (obj == null || !(obj instanceof SyncSettingsError)) {
            return false;
        }
        SyncSettingsError syncSettingsError = (SyncSettingsError) obj;
        if (this._tag != syncSettingsError._tag) {
            return false;
        }
        switch (this._tag) {
            case PATH:
                LookupError lookupError = this.pathValue;
                LookupError lookupError2 = syncSettingsError.pathValue;
                if (lookupError != lookupError2 && !lookupError.equals(lookupError2)) {
                    z = false;
                }
                return z;
            case UNSUPPORTED_COMBINATION:
                return true;
            case UNSUPPORTED_CONFIGURATION:
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
