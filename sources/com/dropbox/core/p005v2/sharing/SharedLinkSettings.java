package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import org.apache.http.cookie.ClientCookie;

/* renamed from: com.dropbox.core.v2.sharing.SharedLinkSettings */
public class SharedLinkSettings {
    protected final Date expires;
    protected final String linkPassword;
    protected final RequestedVisibility requestedVisibility;

    /* renamed from: com.dropbox.core.v2.sharing.SharedLinkSettings$Builder */
    public static class Builder {
        protected Date expires = null;
        protected String linkPassword = null;
        protected RequestedVisibility requestedVisibility = null;

        protected Builder() {
        }

        public Builder withRequestedVisibility(RequestedVisibility requestedVisibility2) {
            this.requestedVisibility = requestedVisibility2;
            return this;
        }

        public Builder withLinkPassword(String str) {
            this.linkPassword = str;
            return this;
        }

        public Builder withExpires(Date date) {
            this.expires = LangUtil.truncateMillis(date);
            return this;
        }

        public SharedLinkSettings build() {
            return new SharedLinkSettings(this.requestedVisibility, this.linkPassword, this.expires);
        }
    }

    /* renamed from: com.dropbox.core.v2.sharing.SharedLinkSettings$Serializer */
    static class Serializer extends StructSerializer<SharedLinkSettings> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedLinkSettings sharedLinkSettings, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (sharedLinkSettings.requestedVisibility != null) {
                jsonGenerator.writeFieldName("requested_visibility");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(sharedLinkSettings.requestedVisibility, jsonGenerator);
            }
            if (sharedLinkSettings.linkPassword != null) {
                jsonGenerator.writeFieldName("link_password");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sharedLinkSettings.linkPassword, jsonGenerator);
            }
            if (sharedLinkSettings.expires != null) {
                jsonGenerator.writeFieldName(ClientCookie.EXPIRES_ATTR);
                StoneSerializers.nullable(StoneSerializers.timestamp()).serialize(sharedLinkSettings.expires, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedLinkSettings deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            RequestedVisibility requestedVisibility = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                Date date = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("requested_visibility".equals(currentName)) {
                        requestedVisibility = (RequestedVisibility) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("link_password".equals(currentName)) {
                        str2 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else if (ClientCookie.EXPIRES_ATTR.equals(currentName)) {
                        date = (Date) StoneSerializers.nullable(StoneSerializers.timestamp()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                SharedLinkSettings sharedLinkSettings = new SharedLinkSettings(requestedVisibility, str2, date);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(sharedLinkSettings, sharedLinkSettings.toStringMultiline());
                return sharedLinkSettings;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public SharedLinkSettings(RequestedVisibility requestedVisibility2, String str, Date date) {
        this.requestedVisibility = requestedVisibility2;
        this.linkPassword = str;
        this.expires = LangUtil.truncateMillis(date);
    }

    public SharedLinkSettings() {
        this(null, null, null);
    }

    public RequestedVisibility getRequestedVisibility() {
        return this.requestedVisibility;
    }

    public String getLinkPassword() {
        return this.linkPassword;
    }

    public Date getExpires() {
        return this.expires;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.requestedVisibility, this.linkPassword, this.expires});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0040, code lost:
        if (r2.equals(r5) == false) goto L_0x0043;
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
            if (r2 == 0) goto L_0x0045
            com.dropbox.core.v2.sharing.SharedLinkSettings r5 = (com.dropbox.core.p005v2.sharing.SharedLinkSettings) r5
            com.dropbox.core.v2.sharing.RequestedVisibility r2 = r4.requestedVisibility
            com.dropbox.core.v2.sharing.RequestedVisibility r3 = r5.requestedVisibility
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0026:
            java.lang.String r2 = r4.linkPassword
            java.lang.String r3 = r5.linkPassword
            if (r2 == r3) goto L_0x0034
            if (r2 == 0) goto L_0x0043
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0043
        L_0x0034:
            java.util.Date r2 = r4.expires
            java.util.Date r5 = r5.expires
            if (r2 == r5) goto L_0x0044
            if (r2 == 0) goto L_0x0043
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r0 = 0
        L_0x0044:
            return r0
        L_0x0045:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.sharing.SharedLinkSettings.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
