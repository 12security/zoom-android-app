package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxList;
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

/* renamed from: com.dropbox.core.v2.files.RelocationBatchArg */
class RelocationBatchArg {
    protected final boolean allowOwnershipTransfer;
    protected final boolean allowSharedFolder;
    protected final boolean autorename;
    protected final List<RelocationPath> entries;

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchArg$Builder */
    public static class Builder {
        protected boolean allowOwnershipTransfer;
        protected boolean allowSharedFolder;
        protected boolean autorename;
        protected final List<RelocationPath> entries;

        protected Builder(List<RelocationPath> list) {
            if (list == null) {
                throw new IllegalArgumentException("Required value for 'entries' is null");
            } else if (list.size() >= 1) {
                for (RelocationPath relocationPath : list) {
                    if (relocationPath == null) {
                        throw new IllegalArgumentException("An item in list 'entries' is null");
                    }
                }
                this.entries = list;
                this.allowSharedFolder = false;
                this.autorename = false;
                this.allowOwnershipTransfer = false;
            } else {
                throw new IllegalArgumentException("List 'entries' has fewer than 1 items");
            }
        }

        public Builder withAllowSharedFolder(Boolean bool) {
            if (bool != null) {
                this.allowSharedFolder = bool.booleanValue();
            } else {
                this.allowSharedFolder = false;
            }
            return this;
        }

        public Builder withAutorename(Boolean bool) {
            if (bool != null) {
                this.autorename = bool.booleanValue();
            } else {
                this.autorename = false;
            }
            return this;
        }

        public Builder withAllowOwnershipTransfer(Boolean bool) {
            if (bool != null) {
                this.allowOwnershipTransfer = bool.booleanValue();
            } else {
                this.allowOwnershipTransfer = false;
            }
            return this;
        }

        public RelocationBatchArg build() {
            return new RelocationBatchArg(this.entries, this.allowSharedFolder, this.autorename, this.allowOwnershipTransfer);
        }
    }

    /* renamed from: com.dropbox.core.v2.files.RelocationBatchArg$Serializer */
    static class Serializer extends StructSerializer<RelocationBatchArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelocationBatchArg relocationBatchArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName(BoxList.FIELD_ENTRIES);
            StoneSerializers.list(Serializer.INSTANCE).serialize(relocationBatchArg.entries, jsonGenerator);
            jsonGenerator.writeFieldName("allow_shared_folder");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(relocationBatchArg.allowSharedFolder), jsonGenerator);
            jsonGenerator.writeFieldName("autorename");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(relocationBatchArg.autorename), jsonGenerator);
            jsonGenerator.writeFieldName("allow_ownership_transfer");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(relocationBatchArg.allowOwnershipTransfer), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RelocationBatchArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                Boolean valueOf3 = Boolean.valueOf(false);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if (BoxList.FIELD_ENTRIES.equals(currentName)) {
                        list = (List) StoneSerializers.list(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("allow_shared_folder".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("autorename".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("allow_ownership_transfer".equals(currentName)) {
                        valueOf3 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (list != null) {
                    RelocationBatchArg relocationBatchArg = new RelocationBatchArg(list, valueOf.booleanValue(), valueOf2.booleanValue(), valueOf3.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(relocationBatchArg, relocationBatchArg.toStringMultiline());
                    return relocationBatchArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"entries\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public RelocationBatchArg(List<RelocationPath> list, boolean z, boolean z2, boolean z3) {
        if (list == null) {
            throw new IllegalArgumentException("Required value for 'entries' is null");
        } else if (list.size() >= 1) {
            for (RelocationPath relocationPath : list) {
                if (relocationPath == null) {
                    throw new IllegalArgumentException("An item in list 'entries' is null");
                }
            }
            this.entries = list;
            this.allowSharedFolder = z;
            this.autorename = z2;
            this.allowOwnershipTransfer = z3;
        } else {
            throw new IllegalArgumentException("List 'entries' has fewer than 1 items");
        }
    }

    public RelocationBatchArg(List<RelocationPath> list) {
        this(list, false, false, false);
    }

    public List<RelocationPath> getEntries() {
        return this.entries;
    }

    public boolean getAllowSharedFolder() {
        return this.allowSharedFolder;
    }

    public boolean getAutorename() {
        return this.autorename;
    }

    public boolean getAllowOwnershipTransfer() {
        return this.allowOwnershipTransfer;
    }

    public static Builder newBuilder(List<RelocationPath> list) {
        return new Builder(list);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.entries, Boolean.valueOf(this.allowSharedFolder), Boolean.valueOf(this.autorename), Boolean.valueOf(this.allowOwnershipTransfer)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RelocationBatchArg relocationBatchArg = (RelocationBatchArg) obj;
        List<RelocationPath> list = this.entries;
        List<RelocationPath> list2 = relocationBatchArg.entries;
        if (!((list == list2 || list.equals(list2)) && this.allowSharedFolder == relocationBatchArg.allowSharedFolder && this.autorename == relocationBatchArg.autorename && this.allowOwnershipTransfer == relocationBatchArg.allowOwnershipTransfer)) {
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
