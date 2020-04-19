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

/* renamed from: com.dropbox.core.v2.teamlog.GroupJoinPolicyUpdatedDetails */
public class GroupJoinPolicyUpdatedDetails {
    protected final Boolean isCompanyManaged;
    protected final GroupJoinPolicy joinPolicy;

    /* renamed from: com.dropbox.core.v2.teamlog.GroupJoinPolicyUpdatedDetails$Builder */
    public static class Builder {
        protected Boolean isCompanyManaged = null;
        protected GroupJoinPolicy joinPolicy = null;

        protected Builder() {
        }

        public Builder withIsCompanyManaged(Boolean bool) {
            this.isCompanyManaged = bool;
            return this;
        }

        public Builder withJoinPolicy(GroupJoinPolicy groupJoinPolicy) {
            this.joinPolicy = groupJoinPolicy;
            return this;
        }

        public GroupJoinPolicyUpdatedDetails build() {
            return new GroupJoinPolicyUpdatedDetails(this.isCompanyManaged, this.joinPolicy);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.GroupJoinPolicyUpdatedDetails$Serializer */
    static class Serializer extends StructSerializer<GroupJoinPolicyUpdatedDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupJoinPolicyUpdatedDetails groupJoinPolicyUpdatedDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (groupJoinPolicyUpdatedDetails.isCompanyManaged != null) {
                jsonGenerator.writeFieldName("is_company_managed");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(groupJoinPolicyUpdatedDetails.isCompanyManaged, jsonGenerator);
            }
            if (groupJoinPolicyUpdatedDetails.joinPolicy != null) {
                jsonGenerator.writeFieldName("join_policy");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(groupJoinPolicyUpdatedDetails.joinPolicy, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupJoinPolicyUpdatedDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Boolean bool = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                GroupJoinPolicy groupJoinPolicy = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("is_company_managed".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else if ("join_policy".equals(currentName)) {
                        groupJoinPolicy = (GroupJoinPolicy) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                GroupJoinPolicyUpdatedDetails groupJoinPolicyUpdatedDetails = new GroupJoinPolicyUpdatedDetails(bool, groupJoinPolicy);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(groupJoinPolicyUpdatedDetails, groupJoinPolicyUpdatedDetails.toStringMultiline());
                return groupJoinPolicyUpdatedDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GroupJoinPolicyUpdatedDetails(Boolean bool, GroupJoinPolicy groupJoinPolicy) {
        this.isCompanyManaged = bool;
        this.joinPolicy = groupJoinPolicy;
    }

    public GroupJoinPolicyUpdatedDetails() {
        this(null, null);
    }

    public Boolean getIsCompanyManaged() {
        return this.isCompanyManaged;
    }

    public GroupJoinPolicy getJoinPolicy() {
        return this.joinPolicy;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.isCompanyManaged, this.joinPolicy});
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
            com.dropbox.core.v2.teamlog.GroupJoinPolicyUpdatedDetails r5 = (com.dropbox.core.p005v2.teamlog.GroupJoinPolicyUpdatedDetails) r5
            java.lang.Boolean r2 = r4.isCompanyManaged
            java.lang.Boolean r3 = r5.isCompanyManaged
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            com.dropbox.core.v2.teamlog.GroupJoinPolicy r2 = r4.joinPolicy
            com.dropbox.core.v2.teamlog.GroupJoinPolicy r5 = r5.joinPolicy
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.GroupJoinPolicyUpdatedDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
