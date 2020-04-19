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

/* renamed from: com.dropbox.core.v2.teamlog.PaperDocChangeMemberRoleDetails */
public class PaperDocChangeMemberRoleDetails {
    protected final PaperAccessType accessType;
    protected final String eventUuid;

    /* renamed from: com.dropbox.core.v2.teamlog.PaperDocChangeMemberRoleDetails$Serializer */
    static class Serializer extends StructSerializer<PaperDocChangeMemberRoleDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(PaperDocChangeMemberRoleDetails paperDocChangeMemberRoleDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("event_uuid");
            StoneSerializers.string().serialize(paperDocChangeMemberRoleDetails.eventUuid, jsonGenerator);
            jsonGenerator.writeFieldName("access_type");
            Serializer.INSTANCE.serialize(paperDocChangeMemberRoleDetails.accessType, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public PaperDocChangeMemberRoleDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            String str2 = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                PaperAccessType paperAccessType = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("event_uuid".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("access_type".equals(currentName)) {
                        paperAccessType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"event_uuid\" missing.");
                } else if (paperAccessType != null) {
                    PaperDocChangeMemberRoleDetails paperDocChangeMemberRoleDetails = new PaperDocChangeMemberRoleDetails(str2, paperAccessType);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(paperDocChangeMemberRoleDetails, paperDocChangeMemberRoleDetails.toStringMultiline());
                    return paperDocChangeMemberRoleDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"access_type\" missing.");
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

    public PaperDocChangeMemberRoleDetails(String str, PaperAccessType paperAccessType) {
        if (str != null) {
            this.eventUuid = str;
            if (paperAccessType != null) {
                this.accessType = paperAccessType;
                return;
            }
            throw new IllegalArgumentException("Required value for 'accessType' is null");
        }
        throw new IllegalArgumentException("Required value for 'eventUuid' is null");
    }

    public String getEventUuid() {
        return this.eventUuid;
    }

    public PaperAccessType getAccessType() {
        return this.accessType;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.eventUuid, this.accessType});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.teamlog.PaperDocChangeMemberRoleDetails r5 = (com.dropbox.core.p005v2.teamlog.PaperDocChangeMemberRoleDetails) r5
            java.lang.String r2 = r4.eventUuid
            java.lang.String r3 = r5.eventUuid
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.teamlog.PaperAccessType r2 = r4.accessType
            com.dropbox.core.v2.teamlog.PaperAccessType r5 = r5.accessType
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.PaperDocChangeMemberRoleDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
