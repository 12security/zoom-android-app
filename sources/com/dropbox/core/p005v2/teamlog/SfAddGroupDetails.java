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

/* renamed from: com.dropbox.core.v2.teamlog.SfAddGroupDetails */
public class SfAddGroupDetails {
    protected final String originalFolderName;
    protected final String sharingPermission;
    protected final long targetAssetIndex;
    protected final String teamName;

    /* renamed from: com.dropbox.core.v2.teamlog.SfAddGroupDetails$Serializer */
    static class Serializer extends StructSerializer<SfAddGroupDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(SfAddGroupDetails sfAddGroupDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("target_asset_index");
            StoneSerializers.uInt64().serialize(Long.valueOf(sfAddGroupDetails.targetAssetIndex), jsonGenerator);
            jsonGenerator.writeFieldName("original_folder_name");
            StoneSerializers.string().serialize(sfAddGroupDetails.originalFolderName, jsonGenerator);
            jsonGenerator.writeFieldName("team_name");
            StoneSerializers.string().serialize(sfAddGroupDetails.teamName, jsonGenerator);
            if (sfAddGroupDetails.sharingPermission != null) {
                jsonGenerator.writeFieldName("sharing_permission");
                StoneSerializers.nullable(StoneSerializers.string()).serialize(sfAddGroupDetails.sharingPermission, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public SfAddGroupDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
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
                String str3 = null;
                String str4 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("target_asset_index".equals(currentName)) {
                        l = (Long) StoneSerializers.uInt64().deserialize(jsonParser);
                    } else if ("original_folder_name".equals(currentName)) {
                        str2 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("team_name".equals(currentName)) {
                        str3 = (String) StoneSerializers.string().deserialize(jsonParser);
                    } else if ("sharing_permission".equals(currentName)) {
                        str4 = (String) StoneSerializers.nullable(StoneSerializers.string()).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (l == null) {
                    throw new JsonParseException(jsonParser, "Required field \"target_asset_index\" missing.");
                } else if (str2 == null) {
                    throw new JsonParseException(jsonParser, "Required field \"original_folder_name\" missing.");
                } else if (str3 != null) {
                    SfAddGroupDetails sfAddGroupDetails = new SfAddGroupDetails(l.longValue(), str2, str3, str4);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(sfAddGroupDetails, sfAddGroupDetails.toStringMultiline());
                    return sfAddGroupDetails;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"team_name\" missing.");
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

    public SfAddGroupDetails(long j, String str, String str2, String str3) {
        this.targetAssetIndex = j;
        if (str != null) {
            this.originalFolderName = str;
            this.sharingPermission = str3;
            if (str2 != null) {
                this.teamName = str2;
                return;
            }
            throw new IllegalArgumentException("Required value for 'teamName' is null");
        }
        throw new IllegalArgumentException("Required value for 'originalFolderName' is null");
    }

    public SfAddGroupDetails(long j, String str, String str2) {
        this(j, str, str2, null);
    }

    public long getTargetAssetIndex() {
        return this.targetAssetIndex;
    }

    public String getOriginalFolderName() {
        return this.originalFolderName;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public String getSharingPermission() {
        return this.sharingPermission;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Long.valueOf(this.targetAssetIndex), this.originalFolderName, this.sharingPermission, this.teamName});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0044, code lost:
        if (r2.equals(r7) == false) goto L_0x0047;
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
            if (r2 == 0) goto L_0x0049
            com.dropbox.core.v2.teamlog.SfAddGroupDetails r7 = (com.dropbox.core.p005v2.teamlog.SfAddGroupDetails) r7
            long r2 = r6.targetAssetIndex
            long r4 = r7.targetAssetIndex
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x0047
            java.lang.String r2 = r6.originalFolderName
            java.lang.String r3 = r7.originalFolderName
            if (r2 == r3) goto L_0x002c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0047
        L_0x002c:
            java.lang.String r2 = r6.teamName
            java.lang.String r3 = r7.teamName
            if (r2 == r3) goto L_0x0038
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0047
        L_0x0038:
            java.lang.String r2 = r6.sharingPermission
            java.lang.String r7 = r7.sharingPermission
            if (r2 == r7) goto L_0x0048
            if (r2 == 0) goto L_0x0047
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x0047
            goto L_0x0048
        L_0x0047:
            r0 = 0
        L_0x0048:
            return r0
        L_0x0049:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.SfAddGroupDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
