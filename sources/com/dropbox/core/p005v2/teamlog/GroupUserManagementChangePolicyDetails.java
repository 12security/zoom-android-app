package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.p005v2.teampolicies.GroupCreation;
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

/* renamed from: com.dropbox.core.v2.teamlog.GroupUserManagementChangePolicyDetails */
public class GroupUserManagementChangePolicyDetails {
    protected final GroupCreation newValue;
    protected final GroupCreation previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.GroupUserManagementChangePolicyDetails$Serializer */
    static class Serializer extends StructSerializer<GroupUserManagementChangePolicyDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupUserManagementChangePolicyDetails groupUserManagementChangePolicyDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            com.dropbox.core.p005v2.teampolicies.GroupCreation.Serializer.INSTANCE.serialize(groupUserManagementChangePolicyDetails.newValue, jsonGenerator);
            if (groupUserManagementChangePolicyDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.GroupCreation.Serializer.INSTANCE).serialize(groupUserManagementChangePolicyDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupUserManagementChangePolicyDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            GroupCreation groupCreation = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                GroupCreation groupCreation2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        groupCreation = com.dropbox.core.p005v2.teampolicies.GroupCreation.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        groupCreation2 = (GroupCreation) StoneSerializers.nullable(com.dropbox.core.p005v2.teampolicies.GroupCreation.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (groupCreation != null) {
                    GroupUserManagementChangePolicyDetails groupUserManagementChangePolicyDetails = new GroupUserManagementChangePolicyDetails(groupCreation, groupCreation2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupUserManagementChangePolicyDetails, groupUserManagementChangePolicyDetails.toStringMultiline());
                    return groupUserManagementChangePolicyDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GroupUserManagementChangePolicyDetails(GroupCreation groupCreation, GroupCreation groupCreation2) {
        if (groupCreation != null) {
            this.newValue = groupCreation;
            this.previousValue = groupCreation2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public GroupUserManagementChangePolicyDetails(GroupCreation groupCreation) {
        this(groupCreation, null);
    }

    public GroupCreation getNewValue() {
        return this.newValue;
    }

    public GroupCreation getPreviousValue() {
        return this.previousValue;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.newValue, this.previousValue});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r2.equals(r5) == false) goto L_0x0033;
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
            if (r2 == 0) goto L_0x0035
            com.dropbox.core.v2.teamlog.GroupUserManagementChangePolicyDetails r5 = (com.dropbox.core.p005v2.teamlog.GroupUserManagementChangePolicyDetails) r5
            com.dropbox.core.v2.teampolicies.GroupCreation r2 = r4.newValue
            com.dropbox.core.v2.teampolicies.GroupCreation r3 = r5.newValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.teampolicies.GroupCreation r2 = r4.previousValue
            com.dropbox.core.v2.teampolicies.GroupCreation r5 = r5.previousValue
            if (r2 == r5) goto L_0x0034
            if (r2 == 0) goto L_0x0033
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0033
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.GroupUserManagementChangePolicyDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
