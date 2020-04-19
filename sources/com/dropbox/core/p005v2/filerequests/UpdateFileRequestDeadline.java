package com.dropbox.core.p005v2.filerequests;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.filerequests.UpdateFileRequestDeadline */
public final class UpdateFileRequestDeadline {
    public static final UpdateFileRequestDeadline NO_UPDATE = new UpdateFileRequestDeadline().withTag(Tag.NO_UPDATE);
    public static final UpdateFileRequestDeadline OTHER = new UpdateFileRequestDeadline().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public FileRequestDeadline updateValue;

    /* renamed from: com.dropbox.core.v2.filerequests.UpdateFileRequestDeadline$Serializer */
    static class Serializer extends UnionSerializer<UpdateFileRequestDeadline> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UpdateFileRequestDeadline updateFileRequestDeadline, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (updateFileRequestDeadline.tag()) {
                case NO_UPDATE:
                    jsonGenerator.writeString("no_update");
                    return;
                case UPDATE:
                    jsonGenerator.writeStartObject();
                    writeTag("update", jsonGenerator);
                    StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(updateFileRequestDeadline.updateValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public UpdateFileRequestDeadline deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            UpdateFileRequestDeadline updateFileRequestDeadline;
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
                if ("no_update".equals(str)) {
                    updateFileRequestDeadline = UpdateFileRequestDeadline.NO_UPDATE;
                } else if ("update".equals(str)) {
                    FileRequestDeadline fileRequestDeadline = null;
                    if (jsonParser.getCurrentToken() != JsonToken.END_OBJECT) {
                        fileRequestDeadline = (FileRequestDeadline) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser, true);
                    }
                    if (fileRequestDeadline == null) {
                        updateFileRequestDeadline = UpdateFileRequestDeadline.update();
                    } else {
                        updateFileRequestDeadline = UpdateFileRequestDeadline.update(fileRequestDeadline);
                    }
                } else {
                    updateFileRequestDeadline = UpdateFileRequestDeadline.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return updateFileRequestDeadline;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.filerequests.UpdateFileRequestDeadline$Tag */
    public enum Tag {
        NO_UPDATE,
        UPDATE,
        OTHER
    }

    private UpdateFileRequestDeadline() {
    }

    private UpdateFileRequestDeadline withTag(Tag tag) {
        UpdateFileRequestDeadline updateFileRequestDeadline = new UpdateFileRequestDeadline();
        updateFileRequestDeadline._tag = tag;
        return updateFileRequestDeadline;
    }

    private UpdateFileRequestDeadline withTagAndUpdate(Tag tag, FileRequestDeadline fileRequestDeadline) {
        UpdateFileRequestDeadline updateFileRequestDeadline = new UpdateFileRequestDeadline();
        updateFileRequestDeadline._tag = tag;
        updateFileRequestDeadline.updateValue = fileRequestDeadline;
        return updateFileRequestDeadline;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isNoUpdate() {
        return this._tag == Tag.NO_UPDATE;
    }

    public boolean isUpdate() {
        return this._tag == Tag.UPDATE;
    }

    public static UpdateFileRequestDeadline update(FileRequestDeadline fileRequestDeadline) {
        return new UpdateFileRequestDeadline().withTagAndUpdate(Tag.UPDATE, fileRequestDeadline);
    }

    public static UpdateFileRequestDeadline update() {
        return update(null);
    }

    public FileRequestDeadline getUpdateValue() {
        if (this._tag == Tag.UPDATE) {
            return this.updateValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.UPDATE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.updateValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof UpdateFileRequestDeadline)) {
            return false;
        }
        UpdateFileRequestDeadline updateFileRequestDeadline = (UpdateFileRequestDeadline) obj;
        if (this._tag != updateFileRequestDeadline._tag) {
            return false;
        }
        switch (this._tag) {
            case NO_UPDATE:
                return true;
            case UPDATE:
                FileRequestDeadline fileRequestDeadline = this.updateValue;
                FileRequestDeadline fileRequestDeadline2 = updateFileRequestDeadline.updateValue;
                if (fileRequestDeadline != fileRequestDeadline2 && (fileRequestDeadline == null || !fileRequestDeadline.equals(fileRequestDeadline2))) {
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
