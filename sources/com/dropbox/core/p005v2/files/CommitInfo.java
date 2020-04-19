package com.dropbox.core.p005v2.files;

import com.dropbox.core.p005v2.fileproperties.PropertyGroup;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.CommitInfo */
public class CommitInfo {
    protected final boolean autorename;
    protected final Date clientModified;
    protected final WriteMode mode;
    protected final boolean mute;
    protected final String path;
    protected final List<PropertyGroup> propertyGroups;
    protected final boolean strictConflict;

    /* renamed from: com.dropbox.core.v2.files.CommitInfo$Builder */
    public static class Builder {
        protected boolean autorename;
        protected Date clientModified;
        protected WriteMode mode;
        protected boolean mute;
        protected final String path;
        protected List<PropertyGroup> propertyGroups;
        protected boolean strictConflict;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'path' is null");
            } else if (Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)|(id:.*)", str)) {
                this.path = str;
                this.mode = WriteMode.ADD;
                this.autorename = false;
                this.clientModified = null;
                this.mute = false;
                this.propertyGroups = null;
                this.strictConflict = false;
            } else {
                throw new IllegalArgumentException("String 'path' does not match pattern");
            }
        }

        public Builder withMode(WriteMode writeMode) {
            if (writeMode != null) {
                this.mode = writeMode;
            } else {
                this.mode = WriteMode.ADD;
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

        public Builder withClientModified(Date date) {
            this.clientModified = LangUtil.truncateMillis(date);
            return this;
        }

        public Builder withMute(Boolean bool) {
            if (bool != null) {
                this.mute = bool.booleanValue();
            } else {
                this.mute = false;
            }
            return this;
        }

        public Builder withPropertyGroups(List<PropertyGroup> list) {
            if (list != null) {
                for (PropertyGroup propertyGroup : list) {
                    if (propertyGroup == null) {
                        throw new IllegalArgumentException("An item in list 'propertyGroups' is null");
                    }
                }
            }
            this.propertyGroups = list;
            return this;
        }

        public Builder withStrictConflict(Boolean bool) {
            if (bool != null) {
                this.strictConflict = bool.booleanValue();
            } else {
                this.strictConflict = false;
            }
            return this;
        }

        public CommitInfo build() {
            CommitInfo commitInfo = new CommitInfo(this.path, this.mode, this.autorename, this.clientModified, this.mute, this.propertyGroups, this.strictConflict);
            return commitInfo;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.CommitInfo$Serializer */
    static class Serializer extends StructSerializer<CommitInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(CommitInfo commitInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(commitInfo.path, jsonGenerator);
            jsonGenerator.writeFieldName("mode");
            Serializer.INSTANCE.serialize(commitInfo.mode, jsonGenerator);
            jsonGenerator.writeFieldName("autorename");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(commitInfo.autorename), jsonGenerator);
            if (commitInfo.clientModified != null) {
                jsonGenerator.writeFieldName("client_modified");
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(commitInfo.clientModified, jsonGenerator);
            }
            jsonGenerator.writeFieldName("mute");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(commitInfo.mute), jsonGenerator);
            if (commitInfo.propertyGroups != null) {
                jsonGenerator.writeFieldName("property_groups");
                StoneSerializers.nullable(StoneSerializers.list(com.dropbox.core.p005v2.fileproperties.PropertyGroup.Serializer.INSTANCE)).serialize(commitInfo.propertyGroups, jsonGenerator);
            }
            jsonGenerator.writeFieldName("strict_conflict");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(commitInfo.strictConflict), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public CommitInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    CommitInfo commitInfo = new CommitInfo(str2, writeMode2, valueOf.booleanValue(), date, valueOf2.booleanValue(), list, valueOf3.booleanValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(commitInfo, commitInfo.toStringMultiline());
                    return commitInfo;
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

    public CommitInfo(String str, WriteMode writeMode, boolean z, Date date, boolean z2, List<PropertyGroup> list, boolean z3) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)|(id:.*)", str)) {
            this.path = str;
            if (writeMode != null) {
                this.mode = writeMode;
                this.autorename = z;
                this.clientModified = LangUtil.truncateMillis(date);
                this.mute = z2;
                if (list != null) {
                    for (PropertyGroup propertyGroup : list) {
                        if (propertyGroup == null) {
                            throw new IllegalArgumentException("An item in list 'propertyGroups' is null");
                        }
                    }
                }
                this.propertyGroups = list;
                this.strictConflict = z3;
                return;
            }
            throw new IllegalArgumentException("Required value for 'mode' is null");
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public CommitInfo(String str) {
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
        return Arrays.hashCode(new Object[]{this.path, this.mode, Boolean.valueOf(this.autorename), this.clientModified, Boolean.valueOf(this.mute), this.propertyGroups, Boolean.valueOf(this.strictConflict)});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0056, code lost:
        if (r2.equals(r3) == false) goto L_0x005f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x005c, code lost:
        if (r4.strictConflict != r5.strictConflict) goto L_0x005f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0061
            com.dropbox.core.v2.files.CommitInfo r5 = (com.dropbox.core.p005v2.files.CommitInfo) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005f
        L_0x0024:
            com.dropbox.core.v2.files.WriteMode r2 = r4.mode
            com.dropbox.core.v2.files.WriteMode r3 = r5.mode
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005f
        L_0x0030:
            boolean r2 = r4.autorename
            boolean r3 = r5.autorename
            if (r2 != r3) goto L_0x005f
            java.util.Date r2 = r4.clientModified
            java.util.Date r3 = r5.clientModified
            if (r2 == r3) goto L_0x0044
            if (r2 == 0) goto L_0x005f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005f
        L_0x0044:
            boolean r2 = r4.mute
            boolean r3 = r5.mute
            if (r2 != r3) goto L_0x005f
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r2 = r4.propertyGroups
            java.util.List<com.dropbox.core.v2.fileproperties.PropertyGroup> r3 = r5.propertyGroups
            if (r2 == r3) goto L_0x0058
            if (r2 == 0) goto L_0x005f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005f
        L_0x0058:
            boolean r2 = r4.strictConflict
            boolean r5 = r5.strictConflict
            if (r2 != r5) goto L_0x005f
            goto L_0x0060
        L_0x005f:
            r0 = 0
        L_0x0060:
            return r0
        L_0x0061:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.CommitInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
