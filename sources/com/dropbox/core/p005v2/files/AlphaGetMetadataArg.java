package com.dropbox.core.p005v2.files;

import com.dropbox.core.p005v2.fileproperties.TemplateFilterBase;
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

/* renamed from: com.dropbox.core.v2.files.AlphaGetMetadataArg */
class AlphaGetMetadataArg extends GetMetadataArg {
    protected final List<String> includePropertyTemplates;

    /* renamed from: com.dropbox.core.v2.files.AlphaGetMetadataArg$Builder */
    public static class Builder extends com.dropbox.core.p005v2.files.GetMetadataArg.Builder {
        protected List<String> includePropertyTemplates = null;

        protected Builder(String str) {
            super(str);
        }

        public Builder withIncludePropertyTemplates(List<String> list) {
            if (list != null) {
                for (String str : list) {
                    if (str == null) {
                        throw new IllegalArgumentException("An item in list 'includePropertyTemplates' is null");
                    } else if (str.length() < 1) {
                        throw new IllegalArgumentException("Stringan item in list 'includePropertyTemplates' is shorter than 1");
                    } else if (!Pattern.matches("(/|ptid:).*", str)) {
                        throw new IllegalArgumentException("Stringan item in list 'includePropertyTemplates' does not match pattern");
                    }
                }
            }
            this.includePropertyTemplates = list;
            return this;
        }

        public Builder withIncludeMediaInfo(Boolean bool) {
            super.withIncludeMediaInfo(bool);
            return this;
        }

        public Builder withIncludeDeleted(Boolean bool) {
            super.withIncludeDeleted(bool);
            return this;
        }

        public Builder withIncludeHasExplicitSharedMembers(Boolean bool) {
            super.withIncludeHasExplicitSharedMembers(bool);
            return this;
        }

        public Builder withIncludePropertyGroups(TemplateFilterBase templateFilterBase) {
            super.withIncludePropertyGroups(templateFilterBase);
            return this;
        }

        public AlphaGetMetadataArg build() {
            AlphaGetMetadataArg alphaGetMetadataArg = new AlphaGetMetadataArg(this.path, this.includeMediaInfo, this.includeDeleted, this.includeHasExplicitSharedMembers, this.includePropertyGroups, this.includePropertyTemplates);
            return alphaGetMetadataArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.AlphaGetMetadataArg$Serializer */
    static class Serializer extends StructSerializer<AlphaGetMetadataArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AlphaGetMetadataArg alphaGetMetadataArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(alphaGetMetadataArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("include_media_info");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(alphaGetMetadataArg.includeMediaInfo), jsonGenerator);
            jsonGenerator.writeFieldName("include_deleted");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(alphaGetMetadataArg.includeDeleted), jsonGenerator);
            jsonGenerator.writeFieldName("include_has_explicit_shared_members");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(alphaGetMetadataArg.includeHasExplicitSharedMembers), jsonGenerator);
            if (alphaGetMetadataArg.includePropertyGroups != null) {
                jsonGenerator.writeFieldName("include_property_groups");
                StoneSerializers.nullable(com.dropbox.core.p005v2.fileproperties.TemplateFilterBase.Serializer.INSTANCE).serialize(alphaGetMetadataArg.includePropertyGroups, jsonGenerator);
            }
            if (alphaGetMetadataArg.includePropertyTemplates != null) {
                jsonGenerator.writeFieldName("include_property_templates");
                StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).serialize(alphaGetMetadataArg.includePropertyTemplates, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AlphaGetMetadataArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                TemplateFilterBase templateFilterBase = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("include_media_info".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("include_deleted".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("include_has_explicit_shared_members".equals(currentName)) {
                        valueOf3 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("include_property_groups".equals(currentName)) {
                        templateFilterBase = (TemplateFilterBase) StoneSerializers.nullable(com.dropbox.core.p005v2.fileproperties.TemplateFilterBase.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("include_property_templates".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(StoneSerializers.string())).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    AlphaGetMetadataArg alphaGetMetadataArg = new AlphaGetMetadataArg(str2, valueOf.booleanValue(), valueOf2.booleanValue(), valueOf3.booleanValue(), templateFilterBase, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(alphaGetMetadataArg, alphaGetMetadataArg.toStringMultiline());
                    return alphaGetMetadataArg;
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

    public AlphaGetMetadataArg(String str, boolean z, boolean z2, boolean z3, TemplateFilterBase templateFilterBase, List<String> list) {
        super(str, z, z2, z3, templateFilterBase);
        if (list != null) {
            for (String str2 : list) {
                if (str2 == null) {
                    throw new IllegalArgumentException("An item in list 'includePropertyTemplates' is null");
                } else if (str2.length() < 1) {
                    throw new IllegalArgumentException("Stringan item in list 'includePropertyTemplates' is shorter than 1");
                } else if (!Pattern.matches("(/|ptid:).*", str2)) {
                    throw new IllegalArgumentException("Stringan item in list 'includePropertyTemplates' does not match pattern");
                }
            }
        }
        this.includePropertyTemplates = list;
    }

    public AlphaGetMetadataArg(String str) {
        this(str, false, false, false, null, null);
    }

    public String getPath() {
        return this.path;
    }

    public boolean getIncludeMediaInfo() {
        return this.includeMediaInfo;
    }

    public boolean getIncludeDeleted() {
        return this.includeDeleted;
    }

    public boolean getIncludeHasExplicitSharedMembers() {
        return this.includeHasExplicitSharedMembers;
    }

    public TemplateFilterBase getIncludePropertyGroups() {
        return this.includePropertyGroups;
    }

    public List<String> getIncludePropertyTemplates() {
        return this.includePropertyTemplates;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.includePropertyTemplates});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005a, code lost:
        if (r2.equals(r5) == false) goto L_0x005d;
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
            if (r2 == 0) goto L_0x005f
            com.dropbox.core.v2.files.AlphaGetMetadataArg r5 = (com.dropbox.core.p005v2.files.AlphaGetMetadataArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x0028:
            boolean r2 = r4.includeMediaInfo
            boolean r3 = r5.includeMediaInfo
            if (r2 != r3) goto L_0x005d
            boolean r2 = r4.includeDeleted
            boolean r3 = r5.includeDeleted
            if (r2 != r3) goto L_0x005d
            boolean r2 = r4.includeHasExplicitSharedMembers
            boolean r3 = r5.includeHasExplicitSharedMembers
            if (r2 != r3) goto L_0x005d
            com.dropbox.core.v2.fileproperties.TemplateFilterBase r2 = r4.includePropertyGroups
            com.dropbox.core.v2.fileproperties.TemplateFilterBase r3 = r5.includePropertyGroups
            if (r2 == r3) goto L_0x004e
            com.dropbox.core.v2.fileproperties.TemplateFilterBase r2 = r4.includePropertyGroups
            if (r2 == 0) goto L_0x005d
            com.dropbox.core.v2.fileproperties.TemplateFilterBase r2 = r4.includePropertyGroups
            com.dropbox.core.v2.fileproperties.TemplateFilterBase r3 = r5.includePropertyGroups
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005d
        L_0x004e:
            java.util.List<java.lang.String> r2 = r4.includePropertyTemplates
            java.util.List<java.lang.String> r5 = r5.includePropertyTemplates
            if (r2 == r5) goto L_0x005e
            if (r2 == 0) goto L_0x005d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x005d
            goto L_0x005e
        L_0x005d:
            r0 = 0
        L_0x005e:
            return r0
        L_0x005f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.AlphaGetMetadataArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
