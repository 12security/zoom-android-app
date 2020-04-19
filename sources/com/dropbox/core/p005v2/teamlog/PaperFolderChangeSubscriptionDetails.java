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

/* renamed from: com.dropbox.core.v2.teamlog.PaperFolderChangeSubscriptionDetails */
public class PaperFolderChangeSubscriptionDetails {
    protected final String eventUuid;
    protected final String newSubscriptionLevel;
    protected final String previousSubscriptionLevel;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperFolderChangeSubscriptionDetails$Serializer */
    static class Serializer extends StructSerializer<PaperFolderChangeSubscriptionDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperFolderChangeSubscriptionDetails paperFolderChangeSubscriptionDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(paperFolderChangeSubscriptionDetails.eventUuid, jsonGenerator);
            jsonGenerator.writeFieldName("new_subscription_level");
            StoneSerializers.string().serialize(paperFolderChangeSubscriptionDetails.newSubscriptionLevel, jsonGenerator);
            if (paperFolderChangeSubscriptionDetails.previousSubscriptionLevel != null) {
                jsonGenerator.writeFieldName("previous_subscription_level");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(paperFolderChangeSubscriptionDetails.previousSubscriptionLevel, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperFolderChangeSubscriptionDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                    } else if ("new_subscription_level".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("previous_subscription_level".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"event_uuid\" missing.");
                } else if (str3 != null) {
                    PaperFolderChangeSubscriptionDetails paperFolderChangeSubscriptionDetails = new PaperFolderChangeSubscriptionDetails(str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperFolderChangeSubscriptionDetails, paperFolderChangeSubscriptionDetails.toStringMultiline());
                    return paperFolderChangeSubscriptionDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"new_subscription_level\" missing.");
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

    public PaperFolderChangeSubscriptionDetails(String str, String str2, String str3) {
        if (str != null) {
            this.eventUuid = str;
            if (str2 != null) {
                this.newSubscriptionLevel = str2;
                this.previousSubscriptionLevel = str3;
                return;
            }
            throw new IllegalArgumentException("Required value for 'newSubscriptionLevel' is null");
        }
        throw new IllegalArgumentException("Required value for 'eventUuid' is null");
    }

    public PaperFolderChangeSubscriptionDetails(String str, String str2) {
        this(str, str2, null);
    }

    public String getEventUuid() {
        return this.eventUuid;
    }

    public String getNewSubscriptionLevel() {
        return this.newSubscriptionLevel;
    }

    public String getPreviousSubscriptionLevel() {
        return this.previousSubscriptionLevel;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.eventUuid, this.newSubscriptionLevel, this.previousSubscriptionLevel});
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
            com.dropbox.core.v2.teamlog.PaperFolderChangeSubscriptionDetails r5 = (com.dropbox.core.p005v2.teamlog.PaperFolderChangeSubscriptionDetails) r5
            java.lang.String r2 = r4.eventUuid
            java.lang.String r3 = r5.eventUuid
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0024:
            java.lang.String r2 = r4.newSubscriptionLevel
            java.lang.String r3 = r5.newSubscriptionLevel
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x003f
        L_0x0030:
            java.lang.String r2 = r4.previousSubscriptionLevel
            java.lang.String r5 = r5.previousSubscriptionLevel
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
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.PaperFolderChangeSubscriptionDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
