package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.CreateFolderBatchArg */
class CreateFolderBatchArg {
    protected final boolean autorename;
    protected final boolean forceAsync;
    protected final List<String> paths;

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchArg$Builder */
    public static class Builder {
        protected boolean autorename;
        protected boolean forceAsync;
        protected final List<String> paths;

        protected Builder(List<String> list) {
            if (list != null) {
                for (String str : list) {
                    if (str == null) {
                        throw new IllegalArgumentException("An item in list 'paths' is null");
                    } else if (!Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)", str)) {
                        throw new IllegalArgumentException("Stringan item in list 'paths' does not match pattern");
                    }
                }
                this.paths = list;
                this.autorename = false;
                this.forceAsync = false;
                return;
            }
            throw new IllegalArgumentException("Required value for 'paths' is null");
        }

        public Builder withAutorename(Boolean bool) {
            if (bool != null) {
                this.autorename = bool.booleanValue();
            } else {
                this.autorename = false;
            }
            return this;
        }

        public Builder withForceAsync(Boolean bool) {
            if (bool != null) {
                this.forceAsync = bool.booleanValue();
            } else {
                this.forceAsync = false;
            }
            return this;
        }

        public CreateFolderBatchArg build() {
            return new CreateFolderBatchArg(this.paths, this.autorename, this.forceAsync);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CreateFolderBatchArg$Serializer */
    static class Serializer extends StructSerializer<CreateFolderBatchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CreateFolderBatchArg createFolderBatchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("paths");
            StoneSerializers.list(StoneSerializers.string()).serialize(createFolderBatchArg.paths, jsonGenerator);
            jsonGenerator.writeFieldName("autorename");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(createFolderBatchArg.autorename), jsonGenerator);
            jsonGenerator.writeFieldName("force_async");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(createFolderBatchArg.forceAsync), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CreateFolderBatchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            List list = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                Boolean valueOf2 = Boolean.valueOf(false);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("paths".equals(currentName)) {
                        list = (List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("autorename".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("force_async".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    CreateFolderBatchArg createFolderBatchArg = new CreateFolderBatchArg(list, valueOf.booleanValue(), valueOf2.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(createFolderBatchArg, createFolderBatchArg.toStringMultiline());
                    return createFolderBatchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"paths\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public CreateFolderBatchArg(List<String> list, boolean z, boolean z2) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list 'paths' is null");
                } else if (!Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)", str)) {
                    throw new IllegalArgumentException("Stringan item in list 'paths' does not match pattern");
                }
            }
            this.paths = list;
            this.autorename = z;
            this.forceAsync = z2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'paths' is null");
    }

    public CreateFolderBatchArg(List<String> list) {
        this(list, false, false);
    }

    public List<String> getPaths() {
        return this.paths;
    }

    public boolean getAutorename() {
        return this.autorename;
    }

    public boolean getForceAsync() {
        return this.forceAsync;
    }

    public static Builder newBuilder(List<String> list) {
        return new Builder(list);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.paths, Boolean.valueOf(this.autorename), Boolean.valueOf(this.forceAsync)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        CreateFolderBatchArg createFolderBatchArg = (CreateFolderBatchArg) obj;
        List<String> list = this.paths;
        List<String> list2 = createFolderBatchArg.paths;
        if (!((list == list2 || list.equals(list2)) && this.autorename == createFolderBatchArg.autorename && this.forceAsync == createFolderBatchArg.forceAsync)) {
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
