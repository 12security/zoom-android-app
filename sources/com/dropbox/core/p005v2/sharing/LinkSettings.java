package com.dropbox.core.p005v2.sharing;

import com.box.androidsdk.content.models.BoxSharedLink;
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

/* renamed from: com.dropbox.core.v2.sharing.LinkSettings */
public class LinkSettings {
    protected final AccessLevel accessLevel;
    protected final LinkAudience audience;
    protected final LinkExpiry expiry;
    protected final LinkPassword password;

    /* renamed from: com.dropbox.core.v2.sharing.LinkSettings$Builder */
    public static class Builder {
        protected AccessLevel accessLevel = null;
        protected LinkAudience audience = null;
        protected LinkExpiry expiry = null;
        protected LinkPassword password = null;

        protected Builder() {
        }

        public Builder withAccessLevel(AccessLevel accessLevel2) {
            this.accessLevel = accessLevel2;
            return this;
        }

        public Builder withAudience(LinkAudience linkAudience) {
            this.audience = linkAudience;
            return this;
        }

        public Builder withExpiry(LinkExpiry linkExpiry) {
            this.expiry = linkExpiry;
            return this;
        }

        public Builder withPassword(LinkPassword linkPassword) {
            this.password = linkPassword;
            return this;
        }

        public LinkSettings build() {
            return new LinkSettings(this.accessLevel, this.audience, this.expiry, this.password);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.LinkSettings$Serializer */
    static class Serializer extends StructSerializer<LinkSettings> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(LinkSettings linkSettings, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (linkSettings.accessLevel != null) {
                jsonGenerator.writeFieldName("access_level");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).serialize(linkSettings.accessLevel, jsonGenerator);
            }
            if (linkSettings.audience != null) {
                jsonGenerator.writeFieldName("audience");
                StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE).serialize(linkSettings.audience, jsonGenerator);
            }
            if (linkSettings.expiry != null) {
                jsonGenerator.writeFieldName("expiry");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(linkSettings.expiry, jsonGenerator);
            }
            if (linkSettings.password != null) {
                jsonGenerator.writeFieldName(BoxSharedLink.FIELD_PASSWORD);
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(linkSettings.password, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public LinkSettings deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            AccessLevel accessLevel = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                LinkAudience linkAudience = null;
                LinkExpiry linkExpiry = null;
                LinkPassword linkPassword = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("access_level".equals(currentName)) {
                        accessLevel = (AccessLevel) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("audience".equals(currentName)) {
                        linkAudience = (LinkAudience) StoneSerializers.nullable(com.dropbox.core.p005v2.sharing.LinkAudience.Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("expiry".equals(currentName)) {
                        linkExpiry = (LinkExpiry) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if (BoxSharedLink.FIELD_PASSWORD.equals(currentName)) {
                        linkPassword = (LinkPassword) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                LinkSettings linkSettings = new LinkSettings(accessLevel, linkAudience, linkExpiry, linkPassword);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(linkSettings, linkSettings.toStringMultiline());
                return linkSettings;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public LinkSettings(AccessLevel accessLevel2, LinkAudience linkAudience, LinkExpiry linkExpiry, LinkPassword linkPassword) {
        this.accessLevel = accessLevel2;
        this.audience = linkAudience;
        this.expiry = linkExpiry;
        this.password = linkPassword;
    }

    public LinkSettings() {
        this(null, null, null, null);
    }

    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    public LinkAudience getAudience() {
        return this.audience;
    }

    public LinkExpiry getExpiry() {
        return this.expiry;
    }

    public LinkPassword getPassword() {
        return this.password;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.accessLevel, this.audience, this.expiry, this.password});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004e, code lost:
        if (r2.equals(r5) == false) goto L_0x0051;
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
            if (r2 == 0) goto L_0x0053
            com.dropbox.core.v2.sharing.LinkSettings r5 = (com.dropbox.core.p005v2.sharing.LinkSettings) r5
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.accessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.accessLevel
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0051
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0026:
            com.dropbox.core.v2.sharing.LinkAudience r2 = r4.audience
            com.dropbox.core.v2.sharing.LinkAudience r3 = r5.audience
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0051
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0034:
            com.dropbox.core.v2.sharing.LinkExpiry r2 = r4.expiry
            com.dropbox.core.v2.sharing.LinkExpiry r3 = r5.expiry
            if (r2 == r3) goto L_0x0042
            if (r2 == 0) goto L_0x0051
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0051
        L_0x0042:
            com.dropbox.core.v2.sharing.LinkPassword r2 = r4.password
            com.dropbox.core.v2.sharing.LinkPassword r5 = r5.password
            if (r2 == r5) goto L_0x0052
            if (r2 == 0) goto L_0x0051
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0051
            goto L_0x0052
        L_0x0051:
            r0 = 0
        L_0x0052:
            return r0
        L_0x0053:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.LinkSettings.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
