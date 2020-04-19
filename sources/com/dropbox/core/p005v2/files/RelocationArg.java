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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.RelocationArg */
class RelocationArg extends RelocationPath {
    protected final boolean allowOwnershipTransfer;
    protected final boolean allowSharedFolder;
    protected final boolean autorename;

    /* renamed from: com.dropbox.core.v2.files.RelocationArg$Builder */
    public static class Builder {
        protected boolean allowOwnershipTransfer;
        protected boolean allowSharedFolder;
        protected boolean autorename;
        protected final String fromPath;
        protected final String toPath;

        protected Builder(String str, String str2) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'fromPath' is null");
            } else if (Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)|(id:.*)", str)) {
                this.fromPath = str;
                if (str2 == null) {
                    throw new IllegalArgumentException("Required value for 'toPath' is null");
                } else if (Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)|(id:.*)", str2)) {
                    this.toPath = str2;
                    this.allowSharedFolder = false;
                    this.autorename = false;
                    this.allowOwnershipTransfer = false;
                } else {
                    throw new IllegalArgumentException("String 'toPath' does not match pattern");
                }
            } else {
                throw new IllegalArgumentException("String 'fromPath' does not match pattern");
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

        public RelocationArg build() {
            RelocationArg relocationArg = new RelocationArg(this.fromPath, this.toPath, this.allowSharedFolder, this.autorename, this.allowOwnershipTransfer);
            return relocationArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.RelocationArg$Serializer */
    static class Serializer extends StructSerializer<RelocationArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(RelocationArg relocationArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("from_path");
            StoneSerializers.string().serialize(relocationArg.fromPath, jsonGenerator);
            jsonGenerator.writeFieldName("to_path");
            StoneSerializers.string().serialize(relocationArg.toPath, jsonGenerator);
            jsonGenerator.writeFieldName("allow_shared_folder");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(relocationArg.allowSharedFolder), jsonGenerator);
            jsonGenerator.writeFieldName("autorename");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(relocationArg.autorename), jsonGenerator);
            jsonGenerator.writeFieldName("allow_ownership_transfer");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(relocationArg.allowOwnershipTransfer), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public RelocationArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
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
                String str2 = null;
                String str3 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("from_path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("to_path".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
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
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"from_path\" missing.");
                } else if (str3 != null) {
                    RelocationArg relocationArg = new RelocationArg(str2, str3, valueOf.booleanValue(), valueOf2.booleanValue(), valueOf3.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(relocationArg, relocationArg.toStringMultiline());
                    return relocationArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"to_path\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public RelocationArg(String str, String str2, boolean z, boolean z2, boolean z3) {
        super(str, str2);
        this.allowSharedFolder = z;
        this.autorename = z2;
        this.allowOwnershipTransfer = z3;
    }

    public RelocationArg(String str, String str2) {
        this(str, str2, false, false, false);
    }

    public String getFromPath() {
        return this.fromPath;
    }

    public String getToPath() {
        return this.toPath;
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

    public static Builder newBuilder(String str, String str2) {
        return new Builder(str, str2);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{Boolean.valueOf(this.allowSharedFolder), Boolean.valueOf(this.autorename), Boolean.valueOf(this.allowOwnershipTransfer)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        RelocationArg relocationArg = (RelocationArg) obj;
        if ((this.fromPath != relocationArg.fromPath && !this.fromPath.equals(relocationArg.fromPath)) || !((this.toPath == relocationArg.toPath || this.toPath.equals(relocationArg.toPath)) && this.allowSharedFolder == relocationArg.allowSharedFolder && this.autorename == relocationArg.autorename && this.allowOwnershipTransfer == relocationArg.allowOwnershipTransfer)) {
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
