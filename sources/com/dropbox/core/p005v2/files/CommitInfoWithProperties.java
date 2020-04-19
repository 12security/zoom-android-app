package com.dropbox.core.p005v2.files;

import com.dropbox.core.p005v2.fileproperties.PropertyGroup;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v2.files.CommitInfoWithProperties */
class CommitInfoWithProperties extends CommitInfo {

    /* renamed from: com.dropbox.core.v2.files.CommitInfoWithProperties$Builder */
    public static class Builder extends com.dropbox.core.p005v2.files.CommitInfo.Builder {
        protected Builder(String str) {
            super(str);
        }

        public Builder withMode(WriteMode writeMode) {
            super.withMode(writeMode);
            return this;
        }

        public Builder withAutorename(Boolean bool) {
            super.withAutorename(bool);
            return this;
        }

        public Builder withClientModified(Date date) {
            super.withClientModified(date);
            return this;
        }

        public Builder withMute(Boolean bool) {
            super.withMute(bool);
            return this;
        }

        public Builder withPropertyGroups(List<PropertyGroup> list) {
            super.withPropertyGroups(list);
            return this;
        }

        public Builder withStrictConflict(Boolean bool) {
            super.withStrictConflict(bool);
            return this;
        }

        public CommitInfoWithProperties build() {
            CommitInfoWithProperties commitInfoWithProperties = new CommitInfoWithProperties(this.path, this.mode, this.autorename, this.clientModified, this.mute, this.propertyGroups, this.strictConflict);
            return commitInfoWithProperties;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CommitInfoWithProperties$Serializer */
    static class Serializer extends StructSerializer<CommitInfoWithProperties> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CommitInfoWithProperties commitInfoWithProperties, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(commitInfoWithProperties.path, jsonGenerator);
            jsonGenerator.writeFieldName("mode");
            Serializer.INSTANCE.serialize(commitInfoWithProperties.mode, jsonGenerator);
            jsonGenerator.writeFieldName("autorename");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(commitInfoWithProperties.autorename), jsonGenerator);
            if (commitInfoWithProperties.clientModified != null) {
                jsonGenerator.writeFieldName("client_modified");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(commitInfoWithProperties.clientModified, jsonGenerator);
            }
            jsonGenerator.writeFieldName("mute");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(commitInfoWithProperties.mute), jsonGenerator);
            if (commitInfoWithProperties.propertyGroups != null) {
                jsonGenerator.writeFieldName("property_groups");
                StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE)).serialize(commitInfoWithProperties.propertyGroups, jsonGenerator);
            }
            jsonGenerator.writeFieldName("strict_conflict");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(commitInfoWithProperties.strictConflict), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CommitInfoWithProperties deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                WriteMode writeMode = WriteMode.ADD;
                Boolean valueOf = Boolean.valueOf(false);
                Boolean valueOf2 = Boolean.valueOf(false);
                Boolean valueOf3 = Boolean.valueOf(false);
                String str2 = null;
                Date date = null;
                List list = null;
                WriteMode writeMode2 = writeMode;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("mode".equals(currentName)) {
                        writeMode2 = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("autorename".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("client_modified".equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else if ("mute".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("property_groups".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("strict_conflict".equals(currentName)) {
                        valueOf3 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    CommitInfoWithProperties commitInfoWithProperties = new CommitInfoWithProperties(str2, writeMode2, valueOf.booleanValue(), date, valueOf2.booleanValue(), list, valueOf3.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(commitInfoWithProperties, commitInfoWithProperties.toStringMultiline());
                    return commitInfoWithProperties;
                }
                throw new JsonParseException(jsonParser, "Required field \"path\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public CommitInfoWithProperties(String str, WriteMode writeMode, boolean z, Date date, boolean z2, List<PropertyGroup> list, boolean z3) {
        super(str, writeMode, z, date, z2, list, z3);
    }

    public CommitInfoWithProperties(String str) {
        this(str, WriteMode.ADD, false, null, false, null, false);
    }

    public String getPath() {
        return this.path;
    }

    public WriteMode getMode() {
        return this.mode;
    }

    public boolean getAutorename() {
        return this.autorename;
    }

    public Date getClientModified() {
        return this.clientModified;
    }

    public boolean getMute() {
        return this.mute;
    }

    public List<PropertyGroup> getPropertyGroups() {
        return this.propertyGroups;
    }

    public boolean getStrictConflict() {
        return this.strictConflict;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return getClass().toString().hashCode();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        CommitInfoWithProperties commitInfoWithProperties = (CommitInfoWithProperties) obj;
        if ((this.path != commitInfoWithProperties.path && !this.path.equals(commitInfoWithProperties.path)) || ((this.mode != commitInfoWithProperties.mode && !this.mode.equals(commitInfoWithProperties.mode)) || this.autorename != commitInfoWithProperties.autorename || ((this.clientModified != commitInfoWithProperties.clientModified && (this.clientModified == null || !this.clientModified.equals(commitInfoWithProperties.clientModified))) || this.mute != commitInfoWithProperties.mute || ((this.propertyGroups != commitInfoWithProperties.propertyGroups && (this.propertyGroups == null || !this.propertyGroups.equals(commitInfoWithProperties.propertyGroups))) || this.strictConflict != commitInfoWithProperties.strictConflict)))) {
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
