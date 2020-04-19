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
import java.util.List;

/* renamed from: com.dropbox.core.v2.teamlog.SharedLinkShareDetails */
public class SharedLinkShareDetails {
    protected final List<ExternalUserLogInfo> externalUsers;
    protected final UserLogInfo sharedLinkOwner;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedLinkShareDetails$Builder */
    public static class Builder {
        protected List<ExternalUserLogInfo> externalUsers = null;
        protected UserLogInfo sharedLinkOwner = null;

        protected Builder() {
        }

        public Builder withSharedLinkOwner(UserLogInfo userLogInfo) {
            this.sharedLinkOwner = userLogInfo;
            return this;
        }

        public Builder withExternalUsers(List<ExternalUserLogInfo> list) {
            if (list != null) {
                for (ExternalUserLogInfo externalUserLogInfo : list) {
                    if (externalUserLogInfo == null) {
                        throw new IllegalArgumentException("An item in list 'externalUsers' is null");
                    }
                }
            }
            this.externalUsers = list;
            return this;
        }

        public SharedLinkShareDetails build() {
            return new SharedLinkShareDetails(this.sharedLinkOwner, this.externalUsers);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.SharedLinkShareDetails$Serializer */
    static class Serializer extends StructSerializer<SharedLinkShareDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedLinkShareDetails sharedLinkShareDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedLinkShareDetails.sharedLinkOwner != null) {
                jsonGenerator.writeFieldName("shared_link_owner");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedLinkShareDetails.sharedLinkOwner, jsonGenerator);
            }
            if (sharedLinkShareDetails.externalUsers != null) {
                jsonGenerator.writeFieldName("external_users");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(sharedLinkShareDetails.externalUsers, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedLinkShareDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserLogInfo userLogInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_link_owner".equals(currentName)) {
                        userLogInfo = (UserLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("external_users".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedLinkShareDetails sharedLinkShareDetails = new SharedLinkShareDetails(userLogInfo, list);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedLinkShareDetails, sharedLinkShareDetails.toStringMultiline());
                return sharedLinkShareDetails;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedLinkShareDetails(UserLogInfo userLogInfo, List<ExternalUserLogInfo> list) {
        this.sharedLinkOwner = userLogInfo;
        if (list != null) {
            for (ExternalUserLogInfo externalUserLogInfo : list) {
                if (externalUserLogInfo == null) {
                    throw new IllegalArgumentException("An item in list 'externalUsers' is null");
                }
            }
        }
        this.externalUsers = list;
    }

    public SharedLinkShareDetails() {
        this(null, null);
    }

    public UserLogInfo getSharedLinkOwner() {
        return this.sharedLinkOwner;
    }

    public List<ExternalUserLogInfo> getExternalUsers() {
        return this.externalUsers;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedLinkOwner, this.externalUsers});
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
            com.dropbox.core.v2.teamlog.SharedLinkShareDetails r5 = (com.dropbox.core.p005v2.teamlog.SharedLinkShareDetails) r5
            com.dropbox.core.v2.teamlog.UserLogInfo r2 = r4.sharedLinkOwner
            com.dropbox.core.v2.teamlog.UserLogInfo r3 = r5.sharedLinkOwner
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.util.List<com.dropbox.core.v2.teamlog.ExternalUserLogInfo> r2 = r4.externalUsers
            java.util.List<com.dropbox.core.v2.teamlog.ExternalUserLogInfo> r5 = r5.externalUsers
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SharedLinkShareDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
