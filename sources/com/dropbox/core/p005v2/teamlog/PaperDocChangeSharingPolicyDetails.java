package com.dropbox.core.p005v2.teamlog;

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

/* renamed from: com.dropbox.core.v2.teamlog.PaperDocChangeSharingPolicyDetails */
public class PaperDocChangeSharingPolicyDetails {
    protected final String eventUuid;
    protected final String publicSharingPolicy;
    protected final String teamSharingPolicy;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperDocChangeSharingPolicyDetails$Builder */
    public static class Builder {
        protected final String eventUuid;
        protected String publicSharingPolicy;
        protected String teamSharingPolicy;

        protected Builder(String str) {
            if (str != null) {
                this.eventUuid = str;
                this.publicSharingPolicy = null;
                this.teamSharingPolicy = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'eventUuid' is null");
        }

        public Builder withPublicSharingPolicy(String str) {
            this.publicSharingPolicy = str;
            return this;
        }

        public Builder withTeamSharingPolicy(String str) {
            this.teamSharingPolicy = str;
            return this;
        }

        public PaperDocChangeSharingPolicyDetails build() {
            return new PaperDocChangeSharingPolicyDetails(this.eventUuid, this.publicSharingPolicy, this.teamSharingPolicy);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.PaperDocChangeSharingPolicyDetails$Serializer */
    static class Serializer extends StructSerializer<PaperDocChangeSharingPolicyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocChangeSharingPolicyDetails paperDocChangeSharingPolicyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(paperDocChangeSharingPolicyDetails.eventUuid, jsonGenerator);
            if (paperDocChangeSharingPolicyDetails.publicSharingPolicy != null) {
                jsonGenerator.writeFieldName("public_sharing_policy");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(paperDocChangeSharingPolicyDetails.publicSharingPolicy, jsonGenerator);
            }
            if (paperDocChangeSharingPolicyDetails.teamSharingPolicy != null) {
                jsonGenerator.writeFieldName("team_sharing_policy");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(paperDocChangeSharingPolicyDetails.teamSharingPolicy, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocChangeSharingPolicyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("event_uuid".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("public_sharing_policy".equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("team_sharing_policy".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    PaperDocChangeSharingPolicyDetails paperDocChangeSharingPolicyDetails = new PaperDocChangeSharingPolicyDetails(str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocChangeSharingPolicyDetails, paperDocChangeSharingPolicyDetails.toStringMultiline());
                    return paperDocChangeSharingPolicyDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"event_uuid\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public PaperDocChangeSharingPolicyDetails(String str, String str2, String str3) {
        if (str != null) {
            this.eventUuid = str;
            this.publicSharingPolicy = str2;
            this.teamSharingPolicy = str3;
            return;
        }
        throw new IllegalArgumentException("Required value for 'eventUuid' is null");
    }

    public PaperDocChangeSharingPolicyDetails(String str) {
        this(str, null, null);
    }

    public String getEventUuid() {
        return this.eventUuid;
    }

    public String getPublicSharingPolicy() {
        return this.publicSharingPolicy;
    }

    public String getTeamSharingPolicy() {
        return this.teamSharingPolicy;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.eventUuid, this.publicSharingPolicy, this.teamSharingPolicy});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2.equals(r5) == false) goto L_0x0041;
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
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.teamlog.PaperDocChangeSharingPolicyDetails r5 = (com.dropbox.core.p005v2.teamlog.PaperDocChangeSharingPolicyDetails) r5
            java.lang.String r2 = r4.eventUuid
            java.lang.String r3 = r5.eventUuid
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            java.lang.String r2 = r4.publicSharingPolicy
            java.lang.String r3 = r5.publicSharingPolicy
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            java.lang.String r2 = r4.teamSharingPolicy
            java.lang.String r5 = r5.teamSharingPolicy
            if (r2 == r5) goto L_0x0042
            if (r2 == 0) goto L_0x0041
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.PaperDocChangeSharingPolicyDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
