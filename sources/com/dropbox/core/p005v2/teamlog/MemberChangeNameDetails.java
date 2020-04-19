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

/* renamed from: com.dropbox.core.v2.teamlog.MemberChangeNameDetails */
public class MemberChangeNameDetails {
    protected final UserNameLogInfo newValue;
    protected final UserNameLogInfo previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberChangeNameDetails$Serializer */
    static class Serializer extends StructSerializer<MemberChangeNameDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberChangeNameDetails memberChangeNameDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(memberChangeNameDetails.newValue, jsonGenerator);
            if (memberChangeNameDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(memberChangeNameDetails.previousValue, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberChangeNameDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            UserNameLogInfo userNameLogInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                UserNameLogInfo userNameLogInfo2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        userNameLogInfo = (UserNameLogInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        userNameLogInfo2 = (UserNameLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (userNameLogInfo != null) {
                    MemberChangeNameDetails memberChangeNameDetails = new MemberChangeNameDetails(userNameLogInfo, userNameLogInfo2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberChangeNameDetails, memberChangeNameDetails.toStringMultiline());
                    return memberChangeNameDetails;
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

    public MemberChangeNameDetails(UserNameLogInfo userNameLogInfo, UserNameLogInfo userNameLogInfo2) {
        if (userNameLogInfo != null) {
            this.newValue = userNameLogInfo;
            this.previousValue = userNameLogInfo2;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public MemberChangeNameDetails(UserNameLogInfo userNameLogInfo) {
        this(userNameLogInfo, null);
    }

    public UserNameLogInfo getNewValue() {
        return this.newValue;
    }

    public UserNameLogInfo getPreviousValue() {
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
            com.dropbox.core.v2.teamlog.MemberChangeNameDetails r5 = (com.dropbox.core.p005v2.teamlog.MemberChangeNameDetails) r5
            com.dropbox.core.v2.teamlog.UserNameLogInfo r2 = r4.newValue
            com.dropbox.core.v2.teamlog.UserNameLogInfo r3 = r5.newValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0033
        L_0x0024:
            com.dropbox.core.v2.teamlog.UserNameLogInfo r2 = r4.previousValue
            com.dropbox.core.v2.teamlog.UserNameLogInfo r5 = r5.previousValue
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.MemberChangeNameDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
