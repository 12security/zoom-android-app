package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.p005v2.sharing.AccessLevel;
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

/* renamed from: com.dropbox.core.v2.teamlog.SharedContentViewDetails */
public class SharedContentViewDetails {
    protected final AccessLevel sharedContentAccessLevel;
    protected final String sharedContentLink;
    protected final UserLogInfo sharedContentOwner;

    /* renamed from: com.dropbox.core.v2.teamlog.SharedContentViewDetails$Serializer */
    static class Serializer extends StructSerializer<SharedContentViewDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SharedContentViewDetails sharedContentViewDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("shared_content_link");
            StoneSerializers.string().serialize(sharedContentViewDetails.sharedContentLink, jsonGenerator);
            jsonGenerator.writeFieldName("shared_content_access_level");
            com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.serialize(sharedContentViewDetails.sharedContentAccessLevel, jsonGenerator);
            if (sharedContentViewDetails.sharedContentOwner != null) {
                jsonGenerator.writeFieldName("shared_content_owner");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(sharedContentViewDetails.sharedContentOwner, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SharedContentViewDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AccessLevel accessLevel = null;
                UserLogInfo userLogInfo = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("shared_content_link".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("shared_content_access_level".equals(currentName)) {
                        accessLevel = com.dropbox.core.p005v2.sharing.AccessLevel.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("shared_content_owner".equals(currentName)) {
                        userLogInfo = (UserLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"shared_content_link\" missing.");
                } else if (accessLevel != null) {
                    SharedContentViewDetails sharedContentViewDetails = new SharedContentViewDetails(str2, accessLevel, userLogInfo);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sharedContentViewDetails, sharedContentViewDetails.toStringMultiline());
                    return sharedContentViewDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"shared_content_access_level\" missing.");
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

    public SharedContentViewDetails(String str, AccessLevel accessLevel, UserLogInfo userLogInfo) {
        if (str != null) {
            this.sharedContentLink = str;
            this.sharedContentOwner = userLogInfo;
            if (accessLevel != null) {
                this.sharedContentAccessLevel = accessLevel;
                return;
            }
            throw new IllegalArgumentException("Required value for 'sharedContentAccessLevel' is null");
        }
        throw new IllegalArgumentException("Required value for 'sharedContentLink' is null");
    }

    public SharedContentViewDetails(String str, AccessLevel accessLevel) {
        this(str, accessLevel, null);
    }

    public String getSharedContentLink() {
        return this.sharedContentLink;
    }

    public AccessLevel getSharedContentAccessLevel() {
        return this.sharedContentAccessLevel;
    }

    public UserLogInfo getSharedContentOwner() {
        return this.sharedContentOwner;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.sharedContentLink, this.sharedContentOwner, this.sharedContentAccessLevel});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        if (r2.equals(r5) == false) goto L_0x003f;
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
            if (r2 == 0) goto L_0x0041
            com.dropbox.core.v2.teamlog.SharedContentViewDetails r5 = (com.dropbox.core.p005v2.teamlog.SharedContentViewDetails) r5
            java.lang.String r2 = r4.sharedContentLink
            java.lang.String r3 = r5.sharedContentLink
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0024:
            com.dropbox.core.v2.sharing.AccessLevel r2 = r4.sharedContentAccessLevel
            com.dropbox.core.v2.sharing.AccessLevel r3 = r5.sharedContentAccessLevel
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0030:
            com.dropbox.core.v2.teamlog.UserLogInfo r2 = r4.sharedContentOwner
            com.dropbox.core.v2.teamlog.UserLogInfo r5 = r5.sharedContentOwner
            if (r2 == r5) goto L_0x0040
            if (r2 == 0) goto L_0x003f
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r0 = 0
        L_0x0040:
            return r0
        L_0x0041:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SharedContentViewDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
