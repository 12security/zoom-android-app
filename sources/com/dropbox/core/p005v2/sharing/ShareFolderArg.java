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
import java.util.List;

/* renamed from: com.dropbox.core.v2.sharing.ShareFolderArg */
class ShareFolderArg extends ShareFolderArgBase {
    protected final List<FolderAction> actions;
    protected final LinkSettings linkSettings;

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderArg$Builder */
    public static class Builder extends com.dropbox.core.p005v2.sharing.ShareFolderArgBase.Builder {
        protected List<FolderAction> actions = null;
        protected LinkSettings linkSettings = null;

        protected Builder(String str) {
            super(str);
        }

        public Builder withActions(List<FolderAction> list) {
            if (list != null) {
                for (FolderAction folderAction : list) {
                    if (folderAction == null) {
                        throw new IllegalArgumentException("An item in list 'actions' is null");
                    }
                }
            }
            this.actions = list;
            return this;
        }

        public Builder withLinkSettings(LinkSettings linkSettings2) {
            this.linkSettings = linkSettings2;
            return this;
        }

        public Builder withAclUpdatePolicy(AclUpdatePolicy aclUpdatePolicy) {
            super.withAclUpdatePolicy(aclUpdatePolicy);
            return this;
        }

        public Builder withForceAsync(Boolean bool) {
            super.withForceAsync(bool);
            return this;
        }

        public Builder withMemberPolicy(MemberPolicy memberPolicy) {
            super.withMemberPolicy(memberPolicy);
            return this;
        }

        public Builder withSharedLinkPolicy(SharedLinkPolicy sharedLinkPolicy) {
            super.withSharedLinkPolicy(sharedLinkPolicy);
            return this;
        }

        public Builder withViewerInfoPolicy(ViewerInfoPolicy viewerInfoPolicy) {
            super.withViewerInfoPolicy(viewerInfoPolicy);
            return this;
        }

        public Builder withAccessInheritance(AccessInheritance accessInheritance) {
            super.withAccessInheritance(accessInheritance);
            return this;
        }

        public ShareFolderArg build() {
            ShareFolderArg shareFolderArg = new ShareFolderArg(this.path, this.aclUpdatePolicy, this.forceAsync, this.memberPolicy, this.sharedLinkPolicy, this.viewerInfoPolicy, this.accessInheritance, this.actions, this.linkSettings);
            return shareFolderArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.ShareFolderArg$Serializer */
    static class Serializer extends StructSerializer<ShareFolderArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(ShareFolderArg shareFolderArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("path");
            StoneSerializers.string().serialize(shareFolderArg.path, jsonGenerator);
            if (shareFolderArg.aclUpdatePolicy != null) {
                jsonGenerator.writeFieldName("acl_update_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AclUpdatePolicy.Serializer.INSTANCE).serialize(shareFolderArg.aclUpdatePolicy, jsonGenerator);
            }
            jsonGenerator.writeFieldName("force_async");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(shareFolderArg.forceAsync), jsonGenerator);
            if (shareFolderArg.memberPolicy != null) {
                jsonGenerator.writeFieldName("member_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).serialize(shareFolderArg.memberPolicy, jsonGenerator);
            }
            if (shareFolderArg.sharedLinkPolicy != null) {
                jsonGenerator.writeFieldName("shared_link_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.SharedLinkPolicy.Serializer.INSTANCE).serialize(shareFolderArg.sharedLinkPolicy, jsonGenerator);
            }
            if (shareFolderArg.viewerInfoPolicy != null) {
                jsonGenerator.writeFieldName("viewer_info_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.ViewerInfoPolicy.Serializer.INSTANCE).serialize(shareFolderArg.viewerInfoPolicy, jsonGenerator);
            }
            jsonGenerator.writeFieldName("access_inheritance");
            Serializer.INSTANCE.serialize(shareFolderArg.accessInheritance, jsonGenerator);
            if (shareFolderArg.actions != null) {
                jsonGenerator.writeFieldName("actions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(shareFolderArg.actions, jsonGenerator);
            }
            if (shareFolderArg.linkSettings != null) {
                jsonGenerator.writeFieldName("link_settings");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(shareFolderArg.linkSettings, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public ShareFolderArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                List list = null;
                LinkSettings linkSettings = null;
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
                    } else if ("actions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("link_settings".equals(currentName)) {
                        linkSettings = (LinkSettings) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    ShareFolderArg shareFolderArg = new ShareFolderArg(str2, aclUpdatePolicy, valueOf.booleanValue(), memberPolicy, sharedLinkPolicy, viewerInfoPolicy, accessInheritance, list, linkSettings);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(shareFolderArg, shareFolderArg.toStringMultiline());
                    return shareFolderArg;
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

    public ShareFolderArg(String str, AclUpdatePolicy aclUpdatePolicy, boolean z, MemberPolicy memberPolicy, SharedLinkPolicy sharedLinkPolicy, ViewerInfoPolicy viewerInfoPolicy, AccessInheritance accessInheritance, List<FolderAction> list, LinkSettings linkSettings2) {
        super(str, aclUpdatePolicy, z, memberPolicy, sharedLinkPolicy, viewerInfoPolicy, accessInheritance);
        if (list != null) {
            for (FolderAction folderAction : list) {
                if (folderAction == null) {
                    throw new IllegalArgumentException("An item in list 'actions' is null");
                }
            }
        }
        this.actions = list;
        this.linkSettings = linkSettings2;
    }

    public ShareFolderArg(String str) {
        this(str, null, false, null, null, null, AccessInheritance.INHERIT, null, null);
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

    public List<FolderAction> getActions() {
        return this.actions;
    }

    public LinkSettings getLinkSettings() {
        return this.linkSettings;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return (super.hashCode() * 31) + Arrays.hashCode(new Object[]{this.actions, this.linkSettings});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00a8, code lost:
        if (r2.equals(r5) == false) goto L_0x00ab;
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
            if (r2 == 0) goto L_0x00ad
            com.dropbox.core.v2.sharing.ShareFolderArg r5 = (com.dropbox.core.p005v2.sharing.ShareFolderArg) r5
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            if (r2 == r3) goto L_0x0028
            java.lang.String r2 = r4.path
            java.lang.String r3 = r5.path
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x0028:
            com.dropbox.core.v2.sharing.AclUpdatePolicy r2 = r4.aclUpdatePolicy
            com.dropbox.core.v2.sharing.AclUpdatePolicy r3 = r5.aclUpdatePolicy
            if (r2 == r3) goto L_0x003c
            com.dropbox.core.v2.sharing.AclUpdatePolicy r2 = r4.aclUpdatePolicy
            if (r2 == 0) goto L_0x00ab
            com.dropbox.core.v2.sharing.AclUpdatePolicy r2 = r4.aclUpdatePolicy
            com.dropbox.core.v2.sharing.AclUpdatePolicy r3 = r5.aclUpdatePolicy
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x003c:
            boolean r2 = r4.forceAsync
            boolean r3 = r5.forceAsync
            if (r2 != r3) goto L_0x00ab
            com.dropbox.core.v2.sharing.MemberPolicy r2 = r4.memberPolicy
            com.dropbox.core.v2.sharing.MemberPolicy r3 = r5.memberPolicy
            if (r2 == r3) goto L_0x0056
            com.dropbox.core.v2.sharing.MemberPolicy r2 = r4.memberPolicy
            if (r2 == 0) goto L_0x00ab
            com.dropbox.core.v2.sharing.MemberPolicy r2 = r4.memberPolicy
            com.dropbox.core.v2.sharing.MemberPolicy r3 = r5.memberPolicy
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x0056:
            com.dropbox.core.v2.sharing.SharedLinkPolicy r2 = r4.sharedLinkPolicy
            com.dropbox.core.v2.sharing.SharedLinkPolicy r3 = r5.sharedLinkPolicy
            if (r2 == r3) goto L_0x006a
            com.dropbox.core.v2.sharing.SharedLinkPolicy r2 = r4.sharedLinkPolicy
            if (r2 == 0) goto L_0x00ab
            com.dropbox.core.v2.sharing.SharedLinkPolicy r2 = r4.sharedLinkPolicy
            com.dropbox.core.v2.sharing.SharedLinkPolicy r3 = r5.sharedLinkPolicy
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x006a:
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r2 = r4.viewerInfoPolicy
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r3 = r5.viewerInfoPolicy
            if (r2 == r3) goto L_0x007e
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r2 = r4.viewerInfoPolicy
            if (r2 == 0) goto L_0x00ab
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r2 = r4.viewerInfoPolicy
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r3 = r5.viewerInfoPolicy
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x007e:
            com.dropbox.core.v2.sharing.AccessInheritance r2 = r4.accessInheritance
            com.dropbox.core.v2.sharing.AccessInheritance r3 = r5.accessInheritance
            if (r2 == r3) goto L_0x008e
            com.dropbox.core.v2.sharing.AccessInheritance r2 = r4.accessInheritance
            com.dropbox.core.v2.sharing.AccessInheritance r3 = r5.accessInheritance
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x008e:
            java.util.List<com.dropbox.core.v2.sharing.FolderAction> r2 = r4.actions
            java.util.List<com.dropbox.core.v2.sharing.FolderAction> r3 = r5.actions
            if (r2 == r3) goto L_0x009c
            if (r2 == 0) goto L_0x00ab
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x00ab
        L_0x009c:
            com.dropbox.core.v2.sharing.LinkSettings r2 = r4.linkSettings
            com.dropbox.core.v2.sharing.LinkSettings r5 = r5.linkSettings
            if (r2 == r5) goto L_0x00ac
            if (r2 == 0) goto L_0x00ab
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00ab
            goto L_0x00ac
        L_0x00ab:
            r0 = 0
        L_0x00ac:
            return r0
        L_0x00ad:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.ShareFolderArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
