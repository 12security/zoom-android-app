package com.dropbox.core.p005v2.files;

import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxList;
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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.ListFolderArg */
class ListFolderArg {
    protected final boolean includeDeleted;
    protected final boolean includeHasExplicitSharedMembers;
    protected final boolean includeMediaInfo;
    protected final boolean includeMountedFolders;
    protected final TemplateFilterBase includePropertyGroups;
    protected final Long limit;
    protected final String path;
    protected final boolean recursive;
    protected final SharedLink sharedLink;

    /* renamed from: com.dropbox.core.v2.files.ListFolderArg$Builder */
    public static class Builder {
        protected boolean includeDeleted;
        protected boolean includeHasExplicitSharedMembers;
        protected boolean includeMediaInfo;
        protected boolean includeMountedFolders;
        protected TemplateFilterBase includePropertyGroups;
        protected Long limit;
        protected final String path;
        protected boolean recursive;
        protected SharedLink sharedLink;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'path' is null");
            } else if (Pattern.matches("(/(.|[\\r\\n])*)?|id:.*|(ns:[0-9]+(/.*)?)", str)) {
                this.path = str;
                this.recursive = false;
                this.includeMediaInfo = false;
                this.includeDeleted = false;
                this.includeHasExplicitSharedMembers = false;
                this.includeMountedFolders = true;
                this.limit = null;
                this.sharedLink = null;
                this.includePropertyGroups = null;
            } else {
                throw new IllegalArgumentException("String 'path' does not match pattern");
            }
        }

        public Builder withRecursive(Boolean bool) {
            if (bool != null) {
                this.recursive = bool.booleanValue();
            } else {
                this.recursive = false;
            }
            return this;
        }

        public Builder withIncludeMediaInfo(Boolean bool) {
            if (bool != null) {
                this.includeMediaInfo = bool.booleanValue();
            } else {
                this.includeMediaInfo = false;
            }
            return this;
        }

        public Builder withIncludeDeleted(Boolean bool) {
            if (bool != null) {
                this.includeDeleted = bool.booleanValue();
            } else {
                this.includeDeleted = false;
            }
            return this;
        }

        public Builder withIncludeHasExplicitSharedMembers(Boolean bool) {
            if (bool != null) {
                this.includeHasExplicitSharedMembers = bool.booleanValue();
            } else {
                this.includeHasExplicitSharedMembers = false;
            }
            return this;
        }

        public Builder withIncludeMountedFolders(Boolean bool) {
            if (bool != null) {
                this.includeMountedFolders = bool.booleanValue();
            } else {
                this.includeMountedFolders = true;
            }
            return this;
        }

        public Builder withLimit(Long l) {
            if (l != null) {
                if (l.longValue() < 1) {
                    throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
                } else if (l.longValue() > 2000) {
                    throw new IllegalArgumentException("Number 'limit' is larger than 2000L");
                }
            }
            this.limit = l;
            return this;
        }

        public Builder withSharedLink(SharedLink sharedLink2) {
            this.sharedLink = sharedLink2;
            return this;
        }

        public Builder withIncludePropertyGroups(TemplateFilterBase templateFilterBase) {
            this.includePropertyGroups = templateFilterBase;
            return this;
        }

        public ListFolderArg build() {
            ListFolderArg listFolderArg = new ListFolderArg(this.path, this.recursive, this.includeMediaInfo, this.includeDeleted, this.includeHasExplicitSharedMembers, this.includeMountedFolders, this.limit, this.sharedLink, this.includePropertyGroups);
            return listFolderArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.files.ListFolderArg$Serializer */
    static class Serializer extends StructSerializer<ListFolderArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ListFolderArg listFolderArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(listFolderArg.path, jsonGenerator);
            jsonGenerator.writeFieldName("recursive");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listFolderArg.recursive), jsonGenerator);
            jsonGenerator.writeFieldName("include_media_info");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listFolderArg.includeMediaInfo), jsonGenerator);
            jsonGenerator.writeFieldName("include_deleted");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listFolderArg.includeDeleted), jsonGenerator);
            jsonGenerator.writeFieldName("include_has_explicit_shared_members");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listFolderArg.includeHasExplicitSharedMembers), jsonGenerator);
            jsonGenerator.writeFieldName("include_mounted_folders");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(listFolderArg.includeMountedFolders), jsonGenerator);
            if (listFolderArg.limit != null) {
                jsonGenerator.writeFieldName(BoxList.FIELD_LIMIT);
                StoneSerializers.nullable(StoneSerializers.uInt32()).serialize(listFolderArg.limit, jsonGenerator);
            }
            if (listFolderArg.sharedLink != null) {
                jsonGenerator.writeFieldName(BoxItem.FIELD_SHARED_LINK);
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(listFolderArg.sharedLink, jsonGenerator);
            }
            if (listFolderArg.includePropertyGroups != null) {
                jsonGenerator.writeFieldName("include_property_groups");
                StoneSerializers.nullable(com.dropbox.core.p005v2.fileproperties.TemplateFilterBase.Serializer.INSTANCE).serialize(listFolderArg.includePropertyGroups, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ListFolderArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            JsonParser jsonParser2 = jsonParser;
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
                Boolean valueOf4 = Boolean.valueOf(false);
                Boolean valueOf5 = Boolean.valueOf(true);
                String str2 = null;
                Long l = null;
                SharedLink sharedLink = null;
                TemplateFilterBase templateFilterBase = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser2);
                    } else if ("recursive".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("include_media_info".equals(currentName)) {
                        valueOf2 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("include_deleted".equals(currentName)) {
                        valueOf3 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("include_has_explicit_shared_members".equals(currentName)) {
                        valueOf4 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if ("include_mounted_folders".equals(currentName)) {
                        valueOf5 = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser2);
                    } else if (BoxList.FIELD_LIMIT.equals(currentName)) {
                        l = (Long) StoneSerializers.nullable(StoneSerializers.uInt32()).deserialize(jsonParser2);
                    } else if (BoxItem.FIELD_SHARED_LINK.equals(currentName)) {
                        sharedLink = (SharedLink) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser2);
                    } else if ("include_property_groups".equals(currentName)) {
                        templateFilterBase = (TemplateFilterBase) StoneSerializers.nullable(com.dropbox.core.p005v2.fileproperties.TemplateFilterBase.Serializer.INSTANCE).deserialize(jsonParser2);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ListFolderArg listFolderArg = new ListFolderArg(str2, valueOf.booleanValue(), valueOf2.booleanValue(), valueOf3.booleanValue(), valueOf4.booleanValue(), valueOf5.booleanValue(), l, sharedLink, templateFilterBase);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(listFolderArg, listFolderArg.toStringMultiline());
                    return listFolderArg;
                }
                throw new JsonParseException(jsonParser2, "Required field \"path\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser2, sb.toString());
        }
    }

    public ListFolderArg(String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, Long l, SharedLink sharedLink2, TemplateFilterBase templateFilterBase) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("(/(.|[\\r\\n])*)?|id:.*|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            this.recursive = z;
            this.includeMediaInfo = z2;
            this.includeDeleted = z3;
            this.includeHasExplicitSharedMembers = z4;
            this.includeMountedFolders = z5;
            if (l != null) {
                if (l.longValue() < 1) {
                    throw new IllegalArgumentException("Number 'limit' is smaller than 1L");
                } else if (l.longValue() > 2000) {
                    throw new IllegalArgumentException("Number 'limit' is larger than 2000L");
                }
            }
            this.limit = l;
            this.sharedLink = sharedLink2;
            this.includePropertyGroups = templateFilterBase;
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public ListFolderArg(String str) {
        this(str, false, false, false, false, true, null, null, null);
    }

    public String getPath() {
        return this.path;
    }

    public boolean getRecursive() {
        return this.recursive;
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

    public boolean getIncludeMountedFolders() {
        return this.includeMountedFolders;
    }

    public Long getLimit() {
        return this.limit;
    }

    public SharedLink getSharedLink() {
        return this.sharedLink;
    }

    public TemplateFilterBase getIncludePropertyGroups() {
        return this.includePropertyGroups;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.path, Boolean.valueOf(this.recursive), Boolean.valueOf(this.includeMediaInfo), Boolean.valueOf(this.includeDeleted), Boolean.valueOf(this.includeHasExplicitSharedMembers), Boolean.valueOf(this.includeMountedFolders), this.limit, this.sharedLink, this.includePropertyGroups});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x006a, code lost:
        if (r2.equals(r5) == false) goto L_0x006d;
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
            if (r2 == 0) goto L_0x006f
            com.dropbox.core.v2.files.ListFolderArg r5 = (com.dropbox.core.p005v2.files.ListFolderArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0024:
            boolean r2 = r4.recursive
            boolean r3 = r5.recursive
            if (r2 != r3) goto L_0x006d
            boolean r2 = r4.includeMediaInfo
            boolean r3 = r5.includeMediaInfo
            if (r2 != r3) goto L_0x006d
            boolean r2 = r4.includeDeleted
            boolean r3 = r5.includeDeleted
            if (r2 != r3) goto L_0x006d
            boolean r2 = r4.includeHasExplicitSharedMembers
            boolean r3 = r5.includeHasExplicitSharedMembers
            if (r2 != r3) goto L_0x006d
            boolean r2 = r4.includeMountedFolders
            boolean r3 = r5.includeMountedFolders
            if (r2 != r3) goto L_0x006d
            java.lang.Long r2 = r4.limit
            java.lang.Long r3 = r5.limit
            if (r2 == r3) goto L_0x0050
            if (r2 == 0) goto L_0x006d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x0050:
            com.dropbox.core.v2.files.SharedLink r2 = r4.sharedLink
            com.dropbox.core.v2.files.SharedLink r3 = r5.sharedLink
            if (r2 == r3) goto L_0x005e
            if (r2 == 0) goto L_0x006d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006d
        L_0x005e:
            com.dropbox.core.v2.fileproperties.TemplateFilterBase r2 = r4.includePropertyGroups
            com.dropbox.core.v2.fileproperties.TemplateFilterBase r5 = r5.includePropertyGroups
            if (r2 == r5) goto L_0x006e
            if (r2 == 0) goto L_0x006d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x006d
            goto L_0x006e
        L_0x006d:
            r0 = 0
        L_0x006e:
            return r0
        L_0x006f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.files.ListFolderArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
