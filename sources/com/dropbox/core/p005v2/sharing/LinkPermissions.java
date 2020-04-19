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

/* renamed from: com.dropbox.core.v2.sharing.LinkPermissions */
public class LinkPermissions {
    protected final boolean canRevoke;
    protected final RequestedVisibility requestedVisibility;
    protected final ResolvedVisibility resolvedVisibility;
    protected final SharedLinkAccessFailureReason revokeFailureReason;

    /* renamed from: com.dropbox.core.v2.sharing.LinkPermissions$Builder */
    public static class Builder {
        protected final boolean canRevoke;
        protected RequestedVisibility requestedVisibility = null;
        protected ResolvedVisibility resolvedVisibility = null;
        protected SharedLinkAccessFailureReason revokeFailureReason = null;

        protected Builder(boolean z) {
            this.canRevoke = z;
        }

        public Builder withResolvedVisibility(ResolvedVisibility resolvedVisibility2) {
            this.resolvedVisibility = resolvedVisibility2;
            return this;
        }

        public Builder withRequestedVisibility(RequestedVisibility requestedVisibility2) {
            this.requestedVisibility = requestedVisibility2;
            return this;
        }

        public Builder withRevokeFailureReason(SharedLinkAccessFailureReason sharedLinkAccessFailureReason) {
            this.revokeFailureReason = sharedLinkAccessFailureReason;
            return this;
        }

        public LinkPermissions build() {
            return new LinkPermissions(this.canRevoke, this.resolvedVisibility, this.requestedVisibility, this.revokeFailureReason);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.LinkPermissions$Serializer */
    static class Serializer extends StructSerializer<LinkPermissions> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(LinkPermissions linkPermissions, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("can_revoke");
            StoneSerializers.boolean_().serialize(Boolean.valueOf(linkPermissions.canRevoke), jsonGenerator);
            if (linkPermissions.resolvedVisibility != null) {
                jsonGenerator.writeFieldName("resolved_visibility");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(linkPermissions.resolvedVisibility, jsonGenerator);
            }
            if (linkPermissions.requestedVisibility != null) {
                jsonGenerator.writeFieldName("requested_visibility");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(linkPermissions.requestedVisibility, jsonGenerator);
            }
            if (linkPermissions.revokeFailureReason != null) {
                jsonGenerator.writeFieldName("revoke_failure_reason");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(linkPermissions.revokeFailureReason, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public LinkPermissions deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                ResolvedVisibility resolvedVisibility = null;
                RequestedVisibility requestedVisibility = null;
                SharedLinkAccessFailureReason sharedLinkAccessFailureReason = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("can_revoke".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.boolean_().deserialize(jsonParser);
                    } else if ("resolved_visibility".equals(currentName)) {
                        resolvedVisibility = (ResolvedVisibility) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("requested_visibility".equals(currentName)) {
                        requestedVisibility = (RequestedVisibility) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("revoke_failure_reason".equals(currentName)) {
                        sharedLinkAccessFailureReason = (SharedLinkAccessFailureReason) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (bool != null) {
                    LinkPermissions linkPermissions = new LinkPermissions(bool.booleanValue(), resolvedVisibility, requestedVisibility, sharedLinkAccessFailureReason);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(linkPermissions, linkPermissions.toStringMultiline());
                    return linkPermissions;
                }
                throw new JsonParseException(jsonParser, "Required field \"can_revoke\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public LinkPermissions(boolean z, ResolvedVisibility resolvedVisibility2, RequestedVisibility requestedVisibility2, SharedLinkAccessFailureReason sharedLinkAccessFailureReason) {
        this.resolvedVisibility = resolvedVisibility2;
        this.requestedVisibility = requestedVisibility2;
        this.canRevoke = z;
        this.revokeFailureReason = sharedLinkAccessFailureReason;
    }

    public LinkPermissions(boolean z) {
        this(z, null, null, null);
    }

    public boolean getCanRevoke() {
        return this.canRevoke;
    }

    public ResolvedVisibility getResolvedVisibility() {
        return this.resolvedVisibility;
    }

    public RequestedVisibility getRequestedVisibility() {
        return this.requestedVisibility;
    }

    public SharedLinkAccessFailureReason getRevokeFailureReason() {
        return this.revokeFailureReason;
    }

    public static Builder newBuilder(boolean z) {
        return new Builder(z);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.resolvedVisibility, this.requestedVisibility, Boolean.valueOf(this.canRevoke), this.revokeFailureReason});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0046, code lost:
        if (r2.equals(r5) == false) goto L_0x0049;
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
            if (r2 == 0) goto L_0x004b
            com.dropbox.core.v2.sharing.LinkPermissions r5 = (com.dropbox.core.p005v2.sharing.LinkPermissions) r5
            boolean r2 = r4.canRevoke
            boolean r3 = r5.canRevoke
            if (r2 != r3) goto L_0x0049
            com.dropbox.core.v2.sharing.ResolvedVisibility r2 = r4.resolvedVisibility
            com.dropbox.core.v2.sharing.ResolvedVisibility r3 = r5.resolvedVisibility
            if (r2 == r3) goto L_0x002c
            if (r2 == 0) goto L_0x0049
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x002c:
            com.dropbox.core.v2.sharing.RequestedVisibility r2 = r4.requestedVisibility
            com.dropbox.core.v2.sharing.RequestedVisibility r3 = r5.requestedVisibility
            if (r2 == r3) goto L_0x003a
            if (r2 == 0) goto L_0x0049
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0049
        L_0x003a:
            com.dropbox.core.v2.sharing.SharedLinkAccessFailureReason r2 = r4.revokeFailureReason
            com.dropbox.core.v2.sharing.SharedLinkAccessFailureReason r5 = r5.revokeFailureReason
            if (r2 == r5) goto L_0x004a
            if (r2 == 0) goto L_0x0049
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0049
            goto L_0x004a
        L_0x0049:
            r0 = 0
        L_0x004a:
            return r0
        L_0x004b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.LinkPermissions.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
