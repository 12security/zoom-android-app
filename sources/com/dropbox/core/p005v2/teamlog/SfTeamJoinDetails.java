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

/* renamed from: com.dropbox.core.v2.teamlog.SfTeamJoinDetails */
public class SfTeamJoinDetails {
    protected final String originalFolderName;
    protected final long targetAssetIndex;

    /* renamed from: com.dropbox.core.v2.teamlog.SfTeamJoinDetails$Serializer */
    static class Serializer extends StructSerializer<SfTeamJoinDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SfTeamJoinDetails sfTeamJoinDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("target_asset_index");
            StoneSerializers.uInt64().serialize(Long.valueOf(sfTeamJoinDetails.targetAssetIndex), jsonGenerator);
            jsonGenerator.writeFieldName("original_folder_name");
            StoneSerializers.string().serialize(sfTeamJoinDetails.originalFolderName, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SfTeamJoinDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            Long l = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                String str2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("target_asset_index".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("original_folder_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"target_asset_index\" missing.");
                } else if (str2 != null) {
                    SfTeamJoinDetails sfTeamJoinDetails = new SfTeamJoinDetails(l.longValue(), str2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sfTeamJoinDetails, sfTeamJoinDetails.toStringMultiline());
                    return sfTeamJoinDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"original_folder_name\" missing.");
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

    public SfTeamJoinDetails(long j, String str) {
        this.targetAssetIndex = j;
        if (str != null) {
            this.originalFolderName = str;
            return;
        }
        throw new IllegalArgumentException("Required value for 'originalFolderName' is null");
    }

    public long getTargetAssetIndex() {
        return this.targetAssetIndex;
    }

    public String getOriginalFolderName() {
        return this.originalFolderName;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.targetAssetIndex), this.originalFolderName});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        if (r2.equals(r7) == false) goto L_0x002d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r7) {
        /*
            r6 = this;
            r0 = 1
            if (r7 != r6) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r7 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r7.getClass()
            java.lang.Class r3 = r6.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x002f
            com.dropbox.core.v2.teamlog.SfTeamJoinDetails r7 = (com.dropbox.core.p005v2.teamlog.SfTeamJoinDetails) r7
            long r2 = r6.targetAssetIndex
            long r4 = r7.targetAssetIndex
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x002d
            java.lang.String r2 = r6.originalFolderName
            java.lang.String r7 = r7.originalFolderName
            if (r2 == r7) goto L_0x002e
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x002d
            goto L_0x002e
        L_0x002d:
            r0 = 0
        L_0x002e:
            return r0
        L_0x002f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SfTeamJoinDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
