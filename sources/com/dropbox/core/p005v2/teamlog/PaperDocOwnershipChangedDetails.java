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

/* renamed from: com.dropbox.core.v2.teamlog.PaperDocOwnershipChangedDetails */
public class PaperDocOwnershipChangedDetails {
    protected final String eventUuid;
    protected final String newOwnerUserId;
    protected final String oldOwnerUserId;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperDocOwnershipChangedDetails$Serializer */
    static class Serializer extends StructSerializer<PaperDocOwnershipChangedDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocOwnershipChangedDetails paperDocOwnershipChangedDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(paperDocOwnershipChangedDetails.eventUuid, jsonGenerator);
            jsonGenerator.writeFieldName("new_owner_user_id");
            StoneSerializers.string().serialize(paperDocOwnershipChangedDetails.newOwnerUserId, jsonGenerator);
            if (paperDocOwnershipChangedDetails.oldOwnerUserId != null) {
                jsonGenerator.writeFieldName("old_owner_user_id");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(paperDocOwnershipChangedDetails.oldOwnerUserId, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocOwnershipChangedDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    if ("event_uuid".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("new_owner_user_id".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("old_owner_user_id".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"event_uuid\" missing.");
                } else if (str3 != null) {
                    PaperDocOwnershipChangedDetails paperDocOwnershipChangedDetails = new PaperDocOwnershipChangedDetails(str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocOwnershipChangedDetails, paperDocOwnershipChangedDetails.toStringMultiline());
                    return paperDocOwnershipChangedDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"new_owner_user_id\" missing.");
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

    public PaperDocOwnershipChangedDetails(String str, String str2, String str3) {
        if (str != null) {
            this.eventUuid = str;
            if (str3 != null) {
                if (str3.length() < 40) {
                    throw new IllegalArgumentException("String 'oldOwnerUserId' is shorter than 40");
                } else if (str3.length() > 40) {
                    throw new IllegalArgumentException("String 'oldOwnerUserId' is longer than 40");
                }
            }
            this.oldOwnerUserId = str3;
            if (str2 == null) {
                throw new IllegalArgumentException("Required value for 'newOwnerUserId' is null");
            } else if (str2.length() < 40) {
                throw new IllegalArgumentException("String 'newOwnerUserId' is shorter than 40");
            } else if (str2.length() <= 40) {
                this.newOwnerUserId = str2;
            } else {
                throw new IllegalArgumentException("String 'newOwnerUserId' is longer than 40");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'eventUuid' is null");
        }
    }

    public PaperDocOwnershipChangedDetails(String str, String str2) {
        this(str, str2, null);
    }

    public String getEventUuid() {
        return this.eventUuid;
    }

    public String getNewOwnerUserId() {
        return this.newOwnerUserId;
    }

    public String getOldOwnerUserId() {
        return this.oldOwnerUserId;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.eventUuid, this.oldOwnerUserId, this.newOwnerUserId});
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
            com.dropbox.core.v2.teamlog.PaperDocOwnershipChangedDetails r5 = (com.dropbox.core.p005v2.teamlog.PaperDocOwnershipChangedDetails) r5
            java.lang.String r2 = r4.eventUuid
            java.lang.String r3 = r5.eventUuid
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0024:
            java.lang.String r2 = r4.newOwnerUserId
            java.lang.String r3 = r5.newOwnerUserId
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0030:
            java.lang.String r2 = r4.oldOwnerUserId
            java.lang.String r5 = r5.oldOwnerUserId
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.PaperDocOwnershipChangedDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
