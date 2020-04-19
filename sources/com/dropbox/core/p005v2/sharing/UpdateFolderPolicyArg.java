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
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.sharing.UpdateFolderPolicyArg */
class UpdateFolderPolicyArg {
    protected final AclUpdatePolicy aclUpdatePolicy;
    protected final List<FolderAction> actions;
    protected final LinkSettings linkSettings;
    protected final MemberPolicy memberPolicy;
    protected final String sharedFolderId;
    protected final SharedLinkPolicy sharedLinkPolicy;
    protected final ViewerInfoPolicy viewerInfoPolicy;

    /* renamed from: com.dropbox.core.v2.sharing.UpdateFolderPolicyArg$Builder */
    public static class Builder {
        protected AclUpdatePolicy aclUpdatePolicy;
        protected List<FolderAction> actions;
        protected LinkSettings linkSettings;
        protected MemberPolicy memberPolicy;
        protected final String sharedFolderId;
        protected SharedLinkPolicy sharedLinkPolicy;
        protected ViewerInfoPolicy viewerInfoPolicy;

        protected Builder(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
            } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
                this.sharedFolderId = str;
                this.memberPolicy = null;
                this.aclUpdatePolicy = null;
                this.viewerInfoPolicy = null;
                this.sharedLinkPolicy = null;
                this.linkSettings = null;
                this.actions = null;
            } else {
                throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
            }
        }

        public Builder withMemberPolicy(MemberPolicy memberPolicy2) {
            this.memberPolicy = memberPolicy2;
            return this;
        }

        public Builder withAclUpdatePolicy(AclUpdatePolicy aclUpdatePolicy2) {
            this.aclUpdatePolicy = aclUpdatePolicy2;
            return this;
        }

        public Builder withViewerInfoPolicy(ViewerInfoPolicy viewerInfoPolicy2) {
            this.viewerInfoPolicy = viewerInfoPolicy2;
            return this;
        }

        public Builder withSharedLinkPolicy(SharedLinkPolicy sharedLinkPolicy2) {
            this.sharedLinkPolicy = sharedLinkPolicy2;
            return this;
        }

        public Builder withLinkSettings(LinkSettings linkSettings2) {
            this.linkSettings = linkSettings2;
            return this;
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

        public UpdateFolderPolicyArg build() {
            UpdateFolderPolicyArg updateFolderPolicyArg = new UpdateFolderPolicyArg(this.sharedFolderId, this.memberPolicy, this.aclUpdatePolicy, this.viewerInfoPolicy, this.sharedLinkPolicy, this.linkSettings, this.actions);
            return updateFolderPolicyArg;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.UpdateFolderPolicyArg$Serializer */
    static class Serializer extends StructSerializer<UpdateFolderPolicyArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UpdateFolderPolicyArg updateFolderPolicyArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_folder_id");
            StoneSerializers.string().serialize(updateFolderPolicyArg.sharedFolderId, jsonGenerator);
            if (updateFolderPolicyArg.memberPolicy != null) {
                jsonGenerator.writeFieldName("member_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).serialize(updateFolderPolicyArg.memberPolicy, jsonGenerator);
            }
            if (updateFolderPolicyArg.aclUpdatePolicy != null) {
                jsonGenerator.writeFieldName("acl_update_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AclUpdatePolicy.Serializer.INSTANCE).serialize(updateFolderPolicyArg.aclUpdatePolicy, jsonGenerator);
            }
            if (updateFolderPolicyArg.viewerInfoPolicy != null) {
                jsonGenerator.writeFieldName("viewer_info_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.ViewerInfoPolicy.Serializer.INSTANCE).serialize(updateFolderPolicyArg.viewerInfoPolicy, jsonGenerator);
            }
            if (updateFolderPolicyArg.sharedLinkPolicy != null) {
                jsonGenerator.writeFieldName("shared_link_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.SharedLinkPolicy.Serializer.INSTANCE).serialize(updateFolderPolicyArg.sharedLinkPolicy, jsonGenerator);
            }
            if (updateFolderPolicyArg.linkSettings != null) {
                jsonGenerator.writeFieldName("link_settings");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(updateFolderPolicyArg.linkSettings, jsonGenerator);
            }
            if (updateFolderPolicyArg.actions != null) {
                jsonGenerator.writeFieldName("actions");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(updateFolderPolicyArg.actions, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UpdateFolderPolicyArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                MemberPolicy memberPolicy = null;
                AclUpdatePolicy aclUpdatePolicy = null;
                ViewerInfoPolicy viewerInfoPolicy = null;
                SharedLinkPolicy sharedLinkPolicy = null;
                LinkSettings linkSettings = null;
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_folder_id".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("member_policy".equals(currentName)) {
                        memberPolicy = (MemberPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("acl_update_policy".equals(currentName)) {
                        aclUpdatePolicy = (AclUpdatePolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AclUpdatePolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("viewer_info_policy".equals(currentName)) {
                        viewerInfoPolicy = (ViewerInfoPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.ViewerInfoPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("shared_link_policy".equals(currentName)) {
                        sharedLinkPolicy = (SharedLinkPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.SharedLinkPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("link_settings".equals(currentName)) {
                        linkSettings = (LinkSettings) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("actions".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    UpdateFolderPolicyArg updateFolderPolicyArg = new UpdateFolderPolicyArg(str2, memberPolicy, aclUpdatePolicy, viewerInfoPolicy, sharedLinkPolicy, linkSettings, list);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(updateFolderPolicyArg, updateFolderPolicyArg.toStringMultiline());
                    return updateFolderPolicyArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"shared_folder_id\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UpdateFolderPolicyArg(String str, MemberPolicy memberPolicy2, AclUpdatePolicy aclUpdatePolicy2, ViewerInfoPolicy viewerInfoPolicy2, SharedLinkPolicy sharedLinkPolicy2, LinkSettings linkSettings2, List<FolderAction> list) {
        if (str == null) {
            throw new IllegalArgumentException("Required value for 'sharedFolderId' is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            this.sharedFolderId = str;
            this.memberPolicy = memberPolicy2;
            this.aclUpdatePolicy = aclUpdatePolicy2;
            this.viewerInfoPolicy = viewerInfoPolicy2;
            this.sharedLinkPolicy = sharedLinkPolicy2;
            this.linkSettings = linkSettings2;
            if (list != null) {
                for (FolderAction folderAction : list) {
                    if (folderAction == null) {
                        throw new IllegalArgumentException("An item in list 'actions' is null");
                    }
                }
            }
            this.actions = list;
        } else {
            throw new IllegalArgumentException("String 'sharedFolderId' does not match pattern");
        }
    }

    public UpdateFolderPolicyArg(String str) {
        this(str, null, null, null, null, null, null);
    }

    public String getSharedFolderId() {
        return this.sharedFolderId;
    }

    public MemberPolicy getMemberPolicy() {
        return this.memberPolicy;
    }

    public AclUpdatePolicy getAclUpdatePolicy() {
        return this.aclUpdatePolicy;
    }

    public ViewerInfoPolicy getViewerInfoPolicy() {
        return this.viewerInfoPolicy;
    }

    public SharedLinkPolicy getSharedLinkPolicy() {
        return this.sharedLinkPolicy;
    }

    public LinkSettings getLinkSettings() {
        return this.linkSettings;
    }

    public List<FolderAction> getActions() {
        return this.actions;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedFolderId, this.memberPolicy, this.aclUpdatePolicy, this.viewerInfoPolicy, this.sharedLinkPolicy, this.linkSettings, this.actions});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0076, code lost:
        if (r2.equals(r5) == false) goto L_0x0079;
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
            if (r2 == 0) goto L_0x007b
            com.dropbox.core.v2.sharing.UpdateFolderPolicyArg r5 = (com.dropbox.core.p005v2.sharing.UpdateFolderPolicyArg) r5
            java.lang.String r2 = r4.sharedFolderId
            java.lang.String r3 = r5.sharedFolderId
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x0024:
            com.dropbox.core.v2.sharing.MemberPolicy r2 = r4.memberPolicy
            com.dropbox.core.v2.sharing.MemberPolicy r3 = r5.memberPolicy
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x0032:
            com.dropbox.core.v2.sharing.AclUpdatePolicy r2 = r4.aclUpdatePolicy
            com.dropbox.core.v2.sharing.AclUpdatePolicy r3 = r5.aclUpdatePolicy
            if (r2 == r3) goto L_0x0040
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x0040:
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r2 = r4.viewerInfoPolicy
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r3 = r5.viewerInfoPolicy
            if (r2 == r3) goto L_0x004e
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x004e:
            com.dropbox.core.v2.sharing.SharedLinkPolicy r2 = r4.sharedLinkPolicy
            com.dropbox.core.v2.sharing.SharedLinkPolicy r3 = r5.sharedLinkPolicy
            if (r2 == r3) goto L_0x005c
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x005c:
            com.dropbox.core.v2.sharing.LinkSettings r2 = r4.linkSettings
            com.dropbox.core.v2.sharing.LinkSettings r3 = r5.linkSettings
            if (r2 == r3) goto L_0x006a
            if (r2 == 0) goto L_0x0079
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0079
        L_0x006a:
            java.util.List<com.dropbox.core.v2.sharing.FolderAction> r2 = r4.actions
            java.util.List<com.dropbox.core.v2.sharing.FolderAction> r5 = r5.actions
            if (r2 == r5) goto L_0x007a
            if (r2 == 0) goto L_0x0079
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0079
            goto L_0x007a
        L_0x0079:
            r0 = 0
        L_0x007a:
            return r0
        L_0x007b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.UpdateFolderPolicyArg.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
