package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.p005v2.teamcommon.GroupManagementType;
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

/* renamed from: com.dropbox.core.v2.teamlog.GroupChangeManagementTypeDetails */
public class GroupChangeManagementTypeDetails {
    protected final GroupManagementType newValue;
    protected final GroupManagementType previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.GroupChangeManagementTypeDetails$Serializer */
    static class Serializer extends StructSerializer<GroupChangeManagementTypeDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupChangeManagementTypeDetails groupChangeManagementTypeDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE.serialize(groupChangeManagementTypeDetails.newValue, jsonGenerator);
            if (groupChangeManagementTypeDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE).serialize(groupChangeManagementTypeDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupChangeManagementTypeDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            GroupManagementType groupManagementType = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                GroupManagementType groupManagementType2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        groupManagementType = com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        groupManagementType2 = (GroupManagementType) StoneSerializers.nullable(com.dropbox.core.p005v2.teamcommon.GroupManagementType.Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (groupManagementType != null) {
                    GroupChangeManagementTypeDetails groupChangeManagementTypeDetails = new GroupChangeManagementTypeDetails(groupManagementType, groupManagementType2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupChangeManagementTypeDetails, groupChangeManagementTypeDetails.toStringMultiline());
                    return groupChangeManagementTypeDetails;
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

    public GroupChangeManagementTypeDetails(GroupManagementType groupManagementType, GroupManagementType groupManagementType2) {
        if (groupManagementType != null) {
            this.newValue = groupManagementType;
            this.previousValue = groupManagementType2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public GroupChangeManagementTypeDetails(GroupManagementType groupManagementType) {
        this(groupManagementType, null);
    }

    public GroupManagementType getNewValue() {
        return this.newValue;
    }

    public GroupManagementType getPreviousValue() {
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
            com.dropbox.core.v2.teamlog.GroupChangeManagementTypeDetails r5 = (com.dropbox.core.p005v2.teamlog.GroupChangeManagementTypeDetails) r5
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r4.newValue
            com.dropbox.core.v2.teamcommon.GroupManagementType r3 = r5.newValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.teamcommon.GroupManagementType r2 = r4.previousValue
            com.dropbox.core.v2.teamcommon.GroupManagementType r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.GroupChangeManagementTypeDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
