package com.dropbox.core.p005v2.team;

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

/* renamed from: com.dropbox.core.v2.team.UserCustomQuotaResult */
public class UserCustomQuotaResult {
    protected final Long quotaGb;
    protected final UserSelectorArg user;

    /* renamed from: com.dropbox.core.v2.team.UserCustomQuotaResult$Serializer */
    static class Serializer extends StructSerializer<UserCustomQuotaResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserCustomQuotaResult userCustomQuotaResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(userCustomQuotaResult.user, jsonGenerator);
            if (userCustomQuotaResult.quotaGb != null) {
                jsonGenerator.writeFieldName("quota_gb");
                StoneSerializers.nullable(StoneSerializers.uInt32()).serialize(userCustomQuotaResult.quotaGb, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserCustomQuotaResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserSelectorArg userSelectorArg = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Long l = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("user".equals(currentName)) {
                        userSelectorArg = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("quota_gb".equals(currentName)) {
                        l = (Long) StoneSerializers.nullable(StoneSerializers.uInt32()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userSelectorArg != null) {
                    UserCustomQuotaResult userCustomQuotaResult = new UserCustomQuotaResult(userSelectorArg, l);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(userCustomQuotaResult, userCustomQuotaResult.toStringMultiline());
                    return userCustomQuotaResult;
                }
                throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public UserCustomQuotaResult(UserSelectorArg userSelectorArg, Long l) {
        if (userSelectorArg != null) {
            this.user = userSelectorArg;
            if (l == null || l.longValue() >= 15) {
                this.quotaGb = l;
                return;
            }
            throw new IllegalArgumentException("Number 'quotaGb' is smaller than 15L");
        }
        throw new IllegalArgumentException("Required value for 'user' is null");
    }

    public UserCustomQuotaResult(UserSelectorArg userSelectorArg) {
        this(userSelectorArg, null);
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public Long getQuotaGb() {
        return this.quotaGb;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.user, this.quotaGb});
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
            com.dropbox.core.v2.team.UserCustomQuotaResult r5 = (com.dropbox.core.p005v2.team.UserCustomQuotaResult) r5
            com.dropbox.core.v2.team.UserSelectorArg r2 = r4.user
            com.dropbox.core.v2.team.UserSelectorArg r3 = r5.user
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            java.lang.Long r2 = r4.quotaGb
            java.lang.Long r5 = r5.quotaGb
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.team.UserCustomQuotaResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
