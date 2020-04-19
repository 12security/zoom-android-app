package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.GroupLogInfo */
public class GroupLogInfo {
    protected final String displayName;
    protected final String externalId;
    protected final String groupId;

    /* renamed from: com.dropbox.core.v2.teamlog.GroupLogInfo$Builder */
    public static class Builder {
        protected final String displayName;
        protected String externalId;
        protected String groupId;

        protected Builder(String str) {
            if (str != null) {
                this.displayName = str;
                this.groupId = null;
                this.externalId = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'displayName' is null");
        }

        public Builder withGroupId(String str) {
            this.groupId = str;
            return this;
        }

        public Builder withExternalId(String str) {
            this.externalId = str;
            return this;
        }

        public GroupLogInfo build() {
            return new GroupLogInfo(this.displayName, this.groupId, this.externalId);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.GroupLogInfo$Serializer */
    static class Serializer extends StructSerializer<GroupLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupLogInfo groupLogInfo, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("display_name");
            StoneSerializers.string().serialize(groupLogInfo.displayName, jsonGenerator);
            if (groupLogInfo.groupId != null) {
                jsonGenerator.writeFieldName(Param.GROUP_ID);
                StoneSerializers.nullable(StoneSerializers.string()).serialize(groupLogInfo.groupId, jsonGenerator);
            }
            if (groupLogInfo.externalId != null) {
                jsonGenerator.writeFieldName("external_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(groupLogInfo.externalId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GroupLogInfo deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("display_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if (Param.GROUP_ID.equals(currentName)) {
                        str3 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if ("external_id".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 != null) {
                    GroupLogInfo groupLogInfo = new GroupLogInfo(str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(groupLogInfo, groupLogInfo.toStringMultiline());
                    return groupLogInfo;
                }
                throw new JsonParseException(jsonParser, "Required field \"display_name\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GroupLogInfo(String str, String str2, String str3) {
        this.groupId = str2;
        if (str != null) {
            this.displayName = str;
            this.externalId = str3;
            return;
        }
        throw new IllegalArgumentException("Required value for 'displayName' is null");
    }

    public GroupLogInfo(String str) {
        this(str, null, null);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getExternalId() {
        return this.externalId;
    }

    public static Builder newBuilder(String str) {
        return new Builder(str);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.groupId, this.displayName, this.externalId});
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
            com.dropbox.core.v2.teamlog.GroupLogInfo r5 = (com.dropbox.core.p005v2.teamlog.GroupLogInfo) r5
            java.lang.String r2 = r4.displayName
            java.lang.String r3 = r5.displayName
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            java.lang.String r2 = r4.groupId
            java.lang.String r3 = r5.groupId
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            java.lang.String r2 = r4.externalId
            java.lang.String r5 = r5.externalId
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.GroupLogInfo.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
