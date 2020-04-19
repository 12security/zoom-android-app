package com.dropbox.core.p005v2.paper;

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

/* renamed from: com.dropbox.core.v2.paper.SharingPolicy */
public class SharingPolicy {
    protected final SharingPublicPolicyType publicSharingPolicy;
    protected final SharingTeamPolicyType teamSharingPolicy;

    /* renamed from: com.dropbox.core.v2.paper.SharingPolicy$Builder */
    public static class Builder {
        protected SharingPublicPolicyType publicSharingPolicy = null;
        protected SharingTeamPolicyType teamSharingPolicy = null;

        protected Builder() {
        }

        public Builder withPublicSharingPolicy(SharingPublicPolicyType sharingPublicPolicyType) {
            this.publicSharingPolicy = sharingPublicPolicyType;
            return this;
        }

        public Builder withTeamSharingPolicy(SharingTeamPolicyType sharingTeamPolicyType) {
            this.teamSharingPolicy = sharingTeamPolicyType;
            return this;
        }

        public SharingPolicy build() {
            return new SharingPolicy(this.publicSharingPolicy, this.teamSharingPolicy);
        }
    }

    /* renamed from: com.dropbox.core.v2.paper.SharingPolicy$Serializer */
    static class Serializer extends StructSerializer<SharingPolicy> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharingPolicy sharingPolicy, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharingPolicy.publicSharingPolicy != null) {
                jsonGenerator.writeFieldName("public_sharing_policy");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(sharingPolicy.publicSharingPolicy, jsonGenerator);
            }
            if (sharingPolicy.teamSharingPolicy != null) {
                jsonGenerator.writeFieldName("team_sharing_policy");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(sharingPolicy.teamSharingPolicy, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharingPolicy deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            SharingPublicPolicyType sharingPublicPolicyType = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                SharingTeamPolicyType sharingTeamPolicyType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("public_sharing_policy".equals(currentName)) {
                        sharingPublicPolicyType = (SharingPublicPolicyType) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("team_sharing_policy".equals(currentName)) {
                        sharingTeamPolicyType = (SharingTeamPolicyType) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharingPolicy sharingPolicy = new SharingPolicy(sharingPublicPolicyType, sharingTeamPolicyType);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharingPolicy, sharingPolicy.toStringMultiline());
                return sharingPolicy;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharingPolicy(SharingPublicPolicyType sharingPublicPolicyType, SharingTeamPolicyType sharingTeamPolicyType) {
        this.publicSharingPolicy = sharingPublicPolicyType;
        this.teamSharingPolicy = sharingTeamPolicyType;
    }

    public SharingPolicy() {
        this(null, null);
    }

    public SharingPublicPolicyType getPublicSharingPolicy() {
        return this.publicSharingPolicy;
    }

    public SharingTeamPolicyType getTeamSharingPolicy() {
        return this.teamSharingPolicy;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.publicSharingPolicy, this.teamSharingPolicy});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (r2.equals(r5) == false) goto L_0x0035;
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
            if (r2 == 0) goto L_0x0037
            com.dropbox.core.v2.paper.SharingPolicy r5 = (com.dropbox.core.p005v2.paper.SharingPolicy) r5
            com.dropbox.core.v2.paper.SharingPublicPolicyType r2 = r4.publicSharingPolicy
            com.dropbox.core.v2.paper.SharingPublicPolicyType r3 = r5.publicSharingPolicy
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            com.dropbox.core.v2.paper.SharingTeamPolicyType r2 = r4.teamSharingPolicy
            com.dropbox.core.v2.paper.SharingTeamPolicyType r5 = r5.teamSharingPolicy
            if (r2 == r5) goto L_0x0036
            if (r2 == 0) goto L_0x0035
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r0 = 0
        L_0x0036:
            return r0
        L_0x0037:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.SharingPolicy.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
