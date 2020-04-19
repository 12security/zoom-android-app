package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.CreateFolderError */
public final class CreateFolderError {
    private Tag _tag;
    /* access modifiers changed from: private */
    public WriteError pathValue;

    /* renamed from: com.dropbox.core.v2.files.CreateFolderError$1 */
    static /* synthetic */ class C06611 {
        static final /* synthetic */ int[] $SwitchMap$com$dropbox$core$v2$files$CreateFolderError$Tag = new int[Tag.values().length];

        static {
            try {
                $SwitchMap$com$dropbox$core$v2$files$CreateFolderError$Tag[Tag.PATH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CreateFolderError$Serializer */
    static class Serializer extends UnionSerializer<CreateFolderError> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateFolderError createFolderError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            if (C06611.$SwitchMap$com$dropbox$core$v2$files$CreateFolderError$Tag[createFolderError.tag().ordinal()] == 1) {
                jsonGenerator.writeStartObject();
                writeTag("path", jsonGenerator);
                jsonGenerator.writeFieldName("path");
                Serializer.INSTANCE.serialize(createFolderError.pathValue, jsonGenerator);
                jsonGenerator.writeEndObject();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unrecognized tag: ");
            sb.append(createFolderError.tag());
            throw new IllegalArgumentException(sb.toString());
        }

        public CreateFolderError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
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
                CreateFolderError path = CreateFolderError.path(Serializer.INSTANCE.deserialize(jsonParser));
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

    /* renamed from: com.dropbox.core.v2.files.CreateFolderError$Tag */
    public enum Tag {
        PATH
    }

    private CreateFolderError() {
    }

    private CreateFolderError withTag(Tag tag) {
        CreateFolderError createFolderError = new CreateFolderError();
        createFolderError._tag = tag;
        return createFolderError;
    }

    private CreateFolderError withTagAndPath(Tag tag, WriteError writeError) {
        CreateFolderError createFolderError = new CreateFolderError();
        createFolderError._tag = tag;
        createFolderError.pathValue = writeError;
        return createFolderError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isPath() {
        return this._tag == Tag.PATH;
    }

    public static CreateFolderError path(WriteError writeError) {
        if (writeError != null) {
            return new CreateFolderError().withTagAndPath(Tag.PATH, writeError);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public WriteError getPathValue() {
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
        if (obj == null || !(obj instanceof CreateFolderError)) {
            return false;
        }
        CreateFolderError createFolderError = (CreateFolderError) obj;
        if (this._tag != createFolderError._tag || C06611.$SwitchMap$com$dropbox$core$v2$files$CreateFolderError$Tag[this._tag.ordinal()] != 1) {
            return false;
        }
        WriteError writeError = this.pathValue;
        WriteError writeError2 = createFolderError.pathValue;
        if (writeError != writeError2 && !writeError.equals(writeError2)) {
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
