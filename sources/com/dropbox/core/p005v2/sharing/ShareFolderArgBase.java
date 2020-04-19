package com.dropbox.core.p005v2.sharing;

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

/* renamed from: com.dropbox.core.v2.sharing.ShareFolderArgBase */
class ShareFolderArgBase {
    protected final AccessInheritance accessInheritance;
    protected final AclUpdatePolicy aclUpdatePolicy;
    protected final boolean forceAsync;
    protected final MemberPolicy memberPolicy;
    protected final String path;
    protected final SharedLinkPolicy sharedLinkPolicy;
    protected final ViewerInfoPolicy viewerInfoPolicy;

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderArgBase$Builder */
    public static class Builder {
        protected AccessInheritance accessInheritance;
        protected AclUpdatePolicy aclUpdatePolicy;
        protected boolean forceAsync;
        protected MemberPolicy memberPolicy;
        protected final String path;
        protected SharedLinkPolicy sharedLinkPolicy;
        protected ViewerInfoPolicy viewerInfoPolicy;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'path' is null");
            } else if (Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)", str)) {
                this.path = str;
                this.aclUpdatePolicy = null;
                this.forceAsync = false;
                this.memberPolicy = null;
                this.sharedLinkPolicy = null;
                this.viewerInfoPolicy = null;
                this.accessInheritance = AccessInheritance.INHERIT;
            } else {
                throw new IllegalArgumentException("String 'path' does not match pattern");
            }
        }

        public Builder withAclUpdatePolicy(AclUpdatePolicy aclUpdatePolicy2) {
            this.aclUpdatePolicy = aclUpdatePolicy2;
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

        public Builder withMemberPolicy(MemberPolicy memberPolicy2) {
            this.memberPolicy = memberPolicy2;
            return this;
        }

        public Builder withSharedLinkPolicy(SharedLinkPolicy sharedLinkPolicy2) {
            this.sharedLinkPolicy = sharedLinkPolicy2;
            return this;
        }

        public Builder withViewerInfoPolicy(ViewerInfoPolicy viewerInfoPolicy2) {
            this.viewerInfoPolicy = viewerInfoPolicy2;
            return this;
        }

        public Builder withAccessInheritance(AccessInheritance accessInheritance2) {
            if (accessInheritance2 != null) {
                this.accessInheritance = accessInheritance2;
            } else {
                this.accessInheritance = AccessInheritance.INHERIT;
            }
            return this;
        }

        public ShareFolderArgBase build() {
            ShareFolderArgBase shareFolderArgBase = new ShareFolderArgBase(this.path, this.aclUpdatePolicy, this.forceAsync, this.memberPolicy, this.sharedLinkPolicy, this.viewerInfoPolicy, this.accessInheritance);
            return shareFolderArgBase;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderArgBase$Serializer */
    private static class Serializer extends StructSerializer<ShareFolderArgBase> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
        }

        public void serialize(ShareFolderArgBase shareFolderArgBase, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(shareFolderArgBase.path, jsonGenerator);
            if (shareFolderArgBase.aclUpdatePolicy != null) {
                jsonGenerator.writeFieldName("acl_update_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AclUpdatePolicy.Serializer.INSTANCE).serialize(shareFolderArgBase.aclUpdatePolicy, jsonGenerator);
            }
            jsonGenerator.writeFieldName("force_async");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(shareFolderArgBase.forceAsync), jsonGenerator);
            if (shareFolderArgBase.memberPolicy != null) {
                jsonGenerator.writeFieldName("member_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).serialize(shareFolderArgBase.memberPolicy, jsonGenerator);
            }
            if (shareFolderArgBase.sharedLinkPolicy != null) {
                jsonGenerator.writeFieldName("shared_link_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.SharedLinkPolicy.Serializer.INSTANCE).serialize(shareFolderArgBase.sharedLinkPolicy, jsonGenerator);
            }
            if (shareFolderArgBase.viewerInfoPolicy != null) {
                jsonGenerator.writeFieldName("viewer_info_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.ViewerInfoPolicy.Serializer.INSTANCE).serialize(shareFolderArgBase.viewerInfoPolicy, jsonGenerator);
            }
            jsonGenerator.writeFieldName("access_inheritance");
            Serializer.INSTANCE.serialize(shareFolderArgBase.accessInheritance, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ShareFolderArgBase deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Boolean valueOf = Boolean.valueOf(false);
                String str2 = null;
                AclUpdatePolicy aclUpdatePolicy = null;
                MemberPolicy memberPolicy = null;
                SharedLinkPolicy sharedLinkPolicy = null;
                ViewerInfoPolicy viewerInfoPolicy = null;
                AccessInheritance accessInheritance = AccessInheritance.INHERIT;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("path".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("acl_update_policy".equals(currentName)) {
                        aclUpdatePolicy = (AclUpdatePolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AclUpdatePolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("force_async".equals(currentName)) {
                        valueOf = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("member_policy".equals(currentName)) {
                        memberPolicy = (MemberPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("shared_link_policy".equals(currentName)) {
                        sharedLinkPolicy = (SharedLinkPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.SharedLinkPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("viewer_info_policy".equals(currentName)) {
                        viewerInfoPolicy = (ViewerInfoPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.ViewerInfoPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("access_inheritance".equals(currentName)) {
                        accessInheritance = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ShareFolderArgBase shareFolderArgBase = new ShareFolderArgBase(str2, aclUpdatePolicy, valueOf.booleanValue(), memberPolicy, sharedLinkPolicy, viewerInfoPolicy, accessInheritance);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(shareFolderArgBase, shareFolderArgBase.toStringMultiline());
                    return shareFolderArgBase;
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

    public ShareFolderArgBase(String str, AclUpdatePolicy aclUpdatePolicy2, boolean z, MemberPolicy memberPolicy2, SharedLinkPolicy sharedLinkPolicy2, ViewerInfoPolicy viewerInfoPolicy2, AccessInheritance accessInheritance2) {
        this.aclUpdatePolicy = aclUpdatePolicy2;
        this.forceAsync = z;
        this.memberPolicy = memberPolicy2;
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'path' is null");
        } else if (Pattern.matches("(/(.|[\\r\\n])*)|(ns:[0-9]+(/.*)?)", str)) {
            this.path = str;
            this.sharedLinkPolicy = sharedLinkPolicy2;
            this.viewerInfoPolicy = viewerInfoPolicy2;
            if (accessInheritance2 != null) {
                this.accessInheritance = accessInheritance2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'accessInheritance' is null");
        } else {
            throw new IllegalArgumentException("String 'path' does not match pattern");
        }
    }

    public ShareFolderArgBase(String str) {
        this(str, null, false, null, null, null, AccessInheritance.INHERIT);
    }

    public String getPath() {
        return this.path;
    }

    public AclUpdatePolicy getAclUpdatePolicy() {
        return this.aclUpdatePolicy;
    }

    public boolean getForceAsync() {
        return this.forceAsync;
    }

    public MemberPolicy getMemberPolicy() {
        return this.memberPolicy;
    }

    public SharedLinkPolicy getSharedLinkPolicy() {
        return this.sharedLinkPolicy;
    }

    public ViewerInfoPolicy getViewerInfoPolicy() {
        return this.viewerInfoPolicy;
    }

    public AccessInheritance getAccessInheritance() {
        return this.accessInheritance;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.aclUpdatePolicy, Boolean.valueOf(this.forceAsync), this.memberPolicy, this.path, this.sharedLinkPolicy, this.viewerInfoPolicy, this.accessInheritance});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006c, code lost:
        if (r2.equals(r5) == false) goto L_0x006f;
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
            if (r2 == 0) goto L_0x0071
            com.dropbox.core.v2.sharing.ShareFolderArgBase r5 = (com.dropbox.core.p005v2.sharing.ShareFolderArgBase) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0024:
            com.dropbox.core.v2.sharing.AclUpdatePolicy r2 = r4.aclUpdatePolicy
            com.dropbox.core.v2.sharing.AclUpdatePolicy r3 = r5.aclUpdatePolicy
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x006f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0032:
            boolean r2 = r4.forceAsync
            boolean r3 = r5.forceAsync
            if (r2 != r3) goto L_0x006f
            com.dropbox.core.v2.sharing.MemberPolicy r2 = r4.memberPolicy
            com.dropbox.core.v2.sharing.MemberPolicy r3 = r5.memberPolicy
            if (r2 == r3) goto L_0x0046
            if (r2 == 0) goto L_0x006f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0046:
            com.dropbox.core.v2.sharing.SharedLinkPolicy r2 = r4.sharedLinkPolicy
            com.dropbox.core.v2.sharing.SharedLinkPolicy r3 = r5.sharedLinkPolicy
            if (r2 == r3) goto L_0x0054
            if (r2 == 0) goto L_0x006f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0054:
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r2 = r4.viewerInfoPolicy
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r3 = r5.viewerInfoPolicy
            if (r2 == r3) goto L_0x0062
            if (r2 == 0) goto L_0x006f
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x006f
        L_0x0062:
            com.dropbox.core.v2.sharing.AccessInheritance r2 = r4.accessInheritance
            com.dropbox.core.v2.sharing.AccessInheritance r5 = r5.accessInheritance
            if (r2 == r5) goto L_0x0070
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x006f
            goto L_0x0070
        L_0x006f:
            r0 = 0
        L_0x0070:
            return r0
        L_0x0071:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.ShareFolderArgBase.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
