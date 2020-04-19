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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.WriteMode */
public final class WriteMode {
    public static final WriteMode ADD = new WriteMode().withTag(Tag.ADD);
    public static final WriteMode OVERWRITE = new WriteMode().withTag(Tag.OVERWRITE);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String updateValue;

    /* renamed from: com.dropbox.core.v2.files.WriteMode$Serializer */
    static class Serializer extends UnionSerializer<WriteMode> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(WriteMode writeMode, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (writeMode.tag()) {
                case ADD:
                    jsonGenerator.writeString("add");
                    return;
                case OVERWRITE:
                    jsonGenerator.writeString(QueryParameters.OVERWRITE);
                    return;
                case UPDATE:
                    jsonGenerator.writeStartObject();
                    writeTag("update", jsonGenerator);
                    jsonGenerator.writeFieldName("update");
                    StoneSerializers.string().serialize(writeMode.updateValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(writeMode.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public WriteMode deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            WriteMode writeMode;
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
                if ("add".equals(str)) {
                    writeMode = WriteMode.ADD;
                } else if (QueryParameters.OVERWRITE.equals(str)) {
                    writeMode = WriteMode.OVERWRITE;
                } else if ("update".equals(str)) {
                    expectField("update", jsonParser);
                    writeMode = WriteMode.update((String) StoneSerializers.string().deserialize(jsonParser));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return writeMode;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.files.WriteMode$Tag */
    public enum Tag {
        ADD,
        OVERWRITE,
        UPDATE
    }

    private WriteMode() {
    }

    private WriteMode withTag(Tag tag) {
        WriteMode writeMode = new WriteMode();
        writeMode._tag = tag;
        return writeMode;
    }

    private WriteMode withTagAndUpdate(Tag tag, String str) {
        WriteMode writeMode = new WriteMode();
        writeMode._tag = tag;
        writeMode.updateValue = str;
        return writeMode;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isAdd() {
        return this._tag == Tag.ADD;
    }

    public boolean isOverwrite() {
        return this._tag == Tag.OVERWRITE;
    }

    public boolean isUpdate() {
        return this._tag == Tag.UPDATE;
    }

    public static WriteMode update(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (str.length() < 9) {
            throw new IllegalArgumentException("String is shorter than 9");
        } else if (Pattern.matches("[0-9a-f]+", str)) {
            return new WriteMode().withTagAndUpdate(Tag.UPDATE, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getUpdateValue() {
        if (this._tag == Tag.UPDATE) {
            return this.updateValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.UPDATE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.updateValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof WriteMode)) {
            return false;
        }
        WriteMode writeMode = (WriteMode) obj;
        if (this._tag != writeMode._tag) {
            return false;
        }
        switch (this._tag) {
            case ADD:
                return true;
            case OVERWRITE:
                return true;
            case UPDATE:
                String str = this.updateValue;
                String str2 = writeMode.updateValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
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
