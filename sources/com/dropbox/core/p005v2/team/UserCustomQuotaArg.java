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

/* renamed from: com.dropbox.core.v2.team.UserCustomQuotaArg */
public class UserCustomQuotaArg {
    protected final long quotaGb;
    protected final UserSelectorArg user;

    /* renamed from: com.dropbox.core.v2.team.UserCustomQuotaArg$Serializer */
    static class Serializer extends StructSerializer<UserCustomQuotaArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(UserCustomQuotaArg userCustomQuotaArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("user");
            Serializer.INSTANCE.serialize(userCustomQuotaArg.user, jsonGenerator);
            jsonGenerator.writeFieldName("quota_gb");
            StoneSerializers.uInt32().serialize(Long.valueOf(userCustomQuotaArg.quotaGb), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public UserCustomQuotaArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                        l = (Long) StoneSerializers.uInt32().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userSelectorArg == null) {
                    throw new JsonParseException(jsonParser, "Required field \"user\" missing.");
                } else if (l != null) {
                    UserCustomQuotaArg userCustomQuotaArg = new UserCustomQuotaArg(userSelectorArg, l.longValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(userCustomQuotaArg, userCustomQuotaArg.toStringMultiline());
                    return userCustomQuotaArg;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"quota_gb\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public UserCustomQuotaArg(UserSelectorArg userSelectorArg, long j) {
        if (userSelectorArg != null) {
            this.user = userSelectorArg;
            if (j >= 15) {
                this.quotaGb = j;
                return;
            }
            throw new IllegalArgumentException("Number 'quotaGb' is smaller than 15L");
        }
        throw new IllegalArgumentException("Required value for 'user' is null");
    }

    public UserSelectorArg getUser() {
        return this.user;
    }

    public long getQuotaGb() {
        return this.quotaGb;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.user, Long.valueOf(this.quotaGb)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        UserCustomQuotaArg userCustomQuotaArg = (UserCustomQuotaArg) obj;
        UserSelectorArg userSelectorArg = this.user;
        UserSelectorArg userSelectorArg2 = userCustomQuotaArg.user;
        if ((userSelectorArg != userSelectorArg2 && !userSelectorArg.equals(userSelectorArg2)) || this.quotaGb != userCustomQuotaArg.quotaGb) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
