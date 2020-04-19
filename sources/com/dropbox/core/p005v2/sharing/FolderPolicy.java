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

/* renamed from: com.dropbox.core.v2.sharing.FolderPolicy */
public class FolderPolicy {
    protected final AclUpdatePolicy aclUpdatePolicy;
    protected final MemberPolicy memberPolicy;
    protected final MemberPolicy resolvedMemberPolicy;
    protected final SharedLinkPolicy sharedLinkPolicy;
    protected final ViewerInfoPolicy viewerInfoPolicy;

    /* renamed from: com.dropbox.core.v2.sharing.FolderPolicy$Builder */
    public static class Builder {
        protected final AclUpdatePolicy aclUpdatePolicy;
        protected MemberPolicy memberPolicy;
        protected MemberPolicy resolvedMemberPolicy;
        protected final SharedLinkPolicy sharedLinkPolicy;
        protected ViewerInfoPolicy viewerInfoPolicy;

        protected Builder(AclUpdatePolicy aclUpdatePolicy2, SharedLinkPolicy sharedLinkPolicy2) {
            if (aclUpdatePolicy2 != null) {
                this.aclUpdatePolicy = aclUpdatePolicy2;
                if (sharedLinkPolicy2 != null) {
                    this.sharedLinkPolicy = sharedLinkPolicy2;
                    this.memberPolicy = null;
                    this.resolvedMemberPolicy = null;
                    this.viewerInfoPolicy = null;
                    return;
                }
                throw new IllegalArgumentException("Required value for 'sharedLinkPolicy' is null");
            }
            throw new IllegalArgumentException("Required value for 'aclUpdatePolicy' is null");
        }

        public Builder withMemberPolicy(MemberPolicy memberPolicy2) {
            this.memberPolicy = memberPolicy2;
            return this;
        }

        public Builder withResolvedMemberPolicy(MemberPolicy memberPolicy2) {
            this.resolvedMemberPolicy = memberPolicy2;
            return this;
        }

        public Builder withViewerInfoPolicy(ViewerInfoPolicy viewerInfoPolicy2) {
            this.viewerInfoPolicy = viewerInfoPolicy2;
            return this;
        }

        public FolderPolicy build() {
            FolderPolicy folderPolicy = new FolderPolicy(this.aclUpdatePolicy, this.sharedLinkPolicy, this.memberPolicy, this.resolvedMemberPolicy, this.viewerInfoPolicy);
            return folderPolicy;
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.FolderPolicy$Serializer */
    static class Serializer extends StructSerializer<FolderPolicy> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FolderPolicy folderPolicy, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("acl_update_policy");
            com.dropbox.core.p005v2.sharing.AclUpdatePolicy.Serializer.INSTANCE.serialize(folderPolicy.aclUpdatePolicy, jsonGenerator);
            jsonGenerator.writeFieldName("shared_link_policy");
            com.dropbox.core.p005v2.sharing.SharedLinkPolicy.Serializer.INSTANCE.serialize(folderPolicy.sharedLinkPolicy, jsonGenerator);
            if (folderPolicy.memberPolicy != null) {
                jsonGenerator.writeFieldName("member_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).serialize(folderPolicy.memberPolicy, jsonGenerator);
            }
            if (folderPolicy.resolvedMemberPolicy != null) {
                jsonGenerator.writeFieldName("resolved_member_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).serialize(folderPolicy.resolvedMemberPolicy, jsonGenerator);
            }
            if (folderPolicy.viewerInfoPolicy != null) {
                jsonGenerator.writeFieldName("viewer_info_policy");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.ViewerInfoPolicy.Serializer.INSTANCE).serialize(folderPolicy.viewerInfoPolicy, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FolderPolicy deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AclUpdatePolicy aclUpdatePolicy = null;
                SharedLinkPolicy sharedLinkPolicy = null;
                MemberPolicy memberPolicy = null;
                MemberPolicy memberPolicy2 = null;
                ViewerInfoPolicy viewerInfoPolicy = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("acl_update_policy".equals(currentName)) {
                        aclUpdatePolicy = com.dropbox.core.p005v2.sharing.AclUpdatePolicy.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("shared_link_policy".equals(currentName)) {
                        sharedLinkPolicy = com.dropbox.core.p005v2.sharing.SharedLinkPolicy.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("member_policy".equals(currentName)) {
                        memberPolicy = (MemberPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("resolved_member_policy".equals(currentName)) {
                        memberPolicy2 = (MemberPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.MemberPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("viewer_info_policy".equals(currentName)) {
                        viewerInfoPolicy = (ViewerInfoPolicy) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.ViewerInfoPolicy.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (aclUpdatePolicy == null) {
                    throw new JsonParseException(jsonParser, "Required field \"acl_update_policy\" missing.");
                } else if (sharedLinkPolicy != null) {
                    FolderPolicy folderPolicy = new FolderPolicy(aclUpdatePolicy, sharedLinkPolicy, memberPolicy, memberPolicy2, viewerInfoPolicy);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(folderPolicy, folderPolicy.toStringMultiline());
                    return folderPolicy;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"shared_link_policy\" missing.");
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

    public FolderPolicy(AclUpdatePolicy aclUpdatePolicy2, SharedLinkPolicy sharedLinkPolicy2, MemberPolicy memberPolicy2, MemberPolicy memberPolicy3, ViewerInfoPolicy viewerInfoPolicy2) {
        this.memberPolicy = memberPolicy2;
        this.resolvedMemberPolicy = memberPolicy3;
        if (aclUpdatePolicy2 != null) {
            this.aclUpdatePolicy = aclUpdatePolicy2;
            if (sharedLinkPolicy2 != null) {
                this.sharedLinkPolicy = sharedLinkPolicy2;
                this.viewerInfoPolicy = viewerInfoPolicy2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'sharedLinkPolicy' is null");
        }
        throw new IllegalArgumentException("Required value for 'aclUpdatePolicy' is null");
    }

    public FolderPolicy(AclUpdatePolicy aclUpdatePolicy2, SharedLinkPolicy sharedLinkPolicy2) {
        this(aclUpdatePolicy2, sharedLinkPolicy2, null, null, null);
    }

    public AclUpdatePolicy getAclUpdatePolicy() {
        return this.aclUpdatePolicy;
    }

    public SharedLinkPolicy getSharedLinkPolicy() {
        return this.sharedLinkPolicy;
    }

    public MemberPolicy getMemberPolicy() {
        return this.memberPolicy;
    }

    public MemberPolicy getResolvedMemberPolicy() {
        return this.resolvedMemberPolicy;
    }

    public ViewerInfoPolicy getViewerInfoPolicy() {
        return this.viewerInfoPolicy;
    }

    public static Builder newBuilder(AclUpdatePolicy aclUpdatePolicy2, SharedLinkPolicy sharedLinkPolicy2) {
        return new Builder(aclUpdatePolicy2, sharedLinkPolicy2);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.memberPolicy, this.resolvedMemberPolicy, this.aclUpdatePolicy, this.sharedLinkPolicy, this.viewerInfoPolicy});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0058, code lost:
        if (r2.equals(r5) == false) goto L_0x005b;
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
            if (r2 == 0) goto L_0x005d
            com.dropbox.core.v2.sharing.FolderPolicy r5 = (com.dropbox.core.p005v2.sharing.FolderPolicy) r5
            com.dropbox.core.v2.sharing.AclUpdatePolicy r2 = r4.aclUpdatePolicy
            com.dropbox.core.v2.sharing.AclUpdatePolicy r3 = r5.aclUpdatePolicy
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x0024:
            com.dropbox.core.v2.sharing.SharedLinkPolicy r2 = r4.sharedLinkPolicy
            com.dropbox.core.v2.sharing.SharedLinkPolicy r3 = r5.sharedLinkPolicy
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x0030:
            com.dropbox.core.v2.sharing.MemberPolicy r2 = r4.memberPolicy
            com.dropbox.core.v2.sharing.MemberPolicy r3 = r5.memberPolicy
            if (r2 == r3) goto L_0x003e
            if (r2 == 0) goto L_0x005b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x003e:
            com.dropbox.core.v2.sharing.MemberPolicy r2 = r4.resolvedMemberPolicy
            com.dropbox.core.v2.sharing.MemberPolicy r3 = r5.resolvedMemberPolicy
            if (r2 == r3) goto L_0x004c
            if (r2 == 0) goto L_0x005b
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x005b
        L_0x004c:
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r2 = r4.viewerInfoPolicy
            com.dropbox.core.v2.sharing.ViewerInfoPolicy r5 = r5.viewerInfoPolicy
            if (r2 == r5) goto L_0x005c
            if (r2 == 0) goto L_0x005b
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x005b
            goto L_0x005c
        L_0x005b:
            r0 = 0
        L_0x005c:
            return r0
        L_0x005d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.FolderPolicy.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
